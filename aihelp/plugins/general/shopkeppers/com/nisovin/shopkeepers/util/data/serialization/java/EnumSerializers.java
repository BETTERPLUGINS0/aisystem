package com.nisovin.shopkeepers.util.data.serialization.java;

import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class EnumSerializers {
   public static <E extends Enum<E>> DataSerializer<E> strict(Class<E> enumType) {
      return new EnumSerializers.EnumSerializer<E>(enumType) {
         public E deserialize(Object data) throws InvalidDataException {
            String valueName = this.deserializeEnumValueName(data);
            E value = EnumUtils.valueOf(this.enumType, valueName);
            if (value == null) {
               throw this.unknownEnumValueError(valueName);
            } else {
               return value;
            }
         }
      };
   }

   public static <E extends Enum<E>> DataSerializer<E> lenient(Class<E> enumType) {
      return new EnumSerializers.EnumSerializer<E>(enumType) {
         public E deserialize(Object data) throws InvalidDataException {
            String valueName = this.deserializeEnumValueName(data);
            E value = ConversionUtils.parseEnum(this.enumType, valueName);
            if (value == null) {
               throw this.unknownEnumValueError(valueName);
            } else {
               return value;
            }
         }
      };
   }

   private EnumSerializers() {
   }

   public abstract static class EnumSerializer<E extends Enum<E>> implements DataSerializer<E> {
      protected final Class<E> enumType;

      public EnumSerializer(Class<E> enumType) {
         Validate.notNull(enumType, (String)"enumType is null");
         this.enumType = enumType;
      }

      protected final String deserializeEnumValueName(Object data) throws InvalidDataException {
         return (String)StringSerializers.STRICT_NON_EMPTY.deserialize(data);
      }

      protected InvalidDataException unknownEnumValueError(String valueName) {
         assert valueName != null;

         String var10002 = this.enumType.getSimpleName();
         return new InvalidDataException("Unknown " + var10002 + ": " + valueName);
      }

      @Nullable
      public Object serialize(E value) {
         Validate.notNull(value, (String)"value is null");
         return value.name();
      }
   }
}
