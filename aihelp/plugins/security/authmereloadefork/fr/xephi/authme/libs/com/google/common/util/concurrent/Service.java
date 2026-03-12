package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@DoNotMock("Create an AbstractIdleService")
@ElementTypesAreNonnullByDefault
@GwtIncompatible
public interface Service {
   @CanIgnoreReturnValue
   Service startAsync();

   boolean isRunning();

   Service.State state();

   @CanIgnoreReturnValue
   Service stopAsync();

   void awaitRunning();

   default void awaitRunning(Duration timeout) throws TimeoutException {
      this.awaitRunning(Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
   }

   void awaitRunning(long var1, TimeUnit var3) throws TimeoutException;

   void awaitTerminated();

   default void awaitTerminated(Duration timeout) throws TimeoutException {
      this.awaitTerminated(Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
   }

   void awaitTerminated(long var1, TimeUnit var3) throws TimeoutException;

   Throwable failureCause();

   void addListener(Service.Listener var1, Executor var2);

   public abstract static class Listener {
      public void starting() {
      }

      public void running() {
      }

      public void stopping(Service.State from) {
      }

      public void terminated(Service.State from) {
      }

      public void failed(Service.State from, Throwable failure) {
      }
   }

   public static enum State {
      NEW,
      STARTING,
      RUNNING,
      STOPPING,
      TERMINATED,
      FAILED;

      // $FF: synthetic method
      private static Service.State[] $values() {
         return new Service.State[]{NEW, STARTING, RUNNING, STOPPING, TERMINATED, FAILED};
      }
   }
}
