package ch.jalu.configme.properties;

import ch.jalu.configme.properties.types.PrimitivePropertyType;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class StringListProperty extends ListProperty<String> {
   public StringListProperty(@NotNull String path, @NotNull String... defaultValue) {
      super(path, PrimitivePropertyType.STRING, (Object[])defaultValue);
   }

   public StringListProperty(@NotNull String path, @NotNull List<String> defaultValue) {
      super(path, PrimitivePropertyType.STRING, defaultValue);
   }

   @NotNull
   public Object toExportValue(@NotNull List<String> value) {
      return value;
   }
}
