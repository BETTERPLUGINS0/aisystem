package org.terraform.utils;

import java.io.Serializable;
import org.jetbrains.annotations.NotNull;

public class Vector3f implements Serializable {
   static final long serialVersionUID = -7031930069184524614L;
   public float x;
   public float y;
   public float z;

   public Vector3f(float x, float y, float z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public Vector3f(@NotNull float[] v) {
      this.x = v[0];
      this.y = v[1];
      this.z = v[2];
   }

   public Vector3f(@NotNull Vector3f v1) {
      this.x = v1.x;
      this.y = v1.y;
      this.z = v1.z;
   }

   public Vector3f() {
   }

   public final float lengthSquared() {
      return this.x * this.x + this.y * this.y + this.z * this.z;
   }

   public final float length() {
      return (float)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
   }

   public final void cross(@NotNull Vector3f v1, @NotNull Vector3f v2) {
      this.x = v1.y * v2.z - v1.z * v2.y;
      this.y = v2.x * v1.z - v2.z * v1.x;
      this.z = v1.x * v2.y - v1.y * v2.x;
   }

   public final float dot(@NotNull Vector3f v1) {
      return this.x * v1.x + this.y * v1.y + this.z * v1.z;
   }

   public final void normalize(@NotNull Vector3f v1) {
      float norm = (float)(1.0D / Math.sqrt((double)(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z)));
      this.x = v1.x * norm;
      this.y = v1.y * norm;
      this.z = v1.z * norm;
   }

   public final void normalize() {
      float norm = (float)(1.0D / Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z)));
      this.x *= norm;
      this.y *= norm;
      this.z *= norm;
   }

   public final float angle(@NotNull Vector3f v1) {
      double vDot = (double)(this.dot(v1) / (this.length() * v1.length()));
      if (vDot < -1.0D) {
         vDot = -1.0D;
      }

      if (vDot > 1.0D) {
         vDot = 1.0D;
      }

      return (float)Math.acos(vDot);
   }
}
