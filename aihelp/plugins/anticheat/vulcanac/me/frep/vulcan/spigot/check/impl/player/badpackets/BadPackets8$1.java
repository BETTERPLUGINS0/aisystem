package me.frep.vulcan.spigot.check.impl.player.badpackets;

import com.github.retrooper.packetevents.protocol.player.DiggingAction;

// $FF: synthetic class
class BadPackets8$1 {
   static final int[] Vulcan_m = new int[DiggingAction.values().length];

   static {
      try {
         Vulcan_m[DiggingAction.DROP_ITEM.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         Vulcan_m[DiggingAction.DROP_ITEM_STACK.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         Vulcan_m[DiggingAction.RELEASE_USE_ITEM.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }

      try {
         Vulcan_m[DiggingAction.SWAP_ITEM_WITH_OFFHAND.ordinal()] = 4;
      } catch (NoSuchFieldError var2) {
      }

      try {
         Vulcan_m[DiggingAction.CANCELLED_DIGGING.ordinal()] = 5;
      } catch (NoSuchFieldError var1) {
      }

   }
}
