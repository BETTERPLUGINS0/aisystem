package github.nighter.smartspawner.libs.mariadb.client.impl;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.Statement;
import github.nighter.smartspawner.libs.mariadb.client.Client;
import github.nighter.smartspawner.libs.mariadb.client.Completion;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.context.RedoContext;
import github.nighter.smartspawner.libs.mariadb.client.util.ClosableLock;
import github.nighter.smartspawner.libs.mariadb.export.ExceptionFactory;
import github.nighter.smartspawner.libs.mariadb.export.Prepare;
import github.nighter.smartspawner.libs.mariadb.message.ClientMessage;
import github.nighter.smartspawner.libs.mariadb.message.client.ChangeDbPacket;
import github.nighter.smartspawner.libs.mariadb.message.client.QueryPacket;
import github.nighter.smartspawner.libs.mariadb.message.client.RedoableWithPrepareClientMessage;
import github.nighter.smartspawner.libs.mariadb.util.log.Logger;
import github.nighter.smartspawner.libs.mariadb.util.log.Loggers;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLTimeoutException;
import java.sql.SQLTransientConnectionException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

public class MultiPrimaryClient implements Client {
   protected static final ConcurrentMap<HostAddress, Long> denyList = new ConcurrentHashMap();
   private static final Logger logger = Loggers.getLogger(MultiPrimaryClient.class);
   protected final long deniedListTimeout;
   protected final Configuration conf;
   protected final ClosableLock lock;
   protected boolean closed = false;
   protected Client currentClient;

   public MultiPrimaryClient(Configuration conf, ClosableLock lock) throws SQLException {
      this.conf = conf;
      this.lock = lock;
      this.deniedListTimeout = Long.parseLong(conf.nonMappedOptions().getProperty("deniedListTimeout", "60000"));
      this.currentClient = this.connectHost(false, false);
   }

   protected Client connectHost(boolean readOnly, boolean failFast) throws SQLException {
      int maxRetries = this.conf.retriesAllDown();

      try {
         Client client = this.tryConnectToAvailableHost(readOnly, maxRetries);
         if (client != null) {
            return client;
         }
      } catch (SQLTimeoutException | SQLNonTransientConnectionException var5) {
         if (failFast) {
            throw var5;
         }
      }

      if (failFast) {
         throw new SQLNonTransientConnectionException("all hosts are blacklisted");
      } else {
         this.validateHostConfiguration(readOnly);
         return this.tryConnectToDeniedHost(readOnly, maxRetries);
      }
   }

   private Client tryConnectToAvailableHost(boolean readOnly, int retriesLeft) throws SQLException {
      SQLNonTransientConnectionException lastException = null;

      while(true) {
         if (retriesLeft > 0) {
            Optional<HostAddress> host = this.conf.haMode().getAvailableHost(this.conf.addresses(), denyList, !readOnly);
            if (host.isPresent()) {
               try {
                  return this.createClient((HostAddress)host.get());
               } catch (SQLTimeoutException | SQLNonTransientConnectionException var6) {
                  lastException = var6;
                  this.addToDenyList((HostAddress)host.get());
                  --retriesLeft;
                  continue;
               }
            }
         }

         if (lastException != null) {
            throw lastException;
         }

         return null;
      }
   }

   private Client tryConnectToDeniedHost(boolean readOnly, int retriesLeft) throws SQLException {
      SQLNonTransientConnectionException lastException = null;

      while(true) {
         while(retriesLeft > 0) {
            Optional<HostAddress> host = this.findHostWithLowestDenyTimeout(readOnly);
            if (host.isPresent()) {
               try {
                  Client client = this.createClient((HostAddress)host.get());
                  denyList.remove(host.get());
                  return client;
               } catch (SQLTimeoutException | SQLNonTransientConnectionException var6) {
                  lastException = var6;
                  host.ifPresent(this::addToDenyList);
                  --retriesLeft;
                  if (retriesLeft > 0) {
                     this.sleepBeforeRetry();
                  }
               }
            } else {
               --retriesLeft;
            }
         }

         throw lastException != null ? lastException : new SQLNonTransientConnectionException("No host");
      }
   }

   private Optional<HostAddress> findHostWithLowestDenyTimeout(boolean readOnly) {
      return denyList.entrySet().stream().sorted(Entry.comparingByValue()).filter((e) -> {
         return this.conf.addresses().contains(e.getKey()) && ((HostAddress)e.getKey()).primary != readOnly;
      }).findFirst().map(Entry::getKey);
   }

   private void validateHostConfiguration(boolean readOnly) throws SQLNonTransientConnectionException {
      boolean hasValidHost = denyList.entrySet().stream().anyMatch((e) -> {
         return this.conf.addresses().contains(e.getKey()) && ((HostAddress)e.getKey()).primary != readOnly;
      });
      if (!hasValidHost) {
         throw new SQLNonTransientConnectionException(String.format("No %s host defined", readOnly ? "replica" : "primary"));
      }
   }

