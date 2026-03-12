package fr.xephi.authme.util;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import java.util.Collection;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public final class Utils {
   public static final long MILLIS_PER_MINUTE = 60000L;
   private static ConsoleLogger logger = ConsoleLoggerFactory.get(Utils.class);
   private static final String[] serverVersion = Bukkit.getServer().getBukkitVersion().substring(0, Bukkit.getServer().getBukkitVersion().indexOf("-")).split("\\.");
   private static final int FIRST_VERSION;
   public static final int MAJOR_VERSION;
   public static final int MINOR_VERSION;

   private Utils() {
   }

   public static Pattern safePatternCompile(String pattern) {
      try {
         return Pattern.compile(pattern);
      } catch (Exception var2) {
         logger.warning("Failed to compile pattern '" + pattern + "' - defaulting to allowing everything");
         return Pattern.compile(".*?");
      }
   }

   public static boolean isClassLoaded(String className) {
      try {
         Class.forName(className);
         return true;
      } catch (ClassNotFoundException var2) {
         return false;
      }
   }

   public static void logAndSendMessage(CommandSender sender, String message) {
      logger.info(message);
      if (sender != null && !(sender instanceof ConsoleCommandSender)) {
         sender.sendMessage(message);
      }

   }

   public static void logAndSendWarning(CommandSender sender, String message) {
      logger.warning(message);
      if (sender != null && !(sender instanceof ConsoleCommandSender)) {
         sender.sendMessage(ChatColor.RED + message);
      }

   }

   public static boolean isCollectionEmpty(Collection<?> coll) {
      return coll == null || coll.isEmpty();
   }

   public static boolean isEmailEmpty(String email) {
      return StringUtils.isBlank(email) || "your@email.com".equalsIgnoreCase(email);
   }

   static {
      FIRST_VERSION = Integer.parseInt(serverVersion[0]);
      MAJOR_VERSION = Integer.parseInt(serverVersion[1]);
      MINOR_VERSION = serverVersion.length == 3 ? Integer.parseInt(serverVersion[2]) : 0;
   }
}
