package fr.xephi.authme.initialization;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.CacheDataSource;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.DataSourceType;
import fr.xephi.authme.datasource.H2;
import fr.xephi.authme.datasource.MariaDB;
import fr.xephi.authme.datasource.MySQL;
import fr.xephi.authme.datasource.PostgreSqlDataSource;
import fr.xephi.authme.datasource.SQLite;
import fr.xephi.authme.datasource.mysqlextensions.MySqlExtensionsFactory;
import fr.xephi.authme.libs.com.alessiodp.libby.Library;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.libs.javax.inject.Provider;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import java.io.File;
import java.sql.SQLException;

public class DataSourceProvider implements Provider<DataSource> {
   private static final int SQLITE_MAX_SIZE = 4000;
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(DataSourceProvider.class);
   @Inject
   @DataFolder
   private File dataFolder;
   @Inject
   private Settings settings;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private MySqlExtensionsFactory mySqlExtensionsFactory;

   DataSourceProvider() {
   }

   public DataSource get() {
      try {
         return this.createDataSource();
      } catch (Exception var2) {
         this.logger.logException("Could not create data source:", var2);
         throw new IllegalStateException("Error during initialization of data source", var2);
      }
   }

   private DataSource createDataSource() throws SQLException {
      DataSourceType dataSourceType = (DataSourceType)this.settings.getProperty(DatabaseSettings.BACKEND);
      Object dataSource;
      switch(dataSourceType) {
      case MYSQL:
         dataSource = new MySQL(this.settings, this.mySqlExtensionsFactory);
         break;
      case MARIADB:
         dataSource = new MariaDB(this.settings, this.mySqlExtensionsFactory);
         break;
      case POSTGRESQL:
         dataSource = new PostgreSqlDataSource(this.settings, this.mySqlExtensionsFactory);
         break;
      case SQLITE:
         dataSource = new SQLite(this.settings, this.dataFolder);
         break;
      case H2:
         Library h2 = Library.builder().groupId("com.h2database").artifactId("h2").version("2.2.224").build();
         AuthMe.libraryManager.addMavenCentral();
         AuthMe.libraryManager.loadLibrary(h2);
         dataSource = new H2(this.settings, this.dataFolder);
         break;
      default:
         throw new UnsupportedOperationException("Unknown data source type '" + dataSourceType + "'");
      }

      if ((Boolean)this.settings.getProperty(DatabaseSettings.USE_CACHING)) {
         dataSource = new CacheDataSource((DataSource)dataSource, this.playerCache);
      }

      if (DataSourceType.SQLITE.equals(dataSourceType)) {
         this.checkDataSourceSize((DataSource)dataSource);
      }

      return (DataSource)dataSource;
   }

   private void checkDataSourceSize(DataSource dataSource) {
      this.bukkitService.runTaskAsynchronously(() -> {
         int accounts = dataSource.getAccountsRegistered();
         if (accounts >= 4000) {
            this.logger.warning("YOU'RE USING THE SQLITE DATABASE WITH " + accounts + "+ ACCOUNTS; FOR BETTER PERFORMANCE, PLEASE UPGRADE TO MYSQL!!");
         }

      });
   }
}
