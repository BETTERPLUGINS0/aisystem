package ch.jalu.configme.properties;

import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.properties.types.PropertyType;
import ch.jalu.configme.resource.PropertyReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public class SetProperty<T> extends BaseProperty<Set<T>> {
   private final PropertyType<T> type;

   @SafeVarargs
   public SetProperty(@NotNull String path, @NotNull PropertyType<T> type, @NotNull T... defaultValue) {
      this(path, type, newSet(defaultValue));
   }

   public SetProperty(@NotNull String path, @NotNull PropertyType<T> type, @NotNull Set<T> defaultValue) {
      super(path, Collections.unmodifiableSet(defaultValue));
      Objects.requireNonNull(type, "type");
      this.type = type;
   }

   protected Set<T> getFromReader(@NotNull PropertyReader reader, @NotNull ConvertErrorRecorder errorRecorder) {
      List<?> list = reader.getList(this.getPath());
      return list != null ? (Set)list.stream().map((elem) -> {
         return this.type.convert(elem, errorRecorder);
      }).filter(Objects::nonNull).collect(this.setCollector()) : null;
   }

   @NotNull
   public Object toExportValue(@NotNull Set<T> value) {
      Stream var10000 = value.stream();
      PropertyType var10001 = this.type;
      var10001.getClass();
      return var10000.map(var10001::toExportValue).collect(Collectors.toList());
   }

   @NotNull
   protected Collector<T, ?, Set<T>> setCollector() {
      return Collectors.collectingAndThen(Collectors.toCollection(LinkedHashSet::new), Collections::unmodifiableSet);
   }

   @NotNull
   private static <E> Set<E> newSet(@NotNull E[] array) {
      return (Set)Arrays.stream(array).collect(Collectors.toCollection(LinkedHashSet::new));
   }
}
