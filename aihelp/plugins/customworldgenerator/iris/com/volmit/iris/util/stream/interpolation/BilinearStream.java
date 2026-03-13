package com.volmit.iris.util.stream.interpolation;

import com.volmit.iris.util.interpolation.IrisInterpolation;
import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class BilinearStream<T> extends BasicStream<T> implements Interpolator<T> {
   private final int rx;
   private final int ry;

   public BilinearStream(ProceduralStream<T> stream, int rx, int ry) {
      super(var1);
      this.rx = var2;
      this.ry = var3;
   }

   public T interpolate(double x, double y) {
      int var5 = (int)Math.floor(var1 / (double)this.rx);
      int var6 = (int)Math.floor(var3 / (double)this.ry);
      int var7 = Math.round((float)(var5 * this.rx));
      int var8 = Math.round((float)(var6 * this.ry));
      int var9 = Math.round((float)((var5 + 1) * this.rx));
      int var10 = Math.round((float)((var6 + 1) * this.ry));
      double var11 = IrisInterpolation.rangeScale(0.0D, 1.0D, (double)var7, (double)var9, var1);
      double var13 = IrisInterpolation.rangeScale(0.0D, 1.0D, (double)var8, (double)var10, var3);
      return this.getTypedSource().fromDouble(IrisInterpolation.blerp(this.getTypedSource().getDouble((double)var7, (double)var8), this.getTypedSource().getDouble((double)var9, (double)var8), this.getTypedSource().getDouble((double)var7, (double)var10), this.getTypedSource().getDouble((double)var9, (double)var10), var11, var13));
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
