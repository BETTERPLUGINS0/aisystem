package advancedplugins.pm2.cv.models.api.utils.future;

import advancedplugins.pm2.cv.models.api.ServerInfo;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface Future<T> extends java.util.concurrent.Future<T> {
   static <U> Future<U> empty() {
      return (Future)(ServerInfo.IS_FOLIA ? GlobalFuture.empty() : LegacyFuture.empty());
   }

   static <U> Future<U> empty(Entity entity) {
      return (Future)(ServerInfo.IS_FOLIA ? EntityFuture.empty(entity) : empty());
   }

   static <U> Future<U> empty(Location location) {
      return (Future)(ServerInfo.IS_FOLIA ? RegionFuture.empty(location) : empty());
   }

   static Future<Void> start() {
      return (Future)(ServerInfo.IS_FOLIA ? GlobalFuture.completed((Object)null) : LegacyFuture.completed((Object)null));
   }

   static Future<Void> start(Entity entity) {
      return (Future)(ServerInfo.IS_FOLIA ? EntityFuture.completed(entity, (Object)null) : start());
   }

   static Future<Void> start(Location location) {
      return (Future)(ServerInfo.IS_FOLIA ? RegionFuture.completed(location, (Object)null) : start());
   }

   static <U> Future<U> completed(@Nullable U value) {
      return (Future)(ServerInfo.IS_FOLIA ? GlobalFuture.completed(value) : LegacyFuture.completed(value));
   }

   static <U> Future<U> completed(Entity entity, @Nullable U value) {
      return (Future)(ServerInfo.IS_FOLIA ? EntityFuture.completed(entity, value) : completed(value));
   }

   static <U> Future<U> completed(Location location, @Nullable U value) {
      return (Future)(ServerInfo.IS_FOLIA ? RegionFuture.completed(location, value) : completed(value));
   }

   static <U> Future<U> supplyingSync(Supplier<U> supplier) {
      Future<U> future = empty();
      return future.supplySync(supplier);
   }

   static <U> Future<U> supplyingSync(Entity entity, Supplier<U> supplier) {
      Future<U> future = empty(entity);
      return future.supplySync(supplier);
   }

   static <U> Future<U> supplyingSync(Location location, Supplier<U> supplier) {
      Future<U> future = empty(location);
      return future.supplySync(supplier);
   }

   static <U> Future<U> supplyingSyncDelay(Supplier<U> supplier, int delay) {
      Future<U> future = empty();
      return future.supplySyncDelay(supplier, delay);
   }

   static <U> Future<U> supplyingSyncDelay(Entity entity, Supplier<U> supplier, int delay) {
      Future<U> future = empty(entity);
      return future.supplySyncDelay(supplier, delay);
   }

   static <U> Future<U> supplyingSyncDelay(Location location, Supplier<U> supplier, int delay) {
      Future<U> future = empty(location);
      return future.supplySyncDelay(supplier, delay);
   }

   static <U> Future<U> supplyingAsync(Supplier<U> supplier) {
      Future<U> future = empty();
      return future.supplyAsync(supplier);
   }

   static <U> Future<U> supplyingAsyncDelay(Supplier<U> supplier, int delay) {
      Future<U> future = empty();
      return future.supplyAsyncDelay(supplier, delay);
   }

   Future<T> runSync(Runnable var1);

   Future<T> runSyncDelay(Runnable var1, int var2);

   Future<T> runAsync(Runnable var1);

   Future<T> runAsyncDelay(Runnable var1, int var2);

   Future<T> supplySync(Supplier<T> var1);

   Future<T> supplySyncDelay(Supplier<T> var1, int var2);

   Future<T> supplyAsync(Supplier<T> var1);

   Future<T> supplyAsyncDelay(Supplier<T> var1, int var2);

   default Future<Void> thenRunSync(Runnable runnable) {
      return this.thenApplySync((t) -> {
         runnable.run();
         return null;
      });
   }

   default Future<Void> thenRunSyncDelay(Runnable runnable, int delay) {
      return this.thenApplySyncDelay((t) -> {
         runnable.run();
         return null;
      }, delay);
   }

   default Future<Void> thenRunAsync(Runnable runnable) {
      return this.thenApplyAsync((t) -> {
         runnable.run();
         return null;
      });
   }

   default Future<Void> thenRunAsyncDelay(Runnable runnable, int delay) {
      return this.thenApplyAsyncDelay((t) -> {
         runnable.run();
         return null;
      }, delay);
   }

   <U> Future<U> thenApplySync(Function<? super T, ? extends U> var1);

   <U> Future<U> thenApplySyncDelay(Function<? super T, ? extends U> var1, int var2);

   <U> Future<U> thenApplyAsync(Function<? super T, ? extends U> var1);

   <U> Future<U> thenApplyAsyncDelay(Function<? super T, ? extends U> var1, int var2);

   default Future<Void> thenAcceptSync(Consumer<? super T> consumer) {
      return this.thenApplySync((t) -> {
         consumer.accept(t);
         return null;
      });
   }

   default Future<Void> thenAcceptSyncDelay(Consumer<? super T> consumer, int delay) {
      return this.thenApplySyncDelay((t) -> {
         consumer.accept(t);
         return null;
      }, delay);
   }

   default Future<Void> thenAcceptAsync(Consumer<? super T> consumer) {
      return this.thenApplyAsync((t) -> {
         consumer.accept(t);
         return null;
      });
   }

   default Future<Void> thenAcceptAsyncDelay(Consumer<? super T> consumer, int delay) {
      return this.thenApplyAsyncDelay((t) -> {
         consumer.accept(t);
         return null;
      }, delay);
   }
}
