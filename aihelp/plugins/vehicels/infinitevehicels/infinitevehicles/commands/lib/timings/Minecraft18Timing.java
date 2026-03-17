package me.PM2.infinitevehicles.commands.lib.timings;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

class Minecraft18Timing extends MCTiming {
   private final Object timing;
   private static Method startTiming;
   private static Method stopTiming;
   private static Method of;

   Minecraft18Timing(Plugin plugin, String name, MCTiming parent) {
      this.timing = of.invoke((Object)null, var1, var2, var3 instanceof Minecraft18Timing ? ((Minecraft18Timing)var3).timing : null);
   }

   public MCTiming startTiming() {
      try {
         if (startTiming != null) {
            startTiming.invoke(this.timing);
         }
      } catch (InvocationTargetException | IllegalAccessException var2) {
      }

      return this;
   }

   public void stopTiming() {
      try {
         if (stopTiming != null) {
            stopTiming.invoke(this.timing);
         }
      } catch (InvocationTargetException | IllegalAccessException var2) {
      }

   }

   static {
      try {
         Class var0 = Class.forName("co.aikar.timings.Timing");
         Class var1 = Class.forName("co.aikar.timings.Timings");
         startTiming = var0.getDeclaredMethod("startTimingIfSync");
         stopTiming = var0.getDeclaredMethod("stopTimingIfSync");
         of = var1.getDeclaredMethod("of", Plugin.class, String.class, var0);
      } catch (NoSuchMethodException | ClassNotFoundException var2) {
         var2.printStackTrace();
         Bukkit.getLogger().severe("Timings18 failed to initialize correctly. Stuff's going to be broken.");
      }

   }
}
