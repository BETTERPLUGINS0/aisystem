package ch.jalu.configme.properties;

import ch.jalu.configme.properties.types.PrimitivePropertyType;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class StringSetProperty extends SetProperty<String> {
   public StringSetProperty(@NotNull String path, @NotNull String... defaultValue) {
      super(path, PrimitivePropertyType.STRING, (Object[])defaultValue);
   }

   public StringSetProperty(@NotNull String path, @NotNull Set<String> defaultValue) {
      super(path, PrimitivePropertyType.STRING, defaultValue);
   }

   @NotNull
   public Object toExportValue(@NotNull Set<String> value) {
      return value;
   }
}
