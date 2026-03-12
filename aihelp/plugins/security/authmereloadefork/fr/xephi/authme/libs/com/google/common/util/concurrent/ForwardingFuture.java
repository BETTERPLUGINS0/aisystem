package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.ForwardingObject;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@ElementTypesAreNonnullByDefault
@CanIgnoreReturnValue
@GwtCompatible
public abstract class ForwardingFuture<V> extends ForwardingObject implements Future<V> {
   protected ForwardingFuture() {
   }

   protected abstract Future<? extends V> delegate();

   public boolean cancel(boolean mayInterruptIfRunning) {
      return this.delegate().cancel(mayInterruptIfRunning);
   }

   public boolean isCancelled() {
      return this.delegate().isCancelled();
   }

   public boolean isDone() {
      return this.delegate().isDone();
   }

   @ParametricNullness
   public V get() throws InterruptedException, ExecutionException {
      return this.delegate().get();
   }

   @ParametricNullness
   public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
      return this.delegate().get(timeout, unit);
   }

   public abstract static class SimpleForwardingFuture<V> extends ForwardingFuture<V> {
      private final Future<V> delegate;

      protected SimpleForwardingFuture(Future<V> delegate) {
         this.delegate = (Future)Preconditions.checkNotNull(delegate);
      }

      protected final Future<V> delegate() {
         return this.delegate;
      }
   }
}
