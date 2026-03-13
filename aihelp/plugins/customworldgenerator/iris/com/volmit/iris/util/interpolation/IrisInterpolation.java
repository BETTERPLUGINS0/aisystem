package com.volmit.iris.util.interpolation;

import com.google.common.util.concurrent.AtomicDouble;
import com.volmit.iris.engine.object.NoiseStyle;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.function.Consumer2;
import com.volmit.iris.util.function.NoiseProvider;
import com.volmit.iris.util.function.NoiseProvider3;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.math.BigDecimal;
import java.util.HashMap;

public class IrisInterpolation {
   public static CNG cng;

   public static double bezier(double t) {
      return var0 * var0 * (3.0D - 2.0D * var0);
   }

   public static double parametric(double t, double alpha) {
      double var4 = Math.pow(var0, var2);
      return var4 / (var2 * (var4 - Math.pow(var0, var2 - 1.0D)) + 1.0D);
   }

   public static float lerpf(float a, float b, float f) {
      return var0 + var2 * (var1 - var0);
   }

   public static double lerpBezier(double a, double b, double f) {
      return var0 + bezier(var4) * (var2 - var0);
   }

   public static double sinCenter(double f) {
      return Math.sin(var0 * 3.141592653589793D);
   }

   public static double lerpCenterSinBezier(double a, double b, double f) {
      return lerpBezier(var0, var2, sinCenter(var4));
   }

   public static double lerpCenterSin(double a, double b, double f) {
      return lerpBezier(var0, var2, sinCenter(var4));
   }

   public static double lerpParametric(double a, double b, double f, double v) {
      return var0 + parametric(var4, var6) * (var2 - var0);
   }

   public static double blerp(double a, double b, double c, double d, double tx, double ty, InterpolationType type) {
      if (var12.equals(InterpolationType.LINEAR)) {
         return blerp(var0, var2, var4, var6, var8, var10);
      } else if (var12.equals(InterpolationType.BEZIER)) {
         return blerpBezier(var0, var2, var4, var6, var8, var10);
      } else if (var12.equals(InterpolationType.PARAMETRIC_2)) {
         return blerpParametric(var0, var2, var4, var6, var8, var10, 2.0D);
      } else {
         return var12.equals(InterpolationType.PARAMETRIC_4) ? blerpParametric(var0, var2, var4, var6, var8, var10, 4.0D) : 0.0D;
      }
   }

   public static double blerpBezier(double a, double b, double c, double d, double tx, double ty) {
      return lerpBezier(lerpBezier(var0, var2, var8), lerpBezier(var4, var6, var8), var10);
   }

   public static double blerpSinCenter(double a, double b, double c, double d, double tx, double ty) {
      return lerpCenterSin(lerpCenterSin(var0, var2, var8), lerpCenterSin(var4, var6, var8), var10);
   }

   public static double blerpParametric(double a, double b, double c, double d, double tx, double ty, double v) {
      return lerpParametric(lerpParametric(var0, var2, var8, var12), lerpParametric(var4, var6, var8, var12), var10, var12);
   }

   public static double hermiteBezier(double p0, double p1, double p2, double p3, double mu, double tension, double bias) {
      return bezier(hermite(var0, var2, var4, var6, var8, var10, var12));
   }

   public static double hermiteParametric(double p0, double p1, double p2, double p3, double mu, double tension, double bias, double a) {
      return parametric(hermite(var0, var2, var4, var6, var8, var10, var12), var14);
   }

   public static double bihermiteBezier(double p00, double p01, double p02, double p03, double p10, double p11, double p12, double p13, double p20, double p21, double p22, double p23, double p30, double p31, double p32, double p33, double mux, double muy, double tension, double bias) {
      return hermiteBezier(hermiteBezier(var0, var2, var4, var6, var34, var36, var38), hermiteBezier(var8, var10, var12, var14, var34, var36, var38), hermiteBezier(var16, var18, var20, var22, var34, var36, var38), hermiteBezier(var24, var26, var28, var30, var34, var36, var38), var32, var36, var38);
   }

   public static double bihermiteParametric(double p00, double p01, double p02, double p03, double p10, double p11, double p12, double p13, double p20, double p21, double p22, double p23, double p30, double p31, double p32, double p33, double mux, double muy, double tension, double bias, double a) {
      return hermiteParametric(hermiteParametric(var0, var2, var4, var6, var34, var36, var38, var40), hermiteParametric(var8, var10, var12, var14, var34, var36, var38, var40), hermiteParametric(var16, var18, var20, var22, var34, var36, var38, var40), hermiteParametric(var24, var26, var28, var30, var34, var36, var38, var40), var32, var36, var38, var40);
   }

   public static double cubicBezier(double p0, double p1, double p2, double p3, double mu) {
      return bezier(cubic(var0, var2, var4, var6, var8));
   }

   public static double cubicParametric(double p0, double p1, double p2, double p3, double mu, double a) {
      return parametric(cubic(var0, var2, var4, var6, var8), var10);
   }

   public static double hermite(double p0, double p1, double p2, double p3, double mu, double tension, double bias) {
      double var18 = var8 * var8;
      double var20 = var18 * var8;
      double var14 = (var2 - var0) * (1.0D + var12) * (1.0D - var10) / 2.0D;
      var14 += (var4 - var2) * (1.0D - var12) * (1.0D - var10) / 2.0D;
      double var16 = (var4 - var2) * (1.0D + var12) * (1.0D - var10) / 2.0D;
      var16 += (var6 - var4) * (1.0D - var12) * (1.0D - var10) / 2.0D;
      double var22 = 2.0D * var20 - 3.0D * var18 + 1.0D;
      double var24 = var20 - 2.0D * var18 + var8;
      double var26 = var20 - var18;
      double var28 = -2.0D * var20 + 3.0D * var18;
      return var22 * var2 + var24 * var14 + var26 * var16 + var28 * var4;
   }

   public static double cubic(double p0, double p1, double p2, double p3, double mu) {
      double var18 = var8 * var8;
      double var10 = var6 - var4 - var0 + var2;
      double var12 = var0 - var2 - var10;
      double var14 = var4 - var0;
      return var10 * var8 * var18 + var12 * var18 + var14 * var8 + var2;
   }

   public static double bicubic(double p00, double p01, double p02, double p03, double p10, double p11, double p12, double p13, double p20, double p21, double p22, double p23, double p30, double p31, double p32, double p33, double mux, double muy) {
      return cubic(cubic(var0, var2, var4, var6, var34), cubic(var8, var10, var12, var14, var34), cubic(var16, var18, var20, var22, var34), cubic(var24, var26, var28, var30, var34), var32);
   }

