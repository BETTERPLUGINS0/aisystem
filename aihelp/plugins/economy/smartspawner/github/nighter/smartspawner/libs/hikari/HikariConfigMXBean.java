package github.nighter.smartspawner.libs.hikari;

import github.nighter.smartspawner.libs.hikari.util.Credentials;

public interface HikariConfigMXBean {
   long getConnectionTimeout();

   void setConnectionTimeout(long var1);

   long getValidationTimeout();

   void setValidationTimeout(long var1);

   long getIdleTimeout();

   void setIdleTimeout(long var1);

   long getLeakDetectionThreshold();

   void setLeakDetectionThreshold(long var1);

   long getMaxLifetime();

   void setMaxLifetime(long var1);

   int getMinimumIdle();

   void setMinimumIdle(int var1);

   int getMaximumPoolSize();

   void setMaximumPoolSize(int var1);

   void setPassword(String var1);

   void setUsername(String var1);

   void setCredentials(Credentials var1);

   String getPoolName();

   String getCatalog();

   void setCatalog(String var1);
}
