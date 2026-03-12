package fr.xephi.authme.datasource.converter;

import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.DataSourceType;
import fr.xephi.authme.datasource.SQLite;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import java.io.File;
import java.sql.SQLException;

public class SqliteToH2 extends AbstractDataSourceConverter<SQLite> {
   private final Settings settings;
   private final File dataFolder;

   @Inject
   SqliteToH2(Settings settings, DataSource dataSource, @DataFolder File dataFolder) {
      super(dataSource, DataSourceType.H2);
      this.settings = settings;
      this.dataFolder = dataFolder;
   }

   protected SQLite getSource() throws SQLException {
      return new SQLite(this.settings, this.dataFolder);
   }
}
