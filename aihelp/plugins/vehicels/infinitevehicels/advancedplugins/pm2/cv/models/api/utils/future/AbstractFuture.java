package advancedplugins.pm2.cv.models.api.utils.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractFuture<T> implements Future<T> {
   @NotNull
   private final CompletableFuture<T> future;

   protected AbstractFuture() {
      this.future = new CompletableFuture();
   }

   protected AbstractFuture(T var1) {
      this.future = CompletableFuture.completedFuture(var1);
   }

   public Future<T> runSync(Runnable var1) {
      this.executeSync(var1);
      return this;
   }

   public Future<T> runSyncDelay(Runnable var1, int var2) {
      this.executeSync(var1, var2);
      return this;
   }

   public Future<T> runAsync(Runnable var1) {
      this.executeAsync(var1);
      return this;
   }

   public Future<T> runAsyncDelay(Runnable var1, int var2) {
      this.executeAsync(var1, var2);
      return this;
   }

   public Future<T> supplySync(Supplier<T> var1) {
      this.executeSync(() -> {
         this.future.complete(var1.get());
      });
      return this;
   }

   public Future<T> supplySyncDelay(Supplier<T> var1, int var2) {
      this.executeSync(() -> {
         this.future.complete(var1.get());
      }, var2);
      return this;
   }

   public Future<T> supplyAsync(Supplier<T> var1) {
      this.executeAsync(() -> {
         this.future.complete(var1.get());
      });
      return this;
   }

   public Future<T> supplyAsyncDelay(Supplier<T> var1, int var2) {
      this.executeAsync(() -> {
         this.future.complete(var1.get());
      }, var2);
      return this;
   }

   public <U> Future<U> thenApplySync(Function<? super T, ? extends U> var1) {
      AbstractFuture var2 = this.createEmpty();
      this.future.whenComplete((var2x, var3) -> {
         var2.executeSync(() -> {
            var2.future.complete(var1.apply(var2x));
         });
      });
      return var2;
   }

   public <U> Future<U> thenApplySyncDelay(Function<? super T, ? extends U> var1, int var2) {
      AbstractFuture var3 = this.createEmpty();
      this.future.whenComplete((var3x, var4) -> {
         var3.executeSync(() -> {
            var3.future.complete(var1.apply(var3x));
         }, var2);
      });
      return var3;
   }

   public <U> Future<U> thenApplyAsync(Function<? super T, ? extends U> var1) {
      AbstractFuture var2 = this.createEmpty();
      this.future.whenComplete((var2x, var3) -> {
         var2.executeAsync(() -> {
            var2.future.complete(var1.apply(var2x));
         });
      });
      return var2;
   }

   public <U> Future<U> thenApplyAsyncDelay(Function<? super T, ? extends U> var1, int var2) {
      AbstractFuture var3 = this.createEmpty();
      this.future.whenComplete((var3x, var4) -> {
         var3.executeAsync(() -> {
            var3.future.complete(var1.apply(var3x));
         }, var2);
      });
      return var3;
   }

   public boolean cancel(boolean var1) {
      return this.future.cancel(var1);
   }

   public boolean isCancelled() {
      return this.future.isCancelled();
   }

   public boolean isDone() {
      return this.future.isDone();
   }

   public T get() {
      return this.future.get();
   }

   public T get(long var1, TimeUnit var3) {
      return this.future.get(var1, var3);
   }

   protected abstract <U> AbstractFuture<U> createEmpty();

   protected abstract void executeSync(Runnable var1);

   protected abstract void executeSync(Runnable var1, int var2);

   protected abstract void executeAsync(Runnable var1);

   protected abstract void executeAsync(Runnable var1, int var2);
}
