package com.nisovin.shopkeepers.util.taskqueue;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayDeque;
import java.util.Queue;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class TaskQueue<T> implements TaskQueueStatistics {
   private final Plugin plugin;
   private final int taskPeriodTicks;
   private final int workUnitsPerExecution;
   private final Queue<T> pending = new ArrayDeque();
   private int maxPending = 0;
   @Nullable
   private BukkitTask task = null;

   public TaskQueue(Plugin plugin, int taskPeriodTicks, int workUnitsPerExecution) {
      Validate.notNull(plugin, (String)"plugin is null");
      Validate.isTrue(taskPeriodTicks > 0, "taskPeriodTicks has to be positive");
      Validate.isTrue(workUnitsPerExecution > 0, "workUnitsPerExecution has to be positive");
      this.plugin = plugin;
      this.taskPeriodTicks = taskPeriodTicks;
      this.workUnitsPerExecution = workUnitsPerExecution;
   }

   public void start() {
      this.startTask();
   }

   public void shutdown() {
      this.pending.forEach(this::onRemoval);
      this.pending.clear();
      this.stopTask();
      this.maxPending = 0;
   }

   public void add(@NonNull T workUnit) {
      assert workUnit != null;

      this.pending.add(workUnit);
      int size = this.pending.size();
      if (size > this.maxPending) {
         this.maxPending = size;
      }

      this.onAdded(workUnit);
   }

   protected void onAdded(@NonNull T workUnit) {
   }

   public void remove(@NonNull T workUnit) {
      assert workUnit != null;

      if (this.pending.remove(workUnit)) {
         this.onRemoval(workUnit);
      }

   }

   protected void onRemoval(@NonNull T workUnit) {
   }

   public int getPendingCount() {
      return this.pending.size();
   }

   public int getMaxPendingCount() {
      return this.maxPending;
   }

   private void startTask() {
      if (this.task == null) {
         this.task = Bukkit.getScheduler().runTaskTimer(this.plugin, this.createTask(), 1L, (long)this.taskPeriodTicks);
      }
   }

   private void stopTask() {
      if (this.task != null) {
         this.task.cancel();
         this.task = null;
      }

   }

   protected Runnable createTask() {
      return this::execute;
   }

   private void execute() {
      Queue<T> queue = this.pending;
      if (!queue.isEmpty()) {
         int localWorkUnitsPerExecution = this.workUnitsPerExecution;

         for(int i = 0; i < localWorkUnitsPerExecution; ++i) {
            T workUnit = queue.poll();
            if (workUnit == null) {
               return;
            }

            this.process(workUnit);
         }

      }
   }

   protected abstract void process(@NonNull T var1);
}
