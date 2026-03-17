package advancedplugins.pm2.cv.util.math;

import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;

public class TransformUtil {
   public static Vector3D transform(Vector3D vector, Matrix4 transform) {
      double[] var2 = var1.elements;
      return new Vector3D(var0.getX() * var2[0] + var0.getY() * var2[4] + var0.getZ() * var2[8] + var2[12], var0.getX() * var2[1] + var0.getY() * var2[5] + var0.getZ() * var2[9] + var2[13], var0.getX() * var2[2] + var0.getY() * var2[6] + var0.getZ() * var2[10] + var2[14]);
   }
}
