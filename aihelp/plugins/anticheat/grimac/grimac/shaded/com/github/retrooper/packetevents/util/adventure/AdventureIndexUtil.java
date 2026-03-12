package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure;

import ac.grim.grimac.shaded.kyori.adventure.util.Index;
import java.util.NoSuchElementException;

public final class AdventureIndexUtil {
   private AdventureIndexUtil() {
   }

   public static <K, V> V indexValueOrThrow(Index<K, V> index, K key) {
      V value = index.value(key);
      if (value == null) {
         throw new NoSuchElementException("There is no value for key " + key);
      } else {
         return value;
      }
   }

   public static <K, V> K indexKeyOrThrow(Index<K, V> index, V value) {
      K key = index.key(value);
      if (key == null) {
         throw new NoSuchElementException("There is no key for value " + value);
      } else {
         return key;
      }
   }
}
