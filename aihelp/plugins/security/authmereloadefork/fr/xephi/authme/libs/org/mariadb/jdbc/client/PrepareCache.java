package fr.xephi.authme.libs.org.mariadb.jdbc.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.ServerPreparedStatement;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.Prepare;

public interface PrepareCache {
   Prepare get(String var1, ServerPreparedStatement var2);

   Prepare put(String var1, Prepare var2, ServerPreparedStatement var3);

   void reset();
}
