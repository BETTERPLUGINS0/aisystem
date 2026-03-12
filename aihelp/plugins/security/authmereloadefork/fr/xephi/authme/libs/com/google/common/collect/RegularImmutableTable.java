package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {
   abstract Table.Cell<R, C, V> getCell(int var1);

   final ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
      return (ImmutableSet)(this.isEmpty() ? ImmutableSet.of() : new RegularImmutableTable.CellSet());
   }

   abstract V getValue(int var1);

   final ImmutableCollection<V> createValues() {
      return (ImmutableCollection)(this.isEmpty() ? ImmutableList.of() : new RegularImmutableTable.Values());
   }

   static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Table.Cell<R, C, V>> cells, @CheckForNull Comparator<? super R> rowComparator, @CheckForNull Comparator<? super C> columnComparator) {
      Preconditions.checkNotNull(cells);
      if (rowComparator != null || columnComparator != null) {
         Comparator<Table.Cell<R, C, V>> comparator = (cell1, cell2) -> {
            int rowCompare = rowComparator == null ? 0 : rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
            if (rowCompare != 0) {
               return rowCompare;
            } else {
               return columnComparator == null ? 0 : columnComparator.compare(cell1.getColumnKey(), cell2.getColumnKey());
            }
         };
         Collections.sort(cells, comparator);
      }

      return forCellsInternal(cells, rowComparator, columnComparator);
   }

   static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Table.Cell<R, C, V>> cells) {
      return forCellsInternal(cells, (Comparator)null, (Comparator)null);
   }

   private static <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Table.Cell<R, C, V>> cells, @CheckForNull Comparator<? super R> rowComparator, @CheckForNull Comparator<? super C> columnComparator) {
      Set<R> rowSpaceBuilder = new LinkedHashSet();
      Set<C> columnSpaceBuilder = new LinkedHashSet();
      ImmutableList<Table.Cell<R, C, V>> cellList = ImmutableList.copyOf(cells);
      Iterator var6 = cells.iterator();

      while(var6.hasNext()) {
         Table.Cell<R, C, V> cell = (Table.Cell)var6.next();
         rowSpaceBuilder.add(cell.getRowKey());
         columnSpaceBuilder.add(cell.getColumnKey());
      }

      ImmutableSet<R> rowSpace = rowComparator == null ? ImmutableSet.copyOf((Collection)rowSpaceBuilder) : ImmutableSet.copyOf((Collection)ImmutableList.sortedCopyOf(rowComparator, rowSpaceBuilder));
      ImmutableSet<C> columnSpace = columnComparator == null ? ImmutableSet.copyOf((Collection)columnSpaceBuilder) : ImmutableSet.copyOf((Collection)ImmutableList.sortedCopyOf(columnComparator, columnSpaceBuilder));
      return forOrderedComponents(cellList, rowSpace, columnSpace);
   }

   static <R, C, V> RegularImmutableTable<R, C, V> forOrderedComponents(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
      return (RegularImmutableTable)((long)cellList.size() > (long)rowSpace.size() * (long)columnSpace.size() / 2L ? new DenseImmutableTable(cellList, rowSpace, columnSpace) : new SparseImmutableTable(cellList, rowSpace, columnSpace));
   }

   final void checkNoDuplicate(R rowKey, C columnKey, @CheckForNull V existingValue, V newValue) {
      Preconditions.checkArgument(existingValue == null, "Duplicate key: (row=%s, column=%s), values: [%s, %s].", rowKey, columnKey, newValue, existingValue);
   }

   private final class Values extends ImmutableList<V> {
      private Values() {
      }

      public int size() {
         return RegularImmutableTable.this.size();
      }

      public V get(int index) {
         return RegularImmutableTable.this.getValue(index);
      }

      boolean isPartialView() {
         return true;
      }

      // $FF: synthetic method
      Values(Object x1) {
         this();
      }
   }

   private final class CellSet extends IndexedImmutableSet<Table.Cell<R, C, V>> {
      private CellSet() {
      }

      public int size() {
         return RegularImmutableTable.this.size();
      }

      Table.Cell<R, C, V> get(int index) {
         return RegularImmutableTable.this.getCell(index);
      }

      public boolean contains(@CheckForNull Object object) {
         if (!(object instanceof Table.Cell)) {
            return false;
         } else {
            Table.Cell<?, ?, ?> cell = (Table.Cell)object;
            Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
            return value != null && value.equals(cell.getValue());
         }
      }

      boolean isPartialView() {
         return false;
      }

      // $FF: synthetic method
      CellSet(Object x1) {
         this();
      }
   }
}
