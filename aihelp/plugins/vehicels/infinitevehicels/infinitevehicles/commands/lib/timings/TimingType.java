package me.PM2.infinitevehicles.commands.lib.timings;

import java.lang.reflect.InvocationTargetException;
import org.bukkit.plugin.Plugin;

enum TimingType {
   SPIGOT(true) {
      MCTiming newTiming(Plugin plugin, String command, MCTiming parent) {
         return new SpigotTiming(var2);
      }
   },
   MINECRAFT {
      MCTiming newTiming(Plugin plugin, String command, MCTiming parent) {
         return new MinecraftTiming(var1, var2, var3);
      }
   },
   MINECRAFT_18 {
      MCTiming newTiming(Plugin plugin, String command, MCTiming parent) {
         try {
            return new Minecraft18Timing(var1, var2, var3);
         } catch (IllegalAccessException | InvocationTargetException var5) {
            return new EmptyTiming();
         }
      }
   },
   EMPTY;

   private final boolean useCache;

   public boolean useCache() {
      return this.useCache;
   }

   private TimingType() {
      this(false);
   }

   private TimingType(boolean useCache) {
      this.useCache = var3;
   }

   MCTiming newTiming(Plugin plugin, String command, MCTiming parent) {
      return new EmptyTiming();
   }

   // $FF: synthetic method
   TimingType(boolean var3, Object var4) {
      this(var3);
   }

   // $FF: synthetic method
   TimingType(Object var3) {
      this();
   }
}
