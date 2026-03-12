package fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.bukkitScheduler;

import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class BukkitScheduledTask implements MyScheduledTask {
   BukkitTask task;
   boolean isRepeating;

   public BukkitScheduledTask(BukkitTask task) {
      this.task = task;
      this.isRepeating = false;
   }

   public BukkitScheduledTask(BukkitTask task, boolean isRepeating) {
      this.task = task;
      this.isRepeating = isRepeating;
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

   public boolean isCurrentlyRunning() {
      return Bukkit.getServer().getScheduler().isCurrentlyRunning(this.task.getTaskId());
   }

   public boolean isRepeatingTask() {
      return this.isRepeating;
   }
}
