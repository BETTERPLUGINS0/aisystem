package me.PM2.infinitevehicles.commands.lib.expiringmap;

import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import me.PM2.infinitevehicles.commands.lib.expiringmap.internal.Assert;
import me.PM2.infinitevehicles.commands.lib.expiringmap.internal.NamedThreadFactory;

public class ExpiringMap<K, V> implements ConcurrentMap<K, V> {
   static volatile ScheduledExecutorService EXPIRER;
   static volatile ThreadPoolExecutor LISTENER_SERVICE;
   static ThreadFactory THREAD_FACTORY;
   List<ExpirationListener<K, V>> expirationListeners;
   List<ExpirationListener<K, V>> asyncExpirationListeners;
   private AtomicLong expirationNanos;
   private int maxSize;
   private final AtomicReference<ExpirationPolicy> expirationPolicy;
   private final EntryLoader<? super K, ? extends V> entryLoader;
   private final ExpiringEntryLoader<? super K, ? extends V> expiringEntryLoader;
   private final ReadWriteLock readWriteLock;
   private final Lock readLock;
   private final Lock writeLock;
   private final ExpiringMap.EntryMap<K, V> entries;
   private final boolean variableExpiration;

   public static void setThreadFactory(ThreadFactory var0) {
      THREAD_FACTORY = (ThreadFactory)Assert.notNull(var0, "threadFactory");
   }

   private ExpiringMap(ExpiringMap.Builder<K, V> var1) {
      this.readWriteLock = new ReentrantReadWriteLock();
      this.readLock = this.readWriteLock.readLock();
      this.writeLock = this.readWriteLock.writeLock();
      if (EXPIRER == null) {
         Class var2 = ExpiringMap.class;
         synchronized(ExpiringMap.class) {
            if (EXPIRER == null) {
               EXPIRER = Executors.newSingleThreadScheduledExecutor((ThreadFactory)(THREAD_FACTORY == null ? new NamedThreadFactory("ExpiringMap-Expirer") : THREAD_FACTORY));
            }
         }
      }

      if (LISTENER_SERVICE == null && var1.asyncExpirationListeners != null) {
         this.initListenerService();
      }

      this.variableExpiration = var1.variableExpiration;
      this.entries = (ExpiringMap.EntryMap)(this.variableExpiration ? new ExpiringMap.EntryTreeHashMap() : new ExpiringMap.EntryLinkedHashMap());
      if (var1.expirationListeners != null) {
         this.expirationListeners = new CopyOnWriteArrayList(var1.expirationListeners);
      }

      if (var1.asyncExpirationListeners != null) {
         this.asyncExpirationListeners = new CopyOnWriteArrayList(var1.asyncExpirationListeners);
      }

      this.expirationPolicy = new AtomicReference(var1.expirationPolicy);
      this.expirationNanos = new AtomicLong(TimeUnit.NANOSECONDS.convert(var1.duration, var1.timeUnit));
      this.maxSize = var1.maxSize;
      this.entryLoader = var1.entryLoader;
      this.expiringEntryLoader = var1.expiringEntryLoader;
   }

   public static ExpiringMap.Builder<Object, Object> builder() {
      return new ExpiringMap.Builder();
   }

   public static <K, V> ExpiringMap<K, V> create() {
      return new ExpiringMap(builder());
   }

   public synchronized void addExpirationListener(ExpirationListener<K, V> var1) {
      Assert.notNull(var1, "listener");
      if (this.expirationListeners == null) {
         this.expirationListeners = new CopyOnWriteArrayList();
      }

      this.expirationListeners.add(var1);
   }

   public synchronized void addAsyncExpirationListener(ExpirationListener<K, V> var1) {
      Assert.notNull(var1, "listener");
      if (this.asyncExpirationListeners == null) {
         this.asyncExpirationListeners = new CopyOnWriteArrayList();
      }

      this.asyncExpirationListeners.add(var1);
      if (LISTENER_SERVICE == null) {
         this.initListenerService();
      }

   }

   public void clear() {
      this.writeLock.lock();

      try {
         Iterator var1 = this.entries.values().iterator();

         while(var1.hasNext()) {
            ExpiringMap.ExpiringEntry var2 = (ExpiringMap.ExpiringEntry)var1.next();
            var2.cancel();
         }

         this.entries.clear();
      } finally {
         this.writeLock.unlock();
      }
   }

