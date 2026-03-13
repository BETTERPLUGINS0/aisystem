package com.volmit.iris.util.stream.interpolation;

import com.volmit.iris.util.interpolation.IrisInterpolation;
import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class BiHermiteStream<T> extends BasicStream<T> implements Interpolator<T> {
   private final int rx;
   private final int ry;
   private final double tension;
   private final double bias;

   public BiHermiteStream(ProceduralStream<T> stream, int rx, int ry, double tension, double bias) {
      super(var1);
      this.rx = var2;
      this.ry = var3;
      this.tension = var4;
      this.bias = var6;
   }

   public BiHermiteStream(ProceduralStream<T> stream, int rx, int ry) {
      this(var1, var2, var3, 0.5D, 0.0D);
   }

   public T interpolate(double x, double y) {
      int var5 = (int)Math.floor(var1 / (double)this.rx);
      int var6 = (int)Math.floor(var3 / (double)this.ry);
      int var7 = Math.round((float)((var5 - 1) * this.rx));
      int var8 = Math.round((float)((var6 - 1) * this.ry));
      int var9 = Math.round((float)(var5 * this.rx));
      int var10 = Math.round((float)(var6 * this.ry));
      int var11 = Math.round((float)((var5 + 1) * this.rx));
      int var12 = Math.round((float)((var6 + 1) * this.ry));
      int var13 = Math.round((float)((var5 + 2) * this.rx));
      int var14 = Math.round((float)((var6 + 2) * this.ry));
      double var15 = IrisInterpolation.rangeScale(0.0D, 1.0D, (double)var9, (double)var11, var1);
      double var17 = IrisInterpolation.rangeScale(0.0D, 1.0D, (double)var10, (double)var12, var3);
      return this.getTypedSource().fromDouble(IrisInterpolation.bihermite(this.getTypedSource().getDouble((double)var7, (double)var8), this.getTypedSource().getDouble((double)var7, (double)var10), this.getTypedSource().getDouble((double)var7, (double)var12), this.getTypedSource().getDouble((double)var7, (double)var14), this.getTypedSource().getDouble((double)var9, (double)var8), this.getTypedSource().getDouble((double)var9, (double)var10), this.getTypedSource().getDouble((double)var9, (double)var12), this.getTypedSource().getDouble((double)var9, (double)var14), this.getTypedSource().getDouble((double)var11, (double)var8), this.getTypedSource().getDouble((double)var11, (double)var10), this.getTypedSource().getDouble((double)var11, (double)var12), this.getTypedSource().getDouble((double)var11, (double)var14), this.getTypedSource().getDouble((double)var13, (double)var8), this.getTypedSource().getDouble((double)var13, (double)var10), this.getTypedSource().getDouble((double)var13, (double)var12), this.getTypedSource().getDouble((double)var13, (double)var14), var15, var17, this.tension, this.bias));
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
