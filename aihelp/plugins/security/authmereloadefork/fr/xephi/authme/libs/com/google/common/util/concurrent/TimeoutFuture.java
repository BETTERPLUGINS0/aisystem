package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
final class TimeoutFuture<V> extends FluentFuture.TrustedFuture<V> {
   @CheckForNull
   private ListenableFuture<V> delegateRef;
   @CheckForNull
   private ScheduledFuture<?> timer;

   static <V> ListenableFuture<V> create(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor) {
      TimeoutFuture<V> result = new TimeoutFuture(delegate);
      TimeoutFuture.Fire<V> fire = new TimeoutFuture.Fire(result);
      result.timer = scheduledExecutor.schedule(fire, time, unit);
      delegate.addListener(fire, MoreExecutors.directExecutor());
      return result;
   }

   private TimeoutFuture(ListenableFuture<V> delegate) {
      this.delegateRef = (ListenableFuture)Preconditions.checkNotNull(delegate);
   }

   @CheckForNull
   protected String pendingToString() {
      ListenableFuture<? extends V> localInputFuture = this.delegateRef;
      ScheduledFuture<?> localTimer = this.timer;
      if (localInputFuture != null) {
         String var4 = String.valueOf(localInputFuture);
         String message = (new StringBuilder(14 + String.valueOf(var4).length())).append("inputFuture=[").append(var4).append("]").toString();
         if (localTimer != null) {
            long delay = localTimer.getDelay(TimeUnit.MILLISECONDS);
            if (delay > 0L) {
               String var6 = String.valueOf(message);
               message = (new StringBuilder(43 + String.valueOf(var6).length())).append(var6).append(", remaining delay=[").append(delay).append(" ms]").toString();
            }
         }

         return message;
      } else {
         return null;
      }
   }

   protected void afterDone() {
      this.maybePropagateCancellationTo(this.delegateRef);
      Future<?> localTimer = this.timer;
      if (localTimer != null) {
         localTimer.cancel(false);
      }

      this.delegateRef = null;
      this.timer = null;
   }

   private static final class TimeoutFutureException extends TimeoutException {
      private TimeoutFutureException(String message) {
         super(message);
      }

      public synchronized Throwable fillInStackTrace() {
         this.setStackTrace(new StackTraceElement[0]);
         return this;
      }

      // $FF: synthetic method
      TimeoutFutureException(String x0, Object x1) {
         this(x0);
      }
   }

   private static final class Fire<V> implements Runnable {
      @CheckForNull
      TimeoutFuture<V> timeoutFutureRef;

      Fire(TimeoutFuture<V> timeoutFuture) {
         this.timeoutFutureRef = timeoutFuture;
      }

      public void run() {
         TimeoutFuture<V> timeoutFuture = this.timeoutFutureRef;
         if (timeoutFuture != null) {
            ListenableFuture<V> delegate = timeoutFuture.delegateRef;
            if (delegate != null) {
               this.timeoutFutureRef = null;
               if (delegate.isDone()) {
                  timeoutFuture.setFuture(delegate);
               } else {
                  try {
                     ScheduledFuture<?> timer = timeoutFuture.timer;
                     timeoutFuture.timer = null;
                     String message = "Timed out";

                     try {
                        if (timer != null) {
                           long overDelayMs = Math.abs(timer.getDelay(TimeUnit.MILLISECONDS));
                           if (overDelayMs > 10L) {
                              String var7 = String.valueOf(message);
                              message = (new StringBuilder(66 + String.valueOf(var7).length())).append(var7).append(" (timeout delayed by ").append(overDelayMs).append(" ms after scheduled time)").toString();
                           }
                        }

                        String var16 = String.valueOf(message);
                        String var6 = String.valueOf(delegate);
                        message = (new StringBuilder(2 + String.valueOf(var16).length() + String.valueOf(var6).length())).append(var16).append(": ").append(var6).toString();
                     } finally {
                        timeoutFuture.setException(new TimeoutFuture.TimeoutFutureException(message));
                     }
                  } finally {
                     delegate.cancel(true);
                  }
               }

            }
         }
      }
   }
}
