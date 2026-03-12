package fr.xephi.authme.datasource.converter;

import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.DataSourceType;
import fr.xephi.authme.datasource.H2;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import java.io.File;
import java.sql.SQLException;

public class H2ToSqlite extends AbstractDataSourceConverter<H2> {
   private final Settings settings;
   private final File dataFolder;

   @Inject
   H2ToSqlite(Settings settings, DataSource dataSource, @DataFolder File dataFolder) {
      super(dataSource, DataSourceType.SQLITE);
      this.settings = settings;
      this.dataFolder = dataFolder;
   }

   protected H2 getSource() throws SQLException {
      return new H2(this.settings, this.dataFolder);
   }
}
