package com.nisovin.shopkeepers.compat;

import com.nisovin.shopkeepers.util.bukkit.ServerUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Compat {
   private static final Map<String, CompatVersion> COMPAT_VERSIONS = new LinkedHashMap();
   @Nullable
   private static CompatProvider provider;

   private static void register(CompatVersion version) {
      String compatVersion = version.getCompatVersion();
      if (COMPAT_VERSIONS.containsKey(compatVersion)) {
         throw new IllegalArgumentException("CompatVersion '" + compatVersion + "' is already registered!");
      } else {
         COMPAT_VERSIONS.put(compatVersion, version);
      }
   }

   @Nullable
   public static CompatVersion getCompatVersion(String compatVersion) {
      return (CompatVersion)COMPAT_VERSIONS.get(compatVersion);
   }

   @Nullable
   private static CompatVersion findCompatVersion(String mappingsVersion, String variant) {
      CompatVersion compatVersion = (CompatVersion)COMPAT_VERSIONS.values().stream().filter((x) -> {
         return x.getVariant().equals(variant) && x.getSupportedServerVersions().stream().anyMatch((v) -> {
            return v.getMappingsVersion().equals(mappingsVersion);
         });
      }).findFirst().orElse((Object)null);
      if (compatVersion == null && !variant.isEmpty()) {
         compatVersion = (CompatVersion)COMPAT_VERSIONS.values().stream().filter((x) -> {
            return !x.hasVariant() && x.getSupportedServerVersions().stream().anyMatch((v) -> {
               return v.getMappingsVersion().equals(mappingsVersion);
            });
         }).findFirst().orElse((Object)null);
      }

      return compatVersion;
   }

   public static boolean hasProvider() {
      return provider != null;
   }

   public static CompatProvider getProvider() {
      return (CompatProvider)Validate.State.notNull(provider, (String)"Compat provider is not set up!");
   }

   public static boolean load(Plugin plugin) {
      if (provider != null) {
         throw new IllegalStateException("Provider already loaded!");
      } else {
         if (isForceFallback(plugin)) {
            Log.warning("Force fallback: Shopkeepers is trying to run in 'fallback mode'.");
         } else {
            String mappingsVersion = ServerUtils.getMappingsVersion();
            String variant = ServerUtils.isPaper() ? "paper" : "";
            CompatVersion compatVersion = findCompatVersion(mappingsVersion, variant);
            if (compatVersion != null) {
               String compatVersionString = compatVersion.getCompatVersion();

               try {
                  Class<?> clazz = Class.forName("com.nisovin.shopkeepers.compat.v" + compatVersionString + ".CompatProviderImpl");
                  provider = (CompatProvider)clazz.getConstructor().newInstance();
                  Log.info("Compatibility provider loaded: " + compatVersionString);
                  return true;
               } catch (Exception var7) {
                  Log.severe((String)("Failed to load compatibility provider for version '" + compatVersionString + "'!"), (Throwable)var7);
               }
            }

            Log.warning("Incompatible server version: " + Bukkit.getBukkitVersion() + " (mappings: " + mappingsVersion + ", variant: " + (variant.isEmpty() ? "default" : variant) + ")");
            Log.warning("Shopkeepers is trying to run in 'fallback mode'.");
            Log.info("Check for updates at: " + plugin.getDescription().getWebsite());
         }

         try {
            provider = new FallbackCompatProvider();
            return true;
         } catch (Exception var6) {
            Log.severe((String)"Failed to enable 'fallback mode'!", (Throwable)var6);
            return false;
         }
      }
   }

   private static boolean isForceFallback(Plugin plugin) {
      Path pluginDataFolder = plugin.getDataFolder().toPath();
      Path forceFallbackFile = pluginDataFolder.resolve(".force-fallback");
      return Files.exists(forceFallbackFile, new LinkOption[0]);
   }

   static {
      register(new CompatVersion("1_21_R9_paper", "1.21.11", "1.21.11"));
      register(new CompatVersion("1_21_R9", "1.21.11", "e3cd927e07e6ff434793a0474c51b2b9"));
      register(new CompatVersion("1_21_R8_paper", "1.21.10", "1.21.10"));
      register(new CompatVersion("1_21_R8", "1.21.10", "614efe5192cd0510bc2ddc5feefa155d"));
      register(new CompatVersion("1_21_R7_paper", Arrays.asList(new ServerVersion("1.21.7", "1.21.7"), new ServerVersion("1.21.8", "1.21.8"))));
      register(new CompatVersion("1_21_R7", Arrays.asList(new ServerVersion("1.21.7", "98b42190c84edaa346fd96106ee35d6f"), new ServerVersion("1.21.8", "98b42190c84edaa346fd96106ee35d6f"))));
      register(new CompatVersion("1_21_R6_paper", "1.21.6", "1.21.6"));
      register(new CompatVersion("1_21_R6", "1.21.6", "164f8e872cb3dff744982fca079642b2"));
      register(new CompatVersion("1_21_R5_paper", "1.21.5", "7ecad754373a5fbc43d381d7450c53a5"));
      register(new CompatVersion("1_21_R5", "1.21.5", "7ecad754373a5fbc43d381d7450c53a5"));
      register(new CompatVersion("fallback", "fallback", "fallback"));
   }
}
