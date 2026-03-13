package com.volmit.iris.util.math;

import java.io.Serializable;

public class Point3f extends Tuple3f implements Serializable {
   static final long serialVersionUID = -8689337816398030143L;

   public Point3f(float x, float y, float z) {
      super(var1, var2, var3);
   }

   public Point3f(float[] p) {
      super(var1);
   }

   public Point3f(Point3f p1) {
      super((Tuple3f)var1);
   }

   public Point3f(Point3d p1) {
      super((Tuple3d)var1);
   }

   public Point3f(Tuple3f t1) {
      super(var1);
   }

   public Point3f(Tuple3d t1) {
      super(var1);
   }

   public Point3f() {
   }

   public final float distanceSquared(Point3f p1) {
      float var2 = this.x - var1.x;
      float var3 = this.y - var1.y;
      float var4 = this.z - var1.z;
      return var2 * var2 + var3 * var3 + var4 * var4;
   }

   public final float distance(Point3f p1) {
      float var2 = this.x - var1.x;
      float var3 = this.y - var1.y;
      float var4 = this.z - var1.z;
      return (float)Math.sqrt((double)(var2 * var2 + var3 * var3 + var4 * var4));
   }

   public final float distanceL1(Point3f p1) {
      return Math.abs(this.x - var1.x) + Math.abs(this.y - var1.y) + Math.abs(this.z - var1.z);
   }

   public final float distanceLinf(Point3f p1) {
      float var2 = Math.max(Math.abs(this.x - var1.x), Math.abs(this.y - var1.y));
      return Math.max(var2, Math.abs(this.z - var1.z));
   }

   public final void project(Point4f p1) {
      float var2 = 1.0F / var1.w;
      this.x = var1.x * var2;
      this.y = var1.y * var2;
      this.z = var1.z * var2;
   }
}
