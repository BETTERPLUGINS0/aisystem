package ac.grim.grimac.utils.inventory;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;

public enum EquipmentType {
   MAINHAND,
   OFFHAND,
   FEET,
   LEGS,
   CHEST,
   HEAD;

   public static EquipmentType byArmorID(int id) {
      EquipmentType var10000;
      switch(id) {
      case 0:
         var10000 = HEAD;
         break;
      case 1:
         var10000 = CHEST;
         break;
      case 2:
         var10000 = LEGS;
         break;
      case 3:
         var10000 = FEET;
         break;
      default:
         var10000 = MAINHAND;
      }

      return var10000;
   }

   public static EquipmentType getEquipmentSlotForItem(ItemStack itemStack) {
      ItemType item = itemStack.getType();
      if (item == ItemTypes.CARVED_PUMPKIN || item.getName().getKey().contains("SKULL") || item.getName().getKey().contains("HEAD") && !item.getName().getKey().contains("PISTON")) {
         return HEAD;
      } else if (item == ItemTypes.ELYTRA) {
         return CHEST;
      } else if (item != ItemTypes.LEATHER_BOOTS && item != ItemTypes.CHAINMAIL_BOOTS && item != ItemTypes.IRON_BOOTS && item != ItemTypes.DIAMOND_BOOTS && item != ItemTypes.GOLDEN_BOOTS && item != ItemTypes.NETHERITE_BOOTS && item != ItemTypes.COPPER_BOOTS) {
         if (item != ItemTypes.LEATHER_LEGGINGS && item != ItemTypes.CHAINMAIL_LEGGINGS && item != ItemTypes.IRON_LEGGINGS && item != ItemTypes.DIAMOND_LEGGINGS && item != ItemTypes.GOLDEN_LEGGINGS && item != ItemTypes.NETHERITE_LEGGINGS && item != ItemTypes.COPPER_LEGGINGS) {
            if (item != ItemTypes.LEATHER_CHESTPLATE && item != ItemTypes.CHAINMAIL_CHESTPLATE && item != ItemTypes.IRON_CHESTPLATE && item != ItemTypes.DIAMOND_CHESTPLATE && item != ItemTypes.GOLDEN_CHESTPLATE && item != ItemTypes.NETHERITE_CHESTPLATE && item != ItemTypes.COPPER_CHESTPLATE) {
               if (item != ItemTypes.LEATHER_HELMET && item != ItemTypes.CHAINMAIL_HELMET && item != ItemTypes.IRON_HELMET && item != ItemTypes.DIAMOND_HELMET && item != ItemTypes.GOLDEN_HELMET && item != ItemTypes.NETHERITE_HELMET && item != ItemTypes.COPPER_HELMET && item != ItemTypes.TURTLE_HELMET) {
                  return ItemTypes.SHIELD == item ? OFFHAND : MAINHAND;
               } else {
                  return HEAD;
               }
            } else {
               return CHEST;
            }
         } else {
            return LEGS;
         }
      } else {
         return FEET;
      }
   }

   public boolean isArmor() {
      return this == FEET || this == LEGS || this == CHEST || this == HEAD;
   }

   public int getIndex() {
      byte var10000;
      switch(this.ordinal()) {
      case 0:
      case 2:
         var10000 = 0;
         break;
      case 1:
      case 3:
         var10000 = 1;
         break;
      case 4:
         var10000 = 2;
         break;
      case 5:
         var10000 = 3;
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var10000;
   }

   // $FF: synthetic method
   private static EquipmentType[] $values() {
      return new EquipmentType[]{MAINHAND, OFFHAND, FEET, LEGS, CHEST, HEAD};
   }
}
