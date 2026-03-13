package tntrun.utils;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public class FormattingCodesParser {
   private static final Pattern HEXCOLOUR = Pattern.compile("<#([A-Fa-f0-9]){6}>");

   public static String parseFormattingCodes(String message) {
      if (message.contains("<GRADIENT") || message.contains("<RAINBOW")) {
         message = IridiumColorAPI.process(message);
      }

      for(Matcher matcher = HEXCOLOUR.matcher(message); matcher.find(); matcher = HEXCOLOUR.matcher(message)) {
         StringBuilder sb = new StringBuilder();
         ChatColor hexColour = ChatColor.of(matcher.group().substring(1, matcher.group().length() - 1));
         sb.append(message.substring(0, matcher.start())).append(hexColour).append(message.substring(matcher.end()));
         message = sb.toString();
      }

      return ChatColor.translateAlternateColorCodes('&', message);
   }
}
