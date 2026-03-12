package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.Internal
public final class MapUtil {
   private MapUtil() {
   }

   public static boolean isDeepEqual(@Nullable Object o1, @Nullable Object o2) {
      if (o1 == o2) {
         return true;
      } else if (o1 != null && o2 != null) {
         if (o1 instanceof DeepComparableEntity) {
            return ((DeepComparableEntity)o1).deepEquals(o2);
         } else {
            return o2 instanceof DeepComparableEntity ? ((DeepComparableEntity)o2).deepEquals(o2) : Objects.equals(o1, o2);
         }
      } else {
         return false;
      }
   }

   public static <K> boolean isDeepEqual(Map<K, ?> map1, Map<K, ?> map2) {
      if (map1.isEmpty() && map2.isEmpty()) {
         return true;
      } else if (map1.size() != map2.size()) {
         return false;
      } else {
         Iterator var2 = map1.entrySet().iterator();

         Entry entry;
         Object val2;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            entry = (Entry)var2.next();
            val2 = map2.get(entry.getKey());
         } while(isDeepEqual(entry.getValue(), val2));

         return false;
      }
   }

   @SafeVarargs
   public static <K, V> Map<K, V> createMap(Entry<? extends K, ? extends V>... entries) {
      if (entries.length == 0) {
         return Collections.emptyMap();
      } else {
         Map<K, V> map = new HashMap(entries.length);
         Entry[] var2 = entries;
         int var3 = entries.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Entry<? extends K, ? extends V> entry = var2[var4];
            map.put(entry.getKey(), entry.getValue());
         }

         return Collections.unmodifiableMap(map);
      }
   }
}
