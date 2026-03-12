package fr.xephi.authme.libs.com.mysql.cj.log;

import fr.xephi.authme.libs.com.mysql.cj.Query;
import fr.xephi.authme.libs.com.mysql.cj.Session;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Resultset;

public interface ProfilerEventHandler {
   void init(Log var1);

   void destroy();

   void consumeEvent(ProfilerEvent var1);

   void processEvent(byte var1, Session var2, Query var3, Resultset var4, long var5, Throwable var7, String var8);
}
