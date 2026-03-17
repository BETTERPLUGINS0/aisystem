package advancedplugins.pm2.cv.models.api.model.rpc.generator.assets;

import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import java.util.Locale;
import java.util.function.Consumer;

public enum JavaDisplay {
   THIRDPERSON_RIGHTHAND,
   THIRDPERSON_LEFTHAND,
   FIRSTPERSON_RIGHTHAND,
   FIRSTPERSON_LEFTHAND,
   GROUND,
   GUI,
   HEAD,
   FIXED;

   private static JavaDisplay[] $values() {
      return new JavaDisplay[]{THIRDPERSON_RIGHTHAND, THIRDPERSON_LEFTHAND, FIRSTPERSON_RIGHTHAND, FIRSTPERSON_LEFTHAND, GROUND, GUI, HEAD, FIXED};
   }

   public String toString() {
      return super.toString().toLowerCase(Locale.ENGLISH);
   }

   // $FF: synthetic method
   private static JavaDisplay[] $values$() {
      return new JavaDisplay[]{THIRDPERSON_RIGHTHAND, THIRDPERSON_LEFTHAND, FIRSTPERSON_RIGHTHAND, FIRSTPERSON_LEFTHAND, GROUND, GUI, HEAD, FIXED};
   }

   public static enum Transform {
      ROTATION((var0) -> {
         var0[0] %= 360.0F;
         var0[1] %= 360.0F;
         var0[2] %= 360.0F;
      }),
      TRANSLATION((var0) -> {
         var0[0] = MathUtils.clamp(var0[0], -80.0F, 80.0F);
         var0[1] = MathUtils.clamp(var0[1], -80.0F, 80.0F);
         var0[2] = MathUtils.clamp(var0[2], -80.0F, 80.0F);
      }),
      SCALE((var0) -> {
         var0[0] = MathUtils.clamp(var0[0], 0.0F, 4.0F);
         var0[1] = MathUtils.clamp(var0[1], 0.0F, 4.0F);
         var0[2] = MathUtils.clamp(var0[2], 0.0F, 4.0F);
      });

      private final Consumer<float[]> sanitizer;

      private Transform(Consumer<float[]> param3) {
         this.sanitizer = var3;
      }

      private static JavaDisplay.Transform[] $values() {
         return new JavaDisplay.Transform[]{ROTATION, TRANSLATION, SCALE};
      }

      public void sanitize(float[] var1) {
         this.sanitizer.accept(var1);
      }

      public String toString() {
         return super.toString().toLowerCase(Locale.ENGLISH);
      }

      // $FF: synthetic method
      private static JavaDisplay.Transform[] $values$() {
         return new JavaDisplay.Transform[]{ROTATION, TRANSLATION, SCALE};
      }
   }
}
