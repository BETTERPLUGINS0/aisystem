package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class SingletonTask {
   private final Plugin plugin;
   private final Object executionLock = new Object();
   private SingletonTask.State state;
   @Nullable
   private BukkitTask asyncTask;
   @Nullable
   private Runnable internalCallback;
   @Nullable
   private Runnable internalSyncCallback;
   private boolean runAgain;
   private boolean runAgainSync;
   private boolean asyncExecution;
   private long startTimeNanos;
   private long preparationEndTimeNanos;
   private long preparationDurationMillis;
   private long lockAcquireDurationMillis;
   private long executionDelayMillis;
   private long executionDurationMillis;
   private long totalDurationMillis;

   public SingletonTask(Plugin plugin) {
      this.state = SingletonTask.State.NOT_RUNNING;
      this.asyncTask = null;
      this.internalCallback = null;
      this.internalSyncCallback = null;
      this.runAgain = false;
      this.runAgainSync = false;
      Validate.notNull(plugin, (String)"plugin is null");
      this.plugin = plugin;
   }

   public final boolean isRunning() {
      assert Bukkit.isPrimaryThread();

      return this.state != SingletonTask.State.NOT_RUNNING;
   }

   public final boolean isPostProcessing() {
      assert Bukkit.isPrimaryThread();

      return this.state == SingletonTask.State.SYNC_CALLBACK;
   }

   public final boolean isExecutionPending() {
      return this.runAgain;
   }

   private boolean isWithinSyncExecution() {
      assert Bukkit.isPrimaryThread();

      return this.state == SingletonTask.State.PREPARING || this.asyncTask == null && this.state == SingletonTask.State.EXECUTING || this.state == SingletonTask.State.SYNC_CALLBACK;
   }

   private void validateMainThreadAndNotWithinExecution() {
      Validate.State.isTrue(Bukkit.isPrimaryThread(), "This operation has to be called from the main thread!");
      if (this.isWithinSyncExecution()) {
         throw Validate.State.error("This operation is not allowed to be called from within the task's execution!");
      }
   }

   public final void shutdown() {
      this.validateMainThreadAndNotWithinExecution();
      this.awaitExecutions();
   }

   public final void run() {
      if (this.plugin.isEnabled()) {
         this.runTask(true);
      } else {
         this.runTask(false);
      }

   }

   public final void runImmediately() {
      this.runTask(false);
   }

   public final void awaitExecutions() {
      this.validateMainThreadAndNotWithinExecution();
      if (this.asyncTask == null) {
         assert !this.runAgain;

      } else {
         synchronized(this.executionLock) {
            if (this.state == SingletonTask.State.PENDING) {
               assert this.asyncTask != null;

               this.asyncTask.cancel();
            } else {
               assert this.state == SingletonTask.State.EXECUTING;
            }
         }

         boolean hasExecuted = this.state == SingletonTask.State.EXECUTING;
         this.runAgainSync = true;
         if (!hasExecuted) {
            this.asyncTask = null;
            this.executeTask((BukkitTask)null);

            assert this.internalSyncCallback == null;
         } else {
            ((Runnable)Unsafe.assertNonNull(this.internalSyncCallback)).run();
         }

         this.runAgainSync = false;
      }
   }

   private void runTask(boolean async) {
      this.validateMainThreadAndNotWithinExecution();
      Validate.State.isTrue(!async || this.plugin.isEnabled(), "Cannot execute asynchronously during or after plugin disable!");
      if (this.asyncTask != null) {
         this.runAgain = true;
         if (!async) {
            this.awaitExecutions();
         }
      } else {
         assert this.state == SingletonTask.State.NOT_RUNNING;

         assert this.asyncTask == null;

         assert this.internalCallback == null;

         assert this.internalSyncCallback == null;

         assert !this.runAgain;

         this.asyncExecution = async;
         this.startTimeNanos = System.nanoTime();
         this.state = SingletonTask.State.PREPARING;
         this.prepare();
         this.internalCallback = () -> {
            assert this.state == SingletonTask.State.EXECUTING;

            SchedulerUtils.runOnMainThreadOrOmit(this.plugin, (Runnable)Unsafe.assertNonNull(this.internalSyncCallback));
         };
         this.internalSyncCallback = this.createInternalSyncCallbackTask();
         this.preparationEndTimeNanos = System.nanoTime();
         this.preparationDurationMillis = TimeUnit.NANOSECONDS.toMillis(this.preparationEndTimeNanos - this.startTimeNanos);
         this.state = SingletonTask.State.PENDING;
         if (async) {
            this.asyncTask = this.createInternalAsyncTask().runTaskAsynchronously();
         } else {
            this.executeTask((BukkitTask)null);
         }

      }
   }

   protected abstract SingletonTask.InternalAsyncTask createInternalAsyncTask();

   protected abstract SingletonTask.InternalSyncCallbackTask createInternalSyncCallbackTask();

   private void executeTask(@Nullable BukkitTask asyncTask) {
      if (asyncTask != null) {
         long lockAcquireStartTimeNanos = System.nanoTime();
         synchronized(this.executionLock) {
            long localLockAcquireDurationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - lockAcquireStartTimeNanos);
            if (asyncTask.isCancelled()) {
               return;
            }

            this.lockAcquireDurationMillis = localLockAcquireDurationMillis;
            this.doExecuteTask();
         }
      } else {
         this.lockAcquireDurationMillis = 0L;
         this.doExecuteTask();
      }

   }

   private void doExecuteTask() {
      this.state = SingletonTask.State.EXECUTING;
      long executionStartTimeNanos = System.nanoTime();
      this.executionDelayMillis = TimeUnit.NANOSECONDS.toMillis(executionStartTimeNanos - this.preparationEndTimeNanos);
      this.execute();
      ((Runnable)Unsafe.assertNonNull(this.internalCallback)).run();
      long executionEndTimeNanos = System.nanoTime();
      this.executionDurationMillis = TimeUnit.NANOSECONDS.toMillis(executionEndTimeNanos - executionStartTimeNanos);
      this.totalDurationMillis = TimeUnit.NANOSECONDS.toMillis(executionEndTimeNanos - this.startTimeNanos);
   }

   public final boolean isAsyncExecution() {
      return this.asyncExecution;
   }

   public final long getPreparationDuration() {
      return this.preparationDurationMillis;
   }

   public final long getLockAcquireDuration() {
      return this.lockAcquireDurationMillis;
   }

   public final long getExecutionDelay() {
      return this.executionDelayMillis;
   }

   public final long getExecutionDuration() {
      return this.executionDurationMillis;
   }

   public final long getTotalDuration() {
      return this.totalDurationMillis;
   }

   public final String getExecutionTimingString() {
      StringBuilder sb = new StringBuilder();
      sb.append(this.totalDurationMillis).append(" ms");
      String details = this.getExecutionTimingDetailString();
      if (!details.isEmpty()) {
         sb.append(" (").append(details).append(")");
      }

      return sb.toString();
   }

   private String getExecutionTimingDetailString() {
      StringBuilder sb = new StringBuilder();
      boolean firstEntry = true;
      if (this.preparationDurationMillis > 0L) {
         firstEntry = false;
         sb.append("Preparation: ").append(this.preparationDurationMillis).append(" ms");
      }

      if (this.executionDelayMillis > 0L) {
         if (!firstEntry) {
            sb.append(", ");
         }

         firstEntry = false;
         if (this.asyncExecution) {
            sb.append("Async execution delay: ");
         } else {
            sb.append("Sync execution delay: ");
         }

         sb.append(this.executionDelayMillis).append(" ms");
         if (this.lockAcquireDurationMillis > 0L) {
            sb.append(" (Lock delay: ").append(this.lockAcquireDurationMillis).append(" ms)");
         }
      }

      if (this.executionDurationMillis > 0L) {
         if (!firstEntry) {
            sb.append(", ");
         }

         firstEntry = false;
         if (this.asyncExecution) {
            sb.append("Async execution: ");
         } else {
            sb.append("Sync execution: ");
         }

         sb.append(this.executionDurationMillis).append(" ms");
      }

      return sb.toString();
   }

   protected abstract void prepare();

   protected abstract void execute();

   protected abstract void syncCallback();

   private static enum State {
      NOT_RUNNING,
      PREPARING,
      PENDING,
      EXECUTING,
      SYNC_CALLBACK;

      // $FF: synthetic method
      private static SingletonTask.State[] $values() {
         return new SingletonTask.State[]{NOT_RUNNING, PREPARING, PENDING, EXECUTING, SYNC_CALLBACK};
      }
   }

   public class InternalSyncCallbackTask implements Runnable {
      public final void run() {
         if (SingletonTask.this.internalSyncCallback == this) {
            SingletonTask.this.internalSyncCallback = null;
            SingletonTask.this.internalCallback = null;
            SingletonTask.this.asyncTask = null;

            assert SingletonTask.this.state == SingletonTask.State.EXECUTING;

            SingletonTask.this.state = SingletonTask.State.SYNC_CALLBACK;
            SingletonTask.this.syncCallback();
            SingletonTask.this.state = SingletonTask.State.NOT_RUNNING;
            if (SingletonTask.this.runAgain) {
               SingletonTask.this.runAgain = false;
               if (SingletonTask.this.runAgainSync) {
                  SingletonTask.this.runImmediately();
               } else {
                  SingletonTask.this.run();
               }
            }

            assert !SingletonTask.this.runAgain;

         }
      }
   }

   public abstract class InternalAsyncTask implements Runnable {
      @Nullable
      private BukkitTask task;

      protected InternalAsyncTask() {
      }

      private BukkitTask runTaskAsynchronously() {
         this.task = Bukkit.getScheduler().runTaskAsynchronously(SingletonTask.this.plugin, this);
         return this.task;
      }

      public final void run() {
         SingletonTask.this.executeTask(this.task);
      }
   }
}
