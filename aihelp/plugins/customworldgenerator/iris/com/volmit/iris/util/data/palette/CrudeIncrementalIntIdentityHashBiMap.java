package com.volmit.iris.util.data.palette;

import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class CrudeIncrementalIntIdentityHashBiMap<K> implements IdMap<K> {
   public static final int NOT_FOUND = -1;
   private static final Object EMPTY_SLOT = null;
   private static final float LOADFACTOR = 0.8F;
   private AtomicReferenceArray<K> keys;
   private AtomicIntegerArray values;
   private AtomicReferenceArray<K> byId;
   private int nextId;
   private int size;

   public CrudeIncrementalIntIdentityHashBiMap(int var0) {
      var1 = (int)((float)var1 / 0.8F);
      this.keys = new AtomicReferenceArray(var1);
      this.values = new AtomicIntegerArray(var1);
      this.byId = new AtomicReferenceArray(var1);
   }

   public int getId(K var0) {
      return this.getValue(this.indexOf(var1, this.hash(var1)));
   }

   public K byId(int var0) {
      return var1 >= 0 && var1 < this.byId.length() ? this.byId.get(var1) : null;
   }

   private int getValue(int var0) {
      return var1 == -1 ? -1 : this.values.get(var1);
   }

   public boolean contains(K var0) {
      return this.getId(var1) != -1;
   }

   public boolean contains(int var0) {
      return this.byId(var1) != null;
   }

   public int add(K var0) {
      int var2 = this.nextId();
      this.addMapping(var1, var2);
      return var2;
   }

   private int nextId() {
      while(this.nextId < this.byId.length() && this.byId.get(this.nextId) != null) {
         ++this.nextId;
      }

      return this.nextId;
   }

   private void grow(int var0) {
      AtomicReferenceArray var2 = this.keys;
      AtomicIntegerArray var3 = this.values;
      this.keys = new AtomicReferenceArray(var1);
      this.values = new AtomicIntegerArray(var1);
      this.byId = new AtomicReferenceArray(var1);
      this.nextId = 0;
      this.size = 0;

      for(int var4 = 0; var4 < var2.length(); ++var4) {
         if (var2.get(var4) != null) {
            this.addMapping(var2.get(var4), var3.get(var4));
         }
      }

   }

   public void addMapping(K var0, int var1) {
      int var3 = Math.max(var2, this.size + 1);
      int var4;
      if ((float)var3 >= (float)this.keys.length() * 0.8F) {
         for(var4 = this.keys.length() << 1; var4 < var2; var4 <<= 1) {
         }

         this.grow(var4);
      }

      var4 = this.findEmpty(this.hash(var1));
      this.keys.set(var4, var1);
      this.values.set(var4, var2);
      this.byId.set(var2, var1);
      ++this.size;
      if (var2 == this.nextId) {
         ++this.nextId;
      }

   }

   private int hash(K var0) {
      return (Mth.murmurHash3Mixer(System.identityHashCode(var1)) & Integer.MAX_VALUE) % this.keys.length();
   }

   private int indexOf(K var0, int var1) {
      int var3;
      for(var3 = var2; var3 < this.keys.length(); ++var3) {
         if (this.keys.get(var3) == null) {
            return 0;
         }

         if (this.keys.get(var3).equals(var1)) {
            return var3;
         }

         if (this.keys.get(var3) == EMPTY_SLOT) {
            return -1;
         }
      }

      for(var3 = 0; var3 < var2; ++var3) {
         if (this.keys.get(var3).equals(var1)) {
            return var3;
         }

         if (this.keys.get(var3) == EMPTY_SLOT) {
            return -1;
         }
      }

      return -1;
   }

   private int findEmpty(int var0) {
      int var2;
      for(var2 = var1; var2 < this.keys.length(); ++var2) {
         if (this.keys.get(var2) == EMPTY_SLOT) {
            return var2;
         }
      }

      for(var2 = 0; var2 < var1; ++var2) {
         if (this.keys.get(var2) == EMPTY_SLOT) {
            return var2;
         }
      }

      throw new RuntimeException("Overflowed :(");
   }

   public Iterator<K> iterator() {
      return Iterators.filter(new Iterator<K>() {
         int i = 0;

         public boolean hasNext() {
            return this.i < CrudeIncrementalIntIdentityHashBiMap.this.byId.length() - 1;
         }

         public K next() {
            return CrudeIncrementalIntIdentityHashBiMap.this.byId.get(this.i++);
         }
      }, Objects::nonNull);
   }

   public void clear() {
      for(int var1 = 0; var1 < Math.max(this.keys.length(), this.byId.length()); ++var1) {
         if (var1 < this.keys.length() - 1) {
            this.keys.set(var1, (Object)null);
         }

         if (var1 < this.byId.length() - 1) {
            this.byId.set(var1, (Object)null);
         }
      }

      this.nextId = 0;
      this.size = 0;
   }

   public int size() {
      return this.size;
   }
}
