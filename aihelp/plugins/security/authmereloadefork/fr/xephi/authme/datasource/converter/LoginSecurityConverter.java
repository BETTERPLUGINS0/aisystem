package fr.xephi.authme.datasource.converter;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.ConverterSettings;
import fr.xephi.authme.util.Utils;
import fr.xephi.authme.util.UuidUtils;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.command.CommandSender;

public class LoginSecurityConverter implements Converter {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(LoginSecurityConverter.class);
   private final File dataFolder;
   private final DataSource dataSource;
   private final boolean useSqlite;
   private final String mySqlHost;
   private final String mySqlDatabase;
   private final String mySqlUser;
   private final String mySqlPassword;

   @Inject
   LoginSecurityConverter(@DataFolder File dataFolder, DataSource dataSource, Settings settings) {
      this.dataFolder = dataFolder;
      this.dataSource = dataSource;
      this.useSqlite = (Boolean)settings.getProperty(ConverterSettings.LOGINSECURITY_USE_SQLITE);
      this.mySqlHost = (String)settings.getProperty(ConverterSettings.LOGINSECURITY_MYSQL_HOST);
      this.mySqlDatabase = (String)settings.getProperty(ConverterSettings.LOGINSECURITY_MYSQL_DATABASE);
      this.mySqlUser = (String)settings.getProperty(ConverterSettings.LOGINSECURITY_MYSQL_USER);
      this.mySqlPassword = (String)settings.getProperty(ConverterSettings.LOGINSECURITY_MYSQL_PASSWORD);
   }

   public void execute(CommandSender sender) {
      try {
         Connection connection = this.createConnectionOrInformSender(sender);

         try {
            if (connection != null) {
               this.performConversion(sender, connection);
               this.logger.info("LoginSecurity conversion completed! Please remember to set \"legacyHashes: ['BCRYPT']\" in your configuration file!");
            }
         } catch (Throwable var6) {
            if (connection != null) {
               try {
                  connection.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (connection != null) {
            connection.close();
         }
      } catch (SQLException var7) {
         sender.sendMessage("Failed to convert from SQLite. Please see the log for more info");
         this.logger.logException("Could not fetch or migrate data:", var7);
      }

   }

   @VisibleForTesting
   void performConversion(CommandSender sender, Connection connection) throws SQLException {
      Statement statement = connection.createStatement();

      try {
         statement.execute("SELECT * from ls_players LEFT JOIN ls_locations ON ls_locations.id = ls_players.id");
         ResultSet resultSet = statement.getResultSet();

         try {
            this.migrateData(sender, resultSet);
         } catch (Throwable var9) {
            if (resultSet != null) {
               try {
                  resultSet.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }
            }

            throw var9;
         }

         if (resultSet != null) {
            resultSet.close();
         }
      } catch (Throwable var10) {
         if (statement != null) {
            try {
               statement.close();
            } catch (Throwable var7) {
               var10.addSuppressed(var7);
            }
         }

         throw var10;
      }

      if (statement != null) {
         statement.close();
      }

   }

   private void migrateData(CommandSender sender, ResultSet resultSet) throws SQLException {
      List<String> skippedPlayers = new ArrayList();
      long successfulSaves = 0L;

      while(resultSet.next()) {
         String name = resultSet.getString("last_name");
         if (this.dataSource.isAuthAvailable(name)) {
            skippedPlayers.add(name);
         } else {
            PlayerAuth auth = buildAuthFromLoginSecurity(name, resultSet);
            this.dataSource.saveAuth(auth);
            this.dataSource.updateSession(auth);
            ++successfulSaves;
         }
      }

      Utils.logAndSendMessage(sender, "Migrated " + successfulSaves + " accounts successfully from LoginSecurity");
      if (!skippedPlayers.isEmpty()) {
         Utils.logAndSendMessage(sender, "Skipped conversion for players which were already in AuthMe: " + String.join(", ", skippedPlayers));
      }

   }

   private static PlayerAuth buildAuthFromLoginSecurity(String name, ResultSet resultSet) throws SQLException {
      Long lastLoginMillis = (Long)Optional.ofNullable(resultSet.getTimestamp("last_login")).map(Timestamp::getTime).orElse((Object)null);
      long regDate = (Long)Optional.ofNullable(resultSet.getDate("registration_date")).map(Date::getTime).orElse(System.currentTimeMillis());
      UUID uuid = UuidUtils.parseUuidSafely(resultSet.getString("unique_user_id"));
      return PlayerAuth.builder().name(name).realName(name).password(resultSet.getString("password"), (String)null).lastIp(resultSet.getString("ip_address")).lastLogin(lastLoginMillis).registrationDate(regDate).locX(resultSet.getDouble("x")).locY(resultSet.getDouble("y")).locZ(resultSet.getDouble("z")).locWorld(resultSet.getString("world")).locYaw(resultSet.getFloat("yaw")).locPitch(resultSet.getFloat("pitch")).uuid(uuid).build();
   }

   private Connection createConnectionOrInformSender(CommandSender sender) {
      Connection connection;
      if (this.useSqlite) {
         File sqliteDatabase = new File(this.dataFolder.getParentFile(), "LoginSecurity/LoginSecurity.db");
         if (!sqliteDatabase.exists()) {
            sender.sendMessage("The file '" + sqliteDatabase.getPath() + "' does not exist");
            return null;
         }

         connection = this.createSqliteConnection("plugins/LoginSecurity/LoginSecurity.db");
      } else {
         if (this.mySqlDatabase.isEmpty() || this.mySqlUser.isEmpty()) {
            sender.sendMessage("The LoginSecurity database or username is not configured in AuthMe's config.yml");
            return null;
         }

         connection = this.createMySqlConnection();
      }

      if (connection == null) {
         sender.sendMessage("Could not connect to LoginSecurity using Sqlite = " + this.useSqlite + ", see log for more info");
         return null;
      } else {
         return connection;
      }
   }

   @VisibleForTesting
   Connection createSqliteConnection(String path) {
      try {
         Class.forName("org.sqlite.JDBC");
      } catch (ClassNotFoundException var4) {
         throw new IllegalStateException(var4);
      }

      try {
         return DriverManager.getConnection("jdbc:sqlite:" + path, "trump", "donald");
      } catch (SQLException var3) {
         this.logger.logException("Could not connect to SQLite database", var3);
         return null;
      }
   }

   private Connection createMySqlConnection() {
      try {
         return DriverManager.getConnection("jdbc:mysql://" + this.mySqlHost + "/" + this.mySqlDatabase, this.mySqlUser, this.mySqlPassword);
      } catch (SQLException var2) {
         this.logger.logException("Could not connect to SQLite database", var2);
         return null;
      }
   }
}
