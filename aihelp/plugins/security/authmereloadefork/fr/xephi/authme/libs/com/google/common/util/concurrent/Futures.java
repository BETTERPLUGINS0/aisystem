package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.common.base.MoreObjects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.libs.com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import fr.xephi.authme.libs.com.google.common.util.concurrent.internal.InternalFutures;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
public final class Futures extends GwtFuturesCatchingSpecialization {
   private Futures() {
   }

   public static <V> ListenableFuture<V> immediateFuture(@ParametricNullness V value) {
      if (value == null) {
         ListenableFuture<V> typedNull = ImmediateFuture.NULL;
         return typedNull;
      } else {
         return new ImmediateFuture(value);
      }
   }

   public static ListenableFuture<Void> immediateVoidFuture() {
      return ImmediateFuture.NULL;
   }

   public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
      Preconditions.checkNotNull(throwable);
      return new ImmediateFuture.ImmediateFailedFuture(throwable);
   }

   public static <V> ListenableFuture<V> immediateCancelledFuture() {
      ListenableFuture<Object> instance = ImmediateFuture.ImmediateCancelledFuture.INSTANCE;
      return instance != null ? instance : new ImmediateFuture.ImmediateCancelledFuture();
   }

   public static <O> ListenableFuture<O> submit(Callable<O> callable, Executor executor) {
      TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
      executor.execute(task);
      return task;
   }

   public static ListenableFuture<Void> submit(Runnable runnable, Executor executor) {
      TrustedListenableFutureTask<Void> task = TrustedListenableFutureTask.create(runnable, (Object)null);
      executor.execute(task);
      return task;
   }

   public static <O> ListenableFuture<O> submitAsync(AsyncCallable<O> callable, Executor executor) {
      TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
      executor.execute(task);
      return task;
   }

   @GwtIncompatible
   public static <O> ListenableFuture<O> scheduleAsync(AsyncCallable<O> callable, Duration delay, ScheduledExecutorService executorService) {
      return scheduleAsync(callable, Internal.toNanosSaturated(delay), TimeUnit.NANOSECONDS, executorService);
   }

   @GwtIncompatible
   public static <O> ListenableFuture<O> scheduleAsync(AsyncCallable<O> callable, long delay, TimeUnit timeUnit, ScheduledExecutorService executorService) {
      TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
      final Future<?> scheduled = executorService.schedule(task, delay, timeUnit);
      task.addListener(new Runnable() {
         public void run() {
            scheduled.cancel(false);
         }
      }, MoreExecutors.directExecutor());
      return task;
   }

   @Beta
   @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
   public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor) {
      return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
   }

   @Beta
   @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
   public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor) {
      return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
   }

   @Beta
   @GwtIncompatible
   public static <V> ListenableFuture<V> withTimeout(ListenableFuture<V> delegate, Duration time, ScheduledExecutorService scheduledExecutor) {
      return withTimeout(delegate, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS, scheduledExecutor);
   }

   @Beta
   @GwtIncompatible
   public static <V> ListenableFuture<V> withTimeout(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor) {
      return delegate.isDone() ? delegate : TimeoutFuture.create(delegate, time, unit, scheduledExecutor);
   }

   @Beta
   public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
      return AbstractTransformFuture.create(input, function, executor);
   }

   @Beta
   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
      return AbstractTransformFuture.create(input, function, executor);
   }

   @Beta
   @GwtIncompatible
   public static <I, O> Future<O> lazyTransform(final Future<I> input, final Function<? super I, ? extends O> function) {
      Preconditions.checkNotNull(input);
      Preconditions.checkNotNull(function);
      return new Future<O>() {
         public boolean cancel(boolean mayInterruptIfRunning) {
            return input.cancel(mayInterruptIfRunning);
         }

         public boolean isCancelled() {
            return input.isCancelled();
         }

         public boolean isDone() {
            return input.isDone();
         }

         public O get() throws InterruptedException, ExecutionException {
            return this.applyTransformation(input.get());
         }

         public O get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return this.applyTransformation(input.get(timeout, unit));
         }

         private O applyTransformation(I inputx) throws ExecutionException {
            try {
               return function.apply(inputx);
            } catch (Throwable var3) {
               throw new ExecutionException(var3);
            }
         }
      };
   }

   @SafeVarargs
   @Beta
   public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... futures) {
      ListenableFuture<List<V>> nullable = new CollectionFuture.ListFuture(ImmutableList.copyOf((Object[])futures), true);
      return nullable;
   }

   @Beta
   public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
      ListenableFuture<List<V>> nullable = new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), true);
      return nullable;
   }

   @SafeVarargs
   @Beta
   public static <V> Futures.FutureCombiner<V> whenAllComplete(ListenableFuture<? extends V>... futures) {
      return new Futures.FutureCombiner(false, ImmutableList.copyOf((Object[])futures));
   }

   @Beta
   public static <V> Futures.FutureCombiner<V> whenAllComplete(Iterable<? extends ListenableFuture<? extends V>> futures) {
      return new Futures.FutureCombiner(false, ImmutableList.copyOf(futures));
   }

   @SafeVarargs
   @Beta
   public static <V> Futures.FutureCombiner<V> whenAllSucceed(ListenableFuture<? extends V>... futures) {
      return new Futures.FutureCombiner(true, ImmutableList.copyOf((Object[])futures));
   }

   @Beta
   public static <V> Futures.FutureCombiner<V> whenAllSucceed(Iterable<? extends ListenableFuture<? extends V>> futures) {
      return new Futures.FutureCombiner(true, ImmutableList.copyOf(futures));
   }

   public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future) {
      if (future.isDone()) {
         return future;
      } else {
         Futures.NonCancellationPropagatingFuture<V> output = new Futures.NonCancellationPropagatingFuture(future);
         future.addListener(output, MoreExecutors.directExecutor());
         return output;
      }
   }

   @SafeVarargs
   @Beta
   public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... futures) {
      return new CollectionFuture.ListFuture(ImmutableList.copyOf((Object[])futures), false);
   }

   @Beta
   public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
      return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), false);
   }

   public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> futures) {
      ListenableFuture<? extends T>[] copy = gwtCompatibleToArray(futures);
      final Futures.InCompletionOrderState<T> state = new Futures.InCompletionOrderState(copy);
      ImmutableList.Builder<AbstractFuture<T>> delegatesBuilder = ImmutableList.builderWithExpectedSize(copy.length);

      for(int i = 0; i < copy.length; ++i) {
         delegatesBuilder.add((Object)(new Futures.InCompletionOrderFuture(state)));
      }

      final ImmutableList<AbstractFuture<T>> delegates = delegatesBuilder.build();

      for(final int i = 0; i < copy.length; ++i) {
         copy[i].addListener(new Runnable() {
            public void run() {
               state.recordInputCompletion(delegates, i);
            }
         }, MoreExecutors.directExecutor());
      }

      return delegates;
   }

   private static <T> ListenableFuture<? extends T>[] gwtCompatibleToArray(Iterable<? extends ListenableFuture<? extends T>> futures) {
      Object collection;
      if (futures instanceof Collection) {
         collection = (Collection)futures;
      } else {
         collection = ImmutableList.copyOf(futures);
      }

      return (ListenableFuture[])((Collection)collection).toArray(new ListenableFuture[0]);
   }

   public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback, Executor executor) {
      Preconditions.checkNotNull(callback);
      future.addListener(new Futures.CallbackListener(future, callback), executor);
   }

   @ParametricNullness
   @CanIgnoreReturnValue
   public static <V> V getDone(Future<V> future) throws ExecutionException {
      Preconditions.checkState(future.isDone(), "Future was expected to be done: %s", (Object)future);
      return Uninterruptibles.getUninterruptibly(future);
   }

   @ParametricNullness
   @Beta
   @CanIgnoreReturnValue
   @GwtIncompatible
   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass) throws X {
      return FuturesGetChecked.getChecked(future, exceptionClass);
   }

   @ParametricNullness
   @Beta
   @CanIgnoreReturnValue
   @GwtIncompatible
   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, Duration timeout) throws X {
      return getChecked(future, exceptionClass, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
   }

   @ParametricNullness
   @Beta
   @CanIgnoreReturnValue
   @GwtIncompatible
   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit) throws X {
      return FuturesGetChecked.getChecked(future, exceptionClass, timeout, unit);
   }

   @ParametricNullness
   @CanIgnoreReturnValue
   public static <V> V getUnchecked(Future<V> future) {
      Preconditions.checkNotNull(future);

      try {
         return Uninterruptibles.getUninterruptibly(future);
      } catch (ExecutionException var2) {
         wrapAndThrowUnchecked(var2.getCause());
         throw new AssertionError();
      }
   }

   private static void wrapAndThrowUnchecked(Throwable cause) {
      if (cause instanceof Error) {
         throw new ExecutionError((Error)cause);
      } else {
         throw new UncheckedExecutionException(cause);
      }
   }

   private static final class CallbackListener<V> implements Runnable {
      final Future<V> future;
      final FutureCallback<? super V> callback;

      CallbackListener(Future<V> future, FutureCallback<? super V> callback) {
         this.future = future;
         this.callback = callback;
      }

      public void run() {
         if (this.future instanceof InternalFutureFailureAccess) {
            Throwable failure = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess)this.future);
            if (failure != null) {
               this.callback.onFailure(failure);
               return;
            }
         }

         Object value;
         try {
            value = Futures.getDone(this.future);
         } catch (ExecutionException var3) {
            this.callback.onFailure(var3.getCause());
            return;
         } catch (Error | RuntimeException var4) {
            this.callback.onFailure(var4);
            return;
         }

         this.callback.onSuccess(value);
      }

      public String toString() {
         return MoreObjects.toStringHelper((Object)this).addValue(this.callback).toString();
      }
   }

   private static final class InCompletionOrderState<T> {
      private boolean wasCancelled;
      private boolean shouldInterrupt;
      private final AtomicInteger incompleteOutputCount;
      private final ListenableFuture<? extends T>[] inputFutures;
      private volatile int delegateIndex;

      private InCompletionOrderState(ListenableFuture<? extends T>[] inputFutures) {
         this.wasCancelled = false;
         this.shouldInterrupt = true;
         this.delegateIndex = 0;
         this.inputFutures = inputFutures;
         this.incompleteOutputCount = new AtomicInteger(inputFutures.length);
      }

      private void recordOutputCancellation(boolean interruptIfRunning) {
         this.wasCancelled = true;
         if (!interruptIfRunning) {
            this.shouldInterrupt = false;
         }

         this.recordCompletion();
      }

      private void recordInputCompletion(ImmutableList<AbstractFuture<T>> delegates, int inputFutureIndex) {
         ListenableFuture<? extends T> inputFuture = (ListenableFuture)Objects.requireNonNull(this.inputFutures[inputFutureIndex]);
         this.inputFutures[inputFutureIndex] = null;

         for(int i = this.delegateIndex; i < delegates.size(); ++i) {
            if (((AbstractFuture)delegates.get(i)).setFuture(inputFuture)) {
               this.recordCompletion();
               this.delegateIndex = i + 1;
               return;
            }
         }

         this.delegateIndex = delegates.size();
      }

      private void recordCompletion() {
         if (this.incompleteOutputCount.decrementAndGet() == 0 && this.wasCancelled) {
            ListenableFuture[] var1 = this.inputFutures;
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               ListenableFuture<? extends T> toCancel = var1[var3];
               if (toCancel != null) {
                  toCancel.cancel(this.shouldInterrupt);
               }
            }
         }

      }

      // $FF: synthetic method
      InCompletionOrderState(ListenableFuture[] x0, Object x1) {
         this(x0);
      }
   }

   private static final class InCompletionOrderFuture<T> extends AbstractFuture<T> {
      @CheckForNull
      private Futures.InCompletionOrderState<T> state;

      private InCompletionOrderFuture(Futures.InCompletionOrderState<T> state) {
         this.state = state;
      }

      public boolean cancel(boolean interruptIfRunning) {
         Futures.InCompletionOrderState<T> localState = this.state;
         if (super.cancel(interruptIfRunning)) {
            ((Futures.InCompletionOrderState)Objects.requireNonNull(localState)).recordOutputCancellation(interruptIfRunning);
            return true;
         } else {
            return false;
         }
      }

      protected void afterDone() {
         this.state = null;
      }

      @CheckForNull
      protected String pendingToString() {
         Futures.InCompletionOrderState<T> localState = this.state;
         if (localState != null) {
            int var2 = localState.inputFutures.length;
            int var3 = localState.incompleteOutputCount.get();
            return (new StringBuilder(49)).append("inputCount=[").append(var2).append("], remaining=[").append(var3).append("]").toString();
         } else {
            return null;
         }
      }

      // $FF: synthetic method
      InCompletionOrderFuture(Futures.InCompletionOrderState x0, Object x1) {
         this(x0);
      }
   }

   private static final class NonCancellationPropagatingFuture<V> extends AbstractFuture.TrustedFuture<V> implements Runnable {
      @CheckForNull
      private ListenableFuture<V> delegate;

      NonCancellationPropagatingFuture(ListenableFuture<V> delegate) {
         this.delegate = delegate;
      }

      public void run() {
         ListenableFuture<V> localDelegate = this.delegate;
         if (localDelegate != null) {
            this.setFuture(localDelegate);
         }

      }

      @CheckForNull
      protected String pendingToString() {
         ListenableFuture<V> localDelegate = this.delegate;
         if (localDelegate != null) {
            String var2 = String.valueOf(localDelegate);
            return (new StringBuilder(11 + String.valueOf(var2).length())).append("delegate=[").append(var2).append("]").toString();
         } else {
            return null;
         }
      }

      protected void afterDone() {
         this.delegate = null;
      }
   }

   @Beta
   @CanIgnoreReturnValue
   @GwtCompatible
   public static final class FutureCombiner<V> {
      private final boolean allMustSucceed;
      private final ImmutableList<ListenableFuture<? extends V>> futures;

      private FutureCombiner(boolean allMustSucceed, ImmutableList<ListenableFuture<? extends V>> futures) {
         this.allMustSucceed = allMustSucceed;
         this.futures = futures;
      }

      public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner, Executor executor) {
         return new CombinedFuture(this.futures, this.allMustSucceed, executor, combiner);
      }

      @CanIgnoreReturnValue
      public <C> ListenableFuture<C> call(Callable<C> combiner, Executor executor) {
         return new CombinedFuture(this.futures, this.allMustSucceed, executor, combiner);
      }

      public ListenableFuture<?> run(final Runnable combiner, Executor executor) {
         return this.call(new Callable<Void>(this) {
            @CheckForNull
            public Void call() throws Exception {
               combiner.run();
               return null;
            }
         }, executor);
      }

      // $FF: synthetic method
      FutureCombiner(boolean x0, ImmutableList x1, Object x2) {
         this(x0, x1);
      }
   }
}
