package me.PM2.infinitevehicles.commands.lib.timings;

import co.aikar.timings.Timing;
import co.aikar.timings.Timings;
import org.bukkit.plugin.Plugin;

class MinecraftTiming extends MCTiming {
   private final Timing timing;

   MinecraftTiming(Plugin plugin, String name, MCTiming parent) {
      this.timing = Timings.of(var1, var2, var3 instanceof MinecraftTiming ? ((MinecraftTiming)var3).timing : null);
   }

   public MCTiming startTiming() {
      this.timing.startTimingIfSync();
      return this;
   }

   public void stopTiming() {
      this.timing.stopTimingIfSync();
   }
}
