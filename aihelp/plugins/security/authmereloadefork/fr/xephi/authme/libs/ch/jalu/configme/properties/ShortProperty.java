package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.types.PrimitivePropertyType;

public class ShortProperty extends TypeBasedProperty<Short> {
   public ShortProperty(String path, Short defaultValue) {
      super(path, defaultValue, PrimitivePropertyType.SHORT);
   }
}
