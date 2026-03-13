package ch.jalu.configme.migration;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.resource.PropertyReader;
import org.jetbrains.annotations.NotNull;

public class PlainMigrationService implements MigrationService {
   public boolean checkAndMigrate(@NotNull PropertyReader reader, @NotNull ConfigurationData configurationData) {
      return this.performMigrations(reader, configurationData) || !configurationData.areAllValuesValidInResource();
   }

   protected boolean performMigrations(@NotNull PropertyReader reader, @NotNull ConfigurationData configurationData) {
      return false;
   }
}
