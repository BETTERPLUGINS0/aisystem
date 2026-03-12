package ac.grim.grimac.shaded.kyori.adventure.util;

import ac.grim.grimac.shaded.jetbrains.annotations.Range;

public interface ARGBLike extends RGBLike {
   @Range(
      from = 0L,
      to = 255L
   )
   int alpha();
}
