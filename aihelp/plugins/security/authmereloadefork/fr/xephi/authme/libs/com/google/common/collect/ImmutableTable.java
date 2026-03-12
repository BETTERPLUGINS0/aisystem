package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.MoreObjects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotCall;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ImmutableTable<R, C, V> extends AbstractTable<R, C, V> implements Serializable {
   public static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction) {
      return TableCollectors.toImmutableTable(rowFunction, columnFunction, valueFunction);
   }

   public static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
      return TableCollectors.toImmutableTable(rowFunction, columnFunction, valueFunction, mergeFunction);
   }

   public static <R, C, V> ImmutableTable<R, C, V> of() {
      return SparseImmutableTable.EMPTY;
   }

   public static <R, C, V> ImmutableTable<R, C, V> of(R rowKey, C columnKey, V value) {
      return new SingletonImmutableTable(rowKey, columnKey, value);
   }

   public static <R, C, V> ImmutableTable<R, C, V> copyOf(Table<? extends R, ? extends C, ? extends V> table) {
      if (table instanceof ImmutableTable) {
         ImmutableTable<R, C, V> parameterizedTable = (ImmutableTable)table;
         return parameterizedTable;
      } else {
         return copyOf((Iterable)table.cellSet());
      }
   }

   static <R, C, V> ImmutableTable<R, C, V> copyOf(Iterable<? extends Table.Cell<? extends R, ? extends C, ? extends V>> cells) {
      ImmutableTable.Builder<R, C, V> builder = builder();
      Iterator var2 = cells.iterator();

      while(var2.hasNext()) {
         Table.Cell<? extends R, ? extends C, ? extends V> cell = (Table.Cell)var2.next();
         builder.put(cell);
      }

      return builder.build();
   }

   public static <R, C, V> ImmutableTable.Builder<R, C, V> builder() {
      return new ImmutableTable.Builder();
   }

   static <R, C, V> Table.Cell<R, C, V> cellOf(R rowKey, C columnKey, V value) {
      return Tables.immutableCell(Preconditions.checkNotNull(rowKey, "rowKey"), Preconditions.checkNotNull(columnKey, "columnKey"), Preconditions.checkNotNull(value, "value"));
   }

   ImmutableTable() {
   }

   public ImmutableSet<Table.Cell<R, C, V>> cellSet() {
      return (ImmutableSet)super.cellSet();
   }

   abstract ImmutableSet<Table.Cell<R, C, V>> createCellSet();

   final UnmodifiableIterator<Table.Cell<R, C, V>> cellIterator() {
      throw new AssertionError("should never be called");
   }

   final Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
      throw new AssertionError("should never be called");
   }

   public ImmutableCollection<V> values() {
      return (ImmutableCollection)super.values();
   }

   abstract ImmutableCollection<V> createValues();

   final Iterator<V> valuesIterator() {
      throw new AssertionError("should never be called");
   }

   public ImmutableMap<R, V> column(C columnKey) {
      Preconditions.checkNotNull(columnKey, "columnKey");
      return (ImmutableMap)MoreObjects.firstNonNull((ImmutableMap)this.columnMap().get(columnKey), ImmutableMap.of());
   }

   public ImmutableSet<C> columnKeySet() {
      return this.columnMap().keySet();
   }

   public abstract ImmutableMap<C, Map<R, V>> columnMap();

   public ImmutableMap<C, V> row(R rowKey) {
      Preconditions.checkNotNull(rowKey, "rowKey");
      return (ImmutableMap)MoreObjects.firstNonNull((ImmutableMap)this.rowMap().get(rowKey), ImmutableMap.of());
   }

   public ImmutableSet<R> rowKeySet() {
      return this.rowMap().keySet();
   }

   public abstract ImmutableMap<R, Map<C, V>> rowMap();

   public boolean contains(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
      return this.get(rowKey, columnKey) != null;
   }

   public boolean containsValue(@CheckForNull Object value) {
      return this.values().contains(value);
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final void clear() {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CheckForNull
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final V put(R rowKey, C columnKey, V value) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final void putAll(Table<? extends R, ? extends C, ? extends V> table) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CheckForNull
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final V remove(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
      throw new UnsupportedOperationException();
   }

   abstract ImmutableTable.SerializedForm createSerializedForm();

   final Object writeReplace() {
      return this.createSerializedForm();
   }

   static final class SerializedForm implements Serializable {
      private final Object[] rowKeys;
      private final Object[] columnKeys;
      private final Object[] cellValues;
      private final int[] cellRowIndices;
      private final int[] cellColumnIndices;
      private static final long serialVersionUID = 0L;

      private SerializedForm(Object[] rowKeys, Object[] columnKeys, Object[] cellValues, int[] cellRowIndices, int[] cellColumnIndices) {
         this.rowKeys = rowKeys;
         this.columnKeys = columnKeys;
         this.cellValues = cellValues;
         this.cellRowIndices = cellRowIndices;
         this.cellColumnIndices = cellColumnIndices;
      }

      static ImmutableTable.SerializedForm create(ImmutableTable<?, ?, ?> table, int[] cellRowIndices, int[] cellColumnIndices) {
         return new ImmutableTable.SerializedForm(table.rowKeySet().toArray(), table.columnKeySet().toArray(), table.values().toArray(), cellRowIndices, cellColumnIndices);
      }

      Object readResolve() {
         if (this.cellValues.length == 0) {
            return ImmutableTable.of();
         } else if (this.cellValues.length == 1) {
            return ImmutableTable.of(this.rowKeys[0], this.columnKeys[0], this.cellValues[0]);
         } else {
            ImmutableList.Builder<Table.Cell<Object, Object, Object>> cellListBuilder = new ImmutableList.Builder(this.cellValues.length);

            for(int i = 0; i < this.cellValues.length; ++i) {
               cellListBuilder.add((Object)ImmutableTable.cellOf(this.rowKeys[this.cellRowIndices[i]], this.columnKeys[this.cellColumnIndices[i]], this.cellValues[i]));
            }

            return RegularImmutableTable.forOrderedComponents(cellListBuilder.build(), ImmutableSet.copyOf(this.rowKeys), ImmutableSet.copyOf(this.columnKeys));
         }
      }
   }

   @DoNotMock
   public static final class Builder<R, C, V> {
      private final List<Table.Cell<R, C, V>> cells = Lists.newArrayList();
      @CheckForNull
      private Comparator<? super R> rowComparator;
      @CheckForNull
      private Comparator<? super C> columnComparator;

      @CanIgnoreReturnValue
      public ImmutableTable.Builder<R, C, V> orderRowsBy(Comparator<? super R> rowComparator) {
         this.rowComparator = (Comparator)Preconditions.checkNotNull(rowComparator, "rowComparator");
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableTable.Builder<R, C, V> orderColumnsBy(Comparator<? super C> columnComparator) {
         this.columnComparator = (Comparator)Preconditions.checkNotNull(columnComparator, "columnComparator");
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableTable.Builder<R, C, V> put(R rowKey, C columnKey, V value) {
         this.cells.add(ImmutableTable.cellOf(rowKey, columnKey, value));
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableTable.Builder<R, C, V> put(Table.Cell<? extends R, ? extends C, ? extends V> cell) {
         if (cell instanceof Tables.ImmutableCell) {
            Preconditions.checkNotNull(cell.getRowKey(), "row");
            Preconditions.checkNotNull(cell.getColumnKey(), "column");
            Preconditions.checkNotNull(cell.getValue(), "value");
            this.cells.add(cell);
         } else {
            this.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
         }

         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableTable.Builder<R, C, V> putAll(Table<? extends R, ? extends C, ? extends V> table) {
         Iterator var2 = table.cellSet().iterator();

         while(var2.hasNext()) {
            Table.Cell<? extends R, ? extends C, ? extends V> cell = (Table.Cell)var2.next();
            this.put(cell);
         }

         return this;
      }

      @CanIgnoreReturnValue
      ImmutableTable.Builder<R, C, V> combine(ImmutableTable.Builder<R, C, V> other) {
         this.cells.addAll(other.cells);
         return this;
      }

      public ImmutableTable<R, C, V> build() {
         return this.buildOrThrow();
      }

      public ImmutableTable<R, C, V> buildOrThrow() {
         int size = this.cells.size();
         switch(size) {
         case 0:
            return ImmutableTable.of();
         case 1:
            return new SingletonImmutableTable((Table.Cell)Iterables.getOnlyElement(this.cells));
         default:
            return RegularImmutableTable.forCells(this.cells, this.rowComparator, this.columnComparator);
         }
      }
   }
}
