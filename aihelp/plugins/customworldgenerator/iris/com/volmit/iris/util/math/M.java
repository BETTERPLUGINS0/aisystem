package com.volmit.iris.util.math;

import java.util.regex.Matcher;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class M {
   private static final int precision = 128;
   private static final int modulus = 46080;
   private static final float[] sin = new float['됀'];
   public static int tick = 0;

   public static double rangeScale(double amin, double amax, double bmin, double bmax, double b) {
      return var0 + (var2 - var0) * ((var8 - var4) / (var6 - var4));
   }

   public static double lerpInverse(double from, double to, double at) {
      return rangeScale(0.0D, 1.0D, var0, var2, var4);
   }

   public static double lerp(double a, double b, double f) {
      return var0 + var4 * (var2 - var0);
   }

   public static double bilerp(double a, double b, double c, double d, double x, double y) {
      return lerp(lerp(var0, var2, var8), lerp(var4, var6, var8), var10);
   }

   public static double trilerp(double a, double b, double c, double d, double e, double f, double g, double h, double x, double y, double z) {
      return lerp(bilerp(var0, var2, var4, var6, var16, var18), bilerp(var8, var10, var12, var14, var16, var18), var20);
   }

   public static <T extends Number> T clip(T value, T min, T max) {
      return Math.min(var2.doubleValue(), Math.max(var1.doubleValue(), var0.doubleValue()));
   }

   public static boolean r(Double d) {
      if (var0 == null) {
         return Math.random() < 0.5D;
      } else {
         return Math.random() < var0;
      }
   }

   public static double tps(long ns, int rad) {
      return 20.0D * ((double)var0 / 5.0E7D) / (double)var2;
   }

   public static double ticksFromNS(long ns) {
      return (double)var0 / 5.0E7D;
   }

   public static int irand(int f, int t) {
      return var0 + (int)(Math.random() * (double)(var1 - var0 + 1));
   }

   public static float frand(float f, float t) {
      return var0 + (float)(Math.random() * (double)(var1 - var0 + 1.0F));
   }

   public static double drand(double f, double t) {
      return var0 + Math.random() * (var2 - var0 + 1.0D);
   }

   public static long ns() {
      return System.nanoTime();
   }

   public static long ms() {
      return System.currentTimeMillis();
   }

   public static float sin(float a) {
      return sinLookup((int)(var0 * 128.0F + 0.5F));
   }

   public static float cos(float a) {
      return sinLookup((int)((var0 + 90.0F) * 128.0F + 0.5F));
   }

   public static float tan(float a) {
      float var1 = cos(var0);
      return sin(var0) / (var1 == 0.0F ? 1.0E-7F : var1);
   }

   public static <T extends Number> T max(T... doubles) {
      double var1 = Double.MIN_VALUE;
      Number[] var3 = var0;
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Number var6 = var3[var5];
         if (var6.doubleValue() > var1) {
            var1 = var6.doubleValue();
         }
      }

      return var1;
   }

   public static <T extends Number> T min(T... doubles) {
      double var1 = Double.MAX_VALUE;
      Number[] var3 = var0;
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Number var6 = var3[var5];
         if (var6.doubleValue() < var1) {
            var1 = var6.doubleValue();
         }
      }

      return var1;
   }

   public static double evaluate(String expression, Double... args) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         String var3 = "$" + var2;
         if (var0.contains(var3)) {
            var0 = var0.replaceAll(Matcher.quoteReplacement(var3), var1[var2].makeConcatWithConstants<invokedynamic>(var1[var2]));
         }
      }

      return evaluate(var0);
   }

   public static double evaluate(String expression) {
      ScriptEngineManager var1 = new ScriptEngineManager();
      ScriptEngine var2 = var1.getEngineByName("JavaScript");
      return Double.parseDouble(var2.eval(var0).toString());
   }

   public static boolean within(int from, int to, int is) {
      return var2 >= var0 && var2 <= var1;
   }

   public static long epochDays() {
      return epochDays(ms());
   }

   private static long epochDays(long ms) {
      return var0 / 1000L / 60L / 60L / 24L;
   }

   private static float sinLookup(int a) {
      return var0 >= 0 ? sin[var0 % '됀'] : -sin[-var0 % '됀'];
   }

   public static boolean interval(int tickInterval) {
      return tick % (var0 <= 0 ? 1 : var0) == 0;
   }

   static {
      for(int var0 = 0; var0 < sin.length; ++var0) {
         sin[var0] = (float)Math.sin((double)var0 * 3.141592653589793D / 23040.0D);
      }

   }
}
