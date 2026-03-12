package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public interface ListeningScheduledExecutorService extends ScheduledExecutorService, ListeningExecutorService {
   ListenableScheduledFuture<?> schedule(Runnable var1, long var2, TimeUnit var4);

   default ListenableScheduledFuture<?> schedule(Runnable command, Duration delay) {
      return this.schedule(command, Internal.toNanosSaturated(delay), TimeUnit.NANOSECONDS);
   }

   <V> ListenableScheduledFuture<V> schedule(Callable<V> var1, long var2, TimeUnit var4);

   default <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, Duration delay) {
      return this.schedule(callable, Internal.toNanosSaturated(delay), TimeUnit.NANOSECONDS);
   }

   ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable var1, long var2, long var4, TimeUnit var6);

   default ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, Duration initialDelay, Duration period) {
      return this.scheduleAtFixedRate(command, Internal.toNanosSaturated(initialDelay), Internal.toNanosSaturated(period), TimeUnit.NANOSECONDS);
   }

   ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6);

   default ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, Duration initialDelay, Duration delay) {
      return this.scheduleWithFixedDelay(command, Internal.toNanosSaturated(initialDelay), Internal.toNanosSaturated(delay), TimeUnit.NANOSECONDS);
   }
}
