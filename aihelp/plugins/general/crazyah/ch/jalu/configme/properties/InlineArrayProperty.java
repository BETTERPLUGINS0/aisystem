package ch.jalu.configme.properties;

import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.properties.inlinearray.InlineArrayConverter;
import ch.jalu.configme.resource.PropertyReader;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InlineArrayProperty<T> extends BaseProperty<T[]> {
   private final InlineArrayConverter<T> inlineConverter;

   public InlineArrayProperty(@NotNull String path, @NotNull T[] defaultValue, @NotNull InlineArrayConverter<T> inlineConverter) {
      super(path, defaultValue);
      Objects.requireNonNull(inlineConverter, "inlineConverter");
      this.inlineConverter = inlineConverter;
   }

   @Nullable
   protected T[] getFromReader(@NotNull PropertyReader reader, @NotNull ConvertErrorRecorder errorRecorder) {
      String value = reader.getString(this.getPath());
      return value == null ? null : this.inlineConverter.fromString(value);
   }

   public Object toExportValue(@NotNull T[] value) {
      return this.inlineConverter.toExportValue(value);
   }
}