   public static double bihermite(double p00, double p01, double p02, double p03, double p10, double p11, double p12, double p13, double p20, double p21, double p22, double p23, double p30, double p31, double p32, double p33, double mux, double muy, double tension, double bias) {
      return hermite(hermite(var0, var2, var4, var6, var34, var36, var38), hermite(var8, var10, var12, var14, var34, var36, var38), hermite(var16, var18, var20, var22, var34, var36, var38), hermite(var24, var26, var28, var30, var34, var36, var38), var32, var36, var38);
   }

   public static double trihermite(double p000, double p001, double p002, double p003, double p010, double p011, double p012, double p013, double p020, double p021, double p022, double p023, double p030, double p031, double p032, double p033, double p100, double p101, double p102, double p103, double p110, double p111, double p112, double p113, double p120, double p121, double p122, double p123, double p130, double p131, double p132, double p133, double p200, double p201, double p202, double p203, double p210, double p211, double p212, double p213, double p220, double p221, double p222, double p223, double p230, double p231, double p232, double p233, double p300, double p301, double p302, double p303, double p310, double p311, double p312, double p313, double p320, double p321, double p322, double p323, double p330, double p331, double p332, double p333, double mux, double muy, double muz, double tension, double bias) {
      return hermite(bihermite(var0, var2, var4, var6, var8, var10, var12, var14, var16, var18, var20, var22, var24, var26, var28, var30, var128, var130, var134, var136), bihermite(var32, var34, var36, var38, var40, var42, var44, var46, var48, var50, var52, var54, var56, var58, var60, var62, var128, var130, var134, var136), bihermite(var64, var66, var68, var70, var72, var74, var76, var78, var80, var82, var84, var86, var88, var90, var92, var94, var128, var130, var134, var136), bihermite(var96, var98, var100, var102, var104, var106, var108, var110, var112, var114, var116, var118, var120, var122, var124, var126, var128, var130, var134, var136), var132, var134, var136);
   }

   public static double tricubic(double p000, double p001, double p002, double p003, double p010, double p011, double p012, double p013, double p020, double p021, double p022, double p023, double p030, double p031, double p032, double p033, double p100, double p101, double p102, double p103, double p110, double p111, double p112, double p113, double p120, double p121, double p122, double p123, double p130, double p131, double p132, double p133, double p200, double p201, double p202, double p203, double p210, double p211, double p212, double p213, double p220, double p221, double p222, double p223, double p230, double p231, double p232, double p233, double p300, double p301, double p302, double p303, double p310, double p311, double p312, double p313, double p320, double p321, double p322, double p323, double p330, double p331, double p332, double p333, double mux, double muy, double muz) {
      return cubic(bicubic(var0, var2, var4, var6, var8, var10, var12, var14, var16, var18, var20, var22, var24, var26, var28, var30, var128, var130), bicubic(var32, var34, var36, var38, var40, var42, var44, var46, var48, var50, var52, var54, var56, var58, var60, var62, var128, var130), bicubic(var64, var66, var68, var70, var72, var74, var76, var78, var80, var82, var84, var86, var88, var90, var92, var94, var128, var130), bicubic(var96, var98, var100, var102, var104, var106, var108, var110, var112, var114, var116, var118, var120, var122, var124, var126, var128, var130), var132);
   }

   public static double lerp(double a, double b, double f) {
      return var0 + var4 * (var2 - var0);
   }

   public static double blerp(double a, double b, double c, double d, double tx, double ty) {
      return lerp(lerp(var0, var2, var8), lerp(var4, var6, var8), var10);
   }

   public static double trilerp(double v1, double v2, double v3, double v4, double v5, double v6, double v7, double v8, double x, double y, double z) {
      return lerp(blerp(var0, var2, var4, var6, var16, var18), blerp(var8, var10, var12, var14, var16, var18), var20);
   }

   public static double bicubicBezier(double p00, double p01, double p02, double p03, double p10, double p11, double p12, double p13, double p20, double p21, double p22, double p23, double p30, double p31, double p32, double p33, double mux, double muy) {
      return cubicBezier(cubicBezier(var0, var2, var4, var6, var34), cubicBezier(var8, var10, var12, var14, var34), cubicBezier(var16, var18, var20, var22, var34), cubicBezier(var24, var26, var28, var30, var34), var32);
   }

   public static double bicubicParametric(double p00, double p01, double p02, double p03, double p10, double p11, double p12, double p13, double p20, double p21, double p22, double p23, double p30, double p31, double p32, double p33, double mux, double muy, double a) {
      return cubicParametric(cubicParametric(var0, var2, var4, var6, var34, var36), cubicParametric(var8, var10, var12, var14, var34, var36), cubicParametric(var16, var18, var20, var22, var34, var36), cubicParametric(var24, var26, var28, var30, var34, var36), var32, var36);
   }

   public static int getRadiusFactor(int coord, double radius) {
      if (var1 == 2.0D) {
         return var0 >> 1;
      } else if (var1 == 4.0D) {
         return var0 >> 2;
      } else if (var1 == 8.0D) {
         return var0 >> 3;
      } else if (var1 == 16.0D) {
         return var0 >> 4;
      } else if (var1 == 32.0D) {
         return var0 >> 5;
      } else if (var1 == 64.0D) {
         return var0 >> 6;
      } else if (var1 == 128.0D) {
         return var0 >> 7;
      } else if (var1 == 256.0D) {
         return var0 >> 8;
      } else if (var1 == 512.0D) {
         return var0 >> 9;
      } else {
         return var1 == 1024.0D ? var0 >> 10 : (int)Math.floor((double)var0 / var1);
      }
   }

   public static double getBilinearNoise(int x, int z, double rad, NoiseProvider n) {
      int var5 = getRadiusFactor(var0, var2);
      int var6 = getRadiusFactor(var1, var2);
      int var7 = (int)Math.round((double)var5 * var2);
      int var8 = (int)Math.round((double)var6 * var2);
      int var9 = (int)Math.round((double)(var5 + 1) * var2);
      int var10 = (int)Math.round((double)(var6 + 1) * var2);
      double var11 = rangeScale(0.0D, 1.0D, (double)var7, (double)var9, (double)var0);
      double var13 = rangeScale(0.0D, 1.0D, (double)var8, (double)var10, (double)var1);
      return blerp(var4.noise((double)var7, (double)var8), var4.noise((double)var9, (double)var8), var4.noise((double)var7, (double)var10), var4.noise((double)var9, (double)var10), var11, var13);
   }

   public static void test(String m, Consumer2<Integer, Integer> f) {
      PrecisionStopwatch var2 = PrecisionStopwatch.start();

      for(int var3 = 0; var3 < 8192; ++var3) {
         var1.accept(var3, -var3 * 234);
      }

      var2.end();
      System.out.println(var0 + ": " + Form.duration(var2.getMilliseconds(), 8));
   }

