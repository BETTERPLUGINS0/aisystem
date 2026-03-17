package advancedplugins.pm2.cv.models.api.utils.scheduling;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

public class FoliaPlatformTask implements PlatformTask {
   protected ScheduledTask task;

   public FoliaPlatformTask(ScheduledTask var1) {
      this.task = var1;
   }

   public void cancel() {
      this.task.cancel();
   }
}
