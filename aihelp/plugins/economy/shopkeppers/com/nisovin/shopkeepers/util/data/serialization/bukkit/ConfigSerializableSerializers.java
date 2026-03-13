package com.nisovin.shopkeepers.util.data.serialization.bukkit;

import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ConfigSerializableSerializers {
   public static <C extends ConfigurationSerializable> DataSerializer<C> strict(Class<C> valueType) {
      return new ConfigSerializableSerializers.ConfigSerializableSerializer(valueType);
   }

   private ConfigSerializableSerializers() {
   }

   private static class ConfigSerializableSerializer<C extends ConfigurationSerializable> implements DataSerializer<C> {
      protected final Class<C> valueType;

      public ConfigSerializableSerializer(Class<C> valueType) {
         Validate.notNull(valueType, (String)"valueType is null");
         this.valueType = valueType;
      }

      @Nullable
      public Object serialize(C value) {
         Validate.notNull(value, (String)"value is null");
         return value;
      }

      public C deserialize(Object data) throws InvalidDataException {
         if (this.valueType.isInstance(data)) {
            return (ConfigurationSerializable)this.valueType.cast(data);
         } else {
            String var10002 = this.valueType.getName();
            throw new InvalidDataException("Data is not of type '" + var10002 + "' but '" + data.getClass().getName() + "'!");
         }
      }
   }
}
