package com.volmit.iris.util.data.registry;

import com.volmit.iris.core.nms.container.Pair;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RegistryUtil {
   private static final AtomicCache<RegistryUtil.RegistryLookup> registryLookup = new AtomicCache();
   private static final KMap<Class<?>, KeyedRegistry<Object>> CACHE = new KMap();

   @NonNull
   public static <T> T find(@NonNull Class<T> typeClass, @NonNull String... keys) {
      if (var0 == null) {
         throw new NullPointerException("typeClass is marked non-null but is null");
      } else if (var1 == null) {
         throw new NullPointerException("keys is marked non-null but is null");
      } else {
         return find(var0, (NamespacedKey[])Arrays.stream(var1).map(NamespacedKey::minecraft).toArray((var0x) -> {
            return new NamespacedKey[var0x];
         }));
      }
   }

   @NonNull
   public static <T> T find(@NonNull Class<T> typeClass, @NonNull NamespacedKey... keys) {
      if (var0 == null) {
         throw new NullPointerException("typeClass is marked non-null but is null");
      } else if (var1 == null) {
         throw new NullPointerException("keys is marked non-null but is null");
      } else {
         return lookup(var0).find(var1).orElseThrow(() -> {
            return new IllegalArgumentException("No element found for keys: " + Arrays.toString(var1));
         });
      }
   }

   @NonNull
   public static <T> KeyedRegistry<T> lookup(@NonNull Class<T> typeClass) {
      if (var0 == null) {
         throw new NullPointerException("typeClass is marked non-null but is null");
      } else {
         return (KeyedRegistry)CACHE.computeIfAbsent(var0, (var1) -> {
            KList var2 = new KList();
            if (Keyed.class.isAssignableFrom(var0)) {
               KeyedRegistry var3 = KeyedRegistry.wrapped(getRegistry(var0.asSubclass(Keyed.class)));
               if (var3 == null) {
                  var3 = (KeyedRegistry)Arrays.stream(Registry.class.getDeclaredFields()).filter((var0x) -> {
                     return Modifier.isStatic(var0x.getModifiers()) && Modifier.isPublic(var0x.getModifiers());
                  }).filter((var0x) -> {
                     return Registry.class.isAssignableFrom(var0x.getType());
                  }).filter((var1x) -> {
                     return ((ParameterizedType)var1x.getGenericType()).getActualTypeArguments()[0].equals(var0);
                  }).map((var0x) -> {
                     try {
                        return (Registry)var0x.get((Object)null);
                     } catch (IllegalAccessException var2) {
                        return null;
                     }
                  }).filter(Objects::nonNull).findFirst().map(KeyedRegistry::wrapped).orElse((Object)null);
               }

               var2.addNonNull((KeyedRegistry)var3);
            }

            var2.add((Object)getKeyedValues(var0));
            var2.add((Object)getEnumValues(var0));
            return KeyedRegistry.wrapped((Collection)var2);
         });
      }
   }

   private static KeyedRegistry<Object> getKeyedValues(@NonNull Class<?> typeClass) {
      if (var0 == null) {
         throw new NullPointerException("typeClass is marked non-null but is null");
      } else {
         return KeyedRegistry.wrapped((Map)Arrays.stream(var0.getDeclaredFields()).filter((var0x) -> {
            return Modifier.isPublic(var0x.getModifiers()) && Modifier.isStatic(var0x.getModifiers());
         }).filter((var0x) -> {
            return Keyed.class.isAssignableFrom(var0x.getType());
         }).map((var0x) -> {
            try {
               Keyed var1 = (Keyed)var0x.get((Object)null);
               return new Pair(var1.getKey(), var1);
            } catch (Throwable var2) {
               return null;
            }
         }).filter(Objects::nonNull).collect(Collectors.toMap(Pair::getA, Pair::getB)));
      }
   }

   private static KeyedRegistry<Object> getEnumValues(@NonNull Class<?> typeClass) {
      if (var0 == null) {
         throw new NullPointerException("typeClass is marked non-null but is null");
      } else {
         return KeyedRegistry.wrapped((Map)Arrays.stream(var0.getDeclaredFields()).filter((var0x) -> {
            return Modifier.isPublic(var0x.getModifiers()) && Modifier.isStatic(var0x.getModifiers());
         }).filter((var1) -> {
            return var0.isAssignableFrom(var1.getType());
         }).map((var0x) -> {
            try {
               return new Pair(NamespacedKey.minecraft(var0x.getName().toLowerCase()), var0x.get((Object)null));
            } catch (Throwable var2) {
               return null;
            }
         }).filter(Objects::nonNull).collect(Collectors.toMap(Pair::getA, Pair::getB)));
      }
   }

   @Nullable
   private static <T extends Keyed> Registry<T> getRegistry(@NotNull Class<T> type) {
      RegistryUtil.RegistryLookup var1 = (RegistryUtil.RegistryLookup)registryLookup.aquire(() -> {
         RegistryUtil.RegistryLookup var0;
         try {
            var0 = Bukkit::getRegistry;
         } catch (Throwable var2) {
            var0 = null;
         }

         return new RegistryUtil.DefaultRegistryLookup(var0);
      });
      return var1.find(var0);
   }

   private interface RegistryLookup {
      @Nullable
      <T extends Keyed> Registry<T> find(@NonNull Class<T> type);
   }

   private static class DefaultRegistryLookup implements RegistryUtil.RegistryLookup {
      private final RegistryUtil.RegistryLookup bukkit;
      private final Map<Type, Object> registries;

      private DefaultRegistryLookup(RegistryUtil.RegistryLookup bukkit) {
         this.bukkit = var1;
         this.registries = (Map)Arrays.stream(Registry.class.getDeclaredFields()).filter((var0) -> {
            return Modifier.isPublic(var0.getModifiers()) && Modifier.isStatic(var0.getModifiers());
         }).filter((var0) -> {
            return Registry.class.isAssignableFrom(var0.getType());
         }).map((var0) -> {
            Type var1 = ((ParameterizedType)var0.getGenericType()).getActualTypeArguments()[0];

            try {
               return new Pair(var1, var0.get((Object)null));
            } catch (Throwable var3) {
               return null;
            }
         }).filter(Objects::nonNull).collect(Collectors.toMap(Pair::getA, Pair::getB, (var0, var1x) -> {
            return var0;
         }));
      }

      @Nullable
      public <T extends Keyed> Registry<T> find(@NonNull Class<T> type) {
         if (var1 == null) {
            throw new NullPointerException("type is marked non-null but is null");
         } else if (this.bukkit == null) {
            return (Registry)this.registries.get(var1);
         } else {
            try {
               return this.bukkit.find(var1);
            } catch (Throwable var3) {
               return (Registry)this.registries.get(var1);
            }
         }
      }
   }
}
