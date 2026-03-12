package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.ForOverride;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AbstractTransformFuture<I, O, F, T> extends FluentFuture.TrustedFuture<O> implements Runnable {
   @CheckForNull
   ListenableFuture<? extends I> inputFuture;
   @CheckForNull
   F function;

   static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
      Preconditions.checkNotNull(executor);
      AbstractTransformFuture.AsyncTransformFuture<I, O> output = new AbstractTransformFuture.AsyncTransformFuture(input, function);
      input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
      return output;
   }

   static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
      Preconditions.checkNotNull(function);
      AbstractTransformFuture.TransformFuture<I, O> output = new AbstractTransformFuture.TransformFuture(input, function);
      input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
      return output;
   }

   AbstractTransformFuture(ListenableFuture<? extends I> inputFuture, F function) {
      this.inputFuture = (ListenableFuture)Preconditions.checkNotNull(inputFuture);
      this.function = Preconditions.checkNotNull(function);
   }

   public final void run() {
      ListenableFuture<? extends I> localInputFuture = this.inputFuture;
      F localFunction = this.function;
      if (!(this.isCancelled() | localInputFuture == null | localFunction == null)) {
         this.inputFuture = null;
         if (localInputFuture.isCancelled()) {
            this.setFuture(localInputFuture);
         } else {
            Object sourceResult;
            try {
               sourceResult = Futures.getDone(localInputFuture);
            } catch (CancellationException var13) {
               this.cancel(false);
               return;
            } catch (ExecutionException var14) {
               this.setException(var14.getCause());
               return;
            } catch (RuntimeException var15) {
               this.setException(var15);
               return;
            } catch (Error var16) {
               this.setException(var16);
               return;
            }

            Object transformResult;
            label85: {
               try {
                  transformResult = this.doTransform(localFunction, sourceResult);
                  break label85;
               } catch (Throwable var17) {
                  this.setException(var17);
               } finally {
                  this.function = null;
               }

               return;
            }

            this.setResult(transformResult);
         }
      }
   }

   @ParametricNullness
   @ForOverride
   abstract T doTransform(F var1, @ParametricNullness I var2) throws Exception;

   @ForOverride
   abstract void setResult(@ParametricNullness T var1);

   protected final void afterDone() {
      this.maybePropagateCancellationTo(this.inputFuture);
      this.inputFuture = null;
      this.function = null;
   }

   @CheckForNull
   protected String pendingToString() {
      ListenableFuture<? extends I> localInputFuture = this.inputFuture;
      F localFunction = this.function;
      String superString = super.pendingToString();
      String resultString = "";
      if (localInputFuture != null) {
         String var5 = String.valueOf(localInputFuture);
         resultString = (new StringBuilder(16 + String.valueOf(var5).length())).append("inputFuture=[").append(var5).append("], ").toString();
      }

      if (localFunction != null) {
         String var6 = String.valueOf(localFunction);
         return (new StringBuilder(11 + String.valueOf(resultString).length() + String.valueOf(var6).length())).append(resultString).append("function=[").append(var6).append("]").toString();
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

   private static final class TransformFuture<I, O> extends AbstractTransformFuture<I, O, Function<? super I, ? extends O>, O> {
      TransformFuture(ListenableFuture<? extends I> inputFuture, Function<? super I, ? extends O> function) {
         super(inputFuture, function);
      }

      @ParametricNullness
      O doTransform(Function<? super I, ? extends O> function, @ParametricNullness I input) {
         return function.apply(input);
      }

      void setResult(@ParametricNullness O result) {
         this.set(result);
      }
   }

   private static final class AsyncTransformFuture<I, O> extends AbstractTransformFuture<I, O, AsyncFunction<? super I, ? extends O>, ListenableFuture<? extends O>> {
      AsyncTransformFuture(ListenableFuture<? extends I> inputFuture, AsyncFunction<? super I, ? extends O> function) {
         super(inputFuture, function);
      }

      ListenableFuture<? extends O> doTransform(AsyncFunction<? super I, ? extends O> function, @ParametricNullness I input) throws Exception {
         ListenableFuture<? extends O> outputFuture = function.apply(input);
         Preconditions.checkNotNull(outputFuture, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", (Object)function);
         return outputFuture;
      }

      void setResult(ListenableFuture<? extends O> result) {
         this.setFuture(result);
      }
   }
}