   public static void printOptimizedSrc(boolean arrays) {
      System.out.println(generateOptimizedStarcast(3.0D, var0));
      System.out.println(generateOptimizedStarcast(5.0D, var0));
      System.out.println(generateOptimizedStarcast(6.0D, var0));
      System.out.println(generateOptimizedStarcast(7.0D, var0));
      System.out.println(generateOptimizedStarcast(9.0D, var0));
      System.out.println(generateOptimizedStarcast(12.0D, var0));
      System.out.println(generateOptimizedStarcast(24.0D, var0));
      System.out.println(generateOptimizedStarcast(32.0D, var0));
      System.out.println(generateOptimizedStarcast(48.0D, var0));
      System.out.println(generateOptimizedStarcast(64.0D, var0));
   }

   public static String generateOptimizedStarcast(double checks, boolean array) {
      double var3 = 360.0D / var0;
      int var5 = 0;
      int var6 = 0;
      StringBuilder var7 = new StringBuilder();
      StringBuilder var8 = new StringBuilder();
      if (var2) {
         var7.append("private static final double[] F").append((int)var0).append("A = {");
      }

      var8.append("private static double sc").append((int)var0).append("(int x, int z, double r, NoiseProvider n) {\n    return (");

      for(int var9 = 0; var9 < 360; var9 = (int)((double)var9 + var3)) {
         double var10 = Math.sin(Math.toRadians((double)var9));
         double var12 = Math.cos(Math.toRadians((double)var9));
         String var14 = (new BigDecimal(var12)).toPlainString();
         String var15 = (new BigDecimal(var10)).toPlainString();
         String var16 = var2 ? "F" + (int)var0 + "A[" + var6++ + "]" : "F" + (int)var0 + "C" + var5;
         String var17 = var2 ? "F" + (int)var0 + "A[" + var6++ + "]" : "F" + (int)var0 + "S" + var5;
         if (var2) {
            var7.append(var5 > 0 ? (var5 % 6 == 0 ? ",\n" : ",") : "").append(var14).append(",").append(var15);
         } else {
            var7.append("private static final double ").append(var16).append(" = ").append(var14).append(";\n");
            var7.append("private static final double ").append(var17).append(" = ").append(var15).append(";\n");
         }

         var8.append(var5 > 0 ? "\n    +" : "").append("n.noise(x + ((r * ").append(var16).append(") - (r * ").append(var17).append(")), z + ((r * ").append(var17).append(") + (r * ").append(var16).append(")))");
         ++var5;
      }

      if (var2) {
         var7.append("};");
      }

      var8.append(")/").append(var0).append("D;\n}");
      String var18 = String.valueOf(var7);
      return var18 + "\n" + String.valueOf(var8);
   }

   public static double getStarcast3D(int x, int y, int z, double rad, double checks, NoiseProvider3 n) {
      return (Starcast.starcast(var0, var2, var3, var5, (var2x, var4) -> {
         return var7.noise(var2x, (double)var1, var4);
      }) + Starcast.starcast(var0, var1, var3, var5, (var2x, var4) -> {
         return var7.noise(var2x, var4, (double)var2);
      }) + Starcast.starcast(var1, var2, var3, var5, (var2x, var4) -> {
         return var7.noise((double)var0, var2x, var4);
      })) / 3.0D;
   }

   public static double getBilinearBezierNoise(int x, int z, double rad, NoiseProvider n) {
      int var5 = getRadiusFactor(var0, var2);
      int var6 = getRadiusFactor(var1, var2);
      int var7 = (int)Math.round((double)var5 * var2);
      int var8 = (int)Math.round((double)var6 * var2);
      int var9 = (int)Math.round((double)(var5 + 1) * var2);
      int var10 = (int)Math.round((double)(var6 + 1) * var2);
      double var11 = rangeScale(0.0D, 1.0D, (double)var7, (double)var9, (double)var0);
      double var13 = rangeScale(0.0D, 1.0D, (double)var8, (double)var10, (double)var1);
      return blerpBezier(var4.noise((double)var7, (double)var8), var4.noise((double)var9, (double)var8), var4.noise((double)var7, (double)var10), var4.noise((double)var9, (double)var10), var11, var13);
   }

   public static double getBilinearParametricNoise(int x, int z, double rad, NoiseProvider n, double a) {
      int var7 = getRadiusFactor(var0, var2);
      int var8 = getRadiusFactor(var1, var2);
      int var9 = (int)Math.round((double)var7 * var2);
      int var10 = (int)Math.round((double)var8 * var2);
      int var11 = (int)Math.round((double)(var7 + 1) * var2);
      int var12 = (int)Math.round((double)(var8 + 1) * var2);
      double var13 = rangeScale(0.0D, 1.0D, (double)var9, (double)var11, (double)var0);
      double var15 = rangeScale(0.0D, 1.0D, (double)var10, (double)var12, (double)var1);
      return blerpParametric(var4.noise((double)var9, (double)var10), var4.noise((double)var11, (double)var10), var4.noise((double)var9, (double)var12), var4.noise((double)var11, (double)var12), var13, var15, var5);
   }

   public static double getTrilinear(int x, int y, int z, double rad, NoiseProvider3 n) {
      return getTrilinear(var0, var1, var2, var3, var3, var3, var5);
   }

   public static double getTrilinear(int x, int y, int z, double radx, double rady, double radz, NoiseProvider3 n) {
      int var10 = getRadiusFactor(var0, var3);
      int var11 = getRadiusFactor(var1, var5);
      int var12 = getRadiusFactor(var2, var7);
      int var13 = (int)Math.round((double)var10 * var3);
      int var14 = (int)Math.round((double)var11 * var5);
      int var15 = (int)Math.round((double)var12 * var7);
      int var16 = (int)Math.round((double)(var10 + 1) * var3);
      int var17 = (int)Math.round((double)(var11 + 1) * var5);
      int var18 = (int)Math.round((double)(var12 + 1) * var7);
      double var19 = rangeScale(0.0D, 1.0D, (double)var13, (double)var16, (double)var0);
      double var21 = rangeScale(0.0D, 1.0D, (double)var14, (double)var17, (double)var1);
      double var23 = rangeScale(0.0D, 1.0D, (double)var15, (double)var18, (double)var2);
      return trilerp(var9.noise((double)var13, (double)var14, (double)var15), var9.noise((double)var16, (double)var14, (double)var15), var9.noise((double)var13, (double)var17, (double)var15), var9.noise((double)var16, (double)var17, (double)var15), var9.noise((double)var13, (double)var14, (double)var18), var9.noise((double)var16, (double)var14, (double)var18), var9.noise((double)var13, (double)var17, (double)var18), var9.noise((double)var16, (double)var17, (double)var18), var19, var21, var23);
   }

