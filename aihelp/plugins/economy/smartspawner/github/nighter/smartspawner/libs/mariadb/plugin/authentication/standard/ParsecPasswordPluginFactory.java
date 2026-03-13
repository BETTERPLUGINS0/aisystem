package github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.plugin.AuthenticationPlugin;
import github.nighter.smartspawner.libs.mariadb.plugin.AuthenticationPluginFactory;

public class ParsecPasswordPluginFactory implements AuthenticationPluginFactory {
   public String type() {
      return "parsec";
   }

   public AuthenticationPlugin initialize(String authenticationData, byte[] seed, Configuration conf, HostAddress hostAddress) {
      return new ParsecPasswordPlugin(authenticationData, seed);
   }
}
