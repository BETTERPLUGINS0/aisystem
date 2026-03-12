package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Callable;

@ElementTypesAreNonnullByDefault
@CanIgnoreReturnValue
@GwtIncompatible
public abstract class ForwardingListeningExecutorService extends ForwardingExecutorService implements ListeningExecutorService {
   protected ForwardingListeningExecutorService() {
   }

   protected abstract ListeningExecutorService delegate();

   public <T> ListenableFuture<T> submit(Callable<T> task) {
      return this.delegate().submit(task);
   }

   public ListenableFuture<?> submit(Runnable task) {
      return this.delegate().submit(task);
   }

   public <T> ListenableFuture<T> submit(Runnable task, @ParametricNullness T result) {
      return this.delegate().submit(task, result);
   }
}
