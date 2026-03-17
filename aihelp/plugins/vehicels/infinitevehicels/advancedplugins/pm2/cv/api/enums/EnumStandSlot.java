package advancedplugins.pm2.cv.api.enums;

import org.bukkit.inventory.EquipmentSlot;

public enum EnumStandSlot {
   HEAD,
   LEFT_HAND,
   RIGHT_HAND;

   public EquipmentSlot toEquipmentSlot() {
      switch(this.ordinal()) {
      case 0:
         return EquipmentSlot.HEAD;
      case 1:
         return EquipmentSlot.OFF_HAND;
      case 2:
         return EquipmentSlot.HAND;
      default:
         throw new IllegalStateException();
      }
   }

   public EnumRotableLimb toRotableLimb() {
      switch(this.ordinal()) {
      case 0:
         return EnumRotableLimb.HEAD;
      case 1:
         return EnumRotableLimb.LEFT_ARM;
      case 2:
         return EnumRotableLimb.RIGHT_ARM;
      default:
         throw new IllegalStateException();
      }
   }

   // $FF: synthetic method
   private static EnumStandSlot[] $values() {
      return new EnumStandSlot[]{HEAD, LEFT_HAND, RIGHT_HAND};
   }
}
