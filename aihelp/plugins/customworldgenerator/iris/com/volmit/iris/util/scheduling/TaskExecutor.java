package com.volmit.iris.util.scheduling;

import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.function.NastyRunnable;
import com.volmit.iris.util.math.M;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import lombok.Generated;

public class TaskExecutor {
   private final ExecutorService service;
   private int xc = 1;

   public TaskExecutor(int threadLimit, int priority, String name) {
      if (var1 == 1) {
         this.service = Executors.newSingleThreadExecutor((var2x) -> {
            Thread var3x = new Thread(var2x);
            var3x.setName(var3);
            var3x.setPriority(var2);
            return var3x;
         });
      } else if (var1 > 1) {
         ForkJoinWorkerThreadFactory var4 = (var3x) -> {
            ForkJoinWorkerThread var4 = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(var3x);
            var4.setName(var3 + " " + this.xc++);
            var4.setPriority(var2);
            return var4;
         };
         this.service = new ForkJoinPool(var1, var4, (UncaughtExceptionHandler)null, false);
      } else {
         this.service = Executors.newCachedThreadPool((var3x) -> {
            Thread var4 = new Thread(var3x);
            var4.setName(var3 + " " + this.xc++);
            var4.setPriority(var2);
            return var4;
         });
      }

   }

   public TaskExecutor.TaskGroup startWork() {
      return new TaskExecutor.TaskGroup(this);
   }

   public void close() {
      J.a(() -> {
         J.sleep(10000L);
         this.service.shutdown();
      });
   }

   public void closeNow() {
      this.service.shutdown();
   }

   public static class TaskGroup {
      private final KList<TaskExecutor.AssignedTask> tasks = new KList();
      private final TaskExecutor e;

      public TaskGroup(TaskExecutor e) {
         this.e = var1;
      }

      public TaskExecutor.TaskGroup queue(NastyRunnable... r) {
         NastyRunnable[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            NastyRunnable var5 = var2[var4];
            this.tasks.add((Object)(new TaskExecutor.AssignedTask(var5)));
         }

         return this;
      }

      public TaskExecutor.TaskGroup queue(KList<NastyRunnable> r) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            NastyRunnable var3 = (NastyRunnable)var2.next();
            this.tasks.add((Object)(new TaskExecutor.AssignedTask(var3)));
         }

         return this;
      }

      public TaskExecutor.TaskResult execute() {
         double var1 = 0.0D;
         int var3 = 0;
         int var4 = 0;
         int var5 = 0;
         this.tasks.forEach((var1x) -> {
            var1x.go(this.e);
         });
         long var6 = M.ns();

         label40:
         while(true) {
            try {
               Thread.sleep(0L);
            } catch (InterruptedException var10) {
            }

            Iterator var8 = this.tasks.iterator();

            TaskExecutor.AssignedTask var9;
            while(var8.hasNext()) {
               var9 = (TaskExecutor.AssignedTask)var8.next();
               if (var9.state.equals(TaskExecutor.TaskState.QUEUED) || var9.state.equals(TaskExecutor.TaskState.RUNNING)) {
                  continue label40;
               }
            }

            var1 = (double)(M.ns() - var6) / 1000000.0D;

            for(var8 = this.tasks.iterator(); var8.hasNext(); ++var3) {
               var9 = (TaskExecutor.AssignedTask)var8.next();
               if (var9.state.equals(TaskExecutor.TaskState.COMPLETED)) {
                  ++var5;
               } else {
                  ++var4;
               }
            }

            return new TaskExecutor.TaskResult(var1, var3, var4, var5);
         }
      }
   }

   public static class AssignedTask {
      private final NastyRunnable task;
      private TaskExecutor.TaskState state;

      public AssignedTask(NastyRunnable task) {
         this.task = var1;
         this.state = TaskExecutor.TaskState.QUEUED;
      }

      public void go(TaskExecutor e) {
         var1.service.execute(() -> {
            this.state = TaskExecutor.TaskState.RUNNING;

            try {
               this.task.run();
               this.state = TaskExecutor.TaskState.COMPLETED;
            } catch (Throwable var2) {
               Iris.reportError(var2);
               var2.printStackTrace();
               Iris.reportError(var2);
               this.state = TaskExecutor.TaskState.FAILED;
            }

         });
      }

      @Generated
      public NastyRunnable getTask() {
         return this.task;
      }

      @Generated
      public TaskExecutor.TaskState getState() {
         return this.state;
      }

      @Generated
      public void setState(final TaskExecutor.TaskState state) {
         this.state = var1;
      }
   }

   public static class TaskResult {
      public final double timeElapsed;
      public final int tasksExecuted;
      public final int tasksFailed;
      public final int tasksCompleted;

      public TaskResult(double timeElapsed, int tasksExecuted, int tasksFailed, int tasksCompleted) {
         this.timeElapsed = var1;
         this.tasksExecuted = var3;
         this.tasksFailed = var4;
         this.tasksCompleted = var5;
      }

      @Generated
      public String toString() {
         return "TaskExecutor.TaskResult(timeElapsed=" + this.timeElapsed + ", tasksExecuted=" + this.tasksExecuted + ", tasksFailed=" + this.tasksFailed + ", tasksCompleted=" + this.tasksCompleted + ")";
      }
   }

   public static enum TaskState {
      QUEUED,
      RUNNING,
      COMPLETED,
      FAILED;

      // $FF: synthetic method
      private static TaskExecutor.TaskState[] $values() {
         return new TaskExecutor.TaskState[]{QUEUED, RUNNING, COMPLETED, FAILED};
      }
   }
}
