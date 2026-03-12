package fr.xephi.authme.libs.org.mariadb.jdbc.client.context;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.HostAddress;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.PrepareCache;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ServerVersion;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.ExceptionFactory;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.InitialHandshakePacket;
import java.util.function.Function;

public class BaseContext implements Context {
   private final long serverCapabilities;
   private final long clientCapabilities;
   private final byte[] seed;
   private final ServerVersion version;
   private final boolean eofDeprecated;
   private final boolean skipMeta;
   private final Function<ReadableByteBuf, ColumnDecoder> columnDecoderFunction;
   private final Configuration conf;
   private final ExceptionFactory exceptionFactory;
   private final PrepareCache prepareCache;
   private final HostAddress hostAddress;
   protected int serverStatus;
   private Long autoIncrement;
   private long threadId;
   private String charset;
   private String database;
   private Integer transactionIsolationLevel;
   private int warning;
   private int stateFlag = 0;

   public BaseContext(HostAddress hostAddress, InitialHandshakePacket handshake, long clientCapabilities, Configuration conf, ExceptionFactory exceptionFactory, PrepareCache prepareCache) {
      this.hostAddress = hostAddress;
      this.threadId = handshake.getThreadId();
      this.seed = handshake.getSeed();
      this.serverCapabilities = handshake.getCapabilities();
      this.serverStatus = handshake.getServerStatus();
      this.version = handshake.getVersion();
      this.clientCapabilities = clientCapabilities;
      this.eofDeprecated = this.hasClientCapability(16777216L);
      this.skipMeta = this.hasClientCapability(68719476736L);
      this.columnDecoderFunction = this.hasClientCapability(34359738368L) ? ColumnDecoder::decode : ColumnDecoder::decodeStd;
      this.conf = conf;
      this.database = conf.database();
      this.exceptionFactory = exceptionFactory;
      this.prepareCache = prepareCache;
   }

   public long getThreadId() {
      return this.threadId;
   }

   public void setThreadId(long connectionId) {
      this.threadId = connectionId;
   }

   public byte[] getSeed() {
      return this.seed;
   }

   public boolean hasServerCapability(long flag) {
      return (this.serverCapabilities & flag) > 0L;
   }

   public boolean hasClientCapability(long flag) {
      return (this.clientCapabilities & flag) > 0L;
   }

   public boolean permitPipeline() {
      return !this.conf.disablePipeline() && (this.clientCapabilities & 17179869184L) > 0L;
   }

   public int getServerStatus() {
      return this.serverStatus;
   }

   public void setServerStatus(int serverStatus) {
      this.serverStatus = serverStatus;
   }

   public String getDatabase() {
      return this.database;
   }

   public void setDatabase(String database) {
      this.database = database;
   }

   public ServerVersion getVersion() {
      return this.version;
   }

   public boolean isEofDeprecated() {
      return this.eofDeprecated;
   }

   public Function<ReadableByteBuf, ColumnDecoder> getColumnDecoderFunction() {
      return this.columnDecoderFunction;
   }

   public boolean canSkipMeta() {
      return this.skipMeta;
   }

   public int getWarning() {
      return this.warning;
   }

   public void setWarning(int warning) {
      this.warning = warning;
   }

   public ExceptionFactory getExceptionFactory() {
      return this.exceptionFactory;
   }

   public Configuration getConf() {
      return this.conf;
   }

   public Integer getTransactionIsolationLevel() {
      return this.transactionIsolationLevel;
   }

   public void setTransactionIsolationLevel(int transactionIsolationLevel) {
      this.transactionIsolationLevel = transactionIsolationLevel;
   }

   public PrepareCache getPrepareCache() {
      return this.prepareCache;
   }

   public void resetPrepareCache() {
      if (this.prepareCache != null) {
         this.prepareCache.reset();
      }

   }

   public int getStateFlag() {
      return this.stateFlag;
   }

   public void resetStateFlag() {
      this.stateFlag = 0;
   }

   public void addStateFlag(int state) {
      this.stateFlag |= state;
   }

   public void setTreadsConnected(long threadsConnected) {
      if (this.hostAddress != null) {
         this.hostAddress.setThreadsConnected(threadsConnected);
      }

   }

   public Long getAutoIncrement() {
      return this.autoIncrement;
   }

   public void setAutoIncrement(long autoIncrement) {
      this.autoIncrement = autoIncrement;
   }

   public String getCharset() {
      return this.charset;
   }

   public void setCharset(String charset) {
      this.charset = charset;
   }
}
