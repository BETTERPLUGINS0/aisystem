package com.volmit.iris.util.plugin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Chunks {
   public static boolean isSafe(World w, int x, int z) {
      return var0.isChunkLoaded(var1, var2) && var0.isChunkLoaded(var1 + 1, var2) && var0.isChunkLoaded(var1, var2 + 1) && var0.isChunkLoaded(var1 - 1, var2) && var0.isChunkLoaded(var1, var2 - 1) && var0.isChunkLoaded(var1 - 1, var2 - 1) && var0.isChunkLoaded(var1 + 1, var2 + 1) && var0.isChunkLoaded(var1 + 1, var2 - 1) && var0.isChunkLoaded(var1 - 1, var2 + 1);
   }

   public static boolean isSafe(Location l) {
      return isSafe(var0.getWorld(), var0.getBlockX() >> 4, var0.getBlockZ() >> 4);
   }

   public static boolean hasPlayersNearby(Location at) {
      try {
         return !var0.getWorld().getNearbyEntities(var0, 32.0D, 32.0D, 32.0D, (var0x) -> {
            return var0x instanceof Player;
         }).isEmpty();
      } catch (Throwable var2) {
         return false;
      }
   }
}
