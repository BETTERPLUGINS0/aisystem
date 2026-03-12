package fr.xephi.authme.libs.org.mariadb.jdbc.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.ExceptionFactory;
import java.util.function.Function;

public interface Context {
   long getThreadId();

   void setThreadId(long var1);

   Long getAutoIncrement();

   void setAutoIncrement(long var1);

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

   Integer getTransactionIsolationLevel();

   void setTransactionIsolationLevel(int var1);

   PrepareCache getPrepareCache();

   void resetPrepareCache();

   int getStateFlag();

   void resetStateFlag();

   void addStateFlag(int var1);

   void setTreadsConnected(long var1);

   String getCharset();

   void setCharset(String var1);
}