   public boolean containsKey(Object var1) {
      this.readLock.lock();

      boolean var2;
      try {
         var2 = this.entries.containsKey(var1);
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   public boolean containsValue(Object var1) {
      this.readLock.lock();

      boolean var2;
      try {
         var2 = this.entries.containsValue(var1);
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   public Set<Entry<K, V>> entrySet() {
      return new AbstractSet<Entry<K, V>>() {
         public void clear() {
            ExpiringMap.this.clear();
         }

         public boolean contains(Object var1) {
            if (!(var1 instanceof Entry)) {
               return false;
            } else {
               Entry var2 = (Entry)var1;
               return ExpiringMap.this.containsKey(var2.getKey());
            }
         }

         public Iterator<Entry<K, V>> iterator() {
            return (Iterator)(ExpiringMap.this.entries instanceof ExpiringMap.EntryLinkedHashMap ? (ExpiringMap.EntryLinkedHashMap)ExpiringMap.this.entries.new EntryIterator() : (ExpiringMap.EntryTreeHashMap)ExpiringMap.this.entries.new EntryIterator());
         }

         public boolean remove(Object var1) {
            if (var1 instanceof Entry) {
               Entry var2 = (Entry)var1;
               return ExpiringMap.this.remove(var2.getKey()) != null;
            } else {
               return false;
            }
         }

         public int size() {
            return ExpiringMap.this.size();
         }
      };
   }

   public boolean equals(Object var1) {
      this.readLock.lock();

      boolean var2;
      try {
         var2 = this.entries.equals(var1);
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   public V get(Object var1) {
      ExpiringMap.ExpiringEntry var2 = this.getEntry(var1);
      if (var2 == null) {
         return this.load(var1);
      } else {
         if (ExpirationPolicy.ACCESSED.equals(var2.expirationPolicy.get())) {
            this.resetEntry(var2, false);
         }

         return var2.getValue();
      }
   }

   private V load(K var1) {
      if (this.entryLoader == null && this.expiringEntryLoader == null) {
         return null;
      } else {
         this.writeLock.lock();

         Object var4;
         try {
            ExpiringMap.ExpiringEntry var2 = this.getEntry(var1);
            Object var11;
            if (var2 != null) {
               var11 = var2.getValue();
               return var11;
            }

            if (this.entryLoader != null) {
               var11 = this.entryLoader.load(var1);
               this.put(var1, var11);
               var4 = var11;
               return var4;
            }

            ExpiringValue var3 = this.expiringEntryLoader.load(var1);
            if (var3 != null) {
               long var12 = var3.getTimeUnit() == null ? this.expirationNanos.get() : var3.getDuration();
               TimeUnit var6 = var3.getTimeUnit() == null ? TimeUnit.NANOSECONDS : var3.getTimeUnit();
               this.put(var1, var3.getValue(), var3.getExpirationPolicy() == null ? (ExpirationPolicy)this.expirationPolicy.get() : var3.getExpirationPolicy(), var12, var6);
               Object var7 = var3.getValue();
               return var7;
            }

            this.put(var1, (Object)null);
            var4 = null;
         } finally {
            this.writeLock.unlock();
         }

         return var4;
      }
   }

   public long getExpiration() {
      return TimeUnit.NANOSECONDS.toMillis(this.expirationNanos.get());
   }

   public long getExpiration(K var1) {
      Assert.notNull(var1, "key");
      ExpiringMap.ExpiringEntry var2 = this.getEntry(var1);
      Assert.element(var2, var1);
      return TimeUnit.NANOSECONDS.toMillis(var2.expirationNanos.get());
   }

   public ExpirationPolicy getExpirationPolicy(K var1) {
      Assert.notNull(var1, "key");
      ExpiringMap.ExpiringEntry var2 = this.getEntry(var1);
      Assert.element(var2, var1);
      return (ExpirationPolicy)var2.expirationPolicy.get();
   }

   public long getExpectedExpiration(K var1) {
      Assert.notNull(var1, "key");
      ExpiringMap.ExpiringEntry var2 = this.getEntry(var1);
      Assert.element(var2, var1);
      return TimeUnit.NANOSECONDS.toMillis(var2.expectedExpiration.get() - System.nanoTime());
   }

   public int getMaxSize() {
      return this.maxSize;
   }

   public int hashCode() {
      this.readLock.lock();

      int var1;
      try {
         var1 = this.entries.hashCode();
      } finally {
         this.readLock.unlock();
      }

      return var1;
   }

   public boolean isEmpty() {
      this.readLock.lock();

      boolean var1;
      try {
         var1 = this.entries.isEmpty();
      } finally {
         this.readLock.unlock();
      }

      return var1;
   }

   public Set<K> keySet() {
      return new AbstractSet<K>() {
         public void clear() {
            ExpiringMap.this.clear();
         }

         public boolean contains(Object var1) {
            return ExpiringMap.this.containsKey(var1);
         }

         public Iterator<K> iterator() {
            return (Iterator)(ExpiringMap.this.entries instanceof ExpiringMap.EntryLinkedHashMap ? (ExpiringMap.EntryLinkedHashMap)ExpiringMap.this.entries.new KeyIterator() : (ExpiringMap.EntryTreeHashMap)ExpiringMap.this.entries.new KeyIterator());
         }

         public boolean remove(Object var1) {
            return ExpiringMap.this.remove(var1) != null;
         }

         public int size() {
            return ExpiringMap.this.size();
         }
      };
   }

   public V put(K var1, V var2) {
      Assert.notNull(var1, "key");
      return this.putInternal(var1, var2, (ExpirationPolicy)this.expirationPolicy.get(), this.expirationNanos.get());
   }

   public V put(K var1, V var2, ExpirationPolicy var3) {
      return this.put(var1, var2, var3, this.expirationNanos.get(), TimeUnit.NANOSECONDS);
   }

   public V put(K var1, V var2, long var3, TimeUnit var5) {
      return this.put(var1, var2, (ExpirationPolicy)this.expirationPolicy.get(), var3, var5);
   }

   public V put(K var1, V var2, ExpirationPolicy var3, long var4, TimeUnit var6) {
      Assert.notNull(var1, "key");
      Assert.notNull(var3, "expirationPolicy");
      Assert.notNull(var6, "timeUnit");
      Assert.operation(this.variableExpiration, "Variable expiration is not enabled");
      return this.putInternal(var1, var2, var3, TimeUnit.NANOSECONDS.convert(var4, var6));
   }

   public void putAll(Map<? extends K, ? extends V> var1) {
      Assert.notNull(var1, "map");
      long var2 = this.expirationNanos.get();
      ExpirationPolicy var4 = (ExpirationPolicy)this.expirationPolicy.get();
      this.writeLock.lock();

      try {
         Iterator var5 = var1.entrySet().iterator();

         while(var5.hasNext()) {
            Entry var6 = (Entry)var5.next();
            this.putInternal(var6.getKey(), var6.getValue(), var4, var2);
         }
      } finally {
         this.writeLock.unlock();
      }

   }

   public V putIfAbsent(K var1, V var2) {
      Assert.notNull(var1, "key");
      this.writeLock.lock();

      Object var3;
      try {
         if (!this.entries.containsKey(var1)) {
            var3 = this.putInternal(var1, var2, (ExpirationPolicy)this.expirationPolicy.get(), this.expirationNanos.get());
            return var3;
         }

         var3 = ((ExpiringMap.ExpiringEntry)this.entries.get(var1)).getValue();
      } finally {
         this.writeLock.unlock();
      }

      return var3;
   }

   public V remove(Object var1) {
      Assert.notNull(var1, "key");
      this.writeLock.lock();

      Object var3;
      try {
         ExpiringMap.ExpiringEntry var2 = (ExpiringMap.ExpiringEntry)this.entries.remove(var1);
         if (var2 != null) {
            if (var2.cancel()) {
               this.scheduleEntry(this.entries.first());
            }

            var3 = var2.getValue();
            return var3;
         }

         var3 = null;
      } finally {
         this.writeLock.unlock();
      }

      return var3;
   }

   public boolean remove(Object var1, Object var2) {
      Assert.notNull(var1, "key");
      this.writeLock.lock();

      boolean var4;
      try {
         ExpiringMap.ExpiringEntry var3 = (ExpiringMap.ExpiringEntry)this.entries.get(var1);
         if (var3 == null || !var3.getValue().equals(var2)) {
            var4 = false;
            return var4;
         }

         this.entries.remove(var1);
         if (var3.cancel()) {
            this.scheduleEntry(this.entries.first());
         }

         var4 = true;
      } finally {
         this.writeLock.unlock();
      }

      return var4;
   }

   public V replace(K var1, V var2) {
      Assert.notNull(var1, "key");
      this.writeLock.lock();

      Object var3;
      try {
         if (!this.entries.containsKey(var1)) {
            var3 = null;
            return var3;
         }

         var3 = this.putInternal(var1, var2, (ExpirationPolicy)this.expirationPolicy.get(), this.expirationNanos.get());
      } finally {
         this.writeLock.unlock();
      }

      return var3;
   }

   public boolean replace(K var1, V var2, V var3) {
      Assert.notNull(var1, "key");
      this.writeLock.lock();

      boolean var5;
      try {
         ExpiringMap.ExpiringEntry var4 = (ExpiringMap.ExpiringEntry)this.entries.get(var1);
         if (var4 == null || !var4.getValue().equals(var2)) {
            var5 = false;
            return var5;
         }

         this.putInternal(var1, var3, (ExpirationPolicy)this.expirationPolicy.get(), this.expirationNanos.get());
         var5 = true;
      } finally {
         this.writeLock.unlock();
      }

      return var5;
   }

   public void removeExpirationListener(ExpirationListener<K, V> var1) {
      Assert.notNull(var1, "listener");

      for(int var2 = 0; var2 < this.expirationListeners.size(); ++var2) {
         if (((ExpirationListener)this.expirationListeners.get(var2)).equals(var1)) {
            this.expirationListeners.remove(var2);
            return;
         }
      }

   }

   public void removeAsyncExpirationListener(ExpirationListener<K, V> var1) {
      Assert.notNull(var1, "listener");

      for(int var2 = 0; var2 < this.asyncExpirationListeners.size(); ++var2) {
         if (((ExpirationListener)this.asyncExpirationListeners.get(var2)).equals(var1)) {
            this.asyncExpirationListeners.remove(var2);
            return;
         }
      }

   }

   public void resetExpiration(K var1) {
      Assert.notNull(var1, "key");
      ExpiringMap.ExpiringEntry var2 = this.getEntry(var1);
      if (var2 != null) {
         this.resetEntry(var2, false);
      }

   }

   public void setExpiration(K var1, long var2, TimeUnit var4) {
      Assert.notNull(var1, "key");
      Assert.notNull(var4, "timeUnit");
      Assert.operation(this.variableExpiration, "Variable expiration is not enabled");
      this.writeLock.lock();

      try {
         ExpiringMap.ExpiringEntry var5 = (ExpiringMap.ExpiringEntry)this.entries.get(var1);
         if (var5 != null) {
            var5.expirationNanos.set(TimeUnit.NANOSECONDS.convert(var2, var4));
            this.resetEntry(var5, true);
         }
      } finally {
         this.writeLock.unlock();
      }

   }

   public void setExpiration(long var1, TimeUnit var3) {
      Assert.notNull(var3, "timeUnit");
      Assert.operation(this.variableExpiration, "Variable expiration is not enabled");
      this.expirationNanos.set(TimeUnit.NANOSECONDS.convert(var1, var3));
   }

   public void setExpirationPolicy(ExpirationPolicy var1) {
      Assert.notNull(var1, "expirationPolicy");
      this.expirationPolicy.set(var1);
   }

   public void setExpirationPolicy(K var1, ExpirationPolicy var2) {
      Assert.notNull(var1, "key");
      Assert.notNull(var2, "expirationPolicy");
      Assert.operation(this.variableExpiration, "Variable expiration is not enabled");
      ExpiringMap.ExpiringEntry var3 = this.getEntry(var1);
      if (var3 != null) {
         var3.expirationPolicy.set(var2);
      }

   }

   public void setMaxSize(int var1) {
      Assert.operation(var1 > 0, "maxSize");
      this.maxSize = var1;
   }

   public int size() {
      this.readLock.lock();

      int var1;
      try {
         var1 = this.entries.size();
      } finally {
         this.readLock.unlock();
      }

      return var1;
   }

   public String toString() {
      this.readLock.lock();

      String var1;
      try {
         var1 = this.entries.toString();
      } finally {
         this.readLock.unlock();
      }

      return var1;
   }

   public Collection<V> values() {
      return new AbstractCollection<V>() {
         public void clear() {
            ExpiringMap.this.clear();
         }

         public boolean contains(Object var1) {
            return ExpiringMap.this.containsValue(var1);
         }

         public Iterator<V> iterator() {
            return (Iterator)(ExpiringMap.this.entries instanceof ExpiringMap.EntryLinkedHashMap ? (ExpiringMap.EntryLinkedHashMap)ExpiringMap.this.entries.new ValueIterator() : (ExpiringMap.EntryTreeHashMap)ExpiringMap.this.entries.new ValueIterator());
         }

         public int size() {
            return ExpiringMap.this.size();
         }
      };
   }

   void notifyListeners(final ExpiringMap.ExpiringEntry<K, V> var1) {
      Iterator var2;
      final ExpirationListener var3;
      if (this.asyncExpirationListeners != null) {
         var2 = this.asyncExpirationListeners.iterator();

         while(var2.hasNext()) {
            var3 = (ExpirationListener)var2.next();
            LISTENER_SERVICE.execute(new Runnable() {
               public void run() {
                  try {
                     var3.expired(var1.key, var1.getValue());
                  } catch (Exception var2) {
                  }

               }
            });
         }
      }

      if (this.expirationListeners != null) {
         var2 = this.expirationListeners.iterator();

         while(var2.hasNext()) {
            var3 = (ExpirationListener)var2.next();

            try {
               var3.expired(var1.key, var1.getValue());
            } catch (Exception var5) {
            }
         }
      }

   }

   ExpiringMap.ExpiringEntry<K, V> getEntry(Object var1) {
      this.readLock.lock();

      ExpiringMap.ExpiringEntry var2;
      try {
         var2 = (ExpiringMap.ExpiringEntry)this.entries.get(var1);
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   V putInternal(K var1, V var2, ExpirationPolicy var3, long var4) {
      this.writeLock.lock();

      try {
         ExpiringMap.ExpiringEntry var6 = (ExpiringMap.ExpiringEntry)this.entries.get(var1);
         Object var7 = null;
         Object var8;
         if (var6 == null) {
            var6 = new ExpiringMap.ExpiringEntry(var1, var2, this.variableExpiration ? new AtomicReference(var3) : this.expirationPolicy, this.variableExpiration ? new AtomicLong(var4) : this.expirationNanos);
            if (this.entries.size() >= this.maxSize) {
               ExpiringMap.ExpiringEntry var12 = this.entries.first();
               this.entries.remove(var12.key);
               this.notifyListeners(var12);
            }

            this.entries.put(var1, var6);
            if (this.entries.size() == 1 || this.entries.first().equals(var6)) {
               this.scheduleEntry(var6);
            }
         } else {
            var7 = var6.getValue();
            if (!ExpirationPolicy.ACCESSED.equals(var3) && (var7 == null && var2 == null || var7 != null && var7.equals(var2))) {
               var8 = var2;
               return var8;
            }

            var6.setValue(var2);
            this.resetEntry(var6, false);
         }

         var8 = var7;
         return var8;
      } finally {
         this.writeLock.unlock();
      }
   }

   void resetEntry(ExpiringMap.ExpiringEntry<K, V> var1, boolean var2) {
      this.writeLock.lock();

      try {
         boolean var3 = var1.cancel();
         this.entries.reorder(var1);
         if (var3 || var2) {
            this.scheduleEntry(this.entries.first());
         }
      } finally {
         this.writeLock.unlock();
      }

   }

   void scheduleEntry(ExpiringMap.ExpiringEntry<K, V> var1) {
      if (var1 != null && !var1.scheduled) {
         Runnable var2 = null;
         synchronized(var1) {
            if (!var1.scheduled) {
               final WeakReference var4 = new WeakReference(var1);
               var2 = new Runnable() {
                  public void run() {
                     ExpiringMap.ExpiringEntry var1 = (ExpiringMap.ExpiringEntry)var4.get();
                     ExpiringMap.this.writeLock.lock();

                     try {
                        if (var1 != null && var1.scheduled) {
                           ExpiringMap.this.entries.remove(var1.key);
                           ExpiringMap.this.notifyListeners(var1);
                        }

                        try {
                           Iterator var2 = ExpiringMap.this.entries.valuesIterator();
                           boolean var3 = true;

                           while(var2.hasNext() && var3) {
                              ExpiringMap.ExpiringEntry var4x = (ExpiringMap.ExpiringEntry)var2.next();
                              if (var4x.expectedExpiration.get() <= System.nanoTime()) {
                                 var2.remove();
                                 ExpiringMap.this.notifyListeners(var4x);
                              } else {
                                 ExpiringMap.this.scheduleEntry(var4x);
                                 var3 = false;
                              }
                           }
                        } catch (NoSuchElementException var8) {
                        }
                     } finally {
                        ExpiringMap.this.writeLock.unlock();
                     }

                  }
               };
               ScheduledFuture var5 = EXPIRER.schedule(var2, var1.expectedExpiration.get() - System.nanoTime(), TimeUnit.NANOSECONDS);
               var1.schedule(var5);
            }
         }
      }
   }

   private static <K, V> Entry<K, V> mapEntryFor(final ExpiringMap.ExpiringEntry<K, V> var0) {
      return new Entry<K, V>() {
         public K getKey() {
            return var0.key;
         }

         public V getValue() {
            return var0.value;
         }

         public V setValue(V var1) {
            throw new UnsupportedOperationException();
         }
      };
   }

   private void initListenerService() {
      Class var1 = ExpiringMap.class;
      synchronized(ExpiringMap.class) {
         if (LISTENER_SERVICE == null) {
            LISTENER_SERVICE = (ThreadPoolExecutor)Executors.newCachedThreadPool((ThreadFactory)(THREAD_FACTORY == null ? new NamedThreadFactory("ExpiringMap-Listener-%s") : THREAD_FACTORY));
         }

      }
   }

   // $FF: synthetic method
   ExpiringMap(ExpiringMap.Builder var1, Object var2) {
      this(var1);
   }

   static class ExpiringEntry<K, V> implements Comparable<ExpiringMap.ExpiringEntry<K, V>> {
      final AtomicLong expirationNanos;
      final AtomicLong expectedExpiration;
      final AtomicReference<ExpirationPolicy> expirationPolicy;
      final K key;
      volatile Future<?> entryFuture;
      V value;
      volatile boolean scheduled;

      ExpiringEntry(K var1, V var2, AtomicReference<ExpirationPolicy> var3, AtomicLong var4) {
         this.key = var1;
         this.value = var2;
         this.expirationPolicy = var3;
         this.expirationNanos = var4;
         this.expectedExpiration = new AtomicLong();
         this.resetExpiration();
      }

      public int compareTo(ExpiringMap.ExpiringEntry<K, V> var1) {
         if (this.key.equals(var1.key)) {
            return 0;
         } else {
            return this.expectedExpiration.get() < var1.expectedExpiration.get() ? -1 : 1;
         }
      }

      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var3 = 31 * var2 + (this.key == null ? 0 : this.key.hashCode());
         var3 = 31 * var3 + (this.value == null ? 0 : this.value.hashCode());
         return var3;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 == null) {
            return false;
         } else if (this.getClass() != var1.getClass()) {
            return false;
         } else {
            ExpiringMap.ExpiringEntry var2 = (ExpiringMap.ExpiringEntry)var1;
            if (!this.key.equals(var2.key)) {
               return false;
            } else {
               if (this.value == null) {
                  if (var2.value != null) {
                     return false;
                  }
               } else if (!this.value.equals(var2.value)) {
                  return false;
               }

               return true;
            }
         }
      }

      public String toString() {
         return this.value.toString();
      }

      synchronized boolean cancel() {
         boolean var1 = this.scheduled;
         if (this.entryFuture != null) {
            this.entryFuture.cancel(false);
         }

         this.entryFuture = null;
         this.scheduled = false;
         return var1;
      }

      synchronized V getValue() {
         return this.value;
      }

      void resetExpiration() {
         this.expectedExpiration.set(this.expirationNanos.get() + System.nanoTime());
      }

      synchronized void schedule(Future<?> var1) {
         this.entryFuture = var1;
         this.scheduled = true;
      }

      synchronized void setValue(V var1) {
         this.value = var1;
      }
   }

   private static class EntryTreeHashMap<K, V> extends HashMap<K, ExpiringMap.ExpiringEntry<K, V>> implements ExpiringMap.EntryMap<K, V> {
      private static final long serialVersionUID = 1L;
      SortedSet<ExpiringMap.ExpiringEntry<K, V>> sortedSet;

      private EntryTreeHashMap() {
         this.sortedSet = new ConcurrentSkipListSet();
      }

      public void clear() {
         super.clear();
         this.sortedSet.clear();
      }

      public boolean containsValue(Object var1) {
         Iterator var2 = this.values().iterator();

         Object var4;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            ExpiringMap.ExpiringEntry var3 = (ExpiringMap.ExpiringEntry)var2.next();
            var4 = var3.value;
         } while(var4 != var1 && (var1 == null || !var1.equals(var4)));

         return true;
      }

      public ExpiringMap.ExpiringEntry<K, V> first() {
         return this.sortedSet.isEmpty() ? null : (ExpiringMap.ExpiringEntry)this.sortedSet.first();
      }

      public ExpiringMap.ExpiringEntry<K, V> put(K var1, ExpiringMap.ExpiringEntry<K, V> var2) {
         this.sortedSet.add(var2);
         return (ExpiringMap.ExpiringEntry)super.put(var1, var2);
      }

      public ExpiringMap.ExpiringEntry<K, V> remove(Object var1) {
         ExpiringMap.ExpiringEntry var2 = (ExpiringMap.ExpiringEntry)super.remove(var1);
         if (var2 != null) {
            this.sortedSet.remove(var2);
         }

         return var2;
      }

      public void reorder(ExpiringMap.ExpiringEntry<K, V> var1) {
         this.sortedSet.remove(var1);
         var1.resetExpiration();
         this.sortedSet.add(var1);
      }

      public Iterator<ExpiringMap.ExpiringEntry<K, V>> valuesIterator() {
         return new ExpiringMap.EntryTreeHashMap.ExpiringEntryIterator();
      }

      // $FF: synthetic method
      EntryTreeHashMap(Object var1) {
         this();
      }

      final class EntryIterator extends ExpiringMap.EntryTreeHashMap<K, V>.AbstractHashIterator implements Iterator<Entry<K, V>> {
         EntryIterator() {
            super();
         }

         public final Entry<K, V> next() {
            return ExpiringMap.mapEntryFor(this.getNext());
         }
      }

      final class ValueIterator extends ExpiringMap.EntryTreeHashMap<K, V>.AbstractHashIterator implements Iterator<V> {
         ValueIterator() {
            super();
         }

         public final V next() {
            return this.getNext().value;
         }
      }

      final class KeyIterator extends ExpiringMap.EntryTreeHashMap<K, V>.AbstractHashIterator implements Iterator<K> {
         KeyIterator() {
            super();
         }

         public final K next() {
            return this.getNext().key;
         }
      }

      final class ExpiringEntryIterator extends ExpiringMap.EntryTreeHashMap<K, V>.AbstractHashIterator implements Iterator<ExpiringMap.ExpiringEntry<K, V>> {
         ExpiringEntryIterator() {
            super();
         }

         public final ExpiringMap.ExpiringEntry<K, V> next() {
            return this.getNext();
         }
      }

      abstract class AbstractHashIterator {
         private final Iterator<ExpiringMap.ExpiringEntry<K, V>> iterator;
         protected ExpiringMap.ExpiringEntry<K, V> next;

         AbstractHashIterator() {
            this.iterator = EntryTreeHashMap.this.sortedSet.iterator();
         }

         public boolean hasNext() {
            return this.iterator.hasNext();
         }

         public ExpiringMap.ExpiringEntry<K, V> getNext() {
            this.next = (ExpiringMap.ExpiringEntry)this.iterator.next();
            return this.next;
         }

         public void remove() {
            ExpiringMap.EntryTreeHashMap.super.remove(this.next.key);
            this.iterator.remove();
         }
      }
   }

   private static class EntryLinkedHashMap<K, V> extends LinkedHashMap<K, ExpiringMap.ExpiringEntry<K, V>> implements ExpiringMap.EntryMap<K, V> {
      private static final long serialVersionUID = 1L;

      private EntryLinkedHashMap() {
      }

      public boolean containsValue(Object var1) {
         Iterator var2 = this.values().iterator();

         Object var4;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            ExpiringMap.ExpiringEntry var3 = (ExpiringMap.ExpiringEntry)var2.next();
            var4 = var3.value;
         } while(var4 != var1 && (var1 == null || !var1.equals(var4)));

         return true;
      }

      public ExpiringMap.ExpiringEntry<K, V> first() {
         return this.isEmpty() ? null : (ExpiringMap.ExpiringEntry)this.values().iterator().next();
      }

      public void reorder(ExpiringMap.ExpiringEntry<K, V> var1) {
         this.remove(var1.key);
         var1.resetExpiration();
         this.put(var1.key, var1);
      }

      public Iterator<ExpiringMap.ExpiringEntry<K, V>> valuesIterator() {
         return this.values().iterator();
      }

      // $FF: synthetic method
      EntryLinkedHashMap(Object var1) {
         this();
      }

      public final class EntryIterator extends ExpiringMap.EntryLinkedHashMap<K, V>.AbstractHashIterator implements Iterator<Entry<K, V>> {
         public EntryIterator() {
            super();
         }

         public final Entry<K, V> next() {
            return ExpiringMap.mapEntryFor(this.getNext());
         }
      }

      final class ValueIterator extends ExpiringMap.EntryLinkedHashMap<K, V>.AbstractHashIterator implements Iterator<V> {
         ValueIterator() {
            super();
         }

         public final V next() {
            return this.getNext().value;
         }
      }

      final class KeyIterator extends ExpiringMap.EntryLinkedHashMap<K, V>.AbstractHashIterator implements Iterator<K> {
         KeyIterator() {
            super();
         }

         public final K next() {
            return this.getNext().key;
         }
      }

      abstract class AbstractHashIterator {
         private final Iterator<Entry<K, ExpiringMap.ExpiringEntry<K, V>>> iterator = EntryLinkedHashMap.this.entrySet().iterator();
         private ExpiringMap.ExpiringEntry<K, V> next;

         public boolean hasNext() {
            return this.iterator.hasNext();
         }

         public ExpiringMap.ExpiringEntry<K, V> getNext() {
            this.next = (ExpiringMap.ExpiringEntry)((Entry)this.iterator.next()).getValue();
            return this.next;
         }

         public void remove() {
            this.iterator.remove();
         }
      }
   }

   private interface EntryMap<K, V> extends Map<K, ExpiringMap.ExpiringEntry<K, V>> {
      ExpiringMap.ExpiringEntry<K, V> first();

      void reorder(ExpiringMap.ExpiringEntry<K, V> var1);

      Iterator<ExpiringMap.ExpiringEntry<K, V>> valuesIterator();
   }

   public static final class Builder<K, V> {
      private ExpirationPolicy expirationPolicy;
      private List<ExpirationListener<K, V>> expirationListeners;
      private List<ExpirationListener<K, V>> asyncExpirationListeners;
      private TimeUnit timeUnit;
      private boolean variableExpiration;
      private long duration;
      private int maxSize;
      private EntryLoader<K, V> entryLoader;
      private ExpiringEntryLoader<K, V> expiringEntryLoader;

      private Builder() {
         this.expirationPolicy = ExpirationPolicy.CREATED;
         this.timeUnit = TimeUnit.SECONDS;
         this.duration = 60L;
         this.maxSize = Integer.MAX_VALUE;
      }

      public <K1 extends K, V1 extends V> ExpiringMap<K1, V1> build() {
         return new ExpiringMap(this);
      }

      public ExpiringMap.Builder<K, V> expiration(long var1, TimeUnit var3) {
         this.duration = var1;
         this.timeUnit = (TimeUnit)Assert.notNull(var3, "timeUnit");
         return this;
      }

      public ExpiringMap.Builder<K, V> maxSize(int var1) {
         Assert.operation(var1 > 0, "maxSize");
         this.maxSize = var1;
         return this;
      }

      public <K1 extends K, V1 extends V> ExpiringMap.Builder<K1, V1> entryLoader(EntryLoader<? super K1, ? super V1> var1) {
         this.assertNoLoaderSet();
         this.entryLoader = (EntryLoader)Assert.notNull(var1, "loader");
         return this;
      }

      public <K1 extends K, V1 extends V> ExpiringMap.Builder<K1, V1> expiringEntryLoader(ExpiringEntryLoader<? super K1, ? super V1> var1) {
         this.assertNoLoaderSet();
         this.expiringEntryLoader = (ExpiringEntryLoader)Assert.notNull(var1, "loader");
         this.variableExpiration();
         return this;
      }

      public <K1 extends K, V1 extends V> ExpiringMap.Builder<K1, V1> expirationListener(ExpirationListener<? super K1, ? super V1> var1) {
         Assert.notNull(var1, "listener");
         if (this.expirationListeners == null) {
            this.expirationListeners = new ArrayList();
         }

         this.expirationListeners.add(var1);
         return this;
      }

      public <K1 extends K, V1 extends V> ExpiringMap.Builder<K1, V1> expirationListeners(List<ExpirationListener<? super K1, ? super V1>> var1) {
         Assert.notNull(var1, "listeners");
         if (this.expirationListeners == null) {
            this.expirationListeners = new ArrayList(var1.size());
         }

         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            ExpirationListener var3 = (ExpirationListener)var2.next();
            this.expirationListeners.add(var3);
         }

         return this;
      }

      public <K1 extends K, V1 extends V> ExpiringMap.Builder<K1, V1> asyncExpirationListener(ExpirationListener<? super K1, ? super V1> var1) {
         Assert.notNull(var1, "listener");
         if (this.asyncExpirationListeners == null) {
            this.asyncExpirationListeners = new ArrayList();
         }

         this.asyncExpirationListeners.add(var1);
         return this;
      }

      public <K1 extends K, V1 extends V> ExpiringMap.Builder<K1, V1> asyncExpirationListeners(List<ExpirationListener<? super K1, ? super V1>> var1) {
         Assert.notNull(var1, "listeners");
         if (this.asyncExpirationListeners == null) {
            this.asyncExpirationListeners = new ArrayList(var1.size());
         }

         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            ExpirationListener var3 = (ExpirationListener)var2.next();
            this.asyncExpirationListeners.add(var3);
         }

         return this;
      }

      public ExpiringMap.Builder<K, V> expirationPolicy(ExpirationPolicy var1) {
         this.expirationPolicy = (ExpirationPolicy)Assert.notNull(var1, "expirationPolicy");
         return this;
      }

      public ExpiringMap.Builder<K, V> variableExpiration() {
         this.variableExpiration = true;
         return this;
      }

      private void assertNoLoaderSet() {
         Assert.state(this.entryLoader == null && this.expiringEntryLoader == null, "Either entryLoader or expiringEntryLoader may be set, not both");
      }

      // $FF: synthetic method
      Builder(Object var1) {
         this();
      }
   }
}