   public static double getTricubic(int x, int y, int z, double rad, NoiseProvider3 n) {
      return getTricubic(var0, var1, var2, var3, var3, var3, var5);
   }

   public static double getTricubic(int x, int y, int z, double radx, double rady, double radz, NoiseProvider3 n) {
      int var10 = getRadiusFactor(var0, var3);
      int var11 = getRadiusFactor(var1, var5);
      int var12 = getRadiusFactor(var2, var7);
      int var13 = (int)Math.round((double)(var10 - 1) * var3);
      int var14 = (int)Math.round((double)(var11 - 1) * var5);
      int var15 = (int)Math.round((double)(var12 - 1) * var7);
      int var16 = (int)Math.round((double)var10 * var3);
      int var17 = (int)Math.round((double)var11 * var5);
      int var18 = (int)Math.round((double)var12 * var7);
      int var19 = (int)Math.round((double)(var10 + 1) * var3);
      int var20 = (int)Math.round((double)(var11 + 1) * var5);
      int var21 = (int)Math.round((double)(var12 + 1) * var7);
      int var22 = (int)Math.round((double)(var10 + 2) * var3);
      int var23 = (int)Math.round((double)(var11 + 2) * var5);
      int var24 = (int)Math.round((double)(var12 + 2) * var7);
      double var25 = rangeScale(0.0D, 1.0D, (double)var16, (double)var19, (double)var0);
      double var27 = rangeScale(0.0D, 1.0D, (double)var17, (double)var20, (double)var1);
      double var29 = rangeScale(0.0D, 1.0D, (double)var18, (double)var21, (double)var2);
      return tricubic(var9.noise((double)var13, (double)var14, (double)var15), var9.noise((double)var13, (double)var17, (double)var15), var9.noise((double)var13, (double)var20, (double)var15), var9.noise((double)var13, (double)var23, (double)var15), var9.noise((double)var16, (double)var14, (double)var15), var9.noise((double)var16, (double)var17, (double)var15), var9.noise((double)var16, (double)var20, (double)var15), var9.noise((double)var16, (double)var23, (double)var15), var9.noise((double)var19, (double)var14, (double)var15), var9.noise((double)var19, (double)var17, (double)var15), var9.noise((double)var19, (double)var20, (double)var15), var9.noise((double)var19, (double)var23, (double)var15), var9.noise((double)var22, (double)var14, (double)var15), var9.noise((double)var22, (double)var17, (double)var15), var9.noise((double)var22, (double)var20, (double)var15), var9.noise((double)var22, (double)var23, (double)var15), var9.noise((double)var13, (double)var14, (double)var18), var9.noise((double)var13, (double)var17, (double)var18), var9.noise((double)var13, (double)var20, (double)var18), var9.noise((double)var13, (double)var23, (double)var18), var9.noise((double)var16, (double)var14, (double)var18), var9.noise((double)var16, (double)var17, (double)var18), var9.noise((double)var16, (double)var20, (double)var18), var9.noise((double)var16, (double)var23, (double)var18), var9.noise((double)var19, (double)var14, (double)var18), var9.noise((double)var19, (double)var17, (double)var18), var9.noise((double)var19, (double)var20, (double)var18), var9.noise((double)var19, (double)var23, (double)var18), var9.noise((double)var22, (double)var14, (double)var18), var9.noise((double)var22, (double)var17, (double)var18), var9.noise((double)var22, (double)var20, (double)var18), var9.noise((double)var22, (double)var23, (double)var18), var9.noise((double)var13, (double)var14, (double)var21), var9.noise((double)var13, (double)var17, (double)var21), var9.noise((double)var13, (double)var20, (double)var21), var9.noise((double)var13, (double)var23, (double)var21), var9.noise((double)var16, (double)var14, (double)var21), var9.noise((double)var16, (double)var17, (double)var21), var9.noise((double)var16, (double)var20, (double)var21), var9.noise((double)var16, (double)var23, (double)var21), var9.noise((double)var19, (double)var14, (double)var21), var9.noise((double)var19, (double)var17, (double)var21), var9.noise((double)var19, (double)var20, (double)var21), var9.noise((double)var19, (double)var23, (double)var21), var9.noise((double)var22, (double)var14, (double)var21), var9.noise((double)var22, (double)var17, (double)var21), var9.noise((double)var22, (double)var20, (double)var21), var9.noise((double)var22, (double)var23, (double)var21), var9.noise((double)var13, (double)var14, (double)var24), var9.noise((double)var13, (double)var17, (double)var24), var9.noise((double)var13, (double)var20, (double)var24), var9.noise((double)var13, (double)var23, (double)var24), var9.noise((double)var16, (double)var14, (double)var24), var9.noise((double)var16, (double)var17, (double)var24), var9.noise((double)var16, (double)var20, (double)var24), var9.noise((double)var16, (double)var23, (double)var24), var9.noise((double)var19, (double)var14, (double)var24), var9.noise((double)var19, (double)var17, (double)var24), var9.noise((double)var19, (double)var20, (double)var24), var9.noise((double)var19, (double)var23, (double)var24), var9.noise((double)var22, (double)var14, (double)var24), var9.noise((double)var22, (double)var17, (double)var24), var9.noise((double)var22, (double)var20, (double)var24), var9.noise((double)var22, (double)var23, (double)var24), var25, var27, var29);
   }

   public static double getTrihermite(int x, int y, int z, double rad, NoiseProvider3 n, double tension, double bias) {
      return getTrihermite(var0, var1, var2, var3, var3, var3, var5, var6, var8);
   }

   public static double getTrihermite(int x, int y, int z, double rad, NoiseProvider3 n) {
      return getTrihermite(var0, var1, var2, var3, var3, var3, var5, 0.0D, 0.0D);
   }

   public static double getTrihermite(int x, int y, int z, double radx, double rady, double radz, NoiseProvider3 n) {
      return getTrihermite(var0, var1, var2, var3, var5, var7, var9, 0.0D, 0.0D);
   }

