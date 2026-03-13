package github.nighter.smartspawner.libs.mariadb.client;

import github.nighter.smartspawner.libs.mariadb.BasePreparedStatement;
import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.export.ExceptionFactory;
import github.nighter.smartspawner.libs.mariadb.export.Prepare;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.function.Function;

public interface Context {
   long getThreadId();

   Boolean isLoopbackAddress();

   void setThreadId(long var1);

   Long getAutoIncrement();

   void setAutoIncrement(long var1);

   void setMaxscaleVersion(String var1);

   String getMaxscaleVersion();

   void setRedirectUrl(String var1);

   String getRedirectUrl();

   byte[] getSeed();

   boolean hasServerCapability(long var1);

   boolean hasClientCapability(long var1);

   boolean permitPipeline();

   int getServerStatus();

   void setServerStatus(int var1);

   String getDatabase();

   void setDatabase(String var1);

   ServerVersion getVersion();

   boolean isEofDeprecated();

   boolean canSkipMeta();

   Function<ReadableByteBuf, ColumnDecoder> getColumnDecoderFunction();

   int getWarning();

   void setWarning(int var1);

   ExceptionFactory getExceptionFactory();

   Configuration getConf();

   boolean canUseTransactionIsolation();

   Integer getTransactionIsolationLevel();

   void setTransactionIsolationLevel(Integer var1);

   Prepare getPrepareCacheCmd(String var1, BasePreparedStatement var2);

   Prepare putPrepareCacheCmd(String var1, Prepare var2, BasePreparedStatement var3);

   void resetPrepareCache();

   int getStateFlag();

   void resetStateFlag();

   void addStateFlag(int var1);

   void setTreadsConnected(long var1);

   String getCharset();

   void setCharset(String var1);

   TimeZone getConnectionTimeZone();

   void setConnectionTimeZone(TimeZone var1);

   Calendar getDefaultCalendar();
}
