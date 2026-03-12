package me.frep.vulcan.spigot.check.impl.player.ghosthand;

import org.bukkit.block.BlockFace;

// $FF: synthetic class
class GhostHandA$1 {
   static final int[] Vulcan_s = new int[BlockFace.values().length];

   static {
      try {
         Vulcan_s[BlockFace.SOUTH.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         Vulcan_s[BlockFace.NORTH.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         Vulcan_s[BlockFace.EAST.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         Vulcan_s[BlockFace.WEST.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }

   }
}
