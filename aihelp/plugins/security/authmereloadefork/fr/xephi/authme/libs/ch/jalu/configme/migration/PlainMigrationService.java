package fr.xephi.authme.libs.ch.jalu.configme.migration;

import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationData;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.PropertyValue;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;

public class PlainMigrationService implements MigrationService {
   public boolean checkAndMigrate(PropertyReader reader, ConfigurationData configurationData) {
      return this.performMigrations(reader, configurationData) || !configurationData.areAllValuesValidInResource();
   }

   protected boolean performMigrations(PropertyReader reader, ConfigurationData configurationData) {
      return false;
   }

   protected static <T> boolean moveProperty(Property<T> oldProperty, Property<T> newProperty, PropertyReader reader, ConfigurationData configurationData) {
      if (reader.contains(oldProperty.getPath())) {
         if (!reader.contains(newProperty.getPath())) {
            PropertyValue<T> value = oldProperty.determineValue(reader);
            configurationData.setValue(newProperty, value.getValue());
         }

         return true;
      } else {
         return false;
      }
   }
}
