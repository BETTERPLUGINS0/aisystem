package com.volmit.iris.util.stream.interpolation;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class BiStarcastStream<T> extends BasicStream<T> implements Interpolator<T> {
   private final int rad;
   private final int checks;

   public BiStarcastStream(ProceduralStream<T> stream, int rad, int checks) {
      super(var1);
      this.rad = var2;
      this.checks = var3;
   }

   public T interpolate(double x, double y) {
      double var5 = 360.0D / (double)this.checks;
      double var7 = 0.0D;

      for(int var9 = 0; var9 < 360; var9 = (int)((double)var9 + var5)) {
         double var10 = Math.sin(Math.toRadians((double)var9));
         double var12 = Math.cos(Math.toRadians((double)var9));
         double var14 = var1 + ((double)this.rad * var12 - (double)this.rad * var10);
         double var16 = var3 + (double)this.rad * var10 + (double)this.rad * var12;
         var7 += this.getTypedSource().getDouble(var14, var16);
      }

      return this.getTypedSource().fromDouble(var7 / (double)this.checks);
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }

   public T get(double x, double z) {
      return this.interpolate(var1, var3);
   }

   public T get(double x, double y, double z) {
      return this.interpolate(var1, var5);
   }
}
