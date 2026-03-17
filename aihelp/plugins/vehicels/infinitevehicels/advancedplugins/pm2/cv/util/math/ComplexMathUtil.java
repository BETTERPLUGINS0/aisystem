package advancedplugins.pm2.cv.util.math;

import java.util.Collection;
import java.util.Iterator;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.jetbrains.annotations.Nullable;

public final class ComplexMathUtil {
   @Nullable
   public static Vector3D calculateCenter(Collection<Vector3D> points) {
      Vector3D var1 = null;
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Vector3D var3 = (Vector3D)var2.next();
         if (var1 == null) {
            var1 = var3;
         } else {
            var1 = var1.add(var3);
         }
      }

      if (var1 != null) {
         int var4 = var0.size();
         return new Vector3D(var1.getX() / (double)var4, var1.getY() / (double)var4, var1.getZ() / (double)var4);
      } else {
         return null;
      }
   }
}
