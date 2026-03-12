package fr.xephi.authme.settings.properties;

import fr.xephi.authme.libs.ch.jalu.configme.Comment;
import fr.xephi.authme.libs.ch.jalu.configme.SettingsHolder;
import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.CommentsConfiguration;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.ch.jalu.configme.properties.PropertyInitializer;

public final class ConverterSettings implements SettingsHolder {
   @Comment({"CrazyLogin database file name"})
   public static final Property<String> CRAZYLOGIN_FILE_NAME = PropertyInitializer.newProperty("Converter.CrazyLogin.fileName", "accounts.db");
   @Comment({"LoginSecurity: convert from SQLite; if false we use MySQL"})
   public static final Property<Boolean> LOGINSECURITY_USE_SQLITE = PropertyInitializer.newProperty("Converter.loginSecurity.useSqlite", true);
   @Comment({"LoginSecurity MySQL: database host"})
   public static final Property<String> LOGINSECURITY_MYSQL_HOST = PropertyInitializer.newProperty("Converter.loginSecurity.mySql.host", "");
   @Comment({"LoginSecurity MySQL: database name"})
   public static final Property<String> LOGINSECURITY_MYSQL_DATABASE = PropertyInitializer.newProperty("Converter.loginSecurity.mySql.database", "");
   @Comment({"LoginSecurity MySQL: database user"})
   public static final Property<String> LOGINSECURITY_MYSQL_USER = PropertyInitializer.newProperty("Converter.loginSecurity.mySql.user", "");
   @Comment({"LoginSecurity MySQL: password for database user"})
   public static final Property<String> LOGINSECURITY_MYSQL_PASSWORD = PropertyInitializer.newProperty("Converter.loginSecurity.mySql.password", "");

   private ConverterSettings() {
   }

   public void registerComments(CommentsConfiguration conf) {
      conf.setComment("Converter", "Converter settings: see https://github.com/AuthMe/AuthMeReloaded/wiki/Converters");
   }
}
