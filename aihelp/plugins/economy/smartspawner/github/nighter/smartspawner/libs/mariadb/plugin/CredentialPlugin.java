package github.nighter.smartspawner.libs.mariadb.plugin;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import java.sql.SQLException;
import java.util.function.Supplier;

public interface CredentialPlugin extends Supplier<Credential> {
   String type();

   default boolean mustUseSsl() {
      return false;
   }

   default String defaultAuthenticationPluginType() {
      return null;
   }

   default CredentialPlugin initialize(Configuration conf, String userName, HostAddress hostAddress) throws SQLException {
      return this;
   }
}
