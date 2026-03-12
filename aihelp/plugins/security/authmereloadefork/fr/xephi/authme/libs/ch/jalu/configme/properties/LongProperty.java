package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.types.PrimitivePropertyType;

public class LongProperty extends TypeBasedProperty<Long> {
   public LongProperty(String path, Long defaultValue) {
      super(path, defaultValue, PrimitivePropertyType.LONG);
   }
}
