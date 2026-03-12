package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Set;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public final class WorldUtils {
   private static final Location TEMP_START_LOCATION = new Location((World)null, 0.0D, 0.0D, 0.0D);
   private static final Vector TEMP_START_POSITION = new Vector();
   private static final Vector DOWN_DIRECTION = new Vector(0.0D, -1.0D, 0.0D);
   private static final double RAY_TRACE_OFFSET = 0.01D;

   public static double getCollisionDistanceToGround(Location startLocation, double maxDistance, Set<? extends Material> collidableFluids) {
      Validate.notNull(startLocation, (String)"startLocation is null");
      Validate.notNull(collidableFluids, (String)"collidableFluids is null");
      World world = startLocation.getWorld();
      Validate.notNull(world, (String)"startLocation's world is null");

      assert world != null;

      TEMP_START_LOCATION.setWorld(world);
      TEMP_START_LOCATION.setX(startLocation.getX());
      TEMP_START_LOCATION.setY(startLocation.getY() + 0.01D);
      TEMP_START_LOCATION.setZ(startLocation.getZ());
      TEMP_START_POSITION.setX(TEMP_START_LOCATION.getX());
      TEMP_START_POSITION.setY(TEMP_START_LOCATION.getY());
      TEMP_START_POSITION.setZ(TEMP_START_LOCATION.getZ());
      double offsetMaxDistance = maxDistance + 0.01D;
      RayTraceResult rayTraceResult = null;
      if (collidableFluids.isEmpty()) {
         rayTraceResult = world.rayTraceBlocks(TEMP_START_LOCATION, DOWN_DIRECTION, offsetMaxDistance, FluidCollisionMode.NEVER, true);
      } else {
         int offsetMaxDistanceBlocks = NumberConversions.ceil(offsetMaxDistance);
         BlockIterator blockIterator = new BlockIterator(world, TEMP_START_POSITION, DOWN_DIRECTION, 0.0D, offsetMaxDistanceBlocks);

         label36:
         do {
            Block block;
            do {
               if (!blockIterator.hasNext()) {
                  break label36;
               }

               block = blockIterator.next();
            } while(block.isPassable() && !collidableFluids.contains(block.getType()));

            rayTraceResult = block.rayTrace(TEMP_START_LOCATION, DOWN_DIRECTION, offsetMaxDistance, FluidCollisionMode.ALWAYS);
         } while(rayTraceResult == null);
      }

      TEMP_START_LOCATION.setWorld((World)null);
      double distanceToGround;
      if (rayTraceResult == null) {
         distanceToGround = maxDistance;
      } else {
         distanceToGround = TEMP_START_POSITION.distance(rayTraceResult.getHitPosition()) - 0.01D;
         if (distanceToGround < 0.0D) {
            distanceToGround = 0.0D;
         }
      }

      return distanceToGround;
   }

   public static boolean isBlockInsideWorldBorder(Block block) {
      return isBlockInsideWorldBorder(block.getWorld(), block.getX(), block.getZ());
   }

   public static boolean isBlockInsideWorldBorder(World world, int x, int z) {
      WorldBorder worldBorder = world.getWorldBorder();
      Location center = worldBorder.getCenter();
      int radius = (int)(worldBorder.getSize() / 2.0D);
      int centerX = center.getBlockX();
      int centerZ = center.getBlockZ();
      int minX = centerX - radius;
      int maxX = centerX + radius;
      int minZ = centerZ - radius;
      int maxZ = centerZ + radius;
      return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
   }

   public static boolean isBlockInsideWorldHeightBounds(Block block) {
      return isBlockInsideWorldHeightBounds(block.getWorld(), block.getY());
   }

   public static boolean isBlockInsideWorldHeightBounds(World world, int y) {
      int maxY = world.getMaxHeight() - 1;
      int minY = world.getMinHeight();
      return y >= minY && y <= maxY;
   }

   private WorldUtils() {
   }
}
