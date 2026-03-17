package com.bergerkiller.bukkit.tc.utils;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import java.util.function.LongUnaryOperator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class LongBlockCoordinates {
   public static final int PACKED_X_LENGTH = 26;
   public static final int PACKED_Z_LENGTH = 26;
   public static final int PACKED_Y_LENGTH = 12;
   public static final long PACKED_X_MASK = 67108863L;
   public static final long PACKED_Y_MASK = 4095L;
   public static final long PACKED_Z_MASK = 67108863L;
   public static final int Y_OFFSET = 0;
   public static final int Z_OFFSET = 12;
   public static final int X_OFFSET = 38;

   public static long map(Block block) {
      return map(block.getX(), block.getY(), block.getZ());
   }

   public static long map(int x, int y, int z) {
      long l = 0L;
      l |= ((long)x & 67108863L) << 38;
      l |= ((long)y & 4095L) << 0;
      l |= ((long)z & 67108863L) << 12;
      return l;
   }

   public static long shiftEast(long key) {
      return key & 274877906943L | key + 274877906944L & -274877906944L;
   }

   public static long shiftWest(long key) {
      return key & 274877906943L | key - 274877906944L & -274877906944L;
   }

   public static long shiftUp(long key) {
      return key & -4096L | key + 1L & 4095L;
   }

   public static long shiftDown(long key) {
      return key & -4096L | key - 1L & 4095L;
   }

   public static long shiftSouth(long key) {
      return key & -274877902849L | key + 4096L & 274877902848L;
   }

   public static long shiftNorth(long key) {
      return key & -274877902849L | key - 4096L & 274877902848L;
   }

   public static LongUnaryOperator shiftOperator(BlockFace face) {
      switch(face) {
      case DOWN:
         return LongBlockCoordinates::shiftDown;
      case UP:
         return LongBlockCoordinates::shiftUp;
      case NORTH:
         return LongBlockCoordinates::shiftNorth;
      case EAST:
         return LongBlockCoordinates::shiftEast;
      case SOUTH:
         return LongBlockCoordinates::shiftSouth;
      case WEST:
         return LongBlockCoordinates::shiftWest;
      case SELF:
         return LongUnaryOperator.identity();
      default:
         return (key) -> {
            return map(getX(key) + face.getModX(), getY(key) + face.getModY(), getZ(key) + face.getModZ());
         };
      }
   }

   public static void forAllBlockSidesAndSelf(long key, LongBlockCoordinates.BlockSideConsumer consumer) {
      consumer.accept(BlockFace.SELF, key);
      consumer.accept(BlockFace.NORTH, shiftNorth(key));
      consumer.accept(BlockFace.EAST, shiftEast(key));
      consumer.accept(BlockFace.SOUTH, shiftSouth(key));
      consumer.accept(BlockFace.WEST, shiftWest(key));
      consumer.accept(BlockFace.UP, shiftUp(key));
      consumer.accept(BlockFace.DOWN, shiftDown(key));
   }

   public static BlockFace findDirection(long from, long to) {
      long diff = to - (from & -274877906944L) & -274877906944L | to - (from & 4095L) & 4095L | to - (from & 274877902848L) & 274877902848L;
      switch((int)(diff ^ diff >> 32)) {
      case 0:
         if (diff == 0L) {
            return BlockFace.SELF;
         }
      case -4033:
         if (diff == 274877902848L) {
            return BlockFace.NORTH;
         }
      case 64:
         if (diff == 274877906944L) {
            return BlockFace.EAST;
         }
      case 4096:
         if (diff == 4096L) {
            return BlockFace.SOUTH;
         }
      case -64:
         if (diff == -274877906944L) {
            return BlockFace.WEST;
         }
      case 1:
         if (diff == 1L) {
            return BlockFace.UP;
         }
      case 4095:
         if (diff == 4095L) {
            return BlockFace.DOWN;
         }
      default:
         return null;
      }
   }

   public static int getChunkEdgeDistance(long key) {
      int relx = (int)(key >> 38) & 15;
      int relz = (int)(key >> 12) & 15;
      if ((relx & 8) != 0) {
         relx = 15 - relx;
      }

      if ((relz & 8) != 0) {
         relz = 15 - relz;
      }

      return Math.min(relx, relz);
   }

   public static int getX(long i) {
      return (int)(i << 0 >> 38);
   }

   public static int getY(long i) {
      return (int)(i << 52 >> 52);
   }

   public static int getZ(long i) {
      return (int)(i << 26 >> 38);
   }

   public static IntVector3 get(long i) {
      return new IntVector3(getX(i), getY(i), getZ(i));
   }

   @FunctionalInterface
   public interface BlockSideConsumer {
      void accept(BlockFace var1, long var2);
   }
}
