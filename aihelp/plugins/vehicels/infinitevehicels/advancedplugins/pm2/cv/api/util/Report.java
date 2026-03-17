package advancedplugins.pm2.cv.api.util;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import org.bukkit.Bukkit;

public class Report {
   public static void error(Throwable var0, String var1) {
      InfiniteVehicles.getPlugin().getLogger().warning(var1.formatted(new Object[]{var0.getMessage()}));
      var0.printStackTrace();
   }

   public static void info(String var0) {
      Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&e[IV-INFO] &b%s".formatted(new Object[]{var0})));
   }
}
