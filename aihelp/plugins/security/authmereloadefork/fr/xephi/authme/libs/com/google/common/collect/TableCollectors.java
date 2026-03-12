package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class TableCollectors {
   static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction) {
      Preconditions.checkNotNull(rowFunction, "rowFunction");
      Preconditions.checkNotNull(columnFunction, "columnFunction");
      Preconditions.checkNotNull(valueFunction, "valueFunction");
      return Collector.of(ImmutableTable.Builder::new, (builder, t) -> {
         builder.put(rowFunction.apply(t), columnFunction.apply(t), valueFunction.apply(t));
      }, ImmutableTable.Builder::combine, ImmutableTable.Builder::build);
   }

   static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
      Preconditions.checkNotNull(rowFunction, "rowFunction");
      Preconditions.checkNotNull(columnFunction, "columnFunction");
      Preconditions.checkNotNull(valueFunction, "valueFunction");
      Preconditions.checkNotNull(mergeFunction, "mergeFunction");
      return Collector.of(() -> {
         return new TableCollectors.ImmutableTableCollectorState();
      }, (state, input) -> {
         state.put(rowFunction.apply(input), columnFunction.apply(input), valueFunction.apply(input), mergeFunction);
      }, (s1, s2) -> {
         return s1.combine(s2, mergeFunction);
      }, (state) -> {
         return state.toTable();
      });
   }

   static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, Supplier<I> tableSupplier) {
      return toTable(rowFunction, columnFunction, valueFunction, (v1, v2) -> {
         String var2 = String.valueOf(v1);
         String var3 = String.valueOf(v2);
         throw new IllegalStateException((new StringBuilder(24 + String.valueOf(var2).length() + String.valueOf(var3).length())).append("Conflicting values ").append(var2).append(" and ").append(var3).toString());
      }, tableSupplier);
   }

   static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction, Supplier<I> tableSupplier) {
      Preconditions.checkNotNull(rowFunction);
      Preconditions.checkNotNull(columnFunction);
      Preconditions.checkNotNull(valueFunction);
      Preconditions.checkNotNull(mergeFunction);
      Preconditions.checkNotNull(tableSupplier);
      return Collector.of(tableSupplier, (table, input) -> {
         mergeTables(table, rowFunction.apply(input), columnFunction.apply(input), valueFunction.apply(input), mergeFunction);
      }, (table1, table2) -> {
         Iterator var3 = table2.cellSet().iterator();

         while(var3.hasNext()) {
            Table.Cell<R, C, V> cell2 = (Table.Cell)var3.next();
            mergeTables(table1, cell2.getRowKey(), cell2.getColumnKey(), cell2.getValue(), mergeFunction);
         }

         return table1;
      });
   }

   private static <R, C, V> void mergeTables(Table<R, C, V> table, @ParametricNullness R row, @ParametricNullness C column, @ParametricNullness V value, BinaryOperator<V> mergeFunction) {
      Preconditions.checkNotNull(value);
      V oldValue = table.get(row, column);
      if (oldValue == null) {
         table.put(row, column, value);
      } else {
         V newValue = mergeFunction.apply(oldValue, value);
         if (newValue == null) {
            table.remove(row, column);
         } else {
            table.put(row, column, newValue);
         }
      }

   }

   private TableCollectors() {
   }

   private static final class MutableCell<R, C, V> extends Tables.AbstractCell<R, C, V> {
      private final R row;
      private final C column;
      private V value;

      MutableCell(R row, C column, V value) {
         this.row = Preconditions.checkNotNull(row, "row");
         this.column = Preconditions.checkNotNull(column, "column");
         this.value = Preconditions.checkNotNull(value, "value");
      }

      public R getRowKey() {
         return this.row;
      }

      public C getColumnKey() {
         return this.column;
      }

      public V getValue() {
         return this.value;
      }

      void merge(V value, BinaryOperator<V> mergeFunction) {
         Preconditions.checkNotNull(value, "value");
         this.value = Preconditions.checkNotNull(mergeFunction.apply(this.value, value), "mergeFunction.apply");
      }
   }

   private static final class ImmutableTableCollectorState<R, C, V> {
      final List<TableCollectors.MutableCell<R, C, V>> insertionOrder;
      final Table<R, C, TableCollectors.MutableCell<R, C, V>> table;

      private ImmutableTableCollectorState() {
         this.insertionOrder = new ArrayList();
         this.table = HashBasedTable.create();
      }

      void put(R row, C column, V value, BinaryOperator<V> merger) {
         TableCollectors.MutableCell<R, C, V> oldCell = (TableCollectors.MutableCell)this.table.get(row, column);
         if (oldCell == null) {
            TableCollectors.MutableCell<R, C, V> cell = new TableCollectors.MutableCell(row, column, value);
            this.insertionOrder.add(cell);
            this.table.put(row, column, cell);
         } else {
            oldCell.merge(value, merger);
         }

      }

      TableCollectors.ImmutableTableCollectorState<R, C, V> combine(TableCollectors.ImmutableTableCollectorState<R, C, V> other, BinaryOperator<V> merger) {
         Iterator var3 = other.insertionOrder.iterator();

         while(var3.hasNext()) {
            TableCollectors.MutableCell<R, C, V> cell = (TableCollectors.MutableCell)var3.next();
            this.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue(), merger);
         }

         return this;
      }

      ImmutableTable<R, C, V> toTable() {
         return ImmutableTable.copyOf((Iterable)this.insertionOrder);
      }

      // $FF: synthetic method
      ImmutableTableCollectorState(Object x0) {
         this();
      }
   }
}
