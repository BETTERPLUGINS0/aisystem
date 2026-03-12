package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.ForOverride;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.GuardedBy;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public abstract class AbstractService implements Service {
   private static final ListenerCallQueue.Event<Service.Listener> STARTING_EVENT = new ListenerCallQueue.Event<Service.Listener>() {
      public void call(Service.Listener listener) {
         listener.starting();
      }

      public String toString() {
         return "starting()";
      }
   };
   private static final ListenerCallQueue.Event<Service.Listener> RUNNING_EVENT = new ListenerCallQueue.Event<Service.Listener>() {
      public void call(Service.Listener listener) {
         listener.running();
      }

      public String toString() {
         return "running()";
      }
   };
   private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_STARTING_EVENT;
   private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_RUNNING_EVENT;
   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_NEW_EVENT;
   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_STARTING_EVENT;
   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_RUNNING_EVENT;
   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_STOPPING_EVENT;
   private final Monitor monitor = new Monitor();
   private final Monitor.Guard isStartable = new AbstractService.IsStartableGuard();
   private final Monitor.Guard isStoppable = new AbstractService.IsStoppableGuard();
   private final Monitor.Guard hasReachedRunning = new AbstractService.HasReachedRunningGuard();
   private final Monitor.Guard isStopped = new AbstractService.IsStoppedGuard();
   private final ListenerCallQueue<Service.Listener> listeners = new ListenerCallQueue();
   private volatile AbstractService.StateSnapshot snapshot;

   private static ListenerCallQueue.Event<Service.Listener> terminatedEvent(final Service.State from) {
      return new ListenerCallQueue.Event<Service.Listener>() {
         public void call(Service.Listener listener) {
            listener.terminated(from);
         }

         public String toString() {
            String var1 = String.valueOf(from);
            return (new StringBuilder(21 + String.valueOf(var1).length())).append("terminated({from = ").append(var1).append("})").toString();
         }
      };
   }

   private static ListenerCallQueue.Event<Service.Listener> stoppingEvent(final Service.State from) {
      return new ListenerCallQueue.Event<Service.Listener>() {
         public void call(Service.Listener listener) {
            listener.stopping(from);
         }

         public String toString() {
            String var1 = String.valueOf(from);
            return (new StringBuilder(19 + String.valueOf(var1).length())).append("stopping({from = ").append(var1).append("})").toString();
         }
      };
   }

   protected AbstractService() {
      this.snapshot = new AbstractService.StateSnapshot(Service.State.NEW);
   }

   @ForOverride
   protected abstract void doStart();

   @ForOverride
   protected abstract void doStop();

   @Beta
   @ForOverride
   protected void doCancelStart() {
   }

   @CanIgnoreReturnValue
   public final Service startAsync() {
      if (this.monitor.enterIf(this.isStartable)) {
         try {
            this.snapshot = new AbstractService.StateSnapshot(Service.State.STARTING);
            this.enqueueStartingEvent();
            this.doStart();
         } catch (Throwable var5) {
            this.notifyFailed(var5);
         } finally {
            this.monitor.leave();
            this.dispatchListenerEvents();
         }

         return this;
      } else {
         String var1 = String.valueOf(this);
         throw new IllegalStateException((new StringBuilder(33 + String.valueOf(var1).length())).append("Service ").append(var1).append(" has already been started").toString());
      }
   }

   @CanIgnoreReturnValue
   public final Service stopAsync() {
      if (this.monitor.enterIf(this.isStoppable)) {
         try {
            Service.State previous = this.state();
            switch(previous) {
            case NEW:
               this.snapshot = new AbstractService.StateSnapshot(Service.State.TERMINATED);
               this.enqueueTerminatedEvent(Service.State.NEW);
               break;
            case STARTING:
               this.snapshot = new AbstractService.StateSnapshot(Service.State.STARTING, true, (Throwable)null);
               this.enqueueStoppingEvent(Service.State.STARTING);
               this.doCancelStart();
               break;
            case RUNNING:
               this.snapshot = new AbstractService.StateSnapshot(Service.State.STOPPING);
               this.enqueueStoppingEvent(Service.State.RUNNING);
               this.doStop();
               break;
            case STOPPING:
            case TERMINATED:
            case FAILED:
               String var2 = String.valueOf(previous);
               throw new AssertionError((new StringBuilder(45 + String.valueOf(var2).length())).append("isStoppable is incorrectly implemented, saw: ").append(var2).toString());
            }
         } catch (Throwable var6) {
            this.notifyFailed(var6);
         } finally {
            this.monitor.leave();
            this.dispatchListenerEvents();
         }
      }

      return this;
   }

   public final void awaitRunning() {
      this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);

      try {
         this.checkCurrentState(Service.State.RUNNING);
      } finally {
         this.monitor.leave();
      }

   }

   public final void awaitRunning(Duration timeout) throws TimeoutException {
      Service.super.awaitRunning(timeout);
   }

   public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
      if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, timeout, unit)) {
         try {
            this.checkCurrentState(Service.State.RUNNING);
         } finally {
            this.monitor.leave();
         }

      } else {
         String var4 = String.valueOf(this);
         throw new TimeoutException((new StringBuilder(50 + String.valueOf(var4).length())).append("Timed out waiting for ").append(var4).append(" to reach the RUNNING state.").toString());
      }
   }

   public final void awaitTerminated() {
      this.monitor.enterWhenUninterruptibly(this.isStopped);

      try {
         this.checkCurrentState(Service.State.TERMINATED);
      } finally {
         this.monitor.leave();
      }

   }

   public final void awaitTerminated(Duration timeout) throws TimeoutException {
      Service.super.awaitTerminated(timeout);
   }

   public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
      if (this.monitor.enterWhenUninterruptibly(this.isStopped, timeout, unit)) {
         try {
            this.checkCurrentState(Service.State.TERMINATED);
         } finally {
            this.monitor.leave();
         }

      } else {
         String var4 = String.valueOf(this);
         String var5 = String.valueOf(this.state());
         throw new TimeoutException((new StringBuilder(65 + String.valueOf(var4).length() + String.valueOf(var5).length())).append("Timed out waiting for ").append(var4).append(" to reach a terminal state. Current state: ").append(var5).toString());
      }
   }

   @GuardedBy("monitor")
   private void checkCurrentState(Service.State expected) {
      Service.State actual = this.state();
      if (actual != expected) {
         String var3;
         String var4;
         if (actual == Service.State.FAILED) {
            var3 = String.valueOf(this);
            var4 = String.valueOf(expected);
            throw new IllegalStateException((new StringBuilder(56 + String.valueOf(var3).length() + String.valueOf(var4).length())).append("Expected the service ").append(var3).append(" to be ").append(var4).append(", but the service has FAILED").toString(), this.failureCause());
         } else {
            var3 = String.valueOf(this);
            var4 = String.valueOf(expected);
            String var5 = String.valueOf(actual);
            throw new IllegalStateException((new StringBuilder(38 + String.valueOf(var3).length() + String.valueOf(var4).length() + String.valueOf(var5).length())).append("Expected the service ").append(var3).append(" to be ").append(var4).append(", but was ").append(var5).toString());
         }
      }
   }

   protected final void notifyStarted() {
      this.monitor.enter();

      try {
         if (this.snapshot.state != Service.State.STARTING) {
            String var2 = String.valueOf(this.snapshot.state);
            IllegalStateException failure = new IllegalStateException((new StringBuilder(43 + String.valueOf(var2).length())).append("Cannot notifyStarted() when the service is ").append(var2).toString());
            this.notifyFailed(failure);
            throw failure;
         }

         if (this.snapshot.shutdownWhenStartupFinishes) {
            this.snapshot = new AbstractService.StateSnapshot(Service.State.STOPPING);
            this.doStop();
         } else {
            this.snapshot = new AbstractService.StateSnapshot(Service.State.RUNNING);
            this.enqueueRunningEvent();
         }
      } finally {
         this.monitor.leave();
         this.dispatchListenerEvents();
      }

   }

   protected final void notifyStopped() {
      this.monitor.enter();

      try {
         Service.State previous = this.state();
         switch(previous) {
         case NEW:
         case TERMINATED:
         case FAILED:
            String var2 = String.valueOf(previous);
            throw new IllegalStateException((new StringBuilder(43 + String.valueOf(var2).length())).append("Cannot notifyStopped() when the service is ").append(var2).toString());
         case STARTING:
         case RUNNING:
         case STOPPING:
            this.snapshot = new AbstractService.StateSnapshot(Service.State.TERMINATED);
            this.enqueueTerminatedEvent(previous);
         }
      } finally {
         this.monitor.leave();
         this.dispatchListenerEvents();
      }

   }

   protected final void notifyFailed(Throwable cause) {
      Preconditions.checkNotNull(cause);
      this.monitor.enter();

      try {
         Service.State previous = this.state();
         switch(previous) {
         case NEW:
         case TERMINATED:
            String var3 = String.valueOf(previous);
            throw new IllegalStateException((new StringBuilder(22 + String.valueOf(var3).length())).append("Failed while in state:").append(var3).toString(), cause);
         case STARTING:
         case RUNNING:
         case STOPPING:
            this.snapshot = new AbstractService.StateSnapshot(Service.State.FAILED, false, cause);
            this.enqueueFailedEvent(previous, cause);
         case FAILED:
         }
      } finally {
         this.monitor.leave();
         this.dispatchListenerEvents();
      }

   }

   public final boolean isRunning() {
      return this.state() == Service.State.RUNNING;
   }

   public final Service.State state() {
      return this.snapshot.externalState();
   }

   public final Throwable failureCause() {
      return this.snapshot.failureCause();
   }

   public final void addListener(Service.Listener listener, Executor executor) {
      this.listeners.addListener(listener, executor);
   }

   public String toString() {
      String var1 = this.getClass().getSimpleName();
      String var2 = String.valueOf(this.state());
      return (new StringBuilder(3 + String.valueOf(var1).length() + String.valueOf(var2).length())).append(var1).append(" [").append(var2).append("]").toString();
   }

   private void dispatchListenerEvents() {
      if (!this.monitor.isOccupiedByCurrentThread()) {
         this.listeners.dispatch();
      }

   }

   private void enqueueStartingEvent() {
      this.listeners.enqueue(STARTING_EVENT);
   }

   private void enqueueRunningEvent() {
      this.listeners.enqueue(RUNNING_EVENT);
   }

   private void enqueueStoppingEvent(Service.State from) {
      if (from == Service.State.STARTING) {
         this.listeners.enqueue(STOPPING_FROM_STARTING_EVENT);
      } else {
         if (from != Service.State.RUNNING) {
            throw new AssertionError();
         }

         this.listeners.enqueue(STOPPING_FROM_RUNNING_EVENT);
      }

   }

   private void enqueueTerminatedEvent(Service.State from) {
      switch(from) {
      case NEW:
         this.listeners.enqueue(TERMINATED_FROM_NEW_EVENT);
         break;
      case STARTING:
         this.listeners.enqueue(TERMINATED_FROM_STARTING_EVENT);
         break;
      case RUNNING:
         this.listeners.enqueue(TERMINATED_FROM_RUNNING_EVENT);
         break;
      case STOPPING:
         this.listeners.enqueue(TERMINATED_FROM_STOPPING_EVENT);
         break;
      case TERMINATED:
      case FAILED:
         throw new AssertionError();
      }

   }

   private void enqueueFailedEvent(final Service.State from, final Throwable cause) {
      this.listeners.enqueue(new ListenerCallQueue.Event<Service.Listener>(this) {
         public void call(Service.Listener listener) {
            listener.failed(from, cause);
         }

         public String toString() {
            String var1 = String.valueOf(from);
            String var2 = String.valueOf(cause);
            return (new StringBuilder(27 + String.valueOf(var1).length() + String.valueOf(var2).length())).append("failed({from = ").append(var1).append(", cause = ").append(var2).append("})").toString();
         }
      });
   }

   static {
      STOPPING_FROM_STARTING_EVENT = stoppingEvent(Service.State.STARTING);
      STOPPING_FROM_RUNNING_EVENT = stoppingEvent(Service.State.RUNNING);
      TERMINATED_FROM_NEW_EVENT = terminatedEvent(Service.State.NEW);
      TERMINATED_FROM_STARTING_EVENT = terminatedEvent(Service.State.STARTING);
      TERMINATED_FROM_RUNNING_EVENT = terminatedEvent(Service.State.RUNNING);
      TERMINATED_FROM_STOPPING_EVENT = terminatedEvent(Service.State.STOPPING);
   }

   private static final class StateSnapshot {
      final Service.State state;
      final boolean shutdownWhenStartupFinishes;
      @CheckForNull
      final Throwable failure;

      StateSnapshot(Service.State internalState) {
         this(internalState, false, (Throwable)null);
      }

      StateSnapshot(Service.State internalState, boolean shutdownWhenStartupFinishes, @CheckForNull Throwable failure) {
         Preconditions.checkArgument(!shutdownWhenStartupFinishes || internalState == Service.State.STARTING, "shutdownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", (Object)internalState);
         Preconditions.checkArgument(failure != null == (internalState == Service.State.FAILED), "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", internalState, failure);
         this.state = internalState;
         this.shutdownWhenStartupFinishes = shutdownWhenStartupFinishes;
         this.failure = failure;
      }

      Service.State externalState() {
         return this.shutdownWhenStartupFinishes && this.state == Service.State.STARTING ? Service.State.STOPPING : this.state;
      }

      Throwable failureCause() {
         Preconditions.checkState(this.state == Service.State.FAILED, "failureCause() is only valid if the service has failed, service is %s", (Object)this.state);
         return (Throwable)Objects.requireNonNull(this.failure);
      }
   }

   private final class IsStoppedGuard extends Monitor.Guard {
      IsStoppedGuard() {
         super(AbstractService.this.monitor);
      }

      public boolean isSatisfied() {
         return AbstractService.this.state().compareTo(Service.State.TERMINATED) >= 0;
      }
   }

   private final class HasReachedRunningGuard extends Monitor.Guard {
      HasReachedRunningGuard() {
         super(AbstractService.this.monitor);
      }

      public boolean isSatisfied() {
         return AbstractService.this.state().compareTo(Service.State.RUNNING) >= 0;
      }
   }

   private final class IsStoppableGuard extends Monitor.Guard {
      IsStoppableGuard() {
         super(AbstractService.this.monitor);
      }

      public boolean isSatisfied() {
         return AbstractService.this.state().compareTo(Service.State.RUNNING) <= 0;
      }
   }

   private final class IsStartableGuard extends Monitor.Guard {
      IsStartableGuard() {
         super(AbstractService.this.monitor);
      }

      public boolean isSatisfied() {
         return AbstractService.this.state() == Service.State.NEW;
      }
   }
}
