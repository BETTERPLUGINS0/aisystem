package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.debug.Debug;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.ToIntFunction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class TeleportHelper {
   private static final Set<Material> AVOIDED_BLOCK_TYPES = new HashSet();
   public static final TeleportHelper DEFAULT = new TeleportHelper(3, 2);
   private final BlockLocation[] blockOffsets;

   public TeleportHelper(int radiusXZ, int radiusY) {
      Validate.isTrue(radiusXZ >= 0, "radiusXZ cannot be negative");
      Validate.isTrue(radiusY >= 0, "radiusY cannot be negative");
      List<BlockLocation> blockOffsetsList = new ArrayList();

      for(int x = -radiusXZ; x <= radiusXZ; ++x) {
         for(int z = -radiusXZ; z <= radiusXZ; ++z) {
            for(int y = -radiusY; y <= radiusY; ++y) {
               blockOffsetsList.add(new BlockLocation(x, y, z));
            }
         }
      }

      this.blockOffsets = (BlockLocation[])blockOffsetsList.toArray(new BlockLocation[0]);
   }

   private int getSortKey(BlockLocation location) {
      int x = location.getX();
      int y = location.getY();
      int z = location.getZ();
      return x * x + y * y + z * z;
   }

   @Nullable
   public Location findSafeDestination(Location destination, Entity entity, ToIntFunction<BlockLocation> preference) {
      Validate.notNull(destination, (String)"destination");
      World world = null;
      Validate.isTrue(destination.isWorldLoaded() && (world = destination.getWorld()) != null, "destination world not loaded");

      assert world != null;

      Validate.notNull(preference, (String)"preference");
      Arrays.sort(this.blockOffsets, Comparator.comparingInt(preference).thenComparingInt(this::getSortKey));
      int destinationX = destination.getBlockX();
      int destinationY = destination.getBlockY();
      int destinationZ = destination.getBlockZ();
      BoundingBox boundingBox = entity.getBoundingBox();

      for(int i = 0; i < this.blockOffsets.length; ++i) {
         BlockLocation offset = this.blockOffsets[i];
         if (preference.applyAsInt(offset) != Integer.MAX_VALUE) {
            Block block = offset.getBlockAtOffset(world, destinationX, destinationY, destinationZ);
            Location teleportLocation = this.getSafeTeleportLocation(block, entity, boundingBox);
            if (teleportLocation != null) {
               return teleportLocation;
            }
         }
      }

      return null;
   }

   private static void debugUnsafeTeleportLocation(Block block, String message) {
      if (Debug.isDebugging(DebugOptions.unsafeTeleports)) {
         String var10000 = TextUtils.getLocationString(block);
         Log.info("Unsafe teleport location " + var10000 + ": " + message);
      }

   }

   @Nullable
   private Location getSafeTeleportLocation(Block block, Entity entity, BoundingBox boundingBox) {
      Block blockBelow = block.getRelative(0, -1, 0);
      if (blockBelow.getType().isAir()) {
         debugUnsafeTeleportLocation(block, "Block below is air!");
         return null;
      } else if (AVOIDED_BLOCK_TYPES.contains(blockBelow.getType())) {
         debugUnsafeTeleportLocation(block, "Block below is avoided!");
         return null;
      } else if (block.isLiquid()) {
         debugUnsafeTeleportLocation(block, "Block is liquid!");
         return null;
      } else if (blockBelow.isLiquid()) {
         debugUnsafeTeleportLocation(block, "Block below is liquid!");
         return null;
      } else if (!WorldUtils.isBlockInsideWorldHeightBounds(block)) {
         debugUnsafeTeleportLocation(block, "Block is outside of world height bounds!");
         return null;
      } else if (!WorldUtils.isBlockInsideWorldBorder(block)) {
         debugUnsafeTeleportLocation(block, "Block is outside of world border!");
         return null;
      } else {
         Location location = EntityUtils.getStandingLocation(entity.getType(), block);
         if (location == null) {
            debugUnsafeTeleportLocation(block, "No block to stand on!");
            return null;
         } else {
            double bbShiftX = 0.5D - boundingBox.getCenterX();
            double bbShiftY = location.getY() - (double)location.getBlockY() - boundingBox.getMinY();
            double bbShiftZ = 0.5D - boundingBox.getCenterZ();
            boundingBox.shift(bbShiftX, bbShiftY, bbShiftZ);
            int blockHeight = (int)Math.ceil(boundingBox.getHeight());

            for(int modY = 0; modY < blockHeight; ++modY) {
               if (modY != 0) {
                  boundingBox.shift(0.0D, -1.0D, 0.0D);
               }

               Block blockInBoundingBox = block.getRelative(0, modY, 0);
               if (blockInBoundingBox.isLiquid()) {
                  debugUnsafeTeleportLocation(block, "Block above is liquid!");
                  return null;
               }

               if (Debug.isDebugging(DebugOptions.unsafeTeleports)) {
                  String var10000 = Arrays.toString(blockInBoundingBox.getCollisionShape().getBoundingBoxes().toArray());
                  Log.info("Block shape: " + var10000 + " | Entity bounding box: " + boundingBox.toString());
               }

               if (blockInBoundingBox.getCollisionShape().overlaps(boundingBox)) {
                  debugUnsafeTeleportLocation(block, "Not enough space available!");
                  return null;
               }
            }

            return location;
         }
      }
   }

   static {
      AVOIDED_BLOCK_TYPES.add(Material.CACTUS);
      AVOIDED_BLOCK_TYPES.add(Material.CAMPFIRE);
      AVOIDED_BLOCK_TYPES.add(Material.FIRE);
      AVOIDED_BLOCK_TYPES.add(Material.MAGMA_BLOCK);
      AVOIDED_BLOCK_TYPES.add(Material.SOUL_CAMPFIRE);
      AVOIDED_BLOCK_TYPES.add(Material.SOUL_FIRE);
      AVOIDED_BLOCK_TYPES.add(Material.SWEET_BERRY_BUSH);
      AVOIDED_BLOCK_TYPES.add(Material.WITHER_ROSE);
      AVOIDED_BLOCK_TYPES.add(Material.LAVA);
      AVOIDED_BLOCK_TYPES.add(Material.WATER);
      AVOIDED_BLOCK_TYPES.add(Material.END_PORTAL);
      AVOIDED_BLOCK_TYPES.add(Material.NETHER_PORTAL);
      AVOIDED_BLOCK_TYPES.add(Material.FARMLAND);
   }
}
