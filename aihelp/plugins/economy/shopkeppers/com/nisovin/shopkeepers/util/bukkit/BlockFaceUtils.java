package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public final class BlockFaceUtils {
   public static List<? extends BlockFace> getBlockSides() {
      return BlockFaceUtils.BlockFaceDirections.BLOCK_SIDES.getBlockFaces();
   }

   public static boolean isBlockSide(BlockFace blockFace) {
      return BlockFaceUtils.BlockFaceDirections.BLOCK_SIDES.contains(blockFace);
   }

   public static float getYaw(BlockFace blockFace) {
      Validate.notNull(blockFace, (String)"blockFace is null");
      List<? extends BlockFace> horizontalBlockFaces = BlockFaceUtils.BlockFaceDirections.SECONDARY_INTERCARDINAL.getBlockFaces();
      int blockFaceIndex = horizontalBlockFaces.indexOf(blockFace);
      Validate.isTrue(blockFaceIndex != -1, "blockFace is not horizontal: " + String.valueOf(blockFace));
      float anglePerBlockFace = 360.0F / (float)horizontalBlockFaces.size();
      return (float)blockFaceIndex * anglePerBlockFace;
   }

   public static BlockFaceUtils.BlockFaceDirections getWallSignFacings() {
      return BlockFaceUtils.BlockFaceDirections.CARDINAL;
   }

   public static BlockFace toWallSignFacing(Vector direction) {
      Validate.notNull(direction, (String)"direction is null");
      return getAxisAlignedBlockFace(direction.getX(), 0.0D, direction.getZ());
   }

   public static boolean isWallSignFacing(BlockFace blockFace) {
      return getWallSignFacings().contains(blockFace);
   }

   public static BlockFaceUtils.BlockFaceDirections getSignPostFacings() {
      return BlockFaceUtils.BlockFaceDirections.SECONDARY_INTERCARDINAL;
   }

   public static boolean isSignPostFacing(BlockFace blockFace) {
      return getSignPostFacings().contains(blockFace);
   }

   public static BlockFace getAxisAlignedBlockFace(double modX, double modY, double modZ) {
      double xAbs = Math.abs(modX);
      double yAbs = Math.abs(modY);
      double zAbs = Math.abs(modZ);
      if (xAbs >= zAbs) {
         if (xAbs >= yAbs) {
            if (modX >= 0.0D) {
               return BlockFace.EAST.getModX() == 1 ? BlockFace.EAST : BlockFace.WEST;
            } else {
               return BlockFace.EAST.getModX() == 1 ? BlockFace.WEST : BlockFace.EAST;
            }
         } else {
            return modY >= 0.0D ? BlockFace.UP : BlockFace.DOWN;
         }
      } else if (zAbs >= yAbs) {
         if (modZ >= 0.0D) {
            return BlockFace.SOUTH.getModZ() == 1 ? BlockFace.SOUTH : BlockFace.NORTH;
         } else {
            return BlockFace.SOUTH.getModZ() == 1 ? BlockFace.NORTH : BlockFace.SOUTH;
         }
      } else {
         return modY >= 0.0D ? BlockFace.UP : BlockFace.DOWN;
      }
   }

   private BlockFaceUtils() {
   }

   public static enum BlockFaceDirections {
      BLOCK_SIDES(Arrays.asList(BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST, BlockFace.UP, BlockFace.DOWN)),
      CARDINAL(Arrays.asList(BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST)),
      INTERCARDINAL(Arrays.asList(BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST)),
      SECONDARY_INTERCARDINAL(Arrays.asList(BlockFace.SOUTH, BlockFace.SOUTH_SOUTH_WEST, BlockFace.SOUTH_WEST, BlockFace.WEST_SOUTH_WEST, BlockFace.WEST, BlockFace.WEST_NORTH_WEST, BlockFace.NORTH_WEST, BlockFace.NORTH_NORTH_WEST, BlockFace.NORTH, BlockFace.NORTH_NORTH_EAST, BlockFace.NORTH_EAST, BlockFace.EAST_NORTH_EAST, BlockFace.EAST, BlockFace.EAST_SOUTH_EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_SOUTH_EAST));

      private final List<? extends BlockFace> blockFaces;

      private BlockFaceDirections(List<? extends BlockFace> param3) {
         this.blockFaces = Collections.unmodifiableList(blockFaces);
      }

      public final List<? extends BlockFace> getBlockFaces() {
         return this.blockFaces;
      }

      public final boolean contains(BlockFace blockFace) {
         return this.blockFaces.contains(blockFace);
      }

      public BlockFace fromYaw(float yaw) {
         if (this == BLOCK_SIDES) {
            return CARDINAL.fromYaw(yaw);
         } else {
            int blockFaceCount = this.blockFaces.size();
            float anglePerBlockFace = 360.0F / (float)blockFaceCount;
            int blockFaceIndex = Math.round(yaw / anglePerBlockFace) % blockFaceCount;
            if (blockFaceIndex < 0) {
               blockFaceIndex += blockFaceCount;
            }

            return (BlockFace)this.blockFaces.get(blockFaceIndex);
         }
      }

      // $FF: synthetic method
      private static BlockFaceUtils.BlockFaceDirections[] $values() {
         return new BlockFaceUtils.BlockFaceDirections[]{BLOCK_SIDES, CARDINAL, INTERCARDINAL, SECONDARY_INTERCARDINAL};
      }
   }
}
