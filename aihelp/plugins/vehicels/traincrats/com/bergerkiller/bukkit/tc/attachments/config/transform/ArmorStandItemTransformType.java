package com.bergerkiller.bukkit.tc.attachments.config.transform;

import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityEquipmentHandle;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public enum ArmorStandItemTransformType {
   HEAD("head", "HEAD", false),
   SMALL_HEAD("head ⒮", "HEAD", true),
   LEFT_HAND("left hand", "OFF_HAND", false),
   SMALL_LEFT_HAND("left hand ⒮", "OFF_HAND", true),
   RIGHT_HAND("right hand", "HAND", false),
   SMALL_RIGHT_HAND("right hand ⒮", "HAND", true),
   CHEST("chest", "CHEST", false),
   SMALL_CHEST("chest ⒮", "CHEST", true),
   LEGS("legs", "LEGS", false),
   SMALL_LEGS("legs ⒮", "LEGS", true),
   FEET("feet", "FEET", false),
   SMALL_FEET("feet ⒮", "FEET", true);

   private final String name;
   private final EquipmentSlot slot;
   private final boolean small;

   private ArmorStandItemTransformType(String name, String slotName, boolean small) {
      this.name = name;
      this.small = small;
      EquipmentSlot slot = (EquipmentSlot)ParseUtil.parseEnum(EquipmentSlot.class, slotName, (Object)null);
      if (slot == null && slotName.equals("OFF_HAND")) {
         slot = (EquipmentSlot)ParseUtil.parseEnum(EquipmentSlot.class, "HAND", (Object)null);
      }

      if (slot != null) {
         this.slot = slot;
      } else {
         this.slot = EquipmentSlot.HEAD;
      }

   }

   public String toString() {
      return this.name;
   }

   public EquipmentSlot getSlot() {
      return this.slot;
   }

   public boolean isHead() {
      return this == HEAD || this == SMALL_HEAD;
   }

   public boolean isSmallArmorStand() {
      return this.small;
   }

   public boolean isLeftHand() {
      return this == LEFT_HAND || this == SMALL_LEFT_HAND;
   }

   public boolean isRightHand() {
      return this == RIGHT_HAND || this == SMALL_RIGHT_HAND;
   }

   public boolean isLeg() {
      return this == LEGS || this == SMALL_LEGS || this == FEET || this == SMALL_FEET;
   }

   public double getArmorStandHorizontalOffset() {
      switch(this) {
      case LEFT_HAND:
         return 0.3125D;
      case SMALL_LEFT_HAND:
         return 0.12D;
      case RIGHT_HAND:
         return -0.3125D;
      case SMALL_RIGHT_HAND:
         return -0.12D;
      default:
         return 0.0D;
      }
   }

   public double getArmorStandVerticalOffset() {
      switch(this) {
      case LEFT_HAND:
      case RIGHT_HAND:
         return 1.375D;
      case SMALL_LEFT_HAND:
      case SMALL_RIGHT_HAND:
         return 0.492D;
      case HEAD:
         return 1.44D;
      case SMALL_HEAD:
         return 0.73D;
      default:
         return 1.44D;
      }
   }

   public PacketPlayOutEntityEquipmentHandle createEquipmentPacket(int entityId, ItemStack item) {
      return Util.createNonPlayerEquipmentPacket(entityId, this.getSlot(), item);
   }

   public static ArmorStandItemTransformType get(String name) {
      ArmorStandItemTransformType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ArmorStandItemTransformType type = var1[var3];
         if (type.toString().equals(name)) {
            return type;
         }
      }

      return HEAD;
   }

   // $FF: synthetic method
   private static ArmorStandItemTransformType[] $values() {
      return new ArmorStandItemTransformType[]{HEAD, SMALL_HEAD, LEFT_HAND, SMALL_LEFT_HAND, RIGHT_HAND, SMALL_RIGHT_HAND, CHEST, SMALL_CHEST, LEGS, SMALL_LEGS, FEET, SMALL_FEET};
   }
}
