package advancedplugins.pm2.cv.models.api.utils.math;

import org.joml.Vector3d;

public enum Direction {
   NORTH(new Vector3d(0.0D, 0.0D, -1.0D), (new Vector3d(0.0D, 1.0D, 0.0D)).normalize()),
   EAST(new Vector3d(1.0D, 0.0D, 0.0D), (new Vector3d(0.0D, 1.0D, 0.0D)).normalize()),
   SOUTH(new Vector3d(0.0D, 0.0D, 1.0D), (new Vector3d(0.0D, 1.0D, 0.0D)).normalize()),
   WEST(new Vector3d(-1.0D, 0.0D, 0.0D), (new Vector3d(0.0D, 1.0D, 0.0D)).normalize()),
   UP(new Vector3d(0.0D, 1.0D, 0.0D), (new Vector3d(0.0D, 0.0D, -1.0D)).normalize()),
   DOWN(new Vector3d(0.0D, -1.0D, 0.0D), (new Vector3d(0.0D, 0.0D, 1.0D)).normalize());

   private final Vector3d normal;
   private final Vector3d uvUp;

   private Direction(Vector3d param3, Vector3d param4) {
      this.normal = var3;
      this.uvUp = var4;
   }

   public static Direction fromNormal(Vector3d var0) {
      Direction[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Direction var4 = var1[var3];
         if (MathUtils.isSimilar(var4.normal.dot(var0), 1.0D)) {
            return var4;
         }
      }

      return null;
   }

   private static Direction[] $values() {
      return new Direction[]{NORTH, EAST, SOUTH, WEST, UP, DOWN};
   }

   public Vector3d getNormal() {
      return new Vector3d(this.normal);
   }

   public Vector3d getUvUp() {
      return new Vector3d(this.uvUp);
   }

   // $FF: synthetic method
   private static Direction[] $values$() {
      return new Direction[]{NORTH, EAST, SOUTH, WEST, UP, DOWN};
   }
}