   public static double getTrihermite(int x, int y, int z, double radx, double rady, double radz, NoiseProvider3 n, double tension, double bias) {
      int var14 = getRadiusFactor(var0, var3);
      int var15 = getRadiusFactor(var1, var5);
      int var16 = getRadiusFactor(var2, var7);
      int var17 = (int)Math.round((double)(var14 - 1) * var3);
      int var18 = (int)Math.round((double)(var15 - 1) * var5);
      int var19 = (int)Math.round((double)(var16 - 1) * var7);
      int var20 = (int)Math.round((double)var14 * var3);
      int var21 = (int)Math.round((double)var15 * var5);
      int var22 = (int)Math.round((double)var16 * var7);
      int var23 = (int)Math.round((double)(var14 + 1) * var3);
      int var24 = (int)Math.round((double)(var15 + 1) * var5);
      int var25 = (int)Math.round((double)(var16 + 1) * var7);
      int var26 = (int)Math.round((double)(var14 + 2) * var3);
      int var27 = (int)Math.round((double)(var15 + 2) * var5);
      int var28 = (int)Math.round((double)(var16 + 2) * var7);
      double var29 = rangeScale(0.0D, 1.0D, (double)var20, (double)var23, (double)var0);
      double var31 = rangeScale(0.0D, 1.0D, (double)var21, (double)var24, (double)var1);
      double var33 = rangeScale(0.0D, 1.0D, (double)var22, (double)var25, (double)var2);
      return trihermite(var9.noise((double)var17, (double)var18, (double)var19), var9.noise((double)var17, (double)var21, (double)var19), var9.noise((double)var17, (double)var24, (double)var19), var9.noise((double)var17, (double)var27, (double)var19), var9.noise((double)var20, (double)var18, (double)var19), var9.noise((double)var20, (double)var21, (double)var19), var9.noise((double)var20, (double)var24, (double)var19), var9.noise((double)var20, (double)var27, (double)var19), var9.noise((double)var23, (double)var18, (double)var19), var9.noise((double)var23, (double)var21, (double)var19), var9.noise((double)var23, (double)var24, (double)var19), var9.noise((double)var23, (double)var27, (double)var19), var9.noise((double)var26, (double)var18, (double)var19), var9.noise((double)var26, (double)var21, (double)var19), var9.noise((double)var26, (double)var24, (double)var19), var9.noise((double)var26, (double)var27, (double)var19), var9.noise((double)var17, (double)var18, (double)var22), var9.noise((double)var17, (double)var21, (double)var22), var9.noise((double)var17, (double)var24, (double)var22), var9.noise((double)var17, (double)var27, (double)var22), var9.noise((double)var20, (double)var18, (double)var22), var9.noise((double)var20, (double)var21, (double)var22), var9.noise((double)var20, (double)var24, (double)var22), var9.noise((double)var20, (double)var27, (double)var22), var9.noise((double)var23, (double)var18, (double)var22), var9.noise((double)var23, (double)var21, (double)var22), var9.noise((double)var23, (double)var24, (double)var22), var9.noise((double)var23, (double)var27, (double)var22), var9.noise((double)var26, (double)var18, (double)var22), var9.noise((double)var26, (double)var21, (double)var22), var9.noise((double)var26, (double)var24, (double)var22), var9.noise((double)var26, (double)var27, (double)var22), var9.noise((double)var17, (double)var18, (double)var25), var9.noise((double)var17, (double)var21, (double)var25), var9.noise((double)var17, (double)var24, (double)var25), var9.noise((double)var17, (double)var27, (double)var25), var9.noise((double)var20, (double)var18, (double)var25), var9.noise((double)var20, (double)var21, (double)var25), var9.noise((double)var20, (double)var24, (double)var25), var9.noise((double)var20, (double)var27, (double)var25), var9.noise((double)var23, (double)var18, (double)var25), var9.noise((double)var23, (double)var21, (double)var25), var9.noise((double)var23, (double)var24, (double)var25), var9.noise((double)var23, (double)var27, (double)var25), var9.noise((double)var26, (double)var18, (double)var25), var9.noise((double)var26, (double)var21, (double)var25), var9.noise((double)var26, (double)var24, (double)var25), var9.noise((double)var26, (double)var27, (double)var25), var9.noise((double)var17, (double)var18, (double)var28), var9.noise((double)var17, (double)var21, (double)var28), var9.noise((double)var17, (double)var24, (double)var28), var9.noise((double)var17, (double)var27, (double)var28), var9.noise((double)var20, (double)var18, (double)var28), var9.noise((double)var20, (double)var21, (double)var28), var9.noise((double)var20, (double)var24, (double)var28), var9.noise((double)var20, (double)var27, (double)var28), var9.noise((double)var23, (double)var18, (double)var28), var9.noise((double)var23, (double)var21, (double)var28), var9.noise((double)var23, (double)var24, (double)var28), var9.noise((double)var23, (double)var27, (double)var28), var9.noise((double)var26, (double)var18, (double)var28), var9.noise((double)var26, (double)var21, (double)var28), var9.noise((double)var26, (double)var24, (double)var28), var9.noise((double)var26, (double)var27, (double)var28), var29, var31, var33, var10, var12);
   }

   public static double getBilinearCenterSineNoise(int x, int z, double rad, NoiseProvider n) {
      int var5 = getRadiusFactor(var0, var2);
      int var6 = getRadiusFactor(var1, var2);
      int var7 = (int)Math.round((double)var5 * var2);
      int var8 = (int)Math.round((double)var6 * var2);
      int var9 = (int)Math.round((double)(var5 + 1) * var2);
      int var10 = (int)Math.round((double)(var6 + 1) * var2);
      double var11 = rangeScale(0.0D, 1.0D, (double)var7, (double)var9, (double)var0);
      double var13 = rangeScale(0.0D, 1.0D, (double)var8, (double)var10, (double)var1);
      return blerpSinCenter(var4.noise((double)var7, (double)var8), var4.noise((double)var9, (double)var8), var4.noise((double)var7, (double)var10), var4.noise((double)var9, (double)var10), var11, var13);
   }

   public static double getBicubicNoise(int x, int z, double rad, NoiseProvider n) {
      int var5 = getRadiusFactor(var0, var2);
      int var6 = getRadiusFactor(var1, var2);
      int var7 = (int)Math.round((double)(var5 - 1) * var2);
      int var8 = (int)Math.round((double)(var6 - 1) * var2);
      int var9 = (int)Math.round((double)var5 * var2);
      int var10 = (int)Math.round((double)var6 * var2);
      int var11 = (int)Math.round((double)(var5 + 1) * var2);
      int var12 = (int)Math.round((double)(var6 + 1) * var2);
      int var13 = (int)Math.round((double)(var5 + 2) * var2);
      int var14 = (int)Math.round((double)(var6 + 2) * var2);
      double var15 = rangeScale(0.0D, 1.0D, (double)var9, (double)var11, (double)var0);
      double var17 = rangeScale(0.0D, 1.0D, (double)var10, (double)var12, (double)var1);
      return bicubic(var4.noise((double)var7, (double)var8), var4.noise((double)var7, (double)var10), var4.noise((double)var7, (double)var12), var4.noise((double)var7, (double)var14), var4.noise((double)var9, (double)var8), var4.noise((double)var9, (double)var10), var4.noise((double)var9, (double)var12), var4.noise((double)var9, (double)var14), var4.noise((double)var11, (double)var8), var4.noise((double)var11, (double)var10), var4.noise((double)var11, (double)var12), var4.noise((double)var11, (double)var14), var4.noise((double)var13, (double)var8), var4.noise((double)var13, (double)var10), var4.noise((double)var13, (double)var12), var4.noise((double)var13, (double)var14), var15, var17);
   }

