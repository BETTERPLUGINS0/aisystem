package github.nighter.smartspawner.libs.mariadb.plugin.authentication.addon;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.plugin.AuthenticationPlugin;
import github.nighter.smartspawner.libs.mariadb.plugin.AuthenticationPluginFactory;

public class SendGssApiAuthPacketFactory implements AuthenticationPluginFactory {
   public String type() {
      return "auth_gssapi_client";
   }

   public AuthenticationPlugin initialize(String authenticationData, byte[] seed, Configuration conf, HostAddress hostAddress) {
      return new SendGssApiAuthPacket(seed, conf);
   }
}
