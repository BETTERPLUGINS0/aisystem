package ac.grim.grimac.utils.blockstate.helper;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import lombok.Generated;

public final class BlockFaceHelper {
   @Contract(
      pure = true
   )
   public static boolean isFaceVertical(@Nullable BlockFace face) {
      return face == BlockFace.UP || face == BlockFace.DOWN;
   }

   @Contract(
      pure = true
   )
   public static boolean isFaceHorizontal(@Nullable BlockFace face) {
      return face == BlockFace.NORTH || face == BlockFace.EAST || face == BlockFace.SOUTH || face == BlockFace.WEST;
   }

   @Contract(
      pure = true
   )
   public static BlockFace getClockWise(@NotNull BlockFace face) {
      BlockFace var10000;
      switch(face) {
      case NORTH:
         var10000 = BlockFace.EAST;
         break;
      case SOUTH:
         var10000 = BlockFace.WEST;
         break;
      case WEST:
         var10000 = BlockFace.NORTH;
         break;
      default:
         var10000 = BlockFace.SOUTH;
      }

      return var10000;
   }

   @Contract(
      pure = true
   )
   public static BlockFace getPEClockWise(@NotNull BlockFace face) {
      BlockFace var10000;
      switch(face) {
      case NORTH:
         var10000 = BlockFace.EAST;
         break;
      case SOUTH:
         var10000 = BlockFace.WEST;
         break;
      case WEST:
         var10000 = BlockFace.NORTH;
         break;
      default:
         var10000 = BlockFace.SOUTH;
      }

      return var10000;
   }

   @Contract(
      pure = true
   )
   public static BlockFace getCounterClockwise(@NotNull BlockFace face) {
      BlockFace var10000;
      switch(face) {
      case NORTH:
         var10000 = BlockFace.WEST;
         break;
      case SOUTH:
         var10000 = BlockFace.EAST;
         break;
      case WEST:
         var10000 = BlockFace.SOUTH;
         break;
      default:
         var10000 = BlockFace.NORTH;
      }

      return var10000;
   }

   @Generated
   private BlockFaceHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
