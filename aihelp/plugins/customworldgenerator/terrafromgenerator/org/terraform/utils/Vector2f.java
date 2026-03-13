package org.terraform.utils;

import java.io.Serializable;
import org.jetbrains.annotations.NotNull;

public class Vector2f implements Serializable {
   static final long serialVersionUID = -2168194326883512320L;
   public float x;
   public float y;

   public Vector2f(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public Vector2f(@NotNull float[] v) {
      this.x = v[0];
      this.y = v[1];
   }

   public Vector2f(@NotNull Vector2f v1) {
      this.x = v1.x;
      this.y = v1.y;
   }

   public Vector2f() {
   }

   public final float dot(@NotNull Vector2f v1) {
      return this.x * v1.x + this.y * v1.y;
   }

   public final float length() {
      return (float)Math.sqrt((double)(this.x * this.x + this.y * this.y));
   }

   public final float lengthSquared() {
      return this.x * this.x + this.y * this.y;
   }

   public final void normalize(@NotNull Vector2f v1) {
      float norm = (float)(1.0D / Math.sqrt((double)(v1.x * v1.x + v1.y * v1.y)));
      this.x = v1.x * norm;
      this.y = v1.y * norm;
   }

   public final void normalize() {
      float norm = (float)(1.0D / Math.sqrt((double)(this.x * this.x + this.y * this.y)));
      this.x *= norm;
      this.y *= norm;
   }

   public final float angle(@NotNull Vector2f v1) {
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
