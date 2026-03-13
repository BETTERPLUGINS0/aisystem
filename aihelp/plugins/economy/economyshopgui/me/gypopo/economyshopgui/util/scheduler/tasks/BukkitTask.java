package me.gypopo.economyshopgui.util.scheduler.tasks;

import me.gypopo.economyshopgui.util.scheduler.ScheduledTask;

public class BukkitTask implements ScheduledTask {
   private final org.bukkit.scheduler.BukkitTask task;

   public BukkitTask(org.bukkit.scheduler.BukkitTask task) {
      this.task = task;
   }

   public void cancel() {
      this.task.cancel();
   }
}
