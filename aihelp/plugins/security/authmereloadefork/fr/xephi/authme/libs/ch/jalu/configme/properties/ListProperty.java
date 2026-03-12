package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import fr.xephi.authme.libs.ch.jalu.configme.properties.types.PropertyType;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class ListProperty<T> extends BaseProperty<List<T>> {
   private final PropertyType<T> type;

   @SafeVarargs
   public ListProperty(String path, PropertyType<T> type, T... defaultValue) {
      this(path, type, Arrays.asList(defaultValue));
   }

   public ListProperty(String path, PropertyType<T> type, List<T> defaultValue) {
      super(path, Collections.unmodifiableList(defaultValue));
      Objects.requireNonNull(type, "type");
      this.type = type;
   }

   @Nullable
   protected List<T> getFromReader(PropertyReader reader, ConvertErrorRecorder errorRecorder) {
      List<?> list = reader.getList(this.getPath());
      return list != null ? Collections.unmodifiableList((List)list.stream().map((elem) -> {
         return this.type.convert(elem, errorRecorder);
      }).filter(Objects::nonNull).collect(Collectors.toList())) : null;
   }

   public Object toExportValue(List<T> value) {
      Stream var10000 = value.stream();
      PropertyType var10001 = this.type;
      var10001.getClass();
      return var10000.map(var10001::toExportValue).collect(Collectors.toList());
   }
}
