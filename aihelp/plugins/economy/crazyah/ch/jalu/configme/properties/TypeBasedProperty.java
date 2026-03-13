package ch.jalu.configme.properties;

import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.properties.types.PropertyType;
import ch.jalu.configme.resource.PropertyReader;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TypeBasedProperty<T> extends BaseProperty<T> {
   private final PropertyType<T> type;

   public TypeBasedProperty(@NotNull String path, @NotNull T defaultValue, @NotNull PropertyType<T> type) {
      super(path, defaultValue);
      Objects.requireNonNull(type, "type");
      this.type = type;
   }

   @Nullable
   protected T getFromReader(@NotNull PropertyReader reader, @NotNull ConvertErrorRecorder errorRecorder) {
      return this.type.convert(reader.getObject(this.getPath()), errorRecorder);
   }

   @Nullable
   public Object toExportValue(@NotNull T value) {
      return this.type.toExportValue(value);
   }

   public PropertyType<T> getType() {
      return this.type;
   }
}
