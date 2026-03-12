package ac.grim.grimac.shaded.configuralize.mapping;

import ac.grim.grimac.shaded.maps.weak.Dynamic;
import java.util.function.Function;

public class MappingFunction<T> {
   private final String key;
   private final Function<Dynamic, T> function;

   public MappingFunction(String key, Function<Dynamic, T> function) {
      this.key = key;
      this.function = function;
   }

   public String getKey() {
      return this.key;
   }

   public Function<Dynamic, T> getFunction() {
      return this.function;
   }
}
