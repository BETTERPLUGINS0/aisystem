package advancedplugins.pm2.cv.models.api.utils.math;

import java.util.UUID;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;
import org.joml.Matrix3d;
import org.joml.Matrix3f;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class MathUtils {
   protected static MathUtils.SlerpMode slerpMode;
   protected static double movementResolution;

   public static boolean isSimilar(float var0, float var1) {
      return (double)Math.abs(var1 - var0) < 1.0E-5D;
   }

   public static boolean isSimilar(double var0, double var2) {
      return Math.abs(var2 - var0) < 1.0E-5D;
   }

   public static double clamp(double var0, double var2, double var4) {
      return Math.min(Math.max(var0, var2), var4);
   }

   public static float clamp(float var0, float var1, float var2) {
      return Math.min(Math.max(var0, var1), var2);
   }

   public static int clamp(int var0, int var1, int var2) {
      return Math.min(Math.max(var0, var1), var2);
   }

   public static int absMax(float var0, float var1, float var2) {
      float var3 = Math.abs(var0);
      float var4 = Math.abs(var1);
      float var5 = Math.abs(var2);
      if (var3 > var4) {
         return var3 > var5 ? 0 : 2;
      } else {
         return var4 > var5 ? 1 : 2;
      }
   }

   public static byte setBit(byte var0, int var1, boolean var2) {
      byte var3 = (byte)(1 << var1);
      return (byte)(var2 ? var0 | var3 : var0 & ~var3);
   }

   public static boolean getBit(byte var0, int var1) {
      return (var0 & 1 << var1) != 0;
   }

   public static int floor(double var0) {
      return (int)Math.floor(var0);
   }

   public static int ceil(double var0) {
      return (int)Math.ceil(var0);
   }

   public static int tryParse(String var0, int var1) {
      if (var0 == null) {
         return var1;
      } else {
         try {
            return Integer.parseInt(var0);
         } catch (NumberFormatException var3) {
            return var1;
         }
      }
   }

   public static double tryParse(String var0, double var1) {
      if (var0 == null) {
         return var1;
      } else {
         try {
            return Double.parseDouble(var0);
         } catch (NumberFormatException var4) {
            return var1;
         }
      }
   }

   public static boolean isBoundingBoxWithinDistance(@NotNull Vector var0, @NotNull Vector var1, BoundingBox var2, double var3) {
      double var5 = var0.getX();
      double var7 = var0.getY();
      double var9 = var0.getZ();
      Vector var11 = var1.clone();
      if (var11.getX() == 0.0D) {
         var11.setX(0);
      }

      if (var11.getY() == 0.0D) {
         var11.setY(0);
      }

      if (var11.getZ() == 0.0D) {
         var11.setZ(0);
      }

      var11.normalize();
      double var12 = var11.getX();
      double var14 = var11.getY();
      double var16 = var11.getZ();
      double var18 = 1.0D / var12;
      double var20 = 1.0D / var14;
      double var22 = 1.0D / var16;
      double var24;
      double var26;
      if (var12 >= 0.0D) {
         var24 = (var2.getMinX() - var5) * var18;
         var26 = (var2.getMaxX() - var5) * var18;
      } else {
         var24 = (var2.getMaxX() - var5) * var18;
         var26 = (var2.getMinX() - var5) * var18;
      }

      double var28;
      double var30;
      if (var14 >= 0.0D) {
         var28 = (var2.getMinY() - var7) * var20;
         var30 = (var2.getMaxY() - var7) * var20;
      } else {
         var28 = (var2.getMaxY() - var7) * var20;
         var30 = (var2.getMinY() - var7) * var20;
      }

      if (!(var24 > var30) && !(var26 < var28)) {
         if (var28 > var24) {
            var24 = var28;
         }

         if (var30 < var26) {
            var26 = var30;
         }

         double var32;
         double var34;
         if (var16 >= 0.0D) {
            var32 = (var2.getMinZ() - var9) * var22;
            var34 = (var2.getMaxZ() - var9) * var22;
         } else {
            var32 = (var2.getMaxZ() - var9) * var22;
            var34 = (var2.getMinZ() - var9) * var22;
         }

         if (!(var24 > var34) && !(var26 < var32)) {
            if (var32 > var24) {
               var24 = var32;
            }

            if (var34 < var26) {
               var26 = var34;
            }

            if (var26 < 0.0D) {
               return false;
            } else {
               return !(var24 > var3);
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static double distanceSquaredToBoundingBox(Vector var0, BoundingBox var1) {
      double var2 = Math.max(Math.abs(var0.getX() - var1.getCenterX()) - var1.getWidthX() * 0.5D, 0.0D);
      double var4 = Math.max(Math.abs(var0.getY() - var1.getCenterY()) - var1.getHeight() * 0.5D, 0.0D);
      double var6 = Math.max(Math.abs(var0.getZ() - var1.getCenterZ()) - var1.getWidthZ() * 0.5D, 0.0D);
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   public static boolean isSimilar(Vector var0, Vector var1) {
      return Math.abs(var1.getX() - var0.getX()) < movementResolution && Math.abs(var1.getY() - var0.getY()) < movementResolution && Math.abs(var1.getZ() - var0.getZ()) < movementResolution;
   }

   public static EulerAngle makeAngle(double var0, double var2, double var4) {
      return new EulerAngle(Math.toRadians(var0), Math.toRadians(var2), Math.toRadians(var4));
   }

   public static EulerAngle add(EulerAngle var0, EulerAngle var1) {
      return var0.add(var1.getX(), var1.getY(), var1.getZ());
   }

   public static float wrapRadian(float var0) {
      var0 = (float)((double)var0 % 6.283185307179586D);
      if ((double)var0 < -3.141592653589793D) {
         var0 = (float)((double)var0 + 6.283185307179586D);
      }

      if ((double)var0 > 3.141592653589793D) {
         var0 = (float)((double)var0 - 6.283185307179586D);
      }

      return var0;
   }

   public static float wrapDegree(float var0) {
      var0 %= 360.0F;
      if (var0 < -180.0F) {
         var0 += 360.0F;
      }

      if (var0 > 180.0F) {
         var0 -= 360.0F;
      }

      return var0;
   }

   public static float radianDifference(float var0, float var1) {
      return wrapRadian(var1 - var0);
   }

   public static float degreeDifference(float var0, float var1) {
      return wrapDegree(var1 - var0);
   }

   public static float rotateIfNecessary(float var0, float var1, float var2, float var3) {
      float var4 = degreeDifference(var0, var1);
      float var5 = clamp(var4, var2, var3);
      return var1 - var5;
   }

   public static byte rotToByte(float var0) {
      return (byte)((int)(var0 * 256.0F / 360.0F));
   }

   public static float byteToRot(byte var0) {
      return (float)var0 / 256.0F * 360.0F;
   }

   public static double lerp(double var0, double var2, double var4) {
      return (1.0D - var4) * var0 + var4 * var2;
   }

   public static double lerp(double var0, double var2, double var4, double var6) {
      return var4 * var0 + var6 * var2;
   }

   public static float lerp(float var0, float var1, float var2, float var3) {
      return var2 * var0 + var3 * var1;
   }

   public static double rotLerp(double var0, double var2, double var4) {
      return var0 + (double)degreeDifference((float)var0, (float)var2) * var4;
   }

   public static float rotLerp(float var0, float var1, double var2) {
      return (float)((double)var0 + (double)degreeDifference(var0, var1) * var2);
   }

   public static double smoothLerp(double var0, double var2, double var4, double var6, double var8) {
      double var10 = 0.0D;
      double var12 = 1.0D;
      double var14 = 2.0D;
      double var16 = 3.0D;
      var8 = (var14 - var12) * var8 + var12;
      double var18 = lerp(var0, var2, (var12 - var8) / (var12 - var10), (var8 - var10) / (var12 - var10));
      double var20 = lerp(var2, var4, (var14 - var8) / (var14 - var12), (var8 - var12) / (var14 - var12));
      double var22 = lerp(var4, var6, (var16 - var8) / (var16 - var14), (var8 - var14) / (var16 - var14));
      double var24 = lerp(var18, var20, (var14 - var8) / (var14 - var10), (var8 - var10) / (var14 - var10));
      double var26 = lerp(var20, var22, (var16 - var8) / (var16 - var12), (var8 - var12) / (var16 - var12));
      return lerp(var24, var26, (var14 - var8) / (var14 - var12), (var8 - var12) / (var14 - var12));
   }

   public static Vector lerp(Vector var0, Vector var1, double var2) {
      return new Vector(lerp(var0.getX(), var1.getX(), var2), lerp(var0.getY(), var1.getY(), var2), lerp(var0.getZ(), var1.getZ(), var2));
   }

   public static Vector lerp(Vector var0, Vector var1, double var2, double var4) {
      return new Vector(lerp(var0.getX(), var1.getX(), var2, var4), lerp(var0.getY(), var1.getY(), var2, var4), lerp(var0.getZ(), var1.getZ(), var2, var4));
   }

   public static Vector3f lerp(Vector3f var0, Vector3f var1, double var2) {
      return new Vector3f((float)lerp((double)var0.x, (double)var1.x, var2), (float)lerp((double)var0.y, (double)var1.y, var2), (float)lerp((double)var0.z, (double)var1.z, var2));
   }

   public static Vector3f lerp(Vector3f var0, Vector3f var1, float var2, float var3) {
      return new Vector3f(lerp(var0.x, var1.x, var2, var3), lerp(var0.y, var1.y, var2, var3), lerp(var0.z, var1.z, var2, var3));
   }

   public static Vector smoothLerp(Vector var0, Vector var1, Vector var2, Vector var3, double var4) {
      double var6 = 0.0D;
      double var8 = 1.0D;
      double var10 = 2.0D;
      double var12 = 3.0D;
      var4 = (var10 - var8) * var4 + var8;
      Vector var14 = lerp(var0, var1, (var8 - var4) / (var8 - var6), (var4 - var6) / (var8 - var6));
      Vector var15 = lerp(var1, var2, (var10 - var4) / (var10 - var8), (var4 - var8) / (var10 - var8));
      Vector var16 = lerp(var2, var3, (var12 - var4) / (var12 - var10), (var4 - var10) / (var12 - var10));
      Vector var17 = lerp(var14, var15, (var10 - var4) / (var10 - var6), (var4 - var6) / (var10 - var6));
      Vector var18 = lerp(var15, var16, (var12 - var4) / (var12 - var8), (var4 - var8) / (var12 - var8));
      return lerp(var17, var18, (var10 - var4) / (var10 - var8), (var4 - var8) / (var10 - var8));
   }

   public static Vector3f smoothLerp(Vector3f var0, Vector3f var1, Vector3f var2, Vector3f var3, float var4) {
      float var5 = 0.0F;
      float var6 = 1.0F;
      float var7 = 2.0F;
      float var8 = 3.0F;
      var4 = (var7 - var6) * var4 + var6;
      Vector3f var9 = lerp(var0, var1, (var6 - var4) / (var6 - var5), (var4 - var5) / (var6 - var5));
      Vector3f var10 = lerp(var1, var2, (var7 - var4) / (var7 - var6), (var4 - var6) / (var7 - var6));
      Vector3f var11 = lerp(var2, var3, (var8 - var4) / (var8 - var7), (var4 - var7) / (var8 - var7));
      Vector3f var12 = lerp(var9, var10, (var7 - var4) / (var7 - var5), (var4 - var5) / (var7 - var5));
      Vector3f var13 = lerp(var10, var11, (var8 - var4) / (var8 - var6), (var4 - var6) / (var8 - var6));
      return lerp(var12, var13, (var7 - var4) / (var7 - var6), (var4 - var6) / (var7 - var6));
   }

   public static EulerAngle lerp(EulerAngle var0, EulerAngle var1, double var2) {
      return new EulerAngle(lerp(var0.getX(), var1.getX(), var2), lerp(var0.getY(), var1.getY(), var2), lerp(var0.getZ(), var1.getZ(), var2));
   }

   public static EulerAngle lerp(EulerAngle var0, EulerAngle var1, double var2, double var4) {
      return new EulerAngle(lerp(var0.getX(), var1.getX(), var2, var4), lerp(var0.getY(), var1.getY(), var2, var4), lerp(var0.getZ(), var1.getZ(), var2, var4));
   }

   public static EulerAngle smoothLerp(EulerAngle var0, EulerAngle var1, EulerAngle var2, EulerAngle var3, double var4) {
      double var6 = 0.0D;
      double var8 = 1.0D;
      double var10 = 2.0D;
      double var12 = 3.0D;
      var4 = (var10 - var8) * var4 + var8;
      EulerAngle var14 = lerp(var0, var1, (var8 - var4) / (var8 - var6), (var4 - var6) / (var8 - var6));
      EulerAngle var15 = lerp(var1, var2, (var10 - var4) / (var10 - var8), (var4 - var8) / (var10 - var8));
      EulerAngle var16 = lerp(var2, var3, (var12 - var4) / (var12 - var10), (var4 - var10) / (var12 - var10));
      EulerAngle var17 = lerp(var14, var15, (var10 - var4) / (var10 - var6), (var4 - var6) / (var10 - var6));
      EulerAngle var18 = lerp(var15, var16, (var12 - var4) / (var12 - var8), (var4 - var8) / (var12 - var8));
      return lerp(var17, var18, (var10 - var4) / (var10 - var8), (var4 - var8) / (var10 - var8));
   }

   public static Vector3f slerp(Vector3f var0, Vector3f var1, double var2) {
      if (var0.equals(var1)) {
         return var0;
      } else {
         Quaternionf var4 = (new Quaternionf()).rotationZYX(var0.z, var0.y, var0.x);
         Quaternionf var5 = (new Quaternionf()).rotationZYX(var1.z, var1.y, var1.x);
         var4.slerp(var5, (float)var2);
         return getEulerAnglesZYX(var4, new Vector3f());
      }
   }

   public static Vector3f getEulerAnglesZYX(Quaternionf var0, Vector3f var1) {
      return matToEulerZYX(var0.get(new Matrix3f()), var1);
   }

   public static Vector3f matToEulerZYX(Matrix3f var0, Vector3f var1) {
      var1.y = Math.asin(-Math.clamp(var0.m02, -1.0F, 1.0F));
      if ((double)Math.abs(var0.m02) < 0.9999999D) {
         var1.x = Math.atan2(var0.m12, var0.m22);
         var1.z = Math.atan2(var0.m01, var0.m00);
      } else {
         var1.x = 0.0F;
         var1.z = Math.atan2(-var0.m10, var0.m11);
      }

      return var1;
   }

   public static Vector3d getEulerAnglesZYX(Quaterniond var0, Vector3d var1) {
      return matToEulerZYX(var0.get(new Matrix3d()), var1);
   }

   public static Vector3d matToEulerZYX(Matrix3d var0, Vector3d var1) {
      var1.y = Math.asin(-clamp(var0.m02, -1.0D, 1.0D));
      if (Math.abs(var0.m02) < 0.9999999D) {
         var1.x = Math.atan2(var0.m12, var0.m22);
         var1.z = Math.atan2(var0.m01, var0.m00);
      } else {
         var1.x = 0.0D;
         var1.z = Math.atan2(-var0.m10, var0.m11);
      }

      return var1;
   }

   public static Vector3d toEulerZYX(Quaterniond var0) {
      return getEulerAnglesZYX(var0, new Vector3d()).mul(57.29577951308232D);
   }

   public static Vector3d toEulerXYZ(Quaterniond var0) {
      return matToEulerXYZ(var0.get(new Matrix3d()), new Vector3d()).mul(57.29577951308232D);
   }

   public static Vector3d matToEulerXYZ(Matrix3d var0, Vector3d var1) {
      var1.y = Math.asin(clamp(var0.m20, -1.0D, 1.0D));
      if (Math.abs(var0.m20) < 0.9999999D) {
         var1.x = Math.atan2(-var0.m21, var0.m22);
         var1.z = Math.atan2(-var0.m10, var0.m00);
      } else {
         var1.x = Math.atan2(var0.m12, var0.m11);
         var1.z = 0.0D;
      }

      return var1;
   }

   public static Quaterniond fromEulerZYX(Vector3d var0) {
      return (new Quaterniond()).rotateZYX(var0.z * 0.017453292519943295D, var0.y * 0.017453292519943295D, var0.x * 0.017453292519943295D);
   }

   public static Quaterniond fromEulerXYZ(Vector3d var0) {
      return (new Quaterniond()).rotateXYZ(var0.x * 0.017453292519943295D, var0.y * 0.017453292519943295D, var0.z * 0.017453292519943295D);
   }

   public static boolean isInterval(double var0, double var2) {
      double var4 = var2 * 0.5D;
      return isSimilar(Math.abs(Math.abs(var0) % var2 - var4), var4);
   }

   public static boolean isAlmostBetween(double var0, double var2, double var4) {
      if (var0 >= var2 && var0 <= var4) {
         return true;
      } else {
         return isSimilar(var0, var2) || isSimilar(var0, var4);
      }
   }

   public static Vector3f toPitchYaw(Vector3f var0) {
      double var1 = (double)Math.sqrt(var0.x * var0.x + var0.z * var0.z);
      float var3 = (float)Math.atan2((double)(-var0.y), var1) * 57.29578F;
      float var4 = Math.atan2(-var0.x, var0.z) * 57.29578F;
      return new Vector3f(var3, var4, 0.0F);
   }

   public static Quaternionf toQuaternion(Vector3f var0) {
      return (new Quaternionf()).rotateZYX(var0.z, var0.y, var0.x);
   }

   public static Vector3f fixVector(Vector3f var0) {
      if (isSimilar(var0.x, 0.0F)) {
         var0.x = 0.0F;
      }

      if (isSimilar(var0.y, 0.0F)) {
         var0.y = 0.0F;
      }

      if (isSimilar(var0.z, 0.0F)) {
         var0.z = 0.0F;
      }

      return var0;
   }

   public static Vector3d fixVector(Vector3d var0) {
      if (isSimilar(var0.x, 0.0D)) {
         var0.x = 0.0D;
      }

      if (isSimilar(var0.y, 0.0D)) {
         var0.y = 0.0D;
      }

      if (isSimilar(var0.z, 0.0D)) {
         var0.z = 0.0D;
      }

      return var0;
   }

   public static Vector3d fixEuler(Vector3d var0) {
      fixVector(var0);
      var0.x = (double)Math.round(var0.x * 10000.0D) * 1.0E-4D;
      var0.y = (double)Math.round(var0.y * 10000.0D) * 1.0E-4D;
      var0.z = (double)Math.round(var0.z * 10000.0D) * 1.0E-4D;
      return var0;
   }

   public static Quaternionf fixQuaternion(Quaternionf var0) {
      if (isSimilar(var0.x, 0.0F)) {
         var0.x = 0.0F;
      }

      if (isSimilar(var0.y, 0.0F)) {
         var0.y = 0.0F;
      }

      if (isSimilar(var0.z, 0.0F)) {
         var0.z = 0.0F;
      }

      if (isSimilar(var0.w, 0.0F)) {
         var0.w = 0.0F;
      }

      return var0;
   }

   public static Quaterniond fixQuaternion(Quaterniond var0) {
      if (isSimilar(var0.x, 0.0D)) {
         var0.x = 0.0D;
      }

      if (isSimilar(var0.y, 0.0D)) {
         var0.y = 0.0D;
      }

      if (isSimilar(var0.z, 0.0D)) {
         var0.z = 0.0D;
      }

      if (isSimilar(var0.w, 0.0D)) {
         var0.w = 0.0D;
      }

      return var0;
   }

   public static float[] unwrap(Vector3f var0) {
      return new float[]{var0.x, var0.y, var0.z};
   }

   public static float[] unwrap(Vector3d var0) {
      return new float[]{(float)var0.x, (float)var0.y, (float)var0.z};
   }

   public static String toString(EulerAngle var0) {
      return String.format("[%s, %s, %s]", Math.toDegrees(var0.getX()), Math.toDegrees(var0.getY()), Math.toDegrees(var0.getZ()));
   }

   public static UUID parseUUID(String var0) {
      try {
         return UUID.fromString(var0);
      } catch (IllegalArgumentException var6) {
         if (var0.length() != 32) {
            throw var6;
         } else {
            long var2 = Long.parseUnsignedLong(var0.substring(0, 16), 16);
            long var4 = Long.parseUnsignedLong(var0.substring(16), 16);
            return new UUID(var2, var4);
         }
      }
   }

   static {
      slerpMode = MathUtils.SlerpMode.SLERP;
      movementResolution = 0.001D;
   }

   public static enum SlerpMode {
      SLERP,
      ONLERP;

      public static MathUtils.SlerpMode get(String var0) {
         try {
            return valueOf(var0);
         } catch (IllegalArgumentException var2) {
            return SLERP;
         }
      }

      // $FF: synthetic method
      private static MathUtils.SlerpMode[] $values() {
         return new MathUtils.SlerpMode[]{SLERP, ONLERP};
      }
   }
}
