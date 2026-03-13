package com.volmit.iris.util.math;

import java.io.Serializable;

public class Point4f extends Tuple4f implements Serializable {
   static final long serialVersionUID = 4643134103185764459L;

   public Point4f(float x, float y, float z, float w) {
      super(var1, var2, var3, var4);
   }

   public Point4f(float[] p) {
      super(var1);
   }

   public Point4f(Point4f p1) {
      super((Tuple4f)var1);
   }

   public Point4f(Point4d p1) {
      super((Tuple4d)var1);
   }

   public Point4f(Tuple4f t1) {
      super(var1);
   }

   public Point4f(Tuple4d t1) {
      super(var1);
   }

   public Point4f(Tuple3f t1) {
      super(var1.x, var1.y, var1.z, 1.0F);
   }

   public Point4f() {
   }

   public final void set(Tuple3f t1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = 1.0F;
   }

   public final float distanceSquared(Point4f p1) {
      float var2 = this.x - var1.x;
      float var3 = this.y - var1.y;
      float var4 = this.z - var1.z;
      float var5 = this.w - var1.w;
      return var2 * var2 + var3 * var3 + var4 * var4 + var5 * var5;
   }

   public final float distance(Point4f p1) {
      float var2 = this.x - var1.x;
      float var3 = this.y - var1.y;
      float var4 = this.z - var1.z;
      float var5 = this.w - var1.w;
      return (float)Math.sqrt((double)(var2 * var2 + var3 * var3 + var4 * var4 + var5 * var5));
   }

   public final float distanceL1(Point4f p1) {
      return Math.abs(this.x - var1.x) + Math.abs(this.y - var1.y) + Math.abs(this.z - var1.z) + Math.abs(this.w - var1.w);
   }

   public final float distanceLinf(Point4f p1) {
      float var2 = Math.max(Math.abs(this.x - var1.x), Math.abs(this.y - var1.y));
      float var3 = Math.max(Math.abs(this.z - var1.z), Math.abs(this.w - var1.w));
      return Math.max(var2, var3);
   }

   public final void project(Point4f p1) {
      float var2 = 1.0F / var1.w;
      this.x = var1.x * var2;
      this.y = var1.y * var2;
      this.z = var1.z * var2;
      this.w = 1.0F;
   }
}
