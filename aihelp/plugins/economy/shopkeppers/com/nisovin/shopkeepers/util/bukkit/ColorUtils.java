package com.nisovin.shopkeepers.util.bukkit;

import java.awt.Color;

public final class ColorUtils {
   public static int HSBtoRGB(float hue, float saturation, float brightness) {
      int rgb = Color.HSBtoRGB(hue, saturation, brightness);
      return rgb & 16777215;
   }

   private ColorUtils() {
   }
}
