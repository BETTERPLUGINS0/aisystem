package com.volmit.iris.util.math;

import com.volmit.iris.Iris;
import java.io.Serializable;

public abstract class Tuple4d implements Serializable, Cloneable {
   static final long serialVersionUID = -4748953690425311052L;
   public double x;
   public double y;
   public double z;
   public double w;

   public Tuple4d(double x, double y, double z, double w) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.w = var7;
   }

   public Tuple4d(double[] t) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[3];
   }

   public Tuple4d(Tuple4d t1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = var1.w;
   }

   public Tuple4d(Tuple4f t1) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
      this.z = (double)var1.z;
      this.w = (double)var1.w;
   }

   public Tuple4d() {
      this.x = 0.0D;
      this.y = 0.0D;
      this.z = 0.0D;
      this.w = 0.0D;
   }

   public final void set(double x, double y, double z, double w) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.w = var7;
   }

   public final void set(double[] t) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[3];
   }

   public final void set(Tuple4d t1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = var1.w;
   }

   public final void set(Tuple4f t1) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
      this.z = (double)var1.z;
      this.w = (double)var1.w;
   }

   public final void get(double[] t) {
      var1[0] = this.x;
      var1[1] = this.y;
      var1[2] = this.z;
      var1[3] = this.w;
   }

   public final void get(Tuple4d t) {
      var1.x = this.x;
      var1.y = this.y;
      var1.z = this.z;
      var1.w = this.w;
   }

   public final void add(Tuple4d t1, Tuple4d t2) {
      this.x = var1.x + var2.x;
      this.y = var1.y + var2.y;
      this.z = var1.z + var2.z;
      this.w = var1.w + var2.w;
   }

   public final void add(Tuple4d t1) {
      this.x += var1.x;
      this.y += var1.y;
      this.z += var1.z;
      this.w += var1.w;
   }

   public final void sub(Tuple4d t1, Tuple4d t2) {
      this.x = var1.x - var2.x;
      this.y = var1.y - var2.y;
      this.z = var1.z - var2.z;
      this.w = var1.w - var2.w;
   }

   public final void sub(Tuple4d t1) {
      this.x -= var1.x;
      this.y -= var1.y;
      this.z -= var1.z;
      this.w -= var1.w;
   }

   public final void negate(Tuple4d t1) {
      this.x = -var1.x;
      this.y = -var1.y;
      this.z = -var1.z;
      this.w = -var1.w;
   }

   public final void negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      this.w = -this.w;
   }

   public final void scale(double s, Tuple4d t1) {
      this.x = var1 * var3.x;
      this.y = var1 * var3.y;
      this.z = var1 * var3.z;
      this.w = var1 * var3.w;
   }

   public final void scale(double s) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      this.w *= var1;
   }

   public final void scaleAdd(double s, Tuple4d t1, Tuple4d t2) {
      this.x = var1 * var3.x + var4.x;
      this.y = var1 * var3.y + var4.y;
      this.z = var1 * var3.z + var4.z;
      this.w = var1 * var3.w + var4.w;
   }

   /** @deprecated */
   @Deprecated
   public final void scaleAdd(float s, Tuple4d t1) {
      this.scaleAdd((double)var1, var2);
   }

   public final void scaleAdd(double s, Tuple4d t1) {
      this.x = var1 * this.x + var3.x;
      this.y = var1 * this.y + var3.y;
      this.z = var1 * this.z + var3.z;
      this.w = var1 * this.w + var3.w;
   }

   public String toString() {
      return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
   }

   public boolean equals(Tuple4d t1) {
      try {
         return this.x == var1.x && this.y == var1.y && this.z == var1.z && this.w == var1.w;
      } catch (NullPointerException var3) {
         Iris.reportError(var3);
         return false;
      }
   }

   public boolean equals(Object t1) {
      try {
         Tuple4d var2 = (Tuple4d)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z && this.w == var2.w;
      } catch (ClassCastException | NullPointerException var3) {
         Iris.reportError(var3);
         return false;
      }
   }

   public boolean epsilonEquals(Tuple4d t1, double epsilon) {
      double var4 = this.x - var1.x;
      if (Double.isNaN(var4)) {
         return false;
      } else if ((var4 < 0.0D ? -var4 : var4) > var2) {
         return false;
      } else {
         var4 = this.y - var1.y;
         if (Double.isNaN(var4)) {
            return false;
         } else if ((var4 < 0.0D ? -var4 : var4) > var2) {
            return false;
         } else {
            var4 = this.z - var1.z;
            if (Double.isNaN(var4)) {
               return false;
            } else if ((var4 < 0.0D ? -var4 : var4) > var2) {
               return false;
            } else {
               var4 = this.w - var1.w;
               if (Double.isNaN(var4)) {
                  return false;
               } else {
                  return !((var4 < 0.0D ? -var4 : var4) > var2);
               }
            }
         }
      }
   }

   public int hashCode() {
      long var1 = 1L;
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.x);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.y);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.z);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.w);
      return (int)(var1 ^ var1 >> 32);
   }

   /** @deprecated */
   @Deprecated
   public final void clamp(float min, float max, Tuple4d t) {
      this.clamp((double)var1, (double)var2, var3);
   }

   public final void clamp(double min, double max, Tuple4d t) {
      if (var5.x > var3) {
         this.x = var3;
      } else {
         this.x = Math.max(var5.x, var1);
      }

      if (var5.y > var3) {
         this.y = var3;
      } else {
         this.y = Math.max(var5.y, var1);
      }

      if (var5.z > var3) {
         this.z = var3;
      } else {
         this.z = Math.max(var5.z, var1);
      }

      if (var5.w > var3) {
         this.w = var3;
      } else {
         this.w = Math.max(var5.w, var1);
      }

   }

   /** @deprecated */
   @Deprecated
   public final void clampMin(float min, Tuple4d t) {
      this.clampMin((double)var1, var2);
   }

   public final void clampMin(double min, Tuple4d t) {
      this.x = Math.max(var3.x, var1);
      this.y = Math.max(var3.y, var1);
      this.z = Math.max(var3.z, var1);
      this.w = Math.max(var3.w, var1);
   }

   /** @deprecated */
   @Deprecated
   public final void clampMax(float max, Tuple4d t) {
      this.clampMax((double)var1, var2);
   }

   public final void clampMax(double max, Tuple4d t) {
      this.x = Math.min(var3.x, var1);
      this.y = Math.min(var3.y, var1);
      this.z = Math.min(var3.z, var1);
      if (var3.w > var1) {
         this.w = var1;
      } else {
         this.w = var3.z;
      }

   }

   public final void absolute(Tuple4d t) {
      this.x = Math.abs(var1.x);
      this.y = Math.abs(var1.y);
      this.z = Math.abs(var1.z);
      this.w = Math.abs(var1.w);
   }

   /** @deprecated */
   @Deprecated
   public final void clamp(float min, float max) {
      this.clamp((double)var1, (double)var2);
   }

   public final void clamp(double min, double max) {
      if (this.x > var3) {
         this.x = var3;
      } else if (this.x < var1) {
         this.x = var1;
      }

      if (this.y > var3) {
         this.y = var3;
      } else if (this.y < var1) {
         this.y = var1;
      }

      if (this.z > var3) {
         this.z = var3;
      } else if (this.z < var1) {
         this.z = var1;
      }

      if (this.w > var3) {
         this.w = var3;
      } else if (this.w < var1) {
         this.w = var1;
      }

   }

   /** @deprecated */
   @Deprecated
   public final void clampMin(float min) {
      this.clampMin((double)var1);
   }

   public final void clampMin(double min) {
      if (this.x < var1) {
         this.x = var1;
      }

      if (this.y < var1) {
         this.y = var1;
      }

      if (this.z < var1) {
         this.z = var1;
      }

      if (this.w < var1) {
         this.w = var1;
      }

   }

   /** @deprecated */
   @Deprecated
   public final void clampMax(float max) {
      this.clampMax((double)var1);
   }

   public final void clampMax(double max) {
      if (this.x > var1) {
         this.x = var1;
      }

      if (this.y > var1) {
         this.y = var1;
      }

      if (this.z > var1) {
         this.z = var1;
      }

      if (this.w > var1) {
         this.w = var1;
      }

   }

   public final void absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
      this.z = Math.abs(this.z);
      this.w = Math.abs(this.w);
   }

   /** @deprecated */
   @Deprecated
   public void interpolate(Tuple4d t1, Tuple4d t2, float alpha) {
      this.interpolate(var1, var2, (double)var3);
   }

   public void interpolate(Tuple4d t1, Tuple4d t2, double alpha) {
      this.x = (1.0D - var3) * var1.x + var3 * var2.x;
      this.y = (1.0D - var3) * var1.y + var3 * var2.y;
      this.z = (1.0D - var3) * var1.z + var3 * var2.z;
      this.w = (1.0D - var3) * var1.w + var3 * var2.w;
   }

   /** @deprecated */
   @Deprecated
   public void interpolate(Tuple4d t1, float alpha) {
      this.interpolate(var1, (double)var2);
   }

   public void interpolate(Tuple4d t1, double alpha) {
      this.x = (1.0D - var2) * this.x + var2 * var1.x;
      this.y = (1.0D - var2) * this.y + var2 * var1.y;
      this.z = (1.0D - var2) * this.z + var2 * var1.z;
      this.w = (1.0D - var2) * this.w + var2 * var1.w;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         Iris.reportError(var2);
         throw new InternalError();
      }
   }

   public final double getX() {
      return this.x;
   }

   public final void setX(double x) {
      this.x = var1;
   }

   public final double getY() {
      return this.y;
   }

   public final void setY(double y) {
      this.y = var1;
   }

   public final double getZ() {
      return this.z;
   }

   public final void setZ(double z) {
      this.z = var1;
   }

   public final double getW() {
      return this.w;
   }

   public final void setW(double w) {
      this.w = var1;
   }
}
