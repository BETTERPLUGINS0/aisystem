package com.nisovin.shopkeepers.util.data.serialization.bukkit;

import com.nisovin.shopkeepers.util.bukkit.MinecraftEnumUtils;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.EnumSerializers;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;

public final class MinecraftEnumSerializers {
   public static <E extends Enum<E>> DataSerializer<E> strict(Class<E> enumType) {
      return EnumSerializers.strict(enumType);
   }

   public static <E extends Enum<E>> DataSerializer<E> lenient(Class<E> enumType) {
      return new MinecraftEnumSerializers.LenientMinecraftEnumSerializer(enumType);
   }

   private MinecraftEnumSerializers() {
   }

   private static class LenientMinecraftEnumSerializer<E extends Enum<E>> extends EnumSerializers.EnumSerializer<E> {
      public LenientMinecraftEnumSerializer(Class<E> enumType) {
         super(enumType);
      }

      public E deserialize(Object data) throws InvalidDataException {
         String valueName = this.deserializeEnumValueName(data);
         E value = MinecraftEnumUtils.parseEnum(this.enumType, valueName);
         if (value == null) {
            throw this.unknownEnumValueError(valueName);
         } else {
            return value;
         }
      }
   }

   public static final class EquipmentSlots {
      public static final DataSerializer<EquipmentSlot> LENIENT = MinecraftEnumSerializers.lenient(EquipmentSlot.class);
   }

   public static final class Materials {
      public static final DataSerializer<Material> LENIENT = new MinecraftEnumSerializers.LenientMinecraftEnumSerializer<Material>(Material.class) {
         // $FF: synthetic field
         static final boolean $assertionsDisabled = !MinecraftEnumSerializers.class.desiredAssertionStatus();

         {
            super(enumType);
         }

         protected InvalidDataException unknownEnumValueError(String valueName) {
            if (!$assertionsDisabled && valueName == null) {
               throw new AssertionError();
            } else {
               return new UnknownMaterialException("Unknown material: " + valueName);
            }
         }
      };

      private Materials() {
      }
   }
}
