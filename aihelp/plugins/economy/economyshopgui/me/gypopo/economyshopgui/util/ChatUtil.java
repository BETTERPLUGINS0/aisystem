package me.gypopo.economyshopgui.util;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.gypopo.economyshopgui.EconomyShopGUI;
import org.bukkit.ChatColor;

public class ChatUtil {
   private static final Pattern rgbPattern = Pattern.compile("#[a-fA-F0-9]{6}");
   private static final Pattern startGradientPattern = Pattern.compile("<gradient:#[a-fA-F0-9]{6}>");
   private static final Pattern endGradientPattern = Pattern.compile("</gradient:#[a-fA-F0-9]{6}>");
   private static final AdventureUtil adventureUtils;
   private static final boolean supportsRGB;

   public static AdventureUtil getAdventureUtils() {
      return adventureUtils;
   }

   public static String getLastColor(String input) {
      String result = "";
      int length = input.length();
      int index = length - 1;

      while(true) {
         label60: {
            if (index > -1) {
               char section = input.charAt(index);
               if (section != 167 && section != '&' || index >= length - 1) {
                  if (section == '#' && index < length - 6 && supportsRGB && ServerInfo.supportsSpigot()) {
                     String rgb = input.substring(index, index + 6);

                     try {
                        Color.decode(rgb);
                        return section + rgb;
                     } catch (NumberFormatException var7) {
                     }
                  }
                  break label60;
               }

               char c = input.charAt(index + 1);
               ChatColor color = ChatColor.getByChar(c);
               if (color == null) {
                  break label60;
               }

               if (!color.isColor() && !color.equals(ChatColor.RESET)) {
                  if (color.isFormat()) {
                     result = color.toString() + result;
                  }
                  break label60;
               }

               result = color.toString() + result;
            }

            return result;
         }

         --index;
      }
   }

   private static String formatLegacy(String msg) {
      if (supportsRGB) {
         Gradient gradient = findGradient(msg);
         if (gradient != null) {
            msg = gradient.get();
         }

         for(Matcher matcher = rgbPattern.matcher(msg); matcher.find(); matcher = rgbPattern.matcher(msg)) {
            String hex = msg.substring(matcher.start(), matcher.end());
            net.md_5.bungee.api.ChatColor rgb = net.md_5.bungee.api.ChatColor.of(hex);
            if (matcher.start() != 0 && matcher.end() != msg.length()) {
               if (msg.charAt(matcher.start() - 1) == '&') {
                  hex = "&" + hex;
               }

               if (msg.charAt(matcher.start() - 1) == '{' && msg.charAt(matcher.end()) == '}') {
                  hex = "{" + hex + "}";
               }
            }

            msg = msg.replace(hex, String.valueOf(rgb));
         }
      }

      return ChatColor.translateAlternateColorCodes('&', msg).replace("\\n", "\n");
   }

   private static Gradient findGradient(String value) {
      Matcher matcher = startGradientPattern.matcher(value);
      if (matcher.find()) {
         String g1 = value.substring(matcher.start(), matcher.end());
         matcher = endGradientPattern.matcher(value);
         if (matcher.find()) {
            String g2 = value.substring(matcher.start(), matcher.end());
            return new Gradient(g1, g2, value);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private static String formatVanilla(String msg) {
      if (supportsRGB) {
         for(Matcher matcher = rgbPattern.matcher(msg); matcher.find(); matcher = rgbPattern.matcher(msg)) {
            String hex = msg.substring(matcher.start(), matcher.end());
            net.md_5.bungee.api.ChatColor rgb = net.md_5.bungee.api.ChatColor.of(hex);
            if (matcher.start() != 0 && matcher.end() != msg.length()) {
               if (msg.charAt(matcher.start() - 1) == '&') {
                  hex = "&" + hex;
               }

               if (msg.charAt(matcher.start() - 1) == '{' && msg.charAt(matcher.end()) == '}') {
                  hex = "{" + hex + "}";
               }
            }

            msg = msg.replace(hex, String.valueOf(rgb));
         }
      }

      return ChatColor.translateAlternateColorCodes('&', msg).replace("\\n", "\n");
   }

   public static boolean hasRGB(String s) {
      if (ServerInfo.getVersion().olderOrEqualAs(ServerInfo.Version.v1_15_R1)) {
         return false;
      } else {
         if (adventureUtils != null) {
            s = adventureUtils.formatMiniToLegacy(s);
         }

         String stripped = formatLegacy(s).replace("§x", "#").replaceAll("§", "");
         return rgbPattern.matcher(stripped).find();
      }
   }

   public static String formatColors(String msg) {
      if (adventureUtils != null) {
         msg = adventureUtils.formatMiniToLegacy(msg);
      }

      return formatLegacy(msg);
   }

   public static String formatColorsRaw(String s) {
      return EconomyShopGUI.getInstance().paperMeta ? adventureUtils.formatLegacyToMini(s) : formatColors(s);
   }

   public static String stripColor(String s) {
      return adventureUtils != null ? adventureUtils.stripColor(s) : ChatColor.stripColor(s);
   }

   public static String getLastColors(String s) {
      String var1;
      if (adventureUtils != null) {
         AdventureUtil var10000 = adventureUtils;
         var1 = AdventureUtil.getLastColor(s);
      } else {
         var1 = getLastColor(s);
      }

      return var1;
   }

   public static String getGsonComponent(String s) {
      return adventureUtils.getGsonComponent(s);
   }

   private static boolean isMiniLibAvailable() {
      try {
         Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
         return true;
      } catch (ClassNotFoundException var1) {
         return false;
      }
   }

   static {
      supportsRGB = ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_16_R1);
      adventureUtils = ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_19_R1) ? new AdventureUtil() : null;
   }
}
