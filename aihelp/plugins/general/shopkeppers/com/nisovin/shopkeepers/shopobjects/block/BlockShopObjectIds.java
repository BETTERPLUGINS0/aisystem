package com.nisovin.shopkeepers.shopobjects.block;

import com.nisovin.shopkeepers.util.bukkit.BlockLocation;
import com.nisovin.shopkeepers.util.bukkit.MutableBlockLocation;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.block.Block;

public final class BlockShopObjectIds {
   private static final MutableBlockLocation sharedBlockLocation = new MutableBlockLocation();

   public static Object getObjectId(Block block) {
      Validate.notNull(block, (String)"block is null");
      return getObjectId(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
   }

   public static Object getObjectId(String worldName, int blockX, int blockY, int blockZ) {
      Validate.notEmpty(worldName, "worldName is null or empty");
      return new BlockLocation(worldName, blockX, blockY, blockZ);
   }

   public static Object getSharedObjectId(Block block) {
      Validate.notNull(block, (String)"block is null");
      return getSharedObjectId(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
   }

   public static Object getSharedObjectId(String worldName, int blockX, int blockY, int blockZ) {
      Validate.notEmpty(worldName, "worldName is null or empty");
      sharedBlockLocation.set(worldName, blockX, blockY, blockZ);
      return sharedBlockLocation;
   }

   private BlockShopObjectIds() {
   }
}