   public static double getBicubicBezierNoise(int x, int z, double rad, NoiseProvider n) {
      int var5 = getRadiusFactor(var0, var2);
      int var6 = getRadiusFactor(var1, var2);
      int var7 = (int)Math.round((double)(var5 - 1) * var2);
      int var8 = (int)Math.round((double)(var6 - 1) * var2);
      int var9 = (int)Math.round((double)var5 * var2);
      int var10 = (int)Math.round((double)var6 * var2);
      int var11 = (int)Math.round((double)(var5 + 1) * var2);
      int var12 = (int)Math.round((double)(var6 + 1) * var2);
      int var13 = (int)Math.round((double)(var5 + 2) * var2);
      int var14 = (int)Math.round((double)(var6 + 2) * var2);
      double var15 = rangeScale(0.0D, 1.0D, (double)var9, (double)var11, (double)var0);
      double var17 = rangeScale(0.0D, 1.0D, (double)var10, (double)var12, (double)var1);
      return bicubicBezier(var4.noise((double)var7, (double)var8), var4.noise((double)var7, (double)var10), var4.noise((double)var7, (double)var12), var4.noise((double)var7, (double)var14), var4.noise((double)var9, (double)var8), var4.noise((double)var9, (double)var10), var4.noise((double)var9, (double)var12), var4.noise((double)var9, (double)var14), var4.noise((double)var11, (double)var8), var4.noise((double)var11, (double)var10), var4.noise((double)var11, (double)var12), var4.noise((double)var11, (double)var14), var4.noise((double)var13, (double)var8), var4.noise((double)var13, (double)var10), var4.noise((double)var13, (double)var12), var4.noise((double)var13, (double)var14), var15, var17);
   }

   public static double getBicubicParametricNoise(int x, int z, double rad, NoiseProvider n, double a) {
      int var7 = getRadiusFactor(var0, var2);
      int var8 = getRadiusFactor(var1, var2);
      int var9 = (int)Math.round((double)(var7 - 1) * var2);
      int var10 = (int)Math.round((double)(var8 - 1) * var2);
      int var11 = (int)Math.round((double)var7 * var2);
      int var12 = (int)Math.round((double)var8 * var2);
      int var13 = (int)Math.round((double)(var7 + 1) * var2);
      int var14 = (int)Math.round((double)(var8 + 1) * var2);
      int var15 = (int)Math.round((double)(var7 + 2) * var2);
      int var16 = (int)Math.round((double)(var8 + 2) * var2);
      double var17 = rangeScale(0.0D, 1.0D, (double)var11, (double)var13, (double)var0);
      double var19 = rangeScale(0.0D, 1.0D, (double)var12, (double)var14, (double)var1);
      return bicubicParametric(var4.noise((double)var9, (double)var10), var4.noise((double)var9, (double)var12), var4.noise((double)var9, (double)var14), var4.noise((double)var9, (double)var16), var4.noise((double)var11, (double)var10), var4.noise((double)var11, (double)var12), var4.noise((double)var11, (double)var14), var4.noise((double)var11, (double)var16), var4.noise((double)var13, (double)var10), var4.noise((double)var13, (double)var12), var4.noise((double)var13, (double)var14), var4.noise((double)var13, (double)var16), var4.noise((double)var15, (double)var10), var4.noise((double)var15, (double)var12), var4.noise((double)var15, (double)var14), var4.noise((double)var15, (double)var16), var17, var19, var5);
   }

   public static double getHermiteNoise(int x, int z, double rad, NoiseProvider n) {
      return getHermiteNoise(var0, var1, var2, var4, 0.5D, 0.0D);
   }

   public static double getHermiteBezierNoise(int x, int z, double rad, NoiseProvider n) {
      return getHermiteBezierNoise(var0, var1, var2, var4, 0.5D, 0.0D);
   }

   public static double getHermiteParametricNoise(int x, int z, double rad, NoiseProvider n, double a) {
      return getHermiteParametricNoise(var0, var1, var2, var4, 0.5D, 0.0D, var5);
   }

   public static double getHermiteNoise(int x, int z, double rad, NoiseProvider n, double t, double b) {
      int var9 = getRadiusFactor(var0, var2);
      int var10 = getRadiusFactor(var1, var2);
      int var11 = (int)Math.round((double)(var9 - 1) * var2);
      int var12 = (int)Math.round((double)(var10 - 1) * var2);
      int var13 = (int)Math.round((double)var9 * var2);
      int var14 = (int)Math.round((double)var10 * var2);
      int var15 = (int)Math.round((double)(var9 + 1) * var2);
      int var16 = (int)Math.round((double)(var10 + 1) * var2);
      int var17 = (int)Math.round((double)(var9 + 2) * var2);
      int var18 = (int)Math.round((double)(var10 + 2) * var2);
      double var19 = rangeScale(0.0D, 1.0D, (double)var13, (double)var15, (double)var0);
      double var21 = rangeScale(0.0D, 1.0D, (double)var14, (double)var16, (double)var1);
      return bihermite(var4.noise((double)var11, (double)var12), var4.noise((double)var11, (double)var14), var4.noise((double)var11, (double)var16), var4.noise((double)var11, (double)var18), var4.noise((double)var13, (double)var12), var4.noise((double)var13, (double)var14), var4.noise((double)var13, (double)var16), var4.noise((double)var13, (double)var18), var4.noise((double)var15, (double)var12), var4.noise((double)var15, (double)var14), var4.noise((double)var15, (double)var16), var4.noise((double)var15, (double)var18), var4.noise((double)var17, (double)var12), var4.noise((double)var17, (double)var14), var4.noise((double)var17, (double)var16), var4.noise((double)var17, (double)var18), var19, var21, var5, var7);
   }

