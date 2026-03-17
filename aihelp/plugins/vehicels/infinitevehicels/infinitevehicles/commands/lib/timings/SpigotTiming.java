package me.PM2.infinitevehicles.commands.lib.timings;

import org.bukkit.Bukkit;
import org.spigotmc.CustomTimingsHandler;

class SpigotTiming extends MCTiming {
   private final CustomTimingsHandler timing;

   SpigotTiming(String name) {
      this.timing = new CustomTimingsHandler(var1);
   }

   public MCTiming startTiming() {
      if (Bukkit.isPrimaryThread()) {
         this.timing.startTiming();
      }

      return this;
   }

   public void stopTiming() {
      if (Bukkit.isPrimaryThread()) {
         this.timing.stopTiming();
      }

   }
}
