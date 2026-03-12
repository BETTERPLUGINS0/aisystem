package fr.xephi.authme.libs.org.mariadb.jdbc.client.impl;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.HostAddress;
import fr.xephi.authme.libs.org.mariadb.jdbc.Statement;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Client;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Completion;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.context.RedoContext;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.ExceptionFactory;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.Prepare;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.client.ChangeDbPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.client.QueryPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.client.RedoableWithPrepareClientMessage;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Logger;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Loggers;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLTransientConnectionException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class MultiPrimaryClient implements Client {
   protected static final ConcurrentMap<HostAddress, Long> denyList = new ConcurrentHashMap();
   private static final Logger logger = Loggers.getLogger(MultiPrimaryClient.class);
   protected final long deniedListTimeout;
   protected final Configuration conf;
   protected final ReentrantLock lock;
   protected boolean closed = false;
   protected Client currentClient;

   public MultiPrimaryClient(Configuration conf, ReentrantLock lock) throws SQLException {
      this.conf = conf;
      this.lock = lock;
      this.deniedListTimeout = Long.parseLong(conf.nonMappedOptions().getProperty("deniedListTimeout", "60000"));
      this.currentClient = this.connectHost(false, false);
   }

   protected Client connectHost(boolean readOnly, boolean failFast) throws SQLException {
      SQLNonTransientConnectionException lastSqle = null;
      int maxRetries = this.conf.retriesAllDown();

      Optional host;
      while((host = this.conf.haMode().getAvailableHost(this.conf.addresses(), denyList, !readOnly)).isPresent() && maxRetries > 0) {
         try {
            return (Client)(this.conf.transactionReplay() ? new ReplayClient(this.conf, (HostAddress)host.get(), this.lock, false) : new StandardClient(this.conf, (HostAddress)host.get(), this.lock, false));
         } catch (SQLNonTransientConnectionException var10) {
            lastSqle = var10;
            denyList.putIfAbsent((HostAddress)host.get(), System.currentTimeMillis() + this.deniedListTimeout);
            --maxRetries;
         }
      }

      if (failFast) {
         throw lastSqle != null ? lastSqle : new SQLNonTransientConnectionException("all hosts are blacklisted");
      } else if (denyList.entrySet().stream().noneMatch((e) -> {
         return this.conf.addresses().contains(e.getKey()) && ((HostAddress)e.getKey()).primary != readOnly;
      })) {
         throw new SQLNonTransientConnectionException(String.format("No %s host defined", readOnly ? "replica" : "primary"));
      } else {
         while(maxRetries > 0) {
            try {
               host = denyList.entrySet().stream().sorted(Entry.comparingByValue()).filter((e) -> {
                  return this.conf.addresses().contains(e.getKey()) && ((HostAddress)e.getKey()).primary != readOnly;
               }).findFirst().map(Entry::getKey);
               if (host.isPresent()) {
                  Client client = this.conf.transactionReplay() ? new ReplayClient(this.conf, (HostAddress)host.get(), this.lock, false) : new StandardClient(this.conf, (HostAddress)host.get(), this.lock, false);
                  denyList.remove(host.get());
                  return (Client)client;
               }

               --maxRetries;
            } catch (SQLNonTransientConnectionException var9) {
               lastSqle = var9;
               host.ifPresent((hostAddress) -> {
                  denyList.putIfAbsent(hostAddress, System.currentTimeMillis() + this.deniedListTimeout);
               });
               --maxRetries;
               if (maxRetries > 0) {
                  try {
                     Thread.sleep(250L);
                  } catch (InterruptedException var8) {
                  }
               }
            }
         }

         throw lastSqle != null ? lastSqle : new SQLNonTransientConnectionException("No host");
      }
   }

   protected Client reConnect() throws SQLException {
      denyList.putIfAbsent(this.currentClient.getHostAddress(), System.currentTimeMillis() + this.deniedListTimeout);
      logger.info("Connection error on {}", this.currentClient.getHostAddress());

      try {
         Client oldClient = this.currentClient;
         oldClient.getContext().resetPrepareCache();
         this.currentClient = this.connectHost(false, false);
         this.syncNewState(oldClient);
         return oldClient;
      } catch (SQLNonTransientConnectionException var2) {
         this.currentClient = null;
         this.closed = true;
         throw var2;
      }
   }

   protected void replayIfPossible(Client oldClient, boolean canRedo) throws SQLException {
      if (oldClient != null) {
         if ((oldClient.getContext().getServerStatus() & 1) > 0) {
            if (!this.conf.transactionReplay()) {
               throw new SQLTransientConnectionException(String.format("Driver has reconnect connection after a communications link failure with %s. In progress transaction was lost", oldClient.getHostAddress()), "25S03");
            }

            this.executeTransactionReplay(oldClient);
         } else if (!canRedo) {
            throw new SQLTransientConnectionException(String.format("Driver has reconnect connection after a communications link failure with %s", oldClient.getHostAddress()), "25S03");
         }
      }

   }

   protected void executeTransactionReplay(Client oldCli) throws SQLException {
      RedoContext ctx = (RedoContext)oldCli.getContext();
      if (ctx.getTransactionSaver().isDirty()) {
         ctx.getTransactionSaver().clear();
         throw new SQLTransientConnectionException(String.format("Driver has reconnect connection after a communications link failure with %s. In progress transaction was too big to be replayed, and was lost", oldCli.getHostAddress()), "25S03");
      } else {
         ((ReplayClient)this.currentClient).transactionReplay(ctx.getTransactionSaver());
      }
   }

   public void syncNewState(Client oldCli) throws SQLException {
      Context oldCtx = oldCli.getContext();
      this.currentClient.getExceptionFactory().setConnection(oldCli.getExceptionFactory());
      if ((oldCtx.getStateFlag() & 8) > 0 && (oldCtx.getServerStatus() & 2) != (this.currentClient.getContext().getServerStatus() & 2)) {
         this.currentClient.getContext().addStateFlag(8);
         this.currentClient.execute(new QueryPacket("set autocommit=" + ((oldCtx.getServerStatus() & 2) > 0 ? "1" : "0")), true);
      }

      if ((oldCtx.getStateFlag() & 2) > 0 && !Objects.equals(this.currentClient.getContext().getDatabase(), oldCtx.getDatabase())) {
         this.currentClient.getContext().addStateFlag(2);
         if (oldCtx.getDatabase() != null) {
            this.currentClient.execute(new ChangeDbPacket(oldCtx.getDatabase()), true);
         }

         this.currentClient.getContext().setDatabase(oldCtx.getDatabase());
      }

      if ((oldCtx.getStateFlag() & 1) > 0) {
         this.currentClient.setSocketTimeout(oldCli.getSocketTimeout());
      }

      if ((oldCtx.getStateFlag() & 4) > 0 && !this.currentClient.getHostAddress().primary && this.currentClient.getContext().getVersion().versionGreaterOrEqual(5, 6, 5)) {
         this.currentClient.execute(new QueryPacket("SET SESSION TRANSACTION READ ONLY"), true);
      }

      if ((oldCtx.getStateFlag() & 16) > 0 && !oldCtx.getTransactionIsolationLevel().equals(this.currentClient.getContext().getTransactionIsolationLevel())) {
         String query = "SET SESSION TRANSACTION ISOLATION LEVEL";
         switch(oldCtx.getTransactionIsolationLevel()) {
         case 1:
            query = query + " READ UNCOMMITTED";
            break;
         case 2:
            query = query + " READ COMMITTED";
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            break;
         case 4:
            query = query + " REPEATABLE READ";
            break;
         case 8:
            query = query + " SERIALIZABLE";
         }

         this.currentClient.getContext().setTransactionIsolationLevel(oldCtx.getTransactionIsolationLevel());
         this.currentClient.execute(new QueryPacket(query), true);
      }

   }

   public List<Completion> execute(ClientMessage message, boolean canRedo) throws SQLException {
      return this.execute(message, (Statement)null, 0, 0L, 1007, 1003, false, canRedo);
   }

   public List<Completion> execute(ClientMessage message, Statement stmt, boolean canRedo) throws SQLException {
      return this.execute(message, stmt, 0, 0L, 1007, 1003, false, canRedo);
   }

   public List<Completion> execute(ClientMessage message, Statement stmt, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion, boolean canRedo) throws SQLException {
      if (this.closed) {
         throw new SQLNonTransientConnectionException("Connection is closed", "08000", 1220);
      } else {
         try {
            return this.currentClient.execute(message, stmt, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion, canRedo);
         } catch (SQLNonTransientConnectionException var13) {
            HostAddress hostAddress = this.currentClient.getHostAddress();
            Client oldClient = this.reConnect();
            if (message instanceof QueryPacket && ((QueryPacket)message).isCommit()) {
               throw new SQLTransientConnectionException(String.format("Driver has reconnect connection after a communications failure with %s during a COMMIT statement", hostAddress), "25S03");
            } else {
               this.replayIfPossible(oldClient, canRedo);
               if (message instanceof RedoableWithPrepareClientMessage) {
                  ((RedoableWithPrepareClientMessage)message).rePrepare(this.currentClient);
               }

               return this.currentClient.execute(message, stmt, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion, canRedo);
            }
         }
      }
   }

   public List<Completion> executePipeline(ClientMessage[] messages, Statement stmt, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion, boolean canRedo) throws SQLException {
      if (this.closed) {
         throw new SQLNonTransientConnectionException("Connection is closed", "08000", 1220);
      } else {
         try {
            return this.currentClient.executePipeline(messages, stmt, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion, canRedo);
         } catch (SQLException var12) {
            if (!(var12 instanceof SQLNonTransientConnectionException) && (var12.getCause() == null || !(var12.getCause() instanceof SQLNonTransientConnectionException))) {
               throw var12;
            } else {
               Client oldClient = this.reConnect();
               this.replayIfPossible(oldClient, canRedo);
               Stream var10000 = Arrays.stream(messages);
               Objects.requireNonNull(RedoableWithPrepareClientMessage.class);
               var10000 = var10000.filter(RedoableWithPrepareClientMessage.class::isInstance);
               Objects.requireNonNull(RedoableWithPrepareClientMessage.class);
               var10000.map(RedoableWithPrepareClientMessage.class::cast).forEach((rd) -> {
                  try {
                     rd.rePrepare(this.currentClient);
                  } catch (SQLException var3) {
                  }

               });
               return this.currentClient.executePipeline(messages, stmt, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion, canRedo);
            }
         }
      }
   }

   public void readStreamingResults(List<Completion> completions, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion) throws SQLException {
      if (this.closed) {
         throw new SQLNonTransientConnectionException("Connection is closed", "08000", 1220);
      } else {
         try {
            this.currentClient.readStreamingResults(completions, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion);
         } catch (SQLNonTransientConnectionException var11) {
            try {
               this.reConnect();
            } catch (SQLException var10) {
               throw this.getExceptionFactory().create("Socket error during result streaming", var10.getSQLState(), var10);
            }

            throw this.getExceptionFactory().create("Socket error during result streaming", "HY000", var11);
         }
      }
   }

   public void closePrepare(Prepare prepare) throws SQLException {
      if (this.closed) {
         throw new SQLNonTransientConnectionException("Connection is closed", "08000", 1220);
      } else {
         try {
            this.currentClient.closePrepare(prepare);
         } catch (SQLNonTransientConnectionException var3) {
            this.reConnect();
         }

      }
   }

   public void abort(Executor executor) throws SQLException {
      if (this.closed) {
         throw new SQLNonTransientConnectionException("Connection is closed", "08000", 1220);
      } else {
         this.currentClient.abort(executor);
      }
   }

   public void close() throws SQLException {
      this.closed = true;
      if (this.currentClient != null) {
         this.currentClient.close();
      }

   }

   public void setReadOnly(boolean readOnly) throws SQLException {
      if (this.closed) {
         throw new SQLNonTransientConnectionException("Connection is closed", "08000", 1220);
      }
   }

   public int getSocketTimeout() {
      return this.currentClient.getSocketTimeout();
   }

   public void setSocketTimeout(int milliseconds) throws SQLException {
      if (this.closed) {
         throw new SQLNonTransientConnectionException("Connection is closed", "08000", 1220);
      } else {
         try {
            this.currentClient.setSocketTimeout(milliseconds);
         } catch (SQLNonTransientConnectionException var3) {
            this.reConnect();
            this.currentClient.setSocketTimeout(milliseconds);
         }

      }
   }

   public boolean isClosed() {
      return this.closed;
   }

   public Context getContext() {
      return this.currentClient.getContext();
   }

   public ExceptionFactory getExceptionFactory() {
      return this.currentClient.getExceptionFactory();
   }

   public HostAddress getHostAddress() {
      return this.currentClient.getHostAddress();
   }

   public String getSocketIp() {
      return this.currentClient.getSocketIp();
   }

   public boolean isPrimary() {
      return true;
   }

   public void reset() {
      this.currentClient.getContext().resetStateFlag();
      this.currentClient.getContext().resetPrepareCache();
   }
}
