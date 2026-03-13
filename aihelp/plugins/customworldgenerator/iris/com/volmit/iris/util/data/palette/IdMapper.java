package com.volmit.iris.util.data.palette;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

public class IdMapper<T> implements IdMap<T> {
   public static final int DEFAULT = -1;
   private final IdentityHashMap<T, Integer> tToId;
   private final List<T> idToT;
   private int nextId;

   public IdMapper(IdentityHashMap<T, Integer> tToId, List<T> idToT, int nextId) {
      this.tToId = var1;
      this.idToT = var2;
      this.nextId = var3;
   }

   public IdMapper() {
      this(512);
   }

   public IdMapper(int var0) {
      this.idToT = Lists.newArrayListWithExpectedSize(var1);
      this.tToId = new IdentityHashMap(var1);
   }

   public void addMapping(T var0, int var1) {
      this.tToId.put(var1, var2);

      while(this.idToT.size() <= var2) {
         this.idToT.add((Object)null);
      }

      this.idToT.set(var2, var1);
      if (this.nextId <= var2) {
         this.nextId = var2 + 1;
      }

   }

   public void add(T var0) {
      this.addMapping(var1, this.nextId);
   }

   public int getId(T var0) {
      Integer var2 = (Integer)this.tToId.get(var1);
      return var2 == null ? -1 : var2;
   }

   public final T byId(int var0) {
      return var1 >= 0 && var1 < this.idToT.size() ? this.idToT.get(var1) : null;
   }

   public Iterator<T> iterator() {
      return Iterators.filter(this.idToT.iterator(), Predicates.notNull());
   }

   public boolean contains(int var0) {
      return this.byId(var1) != null;
   }

   public int size() {
      return this.tToId.size();
   }
}
