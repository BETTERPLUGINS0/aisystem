package fr.xephi.authme.datasource.mysqlextensions;

import fr.xephi.authme.datasource.Columns;
import fr.xephi.authme.settings.Settings;

class NoOpExtension extends MySqlExtension {
   NoOpExtension(Settings settings, Columns col) {
      super(settings, col);
   }
}
