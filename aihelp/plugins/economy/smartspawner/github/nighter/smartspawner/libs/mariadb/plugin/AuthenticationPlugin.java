package github.nighter.smartspawner.libs.mariadb.plugin;

import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import java.io.IOException;
import java.sql.SQLException;

public interface AuthenticationPlugin {
   ReadableByteBuf process(Writer var1, Reader var2, Context var3, boolean var4) throws IOException, SQLException;

   default boolean isMitMProof() {
      return false;
   }

   default byte[] hash(Credential credential) {
      return null;
   }
}
