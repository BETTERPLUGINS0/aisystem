package com.volmit.iris.util.data;

import com.volmit.iris.util.collection.KMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import lombok.NonNull;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VectorMap<T> implements Iterable<Entry<BlockVector, T>> {
   private final Map<VectorMap.Key, Map<VectorMap.Key, T>> map = new KMap();

   public int size() {
      return this.map.values().stream().mapToInt(Map::size).sum();
   }

   public boolean isEmpty() {
      return this.map.values().stream().allMatch(Map::isEmpty);
   }

   public boolean containsKey(@NonNull BlockVector vector) {
      if (var1 == null) {
         throw new NullPointerException("vector is marked non-null but is null");
      } else {
         Map var2 = (Map)this.map.get(chunk(var1));
         return var2 != null && var2.containsKey(relative(var1));
      }
   }

   public boolean containsValue(@NonNull T value) {
      if (var1 == null) {
         throw new NullPointerException("value is marked non-null but is null");
      } else {
         return this.map.values().stream().anyMatch((var1x) -> {
            return var1x.containsValue(var1);
         });
      }
   }

   @Nullable
   public T get(@NonNull BlockVector vector) {
      if (var1 == null) {
         throw new NullPointerException("vector is marked non-null but is null");
      } else {
         Map var2 = (Map)this.map.get(chunk(var1));
         return var2 == null ? null : var2.get(relative(var1));
      }
   }

   @Nullable
   public T put(@NonNull BlockVector vector, @NonNull T value) {
      if (var1 == null) {
         throw new NullPointerException("vector is marked non-null but is null");
      } else if (var2 == null) {
         throw new NullPointerException("value is marked non-null but is null");
      } else {
         return ((Map)this.map.computeIfAbsent(chunk(var1), (var0) -> {
            return new KMap();
         })).put(relative(var1), var2);
      }
   }

   @Nullable
   public T computeIfAbsent(@NonNull BlockVector vector, @NonNull Function<BlockVector, T> mappingFunction) {
      if (var1 == null) {
         throw new NullPointerException("vector is marked non-null but is null");
      } else if (var2 == null) {
         throw new NullPointerException("mappingFunction is marked non-null but is null");
      } else {
         return ((Map)this.map.computeIfAbsent(chunk(var1), (var0) -> {
            return new KMap();
         })).computeIfAbsent(relative(var1), (var2x) -> {
            return var2.apply(var1);
         });
      }
   }

   @Nullable
   public T remove(@NonNull BlockVector vector) {
      if (var1 == null) {
         throw new NullPointerException("vector is marked non-null but is null");
      } else {
         Map var2 = (Map)this.map.get(chunk(var1));
         return var2 == null ? null : var2.remove(relative(var1));
      }
   }

   public void putAll(@NonNull VectorMap<T> map) {
      if (var1 == null) {
         throw new NullPointerException("map is marked non-null but is null");
      } else {
         var1.forEach(this::put);
      }
   }

   public void clear() {
      this.map.clear();
   }

   public void forEach(@NonNull BiConsumer<BlockVector, T> consumer) {
      if (var1 == null) {
         throw new NullPointerException("consumer is marked non-null but is null");
      } else {
         this.map.forEach((var1x, var2) -> {
            int var3 = var1x.x << 10;
            int var4 = var1x.y << 10;
            int var5 = var1x.z << 10;
            var2.forEach((var4x, var5x) -> {
               var1.accept(var4x.resolve(var3, var4, var5), var5x);
            });
         });
      }
   }

   private static VectorMap.Key chunk(BlockVector vector) {
      return new VectorMap.Key(var0.getBlockX() >> 10, var0.getBlockY() >> 10, var0.getBlockZ() >> 10);
   }

   private static VectorMap.Key relative(BlockVector vector) {
      return new VectorMap.Key(var0.getBlockX() & 1023, var0.getBlockY() & 1023, var0.getBlockZ() & 1023);
   }

   @NotNull
   public VectorMap<T>.EntryIterator iterator() {
      return new VectorMap.EntryIterator();
   }

   @NotNull
   public VectorMap<T>.KeyIterator keys() {
      return new VectorMap.KeyIterator();
   }

   @NotNull
   public VectorMap<T>.ValueIterator values() {
      return new VectorMap.ValueIterator();
   }

   private static final class Key {
      private final int x;
      private final int y;
      private final int z;
      private final int hashCode;

      private Key(int x, int y, int z) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
         this.hashCode = var1 << 20 | var2 << 10 | var3;
      }

      private BlockVector resolve(int rX, int rY, int rZ) {
         return new BlockVector(var1 + this.x, var2 + this.y, var3 + this.z);
      }

      public int hashCode() {
         return this.hashCode;
      }

      public boolean equals(Object o) {
         if (!(var1 instanceof VectorMap.Key)) {
            return false;
         } else {
            VectorMap.Key var2 = (VectorMap.Key)var1;
            return this.x == var2.x && this.y == var2.y && this.z == var2.z;
         }
      }
   }

   public class EntryIterator implements Iterator<Entry<BlockVector, T>> {
      private final Iterator<Entry<VectorMap.Key, Map<VectorMap.Key, T>>> chunkIterator;
      private Iterator<Entry<VectorMap.Key, T>> relativeIterator;
      private int rX;
      private int rY;
      private int rZ;

      public EntryIterator() {
         this.chunkIterator = VectorMap.this.map.entrySet().iterator();
      }

      public boolean hasNext() {
         return this.relativeIterator != null && this.relativeIterator.hasNext() || this.chunkIterator.hasNext();
      }

      public Entry<BlockVector, T> next() {
         Entry var1;
         if (this.relativeIterator == null || !this.relativeIterator.hasNext()) {
            if (!this.chunkIterator.hasNext()) {
               throw new IllegalStateException("No more elements");
            }

            var1 = (Entry)this.chunkIterator.next();
            this.rX = ((VectorMap.Key)var1.getKey()).x << 10;
            this.rY = ((VectorMap.Key)var1.getKey()).y << 10;
            this.rZ = ((VectorMap.Key)var1.getKey()).z << 10;
            this.relativeIterator = ((Map)var1.getValue()).entrySet().iterator();
         }

         var1 = (Entry)this.relativeIterator.next();
         return Map.entry(((VectorMap.Key)var1.getKey()).resolve(this.rX, this.rY, this.rZ), var1.getValue());
      }

      public void remove() {
         if (this.relativeIterator == null) {
            throw new IllegalStateException("No element to remove");
         } else {
            this.relativeIterator.remove();
         }
      }
   }

   public class KeyIterator implements Iterator<BlockVector>, Iterable<BlockVector> {
      private final Iterator<Entry<VectorMap.Key, Map<VectorMap.Key, T>>> chunkIterator;
      private Iterator<VectorMap.Key> relativeIterator;
      private int rX;
      private int rY;
      private int rZ;

      public KeyIterator() {
         this.chunkIterator = VectorMap.this.map.entrySet().iterator();
      }

      public boolean hasNext() {
         return this.relativeIterator != null && this.relativeIterator.hasNext() || this.chunkIterator.hasNext();
      }

      public BlockVector next() {
         if (this.relativeIterator == null || !this.relativeIterator.hasNext()) {
            Entry var1 = (Entry)this.chunkIterator.next();
            this.rX = ((VectorMap.Key)var1.getKey()).x << 10;
            this.rY = ((VectorMap.Key)var1.getKey()).y << 10;
            this.rZ = ((VectorMap.Key)var1.getKey()).z << 10;
            this.relativeIterator = ((Map)var1.getValue()).keySet().iterator();
         }

         return ((VectorMap.Key)this.relativeIterator.next()).resolve(this.rX, this.rY, this.rZ);
      }

      public void remove() {
         if (this.relativeIterator == null) {
            throw new IllegalStateException("No element to remove");
         } else {
            this.relativeIterator.remove();
         }
      }

      @NotNull
      public Iterator<BlockVector> iterator() {
         return this;
      }
   }

   public class ValueIterator implements Iterator<T>, Iterable<T> {
      private final Iterator<Map<VectorMap.Key, T>> chunkIterator;
      private Iterator<T> relativeIterator;

      public ValueIterator() {
         this.chunkIterator = VectorMap.this.map.values().iterator();
      }

      public boolean hasNext() {
         return this.relativeIterator != null && this.relativeIterator.hasNext() || this.chunkIterator.hasNext();
      }

      public T next() {
         if (this.relativeIterator == null || !this.relativeIterator.hasNext()) {
            this.relativeIterator = ((Map)this.chunkIterator.next()).values().iterator();
         }

         return this.relativeIterator.next();
      }

      public void remove() {
         if (this.relativeIterator == null) {
            throw new IllegalStateException("No element to remove");
         } else {
            this.relativeIterator.remove();
         }
      }

      @NotNull
      public Iterator<T> iterator() {
         return this;
      }
   }
}
