package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import fr.xephi.authme.libs.ch.jalu.configme.properties.types.PropertyType;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetProperty<T> extends BaseProperty<Set<T>> {
   private final PropertyType<T> type;

   @SafeVarargs
   public SetProperty(String path, PropertyType<T> type, T... defaultValue) {
      this(path, type, newSet(defaultValue));
   }

   public SetProperty(String path, PropertyType<T> type, Set<T> defaultValue) {
      super(path, Collections.unmodifiableSet(defaultValue));
      Objects.requireNonNull(type, "type");
      this.type = type;
   }

   protected Set<T> getFromReader(PropertyReader reader, ConvertErrorRecorder errorRecorder) {
      List<?> list = reader.getList(this.getPath());
      return list != null ? (Set)list.stream().map((elem) -> {
         return this.type.convert(elem, errorRecorder);
      }).filter(Objects::nonNull).collect(this.setCollector()) : null;
   }

   public Object toExportValue(Set<T> value) {
      Stream var10000 = value.stream();
      PropertyType var10001 = this.type;
      var10001.getClass();
      return var10000.map(var10001::toExportValue).collect(Collectors.toList());
   }

   protected Collector<T, ?, Set<T>> setCollector() {
      return Collectors.collectingAndThen(Collectors.toCollection(LinkedHashSet::new), Collections::unmodifiableSet);
   }

   private static <E> Set<E> newSet(E[] array) {
      return (Set)Arrays.stream(array).collect(Collectors.toCollection(LinkedHashSet::new));
   }
}
