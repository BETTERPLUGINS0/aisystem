package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.UnsafeValues;

public final class ServerUtils {
   private static final boolean IS_PAPER;
   private static final String MAPPINGS_VERSION;

   private static String findMappingsVersion() {
      UnsafeValues unsafeValues = Bukkit.getUnsafe();

      Method getMappingsVersionMethod;
      try {
         getMappingsVersionMethod = unsafeValues.getClass().getDeclaredMethod("getMappingsVersion");
      } catch (SecurityException | NoSuchMethodException var4) {
         throw new RuntimeException("Could not find method 'getMappingsVersion' in the UnsafeValues implementation!", var4);
      }

      try {
         return (String)Unsafe.cast(getMappingsVersionMethod.invoke(unsafeValues));
      } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var3) {
         throw new RuntimeException("Could not retrieve the server's mappings version!", var3);
      }
   }

   private static String findPaperMinecraftVersion() {
      try {
         Class<?> serverBuildInfoClass = Class.forName("io.papermc.paper.ServerBuildInfo");
         Object serverBuildInfo = serverBuildInfoClass.getMethod("buildInfo").invoke((Object)null);
         Object serverVersion = serverBuildInfoClass.getMethod("minecraftVersionId").invoke(serverBuildInfo);

         assert serverVersion != null;

         return (String)Unsafe.assertNonNull(serverVersion.toString());
      } catch (Exception var3) {
         throw new RuntimeException("Could not retrieve the server's Minecraft version!", var3);
      }
   }

   public static boolean isPaper() {
      return IS_PAPER;
   }

   public static String getMappingsVersion() {
      return MAPPINGS_VERSION;
   }

   public static String getCraftBukkitPackage() {
      Package pkg = (Package)Unsafe.assertNonNull(Bukkit.getServer().getClass().getPackage());
      return pkg.getName();
   }

   public static int getDataVersion() {
      return Bukkit.getUnsafe().getDataVersion();
   }

   private ServerUtils() {
   }

   static {
      boolean isPaper;
      try {
         Class.forName("io.papermc.paper.registry.RegistryAccess");
         isPaper = true;
      } catch (ClassNotFoundException var3) {
         isPaper = false;
      }

      IS_PAPER = isPaper;

      String mappingsVersion;
      try {
         mappingsVersion = findMappingsVersion();
      } catch (Exception var4) {
         if (!isPaper) {
            throw var4;
         }

         mappingsVersion = findPaperMinecraftVersion();
      }

      MAPPINGS_VERSION = mappingsVersion;
   }
}
