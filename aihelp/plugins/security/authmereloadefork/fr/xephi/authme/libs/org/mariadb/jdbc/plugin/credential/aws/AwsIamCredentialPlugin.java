package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.credential.aws;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.HostAddress;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Credential;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.CredentialPlugin;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AwsIamCredentialPlugin implements CredentialPlugin {
   private static final int TOKEN_TTL = 10;
   private static final Map<AwsIamCredentialPlugin.KeyCache, AwsIamCredentialPlugin.IdentityExpire> cache = new ConcurrentHashMap();
   private AwsCredentialGenerator generator;
   private AwsIamCredentialPlugin.KeyCache key;

   public String type() {
      return "AWS-IAM";
   }

   public boolean mustUseSsl() {
      return true;
   }

   public CredentialPlugin initialize(Configuration conf, String userName, HostAddress hostAddress) throws SQLException {
      try {
         Class.forName("software.amazon.awssdk.auth.credentials.AwsBasicCredentials");
      } catch (ClassNotFoundException var5) {
         throw new SQLException("Identity plugin 'AWS-IAM' is used without having AWS SDK in classpath. Please add 'software.amazon.awssdk:rds' to classpath");
      }

      this.generator = new AwsCredentialGenerator(conf.nonMappedOptions(), conf.user(), hostAddress);
      this.key = new AwsIamCredentialPlugin.KeyCache(conf, conf.user(), hostAddress);
      return this;
   }

   public Credential get() {
      AwsIamCredentialPlugin.IdentityExpire val = (AwsIamCredentialPlugin.IdentityExpire)cache.get(this.key);
      if (val != null && val.isValid()) {
         return val.getCredential();
      } else {
         Credential credential = this.generator.getToken();
         cache.put(this.key, new AwsIamCredentialPlugin.IdentityExpire(credential));
         return credential;
      }
   }

   private static class KeyCache {
      private final Configuration conf;
      private final String userName;
      private final HostAddress hostAddress;

      public KeyCache(Configuration conf, String userName, HostAddress hostAddress) {
         this.conf = conf;
         this.userName = userName;
         this.hostAddress = hostAddress;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            AwsIamCredentialPlugin.KeyCache keyCache = (AwsIamCredentialPlugin.KeyCache)o;
            return this.conf.equals(keyCache.conf) && Objects.equals(this.userName, keyCache.userName) && this.hostAddress.equals(keyCache.hostAddress);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.conf, this.userName, this.hostAddress});
      }
   }

   private static class IdentityExpire {
      private final LocalDateTime expiration;
      private final Credential credential;

      public IdentityExpire(Credential credential) {
         this.credential = credential;
         this.expiration = LocalDateTime.now().plusMinutes(10L);
      }

      public boolean isValid() {
         return this.expiration.isAfter(LocalDateTime.now());
      }

      public Credential getCredential() {
         return this.credential;
      }
   }
}
