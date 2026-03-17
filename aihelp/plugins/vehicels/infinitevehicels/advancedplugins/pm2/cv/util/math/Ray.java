package advancedplugins.pm2.cv.util.math;

import advancedplugins.pm2.cv.util.ConvertUtil;
import java.util.Objects;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.bukkit.Location;

public class Ray {
   protected final Vector3D origin;
   protected final Vector3D direction;

   public Ray(Vector3D origin, Vector3D direction) {
      this.origin = var1;
      this.direction = var2.getNorm() != 0.0D ? var2.normalize() : var2;
   }

   public Ray(Location origin, Vector3D direction) {
      this(ConvertUtil.toVector3D(var1), var2);
   }

   public Ray(final Location data) {
      this(ConvertUtil.toVector3D(var1), ConvertUtil.toVector3D(var1.getDirection()));
   }

   public Vector3D getOrigin() {
      return this.origin;
   }

   public Vector3D getDirection() {
      return this.direction;
   }

   public Vector3D getEndPoint(final double distance) {
      return this.direction.scalarMultiply(var1).add(this.origin);
   }

   public boolean equals(Object obj) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Ray)) {
         return false;
      } else {
         return Objects.equals(((Ray)var1).origin, this.origin) && Objects.equals(((Ray)var1).direction, this.direction);
      }
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = 73 * var2 + this.direction.hashCode();
      var3 = 73 * var3 + this.origin.hashCode();
      return var3;
   }
}
