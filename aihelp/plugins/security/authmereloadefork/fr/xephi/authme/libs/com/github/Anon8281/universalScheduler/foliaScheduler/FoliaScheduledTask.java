package fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.foliaScheduler;

import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask.ExecutionState;
import org.bukkit.plugin.Plugin;

public class FoliaScheduledTask implements MyScheduledTask {
   private final ScheduledTask task;

   public FoliaScheduledTask(ScheduledTask task) {
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

   public boolean isCurrentlyRunning() {
      ExecutionState state = this.task.getExecutionState();
      return state == ExecutionState.RUNNING || state == ExecutionState.CANCELLED_RUNNING;
   }

   public boolean isRepeatingTask() {
      return this.task.isRepeatingTask();
   }
}
