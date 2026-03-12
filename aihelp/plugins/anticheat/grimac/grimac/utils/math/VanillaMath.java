package ac.grim.grimac.utils.math;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import lombok.Generated;

public final class VanillaMath {
   private static final float[] SIN = new float[65536];

   @Contract(
      pure = true
   )
   public static float sin(float value) {
      return SIN[(int)(value * 10430.378F) & '\uffff'];
   }

   @Contract(
      pure = true
   )
   public static float cos(float value) {
      return SIN[(int)(value * 10430.378F + 16384.0F) & '\uffff'];
   }

   @Generated
   private VanillaMath() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      for(int i = 0; i < SIN.length; ++i) {
         SIN[i] = (float)StrictMath.sin((double)i * 3.141592653589793D * 2.0D / 65536.0D);
      }

   }
}
