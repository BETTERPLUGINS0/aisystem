package com.volmit.iris.util.stream.arithmetic;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class SlopeStream<T> extends BasicStream<T> {
   private final int range;

   public SlopeStream(ProceduralStream<T> stream, int range) {
      super(var1);
      this.range = var2;
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }

   public T get(double x, double z) {
      double var5 = this.getTypedSource().getDouble(var1, var3);
      double var7 = this.getTypedSource().getDouble(var1 + (double)this.range, var3) - var5;
      double var9 = this.getTypedSource().getDouble(var1, var3 + (double)this.range) - var5;
      return this.fromDouble(Math.sqrt(var7 * var7 + var9 * var9));
   }

   public T get(double x, double y, double z) {
      double var7 = this.getTypedSource().getDouble(var1, var3, var5);
      double var9 = this.getTypedSource().getDouble(var1 + (double)this.range, var3, var5) - var7;
      double var11 = this.getTypedSource().getDouble(var1, var3 + (double)this.range, var5) - var7;
      double var13 = this.getTypedSource().getDouble(var1, var3, var5 + (double)this.range) - var7;
      return this.fromDouble(Math.cbrt(var9 * var9 + var11 * var11 + var13 * var13));
   }
}
