package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Supplier;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.GuardedBy;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public abstract class AbstractScheduledService implements Service {
   private static final Logger logger = Logger.getLogger(AbstractScheduledService.class.getName());
   private final AbstractService delegate = new AbstractScheduledService.ServiceDelegate();

   protected AbstractScheduledService() {
   }

   protected abstract void runOneIteration() throws Exception;

   protected void startUp() throws Exception {
   }

   protected void shutDown() throws Exception {
   }

   protected abstract AbstractScheduledService.Scheduler scheduler();

   protected ScheduledExecutorService executor() {
      class ThreadFactoryImpl implements ThreadFactory {
         public Thread newThread(Runnable runnable) {
            return MoreExecutors.newThread(AbstractScheduledService.this.serviceName(), runnable);
         }
      }

      final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl());
      this.addListener(new Service.Listener(this) {
         public void terminated(Service.State from) {
            executor.shutdown();
         }

         public void failed(Service.State from, Throwable failure) {
            executor.shutdown();
         }
      }, MoreExecutors.directExecutor());
      return executor;
   }

   protected String serviceName() {
      return this.getClass().getSimpleName();
   }

   public String toString() {
      String var1 = this.serviceName();
      String var2 = String.valueOf(this.state());
      return (new StringBuilder(3 + String.valueOf(var1).length() + String.valueOf(var2).length())).append(var1).append(" [").append(var2).append("]").toString();
   }

   public final boolean isRunning() {
      return this.delegate.isRunning();
   }

   public final Service.State state() {
      return this.delegate.state();
   }

   public final void addListener(Service.Listener listener, Executor executor) {
      this.delegate.addListener(listener, executor);
   }

   public final Throwable failureCause() {
      return this.delegate.failureCause();
   }

   @CanIgnoreReturnValue
   public final Service startAsync() {
      this.delegate.startAsync();
      return this;
   }

   @CanIgnoreReturnValue
   public final Service stopAsync() {
      this.delegate.stopAsync();
      return this;
   }

   public final void awaitRunning() {
      this.delegate.awaitRunning();
   }

   public final void awaitRunning(Duration timeout) throws TimeoutException {
      Service.super.awaitRunning(timeout);
   }

   public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
      this.delegate.awaitRunning(timeout, unit);
   }

   public final void awaitTerminated() {
      this.delegate.awaitTerminated();
   }

   public final void awaitTerminated(Duration timeout) throws TimeoutException {
      Service.super.awaitTerminated(timeout);
   }

   public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
      this.delegate.awaitTerminated(timeout, unit);
   }

   public abstract static class CustomScheduler extends AbstractScheduledService.Scheduler {
      public CustomScheduler() {
         super(null);
      }

      final AbstractScheduledService.Cancellable schedule(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
         return (new AbstractScheduledService.CustomScheduler.ReschedulableCallable(service, executor, runnable)).reschedule();
      }

      protected abstract AbstractScheduledService.CustomScheduler.Schedule getNextSchedule() throws Exception;

      protected static final class Schedule {
         private final long delay;
         private final TimeUnit unit;

         public Schedule(long delay, TimeUnit unit) {
            this.delay = delay;
            this.unit = (TimeUnit)Preconditions.checkNotNull(unit);
         }

         public Schedule(Duration delay) {
            this(Internal.toNanosSaturated(delay), TimeUnit.NANOSECONDS);
         }
      }

      private static final class SupplantableFuture implements AbstractScheduledService.Cancellable {
         private final ReentrantLock lock;
         @GuardedBy("lock")
         private Future<Void> currentFuture;

         SupplantableFuture(ReentrantLock lock, Future<Void> currentFuture) {
            this.lock = lock;
            this.currentFuture = currentFuture;
         }

         public void cancel(boolean mayInterruptIfRunning) {
            this.lock.lock();

            try {
               this.currentFuture.cancel(mayInterruptIfRunning);
            } finally {
               this.lock.unlock();
            }

         }

         public boolean isCancelled() {
            this.lock.lock();

            boolean var1;
            try {
               var1 = this.currentFuture.isCancelled();
            } finally {
               this.lock.unlock();
            }

            return var1;
         }
      }

      private final class ReschedulableCallable implements Callable<Void> {
         private final Runnable wrappedRunnable;
         private final ScheduledExecutorService executor;
         private final AbstractService service;
         private final ReentrantLock lock = new ReentrantLock();
         @CheckForNull
         @GuardedBy("lock")
         private AbstractScheduledService.CustomScheduler.SupplantableFuture cancellationDelegate;

         ReschedulableCallable(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
            this.wrappedRunnable = runnable;
            this.executor = executor;
            this.service = service;
         }

         @CheckForNull
         public Void call() throws Exception {
            this.wrappedRunnable.run();
            this.reschedule();
            return null;
         }

         @CanIgnoreReturnValue
         public AbstractScheduledService.Cancellable reschedule() {
            AbstractScheduledService.CustomScheduler.Schedule schedule;
            try {
               schedule = CustomScheduler.this.getNextSchedule();
            } catch (Throwable var11) {
               this.service.notifyFailed(var11);
               return new AbstractScheduledService.FutureAsCancellable(Futures.immediateCancelledFuture());
            }

            Throwable scheduleFailure = null;
            this.lock.lock();

            Object toReturn;
            try {
               toReturn = this.initializeOrUpdateCancellationDelegate(schedule);
            } catch (Throwable var9) {
               scheduleFailure = var9;
               toReturn = new AbstractScheduledService.FutureAsCancellable(Futures.immediateCancelledFuture());
            } finally {
               this.lock.unlock();
            }

            if (scheduleFailure != null) {
               this.service.notifyFailed(scheduleFailure);
            }

            return (AbstractScheduledService.Cancellable)toReturn;
         }

         @GuardedBy("lock")
         private AbstractScheduledService.Cancellable initializeOrUpdateCancellationDelegate(AbstractScheduledService.CustomScheduler.Schedule schedule) {
            if (this.cancellationDelegate == null) {
               return this.cancellationDelegate = new AbstractScheduledService.CustomScheduler.SupplantableFuture(this.lock, this.submitToExecutor(schedule));
            } else {
               if (!this.cancellationDelegate.currentFuture.isCancelled()) {
                  this.cancellationDelegate.currentFuture = this.submitToExecutor(schedule);
               }

               return this.cancellationDelegate;
            }
         }

         private ScheduledFuture<Void> submitToExecutor(AbstractScheduledService.CustomScheduler.Schedule schedule) {
            return this.executor.schedule(this, schedule.delay, schedule.unit);
         }
      }
   }

   private static final class FutureAsCancellable implements AbstractScheduledService.Cancellable {
      private final Future<?> delegate;

      FutureAsCancellable(Future<?> delegate) {
         this.delegate = delegate;
      }

      public void cancel(boolean mayInterruptIfRunning) {
         this.delegate.cancel(mayInterruptIfRunning);
      }

      public boolean isCancelled() {
         return this.delegate.isCancelled();
      }
   }

   interface Cancellable {
      void cancel(boolean var1);

      boolean isCancelled();
   }

   private final class ServiceDelegate extends AbstractService {
      @CheckForNull
      private volatile AbstractScheduledService.Cancellable runningTask;
      @CheckForNull
      private volatile ScheduledExecutorService executorService;
      private final ReentrantLock lock;
      private final Runnable task;

      private ServiceDelegate() {
         this.lock = new ReentrantLock();
         this.task = new AbstractScheduledService.ServiceDelegate.Task();
      }

      protected final void doStart() {
         this.executorService = MoreExecutors.renamingDecorator(AbstractScheduledService.this.executor(), new Supplier<String>() {
            public String get() {
               String var1 = AbstractScheduledService.this.serviceName();
               String var2 = String.valueOf(ServiceDelegate.this.state());
               return (new StringBuilder(1 + String.valueOf(var1).length() + String.valueOf(var2).length())).append(var1).append(" ").append(var2).toString();
            }
         });
         this.executorService.execute(new Runnable() {
            public void run() {
               ServiceDelegate.this.lock.lock();

               try {
                  AbstractScheduledService.this.startUp();
                  ServiceDelegate.this.runningTask = AbstractScheduledService.this.scheduler().schedule(AbstractScheduledService.this.delegate, ServiceDelegate.this.executorService, ServiceDelegate.this.task);
                  ServiceDelegate.this.notifyStarted();
               } catch (Throwable var5) {
                  ServiceDelegate.this.notifyFailed(var5);
                  if (ServiceDelegate.this.runningTask != null) {
                     ServiceDelegate.this.runningTask.cancel(false);
                  }
               } finally {
                  ServiceDelegate.this.lock.unlock();
               }

            }
         });
      }

      protected final void doStop() {
         Objects.requireNonNull(this.runningTask);
         Objects.requireNonNull(this.executorService);
         this.runningTask.cancel(false);
         this.executorService.execute(new Runnable() {
            public void run() {
               try {
                  ServiceDelegate.this.lock.lock();

                  label49: {
                     try {
                        if (ServiceDelegate.this.state() == Service.State.STOPPING) {
                           AbstractScheduledService.this.shutDown();
                           break label49;
                        }
                     } finally {
                        ServiceDelegate.this.lock.unlock();
                     }

                     return;
                  }

                  ServiceDelegate.this.notifyStopped();
               } catch (Throwable var5) {
                  ServiceDelegate.this.notifyFailed(var5);
               }

            }
         });
      }

      public String toString() {
         return AbstractScheduledService.this.toString();
      }

      // $FF: synthetic method
      ServiceDelegate(Object x1) {
         this();
      }

      class Task implements Runnable {
         public void run() {
            ServiceDelegate.this.lock.lock();

            try {
               if (((AbstractScheduledService.Cancellable)Objects.requireNonNull(ServiceDelegate.this.runningTask)).isCancelled()) {
                  return;
               }

               AbstractScheduledService.this.runOneIteration();
            } catch (Throwable var8) {
               try {
                  AbstractScheduledService.this.shutDown();
               } catch (Exception var7) {
                  AbstractScheduledService.logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", var7);
               }

               ServiceDelegate.this.notifyFailed(var8);
               ((AbstractScheduledService.Cancellable)Objects.requireNonNull(ServiceDelegate.this.runningTask)).cancel(false);
            } finally {
               ServiceDelegate.this.lock.unlock();
            }

         }
      }
   }

   public abstract static class Scheduler {
      public static AbstractScheduledService.Scheduler newFixedDelaySchedule(Duration initialDelay, Duration delay) {
         return newFixedDelaySchedule(Internal.toNanosSaturated(initialDelay), Internal.toNanosSaturated(delay), TimeUnit.NANOSECONDS);
      }

      public static AbstractScheduledService.Scheduler newFixedDelaySchedule(final long initialDelay, final long delay, final TimeUnit unit) {
         Preconditions.checkNotNull(unit);
         Preconditions.checkArgument(delay > 0L, "delay must be > 0, found %s", delay);
         return new AbstractScheduledService.Scheduler() {
            public AbstractScheduledService.Cancellable schedule(AbstractService service, ScheduledExecutorService executor, Runnable task) {
               return new AbstractScheduledService.FutureAsCancellable(executor.scheduleWithFixedDelay(task, initialDelay, delay, unit));
            }
         };
      }

      public static AbstractScheduledService.Scheduler newFixedRateSchedule(Duration initialDelay, Duration period) {
         return newFixedRateSchedule(Internal.toNanosSaturated(initialDelay), Internal.toNanosSaturated(period), TimeUnit.NANOSECONDS);
      }

      public static AbstractScheduledService.Scheduler newFixedRateSchedule(final long initialDelay, final long period, final TimeUnit unit) {
         Preconditions.checkNotNull(unit);
         Preconditions.checkArgument(period > 0L, "period must be > 0, found %s", period);
         return new AbstractScheduledService.Scheduler() {
            public AbstractScheduledService.Cancellable schedule(AbstractService service, ScheduledExecutorService executor, Runnable task) {
               return new AbstractScheduledService.FutureAsCancellable(executor.scheduleAtFixedRate(task, initialDelay, period, unit));
            }
         };
      }

      abstract AbstractScheduledService.Cancellable schedule(AbstractService var1, ScheduledExecutorService var2, Runnable var3);

      private Scheduler() {
      }

      // $FF: synthetic method
      Scheduler(Object x0) {
         this();
      }
   }
}
