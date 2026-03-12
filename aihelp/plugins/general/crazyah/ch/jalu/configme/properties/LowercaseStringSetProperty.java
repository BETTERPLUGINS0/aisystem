package ch.jalu.configme.properties;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public class LowercaseStringSetProperty extends StringSetProperty {
   public LowercaseStringSetProperty(@NotNull String path, @NotNull String... defaultEntries) {
      super(path, toLowercaseLinkedHashSet(Arrays.stream(defaultEntries)));
   }

   public LowercaseStringSetProperty(@NotNull String path, @NotNull Collection<String> defaultEntries) {
      super(path, toLowercaseLinkedHashSet(defaultEntries.stream()));
   }

   @NotNull
   protected Collector<String, ?, Set<String>> setCollector() {
      Function<String, String> toLowerCaseFn = (value) -> {
         return String.valueOf(value).toLowerCase();
      };
      return Collectors.mapping(toLowerCaseFn, super.setCollector());
   }

   @NotNull
   protected static Set<String> toLowercaseLinkedHashSet(@NotNull Stream<String> valuesStream) {
      Set<String> valuesLowercase = (Set)valuesStream.map(String::toLowerCase).collect(Collectors.toCollection(LinkedHashSet::new));
      return Collections.unmodifiableSet(valuesLowercase);
   }
}
