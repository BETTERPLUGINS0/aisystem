package com.volmit.iris.util.data;

import com.volmit.iris.engine.framework.MeteredCache;
import com.volmit.iris.util.caffeine.cache.CacheLoader;
import com.volmit.iris.util.caffeine.cache.Caffeine;
import com.volmit.iris.util.caffeine.cache.LoadingCache;
import com.volmit.iris.util.caffeine.cache.Scheduler;
import com.volmit.iris.util.math.RollingSequence;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KCache<K, V> implements MeteredCache {
   public static final ExecutorService EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();
   private final long max;
   private final LoadingCache<K, V> cache;
   private final boolean fastDump;
   private final RollingSequence msu;
   private CacheLoader<K, V> loader;

   public KCache(CacheLoader<K, V> loader, long max) {
      this(var1, var2, false);
   }

   public KCache(CacheLoader<K, V> loader, long max, boolean fastDump) {
      this.msu = new RollingSequence(100);
      this.max = var2;
      this.fastDump = var4;
      this.loader = var1;
      this.cache = this.create(var1);
   }

   private LoadingCache<K, V> create(CacheLoader<K, V> loader) {
      return Caffeine.newBuilder().maximumSize(this.max).scheduler(Scheduler.systemScheduler()).executor(EXECUTOR).initialCapacity((int)this.max).build((var1x) -> {
         return var1 == null ? null : var1.load(var1x);
      });
   }

   public void setLoader(CacheLoader<K, V> loader) {
      this.loader = var1;
   }

   public void invalidate(K k) {
      this.cache.invalidate(var1);
   }

   public void invalidate() {
      this.cache.invalidateAll();
   }

   public V get(K k) {
      return this.cache.get(var1);
   }

   public long getSize() {
      return this.cache.estimatedSize();
   }

   public KCache<?, ?> getRawCache() {
      return this;
   }

   public long getMaxSize() {
      return this.max;
   }

   public boolean isClosed() {
      return false;
   }

   public boolean contains(K next) {
      return this.cache.getIfPresent(var1) != null;
   }
}