   private Client createClient(HostAddress host) throws SQLException {
      return (Client)(this.conf.transactionReplay() ? new ReplayClient(this.conf, host, this.lock, false) : new StandardClient(this.conf, host, this.lock, false));
   }

   private void addToDenyList(HostAddress host) {
      denyList.putIfAbsent(host, System.currentTimeMillis() + this.deniedListTimeout);
   }

   private void sleepBeforeRetry() {
      try {
         Thread.sleep(250L);
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
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
         this.closed = true;
         throw var2;
      }
   }

   protected void replayIfPossible(Client oldClient, boolean canRedo) throws SQLException {
      if (oldClient != null) {
         if ((oldClient.getContext().getServerStatus() & 1) > 0) {
            if (!this.conf.transactionReplay()) {
               oldClient.getContext().setServerStatus(oldClient.getContext().getServerStatus() & -2);
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
      this.syncExceptionFactory(oldCli);
      this.syncAutoCommit(oldCtx);
      this.syncDatabase(oldCtx);
      this.syncNetworkTimeout(oldCtx, oldCli);
      this.syncReadOnlyState(oldCtx);
      this.syncTransactionIsolation(oldCtx);
   }

   private void syncExceptionFactory(Client oldCli) {
      this.currentClient.getExceptionFactory().setConnection(oldCli.getExceptionFactory());
   }

   private void syncAutoCommit(Context oldCtx) throws SQLException {
      if (this.isAutoCommitSyncRequired(oldCtx)) {
         this.currentClient.getContext().addStateFlag(8);
         String autoCommitValue = (oldCtx.getServerStatus() & 2) > 0 ? "1" : "0";
         this.currentClient.execute(new QueryPacket("set autocommit=" + autoCommitValue), true);
      }
   }

   private boolean isAutoCommitSyncRequired(Context oldCtx) {
      return (oldCtx.getStateFlag() & 8) > 0 && (oldCtx.getServerStatus() & 2) != (this.currentClient.getContext().getServerStatus() & 2);
   }

   private void syncDatabase(Context oldCtx) throws SQLException {
      if (this.isDatabaseSyncRequired(oldCtx)) {
         this.currentClient.getContext().addStateFlag(2);
         if (oldCtx.getDatabase() != null) {
            this.currentClient.execute(new ChangeDbPacket(oldCtx.getDatabase()), true);
         }

         this.currentClient.getContext().setDatabase(oldCtx.getDatabase());
      }
   }

   private boolean isDatabaseSyncRequired(Context oldCtx) {
      return (oldCtx.getStateFlag() & 2) > 0 && !Objects.equals(this.currentClient.getContext().getDatabase(), oldCtx.getDatabase());
   }

   private void syncNetworkTimeout(Context oldCtx, Client oldCli) throws SQLException {
      if ((oldCtx.getStateFlag() & 1) > 0) {
         this.currentClient.setSocketTimeout(oldCli.getSocketTimeout());
      }

   }

   private void syncReadOnlyState(Context oldCtx) throws SQLException {
      if (this.isReadOnlySyncRequired(oldCtx)) {
         this.currentClient.execute(new QueryPacket("SET SESSION TRANSACTION READ ONLY"), true);
      }
   }

   private boolean isReadOnlySyncRequired(Context oldCtx) {
      return (oldCtx.getStateFlag() & 4) > 0 && !this.currentClient.getHostAddress().primary && this.currentClient.getContext().getVersion().versionGreaterOrEqual(5, 6, 5);
   }

   private void syncTransactionIsolation(Context oldCtx) throws SQLException {
      if (this.isTransactionIsolationSyncRequired(oldCtx)) {
         String query = this.buildTransactionIsolationQuery(oldCtx.getTransactionIsolationLevel());
         this.currentClient.getContext().setTransactionIsolationLevel(oldCtx.getTransactionIsolationLevel());
         this.currentClient.execute(new QueryPacket(query), true);
      }
   }

   private boolean isTransactionIsolationSyncRequired(Context oldCtx) {
      return (oldCtx.getStateFlag() & 16) > 0 && !oldCtx.getTransactionIsolationLevel().equals(this.currentClient.getContext().getTransactionIsolationLevel());
   }

   private String buildTransactionIsolationQuery(int isolationLevel) {
      String baseQuery = "SET SESSION TRANSACTION ISOLATION LEVEL";
      switch(isolationLevel) {
      case 1:
         return baseQuery + " READ UNCOMMITTED";
      case 2:
         return baseQuery + " READ COMMITTED";
      case 3:
      case 5:
      case 6:
      case 7:
      default:
         throw new IllegalArgumentException("Unsupported isolation level: " + isolationLevel);
      case 4:
         return baseQuery + " REPEATABLE READ";
      case 8:
         return baseQuery + " SERIALIZABLE";
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
      if (!this.closed) {
         this.closed = true;
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
