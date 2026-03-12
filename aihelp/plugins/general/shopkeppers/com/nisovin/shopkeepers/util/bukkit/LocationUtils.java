package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class LocationUtils {
   public static World getWorld(Location location) {
      Validate.notNull(location, (String)"location is null");
      World world = location.getWorld();
      return (World)Validate.notNull(world, (String)"location's world is null");
   }

   public static double getDistanceSquared(Location location1, Location location2) {
      Validate.notNull(location1, (String)"location1 is null");
      Validate.notNull(location2, (String)"location2 is null");
      World world1 = location1.getWorld();
      World world2 = location2.getWorld();
      Validate.notNull(world1, (String)"World of location1 is null");
      Validate.notNull(world2, (String)"World of location2 is null");
      if (world1 != world2) {
         return Double.MAX_VALUE;
      } else {
         double dx = location1.getX() - location2.getX();
         double dy = location1.getY() - location2.getY();
         double dz = location1.getZ() - location2.getZ();
         return dx * dx + dy * dy + dz * dz;
      }
   }

   public static double getSafeDistanceSquared(@Nullable Location location1, @Nullable Location location2) {
      if (location1 != null && location2 != null) {
         if (location1.isWorldLoaded() && location2.isWorldLoaded()) {
            World world1 = location1.getWorld();
            World world2 = location2.getWorld();
            if (world1 != null && world1 == world2) {
               double dx = location1.getX() - location2.getX();
               double dy = location1.getY() - location2.getY();
               double dz = location1.getZ() - location2.getZ();
               return dx * dx + dy * dy + dz * dz;
            } else {
               return Double.MAX_VALUE;
            }
         } else {
            return Double.MAX_VALUE;
         }
      } else {
         return Double.MAX_VALUE;
      }
   }

   public static Location getBlockCenterLocation(Block block) {
      Validate.notNull(block, (String)"block is null");
      return block.getLocation().add(0.5D, 0.5D, 0.5D);
   }

   private LocationUtils() {
   }
}
