package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

abstract class LocalAsyncLoadingCache<K, V> implements LocalAsyncCache<K, V>, AsyncLoadingCache<K, V> {
   static final Logger logger = Logger.getLogger(LocalAsyncLoadingCache.class.getName());
   final boolean canBulkLoad;
   final AsyncCacheLoader<K, V> loader;
   @Nullable
   LocalAsyncLoadingCache.LoadingCacheView<K, V> cacheView;

   LocalAsyncLoadingCache(AsyncCacheLoader<? super K, V> loader) {
      this.loader = loader;
      this.canBulkLoad = canBulkLoad(loader);
   }

   private static boolean canBulkLoad(AsyncCacheLoader<?, ?> loader) {
      try {
         Class<?> defaultLoaderClass = AsyncCacheLoader.class;
         Method classLoadAll;
         Method defaultLoadAll;
         if (loader instanceof CacheLoader) {
            defaultLoaderClass = CacheLoader.class;
            classLoadAll = loader.getClass().getMethod("loadAll", Iterable.class);
            defaultLoadAll = CacheLoader.class.getMethod("loadAll", Iterable.class);
            if (!classLoadAll.equals(defaultLoadAll)) {
               return true;
            }
         }

         classLoadAll = loader.getClass().getMethod("asyncLoadAll", Iterable.class, Executor.class);
         defaultLoadAll = defaultLoaderClass.getMethod("asyncLoadAll", Iterable.class, Executor.class);
         return !classLoadAll.equals(defaultLoadAll);
      } catch (SecurityException | NoSuchMethodException var4) {
         logger.log(Level.WARNING, "Cannot determine if CacheLoader can bulk load", var4);
         return false;
      }
   }

   public CompletableFuture<V> get(K key) {
      AsyncCacheLoader var10002 = this.loader;
      Objects.requireNonNull(var10002);
      return this.get(key, var10002::asyncLoad);
   }

   public CompletableFuture<Map<K, V>> getAll(Iterable<? extends K> keys) {
      if (this.canBulkLoad) {
         AsyncCacheLoader var10002 = this.loader;
         Objects.requireNonNull(var10002);
         return this.getAll(keys, var10002::asyncLoadAll);
      } else {
         Map<K, CompletableFuture<V>> result = new LinkedHashMap();
         Function<K, CompletableFuture<V>> mappingFunction = this::get;
         Iterator var4 = keys.iterator();

         while(var4.hasNext()) {
            K key = var4.next();
            CompletableFuture<V> future = (CompletableFuture)result.computeIfAbsent(key, mappingFunction);
            Objects.requireNonNull(future);
         }

         return this.composeResult(result);
      }
   }

   public LoadingCache<K, V> synchronous() {
      return this.cacheView == null ? (this.cacheView = new LocalAsyncLoadingCache.LoadingCacheView(this)) : this.cacheView;
   }

   static final class LoadingCacheView<K, V> extends LocalAsyncCache.AbstractCacheView<K, V> implements LoadingCache<K, V> {
      private static final long serialVersionUID = 1L;
      final LocalAsyncLoadingCache<K, V> asyncCache;

      LoadingCacheView(LocalAsyncLoadingCache<K, V> asyncCache) {
         this.asyncCache = (LocalAsyncLoadingCache)Objects.requireNonNull(asyncCache);
      }

      LocalAsyncLoadingCache<K, V> asyncCache() {
         return this.asyncCache;
      }

      public V get(K key) {
         return resolve(this.asyncCache.get(key));
      }

      public Map<K, V> getAll(Iterable<? extends K> keys) {
         return (Map)resolve(this.asyncCache.getAll(keys));
      }

      public void refresh(K key) {
         Objects.requireNonNull(key);
         long[] writeTime = new long[1];
         CompletableFuture<V> oldValueFuture = (CompletableFuture)this.asyncCache.cache().getIfPresentQuietly(key, writeTime);
         if (oldValueFuture != null && (!oldValueFuture.isDone() || !oldValueFuture.isCompletedExceptionally())) {
            if (oldValueFuture.isDone()) {
               oldValueFuture.thenAccept((oldValue) -> {
                  long now = this.asyncCache.cache().statsTicker().read();
                  CompletableFuture<V> refreshFuture = oldValue == null ? this.asyncCache.loader.asyncLoad(key, this.asyncCache.cache().executor()) : this.asyncCache.loader.asyncReload(key, oldValue, this.asyncCache.cache().executor());
                  refreshFuture.whenComplete((newValue, error) -> {
                     long loadTime = this.asyncCache.cache().statsTicker().read() - now;
                     if (error != null) {
                        this.asyncCache.cache().statsCounter().recordLoadFailure(loadTime);
                        if (!(error instanceof CancellationException) && !(error instanceof TimeoutException)) {
                           LocalAsyncLoadingCache.logger.log(Level.WARNING, "Exception thrown during refresh", error);
                        }

                     } else {
                        boolean[] discard = new boolean[1];
                        this.asyncCache.cache().compute(key, (k, currentValue) -> {
                           if (currentValue == null) {
                              return newValue == null ? null : refreshFuture;
                           } else {
                              if (currentValue == oldValueFuture) {
                                 long expectedWriteTime = writeTime[0];
                                 if (this.asyncCache.cache().hasWriteTime()) {
                                    this.asyncCache.cache().getIfPresentQuietly(key, writeTime);
                                 }

                                 if (writeTime[0] == expectedWriteTime) {
                                    return newValue == null ? null : refreshFuture;
                                 }
                              }

                              discard[0] = true;
                              return currentValue;
                           }
                        }, false, false, true);
                        if (discard[0] && this.asyncCache.cache().hasRemovalListener()) {
                           this.asyncCache.cache().notifyRemoval(key, refreshFuture, RemovalCause.REPLACED);
                        }

                        if (newValue == null) {
                           this.asyncCache.cache().statsCounter().recordLoadFailure(loadTime);
                        } else {
                           this.asyncCache.cache().statsCounter().recordLoadSuccess(loadTime);
                        }

                     }
                  });
               });
            }
         } else {
            LocalAsyncLoadingCache var10000 = this.asyncCache;
            AsyncCacheLoader var10002 = this.asyncCache.loader;
            Objects.requireNonNull(var10002);
            var10000.get(key, var10002::asyncLoad, false);
         }
      }
   }
}
