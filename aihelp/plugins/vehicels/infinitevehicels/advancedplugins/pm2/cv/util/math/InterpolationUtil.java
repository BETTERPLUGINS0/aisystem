package advancedplugins.pm2.cv.util.math;

import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;

public final class InterpolationUtil {
   public static Vector3D interpolate(Vector3D from, Vector3D to, double progress) {
      double var4 = var0.getX();
      double var6 = var0.getY();
      double var8 = var0.getZ();
      double var10 = var1.getX();
      double var12 = var1.getY();
      double var14 = var1.getZ();
      return new Vector3D(var4 + (var10 - var4) * var2, var6 + (var12 - var6) * var2, var8 + (var14 - var8) * var2);
   }
}
