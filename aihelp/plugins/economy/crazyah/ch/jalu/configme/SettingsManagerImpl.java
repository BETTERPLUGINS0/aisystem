package ch.jalu.configme;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.migration.MigrationService;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.resource.PropertyReader;
import ch.jalu.configme.resource.PropertyResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SettingsManagerImpl implements SettingsManager {
   private final ConfigurationData configurationData;
   private final PropertyResource resource;
   private final MigrationService migrationService;

   protected SettingsManagerImpl(@NotNull PropertyResource resource, @NotNull ConfigurationData configurationData, @Nullable MigrationService migrationService) {
      this.configurationData = configurationData;
      this.resource = resource;
      this.migrationService = migrationService;
      this.loadFromResourceAndValidate();
   }

   @NotNull
   public <T> T getProperty(@NotNull Property<T> property) {
      return this.configurationData.getValue(property);
   }

   public <T> void setProperty(@NotNull Property<T> property, @NotNull T value) {
      this.configurationData.setValue(property, value);
   }

   public void reload() {
      this.loadFromResourceAndValidate();
   }

   public void save() {
      this.resource.exportProperties(this.configurationData);
   }

   protected void loadFromResourceAndValidate() {
      PropertyReader reader = this.resource.createReader();
      this.configurationData.initializeValues(reader);
      if (this.migrationService != null && this.migrationService.checkAndMigrate(reader, this.configurationData)) {
         this.save();
      }

   }

   @NotNull
   protected final PropertyResource getPropertyResource() {
      return this.resource;
   }

   @NotNull
   protected final ConfigurationData getConfigurationData() {
      return this.configurationData;
   }

   @Nullable
   protected final MigrationService getMigrationService() {
      return this.migrationService;
   }
}
