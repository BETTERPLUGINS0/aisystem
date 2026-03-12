package ch.jalu.configme.utils;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.convertresult.PropertyValue;
import ch.jalu.configme.resource.PropertyReader;
import org.jetbrains.annotations.NotNull;

public final class MigrationUtils {
   private MigrationUtils() {
   }

   public static <T> boolean moveProperty(@NotNull Property<T> oldProperty, @NotNull Property<T> newProperty, @NotNull PropertyReader reader, @NotNull ConfigurationData configurationData) {
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
