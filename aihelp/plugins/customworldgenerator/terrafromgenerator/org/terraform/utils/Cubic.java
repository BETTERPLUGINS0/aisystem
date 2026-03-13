package org.terraform.utils;

public class Cubic {
   private final float a;
   private final float b;
   private final float c;
   private final float d;

   public Cubic(float a, float b, float c, float d) {
      this.a = a;
      this.b = b;
      this.c = c;
      this.d = d;
   }

   public float eval(float u) {
      return ((this.d * u + this.c) * u + this.b) * u + this.a;
   }
}
