package libs.com.ryderbelserion.vital.common.util;

import java.awt.Color;
import org.jetbrains.annotations.NotNull;

public class ColorUtil {
   public ColorUtil() {
      throw new AssertionError();
   }

   public static Color toColor(@NotNull String value) {
      return new Color(Integer.valueOf(value.substring(1, 3), 16), Integer.valueOf(value.substring(3, 5), 16), Integer.valueOf(value.substring(5, 7), 16));
   }

   public static String toHex(@NotNull Color color) {
      return String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
   }
}
