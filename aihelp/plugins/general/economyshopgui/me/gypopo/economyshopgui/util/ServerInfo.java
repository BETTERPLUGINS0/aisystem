package me.gypopo.economyshopgui.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import me.gypopo.economyshopgui.EconomyShopGUI;
import org.bukkit.Bukkit;

public class ServerInfo {
   private static ServerInfo.ServerType type;
   private static ServerInfo.Version version;
   private static boolean components;

   public static boolean supportsSpigot() {
      return type != ServerInfo.ServerType.BUKKIT;
   }

   public static boolean supportsPaper() {
      return type != ServerInfo.ServerType.BUKKIT && type != ServerInfo.ServerType.SPIGOT;
   }

   public static boolean usesMojangMappings() {
      try {
         return version.newerOrEqualAs(ServerInfo.Version.v1_21_R1) && Class.forName("net.minecraft.server.MinecraftServer").getDeclaredField("LOGGER") != null;
      } catch (ClassNotFoundException var1) {
         return false;
      } catch (NullPointerException | NoSuchFieldException var2) {
         return false;
      }
   }

   public static boolean supportsPaperNativeComponents() {
      try {
         Field c = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".inventory.CraftMetaItem").getDeclaredField("displayName");
         c.setAccessible(true);
         return c.getType() == Class.forName("net.minecraft.network.chat.Component");
      } catch (ClassNotFoundException | NoSuchFieldException var1) {
         return false;
      }
   }

   public static boolean supportsMiniMessageBukkit() {
      try {
         Class.forName("net.kyori.adventure.platform.bukkit.BukkitAudiences");
         return true;
      } catch (ClassNotFoundException var1) {
         return false;
      }
   }

   public static boolean isPaper() {
      return type == ServerInfo.ServerType.PAPER;
   }

   public static boolean isFolia() {
      return type == ServerInfo.ServerType.FOLIA;
   }

   public static boolean supportsComponents() {
      return components;
   }

   public static ServerInfo.Version getVersion() {
      return version;
   }

   public static boolean isNMS() {
      return version.getSimple() > ServerInfo.Version.v1_16_R3.getSimple();
   }

   public static Object invokeModernVersionMethod(String method, Object... args) throws Exception {
      Class<?>[] classes = new Class[args.length];

      for(int i = 0; i < args.length; ++i) {
         classes[i] = args[i].getClass();
      }

      return invokeModernVersionMethod(method, classes, args);
   }

   public static Object invokeModernVersionMethod(String method, Class<?>[] classes, Object... args) throws Exception {
      Class<?> clazz = EconomyShopGUI.getInstance().versionHandler.getClass();
      Method c = clazz.getDeclaredMethod(method, classes);
      c.setAccessible(true);
      return c.invoke(EconomyShopGUI.getInstance().versionHandler, args);
   }

   static {
      try {
         Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
         type = ServerInfo.ServerType.FOLIA;
      } catch (ClassNotFoundException var6) {
         try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            type = ServerInfo.ServerType.PAPER;
         } catch (ClassNotFoundException var5) {
            try {
               Class.forName("org.spigotmc.SpigotConfig");
               type = ServerInfo.ServerType.SPIGOT;
            } catch (ClassNotFoundException var4) {
               type = ServerInfo.ServerType.BUKKIT;
            }
         }
      }

      try {
         version = ServerInfo.Version.valueOf(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
      } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException var3) {
         version = ServerInfo.Version.getFromSimple(Bukkit.getBukkitVersion().split("-")[0]);
      }

      components = version != null && version.newerOrEqualAs(ServerInfo.Version.v1_20_R4);
   }

   private static enum ServerType {
      BUKKIT,
      SPIGOT,
      PAPER,
      FOLIA;

      // $FF: synthetic method
      private static ServerInfo.ServerType[] $values() {
         return new ServerInfo.ServerType[]{BUKKIT, SPIGOT, PAPER, FOLIA};
      }
   }

   public static enum Version {
      v1_21_R7(new String[]{"1.21.11"}),
      v1_21_R6(new String[]{"1.21.9", "1.21.10"}),
      v1_21_R5(new String[]{"1.21.6", "1.21.7", "1.21.8"}),
      v1_21_R4(new String[]{"1.21.5"}),
      v1_21_R3(new String[]{"1.21.4"}),
      v1_21_R2(new String[]{"1.21.2", "1.21.3"}),
      v1_21_R1(new String[]{"1.21", "1.21.1"}),
      v1_20_R4(new String[]{"1.20.5", "1.20.6"}),
      v1_20_R3(new String[]{"1.20.3", "1.20.4"}),
      v1_20_R2(new String[]{"1.20.2"}),
      v1_20_R1(new String[]{"1.20", "1.20.1"}),
      v1_19_R3(new String[]{"1.19.4"}),
      v1_19_R2(new String[]{"1.19.3"}),
      v1_19_R1(new String[]{"1.19", "1.19.1", "1.19.2"}),
      v1_18_R2(new String[]{"1.18.2"}),
      v1_18_R1(new String[]{"1.18", "1.18.1"}),
      v1_17_R1(new String[]{"1.17", "1.17.1"}),
      v1_16_R3(new String[]{"1.16.4", "1.16.5"}),
      v1_16_R2(new String[]{"1.16.2", "1.16.3"}),
      v1_16_R1(new String[]{"1.16", "1.16.1"}),
      v1_15_R1(new String[]{"1.15", "1.15.1", "1.15.2"}),
      v1_14_R1(new String[]{"1.14", "1.14.1", "1.14.2", "1.14.3", "1.14.4"}),
      v1_13_R2(new String[]{"1.13.1", "1.13.2"}),
      v1_13_R1(new String[]{"1.13"}),
      v1_12_R1(new String[]{"1.12", "1.12.1", "1.12.2"}),
      v1_11_R1(new String[]{"1.11", "1.11.1", "1.11.2"}),
      v1_10_R1(new String[]{"1.10", "1.10.1", "1.10.2"}),
      v1_9_R2(new String[]{"1.9.3", "1.9.4"}),
      v1_9_R1(new String[]{"1.9", "1.9.1", "1.9.2"}),
      v1_8_R3(new String[]{"1.8.4", "1.8.5", "1.8.6", "1.8.7", "1.8.8"}),
      v1_8_R2(new String[]{"1.8.1", "1.8.2", "1.8.3"}),
      v1_8_R1(new String[]{"1.8"});

      private final String[] versions;

      private Version(String... param3) {
         this.versions = versions;
      }

      public static ServerInfo.Version getFromSimple(String version) {
         ServerInfo.Version[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            ServerInfo.Version ver = var1[var3];
            if (Arrays.asList(ver.versions).contains(version)) {
               return ver;
            }
         }

         return null;
      }

      public int getSimple() {
         String first = String.valueOf(this.name().charAt(1));
         String second = this.name().split("_", 2)[1].split("_", 2)[0];
         if (second.length() == 1) {
            second = "0" + second;
         }

         String third = String.valueOf(this.name().charAt(this.name().length() - 1));
         return Integer.parseInt(first + second + third);
      }

      public boolean newerOrEqualAs(ServerInfo.Version version) {
         return this.getSimple() >= version.getSimple();
      }

      public boolean olderOrEqualAs(ServerInfo.Version version) {
         return this.getSimple() <= version.getSimple();
      }

      // $FF: synthetic method
      private static ServerInfo.Version[] $values() {
         return new ServerInfo.Version[]{v1_21_R7, v1_21_R6, v1_21_R5, v1_21_R4, v1_21_R3, v1_21_R2, v1_21_R1, v1_20_R4, v1_20_R3, v1_20_R2, v1_20_R1, v1_19_R3, v1_19_R2, v1_19_R1, v1_18_R2, v1_18_R1, v1_17_R1, v1_16_R3, v1_16_R2, v1_16_R1, v1_15_R1, v1_14_R1, v1_13_R2, v1_13_R1, v1_12_R1, v1_11_R1, v1_10_R1, v1_9_R2, v1_9_R1, v1_8_R3, v1_8_R2, v1_8_R1};
      }
   }
}
