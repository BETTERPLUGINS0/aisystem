package fr.xephi.authme.datasource.converter;

import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.DataSourceType;
import fr.xephi.authme.datasource.MySQL;
import fr.xephi.authme.datasource.mysqlextensions.MySqlExtensionsFactory;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import java.sql.SQLException;

public class MySqlToSqlite extends AbstractDataSourceConverter<MySQL> {
   private final Settings settings;
   private final MySqlExtensionsFactory mySqlExtensionsFactory;

   @Inject
   MySqlToSqlite(DataSource dataSource, Settings settings, MySqlExtensionsFactory mySqlExtensionsFactory) {
      super(dataSource, DataSourceType.SQLITE);
      this.settings = settings;
      this.mySqlExtensionsFactory = mySqlExtensionsFactory;
   }

   protected MySQL getSource() throws SQLException {
      return new MySQL(this.settings, this.mySqlExtensionsFactory);
   }
}
