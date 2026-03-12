package com.nisovin.shopkeepers.util.data.serialization.bukkit;

import com.nisovin.shopkeepers.util.bukkit.NamespacedKeyUtils;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.StringSerializers;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class NamespacedKeySerializers {
   public static final DataSerializer<NamespacedKey> DEFAULT = new NamespacedKeySerializers.NamespacedKeySerializer() {
      public NamespacedKey deserialize(Object data) throws InvalidDataException {
         String keyString = (String)StringSerializers.STRICT.deserialize(data);
         NamespacedKey key = NamespacedKeyUtils.parse(keyString);
         if (key == null) {
            throw new InvalidDataException("Invalid namespaced key: '" + keyString + "'");
         } else {
            return key;
         }
      }
   };

   private NamespacedKeySerializers() {
   }

   private abstract static class NamespacedKeySerializer implements DataSerializer<NamespacedKey> {
      @Nullable
      public Object serialize(NamespacedKey value) {
         Validate.notNull(value, (String)"value is null");
         return value.toString();
      }
   }
}
