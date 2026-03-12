package ac.grim.grimac.shaded.incendo.cloud.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL
)
public final class CompletableFutures {
   private CompletableFutures() {
   }

   @NonNull
   public static <T> CompletableFuture<T> failedFuture(@NonNull final Throwable throwable) {
      CompletableFuture<T> future = new CompletableFuture();
      future.completeExceptionally(throwable);
      return future;
   }

   public static <T> CompletableFuture<T> scheduleOn(final Executor executor, final Supplier<CompletableFuture<T>> futureSupplier) {
      return CompletableFuture.supplyAsync(futureSupplier, executor).thenCompose(Function.identity());
   }
}
