package advancedplugins.pm2.cv.api.enums;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

public enum MinecraftVersion {
   UNKNOWN(Integer.MAX_VALUE),
   MC1_7_R4(174),
   MC1_8_R3(183),
   MC1_9_R1(191),
   MC1_9_R2(192),
   MC1_10_R1(1101),
   MC1_11_R1(1111),
   MC1_12_R1(1121),
   MC1_13_R1(1131),
   MC1_13_R2(1132),
   MC1_14_R1(1141),
   MC1_15_R1(1151),
   MC1_16_R1(1161),
   MC1_16_R2(1162),
   MC1_16_R3(1163),
   MC1_17_R1(1171),
   MC1_18_R1(1181, true),
   MC1_18_R2(1182, true),
   MC1_19_R1(1191, true),
   MC1_19_R2(1192, true),
   MC1_19_R3(1193, true),
   MC1_20_R1(1201, true),
   MC1_20_R2(1202, true),
   MC1_20_R3(1203, true),
   MC1_20_R4(1204, true),
   MC1_21_R1(1211, true),
   MC1_21_R2(1212, true),
   MC1_21_R3(1213, true),
   MC1_21_R4(1214, true),
   MC1_21_R5(1215, true),
   MC1_21_R5_SPIGOT(1215, false),
   MC1_21_R6(1216, true),
   MC1_21_R6_SPIGOT(1216, false),
   MC1_21_R7(1217, true, "MC1_21_R6"),
   MC1_21_R7_SPIGOT(1217, false),
   MC1_21_R8(1219, false, "MC1_21_R7"),
   MC1_21_R10(12111, false),
   MC1_21_R10P(12111, true);

   private static MinecraftVersion version;
   private static Boolean hasGsonSupport;
   private static Boolean isForgePresent;
   private static Boolean isFoliaPresent;
   private static Boolean isPaper = null;
   private final int versionId;
   private final boolean mojangMapping;
   private final boolean paperVersion;
   @Nullable
   public final String alternateID;
   private static final Map<String, MinecraftVersion> VERSION_TO_REVISION = new HashMap<String, MinecraftVersion>() {
      {
         this.put("1.20", MinecraftVersion.MC1_20_R1);
         this.put("1.20.1", MinecraftVersion.MC1_20_R1);
         this.put("1.20.2", MinecraftVersion.MC1_20_R2);
         this.put("1.20.3", MinecraftVersion.MC1_20_R3);
         this.put("1.20.4", MinecraftVersion.MC1_20_R3);
         this.put("1.20.5", MinecraftVersion.MC1_20_R4);
         this.put("1.20.6", MinecraftVersion.MC1_20_R4);
         this.put("1.21", MinecraftVersion.MC1_21_R1);
         this.put("1.21.1", MinecraftVersion.MC1_21_R1);
         this.put("1.21.2", MinecraftVersion.MC1_21_R2);
         this.put("1.21.3", MinecraftVersion.MC1_21_R2);
         this.put("1.21.4", MinecraftVersion.MC1_21_R3);
         this.put("1.21.5", MinecraftVersion.MC1_21_R4);
         this.put("1.21.6", MinecraftVersion.MC1_21_R5);
         this.put("1.21.7", MinecraftVersion.MC1_21_R6);
         this.put("1.21.8", MinecraftVersion.MC1_21_R6);
         this.put("1.21.9", MinecraftVersion.MC1_21_R8);
         this.put("1.21.10", MinecraftVersion.MC1_21_R8);
         this.put("1.21.11", MinecraftVersion.MC1_21_R10);
         this.put("1.21.11P", MinecraftVersion.MC1_21_R10P);
      }
   };
   private static String cachedVersionRaw = "";

   private MinecraftVersion(int param3) {
      this(var3, false, false);
   }

   private MinecraftVersion(int param3, boolean param4) {
      this(var3, var4, false);
   }

   private MinecraftVersion(int param3, boolean param4, String param5) {
      this(var3, var4, var5, false);
   }

   private MinecraftVersion(int param3, boolean param4, boolean param5) {
      this(var3, var4, (String)null, var5);
   }

   private MinecraftVersion(int param3, boolean param4, @Nullable String param5, boolean param6) {
      this.versionId = var3;
      this.mojangMapping = var4;
      this.alternateID = var5;
      this.paperVersion = var6;
   }

   public String getPackageName() {
      if (this == UNKNOWN) {
         try {
            return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
         } catch (Exception var2) {
         }
      }

      if (this == MC1_20_R4) {
         return "v1_20_R4";
      } else if (this == MC1_21_R5_SPIGOT) {
         return "v1_21_R5_spigot";
      } else if (this == MC1_21_R6_SPIGOT) {
         return "v1_21_R6_spigot";
      } else if (this == MC1_21_R7_SPIGOT) {
         return "v1_21_R6_spigot";
      } else if (this == MC1_21_R7) {
         return "v1_21_R6";
      } else if (isPaper() && exists(this.name() + "P")) {
         String var10000 = this.name();
         return var10000.replace("MC", "v") + "P";
      } else {
         return this.alternateID != null ? this.alternateID.replace("MC", "v") : this.name().replace("MC", "v");
      }
   }

