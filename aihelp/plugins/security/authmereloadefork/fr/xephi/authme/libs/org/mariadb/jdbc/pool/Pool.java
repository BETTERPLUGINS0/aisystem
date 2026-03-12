package fr.xephi.authme.libs.org.mariadb.jdbc.pool;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.Connection;
import fr.xephi.authme.libs.org.mariadb.jdbc.Driver;
import fr.xephi.authme.libs.org.mariadb.jdbc.MariaDbPoolConnection;
import fr.xephi.authme.libs.org.mariadb.jdbc.Statement;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Logger;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Loggers;
import java.lang.management.ManagementFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;

public class Pool implements AutoCloseable, PoolMBean {
   private static final Logger logger = Loggers.getLogger(Pool.class);
   private static final int POOL_STATE_OK = 0;
   private static final int POOL_STATE_CLOSING = 1;
   private final AtomicInteger poolState = new AtomicInteger();
   private final Configuration conf;
   private final AtomicInteger pendingRequestNumber = new AtomicInteger();
   private final AtomicInteger totalConnection = new AtomicInteger();
   private final LinkedBlockingDeque<MariaDbInnerPoolConnection> idleConnections;
   private final ThreadPoolExecutor connectionAppender;
   private final BlockingQueue<Runnable> connectionAppenderQueue;
   private final String poolTag;
   private final ScheduledThreadPoolExecutor poolExecutor;
   private final ScheduledFuture<?> scheduledFuture;
   private int waitTimeout;

   public Pool(Configuration conf, int poolIndex, ScheduledThreadPoolExecutor poolExecutor) {
      this.conf = conf;
      this.poolTag = this.generatePoolTag(poolIndex);
      this.connectionAppenderQueue = new ArrayBlockingQueue(conf.maxPoolSize());
      this.connectionAppender = new ThreadPoolExecutor(1, 1, 10L, TimeUnit.SECONDS, this.connectionAppenderQueue, new PoolThreadFactory(this.poolTag + "-appender"));
      this.connectionAppender.allowCoreThreadTimeOut(true);
      this.connectionAppender.prestartCoreThread();
      this.idleConnections = new LinkedBlockingDeque();
      int minDelay = Integer.parseInt(conf.nonMappedOptions().getProperty("testMinRemovalDelay", "30"));
      int scheduleDelay = Math.min(minDelay, conf.maxIdleTime() / 2);
      this.poolExecutor = poolExecutor;
      this.scheduledFuture = poolExecutor.scheduleAtFixedRate(this::removeIdleTimeoutConnection, (long)scheduleDelay, (long)scheduleDelay, TimeUnit.SECONDS);
      if (conf.registerJmxPool()) {
         try {
            this.registerJmx();
         } catch (Exception var10) {
            logger.error("pool " + this.poolTag + " not registered due to exception : " + var10.getMessage());
         }
      }

      try {
         for(int i = 0; i < Math.max(1, conf.minPoolSize()); ++i) {
            this.addConnection();
         }

         this.waitTimeout = 28800;
         if (!this.idleConnections.isEmpty()) {
            Statement stmt = ((MariaDbInnerPoolConnection)this.idleConnections.getFirst()).getConnection().createStatement();

            try {
               ResultSet rs = stmt.executeQuery("SELECT @@wait_timeout");
               if (rs.next()) {
                  this.waitTimeout = rs.getInt(1);
               }
            } catch (Throwable var11) {
               if (stmt != null) {
                  try {
                     stmt.close();
                  } catch (Throwable var9) {
                     var11.addSuppressed(var9);
                  }
               }

               throw var11;
            }

            if (stmt != null) {
               stmt.close();
            }
         }
      } catch (SQLException var12) {
         logger.error("error initializing pool connection", (Throwable)var12);
      }

   }

