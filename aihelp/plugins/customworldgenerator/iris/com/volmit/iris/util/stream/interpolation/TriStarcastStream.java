package com.volmit.iris.util.stream.interpolation;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class TriStarcastStream<T> extends BasicStream<T> implements Interpolator<T> {
   private final int rad;
   private final int checks;

   public TriStarcastStream(ProceduralStream<T> stream, int rad, int checks) {
      super(var1);
      this.rad = var2;
      this.checks = var3;
   }

   public T interpolate(double x, double y, double z) {
      double var7 = 360.0D / (double)this.checks;
      double var9 = 0.0D;

      for(int var11 = 0; var11 < 360; var11 = (int)((double)var11 + var7)) {
         double var12 = Math.sin(Math.toRadians((double)var11));
         double var14 = Math.cos(Math.toRadians((double)var11));
         double var16 = var1 + ((double)this.rad * var14 - (double)this.rad * var12);
         double var18 = var3 + (double)this.rad * var12 + (double)this.rad * var14;
         double var20 = var5 + ((double)this.rad * var14 - (double)this.rad * var12);
         var9 += this.getTypedSource().getDouble(var16, var18, var20);
      }

      return this.getTypedSource().fromDouble(var9 / (double)this.checks);
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }

   public T get(double x, double z) {
      return this.interpolate(var1, 0.0D, var3);
   }

   public T get(double x, double y, double z) {
      return this.interpolate(var1, var3, var5);
   }
}
