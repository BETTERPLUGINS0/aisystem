package fr.xephi.authme.libs.ch.jalu.configme.properties;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LowercaseStringSetProperty extends StringSetProperty {
   public LowercaseStringSetProperty(String path, String... defaultEntries) {
      super(path, toLowercaseLinkedHashSet(Arrays.stream(defaultEntries)));
   }

   public LowercaseStringSetProperty(String path, Collection<String> defaultEntries) {
      super(path, toLowercaseLinkedHashSet(defaultEntries.stream()));
   }

   protected Collector<String, ?, Set<String>> setCollector() {
      Function<String, String> toLowerCaseFn = (value) -> {
         return String.valueOf(value).toLowerCase();
      };
      return Collectors.mapping(toLowerCaseFn, super.setCollector());
   }

   protected static Set<String> toLowercaseLinkedHashSet(Stream<String> valuesStream) {
      Set<String> valuesLowercase = (Set)valuesStream.map(String::toLowerCase).collect(Collectors.toCollection(LinkedHashSet::new));
      return Collections.unmodifiableSet(valuesLowercase);
   }
}
