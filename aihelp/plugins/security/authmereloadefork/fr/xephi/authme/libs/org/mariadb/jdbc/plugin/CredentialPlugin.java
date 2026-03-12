package fr.xephi.authme.libs.org.mariadb.jdbc.plugin;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.HostAddress;
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
