package com.volmit.iris.util.stream.interpolation;

import com.volmit.iris.util.interpolation.IrisInterpolation;
import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class TrilinearStream<T> extends BasicStream<T> implements Interpolator<T> {
   private final int rx;
   private final int ry;
   private final int rz;

   public TrilinearStream(ProceduralStream<T> stream, int rx, int ry, int rz) {
      super(var1);
      this.rx = var2;
      this.ry = var3;
      this.rz = var4;
   }

   public T interpolate(double x, double y, double z) {
      int var7 = (int)Math.floor(var1 / (double)this.rx);
      int var8 = (int)Math.floor(var3 / (double)this.ry);
      int var9 = (int)Math.floor(var5 / (double)this.rz);
      int var10 = Math.round((float)(var7 * this.rx));
      int var11 = Math.round((float)(var8 * this.ry));
      int var12 = Math.round((float)(var9 * this.rz));
      int var13 = Math.round((float)((var7 + 1) * this.rx));
      int var14 = Math.round((float)((var8 + 1) * this.ry));
      int var15 = Math.round((float)((var9 + 1) * this.rz));
      double var16 = IrisInterpolation.rangeScale(0.0D, 1.0D, (double)var10, (double)var13, var1);
      double var18 = IrisInterpolation.rangeScale(0.0D, 1.0D, (double)var11, (double)var14, var3);
      double var20 = IrisInterpolation.rangeScale(0.0D, 1.0D, (double)var12, (double)var15, var5);
      return this.getTypedSource().fromDouble(IrisInterpolation.trilerp(this.getTypedSource().getDouble((double)var10, (double)var11, (double)var12), this.getTypedSource().getDouble((double)var13, (double)var11, (double)var12), this.getTypedSource().getDouble((double)var10, (double)var11, (double)var15), this.getTypedSource().getDouble((double)var13, (double)var11, (double)var15), this.getTypedSource().getDouble((double)var10, (double)var14, (double)var12), this.getTypedSource().getDouble((double)var13, (double)var14, (double)var12), this.getTypedSource().getDouble((double)var10, (double)var14, (double)var15), this.getTypedSource().getDouble((double)var13, (double)var14, (double)var15), var16, var20, var18));
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
