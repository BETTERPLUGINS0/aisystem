package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

final class ExecutorServiceScheduler implements Scheduler, Serializable {
   static final Logger logger = Logger.getLogger(ExecutorServiceScheduler.class.getName());
   static final long serialVersionUID = 1L;
   final ScheduledExecutorService scheduledExecutorService;

   ExecutorServiceScheduler(ScheduledExecutorService scheduledExecutorService) {
      this.scheduledExecutorService = (ScheduledExecutorService)Objects.requireNonNull(scheduledExecutorService);
   }

   public Future<?> schedule(Executor executor, Runnable command, long delay, TimeUnit unit) {
      Objects.requireNonNull(executor);
      Objects.requireNonNull(command);
      Objects.requireNonNull(unit);
      return (Future)(this.scheduledExecutorService.isShutdown() ? DisabledFuture.INSTANCE : this.scheduledExecutorService.schedule(() -> {
         try {
            executor.execute(command);
         } catch (Throwable var3) {
            logger.log(Level.WARNING, "Exception thrown when submitting scheduled task", var3);
            throw var3;
         }
      }, delay, unit));
   }
}
