package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import fr.xephi.authme.libs.ch.jalu.configme.properties.inlinearray.InlineArrayConverter;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import java.util.Objects;

public class InlineArrayProperty<T> extends BaseProperty<T[]> {
   private final InlineArrayConverter<T> inlineConverter;

   public InlineArrayProperty(String path, T[] defaultValue, InlineArrayConverter<T> inlineConverter) {
      super(path, defaultValue);
      Objects.requireNonNull(inlineConverter, "inlineConverter");
      this.inlineConverter = inlineConverter;
   }

   protected T[] getFromReader(PropertyReader reader, ConvertErrorRecorder errorRecorder) {
      String value = reader.getString(this.getPath());
      return value == null ? null : this.inlineConverter.fromString(value);
   }

   public Object toExportValue(T[] value) {
      return this.inlineConverter.toExportValue(value);
   }
}
