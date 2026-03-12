package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import fr.xephi.authme.libs.com.google.common.util.concurrent.internal.InternalFutures;
import fr.xephi.authme.libs.com.google.errorprone.annotations.ForOverride;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AbstractCatchingFuture<V, X extends Throwable, F, T> extends FluentFuture.TrustedFuture<V> implements Runnable {
   @CheckForNull
   ListenableFuture<? extends V> inputFuture;
   @CheckForNull
   Class<X> exceptionType;
   @CheckForNull
   F fallback;

   static <V, X extends Throwable> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor) {
      AbstractCatchingFuture.CatchingFuture<V, X> future = new AbstractCatchingFuture.CatchingFuture(input, exceptionType, fallback);
      input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
      return future;
   }

   static <X extends Throwable, V> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor) {
      AbstractCatchingFuture.AsyncCatchingFuture<V, X> future = new AbstractCatchingFuture.AsyncCatchingFuture(input, exceptionType, fallback);
      input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
      return future;
   }

   AbstractCatchingFuture(ListenableFuture<? extends V> inputFuture, Class<X> exceptionType, F fallback) {
      this.inputFuture = (ListenableFuture)Preconditions.checkNotNull(inputFuture);
      this.exceptionType = (Class)Preconditions.checkNotNull(exceptionType);
      this.fallback = Preconditions.checkNotNull(fallback);
   }

   public final void run() {
      ListenableFuture<? extends V> localInputFuture = this.inputFuture;
      Class<X> localExceptionType = this.exceptionType;
      F localFallback = this.fallback;
      if (!(localInputFuture == null | localExceptionType == null | localFallback == null) && !this.isCancelled()) {
         this.inputFuture = null;
         V sourceResult = null;
         Object throwable = null;

         try {
            if (localInputFuture instanceof InternalFutureFailureAccess) {
               throwable = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess)localInputFuture);
            }

            if (throwable == null) {
               sourceResult = Futures.getDone(localInputFuture);
            }
         } catch (ExecutionException var16) {
            throwable = var16.getCause();
            if (throwable == null) {
               String var7 = String.valueOf(localInputFuture.getClass());
               String var8 = String.valueOf(var16.getClass());
               throwable = new NullPointerException((new StringBuilder(35 + String.valueOf(var7).length() + String.valueOf(var8).length())).append("Future type ").append(var7).append(" threw ").append(var8).append(" without a cause").toString());
            }
         } catch (Throwable var17) {
            throwable = var17;
         }

         if (throwable == null) {
            this.set(NullnessCasts.uncheckedCastNullableTToT(sourceResult));
         } else if (!Platform.isInstanceOfThrowableClass((Throwable)throwable, localExceptionType)) {
            this.setFuture(localInputFuture);
         } else {
            Object castThrowable = throwable;

            Object fallbackResult;
            label121: {
               try {
                  fallbackResult = this.doFallback(localFallback, (Throwable)castThrowable);
                  break label121;
               } catch (Throwable var14) {
                  this.setException(var14);
               } finally {
                  this.exceptionType = null;
                  this.fallback = null;
               }

               return;
            }

            this.setResult(fallbackResult);
         }
      }
   }

   @CheckForNull
   protected String pendingToString() {
      ListenableFuture<? extends V> localInputFuture = this.inputFuture;
      Class<X> localExceptionType = this.exceptionType;
      F localFallback = this.fallback;
      String superString = super.pendingToString();
      String resultString = "";
      if (localInputFuture != null) {
         String var6 = String.valueOf(localInputFuture);
         resultString = (new StringBuilder(16 + String.valueOf(var6).length())).append("inputFuture=[").append(var6).append("], ").toString();
      }

      if (localExceptionType != null && localFallback != null) {
         String var7 = String.valueOf(localExceptionType);
         String var8 = String.valueOf(localFallback);
         return (new StringBuilder(29 + String.valueOf(resultString).length() + String.valueOf(var7).length() + String.valueOf(var8).length())).append(resultString).append("exceptionType=[").append(var7).append("], fallback=[").append(var8).append("]").toString();
      } else if (superString != null) {
         String var10000 = String.valueOf(resultString);
         String var10001 = String.valueOf(superString);
         if (var10001.length() != 0) {
            var10000 = var10000.concat(var10001);
         } else {
            String var10002 = new String;
            var10001 = var10000;
            var10000 = var10002;
            var10002.<init>(var10001);
         }

         return var10000;
      } else {
         return null;
      }
   }

   @ParametricNullness
   @ForOverride
   abstract T doFallback(F var1, X var2) throws Exception;

   @ForOverride
   abstract void setResult(@ParametricNullness T var1);

   protected final void afterDone() {
      this.maybePropagateCancellationTo(this.inputFuture);
      this.inputFuture = null;
      this.exceptionType = null;
      this.fallback = null;
   }

   private static final class CatchingFuture<V, X extends Throwable> extends AbstractCatchingFuture<V, X, Function<? super X, ? extends V>, V> {
      CatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback) {
         super(input, exceptionType, fallback);
      }

      @ParametricNullness
      V doFallback(Function<? super X, ? extends V> fallback, X cause) throws Exception {
         return fallback.apply(cause);
      }

      void setResult(@ParametricNullness V result) {
         this.set(result);
      }
   }

   private static final class AsyncCatchingFuture<V, X extends Throwable> extends AbstractCatchingFuture<V, X, AsyncFunction<? super X, ? extends V>, ListenableFuture<? extends V>> {
      AsyncCatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback) {
         super(input, exceptionType, fallback);
      }

      ListenableFuture<? extends V> doFallback(AsyncFunction<? super X, ? extends V> fallback, X cause) throws Exception {
         ListenableFuture<? extends V> replacement = fallback.apply(cause);
         Preconditions.checkNotNull(replacement, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", (Object)fallback);
         return replacement;
      }

      void setResult(ListenableFuture<? extends V> result) {
         this.setFuture(result);
      }
   }
}
