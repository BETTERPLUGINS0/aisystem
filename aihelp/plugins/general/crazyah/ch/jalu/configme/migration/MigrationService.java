package ch.jalu.configme.migration;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.resource.PropertyReader;
import org.jetbrains.annotations.NotNull;

public interface MigrationService {
   boolean MIGRATION_REQUIRED = true;
   boolean NO_MIGRATION_NEEDED = false;

   boolean checkAndMigrate(@NotNull PropertyReader var1, @NotNull ConfigurationData var2);
}
