package me.SuperRonanCraft.BetterRTP.lib.folialib.wrapper.task;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class WrappedBukkitTask implements WrappedTask {
   private final BukkitTask task;

   public WrappedBukkitTask(BukkitTask task) {
      this.task = task;
   }

   public void cancel() {
      this.task.cancel();
   }

   public boolean isCancelled() {
      return this.task.isCancelled();
   }

   public Plugin getOwningPlugin() {
      return this.task.getOwner();
   }
}
