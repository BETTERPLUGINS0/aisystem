package github.nighter.smartspawner.libs.mariadb.plugin.credential.system;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.plugin.Credential;
import github.nighter.smartspawner.libs.mariadb.plugin.CredentialPlugin;

public class PropertiesCredentialPlugin implements CredentialPlugin {
   private Configuration conf;
   private String userName;

   public String type() {
      return "PROPERTY";
   }

   public CredentialPlugin initialize(Configuration conf, String userName, HostAddress hostAddress) {
      this.conf = conf;
      this.userName = userName;
      return this;
   }

   public Credential get() {
      String userKey = this.conf.nonMappedOptions().getProperty("userKey");
      String pwdKey = this.conf.nonMappedOptions().getProperty("pwdKey");
      String propUser = System.getProperty(userKey != null ? userKey : "mariadb.user");
      return new Credential(propUser == null ? this.userName : propUser, System.getProperty(pwdKey != null ? pwdKey : "mariadb.pwd"));
   }
}