   public static double getHermiteBezierNoise(int x, int z, double rad, NoiseProvider n, double t, double b) {
      int var9 = getRadiusFactor(var0, var2);
      int var10 = getRadiusFactor(var1, var2);
      int var11 = (int)Math.round((double)(var9 - 1) * var2);
      int var12 = (int)Math.round((double)(var10 - 1) * var2);
      int var13 = (int)Math.round((double)var9 * var2);
      int var14 = (int)Math.round((double)var10 * var2);
      int var15 = (int)Math.round((double)(var9 + 1) * var2);
      int var16 = (int)Math.round((double)(var10 + 1) * var2);
      int var17 = (int)Math.round((double)(var9 + 2) * var2);
      int var18 = (int)Math.round((double)(var10 + 2) * var2);
      double var19 = rangeScale(0.0D, 1.0D, (double)var13, (double)var15, (double)var0);
      double var21 = rangeScale(0.0D, 1.0D, (double)var14, (double)var16, (double)var1);
      return bihermiteBezier(var4.noise((double)var11, (double)var12), var4.noise((double)var11, (double)var14), var4.noise((double)var11, (double)var16), var4.noise((double)var11, (double)var18), var4.noise((double)var13, (double)var12), var4.noise((double)var13, (double)var14), var4.noise((double)var13, (double)var16), var4.noise((double)var13, (double)var18), var4.noise((double)var15, (double)var12), var4.noise((double)var15, (double)var14), var4.noise((double)var15, (double)var16), var4.noise((double)var15, (double)var18), var4.noise((double)var17, (double)var12), var4.noise((double)var17, (double)var14), var4.noise((double)var17, (double)var16), var4.noise((double)var17, (double)var18), var19, var21, var5, var7);
   }

   public static double getHermiteParametricNoise(int x, int z, double rad, NoiseProvider n, double t, double b, double a) {
      int var11 = getRadiusFactor(var0, var2);
      int var12 = getRadiusFactor(var1, var2);
      int var13 = (int)Math.round((double)(var11 - 1) * var2);
      int var14 = (int)Math.round((double)(var12 - 1) * var2);
      int var15 = (int)Math.round((double)var11 * var2);
      int var16 = (int)Math.round((double)var12 * var2);
      int var17 = (int)Math.round((double)(var11 + 1) * var2);
      int var18 = (int)Math.round((double)(var12 + 1) * var2);
      int var19 = (int)Math.round((double)(var11 + 2) * var2);
      int var20 = (int)Math.round((double)(var12 + 2) * var2);
      double var21 = rangeScale(0.0D, 1.0D, (double)var15, (double)var17, (double)var0);
      double var23 = rangeScale(0.0D, 1.0D, (double)var16, (double)var18, (double)var1);
      return bihermiteParametric(var4.noise((double)var13, (double)var14), var4.noise((double)var13, (double)var16), var4.noise((double)var13, (double)var18), var4.noise((double)var13, (double)var20), var4.noise((double)var15, (double)var14), var4.noise((double)var15, (double)var16), var4.noise((double)var15, (double)var18), var4.noise((double)var15, (double)var20), var4.noise((double)var17, (double)var14), var4.noise((double)var17, (double)var16), var4.noise((double)var17, (double)var18), var4.noise((double)var17, (double)var20), var4.noise((double)var19, (double)var14), var4.noise((double)var19, (double)var16), var4.noise((double)var19, (double)var18), var4.noise((double)var19, (double)var20), var21, var23, var5, var7, var9);
   }

   public static double getRealRadius(InterpolationMethod method, double h) {
      AtomicDouble var3 = new AtomicDouble(var1);
      new AtomicDouble();
      new AtomicDouble();
      NoiseProvider var6 = (var1x, var3x) -> {
         double var5 = Math.max(Math.abs(var1x), Math.abs(var3x));
         if (var5 > var3.get()) {
            var3.set(var5);
         }

         return 0.0D;
      };
      getNoise(var0, 0, 0, var1, var6);
      return var3.get();
   }

