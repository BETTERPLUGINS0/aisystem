package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Strings;
import fr.xephi.authme.libs.com.google.common.base.Throwables;
import fr.xephi.authme.libs.com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import fr.xephi.authme.libs.com.google.common.util.concurrent.internal.InternalFutures;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.ForOverride;
import fr.xephi.authme.libs.com.google.j2objc.annotations.ReflectionSupport;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;
import sun.misc.Unsafe;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
@ReflectionSupport(ReflectionSupport.Level.FULL)
public abstract class AbstractFuture<V> extends InternalFutureFailureAccess implements ListenableFuture<V> {
   static final boolean GENERATE_CANCELLATION_CAUSES;
   private static final Logger log;
   private static final long SPIN_THRESHOLD_NANOS = 1000L;
   private static final AbstractFuture.AtomicHelper ATOMIC_HELPER;
   private static final Object NULL;
   @CheckForNull
   private volatile Object value;
   @CheckForNull
   private volatile AbstractFuture.Listener listeners;
   @CheckForNull
   private volatile AbstractFuture.Waiter waiters;

   private void removeWaiter(AbstractFuture.Waiter node) {
      node.thread = null;

      label28:
      while(true) {
         AbstractFuture.Waiter pred = null;
         AbstractFuture.Waiter curr = this.waiters;
         if (curr == AbstractFuture.Waiter.TOMBSTONE) {
            return;
         }

         AbstractFuture.Waiter succ;
         for(; curr != null; curr = succ) {
            succ = curr.next;
            if (curr.thread != null) {
               pred = curr;
            } else if (pred != null) {
               pred.next = succ;
               if (pred.thread == null) {
                  continue label28;
               }
            } else if (!ATOMIC_HELPER.casWaiters(this, curr, succ)) {
               continue label28;
            }
         }

         return;
      }
   }

   protected AbstractFuture() {
   }