   private void addConnectionRequest() {
      if (this.totalConnection.get() < this.conf.maxPoolSize() && this.poolState.get() == 0) {
         this.connectionAppender.prestartCoreThread();
         this.connectionAppenderQueue.offer(() -> {
            if ((this.totalConnection.get() < this.conf.minPoolSize() || this.pendingRequestNumber.get() > 0) && this.totalConnection.get() < this.conf.maxPoolSize()) {
               try {
                  this.addConnection();
               } catch (SQLException var2) {
                  logger.error("error adding connection to pool", (Throwable)var2);
               }
            }

         });
      }

   }

   private void removeIdleTimeoutConnection() {
      Iterator iterator = this.idleConnections.descendingIterator();

      while(iterator.hasNext()) {
         MariaDbInnerPoolConnection item = (MariaDbInnerPoolConnection)iterator.next();
         long idleTime = System.nanoTime() - item.getLastUsed().get();
         boolean timedOut = idleTime > TimeUnit.SECONDS.toNanos((long)this.conf.maxIdleTime());
         boolean shouldBeReleased = false;
         Connection con = item.getConnection();
         if (this.waitTimeout > 0) {
            if (idleTime > TimeUnit.SECONDS.toNanos((long)(this.waitTimeout - 45))) {
               shouldBeReleased = true;
            }

            if (timedOut && this.totalConnection.get() > this.conf.minPoolSize()) {
               shouldBeReleased = true;
            }
         } else if (timedOut) {
            shouldBeReleased = true;
         }

         if (shouldBeReleased && this.idleConnections.remove(item)) {
            this.totalConnection.decrementAndGet();
            this.silentCloseConnection(con);
            this.addConnectionRequest();
            if (logger.isDebugEnabled()) {
               logger.debug("pool {} connection {} removed due to inactivity (total:{}, active:{}, pending:{})", this.poolTag, con.getThreadId(), this.totalConnection.get(), this.getActiveConnections(), this.pendingRequestNumber.get());
            }
         }
      }

   }

   private void addConnection() throws SQLException {
      Connection connection = Driver.connect(this.conf);
      MariaDbInnerPoolConnection item = new MariaDbInnerPoolConnection(connection);
      item.addConnectionEventListener(new ConnectionEventListener() {
         public void connectionClosed(ConnectionEvent event) {
            MariaDbInnerPoolConnection item = (MariaDbInnerPoolConnection)event.getSource();
            if (Pool.this.poolState.get() == 0) {
               try {
                  if (!Pool.this.idleConnections.contains(item)) {
                     item.getConnection().setPoolConnection((MariaDbPoolConnection)null);
                     item.getConnection().reset();
                     Pool.this.idleConnections.addFirst(item);
                     item.getConnection().setPoolConnection(item);
                  }
               } catch (SQLException var5) {
                  Pool.this.totalConnection.decrementAndGet();
                  Pool.this.silentCloseConnection(item.getConnection());
                  Pool.logger.debug("connection {} removed from pool {} due to error during reset (total:{}, active:{}, pending:{})", item.getConnection().getThreadId(), Pool.this.poolTag, Pool.this.totalConnection.get(), Pool.this.getActiveConnections(), Pool.this.pendingRequestNumber.get());
               }
            } else {
               try {
                  item.getConnection().close();
               } catch (SQLException var4) {
               }

               Pool.this.totalConnection.decrementAndGet();
            }

         }

         public void connectionErrorOccurred(ConnectionEvent event) {
            MariaDbInnerPoolConnection item = (MariaDbInnerPoolConnection)event.getSource();
            Pool.this.totalConnection.decrementAndGet();
            Pool.this.idleConnections.remove(item);
            Pool.this.idleConnections.forEach(MariaDbInnerPoolConnection::ensureValidation);
            Pool.this.silentCloseConnection(item.getConnection());
            Pool.this.addConnectionRequest();
            Pool.logger.debug("connection {} removed from pool {} due to having throw a Connection exception (total:{}, active:{}, pending:{})", item.getConnection().getThreadId(), Pool.this.poolTag, Pool.this.totalConnection.get(), Pool.this.getActiveConnections(), Pool.this.pendingRequestNumber.get());
         }
      });
      if (this.poolState.get() == 0 && this.totalConnection.incrementAndGet() <= this.conf.maxPoolSize()) {
         this.idleConnections.addFirst(item);
         if (logger.isDebugEnabled()) {
            logger.debug("pool {} new physical connection {} created (total:{}, active:{}, pending:{})", this.poolTag, connection.getThreadId(), this.totalConnection.get(), this.getActiveConnections(), this.pendingRequestNumber.get());
         }

      } else {
         this.silentCloseConnection(connection);
      }
   }

