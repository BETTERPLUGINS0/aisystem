package me.frep.vulcan.spigot;

import org.bukkit.entity.EntityType;

// $FF: synthetic class
class Vulcan_Xb {
   static final int[] Vulcan_N = new int[EntityType.values().length];

   static {
      try {
         Vulcan_N[EntityType.VILLAGER.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         Vulcan_N[EntityType.COW.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         Vulcan_N[EntityType.CHICKEN.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }

      try {
         Vulcan_N[EntityType.WOLF.ordinal()] = 4;
      } catch (NoSuchFieldError var2) {
      }

      try {
         Vulcan_N[EntityType.ITEM_FRAME.ordinal()] = 5;
      } catch (NoSuchFieldError var1) {
      }

   }
}
