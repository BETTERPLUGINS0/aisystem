package github.nighter.smartspawner.libs.mariadb.plugin.authentication.addon.gssapi;

import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import java.io.IOException;
import java.sql.SQLException;

public interface GssapiAuth {
   void authenticate(Writer var1, Reader var2, String var3, String var4) throws IOException, SQLException;
}
