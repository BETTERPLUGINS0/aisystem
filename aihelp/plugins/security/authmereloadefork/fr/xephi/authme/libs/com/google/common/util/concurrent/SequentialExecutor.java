package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.GuardedBy;
import fr.xephi.authme.libs.com.google.j2objc.annotations.RetainedWith;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
final class SequentialExecutor implements Executor {
   private static final Logger log = Logger.getLogger(SequentialExecutor.class.getName());
   private final Executor executor;
   @GuardedBy("queue")
   private final Deque<Runnable> queue = new ArrayDeque();
   @GuardedBy("queue")
   private SequentialExecutor.WorkerRunningState workerRunningState;
   @GuardedBy("queue")
   private long workerRunCount;
   @RetainedWith
   private final SequentialExecutor.QueueWorker worker;

   SequentialExecutor(Executor executor) {
      this.workerRunningState = SequentialExecutor.WorkerRunningState.IDLE;
      this.workerRunCount = 0L;
      this.worker = new SequentialExecutor.QueueWorker();
      this.executor = (Executor)Preconditions.checkNotNull(executor);
   }

   public void execute(final Runnable task) {
      Preconditions.checkNotNull(task);
      Runnable submittedTask;
      long oldRunCount;
      synchronized(this.queue) {
         if (this.workerRunningState == SequentialExecutor.WorkerRunningState.RUNNING || this.workerRunningState == SequentialExecutor.WorkerRunningState.QUEUED) {
            this.queue.add(task);
            return;
         }

         oldRunCount = this.workerRunCount;
         submittedTask = new Runnable(this) {
            public void run() {
               task.run();
            }

            public String toString() {
               return task.toString();
            }
         };
         this.queue.add(submittedTask);
         this.workerRunningState = SequentialExecutor.WorkerRunningState.QUEUING;
      }

      try {
         this.executor.execute(this.worker);
      } catch (Error | RuntimeException var12) {
         Throwable t = var12;
         synchronized(this.queue) {
            boolean removed = (this.workerRunningState == SequentialExecutor.WorkerRunningState.IDLE || this.workerRunningState == SequentialExecutor.WorkerRunningState.QUEUING) && this.queue.removeLastOccurrence(submittedTask);
            if (t instanceof RejectedExecutionException && !removed) {
               return;
            }

            throw t;
         }
      }

      boolean alreadyMarkedQueued = this.workerRunningState != SequentialExecutor.WorkerRunningState.QUEUING;
      if (!alreadyMarkedQueued) {
         synchronized(this.queue) {
            if (this.workerRunCount == oldRunCount && this.workerRunningState == SequentialExecutor.WorkerRunningState.QUEUING) {
               this.workerRunningState = SequentialExecutor.WorkerRunningState.QUEUED;
            }

         }
      }
   }

   public String toString() {
      int var1 = System.identityHashCode(this);
      String var2 = String.valueOf(this.executor);
      return (new StringBuilder(32 + String.valueOf(var2).length())).append("SequentialExecutor@").append(var1).append("{").append(var2).append("}").toString();
   }

   private final class QueueWorker implements Runnable {
      @CheckForNull
      Runnable task;

      private QueueWorker() {
      }

      public void run() {
         try {
            this.workOnQueue();
         } catch (Error var5) {
            synchronized(SequentialExecutor.this.queue) {
               SequentialExecutor.this.workerRunningState = SequentialExecutor.WorkerRunningState.IDLE;
            }

            throw var5;
         }
      }

      private void workOnQueue() {
         boolean interruptedDuringTask = false;
         boolean hasSetRunning = false;

         while(true) {
            try {
               synchronized(SequentialExecutor.this.queue) {
                  if (!hasSetRunning) {
                     if (SequentialExecutor.this.workerRunningState == SequentialExecutor.WorkerRunningState.RUNNING) {
                        return;
                     }

                     SequentialExecutor.this.workerRunCount++;
                     SequentialExecutor.this.workerRunningState = SequentialExecutor.WorkerRunningState.RUNNING;
                     hasSetRunning = true;
                  }

                  this.task = (Runnable)SequentialExecutor.this.queue.poll();
                  if (this.task == null) {
                     SequentialExecutor.this.workerRunningState = SequentialExecutor.WorkerRunningState.IDLE;
                     return;
                  }
               }

               interruptedDuringTask |= Thread.interrupted();

               try {
                  this.task.run();
               } catch (RuntimeException var15) {
                  Logger var10000 = SequentialExecutor.log;
                  Level var10001 = Level.SEVERE;
                  String var4 = String.valueOf(this.task);
                  var10000.log(var10001, (new StringBuilder(35 + String.valueOf(var4).length())).append("Exception while executing runnable ").append(var4).toString(), var15);
               } finally {
                  this.task = null;
               }
            } finally {
               if (interruptedDuringTask) {
                  Thread.currentThread().interrupt();
               }

            }
         }
      }

      public String toString() {
         Runnable currentlyRunning = this.task;
         String var2;
         if (currentlyRunning != null) {
            var2 = String.valueOf(currentlyRunning);
            return (new StringBuilder(34 + String.valueOf(var2).length())).append("SequentialExecutorWorker{running=").append(var2).append("}").toString();
         } else {
            var2 = String.valueOf(SequentialExecutor.this.workerRunningState);
            return (new StringBuilder(32 + String.valueOf(var2).length())).append("SequentialExecutorWorker{state=").append(var2).append("}").toString();
         }
      }

      // $FF: synthetic method
      QueueWorker(Object x1) {
         this();
      }
   }

   static enum WorkerRunningState {
      IDLE,
      QUEUING,
      QUEUED,
      RUNNING;

      // $FF: synthetic method
      private static SequentialExecutor.WorkerRunningState[] $values() {
         return new SequentialExecutor.WorkerRunningState[]{IDLE, QUEUING, QUEUED, RUNNING};
      }
   }
}
