package me.SuperRonanCraft.BetterRTP.lib.folialib.wrapper.task;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;

public class WrappedFoliaTask implements WrappedTask {
   private final ScheduledTask task;

   public WrappedFoliaTask(ScheduledTask task) {
      this.task = task;
   }

   public void cancel() {
      this.task.cancel();
   }

   public boolean isCancelled() {
      return this.task.isCancelled();
   }

   public Plugin getOwningPlugin() {
      return this.task.getOwningPlugin();
   }
}