   @ParametricNullness
   @CanIgnoreReturnValue
   public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
      long timeoutNanos = unit.toNanos(timeout);
      long remainingNanos = timeoutNanos;
      if (Thread.interrupted()) {
         throw new InterruptedException();
      } else {
         Object localValue = this.value;
         if (localValue != null & !(localValue instanceof AbstractFuture.SetFuture)) {
            return this.getDoneValue(localValue);
         } else {
            long endNanos = timeoutNanos > 0L ? System.nanoTime() + timeoutNanos : 0L;
            if (timeoutNanos >= 1000L) {
               label136: {
                  AbstractFuture.Waiter oldHead = this.waiters;
                  if (oldHead != AbstractFuture.Waiter.TOMBSTONE) {
                     AbstractFuture.Waiter node = new AbstractFuture.Waiter();

                     do {
                        node.setNext(oldHead);
                        if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                           do {
                              OverflowAvoidingLockSupport.parkNanos(this, remainingNanos);
                              if (Thread.interrupted()) {
                                 this.removeWaiter(node);
                                 throw new InterruptedException();
                              }

                              localValue = this.value;
                              if (localValue != null & !(localValue instanceof AbstractFuture.SetFuture)) {
                                 return this.getDoneValue(localValue);
                              }

                              remainingNanos = endNanos - System.nanoTime();
                           } while(remainingNanos >= 1000L);

                           this.removeWaiter(node);
                           break label136;
                        }

                        oldHead = this.waiters;
                     } while(oldHead != AbstractFuture.Waiter.TOMBSTONE);
                  }

                  return this.getDoneValue(Objects.requireNonNull(this.value));
               }
            }

            while(remainingNanos > 0L) {
               localValue = this.value;
               if (localValue != null & !(localValue instanceof AbstractFuture.SetFuture)) {
                  return this.getDoneValue(localValue);
               }

               if (Thread.interrupted()) {
                  throw new InterruptedException();
               }

               remainingNanos = endNanos - System.nanoTime();
            }

            String futureToString = this.toString();
            String unitString = unit.toString().toLowerCase(Locale.ROOT);
            String var14 = unit.toString().toLowerCase(Locale.ROOT);
            String message = (new StringBuilder(28 + String.valueOf(var14).length())).append("Waited ").append(timeout).append(" ").append(var14).toString();
            if (remainingNanos + 1000L < 0L) {
               message = String.valueOf(message).concat(" (plus ");
               long overWaitNanos = -remainingNanos;
               long overWaitUnits = unit.convert(overWaitNanos, TimeUnit.NANOSECONDS);
               long overWaitLeftoverNanos = overWaitNanos - unit.toNanos(overWaitUnits);
               boolean shouldShowExtraNanos = overWaitUnits == 0L || overWaitLeftoverNanos > 1000L;
               String var21;
               if (overWaitUnits > 0L) {
                  var21 = String.valueOf(message);
                  message = (new StringBuilder(21 + String.valueOf(var21).length() + String.valueOf(unitString).length())).append(var21).append(overWaitUnits).append(" ").append(unitString).toString();
                  if (shouldShowExtraNanos) {
                     message = String.valueOf(message).concat(",");
                  }

                  message = String.valueOf(message).concat(" ");
               }

               if (shouldShowExtraNanos) {
                  var21 = String.valueOf(message);
                  message = (new StringBuilder(33 + String.valueOf(var21).length())).append(var21).append(overWaitLeftoverNanos).append(" nanoseconds ").toString();
               }

               message = String.valueOf(message).concat("delay)");
            }

            if (this.isDone()) {
               throw new TimeoutException(String.valueOf(message).concat(" but future completed as timeout expired"));
            } else {
               throw new TimeoutException((new StringBuilder(5 + String.valueOf(message).length() + String.valueOf(futureToString).length())).append(message).append(" for ").append(futureToString).toString());
            }
         }
      }
   }

   @ParametricNullness
   @CanIgnoreReturnValue
   public V get() throws InterruptedException, ExecutionException {
      if (Thread.interrupted()) {
         throw new InterruptedException();
      } else {
         Object localValue = this.value;
         if (localValue != null & !(localValue instanceof AbstractFuture.SetFuture)) {
            return this.getDoneValue(localValue);
         } else {
            AbstractFuture.Waiter oldHead = this.waiters;
            if (oldHead != AbstractFuture.Waiter.TOMBSTONE) {
               AbstractFuture.Waiter node = new AbstractFuture.Waiter();

               do {
                  node.setNext(oldHead);
                  if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                     do {
                        LockSupport.park(this);
                        if (Thread.interrupted()) {
                           this.removeWaiter(node);
                           throw new InterruptedException();
                        }

                        localValue = this.value;
                     } while(!(localValue != null & !(localValue instanceof AbstractFuture.SetFuture)));

                     return this.getDoneValue(localValue);
                  }

                  oldHead = this.waiters;
               } while(oldHead != AbstractFuture.Waiter.TOMBSTONE);
            }

            return this.getDoneValue(Objects.requireNonNull(this.value));
         }
      }
   }

   @ParametricNullness
   private V getDoneValue(Object obj) throws ExecutionException {
      if (obj instanceof AbstractFuture.Cancellation) {
         throw cancellationExceptionWithCause("Task was cancelled.", ((AbstractFuture.Cancellation)obj).cause);
      } else if (obj instanceof AbstractFuture.Failure) {
         throw new ExecutionException(((AbstractFuture.Failure)obj).exception);
      } else {
         return obj == NULL ? NullnessCasts.uncheckedNull() : obj;
      }
   }

   public boolean isDone() {
      Object localValue = this.value;
      return localValue != null & !(localValue instanceof AbstractFuture.SetFuture);
   }

   public boolean isCancelled() {
      Object localValue = this.value;
      return localValue instanceof AbstractFuture.Cancellation;
   }

   @CanIgnoreReturnValue
   public boolean cancel(boolean mayInterruptIfRunning) {
      Object localValue = this.value;
      boolean rValue = false;
      if (localValue == null | localValue instanceof AbstractFuture.SetFuture) {
         Object valueToSet = GENERATE_CANCELLATION_CAUSES ? new AbstractFuture.Cancellation(mayInterruptIfRunning, new CancellationException("Future.cancel() was called.")) : Objects.requireNonNull(mayInterruptIfRunning ? AbstractFuture.Cancellation.CAUSELESS_INTERRUPTED : AbstractFuture.Cancellation.CAUSELESS_CANCELLED);
         AbstractFuture abstractFuture = this;

         while(true) {
            while(!ATOMIC_HELPER.casValue(abstractFuture, localValue, valueToSet)) {
               localValue = abstractFuture.value;
               if (!(localValue instanceof AbstractFuture.SetFuture)) {
                  return rValue;
               }
            }

            rValue = true;
            if (mayInterruptIfRunning) {
               abstractFuture.interruptTask();
            }

            complete(abstractFuture);
            if (!(localValue instanceof AbstractFuture.SetFuture)) {
               break;
            }

            ListenableFuture<?> futureToPropagateTo = ((AbstractFuture.SetFuture)localValue).future;
            if (!(futureToPropagateTo instanceof AbstractFuture.Trusted)) {
               futureToPropagateTo.cancel(mayInterruptIfRunning);
               break;
            }

            AbstractFuture<?> trusted = (AbstractFuture)futureToPropagateTo;
            localValue = trusted.value;
            if (!(localValue == null | localValue instanceof AbstractFuture.SetFuture)) {
               break;
            }

            abstractFuture = trusted;
         }
      }

      return rValue;
   }

   protected void interruptTask() {
   }

   protected final boolean wasInterrupted() {
      Object localValue = this.value;
      return localValue instanceof AbstractFuture.Cancellation && ((AbstractFuture.Cancellation)localValue).wasInterrupted;
   }

   public void addListener(Runnable listener, Executor executor) {
      Preconditions.checkNotNull(listener, "Runnable was null.");
      Preconditions.checkNotNull(executor, "Executor was null.");
      if (!this.isDone()) {
         AbstractFuture.Listener oldHead = this.listeners;
         if (oldHead != AbstractFuture.Listener.TOMBSTONE) {
            AbstractFuture.Listener newNode = new AbstractFuture.Listener(listener, executor);

            do {
               newNode.next = oldHead;
               if (ATOMIC_HELPER.casListeners(this, oldHead, newNode)) {
                  return;
               }

               oldHead = this.listeners;
            } while(oldHead != AbstractFuture.Listener.TOMBSTONE);
         }
      }

      executeListener(listener, executor);
   }

   @CanIgnoreReturnValue
   protected boolean set(@ParametricNullness V value) {
      Object valueToSet = value == null ? NULL : value;
      if (ATOMIC_HELPER.casValue(this, (Object)null, valueToSet)) {
         complete(this);
         return true;
      } else {
         return false;
      }
   }

   @CanIgnoreReturnValue
   protected boolean setException(Throwable throwable) {
      Object valueToSet = new AbstractFuture.Failure((Throwable)Preconditions.checkNotNull(throwable));
      if (ATOMIC_HELPER.casValue(this, (Object)null, valueToSet)) {
         complete(this);
         return true;
      } else {
         return false;
      }
   }

   @CanIgnoreReturnValue
   protected boolean setFuture(ListenableFuture<? extends V> future) {
      Preconditions.checkNotNull(future);
      Object localValue = this.value;
      if (localValue == null) {
         if (future.isDone()) {
            Object value = getFutureValue(future);
            if (ATOMIC_HELPER.casValue(this, (Object)null, value)) {
               complete(this);
               return true;
            }

            return false;
         }

         AbstractFuture.SetFuture<V> valueToSet = new AbstractFuture.SetFuture(this, future);
         if (ATOMIC_HELPER.casValue(this, (Object)null, valueToSet)) {
            try {
               future.addListener(valueToSet, DirectExecutor.INSTANCE);
            } catch (Throwable var8) {
               Throwable t = var8;

               AbstractFuture.Failure failure;
               try {
                  failure = new AbstractFuture.Failure(t);
               } catch (Throwable var7) {
                  failure = AbstractFuture.Failure.FALLBACK_INSTANCE;
               }

               ATOMIC_HELPER.casValue(this, valueToSet, failure);
            }

            return true;
         }

         localValue = this.value;
      }

      if (localValue instanceof AbstractFuture.Cancellation) {
         future.cancel(((AbstractFuture.Cancellation)localValue).wasInterrupted);
      }

      return false;
   }

   private static Object getFutureValue(ListenableFuture<?> future) {
      if (future instanceof AbstractFuture.Trusted) {
         Object v = ((AbstractFuture)future).value;
         if (v instanceof AbstractFuture.Cancellation) {
            AbstractFuture.Cancellation c = (AbstractFuture.Cancellation)v;
            if (c.wasInterrupted) {
               v = c.cause != null ? new AbstractFuture.Cancellation(false, c.cause) : AbstractFuture.Cancellation.CAUSELESS_CANCELLED;
            }
         }

         return Objects.requireNonNull(v);
      } else {
         if (future instanceof InternalFutureFailureAccess) {
            Throwable throwable = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess)future);
            if (throwable != null) {
               return new AbstractFuture.Failure(throwable);
            }
         }

         boolean wasCancelled = future.isCancelled();
         if (!GENERATE_CANCELLATION_CAUSES & wasCancelled) {
            return Objects.requireNonNull(AbstractFuture.Cancellation.CAUSELESS_CANCELLED);
         } else {
            String var3;
            try {
               Object v = getUninterruptibly(future);
               if (wasCancelled) {
                  var3 = String.valueOf(future);
                  return new AbstractFuture.Cancellation(false, new IllegalArgumentException((new StringBuilder(84 + String.valueOf(var3).length())).append("get() did not throw CancellationException, despite reporting isCancelled() == true: ").append(var3).toString()));
               } else {
                  return v == null ? NULL : v;
               }
            } catch (ExecutionException var4) {
               if (wasCancelled) {
                  var3 = String.valueOf(future);
                  return new AbstractFuture.Cancellation(false, new IllegalArgumentException((new StringBuilder(84 + String.valueOf(var3).length())).append("get() did not throw CancellationException, despite reporting isCancelled() == true: ").append(var3).toString(), var4));
               } else {
                  return new AbstractFuture.Failure(var4.getCause());
               }
            } catch (CancellationException var5) {
               if (!wasCancelled) {
                  var3 = String.valueOf(future);
                  return new AbstractFuture.Failure(new IllegalArgumentException((new StringBuilder(77 + String.valueOf(var3).length())).append("get() threw CancellationException, despite reporting isCancelled() == false: ").append(var3).toString(), var5));
               } else {
                  return new AbstractFuture.Cancellation(false, var5);
               }
            } catch (Throwable var6) {
               return new AbstractFuture.Failure(var6);
            }
         }
      }
   }

   @ParametricNullness
   private static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
      boolean interrupted = false;

      try {
         while(true) {
            try {
               Object var2 = future.get();
               return var2;
            } catch (InterruptedException var6) {
               interrupted = true;
            }
         }
      } finally {
         if (interrupted) {
            Thread.currentThread().interrupt();
         }

      }
   }

   private static void complete(AbstractFuture<?> param) {
      AbstractFuture<?> future = param;
      AbstractFuture.Listener next = null;

      label23:
      while(true) {
         future.releaseWaiters();
         future.afterDone();
         next = future.clearListeners(next);
         future = null;

         while(next != null) {
            AbstractFuture.Listener curr = next;
            next = next.next;
            Runnable task = (Runnable)Objects.requireNonNull(curr.task);
            if (task instanceof AbstractFuture.SetFuture) {
               AbstractFuture.SetFuture<?> setFuture = (AbstractFuture.SetFuture)task;
               future = setFuture.owner;
               if (future.value == setFuture) {
                  Object valueToSet = getFutureValue(setFuture.future);
                  if (ATOMIC_HELPER.casValue(future, setFuture, valueToSet)) {
                     continue label23;
                  }
               }
            } else {
               executeListener(task, (Executor)Objects.requireNonNull(curr.executor));
            }
         }

         return;
      }
   }

   @Beta
   @ForOverride
   protected void afterDone() {
   }

   @CheckForNull
   protected final Throwable tryInternalFastPathGetFailure() {
      if (this instanceof AbstractFuture.Trusted) {
         Object obj = this.value;
         if (obj instanceof AbstractFuture.Failure) {
            return ((AbstractFuture.Failure)obj).exception;
         }
      }

      return null;
   }

   final void maybePropagateCancellationTo(@CheckForNull Future<?> related) {
      if (related != null & this.isCancelled()) {
         related.cancel(this.wasInterrupted());
      }

   }

   private void releaseWaiters() {
      AbstractFuture.Waiter head = ATOMIC_HELPER.gasWaiters(this, AbstractFuture.Waiter.TOMBSTONE);

      for(AbstractFuture.Waiter currentWaiter = head; currentWaiter != null; currentWaiter = currentWaiter.next) {
         currentWaiter.unpark();
      }

   }

   @CheckForNull
   private AbstractFuture.Listener clearListeners(@CheckForNull AbstractFuture.Listener onto) {
      AbstractFuture.Listener head = ATOMIC_HELPER.gasListeners(this, AbstractFuture.Listener.TOMBSTONE);

      AbstractFuture.Listener reversedList;
      AbstractFuture.Listener tmp;
      for(reversedList = onto; head != null; reversedList = tmp) {
         tmp = head;
         head = head.next;
         tmp.next = reversedList;
      }

      return reversedList;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      if (this.getClass().getName().startsWith("fr.xephi.authme.libs.com.google.common.util.concurrent.")) {
         builder.append(this.getClass().getSimpleName());
      } else {
         builder.append(this.getClass().getName());
      }

      builder.append('@').append(Integer.toHexString(System.identityHashCode(this))).append("[status=");
      if (this.isCancelled()) {
         builder.append("CANCELLED");
      } else if (this.isDone()) {
         this.addDoneString(builder);
      } else {
         this.addPendingString(builder);
      }

      return builder.append("]").toString();
   }

   @CheckForNull
   protected String pendingToString() {
      if (this instanceof ScheduledFuture) {
         long var1 = ((ScheduledFuture)this).getDelay(TimeUnit.MILLISECONDS);
         return (new StringBuilder(41)).append("remaining delay=[").append(var1).append(" ms]").toString();
      } else {
         return null;
      }
   }

   private void addPendingString(StringBuilder builder) {
      int truncateLength = builder.length();
      builder.append("PENDING");
      Object localValue = this.value;
      if (localValue instanceof AbstractFuture.SetFuture) {
         builder.append(", setFuture=[");
         this.appendUserObject(builder, ((AbstractFuture.SetFuture)localValue).future);
         builder.append("]");
      } else {
         String pendingDescription;
         try {
            pendingDescription = Strings.emptyToNull(this.pendingToString());
         } catch (StackOverflowError | RuntimeException var7) {
            String var6 = String.valueOf(var7.getClass());
            pendingDescription = (new StringBuilder(38 + String.valueOf(var6).length())).append("Exception thrown from implementation: ").append(var6).toString();
         }

         if (pendingDescription != null) {
            builder.append(", info=[").append(pendingDescription).append("]");
         }
      }

      if (this.isDone()) {
         builder.delete(truncateLength, builder.length());
         this.addDoneString(builder);
      }

   }

   private void addDoneString(StringBuilder builder) {
      try {
         V value = getUninterruptibly(this);
         builder.append("SUCCESS, result=[");
         this.appendResultObject(builder, value);
         builder.append("]");
      } catch (ExecutionException var3) {
         builder.append("FAILURE, cause=[").append(var3.getCause()).append("]");
      } catch (CancellationException var4) {
         builder.append("CANCELLED");
      } catch (RuntimeException var5) {
         builder.append("UNKNOWN, cause=[").append(var5.getClass()).append(" thrown from get()]");
      }

   }

   private void appendResultObject(StringBuilder builder, @CheckForNull Object o) {
      if (o == null) {
         builder.append("null");
      } else if (o == this) {
         builder.append("this future");
      } else {
         builder.append(o.getClass().getName()).append("@").append(Integer.toHexString(System.identityHashCode(o)));
      }

   }

   private void appendUserObject(StringBuilder builder, @CheckForNull Object o) {
      try {
         if (o == this) {
            builder.append("this future");
         } else {
            builder.append(o);
         }
      } catch (StackOverflowError | RuntimeException var4) {
         builder.append("Exception thrown from implementation: ").append(var4.getClass());
      }

   }

   private static void executeListener(Runnable runnable, Executor executor) {
      try {
         executor.execute(runnable);
      } catch (RuntimeException var5) {
         Logger var10000 = log;
         Level var10001 = Level.SEVERE;
         String var3 = String.valueOf(runnable);
         String var4 = String.valueOf(executor);
         var10000.log(var10001, (new StringBuilder(57 + String.valueOf(var3).length() + String.valueOf(var4).length())).append("RuntimeException while executing runnable ").append(var3).append(" with executor ").append(var4).toString(), var5);
      }

   }

   private static CancellationException cancellationExceptionWithCause(String message, @CheckForNull Throwable cause) {
      CancellationException exception = new CancellationException(message);
      exception.initCause(cause);
      return exception;
   }

   static {
      boolean generateCancellationCauses;
      try {
         generateCancellationCauses = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
      } catch (SecurityException var7) {
         generateCancellationCauses = false;
      }

      GENERATE_CANCELLATION_CAUSES = generateCancellationCauses;
      log = Logger.getLogger(AbstractFuture.class.getName());
      Throwable thrownUnsafeFailure = null;
      Throwable thrownAtomicReferenceFieldUpdaterFailure = null;

      Object helper;
      try {
         helper = new AbstractFuture.UnsafeAtomicHelper();
      } catch (Throwable var6) {
         thrownUnsafeFailure = var6;

         try {
            helper = new AbstractFuture.SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.Waiter.class, AbstractFuture.Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, AbstractFuture.Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, AbstractFuture.Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));
         } catch (Throwable var5) {
            thrownAtomicReferenceFieldUpdaterFailure = var5;
            helper = new AbstractFuture.SynchronizedHelper();
         }
      }

      ATOMIC_HELPER = (AbstractFuture.AtomicHelper)helper;
      Class<?> ensureLoaded = LockSupport.class;
      if (thrownAtomicReferenceFieldUpdaterFailure != null) {
         log.log(Level.SEVERE, "UnsafeAtomicHelper is broken!", thrownUnsafeFailure);
         log.log(Level.SEVERE, "SafeAtomicHelper is broken!", thrownAtomicReferenceFieldUpdaterFailure);
      }

      NULL = new Object();
   }

   private static final class SynchronizedHelper extends AbstractFuture.AtomicHelper {
      private SynchronizedHelper() {
         super(null);
      }

      void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
         waiter.thread = newValue;
      }

      void putNext(AbstractFuture.Waiter waiter, @CheckForNull AbstractFuture.Waiter newValue) {
         waiter.next = newValue;
      }

      boolean casWaiters(AbstractFuture<?> future, @CheckForNull AbstractFuture.Waiter expect, @CheckForNull AbstractFuture.Waiter update) {
         synchronized(future) {
            if (future.waiters == expect) {
               future.waiters = update;
               return true;
            } else {
               return false;
            }
         }
      }

      boolean casListeners(AbstractFuture<?> future, @CheckForNull AbstractFuture.Listener expect, AbstractFuture.Listener update) {
         synchronized(future) {
            if (future.listeners == expect) {
               future.listeners = update;
               return true;
            } else {
               return false;
            }
         }
      }

      AbstractFuture.Listener gasListeners(AbstractFuture<?> future, AbstractFuture.Listener update) {
         synchronized(future) {
            AbstractFuture.Listener old = future.listeners;
            if (old != update) {
               future.listeners = update;
            }

            return old;
         }
      }

      AbstractFuture.Waiter gasWaiters(AbstractFuture<?> future, AbstractFuture.Waiter update) {
         synchronized(future) {
            AbstractFuture.Waiter old = future.waiters;
            if (old != update) {
               future.waiters = update;
            }

            return old;
         }
      }

      boolean casValue(AbstractFuture<?> future, @CheckForNull Object expect, Object update) {
         synchronized(future) {
            if (future.value == expect) {
               future.value = update;
               return true;
            } else {
               return false;
            }
         }
      }

      // $FF: synthetic method
      SynchronizedHelper(Object x0) {
         this();
      }
   }

   private static final class SafeAtomicHelper extends AbstractFuture.AtomicHelper {
      final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater;
      final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater;
      final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater;
      final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater;
      final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;

      SafeAtomicHelper(AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater, AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater) {
         super(null);
         this.waiterThreadUpdater = waiterThreadUpdater;
         this.waiterNextUpdater = waiterNextUpdater;
         this.waitersUpdater = waitersUpdater;
         this.listenersUpdater = listenersUpdater;
         this.valueUpdater = valueUpdater;
      }

      void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
         this.waiterThreadUpdater.lazySet(waiter, newValue);
      }

      void putNext(AbstractFuture.Waiter waiter, @CheckForNull AbstractFuture.Waiter newValue) {
         this.waiterNextUpdater.lazySet(waiter, newValue);
      }

      boolean casWaiters(AbstractFuture<?> future, @CheckForNull AbstractFuture.Waiter expect, @CheckForNull AbstractFuture.Waiter update) {
         return this.waitersUpdater.compareAndSet(future, expect, update);
      }

      boolean casListeners(AbstractFuture<?> future, @CheckForNull AbstractFuture.Listener expect, AbstractFuture.Listener update) {
         return this.listenersUpdater.compareAndSet(future, expect, update);
      }

      AbstractFuture.Listener gasListeners(AbstractFuture<?> future, AbstractFuture.Listener update) {
         return (AbstractFuture.Listener)this.listenersUpdater.getAndSet(future, update);
      }

      AbstractFuture.Waiter gasWaiters(AbstractFuture<?> future, AbstractFuture.Waiter update) {
         return (AbstractFuture.Waiter)this.waitersUpdater.getAndSet(future, update);
      }

      boolean casValue(AbstractFuture<?> future, @CheckForNull Object expect, Object update) {
         return this.valueUpdater.compareAndSet(future, expect, update);
      }
   }

   private static final class UnsafeAtomicHelper extends AbstractFuture.AtomicHelper {
      static final Unsafe UNSAFE;
      static final long LISTENERS_OFFSET;
      static final long WAITERS_OFFSET;
      static final long VALUE_OFFSET;
      static final long WAITER_THREAD_OFFSET;
      static final long WAITER_NEXT_OFFSET;

      private UnsafeAtomicHelper() {
         super(null);
      }

      void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
         UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, newValue);
      }

      void putNext(AbstractFuture.Waiter waiter, @CheckForNull AbstractFuture.Waiter newValue) {
         UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, newValue);
      }

      boolean casWaiters(AbstractFuture<?> future, @CheckForNull AbstractFuture.Waiter expect, @CheckForNull AbstractFuture.Waiter update) {
         return UNSAFE.compareAndSwapObject(future, WAITERS_OFFSET, expect, update);
      }

      boolean casListeners(AbstractFuture<?> future, @CheckForNull AbstractFuture.Listener expect, AbstractFuture.Listener update) {
         return UNSAFE.compareAndSwapObject(future, LISTENERS_OFFSET, expect, update);
      }

      AbstractFuture.Listener gasListeners(AbstractFuture<?> future, AbstractFuture.Listener update) {
         return (AbstractFuture.Listener)UNSAFE.getAndSetObject(future, LISTENERS_OFFSET, update);
      }

      AbstractFuture.Waiter gasWaiters(AbstractFuture<?> future, AbstractFuture.Waiter update) {
         return (AbstractFuture.Waiter)UNSAFE.getAndSetObject(future, WAITERS_OFFSET, update);
      }

      boolean casValue(AbstractFuture<?> future, @CheckForNull Object expect, Object update) {
         return UNSAFE.compareAndSwapObject(future, VALUE_OFFSET, expect, update);
      }

      // $FF: synthetic method
      UnsafeAtomicHelper(Object x0) {
         this();
      }

      static {
         Unsafe unsafe = null;

         try {
            unsafe = Unsafe.getUnsafe();
         } catch (SecurityException var5) {
            try {
               unsafe = (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>() {
                  public Unsafe run() throws Exception {
                     Class<Unsafe> k = Unsafe.class;
                     Field[] var2 = k.getDeclaredFields();
                     int var3 = var2.length;

                     for(int var4 = 0; var4 < var3; ++var4) {
                        Field f = var2[var4];
                        f.setAccessible(true);
                        Object x = f.get((Object)null);
                        if (k.isInstance(x)) {
                           return (Unsafe)k.cast(x);
                        }
                     }

                     throw new NoSuchFieldError("the Unsafe");
                  }
               });
            } catch (PrivilegedActionException var4) {
               throw new RuntimeException("Could not initialize intrinsics", var4.getCause());
            }
         }

         try {
            Class<?> abstractFuture = AbstractFuture.class;
            WAITERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("waiters"));
            LISTENERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("listeners"));
            VALUE_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("value"));
            WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("thread"));
            WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("next"));
            UNSAFE = unsafe;
         } catch (Exception var3) {
            Throwables.throwIfUnchecked(var3);
            throw new RuntimeException(var3);
         }
      }
   }

   private abstract static class AtomicHelper {
      private AtomicHelper() {
      }

      abstract void putThread(AbstractFuture.Waiter var1, Thread var2);

      abstract void putNext(AbstractFuture.Waiter var1, @CheckForNull AbstractFuture.Waiter var2);

      abstract boolean casWaiters(AbstractFuture<?> var1, @CheckForNull AbstractFuture.Waiter var2, @CheckForNull AbstractFuture.Waiter var3);

      abstract boolean casListeners(AbstractFuture<?> var1, @CheckForNull AbstractFuture.Listener var2, AbstractFuture.Listener var3);

      abstract AbstractFuture.Waiter gasWaiters(AbstractFuture<?> var1, AbstractFuture.Waiter var2);

      abstract AbstractFuture.Listener gasListeners(AbstractFuture<?> var1, AbstractFuture.Listener var2);

      abstract boolean casValue(AbstractFuture<?> var1, @CheckForNull Object var2, Object var3);

      // $FF: synthetic method
      AtomicHelper(Object x0) {
         this();
      }
   }

   private static final class SetFuture<V> implements Runnable {
      final AbstractFuture<V> owner;
      final ListenableFuture<? extends V> future;

      SetFuture(AbstractFuture<V> owner, ListenableFuture<? extends V> future) {
         this.owner = owner;
         this.future = future;
      }

      public void run() {
         if (this.owner.value == this) {
            Object valueToSet = AbstractFuture.getFutureValue(this.future);
            if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, valueToSet)) {
               AbstractFuture.complete(this.owner);
            }

         }
      }
   }

   private static final class Cancellation {
      @CheckForNull
      static final AbstractFuture.Cancellation CAUSELESS_INTERRUPTED;
      @CheckForNull
      static final AbstractFuture.Cancellation CAUSELESS_CANCELLED;
      final boolean wasInterrupted;
      @CheckForNull
      final Throwable cause;

      Cancellation(boolean wasInterrupted, @CheckForNull Throwable cause) {
         this.wasInterrupted = wasInterrupted;
         this.cause = cause;
      }

      static {
         if (AbstractFuture.GENERATE_CANCELLATION_CAUSES) {
            CAUSELESS_CANCELLED = null;
            CAUSELESS_INTERRUPTED = null;
         } else {
            CAUSELESS_CANCELLED = new AbstractFuture.Cancellation(false, (Throwable)null);
            CAUSELESS_INTERRUPTED = new AbstractFuture.Cancellation(true, (Throwable)null);
         }

      }
   }

   private static final class Failure {
      static final AbstractFuture.Failure FALLBACK_INSTANCE = new AbstractFuture.Failure(new Throwable("Failure occurred while trying to finish a future.") {
         public synchronized Throwable fillInStackTrace() {
            return this;
         }
      });
      final Throwable exception;

      Failure(Throwable exception) {
         this.exception = (Throwable)Preconditions.checkNotNull(exception);
      }
   }

   private static final class Listener {
      static final AbstractFuture.Listener TOMBSTONE = new AbstractFuture.Listener();
      @CheckForNull
      final Runnable task;
      @CheckForNull
      final Executor executor;
      @CheckForNull
      AbstractFuture.Listener next;

      Listener(Runnable task, Executor executor) {
         this.task = task;
         this.executor = executor;
      }

      Listener() {
         this.task = null;
         this.executor = null;
      }
   }

   private static final class Waiter {
      static final AbstractFuture.Waiter TOMBSTONE = new AbstractFuture.Waiter(false);
      @CheckForNull
      volatile Thread thread;
      @CheckForNull
      volatile AbstractFuture.Waiter next;

      Waiter(boolean unused) {
      }

      Waiter() {
         AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
      }

      void setNext(@CheckForNull AbstractFuture.Waiter next) {
         AbstractFuture.ATOMIC_HELPER.putNext(this, next);
      }

      void unpark() {
         Thread w = this.thread;
         if (w != null) {
            this.thread = null;
            LockSupport.unpark(w);
         }

      }
   }

   abstract static class TrustedFuture<V> extends AbstractFuture<V> implements AbstractFuture.Trusted<V> {
      @ParametricNullness
      @CanIgnoreReturnValue
      public final V get() throws InterruptedException, ExecutionException {
         return super.get();
      }

      @ParametricNullness
      @CanIgnoreReturnValue
      public final V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
         return super.get(timeout, unit);
      }

      public final boolean isDone() {
         return super.isDone();
      }

      public final boolean isCancelled() {
         return super.isCancelled();
      }

      public final void addListener(Runnable listener, Executor executor) {
         super.addListener(listener, executor);
      }

      @CanIgnoreReturnValue
      public final boolean cancel(boolean mayInterruptIfRunning) {
         return super.cancel(mayInterruptIfRunning);
      }
   }

   interface Trusted<V> extends ListenableFuture<V> {
   }
}
