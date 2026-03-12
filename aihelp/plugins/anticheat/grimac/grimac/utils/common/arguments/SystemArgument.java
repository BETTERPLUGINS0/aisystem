package ac.grim.grimac.utils.common.arguments;

import java.util.function.Function;
import java.util.function.Predicate;

public record SystemArgument<T>(String key, Class<T> clazz, T value, boolean set, SystemArgument.Visibility visibility) {
   public SystemArgument(String key, Class<T> clazz, T value, boolean set, SystemArgument.Visibility visibility) {
      this.key = key;
      this.clazz = clazz;
      this.value = value;
      this.set = set;
      this.visibility = visibility;
   }

   public boolean matches(Predicate<T> predicate) {
      return predicate.test(this.value);
   }

   public <K> K mapValue(Function<T, K> mapper, K otherwise) {
      try {
         return this.value == null ? otherwise : mapper.apply(this.value);
      } catch (Exception var4) {
         return otherwise;
      }
   }

   public boolean equals(Object o) {
      if (o != null && this.getClass() == o.getClass()) {
         SystemArgument<?> that = (SystemArgument)o;
         return this.key.equals(that.key);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.key.hashCode();
   }

   public String key() {
      return this.key;
   }

   public Class<T> clazz() {
      return this.clazz;
   }

   public T value() {
      return this.value;
   }

   public boolean set() {
      return this.set;
   }

   public SystemArgument.Visibility visibility() {
      return this.visibility;
   }

   public static enum Visibility {
      VISIBLE,
      HIDDEN,
      SECRET;

      // $FF: synthetic method
      private static SystemArgument.Visibility[] $values() {
         return new SystemArgument.Visibility[]{VISIBLE, HIDDEN, SECRET};
      }
   }
}
