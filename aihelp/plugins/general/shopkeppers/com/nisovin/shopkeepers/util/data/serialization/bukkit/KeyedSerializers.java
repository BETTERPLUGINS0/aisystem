package com.nisovin.shopkeepers.util.data.serialization.bukkit;

import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class KeyedSerializers {
   public static <E extends Keyed> DataSerializer<E> forResolver(Class<E> keyedType, KeyedSerializers.KeyedResolver<E> resolver) {
      return new KeyedSerializers.KeyedSerializer(keyedType, resolver);
   }

   public static <E extends Keyed> DataSerializer<E> forRegistry(Class<E> keyedType) {
      Registry<E> registry = Compat.getProvider().getRegistry(keyedType);
      return forResolver(keyedType, (key) -> {
         return registry.get(key);
      });
   }

   private KeyedSerializers() {
   }

   private static class KeyedSerializer<E extends Keyed> implements DataSerializer<E> {
      private final Class<E> keyedType;
      private final KeyedSerializers.KeyedResolver<E> resolver;

      private KeyedSerializer(Class<E> keyedType, KeyedSerializers.KeyedResolver<E> resolver) {
         Validate.notNull(keyedType, (String)"keyedType is null");
         Validate.notNull(resolver, (String)"resolver is null");
         this.keyedType = keyedType;
         this.resolver = resolver;
      }

      @Nullable
      public Object serialize(@NonNull E value) {
         Validate.notNull(value, (String)"value is null");
         return value.getKey().toString();
      }

      public E deserialize(Object data) throws InvalidDataException {
         NamespacedKey key = (NamespacedKey)NamespacedKeySerializers.DEFAULT.deserialize(data);
         E value = this.resolver.resolve(key);
         if (value == null) {
            String var10002 = this.keyedType.getSimpleName();
            throw new InvalidDataException("Unknown " + var10002 + ": " + key.toString());
         } else {
            return value;
         }
      }
   }

   public interface KeyedResolver<E extends Keyed> {
      @Nullable
      E resolve(NamespacedKey var1);
   }
}
