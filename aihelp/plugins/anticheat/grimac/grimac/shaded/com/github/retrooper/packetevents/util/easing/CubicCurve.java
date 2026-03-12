package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.easing;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CubicCurve {
   private final float a;
   private final float b;
   private final float c;

   public CubicCurve(float a, float b, float c) {
      this.a = a;
      this.b = b;
      this.c = c;
   }

   public float calcSample(float t) {
      return ((this.a * t + this.b) * t + this.c) * t;
   }

   public float calcSampleGradient(float t) {
      return (3.0F * this.a * t + 2.0F * this.b) * t + this.c;
   }

   public float getA() {
      return this.a;
   }

   public float getB() {
      return this.b;
   }

   public float getC() {
      return this.c;
   }
}
