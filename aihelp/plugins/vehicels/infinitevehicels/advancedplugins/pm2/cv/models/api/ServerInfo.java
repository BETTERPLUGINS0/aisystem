package advancedplugins.pm2.cv.models.api;

import java.util.Objects;
import org.bukkit.Bukkit;

public class ServerInfo {
   public static final boolean IS_PAPER = classExists("com.destroystokyo.paper.VersionHistoryManager$VersionData");
   public static final boolean IS_FOLIA = classExists("io.papermc.paper.threadedregions.RegionizedServer");
   public static final String NMS_VERSION = (String)Objects.requireNonNull(Bukkit.getServer().getVersion().split(" \\(MC: ")[1].replace(")", ""));
   public static final int VERSION_NUMBER;
   public static boolean HAS_VIAVERSION;
   public static boolean HAS_CITIZENS;

   public static boolean higherThanOrEqual(String var0) {
      String[] var1 = var0.split("\\.");
      String[] var2 = NMS_VERSION.split("\\.");
      if (var1.length == 2) {
         return higherThanOrEqualSubVersion(var1, var2);
      } else if (var1.length != 3) {
         return false;
      } else {
         try {
            int var3 = Integer.parseInt(var1[2]);
            int var4 = Integer.parseInt(var2[2]);
            return var4 >= var3 && higherThanOrEqualSubVersion(var1, var2);
         } catch (NumberFormatException var5) {
            return false;
         }
      }
   }

   private static boolean higherThanOrEqualSubVersion(String[] var0, String[] var1) {
      try {
         int var2 = Integer.parseInt(var0[1]);
         int var3 = Integer.parseInt(var1[1]);
         return var3 >= var2;
      } catch (NumberFormatException var4) {
         return false;
      }
   }

   private static boolean classExists(String var0) {
      try {
         Class.forName(var0);
         return true;
      } catch (ClassNotFoundException var2) {
         return false;
      }
   }

   public static int getCurrentTick() {
      if (!IS_PAPER) {
         try {
            Class var0 = Class.forName("net.minecraft.server.MinecraftServer");
            return var0.getDeclaredField("currentTick").getInt((Object)null);
         } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException var1) {
         }
      }

      return Bukkit.getCurrentTick();
   }

   static {
      VERSION_NUMBER = Integer.parseInt(NMS_VERSION.split("\\.")[1]);
   }
}
