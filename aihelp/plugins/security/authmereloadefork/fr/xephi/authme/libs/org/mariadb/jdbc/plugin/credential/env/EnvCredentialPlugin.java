package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.credential.env;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.HostAddress;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Credential;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.CredentialPlugin;

public class EnvCredentialPlugin implements CredentialPlugin {
   private Configuration conf;
   private String userName;

   public String type() {
      return "ENV";
   }

   public CredentialPlugin initialize(Configuration conf, String userName, HostAddress hostAddress) {
      this.conf = conf;
      this.userName = userName;
      return this;
   }

   public Credential get() {
      String userKey = this.conf.nonMappedOptions().getProperty("userKey");
      String pwdKey = this.conf.nonMappedOptions().getProperty("pwdKey");
      String envUser = System.getenv(userKey != null ? userKey : "MARIADB_USER");
      return new Credential(envUser == null ? this.userName : envUser, System.getenv(pwdKey != null ? pwdKey : "MARIADB_PWD"));
   }
}
