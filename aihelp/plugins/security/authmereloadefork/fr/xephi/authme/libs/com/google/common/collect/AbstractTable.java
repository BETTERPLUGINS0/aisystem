package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AbstractTable<R, C, V> implements Table<R, C, V> {
   @LazyInit
   @CheckForNull
   private transient Set<Table.Cell<R, C, V>> cellSet;
   @LazyInit
   @CheckForNull
   private transient Collection<V> values;

   public boolean containsRow(@CheckForNull Object rowKey) {
      return Maps.safeContainsKey(this.rowMap(), rowKey);
   }

   public boolean containsColumn(@CheckForNull Object columnKey) {
      return Maps.safeContainsKey(this.columnMap(), columnKey);
   }

   public Set<R> rowKeySet() {
      return this.rowMap().keySet();
   }

   public Set<C> columnKeySet() {
      return this.columnMap().keySet();
   }

   public boolean containsValue(@CheckForNull Object value) {
      Iterator var2 = this.rowMap().values().iterator();

      Map row;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         row = (Map)var2.next();
      } while(!row.containsValue(value));

      return true;
   }

   public boolean contains(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
      Map<C, V> row = (Map)Maps.safeGet(this.rowMap(), rowKey);
      return row != null && Maps.safeContainsKey(row, columnKey);
   }

   @CheckForNull
   public V get(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
      Map<C, V> row = (Map)Maps.safeGet(this.rowMap(), rowKey);
      return row == null ? null : Maps.safeGet(row, columnKey);
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public void clear() {
      Iterators.clear(this.cellSet().iterator());
   }

   @CheckForNull
   @CanIgnoreReturnValue
   public V remove(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
      Map<C, V> row = (Map)Maps.safeGet(this.rowMap(), rowKey);
      return row == null ? null : Maps.safeRemove(row, columnKey);
   }

   @CheckForNull
   @CanIgnoreReturnValue
   public V put(@ParametricNullness R rowKey, @ParametricNullness C columnKey, @ParametricNullness V value) {
      return this.row(rowKey).put(columnKey, value);
   }

   public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
      Iterator var2 = table.cellSet().iterator();

      while(var2.hasNext()) {
         Table.Cell<? extends R, ? extends C, ? extends V> cell = (Table.Cell)var2.next();
         this.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
      }

   }

   public Set<Table.Cell<R, C, V>> cellSet() {
      Set<Table.Cell<R, C, V>> result = this.cellSet;
      return result == null ? (this.cellSet = this.createCellSet()) : result;
   }

   Set<Table.Cell<R, C, V>> createCellSet() {
      return new AbstractTable.CellSet();
   }

   abstract Iterator<Table.Cell<R, C, V>> cellIterator();

   abstract Spliterator<Table.Cell<R, C, V>> cellSpliterator();

   public Collection<V> values() {
      Collection<V> result = this.values;
      return result == null ? (this.values = this.createValues()) : result;
   }

   Collection<V> createValues() {
      return new AbstractTable.Values();
   }

   Iterator<V> valuesIterator() {
      return new TransformedIterator<Table.Cell<R, C, V>, V>(this, this.cellSet().iterator()) {
         @ParametricNullness
         V transform(Table.Cell<R, C, V> cell) {
            return cell.getValue();
         }
      };
   }

   Spliterator<V> valuesSpliterator() {
      return CollectSpliterators.map(this.cellSpliterator(), Table.Cell::getValue);
   }

   public boolean equals(@CheckForNull Object obj) {
      return Tables.equalsImpl(this, obj);
   }

   public int hashCode() {
      return this.cellSet().hashCode();
   }

   public String toString() {
      return this.rowMap().toString();
   }

   class Values extends AbstractCollection<V> {
      public Iterator<V> iterator() {
         return AbstractTable.this.valuesIterator();
      }

      public Spliterator<V> spliterator() {
         return AbstractTable.this.valuesSpliterator();
      }

      public boolean contains(@CheckForNull Object o) {
         return AbstractTable.this.containsValue(o);
      }

      public void clear() {
         AbstractTable.this.clear();
      }

      public int size() {
         return AbstractTable.this.size();
      }
   }

   class CellSet extends AbstractSet<Table.Cell<R, C, V>> {
      public boolean contains(@CheckForNull Object o) {
         if (!(o instanceof Table.Cell)) {
            return false;
         } else {
            Table.Cell<?, ?, ?> cell = (Table.Cell)o;
            Map<C, V> row = (Map)Maps.safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
            return row != null && Collections2.safeContains(row.entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue()));
         }
      }

      public boolean remove(@CheckForNull Object o) {
         if (!(o instanceof Table.Cell)) {
            return false;
         } else {
            Table.Cell<?, ?, ?> cell = (Table.Cell)o;
            Map<C, V> row = (Map)Maps.safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
            return row != null && Collections2.safeRemove(row.entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue()));
         }
      }

      public void clear() {
         AbstractTable.this.clear();
      }

      public Iterator<Table.Cell<R, C, V>> iterator() {
         return AbstractTable.this.cellIterator();
      }

      public Spliterator<Table.Cell<R, C, V>> spliterator() {
         return AbstractTable.this.cellSpliterator();
      }

      public int size() {
         return AbstractTable.this.size();
      }
   }
}
