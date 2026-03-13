package github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.plugin.AuthenticationPlugin;
import github.nighter.smartspawner.libs.mariadb.plugin.AuthenticationPluginFactory;

public class Ed25519PasswordPluginFactory implements AuthenticationPluginFactory {
   public String type() {
      return "client_ed25519";
   }

   public AuthenticationPlugin initialize(String authenticationData, byte[] seed, Configuration conf, HostAddress hostAddress) {
      return new Ed25519PasswordPlugin(authenticationData, seed);
   }
}
