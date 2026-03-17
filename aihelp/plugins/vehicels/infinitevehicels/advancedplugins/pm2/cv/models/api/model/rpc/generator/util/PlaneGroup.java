package advancedplugins.pm2.cv.models.api.model.rpc.generator.util;

import it.unimi.dsi.fastutil.ints.IntSet;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public record PlaneGroup(Vector3d axis, int modAngle, Quaterniond origin, Quaterniond invOrigin, IntSet cubes) {
   public PlaneGroup(Vector3d axis, int modAngle, Quaterniond origin, Quaterniond invOrigin, IntSet cubes) {
      this.axis = var1;
      this.modAngle = var2;
      this.origin = var3;
      this.invOrigin = var4;
      this.cubes = var5;
   }

   public Vector3d axis() {
      return this.axis;
   }

   public int modAngle() {
      return this.modAngle;
   }

   public Quaterniond origin() {
      return this.origin;
   }

   public Quaterniond invOrigin() {
      return this.invOrigin;
   }

   public IntSet cubes() {
      return this.cubes;
   }
}
