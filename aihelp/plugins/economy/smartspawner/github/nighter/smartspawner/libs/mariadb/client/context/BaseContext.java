package github.nighter.smartspawner.libs.mariadb.client.context;

import github.nighter.smartspawner.libs.mariadb.BasePreparedStatement;
import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.PrepareCache;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.ServerVersion;
import github.nighter.smartspawner.libs.mariadb.export.ExceptionFactory;
import github.nighter.smartspawner.libs.mariadb.export.Prepare;
import github.nighter.smartspawner.libs.mariadb.message.server.InitialHandshakePacket;
import java.util.Calendar;
import java.util.TimeZone;
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
   private final boolean canUseTransactionIsolation;
   private final Boolean loopbackAddress;
   private final PrepareCache prepareCache;
   private final HostAddress hostAddress;
   protected int serverStatus;
   private Long autoIncrement;
   private String maxscaleVersion = null;
   private long threadId;
   private String charset;
   private String database;
   private Integer transactionIsolationLevel;
   private int warning;
   private int stateFlag = 0;
   private String redirectUrl = null;
   private TimeZone connectionTimeZone = null;

   public BaseContext(HostAddress hostAddress, InitialHandshakePacket handshake, long clientCapabilities, Configuration conf, ExceptionFactory exceptionFactory, PrepareCache prepareCache, Boolean loopbackAddress) {
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
      this.canUseTransactionIsolation = this.version.isMariaDBServer() && this.version.versionGreaterOrEqual(11, 1, 1) || !this.version.isMariaDBServer() && (this.version.getMajorVersion() >= 8 && this.version.versionGreaterOrEqual(8, 0, 3) || this.version.getMajorVersion() < 8 && this.version.versionGreaterOrEqual(5, 7, 20));
      this.loopbackAddress = loopbackAddress;
   }

   public Boolean isLoopbackAddress() {
      return this.loopbackAddress;
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

   public void setMaxscaleVersion(String maxscaleVersion) {
      this.maxscaleVersion = maxscaleVersion;
   }

   public void setDatabase(String database) {
      this.database = database;
   }

   public String getMaxscaleVersion() {
      return this.maxscaleVersion;
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

   public void setTransactionIsolationLevel(Integer transactionIsolationLevel) {
      this.transactionIsolationLevel = transactionIsolationLevel;
   }

   public Prepare getPrepareCacheCmd(String sql, BasePreparedStatement preparedStatement) {
      return this.prepareCache.get(this.database + "|" + sql, preparedStatement);
   }

   public Prepare putPrepareCacheCmd(String sql, Prepare result, BasePreparedStatement preparedStatement) {
      return this.prepareCache.put(this.database + "|" + sql, result, preparedStatement);
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

   public String getRedirectUrl() {
      return this.redirectUrl;
   }

   public boolean canUseTransactionIsolation() {
      return this.canUseTransactionIsolation;
   }

   public void setRedirectUrl(String redirectUrl) {
      this.redirectUrl = redirectUrl;
   }

   public TimeZone getConnectionTimeZone() {
      return this.connectionTimeZone;
   }

   public void setConnectionTimeZone(TimeZone connectionTimeZone) {
      this.connectionTimeZone = connectionTimeZone;
   }

   public Calendar getDefaultCalendar() {
      return this.conf.preserveInstants() ? Calendar.getInstance(this.connectionTimeZone) : Calendar.getInstance();
   }
}
