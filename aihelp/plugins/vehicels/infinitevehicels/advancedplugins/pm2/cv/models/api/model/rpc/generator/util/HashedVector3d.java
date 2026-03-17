package advancedplugins.pm2.cv.models.api.model.rpc.generator.util;

import advancedplugins.pm2.cv.models.api.utils.math.Axis;
import java.util.Objects;
import org.joml.Vector3d;

public class HashedVector3d extends Vector3d {
   private static final int PRECISION = 10000;
   private final Axis axis;

   public HashedVector3d(Axis var1, double var2, double var4, double var6) {
      super(var2, var4, var6);
      this.axis = var1;
   }

   public HashedVector3d() {
      this.axis = Axis.X;
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.axis, (int)Math.round(this.x * 10000.0D), (int)Math.round(this.y * 10000.0D), (int)Math.round(this.z * 10000.0D)});
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         return this.hashCode() == var1.hashCode();
      }
   }
}
