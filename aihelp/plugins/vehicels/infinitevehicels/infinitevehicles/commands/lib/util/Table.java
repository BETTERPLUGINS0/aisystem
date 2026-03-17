package me.PM2.infinitevehicles.commands.lib.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.jetbrains.annotations.Nullable;

public class Table<R, C, V> implements Iterable<Table.Entry<R, C, V>> {
   private final Map<R, Map<C, V>> rowMap;
   private final Function<R, Map<C, V>> colMapSupplier;

   public Table() {
      this(new HashMap(), (Supplier)(HashMap::new));
   }

   public Table(Supplier<Map<C, V>> var1) {
      this(new HashMap(), (Supplier)var1);
   }

   public Table(Map<R, Map<C, V>> var1, Supplier<Map<C, V>> var2) {
      this(var1, (var1x) -> {
         return (Map)var2.get();
      });
   }

   public Table(Map<R, Map<C, V>> var1, Function<R, Map<C, V>> var2) {
      this.rowMap = var1;
      this.colMapSupplier = var2;
   }

   public V get(R var1, C var2) {
      return this.getIfExists(var1, var2);
   }

   public V getOrDefault(R var1, C var2, V var3) {
      Map var4 = this.getColMapIfExists(var1);
      if (var4 == null) {
         return var3;
      } else {
         Object var5 = var4.get(var2);
         return var5 == null && !var4.containsKey(var2) ? var3 : var5;
      }
   }

   public boolean containsKey(R var1, C var2) {
      Map var3 = this.getColMapIfExists(var1);
      return var3 == null ? false : var3.containsKey(var2);
   }

   @Nullable
   public V put(R var1, C var2, V var3) {
      return this.getColMapForWrite(var1).put(var2, var3);
   }

