package com.volmit.iris.core.tools;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.gui.PregeneratorJob;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.pregenerator.PregenTask;
import com.volmit.iris.core.pregenerator.PregeneratorMethod;
import com.volmit.iris.core.pregenerator.methods.CachedPregenMethod;
import com.volmit.iris.core.pregenerator.methods.HybridPregenMethod;
import com.volmit.iris.core.service.StudioSVC;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import com.volmit.iris.util.plugin.VolmitSender;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.ApiStatus.Internal;

public class IrisToolbelt {
   @Internal
   public static Map<String, Boolean> toolbeltConfiguration = new HashMap();

   public static IrisDimension getDimension(String dimension) {
      File var1 = Iris.instance.getDataFolder(new String[]{"packs", var0});
      if (!var1.exists()) {
         ((StudioSVC)Iris.service(StudioSVC.class)).downloadSearch(new VolmitSender(Bukkit.getConsoleSender(), Iris.instance.getTag()), var0, false, false);
      }

      return !var1.exists() ? null : (IrisDimension)IrisData.get(var1).getDimensionLoader().load(var0);
   }

   public static IrisCreator createWorld() {
      return new IrisCreator();
   }

   public static boolean isIrisWorld(World world) {
      if (var0 == null) {
         return false;
      } else {
         ChunkGenerator var2 = var0.getGenerator();
         if (var2 instanceof PlatformChunkGenerator) {
            PlatformChunkGenerator var1 = (PlatformChunkGenerator)var2;
            var1.touch(var0);
            return true;
         } else {
            return false;
         }
      }
   }

   public static boolean isIrisStudioWorld(World world) {
      return isIrisWorld(var0) && access(var0).isStudio();
   }

   public static PlatformChunkGenerator access(World world) {
      return isIrisWorld(var0) ? (PlatformChunkGenerator)var0.getGenerator() : null;
   }

   public static PregeneratorJob pregenerate(PregenTask task, PregeneratorMethod method, Engine engine) {
      return pregenerate(var0, var1, var2, IrisSettings.get().getPregen().useCacheByDefault);
   }

   public static PregeneratorJob pregenerate(PregenTask task, PregeneratorMethod method, Engine engine, boolean cached) {
      return new PregeneratorJob(var0, (PregeneratorMethod)(var3 && var2 != null ? new CachedPregenMethod(var1, var2.getWorld().name()) : var1), var2);
   }

   public static PregeneratorJob pregenerate(PregenTask task, PlatformChunkGenerator gen) {
      return pregenerate(var0, new HybridPregenMethod(var1.getEngine().getWorld().realWorld(), IrisSettings.getThreadCount(IrisSettings.get().getConcurrency().getParallelism())), var1.getEngine());
   }

   public static PregeneratorJob pregenerate(PregenTask task, World world) {
      return isIrisWorld(var1) ? pregenerate(var0, access(var1)) : pregenerate(var0, new HybridPregenMethod(var1, IrisSettings.getThreadCount(IrisSettings.get().getConcurrency().getParallelism())), (Engine)null);
   }

   public static boolean evacuate(World world) {
      Iterator var1 = Bukkit.getWorlds().iterator();

      World var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (World)var1.next();
      } while(var2.getName().equals(var0.getName()));

      Iterator var3 = var0.getPlayers().iterator();

      while(var3.hasNext()) {
         Player var4 = (Player)var3.next();
         (new VolmitSender(var4, Iris.instance.getTag())).sendMessage("You have been evacuated from this world.");
         var4.teleport(var2.getSpawnLocation());
      }

      return true;
   }

   public static boolean evacuate(World world, String m) {
      Iterator var2 = Bukkit.getWorlds().iterator();

      World var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (World)var2.next();
      } while(var3.getName().equals(var0.getName()));

      Iterator var4 = var0.getPlayers().iterator();

      while(var4.hasNext()) {
         Player var5 = (Player)var4.next();
         (new VolmitSender(var5, Iris.instance.getTag())).sendMessage("You have been evacuated from this world. " + var1);
         var5.teleport(var3.getSpawnLocation());
      }

      return true;
   }

   public static boolean isStudio(World i) {
      return isIrisWorld(var0) && access(var0).isStudio();
   }

   public static void retainMantleDataForSlice(String className) {
      toolbeltConfiguration.put("retain.mantle." + var0, Boolean.TRUE);
   }

   public static boolean isRetainingMantleDataForSlice(String className) {
      return !toolbeltConfiguration.isEmpty() && toolbeltConfiguration.get("retain.mantle." + var0) == Boolean.TRUE;
   }

   public static <T> T getMantleData(World world, int x, int y, int z, Class<T> of) {
      PlatformChunkGenerator var5 = access(var0);
      return var5 == null ? null : var5.getEngine().getMantle().getMantle().get(var1, var2 - var0.getMinHeight(), var3, var4);
   }

   public static <T> void deleteMantleData(World world, int x, int y, int z, Class<T> of) {
      PlatformChunkGenerator var5 = access(var0);
      if (var5 != null) {
         var5.getEngine().getMantle().getMantle().remove(var1, var2 - var0.getMinHeight(), var3, var4);
      }
   }

   public static boolean removeWorld(World world) {
      return IrisCreator.removeFromBukkitYml(var0.getName());
   }
}
