package fr.xephi.authme.datasource.mysqlextensions;

import fr.xephi.authme.datasource.Columns;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.SecuritySettings;

public class MySqlExtensionsFactory {
   @Inject
   private Settings settings;

   public MySqlExtension buildExtension(Columns columnsConfig) {
      HashAlgorithm hash = (HashAlgorithm)this.settings.getProperty(SecuritySettings.PASSWORD_HASH);
      switch(hash) {
      case IPB4:
         return new Ipb4Extension(this.settings, columnsConfig);
      case PHPBB:
         return new PhpBbExtension(this.settings, columnsConfig);
      case WORDPRESS:
         return new WordpressExtension(this.settings, columnsConfig);
      case XFBCRYPT:
         return new XfBcryptExtension(this.settings, columnsConfig);
      default:
         return new NoOpExtension(this.settings, columnsConfig);
      }
   }
}
