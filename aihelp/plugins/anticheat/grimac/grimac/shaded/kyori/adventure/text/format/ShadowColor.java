package ac.grim.grimac.shaded.kyori.adventure.text.format;

import ac.grim.grimac.shaded.intellij.lang.annotations.Pattern;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.Range;
import ac.grim.grimac.shaded.kyori.adventure.util.ARGBLike;
import ac.grim.grimac.shaded.kyori.adventure.util.RGBLike;

public interface ShadowColor extends StyleBuilderApplicable, ARGBLike {
   @NotNull
   static ShadowColor lerp(final float t, @NotNull final ARGBLike a, @NotNull final ARGBLike b) {
      float clampedT = Math.min(1.0F, Math.max(0.0F, t));
      int ar = a.red();
      int br = b.red();
      int ag = a.green();
      int bg = b.green();
      int ab = a.blue();
      int bb = b.blue();
      int aa = a.alpha();
      int ba = b.alpha();
      return shadowColor(Math.round((float)ar + clampedT * (float)(br - ar)), Math.round((float)ag + clampedT * (float)(bg - ag)), Math.round((float)ab + clampedT * (float)(bb - ab)), Math.round((float)aa + clampedT * (float)(ba - aa)));
   }

   @NotNull
   static ShadowColor none() {
      return ShadowColorImpl.NONE;
   }

   @Contract(
      pure = true
   )
   @NotNull
   static ShadowColor shadowColor(final int argb) {
      return (ShadowColor)(argb == 0 ? none() : new ShadowColorImpl(argb));
   }

   @Contract(
      pure = true
   )
   @NotNull
   static ShadowColor shadowColor(@Range(from = 0L,to = 255L) final int red, @Range(from = 0L,to = 255L) final int green, @Range(from = 0L,to = 255L) final int blue, @Range(from = 0L,to = 255L) final int alpha) {
      int value = (alpha & 255) << 24 | (red & 255) << 16 | (green & 255) << 8 | blue & 255;
      return (ShadowColor)(value == 0 ? none() : new ShadowColorImpl(value));
   }

   @Contract(
      pure = true
   )
   @NotNull
   static ShadowColor shadowColor(@NotNull final RGBLike rgb, @Range(from = 0L,to = 255L) final int alpha) {
      return shadowColor(rgb.red(), rgb.green(), rgb.blue(), alpha);
   }

   @NotNull
   static ShadowColor shadowColor(@NotNull final ARGBLike argb) {
      return argb instanceof ShadowColor ? (ShadowColor)argb : shadowColor(argb.red(), argb.green(), argb.blue(), argb.alpha());
   }

   @Contract(
      pure = true
   )
   @Nullable
   static ShadowColor fromHexString(@Pattern("#[0-9a-fA-F]{8}") @NotNull final String hex) {
      if (hex.length() != 9) {
         return null;
      } else if (!hex.startsWith("#")) {
         return null;
      } else {
         try {
            int r = Integer.parseInt(hex.substring(1, 3), 16);
            int g = Integer.parseInt(hex.substring(3, 5), 16);
            int b = Integer.parseInt(hex.substring(5, 7), 16);
            int a = Integer.parseInt(hex.substring(7, 9), 16);
            return new ShadowColorImpl(a << 24 | r << 16 | g << 8 | b);
         } catch (NumberFormatException var5) {
            return null;
         }
      }
   }

   @NotNull
   default String asHexString() {
      int argb = this.value();
      int a = argb >> 24 & 255;
      int r = argb >> 16 & 255;
      int g = argb >> 8 & 255;
      int b = argb & 255;
      return String.format("#%02X%02X%02X%02X", r, g, b, a);
   }

   @Range(
      from = 0L,
      to = 255L
   )
   default int red() {
      return this.value() >> 16 & 255;
   }

   @Range(
      from = 0L,
      to = 255L
   )
   default int green() {
      return this.value() >> 8 & 255;
   }

   @Range(
      from = 0L,
      to = 255L
   )
   default int blue() {
      return this.value() & 255;
   }

   @Range(
      from = 0L,
      to = 255L
   )
   default int alpha() {
      return this.value() >> 24 & 255;
   }

   int value();

   default void styleApply(@NotNull final Style.Builder style) {
      style.shadowColor(this);
   }
}
