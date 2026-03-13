package com.volmit.iris.util.collection;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;

public class KSet<T> extends AbstractSet<T> implements Serializable {
   private static final long serialVersionUID = 1L;
   private final ConcurrentHashMap<T, Boolean> map;

   public KSet(Collection<? extends T> c) {
      this(var1.size());
      this.addAll(var1);
   }

   @SafeVarargs
   public KSet(T... values) {
      this(var1.length);
      this.addAll(Arrays.asList(var1));
   }

   public KSet(int initialCapacity, float loadFactor) {
      this.map = new ConcurrentHashMap(var1, var2);
   }

   public KSet(int initialCapacity) {
      this.map = new ConcurrentHashMap(var1);
   }

   public static <T> KSet<T> merge(Collection<? extends T> first, Collection<? extends T> second) {
      KSet var2 = new KSet(new Object[0]);
      var2.addAll(var0);
      var2.addAll(var1);
      return var2;
   }

   public int size() {
      return this.map.size();
   }

   public boolean contains(Object o) {
      return this.map.containsKey(var1);
   }

   public boolean add(T t) {
      return this.map.putIfAbsent(var1, Boolean.TRUE) == null;
   }

   public boolean remove(Object o) {
      return this.map.remove(var1) != null;
   }

   public void clear() {
      this.map.clear();
   }

   @NotNull
   public Iterator<T> iterator() {
      return this.map.keySet().iterator();
   }

   public KSet<T> copy() {
      return new KSet(this);
   }
}
