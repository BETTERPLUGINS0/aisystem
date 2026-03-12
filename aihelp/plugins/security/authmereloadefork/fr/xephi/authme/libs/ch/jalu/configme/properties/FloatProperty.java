package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.types.PrimitivePropertyType;

public class FloatProperty extends TypeBasedProperty<Float> {
   public FloatProperty(String path, float defaultValue) {
      super(path, defaultValue, PrimitivePropertyType.FLOAT);
   }
}
