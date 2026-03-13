package com.volmit.iris.core.link;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.util.data.Cuboid;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldEditLink {
   private static final AtomicCache<Boolean> active = new AtomicCache();

   public static Cuboid getSelection(Player p) {
      if (!hasWorldEdit()) {
         return null;
      } else {
         try {
            Object var1 = Class.forName("com.sk89q.worldedit.WorldEdit").getDeclaredMethod("getInstance").invoke((Object)null);
            Object var2 = var1.getClass().getDeclaredMethod("getSessionManager").invoke(var1);
            Class var3 = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter");
            Object var4 = var3.getDeclaredMethod("adapt", World.class).invoke((Object)null, var0.getWorld());
            Object var5 = var3.getDeclaredMethod("adapt", Player.class).invoke((Object)null, var0);
            Object var6 = var2.getClass().getDeclaredMethod("getIfPresent", Class.forName("com.sk89q.worldedit.session.SessionOwner")).invoke(var2, var5);
            if (var6 == null) {
               return null;
            } else {
               Object var7 = null;

               try {
                  var7 = var6.getClass().getDeclaredMethod("getSelection", Class.forName("com.sk89q.worldedit.world.World")).invoke(var6, var4);
               } catch (InvocationTargetException var10) {
               }

               if (var7 == null) {
                  return null;
               } else {
                  Object var8 = var7.getClass().getDeclaredMethod("getMinimumPoint").invoke(var7);
                  Object var9 = var7.getClass().getDeclaredMethod("getMaximumPoint").invoke(var7);
                  return new Cuboid(var0.getWorld(), (Integer)var8.getClass().getDeclaredMethod("x").invoke(var8), (Integer)var8.getClass().getDeclaredMethod("y").invoke(var8), (Integer)var8.getClass().getDeclaredMethod("z").invoke(var8), (Integer)var8.getClass().getDeclaredMethod("x").invoke(var9), (Integer)var8.getClass().getDeclaredMethod("y").invoke(var9), (Integer)var8.getClass().getDeclaredMethod("z").invoke(var9));
               }
            }
         } catch (Throwable var11) {
            Iris.error("Could not get selection");
            var11.printStackTrace();
            Iris.reportError(var11);
            active.reset();
            active.aquire(() -> {
               return false;
            });
            return null;
         }
      }
   }

   public static boolean hasWorldEdit() {
      return (Boolean)active.aquire(() -> {
         return Bukkit.getPluginManager().isPluginEnabled("WorldEdit");
      });
   }
}
