package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import fr.xephi.authme.libs.ch.jalu.configme.properties.types.PropertyType;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import java.util.Collection;
import java.util.Objects;
import java.util.function.IntFunction;

public class ArrayProperty<T> extends BaseProperty<T[]> {
   private final PropertyType<T> type;
   private final IntFunction<T[]> arrayProducer;

   public ArrayProperty(String path, T[] defaultValue, PropertyType<T> type, IntFunction<T[]> arrayProducer) {
      super(path, defaultValue);
      Objects.requireNonNull(type, "type");
      Objects.requireNonNull(arrayProducer, "arrayProducer");
      this.type = type;
      this.arrayProducer = arrayProducer;
   }

   protected T[] getFromReader(PropertyReader reader, ConvertErrorRecorder errorRecorder) {
      Object object = reader.getObject(this.getPath());
      if (object instanceof Collection) {
         Collection<?> collection = (Collection)object;
         return collection.stream().map((elem) -> {
            return this.type.convert(elem, errorRecorder);
         }).filter(Objects::nonNull).toArray(this.arrayProducer);
      } else {
         return null;
      }
   }

   public Object toExportValue(T[] value) {
      Object[] array = new Object[value.length];

      for(int i = 0; i < array.length; ++i) {
         array[i] = this.type.toExportValue(value[i]);
      }

      return array;
   }
}
