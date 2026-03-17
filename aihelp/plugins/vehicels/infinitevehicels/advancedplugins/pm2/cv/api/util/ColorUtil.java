package advancedplugins.pm2.cv.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class ColorUtil {
   private static final Pattern HEX_PATTERN = Pattern.compile("(?:#|&#)([a-fA-F0-9]{6})");

   public static String translate(String var0) {
      if (var0 == null) {
         return null;
      } else {
         for(Matcher var1 = HEX_PATTERN.matcher(var0); var1.find(); var1 = HEX_PATTERN.matcher(var0)) {
            String var2 = var1.group(0);
            String var3 = "#" + var1.group(1);
            var0 = var0.replace(var2, String.valueOf(ChatColor.of(var3)).makeConcatWithConstants<invokedynamic>(String.valueOf(ChatColor.of(var3))));
         }

         return org.bukkit.ChatColor.translateAlternateColorCodes('&', var0);
      }
   }

   public static String untranslate(String var0) {
      if (var0 == null) {
         return null;
      } else {
         Pattern var1 = Pattern.compile("(?i)§x(?:§[0-9A-F]){6}");

         for(Matcher var2 = var1.matcher(var0); var2.find(); var2 = var1.matcher(var0)) {
            String var3 = var2.group();
            StringBuilder var4 = new StringBuilder("#");

            for(int var5 = 3; var5 < var3.length(); var5 += 2) {
               var4.append(var3.charAt(var5));
            }

            var0 = var0.replace(var3, var4.toString());
         }

         var0 = var0.replaceAll("(?i)§([0-9A-FK-OR])", "&$1");
         var0 = var0.replace("§", "");
         return var0;
      }
   }

   public static String translate(String var0, Player var1) {
      if (var0 == null) {
         return null;
      } else {
         var0 = translate(var0);
         return var0;
      }
   }

   public static List<String> translate(List<String> var0) {
      return (List)(new ArrayList(var0)).stream().map(ColorUtil::translate).collect(Collectors.toList());
   }
}
