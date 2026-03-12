package ac.grim.grimac.utils.math;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import lombok.Generated;

public final class OptifineFastMath {
   private static final float[] SIN = new float[4096];

   @Contract(
      pure = true
   )
   public static float sin(float value) {
      return SIN[(int)(value * 651.8986F) & 4095];
   }

   @Contract(
      pure = true
   )
   public static float cos(float value) {
      return SIN[(int)(value * 651.8986F + 1024.0F) & 4095];
   }

   @Contract(
      pure = true
   )
   public static float roundToFloat(double value) {
      return (float)((double)Math.round(value * 1.0E8D) / 1.0E8D);
   }

   @Generated
   private OptifineFastMath() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      for(int i = 0; i < 4096; ++i) {
         SIN[i] = roundToFloat(StrictMath.sin((double)i * 3.141592653589793D * 2.0D / 4096.0D));
      }

   }
}
