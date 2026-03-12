package ac.grim.grimac.shaded.maps.weak;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Weak<Self extends Weak<Self>> {
   boolean isPresent();

   Object asObject();

   default Optional<Object> asOptional() {
      return this.isPresent() ? Optional.of(this.asObject()) : Optional.empty();
   }

   default <T> T as(Class<T> type) {
      return type.cast(this.asObject());
   }

   default String asString() {
      return (String)this.as(String.class);
   }

   default <T> List<T> asList() {
      return (List)this.as(List.class);
   }

   default <K, V> Map<K, V> asMap() {
      return (Map)this.as(Map.class);
   }

   default boolean is(Class<?> type) {
      return this.isPresent() && type.isInstance(this.asObject());
   }

   default boolean isMap() {
      return this.is(Map.class);
   }

   default boolean isString() {
      return this.is(String.class);
   }

   default boolean isList() {
      return this.is(List.class);
   }

   default Converter convert() {
      return Converter.convert(this.asObject());
   }

   default OptionalWeak<Self> maybe() {
      return this.isPresent() ? OptionalWeak.of(this) : OptionalWeak.empty();
   }
}
