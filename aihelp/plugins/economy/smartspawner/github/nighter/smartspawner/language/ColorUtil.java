package github.nighter.smartspawner.language;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public class ColorUtil {
   private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

   public static String translateHexColorCodes(String message) {
      if (message == null) {
         return null;
      } else {
         Matcher matcher = HEX_PATTERN.matcher(message);
         StringBuffer buffer = new StringBuffer(message.length() + 32);

         while(matcher.find()) {
            String hexColor = matcher.group(1);
            String replacement = ChatColor.of("#" + hexColor).toString();
            matcher.appendReplacement(buffer, replacement);
         }

         matcher.appendTail(buffer);
         return org.bukkit.ChatColor.translateAlternateColorCodes('&', buffer.toString());
      }
   }
}
