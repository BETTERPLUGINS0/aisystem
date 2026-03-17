package advancedplugins.pm2.cv.util.math;

import java.io.Serializable;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;

public class Plane implements Serializable {
   private static final long serialVersionUID = -1240652082930747866L;
   protected Vector3D normal;
   protected double d;

   public Plane() {
      this.normal = Vector3D.ZERO;
      this.d = 0.0D;
   }

   public Plane(Vector3D normal, float d) {
      this.normal = Vector3D.ZERO;
      this.d = 0.0D;
      this.normal = NormalizationUtil.normalize(var1);
      this.d = (double)var2;
   }

   public Plane(Vector3D normal, Vector3D point) {
      this.normal = Vector3D.ZERO;
      this.d = 0.0D;
      this.normal = NormalizationUtil.normalize(var1);
      this.d = -this.normal.dotProduct(var2);
   }

   public Plane(Vector3D point1, Vector3D point2, Vector3D point3) {
      this.normal = Vector3D.ZERO;
      this.d = 0.0D;
      this.set(var1, var2, var3);
   }

   public void set(Vector3D point1, Vector3D point2, Vector3D point3) {
      this.normal = NormalizationUtil.normalize(var1.subtract(var2).crossProduct(new Vector3D(var2.getX() - var3.getX(), var2.getY() - var3.getY(), var2.getZ() - var3.getZ())));
      this.d = -var1.dotProduct(this.normal);
   }

   public void set(float nx, float ny, float nz, float d) {
      this.normal = new Vector3D((double)var1, (double)var2, (double)var3);
      this.d = (double)var4;
   }

   public double distance(Vector3D point) {
      return this.normal.dotProduct(var1) + this.d;
   }

   public Plane.PlaneSide testPoint(Vector3D point) {
      double var2 = this.normal.dotProduct(var1) + this.d;
      if (var2 == 0.0D) {
         return Plane.PlaneSide.OnPlane;
      } else {
         return var2 < 0.0D ? Plane.PlaneSide.Back : Plane.PlaneSide.Front;
      }
   }

   public Plane.PlaneSide testPoint(float x, float y, float z) {
      double var4 = this.normal.dotProduct(new Vector3D((double)var1, (double)var2, (double)var3)) + this.d;
      if (var4 == 0.0D) {
         return Plane.PlaneSide.OnPlane;
      } else {
         return var4 < 0.0D ? Plane.PlaneSide.Back : Plane.PlaneSide.Front;
      }
   }

   public boolean isFrontFacing(Vector3D direction) {
      double var2 = this.normal.dotProduct(var1);
      return var2 <= 0.0D;
   }

   public Vector3D getNormal() {
      return this.normal;
   }

   public double getD() {
      return this.d;
   }

   public void set(Vector3D point, Vector3D normal) {
      this.normal = var1;
      this.d = -var1.dotProduct(var2);
   }

   public void set(float pointX, float pointY, float pointZ, float norX, float norY, float norZ) {
      this.normal = new Vector3D((double)var4, (double)var5, (double)var6);
      this.d = (double)(-(var1 * var4 + var2 * var5 + var3 * var6));
   }

   public void set(Plane plane) {
      this.normal = var1.normal;
      this.d = var1.d;
   }

   public String toString() {
      String var10000 = this.normal.toString();
      return var10000 + ", " + this.d;
   }

   public static enum PlaneSide {
      OnPlane,
      Back,
      Front;

      // $FF: synthetic method
      private static Plane.PlaneSide[] $values() {
         return new Plane.PlaneSide[]{OnPlane, Back, Front};
      }
   }
}
