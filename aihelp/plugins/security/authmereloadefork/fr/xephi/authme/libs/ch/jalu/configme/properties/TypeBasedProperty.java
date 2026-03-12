package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import fr.xephi.authme.libs.ch.jalu.configme.properties.types.PropertyType;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import java.util.Objects;
import javax.annotation.Nullable;

public class TypeBasedProperty<T> extends BaseProperty<T> {
   private final PropertyType<T> type;

   public TypeBasedProperty(String path, T defaultValue, PropertyType<T> type) {
      super(path, defaultValue);
      Objects.requireNonNull(type, "type");
      this.type = type;
   }

   @Nullable
   protected T getFromReader(PropertyReader reader, ConvertErrorRecorder errorRecorder) {
      return this.type.convert(reader.getObject(this.getPath()), errorRecorder);
   }

   @Nullable
   public Object toExportValue(T value) {
      return this.type.toExportValue(value);
   }
}
