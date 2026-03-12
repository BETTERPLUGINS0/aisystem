package fr.xephi.authme.libs.ch.jalu.configme;

import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationData;
import fr.xephi.authme.libs.ch.jalu.configme.migration.MigrationService;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyResource;
import javax.annotation.Nullable;

public class SettingsManagerImpl implements SettingsManager {
   private final ConfigurationData configurationData;
   private final PropertyResource resource;
   private final MigrationService migrationService;

   protected SettingsManagerImpl(PropertyResource resource, ConfigurationData configurationData, @Nullable MigrationService migrationService) {
      this.configurationData = configurationData;
      this.resource = resource;
      this.migrationService = migrationService;
      this.loadFromResourceAndValidate();
   }

   public <T> T getProperty(Property<T> property) {
      return this.configurationData.getValue(property);
   }

   public <T> void setProperty(Property<T> property, T value) {
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

   protected final PropertyResource getPropertyResource() {
      return this.resource;
   }

   protected final ConfigurationData getConfigurationData() {
      return this.configurationData;
   }

   @Nullable
   protected final MigrationService getMigrationService() {
      return this.migrationService;
   }
}
