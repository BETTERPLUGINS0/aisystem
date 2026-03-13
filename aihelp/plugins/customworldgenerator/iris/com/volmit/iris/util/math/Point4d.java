package com.volmit.iris.util.math;

import java.io.Serializable;

public class Point4d extends Tuple4d implements Serializable {
   static final long serialVersionUID = 1733471895962736949L;

   public Point4d(double x, double y, double z, double w) {
      super(var1, var3, var5, var7);
   }

   public Point4d(double[] p) {
      super(var1);
   }

   public Point4d(Point4d p1) {
      super((Tuple4d)var1);
   }

   public Point4d(Point4f p1) {
      super((Tuple4f)var1);
   }

   public Point4d(Tuple4f t1) {
      super(var1);
   }

   public Point4d(Tuple4d t1) {
      super(var1);
   }

   public Point4d(Tuple3d t1) {
      super(var1.x, var1.y, var1.z, 1.0D);
   }

   public Point4d() {
   }

   public final void set(Tuple3d t1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = 1.0D;
   }

   public final double distanceSquared(Point4d p1) {
      double var2 = this.x - var1.x;
      double var4 = this.y - var1.y;
      double var6 = this.z - var1.z;
      double var8 = this.w - var1.w;
      return var2 * var2 + var4 * var4 + var6 * var6 + var8 * var8;
   }

   public final double distance(Point4d p1) {
      double var2 = this.x - var1.x;
      double var4 = this.y - var1.y;
      double var6 = this.z - var1.z;
      double var8 = this.w - var1.w;
      return Math.sqrt(var2 * var2 + var4 * var4 + var6 * var6 + var8 * var8);
   }

   public final double distanceL1(Point4d p1) {
      return Math.abs(this.x - var1.x) + Math.abs(this.y - var1.y) + Math.abs(this.z - var1.z) + Math.abs(this.w - var1.w);
   }

   public final double distanceLinf(Point4d p1) {
      double var2 = Math.max(Math.abs(this.x - var1.x), Math.abs(this.y - var1.y));
      double var4 = Math.max(Math.abs(this.z - var1.z), Math.abs(this.w - var1.w));
      return Math.max(var2, var4);
   }

   public final void project(Point4d p1) {
      double var2 = 1.0D / var1.w;
      this.x = var1.x * var2;
      this.y = var1.y * var2;
      this.z = var1.z * var2;
      this.w = 1.0D;
   }
}
