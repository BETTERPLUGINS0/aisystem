package com.volmit.iris.util.stream.interpolation;

import com.volmit.iris.util.interpolation.IrisInterpolation;
import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class TriHermiteStream<T> extends BasicStream<T> implements Interpolator<T> {
   private final int rx;
   private final int ry;
   private final int rz;
   private final double tension;
   private final double bias;

   public TriHermiteStream(ProceduralStream<T> stream, int rx, int ry, int rz, double tension, double bias) {
      super(var1);
      this.rx = var2;
      this.ry = var3;
      this.rz = var4;
      this.tension = var5;
      this.bias = var7;
   }

   public T interpolate(double x, double y, double z) {
      int var7 = (int)Math.floor(var1 / (double)this.rx);
      int var8 = (int)Math.floor(var3 / (double)this.ry);
      int var9 = (int)Math.floor(var5 / (double)this.rz);
      int var10 = Math.round((float)((var7 - 1) * this.rx));
      int var11 = Math.round((float)((var8 - 1) * this.ry));
      int var12 = Math.round((float)((var9 - 1) * this.rz));
      int var13 = Math.round((float)(var7 * this.rx));
      int var14 = Math.round((float)(var8 * this.ry));
      int var15 = Math.round((float)(var9 * this.rz));
      int var16 = Math.round((float)((var7 + 1) * this.rx));
      int var17 = Math.round((float)((var8 + 1) * this.ry));
      int var18 = Math.round((float)((var9 + 1) * this.rz));
      int var19 = Math.round((float)((var7 + 2) * this.rx));
      int var20 = Math.round((float)((var8 + 2) * this.ry));
      int var21 = Math.round((float)((var9 + 2) * this.rz));
      double var22 = IrisInterpolation.rangeScale(0.0D, 1.0D, (double)var13, (double)var16, var1);
      double var24 = IrisInterpolation.rangeScale(0.0D, 1.0D, (double)var14, (double)var17, var3);
      double var26 = IrisInterpolation.rangeScale(0.0D, 1.0D, (double)var15, (double)var18, var5);
      return this.getTypedSource().fromDouble(IrisInterpolation.trihermite(this.getTypedSource().getDouble((double)var10, (double)var11, (double)var12), this.getTypedSource().getDouble((double)var10, (double)var11, (double)var15), this.getTypedSource().getDouble((double)var10, (double)var11, (double)var18), this.getTypedSource().getDouble((double)var10, (double)var11, (double)var21), this.getTypedSource().getDouble((double)var13, (double)var11, (double)var12), this.getTypedSource().getDouble((double)var13, (double)var11, (double)var15), this.getTypedSource().getDouble((double)var13, (double)var11, (double)var18), this.getTypedSource().getDouble((double)var13, (double)var11, (double)var21), this.getTypedSource().getDouble((double)var16, (double)var11, (double)var12), this.getTypedSource().getDouble((double)var16, (double)var11, (double)var15), this.getTypedSource().getDouble((double)var16, (double)var11, (double)var18), this.getTypedSource().getDouble((double)var16, (double)var11, (double)var21), this.getTypedSource().getDouble((double)var19, (double)var11, (double)var12), this.getTypedSource().getDouble((double)var19, (double)var11, (double)var15), this.getTypedSource().getDouble((double)var19, (double)var11, (double)var18), this.getTypedSource().getDouble((double)var19, (double)var11, (double)var21), this.getTypedSource().getDouble((double)var10, (double)var14, (double)var12), this.getTypedSource().getDouble((double)var10, (double)var14, (double)var15), this.getTypedSource().getDouble((double)var10, (double)var14, (double)var18), this.getTypedSource().getDouble((double)var10, (double)var14, (double)var21), this.getTypedSource().getDouble((double)var13, (double)var14, (double)var12), this.getTypedSource().getDouble((double)var13, (double)var14, (double)var15), this.getTypedSource().getDouble((double)var13, (double)var14, (double)var18), this.getTypedSource().getDouble((double)var13, (double)var14, (double)var21), this.getTypedSource().getDouble((double)var16, (double)var14, (double)var12), this.getTypedSource().getDouble((double)var16, (double)var14, (double)var15), this.getTypedSource().getDouble((double)var16, (double)var14, (double)var18), this.getTypedSource().getDouble((double)var16, (double)var14, (double)var21), this.getTypedSource().getDouble((double)var19, (double)var14, (double)var12), this.getTypedSource().getDouble((double)var19, (double)var14, (double)var15), this.getTypedSource().getDouble((double)var19, (double)var14, (double)var18), this.getTypedSource().getDouble((double)var19, (double)var14, (double)var21), this.getTypedSource().getDouble((double)var10, (double)var17, (double)var12), this.getTypedSource().getDouble((double)var10, (double)var17, (double)var15), this.getTypedSource().getDouble((double)var10, (double)var17, (double)var18), this.getTypedSource().getDouble((double)var10, (double)var17, (double)var21), this.getTypedSource().getDouble((double)var13, (double)var17, (double)var12), this.getTypedSource().getDouble((double)var13, (double)var17, (double)var15), this.getTypedSource().getDouble((double)var13, (double)var17, (double)var18), this.getTypedSource().getDouble((double)var13, (double)var17, (double)var21), this.getTypedSource().getDouble((double)var16, (double)var17, (double)var12), this.getTypedSource().getDouble((double)var16, (double)var17, (double)var15), this.getTypedSource().getDouble((double)var16, (double)var17, (double)var18), this.getTypedSource().getDouble((double)var16, (double)var17, (double)var21), this.getTypedSource().getDouble((double)var19, (double)var17, (double)var12), this.getTypedSource().getDouble((double)var19, (double)var17, (double)var15), this.getTypedSource().getDouble((double)var19, (double)var17, (double)var18), this.getTypedSource().getDouble((double)var19, (double)var17, (double)var21), this.getTypedSource().getDouble((double)var10, (double)var20, (double)var12), this.getTypedSource().getDouble((double)var10, (double)var20, (double)var15), this.getTypedSource().getDouble((double)var10, (double)var20, (double)var18), this.getTypedSource().getDouble((double)var10, (double)var20, (double)var21), this.getTypedSource().getDouble((double)var13, (double)var20, (double)var12), this.getTypedSource().getDouble((double)var13, (double)var20, (double)var15), this.getTypedSource().getDouble((double)var13, (double)var20, (double)var18), this.getTypedSource().getDouble((double)var13, (double)var20, (double)var21), this.getTypedSource().getDouble((double)var16, (double)var20, (double)var12), this.getTypedSource().getDouble((double)var16, (double)var20, (double)var15), this.getTypedSource().getDouble((double)var16, (double)var20, (double)var18), this.getTypedSource().getDouble((double)var16, (double)var20, (double)var21), this.getTypedSource().getDouble((double)var19, (double)var20, (double)var12), this.getTypedSource().getDouble((double)var19, (double)var20, (double)var15), this.getTypedSource().getDouble((double)var19, (double)var20, (double)var18), this.getTypedSource().getDouble((double)var19, (double)var20, (double)var21), var22, var26, var24, this.tension, this.bias));
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
