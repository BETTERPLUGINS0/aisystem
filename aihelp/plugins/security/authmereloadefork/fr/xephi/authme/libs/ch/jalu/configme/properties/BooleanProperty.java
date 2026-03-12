package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.types.PrimitivePropertyType;

public class BooleanProperty extends TypeBasedProperty<Boolean> {
   public BooleanProperty(String path, Boolean defaultValue) {
      super(path, defaultValue, PrimitivePropertyType.BOOLEAN);
   }
}