   public static boolean isAtLeastVersion(MinecraftVersion var0) {
      return getVersion().getVersionId() >= var0.getVersionId();
   }

   public static boolean isNewerThan(MinecraftVersion var0) {
      return getVersion().getVersionId() > var0.getVersionId();
   }

   public static MinecraftVersion getVersion() {
      if (version != null) {
         return version;
      } else {
         try {
            String var0 = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            version = valueOf(var0.replace("v", "MC"));
         } catch (Exception var1) {
            version = (MinecraftVersion)VERSION_TO_REVISION.getOrDefault(Bukkit.getServer().getBukkitVersion().split("-")[0], UNKNOWN);
         }

         if (version == MC1_21_R5 && !isPaper()) {
            version = MC1_21_R5_SPIGOT;
         }

         if (version == MC1_21_R6 && !isPaper()) {
            version = MC1_21_R6_SPIGOT;
         }

         return version;
      }
   }

   public static boolean hasGsonSupport() {
      if (hasGsonSupport != null) {
         return hasGsonSupport;
      } else {
         try {
            Class.forName("com.google.gson.Gson");
            hasGsonSupport = true;
         } catch (Exception var1) {
            hasGsonSupport = false;
         }

         return hasGsonSupport;
      }
   }

   public static boolean isForgePresent() {
      if (isForgePresent != null) {
         return isForgePresent;
      } else {
         try {
            isForgePresent = true;
         } catch (Exception var1) {
            isForgePresent = false;
         }

         return isForgePresent;
      }
   }

   public static boolean isFoliaPresent() {
      if (isFoliaPresent != null) {
         return isFoliaPresent;
      } else {
         try {
            isFoliaPresent = true;
         } catch (Exception var1) {
            isFoliaPresent = false;
         }

         return isFoliaPresent;
      }
   }

   public static String getVersionRaw() {
      if (cachedVersionRaw.equals("")) {
         cachedVersionRaw = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
      }

      return cachedVersionRaw;
   }

   public static String getVersionFull() {
      if (cachedVersionRaw.equals("")) {
         String[] var0 = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
         cachedVersionRaw = var0.length > 3 ? var0[3] : var0[var0.length - 1];
      }

      String var1 = cachedVersionRaw;
      var1 = var1.replaceAll("v", "");
      var1 = var1.replaceAll("R", "");
      var1 = var1.replaceAll("_", "");
      var1 = var1.replaceAll("craftbukkit", "");
      return var1;
   }

   public static String getVersionFullRaw() {
      if (cachedVersionRaw.isEmpty()) {
         String[] var0 = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
         cachedVersionRaw = var0.length > 3 ? var0[3] : var0[var0.length - 1];
      }

      if (cachedVersionRaw.isEmpty() || cachedVersionRaw.equalsIgnoreCase("craftbukkit")) {
         cachedVersionRaw = Bukkit.getServer().getBukkitVersion().split("-")[0];
      }

      String var1 = cachedVersionRaw;
      var1 = var1.replaceAll("craftbukkit", "");
      return var1;
   }

   public static boolean isOlderOrEqualThanVersion(MinecraftVersion var0) {
      return getVersion().getVersionId() <= var0.getVersionId();
   }

   public static boolean isPaper() {
      if (isPaper == null) {
         try {
            InfiniteVehicles.getPlugin().getLogger().info("Found Paper: " + String.valueOf(Class.forName("com.destroystokyo.paper.event.server.ServerTickStartEvent")));
            isPaper = true;
         } catch (ClassNotFoundException var1) {
            InfiniteVehicles.getPlugin().getLogger().info("Using SpigotMC mappings...");
            isPaper = false;
         }
      }

      return isPaper;
   }

   public static boolean exists(String var0) {
      try {
         valueOf(var0);
         return true;
      } catch (IllegalArgumentException var2) {
         return false;
      }
   }

   public int getVersionId() {
      return this.versionId;
   }

   public boolean isMojangMapping() {
      return this.mojangMapping;
   }

   public boolean isPaperVersion() {
      return this.paperVersion;
   }

   @Nullable
   public String getAlternateID() {
      return this.alternateID;
   }

   // $FF: synthetic method
   private static MinecraftVersion[] $values() {
      return new MinecraftVersion[]{UNKNOWN, MC1_7_R4, MC1_8_R3, MC1_9_R1, MC1_9_R2, MC1_10_R1, MC1_11_R1, MC1_12_R1, MC1_13_R1, MC1_13_R2, MC1_14_R1, MC1_15_R1, MC1_16_R1, MC1_16_R2, MC1_16_R3, MC1_17_R1, MC1_18_R1, MC1_18_R2, MC1_19_R1, MC1_19_R2, MC1_19_R3, MC1_20_R1, MC1_20_R2, MC1_20_R3, MC1_20_R4, MC1_21_R1, MC1_21_R2, MC1_21_R3, MC1_21_R4, MC1_21_R5, MC1_21_R5_SPIGOT, MC1_21_R6, MC1_21_R6_SPIGOT, MC1_21_R7, MC1_21_R7_SPIGOT, MC1_21_R8, MC1_21_R10, MC1_21_R10P};
   }
}
