package ch.jalu.configme.properties;

import ch.jalu.configme.properties.types.PrimitivePropertyType;
import org.jetbrains.annotations.NotNull;

public class BooleanProperty extends TypeBasedProperty<Boolean> {
   public BooleanProperty(@NotNull String path, @NotNull Boolean defaultValue) {
      super(path, defaultValue, PrimitivePropertyType.BOOLEAN);
   }
}
