package com.volmit.iris.core;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.ResourceLoader;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.nms.datapack.DataVersion;
import com.volmit.iris.core.nms.datapack.IDataFixer;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisBiomeCustom;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisDimensionTypeOptions;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.misc.ServerProperties;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.J;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.Stream;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class ServerConfigurator {
   public static void configure() {
      IrisSettings.IrisSettingsAutoconfiguration var0 = IrisSettings.get().getAutoConfiguration();
      if (var0.isConfigureSpigotTimeoutTime()) {
         J.attempt(ServerConfigurator::increaseKeepAliveSpigot);
      }

      if (var0.isConfigurePaperWatchdogDelay()) {
         J.attempt(ServerConfigurator::increasePaperWatchdog);
      }

      installDataPacks(true);
   }

   private static void increaseKeepAliveSpigot() {
      File var0 = new File("spigot.yml");
      YamlConfiguration var1 = new YamlConfiguration();
      var1.load(var0);
      long var2 = var1.getLong("settings.timeout-time");
      long var4 = TimeUnit.MINUTES.toSeconds(5L);
      if (var2 < var4) {
         Iris.warn("Updating spigot.yml timeout-time: " + var2 + " -> " + var4 + " (5 minutes)");
         Iris.warn("You can disable this change (autoconfigureServer) in Iris settings, then change back the value.");
         var1.set("settings.timeout-time", var4);
         var1.save(var0);
      }

   }

   private static void increasePaperWatchdog() {
      File var0 = new File("config/paper-global.yml");
      YamlConfiguration var1 = new YamlConfiguration();
      var1.load(var0);
      long var2 = var1.getLong("watchdog.early-warning-delay");
      long var4 = TimeUnit.MINUTES.toMillis(3L);
      if (var2 < var4) {
         Iris.warn("Updating paper.yml watchdog early-warning-delay: " + var2 + " -> " + var4 + " (3 minutes)");
         Iris.warn("You can disable this change (autoconfigureServer) in Iris settings, then change back the value.");
         var1.set("watchdog.early-warning-delay", var4);
         var1.save(var0);
      }

   }

   private static KList<File> getDatapacksFolder() {
      if (!IrisSettings.get().getGeneral().forceMainWorld.isEmpty()) {
         return (new KList()).qadd(new File(Bukkit.getWorldContainer(), IrisSettings.get().getGeneral().forceMainWorld + "/datapacks"));
      } else {
         KList var0 = new KList();
         Bukkit.getServer().getWorlds().forEach((var1) -> {
            var0.add((Object)(new File(var1.getWorldFolder(), "datapacks")));
         });
         if (var0.isEmpty()) {
            var0.add((Object)(new File(Bukkit.getWorldContainer(), ServerProperties.LEVEL_NAME + "/datapacks")));
         }

         return var0;
      }
   }

   public static boolean installDataPacks(boolean fullInstall) {
      return installDataPacks(DataVersion.getDefault(), var0);
   }

   public static boolean installDataPacks(IDataFixer fixer, boolean fullInstall) {
      if (var0 == null) {
         Iris.error("Unable to install datapacks, fixer is null!");
         return false;
      } else {
         Iris.info("Checking Data Packs...");
         ServerConfigurator.DimensionHeight var2 = new ServerConfigurator.DimensionHeight(var0);
         KList var3 = getDatapacksFolder();
         KMap var4 = new KMap();
         Stream var5 = allPacks();

         try {
            Objects.requireNonNull(var2);
            ((Stream)var5.flatMap(var2::merge).parallel()).forEach((var3x) -> {
               Iris.verbose("  Checking Dimension " + var3x.getLoadFile().getPath());
               Objects.requireNonNull(var3x);
               var3x.installBiomes(var0, var3x::getLoader, var3, (KSet)var4.computeIfAbsent(var3x.getLoadKey(), (var0x) -> {
                  return new KSet(new String[0]);
               }));
               var3x.installDimensionType(var0, var3);
            });
         } catch (Throwable var9) {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }
            }

            throw var9;
         }

         if (var5 != null) {
            var5.close();
         }

         IrisDimension.writeShared(var3, var2);
         Iris.info("Data Packs Setup!");
         return var1 && verifyDataPacksPost(IrisSettings.get().getAutoConfiguration().isAutoRestartOnCustomBiomeInstall());
      }
   }

   private static boolean verifyDataPacksPost(boolean allowRestarting) {
      Stream var1 = allPacks();

      boolean var8;
      label65: {
         try {
            boolean var2 = var1.map((var0x) -> {
               Iris.verbose("Checking Pack: " + var0x.getDataFolder().getPath());
               ResourceLoader var1 = var0x.getDimensionLoader();
               return var1.loadAll(var1.getPossibleKeys()).stream().filter(Objects::nonNull).map(ServerConfigurator::verifyDataPackInstalled).toList().contains(false);
            }).toList().contains(true);
            if (!var2) {
               var8 = false;
               break label65;
            }
         } catch (Throwable var5) {
            if (var1 != null) {
               try {
                  var1.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (var1 != null) {
            var1.close();
         }

         if (var0) {
            restart();
         } else if (INMS.get().supportsDataPacks()) {
            Iris.error("============================================================================");
            Iris.error(String.valueOf(C.ITALIC) + "You need to restart your server to properly generate custom biomes.");
            Iris.error(String.valueOf(C.ITALIC) + "By continuing, Iris will use backup biomes in place of the custom biomes.");
            Iris.error("----------------------------------------------------------------------------");
            Iris.error(String.valueOf(C.UNDERLINE) + "IT IS HIGHLY RECOMMENDED YOU RESTART THE SERVER BEFORE GENERATING!");
            Iris.error("============================================================================");
            Iterator var6 = Bukkit.getOnlinePlayers().iterator();

            while(true) {
               Player var7;
               do {
                  if (!var6.hasNext()) {
                     J.sleep(3000L);
                     return true;
                  }

                  var7 = (Player)var6.next();
               } while(!var7.isOp() && !var7.hasPermission("iris.all"));

               VolmitSender var3 = new VolmitSender(var7, Iris.instance.getTag("WARNING"));
               var3.sendMessage("There are some Iris Packs that have custom biomes in them");
               var3.sendMessage("You need to restart your server to use these packs.");
            }
         }

         return true;
      }

      if (var1 != null) {
         var1.close();
      }

      return var8;
   }

   public static void restart() {
      J.s(() -> {
         Iris.warn("New data pack entries have been installed in Iris! Restarting server!");
         Iris.warn("This will only happen when your pack changes (updates/first time setup)");
         Iris.warn("(You can disable this auto restart in iris settings)");
         J.s(() -> {
            Iris.warn("Looks like the restart command didn't work. Stopping the server instead!");
            Bukkit.shutdown();
         }, 100);
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
      });
   }

   public static boolean verifyDataPackInstalled(IrisDimension dimension) {
      KSet var1 = new KSet(new String[0]);
      boolean var2 = false;
      Objects.requireNonNull(var0);
      Iterator var3 = var0.getAllBiomes(var0::getLoader).iterator();

      while(true) {
         IrisBiome var4;
         do {
            if (!var3.hasNext()) {
               String var7 = getWorld(var0.getLoader());
               if (var7 == null) {
                  var7 = var0.getLoadKey();
               } else {
                  var7 = var7 + "/" + var0.getLoadKey();
               }

               if (!INMS.get().supportsDataPacks()) {
                  if (!var1.isEmpty()) {
                     Iris.warn("===================================================================================");
                     Iris.warn("Pack " + var7 + " has " + var1.size() + " custom biome(s). ");
                     Iris.warn("Your server version does not yet support datapacks for iris.");
                     Iris.warn("The world will generate these biomes as backup biomes.");
                     Iris.warn("====================================================================================");
                  }

                  return true;
               }

               Iterator var8 = var1.iterator();

               while(var8.hasNext()) {
                  String var9 = (String)var8.next();
                  Object var10 = INMS.get().getCustomBiomeBaseFor(var9);
                  if (var10 == null) {
                     Iris.warn("The Biome " + var9 + " is not registered on the server.");
                     var2 = true;
                  }
               }

               if (INMS.get().missingDimensionTypes(var0.getDimensionTypeKey())) {
                  Iris.warn("The Dimension Type for " + String.valueOf(var0.getLoadFile()) + " is not registered on the server.");
                  var2 = true;
               }

               if (var2) {
                  Iris.error("The Pack " + var7 + " is INCAPABLE of generating custom biomes");
                  Iris.error("If not done automatically, restart your server before generating with this pack!");
               }

               return !var2;
            }

            var4 = (IrisBiome)var3.next();
         } while(!var4.isCustom());

         Iterator var5 = var4.getCustomDerivitives().iterator();

         while(var5.hasNext()) {
            IrisBiomeCustom var6 = (IrisBiomeCustom)var5.next();
            String var10001 = var0.getLoadKey();
            var1.add(var10001 + ":" + var6.getId());
         }
      }
   }

   public static Stream<IrisData> allPacks() {
      return Stream.concat(listFiles(Iris.instance.getDataFolder(new String[]{"packs"})).filter(File::isDirectory).filter((var0) -> {
         File[] var1 = (new File(var0, "dimensions")).listFiles();
         return var1 != null && var1.length > 0;
      }).map(IrisData::get), IrisWorlds.get().getPacks());
   }

   @Nullable
   public static String getWorld(@NonNull IrisData data) {
      if (var0 == null) {
         throw new NullPointerException("data is marked non-null but is null");
      } else {
         String var1 = Bukkit.getWorldContainer().getAbsolutePath();
         if (!var1.endsWith(File.separator)) {
            var1 = var1 + File.separator;
         }

         String var2 = var0.getDataFolder().getAbsolutePath();
         if (!var2.startsWith(var1)) {
            return null;
         } else {
            int var3 = var2.endsWith(File.separator) ? 11 : 10;
            return var2.substring(var1.length(), var2.length() - var3);
         }
      }
   }

   private static Stream<File> listFiles(File parent) {
      try {
         return !var0.isDirectory() ? Stream.empty() : Files.walk(var0.toPath()).map(Path::toFile);
      } catch (Throwable var2) {
         throw var2;
      }
   }

   public static class DimensionHeight {
      private final IDataFixer fixer;
      private final AtomicIntegerArray[] dimensions = new AtomicIntegerArray[3];

      public DimensionHeight(IDataFixer fixer) {
         this.fixer = var1;

         for(int var2 = 0; var2 < 3; ++var2) {
            this.dimensions[var2] = new AtomicIntegerArray(new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE});
         }

      }

      public Stream<IrisDimension> merge(IrisData data) {
         Iris.verbose("Checking Pack: " + var1.getDataFolder().getPath());
         ResourceLoader var2 = var1.getDimensionLoader();
         return var2.loadAll(var2.getPossibleKeys()).stream().filter(Objects::nonNull).peek(this::merge);
      }

      public void merge(IrisDimension dimension) {
         AtomicIntegerArray var2 = this.dimensions[var1.getBaseDimension().ordinal()];
         var2.updateAndGet(0, (var1x) -> {
            return Math.min(var1x, var1.getMinHeight());
         });
         var2.updateAndGet(1, (var1x) -> {
            return Math.max(var1x, var1.getMaxHeight());
         });
         var2.updateAndGet(2, (var1x) -> {
            return Math.max(var1x, var1.getLogicalHeight());
         });
      }

      public String[] jsonStrings() {
         IDataFixer.Dimension[] var1 = IDataFixer.Dimension.values();
         String[] var2 = new String[3];

         for(int var3 = 0; var3 < 3; ++var3) {
            var2[var3] = this.jsonString(var1[var3]);
         }

         return var2;
      }

      public String jsonString(IDataFixer.Dimension dimension) {
         AtomicIntegerArray var2 = this.dimensions[var1.ordinal()];
         int var3 = var2.get(0);
         int var4 = var2.get(1);
         int var5 = var2.get(2);
         return var3 != Integer.MAX_VALUE && var4 != Integer.MIN_VALUE && Integer.MIN_VALUE != var5 ? this.fixer.createDimension(var1, var3, var4 - var3, var5, (IrisDimensionTypeOptions)null).toString(4) : null;
      }
   }
}
