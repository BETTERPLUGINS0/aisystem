package com.volmit.iris.core.link;

import java.lang.reflect.Field;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.utils.result.Attempt;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.WorldManager;
import org.mvplugins.multiverse.core.world.options.ImportWorldOptions;

public class MultiverseCoreLink {
   private final boolean active = Bukkit.getPluginManager().getPlugin("Multiverse-Core") != null;

   public void removeFromConfig(World world) {
      this.removeFromConfig(var1.getName());
   }

   public void removeFromConfig(String world) {
      if (this.active) {
         WorldManager var2 = this.worldManager();
         Attempt var10000 = var2.removeWorld(var1);
         Objects.requireNonNull(var2);
         var10000.onSuccess(var2::saveWorldsConfig);
      }
   }

   public void updateWorld(World bukkitWorld, String pack) {
      try {
         if (this.active) {
            String var3 = "Iris:" + var2;
            WorldManager var4 = this.worldManager();
            MultiverseWorld var5 = (MultiverseWorld)var4.getWorld(var1).getOrElse(() -> {
               ImportWorldOptions var3x = ImportWorldOptions.worldName(var1.getName()).generator(var3).environment(var1.getEnvironment()).useSpawnAdjust(false);
               return (MultiverseWorld)var4.importWorld(var3x).get();
            });
            var5.setAutoLoad(false);
            if (!var3.equals(var5.getGenerator())) {
               Field var6 = MultiverseWorld.class.getDeclaredField("worldConfig");
               var6.setAccessible(true);
               Object var7 = var6.get(var5);
               var7.getClass().getDeclaredMethod("setGenerator", String.class).invoke(var7, var3);
            }

            var4.saveWorldsConfig();
         }
      } catch (Throwable var8) {
         throw var8;
      }
   }

   private WorldManager worldManager() {
      MultiverseCoreApi var1 = MultiverseCoreApi.get();
      return var1.getWorldManager();
   }
}
