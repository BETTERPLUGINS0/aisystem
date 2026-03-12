package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.addon.gssapi;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import java.io.IOException;
import java.sql.SQLException;

public interface GssapiAuth {
   void authenticate(Writer var1, Reader var2, String var3, String var4) throws IOException, SQLException;
}
