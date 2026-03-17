package advancedplugins.pm2.cv.models.api.utils.scheduling;

import org.bukkit.scheduler.BukkitTask;

public class BukkitPlatformTask implements PlatformTask {
   protected BukkitTask task;

   public BukkitPlatformTask(BukkitTask var1) {
      this.task = var1;
   }

   public void cancel() {
      this.task.cancel();
   }
}
