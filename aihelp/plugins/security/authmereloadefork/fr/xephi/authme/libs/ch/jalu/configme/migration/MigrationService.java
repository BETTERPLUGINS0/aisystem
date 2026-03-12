package fr.xephi.authme.libs.ch.jalu.configme.migration;

import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationData;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;

public interface MigrationService {
   boolean MIGRATION_REQUIRED = true;
   boolean NO_MIGRATION_NEEDED = false;

   boolean checkAndMigrate(PropertyReader var1, ConfigurationData var2);
}
