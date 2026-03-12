package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.PropertyValue;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import javax.annotation.Nullable;

public interface Property<T> {
   String getPath();

   PropertyValue<T> determineValue(PropertyReader var1);

   default boolean isValidInResource(PropertyReader propertyReader) {
      return this.determineValue(propertyReader).isValidInResource();
   }

   T getDefaultValue();

   boolean isValidValue(T var1);

   @Nullable
   Object toExportValue(T var1);
}
