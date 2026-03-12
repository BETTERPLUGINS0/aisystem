package ac.grim.grimac.utils.math;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import lombok.Generated;

public final class LegacyFastMath {
   private static final float[] SIN_TABLE_FAST = new float[4096];

   @Contract(
      pure = true
   )
   public static float sin(float value) {
      return SIN_TABLE_FAST[(int)(value * 651.8986F) & 4095];
   }

   @Contract(
      pure = true
   )
   public static float cos(float value) {
      return SIN_TABLE_FAST[(int)((value + 1.5707964F) * 651.8986F) & 4095];
   }

   @Generated
   private LegacyFastMath() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      int i;
      for(i = 0; i < 4096; ++i) {
         SIN_TABLE_FAST[i] = (float)Math.sin((double)(((float)i + 0.5F) / 4096.0F * 6.2831855F));
      }

      for(i = 0; i < 360; i += 90) {
         SIN_TABLE_FAST[(int)((float)i * 11.377778F) & 4095] = (float)Math.sin((double)GrimMath.radians((float)i));
      }

   }
}
