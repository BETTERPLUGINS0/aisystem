package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableCollection;
import fr.xephi.authme.libs.com.google.common.collect.UnmodifiableIterator;
import fr.xephi.authme.libs.com.google.errorprone.annotations.ForOverride;
import fr.xephi.authme.libs.com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AggregateFuture<InputT, OutputT> extends AggregateFutureState<OutputT> {
   private static final Logger logger = Logger.getLogger(AggregateFuture.class.getName());
   @CheckForNull
   private ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures;
   private final boolean allMustSucceed;
   private final boolean collectsValues;

   AggregateFuture(ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures, boolean allMustSucceed, boolean collectsValues) {
      super(futures.size());
      this.futures = (ImmutableCollection)Preconditions.checkNotNull(futures);
      this.allMustSucceed = allMustSucceed;
      this.collectsValues = collectsValues;
   }

   protected final void afterDone() {
      super.afterDone();
      ImmutableCollection<? extends Future<?>> localFutures = this.futures;
      this.releaseResources(AggregateFuture.ReleaseResourcesReason.OUTPUT_FUTURE_DONE);
      if (this.isCancelled() & localFutures != null) {
         boolean wasInterrupted = this.wasInterrupted();
         UnmodifiableIterator var3 = localFutures.iterator();

         while(var3.hasNext()) {
            Future<?> future = (Future)var3.next();
            future.cancel(wasInterrupted);
         }
      }

   }

   @CheckForNull
   protected final String pendingToString() {
      ImmutableCollection<? extends Future<?>> localFutures = this.futures;
      if (localFutures != null) {
         String var2 = String.valueOf(localFutures);
         return (new StringBuilder(8 + String.valueOf(var2).length())).append("futures=").append(var2).toString();
      } else {
         return super.pendingToString();
      }
   }

   final void init() {
      Objects.requireNonNull(this.futures);
      if (this.futures.isEmpty()) {
         this.handleAllCompleted();
      } else {
         if (this.allMustSucceed) {
            int i = 0;
            UnmodifiableIterator var2 = this.futures.iterator();

            while(var2.hasNext()) {
               ListenableFuture<? extends InputT> future = (ListenableFuture)var2.next();
               int index = i++;
               future.addListener(() -> {
                  try {
                     if (future.isCancelled()) {
                        this.futures = null;
                        this.cancel(false);
                     } else {
                        this.collectValueFromNonCancelledFuture(index, future);
                     }
                  } finally {
                     this.decrementCountAndMaybeComplete((ImmutableCollection)null);
                  }

               }, MoreExecutors.directExecutor());
            }
         } else {
            ImmutableCollection<? extends Future<? extends InputT>> localFutures = this.collectsValues ? this.futures : null;
            Runnable listener = () -> {
               this.decrementCountAndMaybeComplete(localFutures);
            };
            UnmodifiableIterator var7 = this.futures.iterator();

            while(var7.hasNext()) {
               ListenableFuture<? extends InputT> future = (ListenableFuture)var7.next();
               future.addListener(listener, MoreExecutors.directExecutor());
            }
         }

      }
   }

   private void handleException(Throwable throwable) {
      Preconditions.checkNotNull(throwable);
      if (this.allMustSucceed) {
         boolean completedWithFailure = this.setException(throwable);
         if (!completedWithFailure) {
            boolean firstTimeSeeingThisException = addCausalChain(this.getOrInitSeenExceptions(), throwable);
            if (firstTimeSeeingThisException) {
               log(throwable);
               return;
            }
         }
      }

      if (throwable instanceof Error) {
         log(throwable);
      }

   }

   private static void log(Throwable throwable) {
      String message = throwable instanceof Error ? "Input Future failed with Error" : "Got more than one input Future failure. Logging failures after the first";
      logger.log(Level.SEVERE, message, throwable);
   }

   final void addInitialException(Set<Throwable> seen) {
      Preconditions.checkNotNull(seen);
      if (!this.isCancelled()) {
         addCausalChain(seen, (Throwable)Objects.requireNonNull(this.tryInternalFastPathGetFailure()));
      }

   }

   private void collectValueFromNonCancelledFuture(int index, Future<? extends InputT> future) {
      try {
         this.collectOneValue(index, Futures.getDone(future));
      } catch (ExecutionException var4) {
         this.handleException(var4.getCause());
      } catch (Throwable var5) {
         this.handleException(var5);
      }

   }

   private void decrementCountAndMaybeComplete(@CheckForNull ImmutableCollection<? extends Future<? extends InputT>> futuresIfNeedToCollectAtCompletion) {
      int newRemaining = this.decrementRemainingAndGet();
      Preconditions.checkState(newRemaining >= 0, "Less than 0 remaining futures");
      if (newRemaining == 0) {
         this.processCompleted(futuresIfNeedToCollectAtCompletion);
      }

   }

   private void processCompleted(@CheckForNull ImmutableCollection<? extends Future<? extends InputT>> futuresIfNeedToCollectAtCompletion) {
      if (futuresIfNeedToCollectAtCompletion != null) {
         int i = 0;

         for(UnmodifiableIterator var3 = futuresIfNeedToCollectAtCompletion.iterator(); var3.hasNext(); ++i) {
            Future<? extends InputT> future = (Future)var3.next();
            if (!future.isCancelled()) {
               this.collectValueFromNonCancelledFuture(i, future);
            }
         }
      }

      this.clearSeenExceptions();
      this.handleAllCompleted();
      this.releaseResources(AggregateFuture.ReleaseResourcesReason.ALL_INPUT_FUTURES_PROCESSED);
   }

   @ForOverride
   @OverridingMethodsMustInvokeSuper
   void releaseResources(AggregateFuture.ReleaseResourcesReason reason) {
      Preconditions.checkNotNull(reason);
      this.futures = null;
   }

   abstract void collectOneValue(int var1, @ParametricNullness InputT var2);

   abstract void handleAllCompleted();

   private static boolean addCausalChain(Set<Throwable> seen, Throwable param) {
      for(Throwable t = param; t != null; t = t.getCause()) {
         boolean firstTimeSeen = seen.add(t);
         if (!firstTimeSeen) {
            return false;
         }
      }

      return true;
   }

   static enum ReleaseResourcesReason {
      OUTPUT_FUTURE_DONE,
      ALL_INPUT_FUTURES_PROCESSED;

      // $FF: synthetic method
      private static AggregateFuture.ReleaseResourcesReason[] $values() {
         return new AggregateFuture.ReleaseResourcesReason[]{OUTPUT_FUTURE_DONE, ALL_INPUT_FUTURES_PROCESSED};
      }
   }
}
