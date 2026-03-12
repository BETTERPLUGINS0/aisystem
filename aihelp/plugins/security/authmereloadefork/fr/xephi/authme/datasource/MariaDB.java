package fr.xephi.authme.datasource;

import fr.xephi.authme.datasource.mysqlextensions.MySqlExtensionsFactory;
import fr.xephi.authme.settings.Settings;
import java.sql.SQLException;

public class MariaDB extends MySQL {
   public MariaDB(Settings settings, MySqlExtensionsFactory extensionsFactory) throws SQLException {
      super(settings, extensionsFactory);
   }

   String getJdbcUrl(String host, String port, String database) {
      return "jdbc:mariadb://" + host + ":" + port + "/" + database;
   }

   protected String getDriverClassName() {
      return "fr.xephi.authme.libs.org.mariadb.jdbc.Driver";
   }

   public DataSourceType getType() {
      return DataSourceType.MARIADB;
   }
}