   private MariaDbInnerPoolConnection getIdleConnection(long timeout, TimeUnit timeUnit) throws InterruptedException {
      while(true) {
         MariaDbInnerPoolConnection item = timeout == 0L ? (MariaDbInnerPoolConnection)this.idleConnections.pollFirst() : (MariaDbInnerPoolConnection)this.idleConnections.pollFirst(timeout, timeUnit);
         if (item == null) {
            return null;
         }

         try {
            if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - item.getLastUsed().get()) <= (long)this.conf.poolValidMinDelay()) {
               item.lastUsedToNow();
               return item;
            }

            if (item.getConnection().isValid(10)) {
               item.lastUsedToNow();
               return item;
            }
         } catch (SQLException var6) {
         }

         this.silentAbortConnection(item.getConnection());
         this.addConnectionRequest();
         if (logger.isDebugEnabled()) {
            logger.debug("pool {} connection {} removed from pool due to failed validation (total:{}, active:{}, pending:{})", this.poolTag, item.getConnection().getThreadId(), this.totalConnection.get(), this.getActiveConnections(), this.pendingRequestNumber.get());
         }
      }
   }

   private void silentCloseConnection(Connection con) {
      con.setPoolConnection((MariaDbPoolConnection)null);

      try {
         con.close();
      } catch (SQLException var3) {
      }

   }

   private void silentAbortConnection(Connection con) {
      con.setPoolConnection((MariaDbPoolConnection)null);

      try {
         con.abort(this.poolExecutor);
      } catch (SQLException var3) {
      }

   }

   public MariaDbInnerPoolConnection getPoolConnection() throws SQLException {
      this.pendingRequestNumber.incrementAndGet();

      MariaDbInnerPoolConnection var2;
      try {
         MariaDbInnerPoolConnection poolConnection;
         if ((poolConnection = this.getIdleConnection(this.totalConnection.get() > 4 ? 0L : 50L, TimeUnit.MICROSECONDS)) == null) {
            this.addConnectionRequest();
            if ((poolConnection = this.getIdleConnection(TimeUnit.MILLISECONDS.toNanos((long)this.conf.connectTimeout()), TimeUnit.NANOSECONDS)) != null) {
               var2 = poolConnection;
               return var2;
            }

            throw new SQLException(String.format("No connection available within the specified time (option 'connectTimeout': %s ms)", NumberFormat.getInstance().format((long)this.conf.connectTimeout())));
         }

         var2 = poolConnection;
      } catch (InterruptedException var6) {
         throw new SQLException("Thread was interrupted", "70100", var6);
      } finally {
         this.pendingRequestNumber.decrementAndGet();
      }

      return var2;
   }

   public MariaDbInnerPoolConnection getPoolConnection(String username, String password) throws SQLException {
      if (username == null) {
         if (this.conf.user() == null) {
            return this.getPoolConnection();
         }
      } else if (username.equals(this.conf.user()) && (password == null || password.isEmpty())) {
         if (this.conf.password() == null) {
            return this.getPoolConnection();
         }
      } else if (password.equals(this.conf.password())) {
         return this.getPoolConnection();
      }

      Configuration tmpConf = this.conf.clone(username, password);
      return new MariaDbInnerPoolConnection(Driver.connect(tmpConf));
   }

   private String generatePoolTag(int poolIndex) {
      return this.conf.poolName() == null ? "MariaDB-pool" : this.conf.poolName() + "-" + poolIndex;
   }

   public Configuration getConf() {
      return this.conf;
   }

   public void close() {
      try {
         synchronized(this) {
            Pools.remove(this);
            this.poolState.set(1);
            this.pendingRequestNumber.set(0);
            this.scheduledFuture.cancel(false);
            this.connectionAppender.shutdown();

            try {
               this.connectionAppender.awaitTermination(10L, TimeUnit.SECONDS);
            } catch (InterruptedException var8) {
            }

            if (logger.isInfoEnabled()) {
               logger.debug("closing pool {} (total:{}, active:{}, pending:{})", this.poolTag, this.totalConnection.get(), this.getActiveConnections(), this.pendingRequestNumber.get());
            }

            ExecutorService connectionRemover = new ThreadPoolExecutor(this.totalConnection.get(), this.conf.maxPoolSize(), 10L, TimeUnit.SECONDS, new LinkedBlockingQueue(this.conf.maxPoolSize()), new PoolThreadFactory(this.poolTag + "-destroyer"));
            long start = System.nanoTime();

            do {
               this.closeAll(this.idleConnections);
               if (this.totalConnection.get() > 0) {
                  Thread.sleep(0L, 1000);
               }
            } while(this.totalConnection.get() > 0 && TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start) < 10L);

            if (this.totalConnection.get() > 0 || this.idleConnections.isEmpty()) {
               this.closeAll(this.idleConnections);
            }

            connectionRemover.shutdown();

            try {
               this.unRegisterJmx();
            } catch (Exception var7) {
            }

            connectionRemover.awaitTermination(10L, TimeUnit.SECONDS);
         }
      } catch (Exception var10) {
      }

   }

   private void closeAll(Collection<MariaDbInnerPoolConnection> collection) {
      synchronized(collection) {
         Iterator var3 = collection.iterator();

         while(var3.hasNext()) {
            MariaDbInnerPoolConnection item = (MariaDbInnerPoolConnection)var3.next();
            collection.remove(item);
            this.totalConnection.decrementAndGet();
            this.silentAbortConnection(item.getConnection());
         }

      }
   }

   public String getPoolTag() {
      return this.poolTag;
   }

   public long getActiveConnections() {
      return (long)(this.totalConnection.get() - this.idleConnections.size());
   }

   public long getTotalConnections() {
      return (long)this.totalConnection.get();
   }

   public long getIdleConnections() {
      return (long)this.idleConnections.size();
   }

   public long getConnectionRequests() {
      return (long)this.pendingRequestNumber.get();
   }

   private void registerJmx() throws Exception {
      MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
      String jmxName = this.poolTag.replace(":", "_");
      ObjectName name = new ObjectName("fr.xephi.authme.libs.org.mariadb.jdbc.pool:type=" + jmxName);
      if (!mbs.isRegistered(name)) {
         mbs.registerMBean(this, name);
      }

   }

   private void unRegisterJmx() throws Exception {
      MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
      String jmxName = this.poolTag.replace(":", "_");
      ObjectName name = new ObjectName("fr.xephi.authme.libs.org.mariadb.jdbc.pool:type=" + jmxName);
      if (mbs.isRegistered(name)) {
         mbs.unregisterMBean(name);
      }

   }

   public List<Long> testGetConnectionIdleThreadIds() {
      List<Long> threadIds = new ArrayList();
      Iterator var2 = this.idleConnections.iterator();

      while(var2.hasNext()) {
         MariaDbInnerPoolConnection pooledConnection = (MariaDbInnerPoolConnection)var2.next();
         threadIds.add(pooledConnection.getConnection().getThreadId());
      }

      return threadIds;
   }
}
