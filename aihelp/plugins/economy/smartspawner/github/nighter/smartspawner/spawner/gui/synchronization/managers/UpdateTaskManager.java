package github.nighter.smartspawner.spawner.gui.synchronization.managers;

import github.nighter.smartspawner.Scheduler;

public class UpdateTaskManager {
   private static final long UPDATE_INTERVAL_TICKS = 20L;
   private static final long INITIAL_DELAY_TICKS = 20L;
   private Scheduler.Task updateTask;
   private volatile boolean isTaskRunning;

   public synchronized void startTask(Runnable updateRunnable) {
      if (!this.isTaskRunning) {
         this.updateTask = Scheduler.runTaskTimer(updateRunnable, 20L, 20L);
         this.isTaskRunning = true;
      }
   }

   public synchronized void stopTask() {
      if (this.isTaskRunning) {
         if (this.updateTask != null) {
            this.updateTask.cancel();
            this.updateTask = null;
         }

         this.isTaskRunning = false;
      }
   }

   public boolean isRunning() {
      return this.isTaskRunning;
   }
}
