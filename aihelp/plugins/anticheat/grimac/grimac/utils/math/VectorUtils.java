package ac.grim.grimac.utils.math;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import lombok.Generated;

public final class VectorUtils {
   @NotNull
   public static Vector3dm cutBoxToVector(@NotNull Vector3dm vectorToCutTo, @NotNull Vector3dm min, @NotNull Vector3dm max) {
      SimpleCollisionBox box = (new SimpleCollisionBox(min, max)).sort();
      return cutBoxToVector(vectorToCutTo, box);
   }

   @Contract("_, _ -> new")
   @NotNull
   public static Vector3dm cutBoxToVector(@NotNull Vector3dm vectorCutTo, @NotNull SimpleCollisionBox box) {
      return cutBoxToVector(vectorCutTo.getX(), vectorCutTo.getY(), vectorCutTo.getZ(), box);
   }

   @NotNull
   public static Vector3dm cutBoxToVector(double x, double y, double z, @NotNull SimpleCollisionBox box) {
      return new Vector3dm(GrimMath.clamp(x, box.minX, box.maxX), GrimMath.clamp(y, box.minY, box.maxY), GrimMath.clamp(z, box.minZ, box.maxZ));
   }

   @Contract("_ -> new")
   @NotNull
   public static Vector3dm fromVec3d(@NotNull Vector3d vector3d) {
      return new Vector3dm(vector3d.getX(), vector3d.getY(), vector3d.getZ());
   }

   @Contract("_ -> new")
   @NotNull
   public static Vector3d clampVector(@NotNull Vector3d toClamp) {
      double x = GrimMath.clamp(toClamp.getX(), -3.0E7D, 3.0E7D);
      double y = GrimMath.clamp(toClamp.getY(), -2.0E7D, 2.0E7D);
      double z = GrimMath.clamp(toClamp.getZ(), -3.0E7D, 3.0E7D);
      return new Vector3d(x, y, z);
   }

   @Generated
   private VectorUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
