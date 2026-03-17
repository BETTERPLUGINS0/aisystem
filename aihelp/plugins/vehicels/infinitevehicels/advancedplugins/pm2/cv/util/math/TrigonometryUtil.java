package advancedplugins.pm2.cv.util.math;

import advancedplugins.pm2.cv.util.ConvertUtil;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import me.PM2.infinitevehicles.math.geometry.euclidean.twod.Vector2D;
import me.PM2.infinitevehicles.math.util.FastMath;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public final class TrigonometryUtil {
   public static double normalize(double angle) {
      return (var0 + 360.0D) % 360.0D;
   }

   public static float normalize(float angle) {
      return (var0 + 360.0F) % 360.0F;
   }

   public static int toIntAngle(float angle) {
      return (int)FastMath.floor((double)normalize(var0));
   }

   public static int calculateAngleBetween(@NotNull Vector2D first, @NotNull Vector2D second) {
      double var2 = var0.getX() * var1.getY() - var0.getY() * var1.getX();
      double var4 = var0.getX() * var1.getX() + var0.getY() * var1.getY();
      return (int)FastMath.toDegrees(FastMath.atan2(var2, var4));
   }

   public static Vector calculateDirectionVector(float yaw, float pitch) {
      Vector var2 = new Vector();
      var2.setY(-FastMath.sin(FastMath.toRadians((double)var1)));
      double var3 = FastMath.cos(FastMath.toRadians((double)var1));
      var2.setX(-var3 * FastMath.sin(FastMath.toRadians((double)var0)));
      var2.setZ(var3 * FastMath.cos(FastMath.toRadians((double)var0)));
      return var2;
   }

   public static float[] getAngles(Vector direction) {
      float var1 = 0.0F;
      float var2 = 0.0F;
      double var3 = 6.283185307179586D;
      double var5 = var0.getX();
      double var7 = var0.getZ();
      if (var5 == 0.0D && var7 == 0.0D) {
         var2 = var0.getY() > 0.0D ? -90.0F : 90.0F;
         return new float[]{var1, var2};
      } else {
         double var9 = Math.atan2(-var5, var7);
         var1 = (float)Math.toDegrees((var9 + 6.283185307179586D) % 6.283185307179586D);
         double var11 = NumberConversions.square(var5);
         double var13 = NumberConversions.square(var7);
         double var15 = Math.sqrt(var11 + var13);
         var2 = (float)Math.toDegrees(Math.atan(-var0.getY() / var15));
         return new float[]{var1, var2};
      }
   }

   public static float[] getAngles(Vector3D direction) {
      return getAngles(ConvertUtil.toVector(var0));
   }
}
