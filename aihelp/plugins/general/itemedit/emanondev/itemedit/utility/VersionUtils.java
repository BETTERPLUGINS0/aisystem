package emanondev.itemedit.utility;

import org.bukkit.Bukkit;

public final class VersionUtils {
   private static final String[] VERSION_PARTS = safeSplitVersion();
   public static final int GAME_MAIN_VERSION;
   public static final int GAME_VERSION;
   public static final int GAME_SUB_VERSION;
   private static final boolean HAS_PAPER;
   private static final boolean HAS_FOLIA;
   private static final boolean HAS_PURPUR;

   private VersionUtils() {
      throw new UnsupportedOperationException("VersionUtils is a utility class and cannot be instantiated.");
   }

   public static String getVersionType() {
      return hasFoliaAPI() ? "Folia" : (hasPurpurAPI() ? "Purpur" : (hasPaperAPI() ? "Paper" : "Spigot"));
   }

   public static String getVersionNumber() {
      return GAME_MAIN_VERSION + "." + GAME_VERSION + "." + GAME_SUB_VERSION;
   }

   public static String getVersion() {
      return getVersionType() + " " + getVersionNumber();
   }

   private static String[] safeSplitVersion() {
      try {
         return Bukkit.getBukkitVersion().split("-")[0].split("\\.");
      } catch (Exception var1) {
         throw new IllegalStateException("Invalid Bukkit version format: " + Bukkit.getBukkitVersion(), var1);
      }
   }

   public static boolean isVersionUpTo(int major, int minor) {
      return isVersionUpTo(major, minor, 99);
   }

   public static boolean isVersionUpTo(int major, int minor, int patch) {
      if (GAME_MAIN_VERSION > major) {
         return false;
      } else if (GAME_MAIN_VERSION < major) {
         return true;
      } else if (GAME_VERSION > minor) {
         return false;
      } else if (GAME_VERSION < minor) {
         return true;
      } else {
         return GAME_SUB_VERSION <= patch;
      }
   }

   public static boolean isVersionAfter(int major, int minor) {
      return isVersionAfter(major, minor, 0);
   }

   public static boolean isVersionAfter(int major, int minor, int patch) {
      if (GAME_MAIN_VERSION < major) {
         return false;
      } else if (GAME_MAIN_VERSION > major) {
         return true;
      } else if (GAME_VERSION < minor) {
         return false;
      } else if (GAME_VERSION > minor) {
         return true;
      } else {
         return GAME_SUB_VERSION >= patch;
      }
   }

   public static boolean isVersionInRange(int majorMin, int minorMin, int majorMax, int minorMax) {
      return isVersionInRange(majorMin, minorMin, 0, majorMax, minorMax, 99);
   }

   public static boolean isVersionInRange(int majorMin, int minorMin, int patchMin, int majorMax, int minorMax, int patchMax) {
      return isVersionAfter(majorMin, minorMin, patchMin) && isVersionUpTo(majorMax, minorMax, patchMax);
   }

   public static boolean hasPaperAPI() {
      return HAS_PAPER;
   }

   public static boolean hasPurpurAPI() {
      return HAS_PURPUR;
   }

   public static boolean hasFoliaAPI() {
      return HAS_FOLIA;
   }

   static {
      GAME_MAIN_VERSION = Integer.parseInt(VERSION_PARTS[0]);
      GAME_VERSION = Integer.parseInt(VERSION_PARTS[1]);
      GAME_SUB_VERSION = VERSION_PARTS.length < 3 ? 0 : Integer.parseInt(VERSION_PARTS[2]);
      HAS_PAPER = ReflectionUtils.isClassPresent("com.destroystokyo.paper.VersionHistoryManager$VersionData");
      HAS_FOLIA = ReflectionUtils.isClassPresent("io.papermc.paper.threadedregions.RegionizedServer");
      HAS_PURPUR = ReflectionUtils.isClassPresent("org.purpurmc.purpur.event.PlayerAFKEvent");
   }
}
