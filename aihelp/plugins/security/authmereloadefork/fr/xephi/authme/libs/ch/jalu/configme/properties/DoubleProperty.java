package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.types.PrimitivePropertyType;

public class DoubleProperty extends TypeBasedProperty<Double> {
   public DoubleProperty(String path, double defaultValue) {
      super(path, defaultValue, PrimitivePropertyType.DOUBLE);
   }
}
