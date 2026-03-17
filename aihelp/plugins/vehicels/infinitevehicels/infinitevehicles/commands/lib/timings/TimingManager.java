package me.PM2.infinitevehicles.commands.lib.timings;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.plugin.Plugin;

public class TimingManager {
   private static TimingType timingProvider;
   private static final Object LOCK = new Object();
   private final Plugin plugin;
   private final Map<String, MCTiming> timingCache = new HashMap(0);

   private TimingManager(Plugin plugin) {
      this.plugin = var1;
   }

   public static TimingManager of(Plugin plugin) {
      return new TimingManager(var0);
   }

   public MCTiming ofStart(String name) {
      return this.ofStart(var1, (MCTiming)null);
   }

   public MCTiming ofStart(String name, MCTiming parent) {
      return this.of(var1, var2).startTiming();
   }

   public MCTiming of(String name) {
      return this.of(var1, (MCTiming)null);
   }

   public MCTiming of(String name, MCTiming parent) {
      if (timingProvider == null) {
         synchronized(LOCK) {
            if (timingProvider == null) {
               try {
                  Class var4 = Class.forName("co.aikar.timings.Timing");
                  Method var5 = var4.getMethod("startTiming");
                  if (var5.getReturnType() != var4) {
                     timingProvider = TimingType.MINECRAFT_18;
                  } else {
                     timingProvider = TimingType.MINECRAFT;
                  }
               } catch (NoSuchMethodException | ClassNotFoundException var10) {
                  try {
                     Class.forName("org.spigotmc.CustomTimingsHandler");
                     timingProvider = TimingType.SPIGOT;
                  } catch (ClassNotFoundException var9) {
                     timingProvider = TimingType.EMPTY;
                  }
               }
            }
         }
      }

      if (timingProvider.useCache()) {
         synchronized(this.timingCache) {
            String var12 = var1.toLowerCase();
            MCTiming var3 = (MCTiming)this.timingCache.get(var12);
            if (var3 == null) {
               var3 = timingProvider.newTiming(this.plugin, var1, var2);
               this.timingCache.put(var12, var3);
            }

            return var3;
         }
      } else {
         return timingProvider.newTiming(this.plugin, var1, var2);
      }
   }
}
