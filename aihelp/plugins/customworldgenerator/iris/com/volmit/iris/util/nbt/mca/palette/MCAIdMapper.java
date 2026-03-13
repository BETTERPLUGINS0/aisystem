package com.volmit.iris.util.nbt.mca.palette;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.Hash.Strategy;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import java.util.Iterator;
import java.util.List;

public class MCAIdMapper<T> implements MCAIdMap<T> {
   public static final int DEFAULT = -1;
   private final Object2IntMap<T> tToId;
   private final List<T> idToT;
   private int nextId;

   public MCAIdMapper(Object2IntMap<T> tToId, List<T> idToT, int nextId) {
      this.tToId = var1;
      this.idToT = var2;
      this.nextId = var3;
   }

   public MCAIdMapper() {
      this(512);
   }

   public MCAIdMapper(int var0) {
      this.idToT = Lists.newArrayListWithExpectedSize(var1);
      this.tToId = new Object2IntOpenCustomHashMap(var1, MCAIdMapper.IdentityStrategy.INSTANCE);
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
      Integer var2 = this.tToId.get(var1);
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

   static enum IdentityStrategy implements Strategy<Object> {
      INSTANCE;

      public int hashCode(Object var0) {
         return System.identityHashCode(var1);
      }

      public boolean equals(Object var0, Object var1) {
         return var1 == var2;
      }

      // $FF: synthetic method
      private static MCAIdMapper.IdentityStrategy[] $values() {
         return new MCAIdMapper.IdentityStrategy[]{INSTANCE};
      }
   }
}
