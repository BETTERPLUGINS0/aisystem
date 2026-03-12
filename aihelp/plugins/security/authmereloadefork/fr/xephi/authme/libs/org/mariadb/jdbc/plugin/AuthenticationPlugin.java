package fr.xephi.authme.libs.org.mariadb.jdbc.plugin;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import java.io.IOException;
import java.sql.SQLException;

public interface AuthenticationPlugin {
   String type();

   void initialize(String var1, byte[] var2, Configuration var3);

   ReadableByteBuf process(Writer var1, Reader var2, Context var3) throws IOException, SQLException;
}
