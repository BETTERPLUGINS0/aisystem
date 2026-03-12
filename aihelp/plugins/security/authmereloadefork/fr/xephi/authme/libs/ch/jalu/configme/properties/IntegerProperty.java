package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.types.PrimitivePropertyType;

public class IntegerProperty extends TypeBasedProperty<Integer> {
   public IntegerProperty(String path, Integer defaultValue) {
      super(path, defaultValue, PrimitivePropertyType.INTEGER);
   }
}
