package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.easing;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EasingFunctions {
   private EasingFunctions() {
   }

   public static float constant(float ignoredX) {
      return 0.0F;
   }

   public static float linear(float x) {
      return x;
   }

   public static float inBack(float x) {
      float c1 = 1.70158F;
      float c3 = c1 + 1.0F;
      return x * x * (c3 * x - c1);
   }

   public static float inBounce(float x) {
      return 1.0F - outBounce(1.0F - x);
   }

   public static float inCubic(float x) {
      return x * x * x;
   }

   public static float inElastic(float x) {
      if (x == 0.0F) {
         return 0.0F;
      } else if (x == 1.0F) {
         return 1.0F;
      } else {
         float c4 = 2.0943952F;
         return (float)(-Math.pow(2.0D, 10.0D * (double)x - 10.0D) * Math.sin(((double)x * 10.0D - 10.75D) * (double)c4));
      }
   }

   public static float inExpo(float x) {
      return x == 0.0F ? 0.0F : (float)Math.pow(2.0D, 10.0D * (double)x - 10.0D);
   }

   public static float inQuart(float x) {
      return x * x * x * x;
   }

   public static float inQuint(float x) {
      return x * x * x * x * x;
   }

   public static float inSine(float x) {
      return 1.0F - (float)Math.cos((double)(x * 1.5707964F));
   }

   public static float inOutBounce(float x) {
      return x < 0.5F ? (1.0F - outBounce(1.0F - 2.0F * x)) / 2.0F : (1.0F + outBounce(2.0F * x - 1.0F)) / 2.0F;
   }

   public static float inOutCirc(float x) {
      return x < 0.5F ? (float)((1.0D - Math.sqrt(1.0D - Math.pow(2.0D * (double)x, 2.0D))) / 2.0D) : (float)((Math.sqrt(1.0D - Math.pow(-2.0D * (double)x + 2.0D, 2.0D)) + 1.0D) / 2.0D);
   }

   public static float inOutCubic(float x) {
      return x < 0.5F ? 4.0F * x * x * x : (float)(1.0D - Math.pow(-2.0D * (double)x + 2.0D, 3.0D) / 2.0D);
   }

   public static float inOutQuad(float x) {
      return x < 0.5F ? 2.0F * x * x : (float)(1.0D - Math.pow(-2.0D * (double)x + 2.0D, 2.0D) / 2.0D);
   }

   public static float inOutQuart(float x) {
      return x < 0.5F ? 8.0F * x * x * x * x : (float)(1.0D - Math.pow(-2.0D * (double)x + 2.0D, 4.0D) / 2.0D);
   }

   public static float inOutQuint(float x) {
      return (double)x < 0.5D ? 16.0F * x * x * x * x * x : (float)(1.0D - Math.pow(-2.0D * (double)x + 2.0D, 5.0D) / 2.0D);
   }

   public static float outBounce(float x) {
      float n1 = 7.5625F;
      float d1 = 2.75F;
      if (x < 1.0F / d1) {
         return n1 * x * x;
      } else if (x < 2.0F / d1) {
         return n1 * MathUtil.square(x - 1.5F / d1) + 0.75F;
      } else {
         return (double)x < (double)(2.5F / d1) ? n1 * MathUtil.square(x - 2.25F / d1) + 0.9375F : n1 * MathUtil.square(x - 2.625F / d1) + 0.984375F;
      }
   }

   public static float outElastic(float x) {
      float c4 = 2.0943952F;
      if (x == 0.0F) {
         return 0.0F;
      } else {
         return x == 1.0F ? 1.0F : (float)(Math.pow(2.0D, -10.0D * (double)x) * Math.sin(((double)x * 10.0D - 0.75D) * (double)c4) + 1.0D);
      }
   }

   public static float outExpo(float x) {
      return x == 1.0F ? 1.0F : 1.0F - (float)Math.pow(2.0D, -10.0D * (double)x);
   }

   public static float outQuad(float x) {
      return 1.0F - MathUtil.square(1.0F - x);
   }

   public static float outQuint(float x) {
      return 1.0F - (float)Math.pow(1.0D - (double)x, 5.0D);
   }

   public static float outSine(float x) {
      return (float)Math.sin((double)(x * 1.5707964F));
   }

   public static float inOutSine(float x) {
      return -((float)Math.cos((double)(3.1415927F * x)) - 1.0F) / 2.0F;
   }

   public static float outBack(float x) {
      float c1 = 1.70158F;
      float c3 = 2.70158F;
      return 1.0F + c3 * MathUtil.cube(x - 1.0F) + c1 * MathUtil.square(x - 1.0F);
   }

   public static float outQuart(float x) {
      return 1.0F - MathUtil.square(MathUtil.square(1.0F - x));
   }

   public static float outCubic(float x) {
      return 1.0F - MathUtil.cube(1.0F - x);
   }

   public static float inOutExpo(float x) {
      if (x == 0.0F) {
         return 0.0F;
      } else if (x == 1.0F) {
         return 1.0F;
      } else {
         return x < 0.5F ? (float)(Math.pow(2.0D, 20.0D * (double)x - 10.0D) / 2.0D) : (float)((2.0D - Math.pow(2.0D, -20.0D * (double)x + 10.0D)) / 2.0D);
      }
   }

   public static float inQuad(float x) {
      return x * x;
   }

   public static float outCirc(float x) {
      return (float)Math.sqrt((double)(1.0F - MathUtil.square(x - 1.0F)));
   }

   public static float inOutElastic(float x) {
      float c5 = 1.3962635F;
      if (x == 0.0F) {
         return 0.0F;
      } else if (x == 1.0F) {
         return 1.0F;
      } else {
         double sin = Math.sin((20.0D * (double)x - 11.125D) * (double)c5);
         return x < 0.5F ? (float)(-(Math.pow(2.0D, 20.0D * (double)x - 10.0D) * sin) / 2.0D) : (float)(Math.pow(2.0D, -20.0D * (double)x + 10.0D) * sin / 2.0D + 1.0D);
      }
   }

   public static float inCirc(float x) {
      return (float)(-Math.sqrt((double)(1.0F - x * x))) + 1.0F;
   }

   public static float inOutBack(float x) {
      float c1 = 1.70158F;
      float c2 = c1 * 1.525F;
      if (x < 0.5F) {
         return 4.0F * x * x * (2.0F * (c2 + 1.0F) * x - c2) / 2.0F;
      } else {
         float dt = 2.0F * x - 2.0F;
         return (dt * dt * ((c2 + 1.0F) * dt + c2) + 2.0F) / 2.0F;
      }
   }
}
