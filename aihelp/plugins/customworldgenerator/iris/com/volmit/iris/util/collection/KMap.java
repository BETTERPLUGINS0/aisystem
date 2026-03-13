package com.volmit.iris.util.collection;

import com.volmit.iris.Iris;
import com.volmit.iris.util.function.Consumer2;
import com.volmit.iris.util.function.Consumer3;
import com.volmit.iris.util.scheduling.Queue;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class KMap<K, V> extends ConcurrentHashMap<K, V> {
   private static final long serialVersionUID = 7288942695300448163L;

   public KMap() {
      this(16);
   }

   public KMap(int initialCapacity) {
      this(var1, 0.75F, 1);
   }

   public KMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
      super(var1, var2, var3);
   }

   public KMap(Map<K, V> gMap) {
      this();
      this.put(var1);
   }

   public K getKey(V value) {
      Iterator var2 = this.keypair().iterator();

      KeyPair var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (KeyPair)var2.next();
      } while(!var3.getV().equals(var1));

      return var3.getK();
   }

   public <S> KMap<K, V> putValueList(K k, S... vs) {
      try {
         if (!this.containsKey(var1)) {
            this.put(var1, new KList());
         }

         ((KList)this.get(var1)).add(var2);
      } catch (Throwable var4) {
         Iris.reportError(var4);
      }

      return this;
   }

   public KList<K> sortK() {
      KList var1 = new KList();
      KList var2 = this.v();
      Collections.sort(var2, new Comparator<V>(this) {
         public int compare(V v, V t1) {
            return var1.toString().compareTo(var2.toString());
         }
      });
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         Iterator var5 = this.k().iterator();

         while(var5.hasNext()) {
            Object var6 = var5.next();
            if (this.get(var6).equals(var4)) {
               var1.add((Object)var6);
            }
         }
      }

      var1.dedupe();
      return var1;
   }

   public KList<K> sortKNumber() {
      KList var1 = new KList();
      KList var2 = this.v();
      Collections.sort(var2, new Comparator<V>(this) {
         public int compare(V v, V t1) {
            Number var3 = (Number)var1;
            Number var4 = (Number)var2;
            return (int)((var3.doubleValue() - var4.doubleValue()) * 1000.0D);
         }
      });
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         Iterator var5 = this.k().iterator();

         while(var5.hasNext()) {
            Object var6 = var5.next();
            if (this.get(var6).equals(var4)) {
               var1.add((Object)var6);
            }
         }
      }

      var1.dedupe();
      return var1;
   }

   public KMap<K, V> put(Map<K, V> m) {
      this.putAll(var1);
      return this;
   }

   public KMap<K, V> merge(KMap<K, V> m, BiFunction<V, V, V> merger) {
      var1.forEach((var2x, var3) -> {
         this.merge(var2x, var3, var2);
      });
      return this;
   }

   public KMap<K, V> copy() {
      return new KMap(this);
   }

   public KMap<K, V> rewrite(Consumer3<K, V, KMap<K, V>> f) {
      KMap var2 = this.copy();
      Iterator var3 = var2.k().iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         var1.accept(var4, this.get(var4), this);
      }

      return this;
   }

   public KMap<K, V> each(Consumer2<K, V> f) {
      Iterator var2 = this.k().iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         var1.accept(var3, this.get(var3));
      }

      return this;
   }

   public KMap<V, K> flipFlatten() {
      KMap var1 = this.flip();
      KMap var2 = new KMap();
      Iterator var3 = var1.k().iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         var2.putNonNull(var4, var2.isEmpty() ? null : var2.get(0));
      }

      return var2;
   }

   public KMap<V, KList<K>> flip() {
      KMap var1 = new KMap();
      Iterator var2 = this.keySet().iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         if (var3 != null) {
            if (!var1.containsKey(this.get(var3))) {
               var1.put(this.get(var3), new KList());
            }

            ((KList)var1.get(this.get(var3))).add((Object)var3);
         }
      }

      return var1;
   }

   public KList<V> sortV() {
      KList var1 = new KList();
      KList var2 = this.k();
      Collections.sort(var2, new Comparator<K>(this) {
         public int compare(K v, K t1) {
            return var1.toString().compareTo(var2.toString());
         }
      });
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         Iterator var5 = this.v().iterator();

         while(var5.hasNext()) {
            Object var6 = var5.next();
            if (this.get(var4).equals(var6)) {
               var1.add((Object)var6);
            }
         }
      }

      var1.dedupe();
      return var1;
   }

   public KList<V> sortVNoDedupe() {
      KList var1 = new KList();
      KList var2 = this.k();
      Collections.sort(var2, new Comparator<K>(this) {
         public int compare(K v, K t1) {
            return var1.toString().compareTo(var2.toString());
         }
      });
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         Iterator var5 = this.v().iterator();

         while(var5.hasNext()) {
            Object var6 = var5.next();
            if (this.get(var4).equals(var6)) {
               var1.add((Object)var6);
            }
         }
      }

      return var1;
   }

   public KList<K> k() {
      KList var1 = new KList();
      Enumeration var2 = this.keys();

      while(var2.hasMoreElements()) {
         Object var3 = var2.nextElement();
         var1.add((Object)var3);
      }

      return var1;
   }

   public KList<V> v() {
      return new KList(this.values());
   }

   public KMap<K, V> qput(K key, V value) {
      super.put(var1, var2);
      return this;
   }

   public KMap<K, V> putNonNull(K key, V value) {
      if (var1 != null || var2 != null) {
         this.put(var1, var2);
      }

      return this;
   }

   public V putThen(K key, V valueIfKeyNotPresent) {
      if (!this.containsKey(var1)) {
         this.put(var1, var2);
      }

      return this.get(var1);
   }

   public KMap<K, V> qclear() {
      super.clear();
      return this;
   }

   public KList<KeyPair<K, V>> keypair() {
      KList var1 = new KList();
      this.each((var1x, var2) -> {
         var1.add((Object)(new KeyPair(var1x, var2)));
      });
      return var1;
   }

   public KMap<K, V> qclear(BiConsumer<K, V> action) {
      Iterator var2 = this.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var2.remove();

         try {
            var1.accept(var3.getKey(), var3.getValue());
         } catch (Throwable var5) {
            Iris.reportError(var5);
         }
      }

      return this;
   }

   public Queue<KeyPair<K, V>> enqueue() {
      return Queue.create(this.keypair());
   }

   public Queue<K> enqueueKeys() {
      return Queue.create(this.k());
   }

   public Queue<V> enqueueValues() {
      return Queue.create(this.v());
   }
}
