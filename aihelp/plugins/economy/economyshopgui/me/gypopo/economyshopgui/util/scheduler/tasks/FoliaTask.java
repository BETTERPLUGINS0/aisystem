package me.gypopo.economyshopgui.util.scheduler.tasks;

import me.gypopo.economyshopgui.util.scheduler.ScheduledTask;

public class FoliaTask implements ScheduledTask {
   private final io.papermc.paper.threadedregions.scheduler.ScheduledTask task;

   public FoliaTask(io.papermc.paper.threadedregions.scheduler.ScheduledTask task) {
      this.task = task;
   }

   public void cancel() {
      this.task.cancel();
   }
}
