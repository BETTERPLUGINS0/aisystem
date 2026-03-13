package ch.jalu.configme.properties;

import ch.jalu.configme.properties.types.PrimitivePropertyType;
import org.jetbrains.annotations.NotNull;

public class DoubleProperty extends TypeBasedProperty<Double> {
   public DoubleProperty(@NotNull String path, double defaultValue) {
      super(path, defaultValue, PrimitivePropertyType.DOUBLE);
   }
}
