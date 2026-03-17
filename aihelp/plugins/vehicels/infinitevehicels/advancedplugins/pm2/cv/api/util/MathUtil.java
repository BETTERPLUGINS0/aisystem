package advancedplugins.pm2.cv.api.util;

import java.util.ArrayList;
import java.util.List;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import me.PM2.infinitevehicles.math.util.FastMath;

public class MathUtil {
   public static int clamp(int var0, int var1, int var2) {
      return Math.max(var1, Math.min(var2, var0));
   }

   public static Vector3D toRadians(Vector3D var0) {
      return new Vector3D(FastMath.toRadians(var0.getX()), FastMath.toRadians(var0.getY()), FastMath.toRadians(var0.getZ()));
   }

   public static Vector3D toDegrees(Vector3D var0) {
      return new Vector3D(FastMath.toDegrees(var0.getX()), FastMath.toDegrees(var0.getY()), FastMath.toDegrees(var0.getZ()));
   }

   public static List<Integer> range(int var0, int var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = var0; var3 <= var1; ++var3) {
         var2.add(var3);
      }

      return var2;
   }
}
