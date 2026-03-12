package ac.grim.grimac.platform.bukkit.initables;

import ac.grim.grimac.manager.init.start.StartableInitable;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bstats.bukkit.Metrics;

public class BukkitBStats implements StartableInitable {
   public void start() {
      try {
         new Metrics(GrimACBukkitLoaderPlugin.LOADER, 12820);
      } catch (Exception var2) {
      }

   }
}
