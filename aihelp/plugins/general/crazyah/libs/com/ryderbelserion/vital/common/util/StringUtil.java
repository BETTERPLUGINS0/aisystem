package libs.com.ryderbelserion.vital.common.util;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class StringUtil {
   private StringUtil() {
      throw new AssertionError();
   }

   public static String formatInteger(int number) {
      return NumberFormat.getIntegerInstance(Locale.US).format((long)number);
   }

   public static String formatDouble(double number) {
      return NumberFormat.getNumberInstance(Locale.US).format(number);
   }

   public static String convertList(@NotNull List<String> list) {
      if (list.isEmpty()) {
         return "";
      } else {
         StringBuilder message = new StringBuilder();
         Iterator var2 = list.iterator();

         while(var2.hasNext()) {
            String line = (String)var2.next();
            message.append(line).append("\n");
         }

         return chomp(message.toString());
      }
   }

   @NotNull
   public static String chomp(@NotNull String key) {
      if (key.isEmpty()) {
         return key;
      } else {
         char CR = 13;
         char LF = 10;
         if (key.length() == 1) {
            char ch = key.charAt(0);
            return ch != CR && ch != LF ? key : "";
         } else {
            int lastIdx = key.length() - 1;
            char last = key.charAt(lastIdx);
            if (last == LF) {
               if (key.charAt(lastIdx - 1) == CR) {
                  --lastIdx;
               }
            } else if (last != CR) {
               ++lastIdx;
            }

            return key.substring(0, lastIdx);
         }
      }
   }

   public static Optional<Number> tryParseInt(@NotNull String value) {
      try {
         return Optional.of(Integer.parseInt(value));
      } catch (NumberFormatException var2) {
         return Optional.empty();
      }
   }

   public static Optional<Boolean> tryParseBoolean(@NotNull String value) {
      try {
         return Optional.of(Boolean.parseBoolean(value));
      } catch (NumberFormatException var2) {
         return Optional.empty();
      }
   }

   public static String getEnchant(@NotNull String enchant) {
      if (enchant.isEmpty()) {
         return "";
      } else {
         String var1 = enchant.toLowerCase();
         byte var2 = -1;
         switch(var1.hashCode()) {
         case -1774590767:
            if (var1.equals("damage_all")) {
               var2 = 7;
            }
            break;
         case -1729996628:
            if (var1.equals("arrow_fire")) {
               var2 = 16;
            }
            break;
         case -1267836636:
            if (var1.equals("protection_explosions")) {
               var2 = 3;
            }
            break;
         case -1002602080:
            if (var1.equals("oxygen")) {
               var2 = 5;
            }
            break;
         case -698289393:
            if (var1.equals("protection_projectile")) {
               var2 = 4;
            }
            break;
         case -601698851:
            if (var1.equals("loot_bonus_blocks")) {
               var2 = 13;
            }
            break;
         case -476384346:
            if (var1.equals("water_worker")) {
               var2 = 6;
            }
            break;
         case -439211931:
            if (var1.equals("arrow_damage")) {
               var2 = 14;
            }
            break;
         case 3333041:
            if (var1.equals("luck")) {
               var2 = 18;
            }
            break;
         case 24678400:
            if (var1.equals("damage_arthropods")) {
               var2 = 9;
            }
            break;
         case 493198669:
            if (var1.equals("damage_undead")) {
               var2 = 8;
            }
            break;
         case 716086281:
            if (var1.equals("durability")) {
               var2 = 12;
            }
            break;
         case 1158132485:
            if (var1.equals("arrow_knockback")) {
               var2 = 15;
            }
            break;
         case 1703059850:
            if (var1.equals("arrow_infinite")) {
               var2 = 17;
            }
            break;
         case 1733780362:
            if (var1.equals("loot_bonus_mobs")) {
               var2 = 10;
            }
            break;
         case 1791328920:
            if (var1.equals("protection_environmental")) {
               var2 = 0;
            }
            break;
         case 2002648650:
            if (var1.equals("dig_speed")) {
               var2 = 11;
            }
            break;
         case 2114147617:
            if (var1.equals("protection_fall")) {
               var2 = 2;
            }
            break;
         case 2114155484:
            if (var1.equals("protection_fire")) {
               var2 = 1;
            }
         }

         String var10000;
         switch(var2) {
         case 0:
            var10000 = "protection";
            break;
         case 1:
            var10000 = "fire_protection";
            break;
         case 2:
            var10000 = "feather_falling";
            break;
         case 3:
            var10000 = "blast_protection";
            break;
         case 4:
            var10000 = "projectile_protection";
            break;
         case 5:
            var10000 = "respiration";
            break;
         case 6:
            var10000 = "aqua_affinity";
            break;
         case 7:
            var10000 = "sharpness";
            break;
         case 8:
            var10000 = "smite";
            break;
         case 9:
            var10000 = "bane_of_arthropods";
            break;
         case 10:
            var10000 = "looting";
            break;
         case 11:
            var10000 = "efficiency";
            break;
         case 12:
            var10000 = "unbreaking";
            break;
         case 13:
            var10000 = "fortune";
            break;
         case 14:
            var10000 = "power";
            break;
         case 15:
            var10000 = "punch";
            break;
         case 16:
            var10000 = "flame";
            break;
         case 17:
            var10000 = "infinity";
            break;
         case 18:
            var10000 = "luck_of_the_sea";
            break;
         default:
            var10000 = enchant.toLowerCase();
         }

         return var10000;
      }
   }
}
