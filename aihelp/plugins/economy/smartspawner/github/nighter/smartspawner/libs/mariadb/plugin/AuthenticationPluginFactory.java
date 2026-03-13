package github.nighter.smartspawner.libs.mariadb.plugin;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;

public interface AuthenticationPluginFactory {
   String type();

   AuthenticationPlugin initialize(String var1, byte[] var2, Configuration var3, HostAddress var4);

   default boolean requireSsl() {
      return false;
   }
}
