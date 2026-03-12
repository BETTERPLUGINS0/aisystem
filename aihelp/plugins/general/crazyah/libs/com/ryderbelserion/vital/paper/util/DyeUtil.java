package libs.com.ryderbelserion.vital.paper.util;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DyeUtil {
   private DyeUtil() {
      throw new AssertionError();
   }

   @NotNull
   public static DyeColor getDyeColor(@NotNull String value) {
      if (value.isEmpty()) {
         return DyeColor.WHITE;
      } else {
         String var1 = value.toUpperCase();
         byte var2 = -1;
         switch(var1.hashCode()) {
         case -1955522002:
            if (var1.equals("ORANGE")) {
               var2 = 0;
            }
            break;
         case -1923613764:
            if (var1.equals("PURPLE")) {
               var2 = 9;
            }
            break;
         case -1680910220:
            if (var1.equals("YELLOW")) {
               var2 = 3;
            }
            break;
         case 81009:
            if (var1.equals("RED")) {
               var2 = 13;
            }
            break;
         case 2041946:
            if (var1.equals("BLUE")) {
               var2 = 10;
            }
            break;
         case 2083619:
            if (var1.equals("CYAN")) {
               var2 = 8;
            }
            break;
         case 2196067:
            if (var1.equals("GRAY")) {
               var2 = 6;
            }
            break;
         case 2336725:
            if (var1.equals("LIME")) {
               var2 = 4;
            }
            break;
         case 2455926:
            if (var1.equals("PINK")) {
               var2 = 5;
            }
            break;
         case 63281119:
            if (var1.equals("BLACK")) {
               var2 = 14;
            }
            break;
         case 63473942:
            if (var1.equals("BROWN")) {
               var2 = 11;
            }
            break;
         case 68081379:
            if (var1.equals("GREEN")) {
               var2 = 12;
            }
            break;
         case 305548803:
            if (var1.equals("LIGHT_BLUE")) {
               var2 = 2;
            }
            break;
         case 305702924:
            if (var1.equals("LIGHT_GRAY")) {
               var2 = 7;
            }
            break;
         case 1546904713:
            if (var1.equals("MAGENTA")) {
               var2 = 1;
            }
         }

         DyeColor var10000;
         switch(var2) {
         case 0:
            var10000 = DyeColor.ORANGE;
            break;
         case 1:
            var10000 = DyeColor.MAGENTA;
            break;
         case 2:
            var10000 = DyeColor.LIGHT_BLUE;
            break;
         case 3:
            var10000 = DyeColor.YELLOW;
            break;
         case 4:
            var10000 = DyeColor.LIME;
            break;
         case 5:
            var10000 = DyeColor.PINK;
            break;
         case 6:
            var10000 = DyeColor.GRAY;
            break;
         case 7:
            var10000 = DyeColor.LIGHT_GRAY;
            break;
         case 8:
            var10000 = DyeColor.CYAN;
            break;
         case 9:
            var10000 = DyeColor.PURPLE;
            break;
         case 10:
            var10000 = DyeColor.BLUE;
            break;
         case 11:
            var10000 = DyeColor.BROWN;
            break;
         case 12:
            var10000 = DyeColor.GREEN;
            break;
         case 13:
            var10000 = DyeColor.RED;
            break;
         case 14:
            var10000 = DyeColor.BLACK;
            break;
         default:
            var10000 = DyeColor.WHITE;
         }

         return var10000;
      }
   }

   @NotNull
   public static Color getDefaultColor(@NotNull String color) {
      if (color.isEmpty()) {
         return Color.WHITE;
      } else {
         String var1 = color.toUpperCase();
         byte var2 = -1;
         switch(var1.hashCode()) {
         case -2027972496:
            if (var1.equals("MAROON")) {
               var2 = 7;
            }
            break;
         case -1955522002:
            if (var1.equals("ORANGE")) {
               var2 = 10;
            }
            break;
         case -1923613764:
            if (var1.equals("PURPLE")) {
               var2 = 11;
            }
            break;
         case -1848981747:
            if (var1.equals("SILVER")) {
               var2 = 13;
            }
            break;
         case -1680910220:
            if (var1.equals("YELLOW")) {
               var2 = 15;
            }
            break;
         case 81009:
            if (var1.equals("RED")) {
               var2 = 12;
            }
            break;
         case 2016956:
            if (var1.equals("AQUA")) {
               var2 = 0;
            }
            break;
         case 2041946:
            if (var1.equals("BLUE")) {
               var2 = 2;
            }
            break;
         case 2196067:
            if (var1.equals("GRAY")) {
               var2 = 4;
            }
            break;
         case 2336725:
            if (var1.equals("LIME")) {
               var2 = 6;
            }
            break;
         case 2388918:
            if (var1.equals("NAVY")) {
               var2 = 8;
            }
            break;
         case 2570844:
            if (var1.equals("TEAL")) {
               var2 = 14;
            }
            break;
         case 63281119:
            if (var1.equals("BLACK")) {
               var2 = 1;
            }
            break;
         case 68081379:
            if (var1.equals("GREEN")) {
               var2 = 5;
            }
            break;
         case 75295163:
            if (var1.equals("OLIVE")) {
               var2 = 9;
            }
            break;
         case 198329015:
            if (var1.equals("FUCHSIA")) {
               var2 = 3;
            }
         }

         Color var10000;
         switch(var2) {
         case 0:
            var10000 = Color.AQUA;
            break;
         case 1:
            var10000 = Color.BLACK;
            break;
         case 2:
            var10000 = Color.BLUE;
            break;
         case 3:
            var10000 = Color.FUCHSIA;
            break;
         case 4:
            var10000 = Color.GRAY;
            break;
         case 5:
            var10000 = Color.GREEN;
            break;
         case 6:
            var10000 = Color.LIME;
            break;
         case 7:
            var10000 = Color.MAROON;
            break;
         case 8:
            var10000 = Color.NAVY;
            break;
         case 9:
            var10000 = Color.OLIVE;
            break;
         case 10:
            var10000 = Color.ORANGE;
            break;
         case 11:
            var10000 = Color.PURPLE;
            break;
         case 12:
            var10000 = Color.RED;
            break;
         case 13:
            var10000 = Color.SILVER;
            break;
         case 14:
            var10000 = Color.TEAL;
            break;
         case 15:
            var10000 = Color.YELLOW;
            break;
         default:
            var10000 = Color.WHITE;
         }

         return var10000;
      }
   }

   @Nullable
   public static Color getColor(@NotNull String color) {
      if (color.isEmpty()) {
         return null;
      } else {
         String[] rgb = color.split(",");
         if (rgb.length != 3) {
            return null;
         } else {
            int red = Integer.parseInt(rgb[0]);
            int green = Integer.parseInt(rgb[1]);
            int blue = Integer.parseInt(rgb[2]);
            return Color.fromRGB(red, green, blue);
         }
      }
   }
}
