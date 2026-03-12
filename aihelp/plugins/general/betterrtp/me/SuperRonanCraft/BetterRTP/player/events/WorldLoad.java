package me.SuperRonanCraft.BetterRTP.player.events;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.lib.folialib.wrapper.task.WrappedTask;
import me.SuperRonanCraft.BetterRTP.versions.AsyncHandler;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoad {
   WrappedTask loader;

   void load(WorldLoadEvent e) {
      if (this.loader != null) {
         this.loader.cancel();
      }

      this.loader = AsyncHandler.syncLater(() -> {
         BetterRTP.debug("New world `" + e.getWorld().getName() + "` detected! Reloaded Databases!");
         BetterRTP.getInstance().getDatabaseHandler().load();
      }, 100L);
   }
}
