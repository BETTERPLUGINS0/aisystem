package fr.xephi.authme.libs.com.mysql.cj.protocol;

import fr.xephi.authme.libs.com.mysql.cj.ServerVersion;

public interface ServerCapabilities {
   int getCapabilityFlags();

   void setCapabilityFlags(int var1);

   ServerVersion getServerVersion();

   long getThreadId();

   void setThreadId(long var1);

   boolean serverSupportsFracSecs();

   int getServerDefaultCollationIndex();
}
