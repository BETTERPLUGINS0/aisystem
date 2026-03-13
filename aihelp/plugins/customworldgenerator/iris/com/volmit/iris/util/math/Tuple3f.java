package com.volmit.iris.util.math;

import com.volmit.iris.Iris;
import java.io.Serializable;

public abstract class Tuple3f implements Serializable, Cloneable {
   static final long serialVersionUID = 5019834619484343712L;
   public float x;
   public float y;
   public float z;

   public Tuple3f(float x, float y, float z) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public Tuple3f(float[] t) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
   }

   public Tuple3f(Tuple3f t1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
   }

   public Tuple3f(Tuple3d t1) {
      this.x = (float)var1.x;
      this.y = (float)var1.y;
      this.z = (float)var1.z;
   }

   public Tuple3f() {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
   }

   public String toString() {
      return "(" + this.x + ", " + this.y + ", " + this.z + ")";
   }

   public final void set(float x, float y, float z) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public final void set(float[] t) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
   }

   public final void set(Tuple3f t1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
   }

   public final void set(Tuple3d t1) {
      this.x = (float)var1.x;
      this.y = (float)var1.y;
      this.z = (float)var1.z;
   }

   public final void get(float[] t) {
      var1[0] = this.x;
      var1[1] = this.y;
      var1[2] = this.z;
   }

   public final void get(Tuple3f t) {
      var1.x = this.x;
      var1.y = this.y;
      var1.z = this.z;
   }

   public final void add(Tuple3f t1, Tuple3f t2) {
      this.x = var1.x + var2.x;
      this.y = var1.y + var2.y;
      this.z = var1.z + var2.z;
   }

   public final void add(Tuple3f t1) {
      this.x += var1.x;
      this.y += var1.y;
      this.z += var1.z;
   }

   public final void sub(Tuple3f t1, Tuple3f t2) {
      this.x = var1.x - var2.x;
      this.y = var1.y - var2.y;
      this.z = var1.z - var2.z;
   }

   public final void sub(Tuple3f t1) {
      this.x -= var1.x;
      this.y -= var1.y;
      this.z -= var1.z;
   }

   public final void negate(Tuple3f t1) {
      this.x = -var1.x;
      this.y = -var1.y;
      this.z = -var1.z;
   }

   public final void negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
   }

   public final void scale(float s, Tuple3f t1) {
      this.x = var1 * var2.x;
      this.y = var1 * var2.y;
      this.z = var1 * var2.z;
   }

   public final void scale(float s) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
   }

   public final void scaleAdd(float s, Tuple3f t1, Tuple3f t2) {
      this.x = var1 * var2.x + var3.x;
      this.y = var1 * var2.y + var3.y;
      this.z = var1 * var2.z + var3.z;
   }

   public final void scaleAdd(float s, Tuple3f t1) {
      this.x = var1 * this.x + var2.x;
      this.y = var1 * this.y + var2.y;
      this.z = var1 * this.z + var2.z;
   }

   public boolean equals(Tuple3f t1) {
      try {
         return this.x == var1.x && this.y == var1.y && this.z == var1.z;
      } catch (NullPointerException var3) {
         Iris.reportError(var3);
         return false;
      }
   }

   public boolean equals(Object t1) {
      try {
         Tuple3f var2 = (Tuple3f)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z;
      } catch (ClassCastException | NullPointerException var3) {
         Iris.reportError(var3);
         return false;
      }
   }

   public boolean epsilonEquals(Tuple3f t1, float epsilon) {
      float var3 = this.x - var1.x;
      if (Float.isNaN(var3)) {
         return false;
      } else if ((var3 < 0.0F ? -var3 : var3) > var2) {
         return false;
      } else {
         var3 = this.y - var1.y;
         if (Float.isNaN(var3)) {
            return false;
         } else if ((var3 < 0.0F ? -var3 : var3) > var2) {
            return false;
         } else {
            var3 = this.z - var1.z;
            if (Float.isNaN(var3)) {
               return false;
            } else {
               return !((var3 < 0.0F ? -var3 : var3) > var2);
            }
         }
      }
   }

   public int hashCode() {
      long var1 = 1L;
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.x);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.y);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.z);
      return (int)(var1 ^ var1 >> 32);
   }

   public final void clamp(float min, float max, Tuple3f t) {
      if (var3.x > var2) {
         this.x = var2;
      } else {
         this.x = Math.max(var3.x, var1);
      }

      if (var3.y > var2) {
         this.y = var2;
      } else {
         this.y = Math.max(var3.y, var1);
      }

      if (var3.z > var2) {
         this.z = var2;
      } else {
         this.z = Math.max(var3.z, var1);
      }

   }

   public final void clampMin(float min, Tuple3f t) {
      this.x = Math.max(var2.x, var1);
      this.y = Math.max(var2.y, var1);
      this.z = Math.max(var2.z, var1);
   }

   public final void clampMax(float max, Tuple3f t) {
      this.x = Math.min(var2.x, var1);
      this.y = Math.min(var2.y, var1);
      this.z = Math.min(var2.z, var1);
   }

   public final void absolute(Tuple3f t) {
      this.x = Math.abs(var1.x);
      this.y = Math.abs(var1.y);
      this.z = Math.abs(var1.z);
   }

   public final void clamp(float min, float max) {
      if (this.x > var2) {
         this.x = var2;
      } else if (this.x < var1) {
         this.x = var1;
      }

      if (this.y > var2) {
         this.y = var2;
      } else if (this.y < var1) {
         this.y = var1;
      }

      if (this.z > var2) {
         this.z = var2;
      } else if (this.z < var1) {
         this.z = var1;
      }

   }

   public final void clampMin(float min) {
      if (this.x < var1) {
         this.x = var1;
      }

      if (this.y < var1) {
         this.y = var1;
      }

      if (this.z < var1) {
         this.z = var1;
      }

   }

   public final void clampMax(float max) {
      if (this.x > var1) {
         this.x = var1;
      }

      if (this.y > var1) {
         this.y = var1;
      }

      if (this.z > var1) {
         this.z = var1;
      }

   }

   public final void absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
      this.z = Math.abs(this.z);
   }

   public final void interpolate(Tuple3f t1, Tuple3f t2, float alpha) {
      this.x = (1.0F - var3) * var1.x + var3 * var2.x;
      this.y = (1.0F - var3) * var1.y + var3 * var2.y;
      this.z = (1.0F - var3) * var1.z + var3 * var2.z;
   }

   public final void interpolate(Tuple3f t1, float alpha) {
      this.x = (1.0F - var2) * this.x + var2 * var1.x;
      this.y = (1.0F - var2) * this.y + var2 * var1.y;
      this.z = (1.0F - var2) * this.z + var2 * var1.z;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         Iris.reportError(var2);
         throw new InternalError();
      }
   }

   public final float getX() {
      return this.x;
   }

   public final void setX(float x) {
      this.x = var1;
   }

   public final float getY() {
      return this.y;
   }

   public final void setY(float y) {
      this.y = var1;
   }

   public final float getZ() {
      return this.z;
   }

   public final void setZ(float z) {
      this.z = var1;
   }
}
