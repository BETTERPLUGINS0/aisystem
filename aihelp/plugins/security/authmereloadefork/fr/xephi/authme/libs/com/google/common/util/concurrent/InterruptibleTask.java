package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.j2objc.annotations.ReflectionSupport;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;
import java.util.concurrent.locks.LockSupport;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
@ReflectionSupport(ReflectionSupport.Level.FULL)
abstract class InterruptibleTask<T> extends AtomicReference<Runnable> implements Runnable {
   private static final Runnable DONE;
   private static final Runnable PARKED;
   private static final int MAX_BUSY_WAIT_SPINS = 1000;

   public final void run() {
      Thread currentThread = Thread.currentThread();
      if (this.compareAndSet((Object)null, currentThread)) {
         boolean run = !this.isDone();
         T result = null;
         Throwable error = null;

         try {
            if (run) {
               result = this.runInterruptibly();
            }
         } catch (Throwable var9) {
            error = var9;
         } finally {
            if (!this.compareAndSet(currentThread, DONE)) {
               this.waitForInterrupt(currentThread);
            }

            if (run) {
               if (error == null) {
                  this.afterRanInterruptiblySuccess(NullnessCasts.uncheckedCastNullableTToT(result));
               } else {
                  this.afterRanInterruptiblyFailure(error);
               }
            }

         }

      }
   }

   private void waitForInterrupt(Thread currentThread) {
      boolean restoreInterruptedBit = false;
      int spinCount = 0;
      Runnable state = (Runnable)this.get();

      for(InterruptibleTask.Blocker blocker = null; state instanceof InterruptibleTask.Blocker || state == PARKED; state = (Runnable)this.get()) {
         if (state instanceof InterruptibleTask.Blocker) {
            blocker = (InterruptibleTask.Blocker)state;
         }

         ++spinCount;
         if (spinCount > 1000) {
            if (state == PARKED || this.compareAndSet(state, PARKED)) {
               restoreInterruptedBit = Thread.interrupted() || restoreInterruptedBit;
               LockSupport.park(blocker);
            }
         } else {
            Thread.yield();
         }
      }

      if (restoreInterruptedBit) {
         currentThread.interrupt();
      }

   }

   abstract boolean isDone();

   @ParametricNullness
   abstract T runInterruptibly() throws Exception;

   abstract void afterRanInterruptiblySuccess(@ParametricNullness T var1);

   abstract void afterRanInterruptiblyFailure(Throwable var1);

   final void interruptTask() {
      Runnable currentRunner = (Runnable)this.get();
      if (currentRunner instanceof Thread) {
         InterruptibleTask.Blocker blocker = new InterruptibleTask.Blocker(this);
         blocker.setOwner(Thread.currentThread());
         if (this.compareAndSet(currentRunner, blocker)) {
            boolean var7 = false;

            try {
               var7 = true;
               ((Thread)currentRunner).interrupt();
               var7 = false;
            } finally {
               if (var7) {
                  Runnable var5 = (Runnable)this.getAndSet(DONE);
                  if (var5 == PARKED) {
                     LockSupport.unpark((Thread)currentRunner);
                  }

               }
            }

            Runnable prev = (Runnable)this.getAndSet(DONE);
            if (prev == PARKED) {
               LockSupport.unpark((Thread)currentRunner);
            }
         }
      }

   }

   public final String toString() {
      Runnable state = (Runnable)this.get();
      String result;
      String var3;
      if (state == DONE) {
         result = "running=[DONE]";
      } else if (state instanceof InterruptibleTask.Blocker) {
         result = "running=[INTERRUPTED]";
      } else if (state instanceof Thread) {
         var3 = ((Thread)state).getName();
         result = (new StringBuilder(21 + String.valueOf(var3).length())).append("running=[RUNNING ON ").append(var3).append("]").toString();
      } else {
         result = "running=[NOT STARTED YET]";
      }

      var3 = this.toPendingString();
      return (new StringBuilder(2 + String.valueOf(result).length() + String.valueOf(var3).length())).append(result).append(", ").append(var3).toString();
   }

   abstract String toPendingString();

   static {
      Class var0 = LockSupport.class;
      DONE = new InterruptibleTask.DoNothingRunnable();
      PARKED = new InterruptibleTask.DoNothingRunnable();
   }

   @VisibleForTesting
   static final class Blocker extends AbstractOwnableSynchronizer implements Runnable {
      private final InterruptibleTask<?> task;

      private Blocker(InterruptibleTask<?> task) {
         this.task = task;
      }

      public void run() {
      }

      private void setOwner(Thread thread) {
         super.setExclusiveOwnerThread(thread);
      }

      public String toString() {
         return this.task.toString();
      }

      // $FF: synthetic method
      Blocker(InterruptibleTask x0, Object x1) {
         this(x0);
      }
   }

   private static final class DoNothingRunnable implements Runnable {
      private DoNothingRunnable() {
      }

      public void run() {
      }

      // $FF: synthetic method
      DoNothingRunnable(Object x0) {
         this();
      }
   }
}