   public static double getNoise3D(InterpolationMethod3D method, int x, int y, int z, double radx, double rady, double radz, NoiseProvider3 n) {
      double var10000;
      switch(var0) {
      case TRILINEAR:
         var10000 = getTrilinear(var1, var2, var3, var4, var6, var8, var10);
         break;
      case TRICUBIC:
         var10000 = getTricubic(var1, var2, var3, var4, var6, var8, var10);
         break;
      case TRIHERMITE:
         var10000 = getTrihermite(var1, var2, var3, var4, var6, var8, var10);
         break;
      case TRISTARCAST_3:
         var10000 = getStarcast3D(var1, var2, var3, var4, 3.0D, var10);
         break;
      case TRISTARCAST_6:
         var10000 = getStarcast3D(var1, var2, var3, var4, 6.0D, var10);
         break;
      case TRISTARCAST_9:
         var10000 = getStarcast3D(var1, var2, var3, var4, 9.0D, var10);
         break;
      case TRISTARCAST_12:
         var10000 = getStarcast3D(var1, var2, var3, var4, 12.0D, var10);
         break;
      case TRILINEAR_TRISTARCAST_3:
         var10000 = getStarcast3D(var1, var2, var3, var4, 3.0D, (var7, var9, var11) -> {
            return getTrilinear((int)var7, (int)var9, (int)var11, var4, var6, var8, var10);
         });
         break;
      case TRILINEAR_TRISTARCAST_6:
         var10000 = getStarcast3D(var1, var2, var3, var4, 6.0D, (var7, var9, var11) -> {
            return getTrilinear((int)var7, (int)var9, (int)var11, var4, var6, var8, var10);
         });
         break;
      case TRILINEAR_TRISTARCAST_9:
         var10000 = getStarcast3D(var1, var2, var3, var4, 9.0D, (var7, var9, var11) -> {
            return getTrilinear((int)var7, (int)var9, (int)var11, var4, var6, var8, var10);
         });
         break;
      case TRILINEAR_TRISTARCAST_12:
         var10000 = getStarcast3D(var1, var2, var3, var4, 12.0D, (var7, var9, var11) -> {
            return getTrilinear((int)var7, (int)var9, (int)var11, var4, var6, var8, var10);
         });
         break;
      case NONE:
         var10000 = var10.noise((double)var1, (double)var2, (double)var3);
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static Hunk<Double> getNoise3D(InterpolationMethod3D method, int xo, int yo, int zo, int w, int h, int d, double rad, NoiseProvider3 n) {
      return getNoise3D(var0, var1, var2, var3, var4, var5, var6, var7, var7, var7, var9);
   }

   public static Hunk<Double> getNoise3D(InterpolationMethod3D method, int xo, int yo, int zo, int w, int h, int d, double radX, double radY, double radZ, NoiseProvider3 n) {
      Hunk var14 = Hunk.newAtomicDoubleHunk(var4, var5, var6);
      HashMap var15 = new HashMap();

      for(int var16 = 0; var16 < var4; ++var16) {
         int var19 = var16;

         for(int var17 = 0; var17 < var5; ++var17) {
            int var20 = var17;

            for(int var18 = 0; var18 < var6; ++var18) {
               var14.set(var16, var17, var18, (Double)var15.computeIfAbsent(var18 * var4 * var5 + var17 * var4 + var16, (var14x) -> {
                  return getNoise3D(var0, var19 + var1, var20 + var2, var18 + var3, var7, var9, var11, var13);
               }));
            }
         }
      }

      return var14;
   }

   public static double getNoise3D(InterpolationMethod3D method, int x, int y, int z, double rad, NoiseProvider3 n) {
      return getNoise3D(var0, var1, var2, var3, var4, var4, var4, var6);
   }

   public static double getNoise(InterpolationMethod method, int x, int z, double h, NoiseProvider noise) {
      HashMap var6 = new HashMap(64);
      NoiseProvider var7 = (var4, var6x) -> {
         return (Double)var6.computeIfAbsent(new IrisInterpolation.NoiseKey(var4 - (double)var1, var6x - (double)var2), (var5x) -> {
            return var5.noise(var4, var6x);
         });
      };
      if (var0.equals(InterpolationMethod.BILINEAR)) {
         return getBilinearNoise(var1, var2, var3, var7);
      } else if (var0.equals(InterpolationMethod.STARCAST_3)) {
         return Starcast.starcast(var1, var2, var3, 3.0D, var7);
      } else if (var0.equals(InterpolationMethod.STARCAST_6)) {
         return Starcast.starcast(var1, var2, var3, 6.0D, var7);
      } else if (var0.equals(InterpolationMethod.STARCAST_9)) {
         return Starcast.starcast(var1, var2, var3, 9.0D, var7);
      } else if (var0.equals(InterpolationMethod.STARCAST_12)) {
         return Starcast.starcast(var1, var2, var3, 12.0D, var7);
      } else if (var0.equals(InterpolationMethod.BILINEAR_STARCAST_3)) {
         return Starcast.starcast(var1, var2, var3, 3.0D, (var3x, var5x) -> {
            return getBilinearNoise((int)var3x, (int)var5x, var3, var7);
         });
      } else if (var0.equals(InterpolationMethod.BILINEAR_STARCAST_6)) {
         return Starcast.starcast(var1, var2, var3, 6.0D, (var3x, var5x) -> {
            return getBilinearNoise((int)var3x, (int)var5x, var3, var7);
         });
      } else if (var0.equals(InterpolationMethod.BILINEAR_STARCAST_9)) {
         return Starcast.starcast(var1, var2, var3, 9.0D, (var3x, var5x) -> {
            return getBilinearNoise((int)var3x, (int)var5x, var3, var7);
         });
      } else if (var0.equals(InterpolationMethod.BILINEAR_STARCAST_12)) {
         return Starcast.starcast(var1, var2, var3, 12.0D, (var3x, var5x) -> {
            return getBilinearNoise((int)var3x, (int)var5x, var3, var7);
         });
      } else if (var0.equals(InterpolationMethod.HERMITE_STARCAST_3)) {
         return Starcast.starcast(var1, var2, var3, 3.0D, (var3x, var5x) -> {
            return getHermiteNoise((int)var3x, (int)var5x, var3, var7, 0.0D, 0.0D);
         });
      } else if (var0.equals(InterpolationMethod.HERMITE_STARCAST_6)) {
         return Starcast.starcast(var1, var2, var3, 6.0D, (var3x, var5x) -> {
            return getHermiteNoise((int)var3x, (int)var5x, var3, var7, 0.0D, 0.0D);
         });
      } else if (var0.equals(InterpolationMethod.HERMITE_STARCAST_9)) {
         return Starcast.starcast(var1, var2, var3, 9.0D, (var3x, var5x) -> {
            return getHermiteNoise((int)var3x, (int)var5x, var3, var7, 0.0D, 0.0D);
         });
      } else if (var0.equals(InterpolationMethod.HERMITE_STARCAST_12)) {
         return Starcast.starcast(var1, var2, var3, 12.0D, (var3x, var5x) -> {
            return getHermiteNoise((int)var3x, (int)var5x, var3, var7, 0.0D, 0.0D);
         });
      } else if (var0.equals(InterpolationMethod.BILINEAR_BEZIER)) {
         return getBilinearBezierNoise(var1, var2, var3, var7);
      } else if (var0.equals(InterpolationMethod.BILINEAR_PARAMETRIC_2)) {
         return getBilinearParametricNoise(var1, var2, var3, var7, 2.0D);
      } else if (var0.equals(InterpolationMethod.BILINEAR_PARAMETRIC_4)) {
         return getBilinearParametricNoise(var1, var2, var3, var7, 4.0D);
      } else if (var0.equals(InterpolationMethod.BILINEAR_PARAMETRIC_1_5)) {
         return getBilinearParametricNoise(var1, var2, var3, var7, 1.5D);
      } else if (var0.equals(InterpolationMethod.BICUBIC)) {
         return getBilinearNoise(var1, var2, var3, var7);
      } else if (var0.equals(InterpolationMethod.HERMITE)) {
         return getHermiteNoise(var1, var2, var3, var7);
      } else if (var0.equals(InterpolationMethod.HERMITE_TENSE)) {
         return getHermiteNoise(var1, var2, var3, var7, 0.8D, 0.0D);
      } else if (var0.equals(InterpolationMethod.CATMULL_ROM_SPLINE)) {
         return getHermiteNoise(var1, var2, var3, var7, 1.0D, 0.0D);
      } else if (var0.equals(InterpolationMethod.HERMITE_LOOSE)) {
         return getHermiteNoise(var1, var2, var3, var7, 0.0D, 0.0D);
      } else if (var0.equals(InterpolationMethod.HERMITE_LOOSE_HALF_NEGATIVE_BIAS)) {
         return getHermiteNoise(var1, var2, var3, var7, 0.0D, -0.5D);
      } else if (var0.equals(InterpolationMethod.HERMITE_LOOSE_HALF_POSITIVE_BIAS)) {
         return getHermiteNoise(var1, var2, var3, var7, 0.0D, 0.5D);
      } else if (var0.equals(InterpolationMethod.HERMITE_LOOSE_FULL_NEGATIVE_BIAS)) {
         return getHermiteNoise(var1, var2, var3, var7, 0.0D, -1.0D);
      } else {
         return var0.equals(InterpolationMethod.HERMITE_LOOSE_FULL_POSITIVE_BIAS) ? getHermiteNoise(var1, var2, var3, var7, 0.0D, 1.0D) : var7.noise((double)var1, (double)var2);
      }
   }

   public static double rangeScale(double amin, double amax, double bmin, double bmax, double b) {
      return var0 + (var2 - var0) * ((var8 - var4) / (var6 - var4));
   }

   static {
      cng = NoiseStyle.SIMPLEX.create(new RNG());
   }

   public static record NoiseKey(double x, double z) {
      public NoiseKey(double x, double z) {
         this.x = var1;
         this.z = var3;
      }

      public double x() {
         return this.x;
      }

      public double z() {
         return this.z;
      }
   }
}
