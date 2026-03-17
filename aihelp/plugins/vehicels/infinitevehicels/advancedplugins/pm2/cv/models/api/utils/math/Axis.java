package advancedplugins.pm2.cv.models.api.utils.math;

import org.joml.Vector3d;

public enum Axis {
   X(new Vector3d(1.0D, 0.0D, 0.0D)),
   Y(new Vector3d(0.0D, 1.0D, 0.0D)),
   Z(new Vector3d(0.0D, 0.0D, 1.0D));

   private final Vector3d vector;

   private Axis(Vector3d param3) {
      this.vector = var3;
   }

   private static Axis[] $values() {
      return new Axis[]{X, Y, Z};
   }

   public Vector3d getVector() {
      return this.vector.get(new Vector3d());
   }

   // $FF: synthetic method
   private static Axis[] $values$() {
      return new Axis[]{X, Y, Z};
   }
}
