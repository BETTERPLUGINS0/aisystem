package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

@ElementTypesAreNonnullByDefault
@GwtCompatible
class ImmediateFuture<V> implements ListenableFuture<V> {
   static final ListenableFuture<?> NULL = new ImmediateFuture((Object)null);
   private static final Logger log = Logger.getLogger(ImmediateFuture.class.getName());
   @ParametricNullness
   private final V value;

   ImmediateFuture(@ParametricNullness V value) {
      this.value = value;
   }

   public void addListener(Runnable listener, Executor executor) {
      Preconditions.checkNotNull(listener, "Runnable was null.");
      Preconditions.checkNotNull(executor, "Executor was null.");

      try {
         executor.execute(listener);
      } catch (RuntimeException var6) {
         Logger var10000 = log;
         Level var10001 = Level.SEVERE;
         String var4 = String.valueOf(listener);
         String var5 = String.valueOf(executor);
         var10000.log(var10001, (new StringBuilder(57 + String.valueOf(var4).length() + String.valueOf(var5).length())).append("RuntimeException while executing runnable ").append(var4).append(" with executor ").append(var5).toString(), var6);
      }

   }

   public boolean cancel(boolean mayInterruptIfRunning) {
      return false;
   }

   @ParametricNullness
   public V get() {
      return this.value;
   }

   @ParametricNullness
   public V get(long timeout, TimeUnit unit) throws ExecutionException {
      Preconditions.checkNotNull(unit);
      return this.get();
   }

   public boolean isCancelled() {
      return false;
   }

   public boolean isDone() {
      return true;
   }

   public String toString() {
      String var1 = super.toString();
      String var2 = String.valueOf(this.value);
      return (new StringBuilder(27 + String.valueOf(var1).length() + String.valueOf(var2).length())).append(var1).append("[status=SUCCESS, result=[").append(var2).append("]]").toString();
   }

   static final class ImmediateCancelledFuture<V> extends AbstractFuture.TrustedFuture<V> {
      @Nullable
      static final ImmediateFuture.ImmediateCancelledFuture<Object> INSTANCE;

      ImmediateCancelledFuture() {
         this.cancel(false);
      }

      static {
         INSTANCE = AbstractFuture.GENERATE_CANCELLATION_CAUSES ? null : new ImmediateFuture.ImmediateCancelledFuture();
      }
   }

   static final class ImmediateFailedFuture<V> extends AbstractFuture.TrustedFuture<V> {
      ImmediateFailedFuture(Throwable thrown) {
         this.setException(thrown);
      }
   }
}
