package com.volmit.iris.util.data.registry;

import com.volmit.iris.util.collection.KMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public interface KeyedRegistry<T> {
   @NonNull
   Map<NamespacedKey, T> map();

   @Nullable
   T get(@NonNull NamespacedKey key);

   @Nullable
   NamespacedKey keyOf(@NonNull T value);

   default boolean isEmpty() {
      return this.map().isEmpty();
   }

   @NonNull
   default Optional<T> find(@NonNull String... keys) {
      if (keys == null) {
         throw new NullPointerException("keys is marked non-null but is null");
      } else if (keys.length == 0) {
         throw new IllegalArgumentException("Need at least one key");
      } else {
         String[] var2 = keys;
         int var3 = keys.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String key = var2[var4];
            T t = this.get(NamespacedKey.minecraft(key));
            if (t != null) {
               return Optional.of(t);
            }
         }

         return Optional.empty();
      }
   }

   @NonNull
   default Optional<T> find(@NonNull NamespacedKey... keys) {
      if (keys == null) {
         throw new NullPointerException("keys is marked non-null but is null");
      } else if (keys.length == 0) {
         throw new IllegalArgumentException("Need at least one key");
      } else {
         NamespacedKey[] var2 = keys;
         int var3 = keys.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            NamespacedKey key = var2[var4];
            T t = this.get(key);
            if (t != null) {
               return Optional.of(t);
            }
         }

         return Optional.empty();
      }
   }

   @Contract(
      value = "null -> null; !null -> new",
      pure = true
   )
   static <T> KeyedRegistry<T> wrapped(Map<NamespacedKey, T> map) {
      return map == null ? null : new KeyedRegistry.MappedRegistry(map);
   }

   @Contract(
      value = "null -> null; !null -> new",
      pure = true
   )
   static <T extends Keyed> KeyedRegistry<T> wrapped(Registry<T> registry) {
      return registry == null ? null : new KeyedRegistry.BukkitRegistry(registry);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   static <T> KeyedRegistry<T> wrapped(@NonNull Collection<KeyedRegistry<T>> registries) {
      if (registries == null) {
         throw new NullPointerException("registries is marked non-null but is null");
      } else {
         return new KeyedRegistry.CompoundRegistry(registries);
      }
   }

   public static record MappedRegistry<T>(Map<NamespacedKey, T> map) implements KeyedRegistry<T> {
      public MappedRegistry(Map<NamespacedKey, T> map) {
         this.map = var1;
      }

      @Nullable
      public T get(@NonNull NamespacedKey key) {
         if (var1 == null) {
            throw new NullPointerException("key is marked non-null but is null");
         } else {
            return this.map.get(var1);
         }
      }

      @Nullable
      public NamespacedKey keyOf(@NonNull T value) {
         if (var1 == null) {
            throw new NullPointerException("value is marked non-null but is null");
         } else {
            return (NamespacedKey)this.map.entrySet().stream().filter((var1x) -> {
               return var1x.getValue().equals(var1);
            }).map(Entry::getKey).findFirst().orElse((Object)null);
         }
      }

      public Map<NamespacedKey, T> map() {
         return this.map;
      }
   }

   public static record BukkitRegistry<T extends Keyed>(Registry<T> registry) implements KeyedRegistry<T> {
      public BukkitRegistry(Registry<T> registry) {
         this.registry = var1;
      }

      @NonNull
      public Map<NamespacedKey, T> map() {
         return (Map)this.registry.stream().collect(Collectors.toMap(Keyed::getKey, Function.identity()));
      }

      @Nullable
      public T get(@NonNull NamespacedKey key) {
         if (var1 == null) {
            throw new NullPointerException("key is marked non-null but is null");
         } else {
            return this.registry.get(var1);
         }
      }

      @NonNull
      public NamespacedKey keyOf(@NonNull T value) {
         if (var1 == null) {
            throw new NullPointerException("value is marked non-null but is null");
         } else {
            return var1.getKey();
         }
      }

      public Registry<T> registry() {
         return this.registry;
      }
   }

   public static record CompoundRegistry<T>(Collection<KeyedRegistry<T>> registries) implements KeyedRegistry<T> {
      public CompoundRegistry(Collection<KeyedRegistry<T>> registries) {
         this.registries = var1;
      }

      @NonNull
      public Map<NamespacedKey, T> map() {
         KMap var1 = new KMap();
         Iterator var2 = this.registries.iterator();

         while(var2.hasNext()) {
            KeyedRegistry var3 = (KeyedRegistry)var2.next();
            var1.put(var3.map());
         }

         return var1;
      }

      @Nullable
      public T get(@NonNull NamespacedKey key) {
         if (var1 == null) {
            throw new NullPointerException("key is marked non-null but is null");
         } else {
            Iterator var2 = this.registries.iterator();

            Object var4;
            do {
               if (!var2.hasNext()) {
                  return null;
               }

               KeyedRegistry var3 = (KeyedRegistry)var2.next();
               var4 = var3.get(var1);
            } while(var4 == null);

            return var4;
         }
      }

      @Nullable
      public NamespacedKey keyOf(@NonNull T value) {
         if (var1 == null) {
            throw new NullPointerException("value is marked non-null but is null");
         } else {
            Iterator var2 = this.registries.iterator();

            NamespacedKey var4;
            do {
               if (!var2.hasNext()) {
                  return null;
               }

               KeyedRegistry var3 = (KeyedRegistry)var2.next();
               var4 = var3.keyOf(var1);
            } while(var4 == null);

            return var4;
         }
      }

      public boolean isEmpty() {
         return this.registries.isEmpty() || this.registries.stream().allMatch(KeyedRegistry::isEmpty);
      }

      public Collection<KeyedRegistry<T>> registries() {
         return this.registries;
      }
   }
}
