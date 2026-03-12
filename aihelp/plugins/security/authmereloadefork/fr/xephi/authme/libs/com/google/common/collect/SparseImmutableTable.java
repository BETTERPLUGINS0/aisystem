package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.Immutable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

@Immutable(
   containerOf = {"R", "C", "V"}
)
@ElementTypesAreNonnullByDefault
@GwtCompatible
final class SparseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
   static final ImmutableTable<Object, Object, Object> EMPTY = new SparseImmutableTable(ImmutableList.of(), ImmutableSet.of(), ImmutableSet.of());
   private final ImmutableMap<R, ImmutableMap<C, V>> rowMap;
   private final ImmutableMap<C, ImmutableMap<R, V>> columnMap;
   private final int[] cellRowIndices;
   private final int[] cellColumnInRowIndices;

   SparseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
      Map<R, Integer> rowIndex = Maps.indexMap(rowSpace);
      Map<R, Map<C, V>> rows = Maps.newLinkedHashMap();
      UnmodifiableIterator var6 = rowSpace.iterator();

      while(var6.hasNext()) {
         R row = var6.next();
         rows.put(row, new LinkedHashMap());
      }

      Map<C, Map<R, V>> columns = Maps.newLinkedHashMap();
      UnmodifiableIterator var17 = columnSpace.iterator();

      while(var17.hasNext()) {
         C col = var17.next();
         columns.put(col, new LinkedHashMap());
      }

      int[] cellRowIndices = new int[cellList.size()];
      int[] cellColumnInRowIndices = new int[cellList.size()];

      for(int i = 0; i < cellList.size(); ++i) {
         Table.Cell<R, C, V> cell = (Table.Cell)cellList.get(i);
         R rowKey = cell.getRowKey();
         C columnKey = cell.getColumnKey();
         V value = cell.getValue();
         cellRowIndices[i] = (Integer)Objects.requireNonNull((Integer)rowIndex.get(rowKey));
         Map<C, V> thisRow = (Map)Objects.requireNonNull((Map)rows.get(rowKey));
         cellColumnInRowIndices[i] = thisRow.size();
         V oldValue = thisRow.put(columnKey, value);
         this.checkNoDuplicate(rowKey, columnKey, oldValue, value);
         ((Map)Objects.requireNonNull((Map)columns.get(columnKey))).put(rowKey, value);
      }

      this.cellRowIndices = cellRowIndices;
      this.cellColumnInRowIndices = cellColumnInRowIndices;
      ImmutableMap.Builder<R, ImmutableMap<C, V>> rowBuilder = new ImmutableMap.Builder(rows.size());
      Iterator var21 = rows.entrySet().iterator();

      while(var21.hasNext()) {
         Entry<R, Map<C, V>> row = (Entry)var21.next();
         rowBuilder.put(row.getKey(), ImmutableMap.copyOf((Map)row.getValue()));
      }

      this.rowMap = rowBuilder.buildOrThrow();
      ImmutableMap.Builder<C, ImmutableMap<R, V>> columnBuilder = new ImmutableMap.Builder(columns.size());
      Iterator var24 = columns.entrySet().iterator();

      while(var24.hasNext()) {
         Entry<C, Map<R, V>> col = (Entry)var24.next();
         columnBuilder.put(col.getKey(), ImmutableMap.copyOf((Map)col.getValue()));
      }

      this.columnMap = columnBuilder.buildOrThrow();
   }

   public ImmutableMap<C, Map<R, V>> columnMap() {
      ImmutableMap<C, ImmutableMap<R, V>> columnMap = this.columnMap;
      return ImmutableMap.copyOf((Map)columnMap);
   }

   public ImmutableMap<R, Map<C, V>> rowMap() {
      ImmutableMap<R, ImmutableMap<C, V>> rowMap = this.rowMap;
      return ImmutableMap.copyOf((Map)rowMap);
   }

   public int size() {
      return this.cellRowIndices.length;
   }

   Table.Cell<R, C, V> getCell(int index) {
      int rowIndex = this.cellRowIndices[index];
      Entry<R, ImmutableMap<C, V>> rowEntry = (Entry)this.rowMap.entrySet().asList().get(rowIndex);
      ImmutableMap<C, V> row = (ImmutableMap)rowEntry.getValue();
      int columnIndex = this.cellColumnInRowIndices[index];
      Entry<C, V> colEntry = (Entry)row.entrySet().asList().get(columnIndex);
      return cellOf(rowEntry.getKey(), colEntry.getKey(), colEntry.getValue());
   }

   V getValue(int index) {
      int rowIndex = this.cellRowIndices[index];
      ImmutableMap<C, V> row = (ImmutableMap)this.rowMap.values().asList().get(rowIndex);
      int columnIndex = this.cellColumnInRowIndices[index];
      return row.values().asList().get(columnIndex);
   }

   ImmutableTable.SerializedForm createSerializedForm() {
      Map<C, Integer> columnKeyToIndex = Maps.indexMap(this.columnKeySet());
      int[] cellColumnIndices = new int[this.cellSet().size()];
      int i = 0;

      Table.Cell cell;
      for(UnmodifiableIterator var4 = this.cellSet().iterator(); var4.hasNext(); cellColumnIndices[i++] = (Integer)Objects.requireNonNull((Integer)columnKeyToIndex.get(cell.getColumnKey()))) {
         cell = (Table.Cell)var4.next();
      }

      return ImmutableTable.SerializedForm.create(this, this.cellRowIndices, cellColumnIndices);
   }
}
