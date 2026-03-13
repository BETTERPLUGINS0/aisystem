package org.terraform.utils.datastructs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ConcurrentLRUCache<K, V> {
   private final ThreadLocal<HashMap<K, ConcurrentLRUCache.LRUNode<K, V>>> localCache = ThreadLocal.withInitial(HashMap::new);
   private final int maxSize;
   private final int localCacheSize;
   private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
   private final Lock readLock;
   private final Lock writeLock;
   private final HashMap<K, ConcurrentLRUCache.LRUNode<K, V>> keyToValue;
   private final Function<K, V> generator;
   private final String name;

   public ConcurrentLRUCache(String name, int maxSize, Function<K, V> generator) {
      this.readLock = this.rwlock.readLock();
      this.writeLock = this.rwlock.writeLock();
      this.name = name;
      this.maxSize = maxSize;
      this.localCacheSize = 5;
      this.generator = generator;
      this.keyToValue = new HashMap(maxSize);
   }

   public ConcurrentLRUCache(String name, int maxSize, int localMaxSize, Function<K, V> generator) {
      this.readLock = this.rwlock.readLock();
      this.writeLock = this.rwlock.writeLock();
      this.name = name;
      this.maxSize = maxSize;
      this.localCacheSize = localMaxSize;
      this.generator = generator;
      this.keyToValue = new HashMap(maxSize);
   }

   private ConcurrentLRUCache.LRUNode<K, V> slowPathGet(K key) {
      ConcurrentLRUCache.LRUNode<K, V> node = null;
      this.readLock.lock();

      try {
         node = (ConcurrentLRUCache.LRUNode)this.keyToValue.get(key);
         if (node != null) {
            node.lastAccess.lazySet(System.nanoTime());
         }
      } finally {
         this.readLock.unlock();
         if (node == null) {
            node = this.calculateAndInsert(key);
         }

      }

      return node;
   }

   public V get(K key) {
      HashMap<K, ConcurrentLRUCache.LRUNode<K, V>> localMap = (HashMap)this.localCache.get();
      ConcurrentLRUCache.LRUNode<K, V> node = (ConcurrentLRUCache.LRUNode)localMap.get(key);
      if (node == null) {
         node = this.slowPathGet(key);
         if (localMap.size() >= this.localCacheSize) {
            localMap.clear();
         }

         localMap.put(key, node);
      } else {
         node.lastAccess.lazySet(System.nanoTime());
      }

      return node.value;
   }

   private ConcurrentLRUCache.LRUNode<K, V> calculateAndInsert(K key) {
      this.writeLock.lock();

      ConcurrentLRUCache.LRUNode node;
      try {
         node = (ConcurrentLRUCache.LRUNode)this.keyToValue.get(key);
         if (node == null) {
            if (this.keyToValue.size() >= this.maxSize) {
               this.pruneLRU();
            }

            node = new ConcurrentLRUCache.LRUNode(key, this.generator.apply(key));
            this.keyToValue.put(key, node);
         }
      } finally {
         this.writeLock.unlock();
      }

      return node;
   }

   private void pruneLRU() {
      ArrayList<ConcurrentLRUCache.LRUNode<K, V>> nodes = new ArrayList(this.keyToValue.values());
      long min = Long.MAX_VALUE;
      long max = Long.MIN_VALUE;

      long cmp;
      for(Iterator var6 = nodes.iterator(); var6.hasNext(); max = Math.max(cmp, max)) {
         ConcurrentLRUCache.LRUNode<K, V> node = (ConcurrentLRUCache.LRUNode)var6.next();
         cmp = node.snap();
         min = Math.min(cmp, min);
      }

      long midPoint = (min + max) / 2L;
      Iterator var11 = nodes.iterator();

      while(var11.hasNext()) {
         ConcurrentLRUCache.LRUNode<K, V> node = (ConcurrentLRUCache.LRUNode)var11.next();
         if (node.snapshot < midPoint) {
            this.keyToValue.remove(node.key);
         }
      }

   }

   private static final class LRUNode<K, V> {
      @NotNull
      public final K key;
      @Nullable
      public final V value;
      @NotNull
      public final AtomicLong lastAccess = new AtomicLong(System.nanoTime());
      private long snapshot;

      public LRUNode(@NotNull K k, @Nullable V v) {
         this.key = k;
         this.value = v;
      }

      public long snap() {
         this.snapshot = this.lastAccess.get();
         return this.snapshot;
      }
   }
}
