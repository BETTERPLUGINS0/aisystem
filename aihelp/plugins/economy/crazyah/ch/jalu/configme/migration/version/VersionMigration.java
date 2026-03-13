package ch.jalu.configme.migration.version;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.resource.PropertyReader;
import org.jetbrains.annotations.NotNull;

public interface VersionMigration {
   int fromVersion();

   int targetVersion();

   void migrate(@NotNull PropertyReader var1, @NotNull ConfigurationData var2);
}
