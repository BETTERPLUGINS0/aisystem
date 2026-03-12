package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
public final class ExecutionSequencer {
   private final AtomicReference<ListenableFuture<Void>> ref = new AtomicReference(Futures.immediateVoidFuture());
   private ExecutionSequencer.ThreadConfinedTaskQueue latestTaskQueue = new ExecutionSequencer.ThreadConfinedTaskQueue();

   private ExecutionSequencer() {
   }

   public static ExecutionSequencer create() {
      return new ExecutionSequencer();
   }

   public <T> ListenableFuture<T> submit(final Callable<T> callable, Executor executor) {
      Preconditions.checkNotNull(callable);
      Preconditions.checkNotNull(executor);
      return this.submitAsync(new AsyncCallable<T>(this) {
         public ListenableFuture<T> call() throws Exception {
            return Futures.immediateFuture(callable.call());
         }

         public String toString() {
            return callable.toString();
         }
      }, executor);
   }

   public <T> ListenableFuture<T> submitAsync(final AsyncCallable<T> callable, Executor executor) {
      Preconditions.checkNotNull(callable);
      Preconditions.checkNotNull(executor);
      final ExecutionSequencer.TaskNonReentrantExecutor taskExecutor = new ExecutionSequencer.TaskNonReentrantExecutor(executor, this);
      AsyncCallable<T> task = new AsyncCallable<T>(this) {
         public ListenableFuture<T> call() throws Exception {
            return !taskExecutor.trySetStarted() ? Futures.immediateCancelledFuture() : callable.call();
         }

         public String toString() {
            return callable.toString();
         }
      };
      SettableFuture<Void> newFuture = SettableFuture.create();
      ListenableFuture<Void> oldFuture = (ListenableFuture)this.ref.getAndSet(newFuture);
      TrustedListenableFutureTask<T> taskFuture = TrustedListenableFutureTask.create(task);
      oldFuture.addListener(taskFuture, taskExecutor);
      ListenableFuture<T> outputFuture = Futures.nonCancellationPropagating(taskFuture);
      Runnable listener = () -> {
         if (taskFuture.isDone()) {
            newFuture.setFuture(oldFuture);
         } else if (outputFuture.isCancelled() && taskExecutor.trySetCancelled()) {
            taskFuture.cancel(false);
         }

      };
      outputFuture.addListener(listener, MoreExecutors.directExecutor());
      taskFuture.addListener(listener, MoreExecutors.directExecutor());
      return outputFuture;
   }

   private static final class TaskNonReentrantExecutor extends AtomicReference<ExecutionSequencer.RunningState> implements Executor, Runnable {
      @CheckForNull
      ExecutionSequencer sequencer;
      @CheckForNull
      Executor delegate;
      @CheckForNull
      Runnable task;
      @CheckForNull
      Thread submitting;

      private TaskNonReentrantExecutor(Executor delegate, ExecutionSequencer sequencer) {
         super(ExecutionSequencer.RunningState.NOT_RUN);
         this.delegate = delegate;
         this.sequencer = sequencer;
      }

      public void execute(Runnable task) {
         if (this.get() == ExecutionSequencer.RunningState.CANCELLED) {
            this.delegate = null;
            this.sequencer = null;
         } else {
            this.submitting = Thread.currentThread();

            try {
               ExecutionSequencer.ThreadConfinedTaskQueue submittingTaskQueue = ((ExecutionSequencer)Objects.requireNonNull(this.sequencer)).latestTaskQueue;
               if (submittingTaskQueue.thread == this.submitting) {
                  this.sequencer = null;
                  Preconditions.checkState(submittingTaskQueue.nextTask == null);
                  submittingTaskQueue.nextTask = task;
                  submittingTaskQueue.nextExecutor = (Executor)Objects.requireNonNull(this.delegate);
                  this.delegate = null;
               } else {
                  Executor localDelegate = (Executor)Objects.requireNonNull(this.delegate);
                  this.delegate = null;
                  this.task = task;
                  localDelegate.execute(this);
               }
            } finally {
               this.submitting = null;
            }

         }
      }

      public void run() {
         Thread currentThread = Thread.currentThread();
         if (currentThread != this.submitting) {
            Runnable localTask = (Runnable)Objects.requireNonNull(this.task);
            this.task = null;
            localTask.run();
         } else {
            ExecutionSequencer.ThreadConfinedTaskQueue executingTaskQueue = new ExecutionSequencer.ThreadConfinedTaskQueue();
            executingTaskQueue.thread = currentThread;
            ((ExecutionSequencer)Objects.requireNonNull(this.sequencer)).latestTaskQueue = executingTaskQueue;
            this.sequencer = null;

            try {
               Runnable localTask = (Runnable)Objects.requireNonNull(this.task);
               this.task = null;
               localTask.run();

               Runnable queuedTask;
               Executor queuedExecutor;
               while((queuedTask = executingTaskQueue.nextTask) != null && (queuedExecutor = executingTaskQueue.nextExecutor) != null) {
                  executingTaskQueue.nextTask = null;
                  executingTaskQueue.nextExecutor = null;
                  queuedExecutor.execute(queuedTask);
               }
            } finally {
               executingTaskQueue.thread = null;
            }

         }
      }

      private boolean trySetStarted() {
         return this.compareAndSet(ExecutionSequencer.RunningState.NOT_RUN, ExecutionSequencer.RunningState.STARTED);
      }

      private boolean trySetCancelled() {
         return this.compareAndSet(ExecutionSequencer.RunningState.NOT_RUN, ExecutionSequencer.RunningState.CANCELLED);
      }

      // $FF: synthetic method
      TaskNonReentrantExecutor(Executor x0, ExecutionSequencer x1, Object x2) {
         this(x0, x1);
      }
   }

   static enum RunningState {
      NOT_RUN,
      CANCELLED,
      STARTED;

      // $FF: synthetic method
      private static ExecutionSequencer.RunningState[] $values() {
         return new ExecutionSequencer.RunningState[]{NOT_RUN, CANCELLED, STARTED};
      }
   }

   private static final class ThreadConfinedTaskQueue {
      @CheckForNull
      Thread thread;
      @CheckForNull
      Runnable nextTask;
      @CheckForNull
      Executor nextExecutor;

      private ThreadConfinedTaskQueue() {
      }

      // $FF: synthetic method
      ThreadConfinedTaskQueue(Object x0) {
         this();
      }
   }
}
