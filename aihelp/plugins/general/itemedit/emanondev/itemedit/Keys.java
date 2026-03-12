package emanondev.itemedit;

import emanondev.itemedit.utility.VersionUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Keys {
   private static NamespacedKey craftKey(String postfix) {
      return new NamespacedKey("minecraft", postfix);
   }

   private static class KeyRegistry<T extends Keyed> implements Registry<T> {
      private final LinkedHashMap<NamespacedKey, T> values = new LinkedHashMap();

      public KeyRegistry(Collection<T> collection) {
         collection.forEach((v) -> {
            this.values.put(v.getKey(), v);
         });
      }

      public KeyRegistry(T... collection) {
         Keyed[] var2 = collection;
         int var3 = collection.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            T value = var2[var4];
            this.values.put(value.getKey(), value);
         }

      }

      @Nullable
      public T get(@NotNull NamespacedKey namespacedKey) {
         return (Keyed)this.values.get(namespacedKey);
      }

      @NotNull
      public T getOrThrow(@NotNull NamespacedKey namespacedKey) {
         if (!this.values.containsKey(namespacedKey)) {
            throw new IllegalArgumentException();
         } else {
            return (Keyed)this.values.get(namespacedKey);
         }
      }

      @NotNull
      public Stream<T> stream() {
         return StreamSupport.stream(this.spliterator(), false);
      }

      @NotNull
      public Iterator<T> iterator() {
         return this.values.values().iterator();
      }
   }

   public abstract static class Component implements Keyed {
      public static final NamespacedKey FOOD = Keys.craftKey("food");
      public static final NamespacedKey CONSUMABLE = Keys.craftKey("consumable");
      public static final NamespacedKey USE_REMAINDER = Keys.craftKey("use_remainder");
      public static final NamespacedKey CROSS_VERSION_CONSUMABLE;

      static {
         CROSS_VERSION_CONSUMABLE = VersionUtils.isVersionUpTo(1, 21, 1) ? FOOD : CONSUMABLE;
      }
   }

   public abstract static class EffectType implements Keyed {
      public static final NamespacedKey APPLY_EFFECTS = Keys.craftKey("apply_effects");
      public static final NamespacedKey REMOVE_EFFECTS = Keys.craftKey("remove_effects");
      public static final NamespacedKey CLEAR_ALL_EFFECTS = Keys.craftKey("clear_all_effects");
      public static final NamespacedKey TELEPORT_RANDOMLY = Keys.craftKey("teleport_randomly");
      public static final NamespacedKey PLAY_SOUND = Keys.craftKey("play_sound");
   }
}
