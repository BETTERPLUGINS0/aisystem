package ac.grim.grimac.utils.collisions;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import lombok.Generated;

public final class AxisUtil {
   @NotNull
   public static SimpleCollisionBox combine(@NotNull SimpleCollisionBox base, @NotNull SimpleCollisionBox toMerge) {
      boolean insideX = toMerge.minX <= base.minX && toMerge.maxX >= base.maxX;
      boolean insideY = toMerge.minY <= base.minY && toMerge.maxY >= base.maxY;
      boolean insideZ = toMerge.minZ <= base.minZ && toMerge.maxZ >= base.maxZ;
      if (insideX && insideY && !insideZ) {
         return new SimpleCollisionBox(base.minX, base.maxY, Math.min(base.minZ, toMerge.minZ), base.minX, base.maxY, Math.max(base.maxZ, toMerge.maxZ));
      } else if (insideX && !insideY && insideZ) {
         return new SimpleCollisionBox(base.minX, Math.min(base.minY, toMerge.minY), base.minZ, base.maxX, Math.max(base.maxY, toMerge.maxY), base.maxZ);
      } else {
         return !insideX && insideY && insideZ ? new SimpleCollisionBox(Math.min(base.minX, toMerge.maxX), base.minY, base.maxZ, Math.max(base.minX, toMerge.minX), base.minY, base.maxZ) : base;
      }
   }

   @Contract(
      pure = true
   )
   public static boolean isSameAxis(BlockFace one, BlockFace two) {
      return one == two || one == two.getOppositeFace();
   }

   @Generated
   private AxisUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