   public void forEach(Table.TableConsumer<R, C, V> var1) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         Table.Entry var3 = (Table.Entry)var2.next();
         var1.accept(var3.getRow(), var3.getCol(), var3.getValue());
      }

   }

   public void forEach(Table.TablePredicate<R, C, V> var1) {
      Iterator var2 = this.iterator();

      Table.Entry var3;
      do {
         if (!var2.hasNext()) {
            return;
         }

         var3 = (Table.Entry)var2.next();
      } while(var1.test(var3.getRow(), var3.getCol(), var3.getValue()));

   }

   public void removeIf(Table.TablePredicate<R, C, V> var1) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         Table.Entry var3 = (Table.Entry)var2.next();
         if (var1.test(var3.getRow(), var3.getCol(), var3.getValue())) {
            var2.remove();
         }
      }

   }

   public Stream<Table.Entry<R, C, V>> stream() {
      return this.stream(false);
   }

   public Stream<Table.Entry<R, C, V>> stream(boolean var1) {
      return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this.iterator(), 0), var1);
   }

   public Iterator<Table.Entry<R, C, V>> iterator() {
      return new Iterator<Table.Entry<R, C, V>>() {
         Iterator<java.util.Map.Entry<R, Map<C, V>>> rowIter;
         Iterator<java.util.Map.Entry<C, V>> colIter;
         private java.util.Map.Entry<R, Map<C, V>> rowEntry;
         private java.util.Map.Entry<C, V> colEntry;
         private Table.Entry<R, C, V> next;

         {
            this.rowIter = Table.this.rowMap.entrySet().iterator();
            this.colIter = null;
            this.next = this.getNext();
         }

         private Table.Entry<R, C, V> getNext() {
            if (this.colIter == null || !this.colIter.hasNext()) {
               if (!this.rowIter.hasNext()) {
                  return null;
               }

               this.rowEntry = (java.util.Map.Entry)this.rowIter.next();
               this.colIter = ((Map)this.rowEntry.getValue()).entrySet().iterator();
            }

            if (!this.colIter.hasNext()) {
               return null;
            } else {
               this.colEntry = (java.util.Map.Entry)this.colIter.next();
               return Table.this.new Node(this.rowEntry, this.colEntry);
            }
         }

         public boolean hasNext() {
            return this.next != null;
         }

         public Table.Entry<R, C, V> next() {
            Table.Entry var1 = this.next;
            this.next = this.getNext();
            return var1;
         }

         public void remove() {
            this.colIter.remove();
         }
      };
   }

   public void replaceAll(Table.TableFunction<R, C, V, V> var1) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         Table.Entry var3 = (Table.Entry)var2.next();
         var3.setValue(var1.compose(var3.getRow(), var3.getCol(), var3.getValue()));
      }

   }

   public V remove(R var1, C var2) {
      Map var3 = (Map)this.rowMap.get(var1);
      return var3 == null ? null : var3.remove(var2);
   }

   @Nullable
   public V replace(R var1, C var2, V var3) {
      Map var4 = this.getColMapIfExists(var1);
      if (var4 == null) {
         return null;
      } else {
         return var4.get(var2) == null && !var4.containsKey(var2) ? null : var4.put(var2, var3);
      }
   }

   @Nullable
   public boolean replace(R var1, C var2, V var3, V var4) {
      Map var5 = this.getColMapIfExists(var1);
      if (var5 == null) {
         return false;
      } else if (Objects.equals(var5.get(var2), var3)) {
         var5.put(var2, var4);
         return true;
      } else {
         return false;
      }
   }

   public V computeIfAbsent(R var1, C var2, BiFunction<R, C, V> var3) {
      return this.getColMapForWrite(var1).computeIfAbsent(var2, (var3x) -> {
         return var3.apply(var1, var2);
      });
   }

   public V computeIfPresent(R var1, C var2, Table.TableFunction<R, C, V, V> var3) {
      Map var4 = this.getColMapForWrite(var1);
      Object var5 = var4.computeIfPresent(var2, (var3x, var4x) -> {
         return var3.compose(var1, var2, var4x);
      });
      this.removeIfEmpty(var1, var4);
      return var5;
   }

   public V compute(R var1, C var2, Table.TableFunction<R, C, V, V> var3) {
      Map var4 = this.getColMapForWrite(var1);
      Object var5 = var4.compute(var2, (var3x, var4x) -> {
         return var3.compose(var1, var2, var4x);
      });
      this.removeIfEmpty(var1, var4);
      return var5;
   }

   public V merge(R var1, C var2, V var3, Table.TableFunction<R, C, V, V> var4) {
      Map var5 = this.getColMapForWrite(var1);
      Object var6 = var5.merge(var2, var3, (var3x, var4x) -> {
         return var4.compose(var1, var2, var4x);
      });
      this.removeIfEmpty(var1, var5);
      return var6;
   }

   public Map<C, V> row(final R var1) {
      final HashMap var2 = new HashMap(0);
      return new DelegatingMap<C, V>() {
         public Map<C, V> delegate(boolean var1x) {
            return var1x ? (Map)Table.this.rowMap.getOrDefault(var1, var2) : Table.this.getColMapForWrite(var1);
         }

         public V remove(Object var1x) {
            Map var2x = this.delegate(false);
            Object var3 = var2x.remove(var1x);
            Table.this.removeIfEmpty(var1, var2x);
            return var3;
         }
      };
   }

   private V getIfExists(R var1, C var2) {
      Map var3 = this.getColMapIfExists(var1);
      return var3 == null ? null : var3.get(var2);
   }

   private Map<C, V> getColMapIfExists(R var1) {
      Map var2 = (Map)this.rowMap.get(var1);
      if (var2 != null && var2.isEmpty()) {
         this.rowMap.remove(var1);
         var2 = null;
      }

      return var2;
   }

   private Map<C, V> getColMapForWrite(R var1) {
      return (Map)this.rowMap.computeIfAbsent(var1, this.colMapSupplier);
   }

   private void removeIfEmpty(R var1, Map<C, V> var2) {
      if (var2.isEmpty()) {
         this.rowMap.remove(var1);
      }

   }

   private class Node implements Table.Entry<R, C, V> {
      private final java.util.Map.Entry<R, Map<C, V>> rowEntry;
      private final java.util.Map.Entry<C, V> colEntry;

      Node(java.util.Map.Entry<R, Map<C, V>> var2, java.util.Map.Entry<C, V> var3) {
         this.rowEntry = var2;
         this.colEntry = var3;
      }

      public final R getRow() {
         return this.rowEntry.getKey();
      }

      public final C getCol() {
         return this.colEntry.getKey();
      }

      public final V getValue() {
         return this.colEntry.getValue();
      }

      public final V setValue(V var1) {
         return this.colEntry.setValue(var1);
      }
   }

   public interface Entry<R, C, V> {
      R getRow();

      C getCol();

      V getValue();

      V setValue(V var1);
   }

   public interface TableConsumer<R, C, V> {
      void accept(R var1, C var2, V var3);
   }

   public interface TableFunction<R, C, V, RETURN> {
      RETURN compose(R var1, C var2, V var3);
   }

   public interface TablePredicate<R, C, V> {
      boolean test(R var1, C var2, V var3);
   }
}
