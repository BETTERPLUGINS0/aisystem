package me.gypopo.economyshopgui.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.gypopo.economyshopgui.methodes.SendMessage;
import net.md_5.bungee.api.ChatColor;

public class Gradient {
   private final String format;
   private final String original;
   private final String start;
   private final String end;
   private final List<ChatColor> colors = new ArrayList();
   private final Pattern rbgPattern = Pattern.compile("#[a-fA-F0-9]{6}");

   public Gradient(String startGradient, String endGradient, String original) {
      int start = original.indexOf(startGradient);
      int end = original.indexOf(endGradient) + endGradient.length();
      this.start = original.substring(0, start);
      this.end = original.substring(end);
      this.format = this.getFormat(this.start);
      this.original = original.substring(start, end).replace(startGradient, "").replace(endGradient, "");
      Color c1 = this.getColor(startGradient);
      if (c1 == null) {
         c1 = Color.decode("#D4AF37");
      }

      Color c2 = this.getColor(endGradient);
      if (c2 == null) {
         c2 = Color.decode("#FADA6F");
      }

      for(int i = 0; i < this.original.length(); ++i) {
         float ratio = (float)i / (float)this.original.length();
         int r = (int)((float)c2.getRed() * ratio + (float)c1.getRed() * (1.0F - ratio));
         int g = (int)((float)c2.getGreen() * ratio + (float)c1.getGreen() * (1.0F - ratio));
         int b = (int)((float)c2.getBlue() * ratio + (float)c1.getBlue() * (1.0F - ratio));
         this.colors.add(ChatColor.of(new Color(r, g, b)));
      }

   }

   private Color getColor(String gradientPattern) {
      Matcher m1 = this.rbgPattern.matcher(gradientPattern);
      if (m1.find()) {
         try {
            return Color.decode(gradientPattern.substring(m1.start(), m1.end()));
         } catch (NumberFormatException var4) {
            SendMessage.warnMessage("Invalid rgb format for '" + gradientPattern + "', using default gradient colors...");
         }
      } else {
         SendMessage.warnMessage("Invalid rgb format for '" + gradientPattern + "', using default gradient colors...");
      }

      return null;
   }

   public ChatColor getColor(int i) {
      return (ChatColor)this.colors.get(i);
   }

   public String get() {
      int i = 0;
      StringBuilder builder = new StringBuilder();
      builder.append(this.start);
      char[] var3 = this.original.toCharArray();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         char c = var3[var5];
         builder.append(this.getColor(i)).append(this.format).append(c);
         ++i;
      }

      builder.append(this.end);
      return builder.toString();
   }

   private String getFormat(String input) {
      String result = "";
      int length = input.length();

      for(int index = length - 1; index > -1; --index) {
         char section = input.charAt(index);
         if ((section == 167 || section == '&') && index < length - 1) {
            char c = input.charAt(index + 1);
            org.bukkit.ChatColor color = org.bukkit.ChatColor.getByChar(c);
            if (color != null && color.isFormat()) {
               result = color.toString() + result;
               if (color.isColor() || color.equals(org.bukkit.ChatColor.RESET)) {
                  break;
               }
            }
         }
      }

      return result;
   }
}
