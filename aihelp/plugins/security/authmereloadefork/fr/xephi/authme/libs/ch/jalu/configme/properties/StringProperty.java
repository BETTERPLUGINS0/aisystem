package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.types.PrimitivePropertyType;

public class StringProperty extends TypeBasedProperty<String> {
   public StringProperty(String path, String defaultValue) {
      super(path, defaultValue, PrimitivePropertyType.STRING);
   }
}
