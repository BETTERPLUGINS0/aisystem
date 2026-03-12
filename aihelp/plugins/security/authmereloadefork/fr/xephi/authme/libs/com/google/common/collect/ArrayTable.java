package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Objects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotCall;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
@GwtCompatible(
   emulated = true
)
public final class ArrayTable<R, C, V> extends AbstractTable<R, C, V> implements Serializable {
   private final ImmutableList<R> rowList;
   private final ImmutableList<C> columnList;
   private final ImmutableMap<R, Integer> rowKeyToIndex;
   private final ImmutableMap<C, Integer> columnKeyToIndex;
   private final V[][] array;
   @CheckForNull
   private transient ArrayTable<R, C, V>.ColumnMap columnMap;
   @CheckForNull
   private transient ArrayTable<R, C, V>.RowMap rowMap;
   private static final long serialVersionUID = 0L;

   public static <R, C, V> ArrayTable<R, C, V> create(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
      return new ArrayTable(rowKeys, columnKeys);
   }

   public static <R, C, V> ArrayTable<R, C, V> create(Table<R, C, ? extends V> table) {
      return table instanceof ArrayTable ? new ArrayTable((ArrayTable)table) : new ArrayTable(table);
   }

   private ArrayTable(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
      this.rowList = ImmutableList.copyOf(rowKeys);
      this.columnList = ImmutableList.copyOf(columnKeys);
      Preconditions.checkArgument(this.rowList.isEmpty() == this.columnList.isEmpty());
      this.rowKeyToIndex = Maps.indexMap(this.rowList);
      this.columnKeyToIndex = Maps.indexMap(this.columnList);
      V[][] tmpArray = new Object[this.rowList.size()][this.columnList.size()];
      this.array = tmpArray;
      this.eraseAll();
   }

   private ArrayTable(Table<R, C, ? extends V> table) {
      this(table.rowKeySet(), table.columnKeySet());
      this.putAll(table);
   }

   private ArrayTable(ArrayTable<R, C, V> table) {
      this.rowList = table.rowList;
      this.columnList = table.columnList;
      this.rowKeyToIndex = table.rowKeyToIndex;
      this.columnKeyToIndex = table.columnKeyToIndex;
      V[][] copy = new Object[this.rowList.size()][this.columnList.size()];
      this.array = copy;

      for(int i = 0; i < this.rowList.size(); ++i) {
         System.arraycopy(table.array[i], 0, copy[i], 0, table.array[i].length);
      }

   }

   public ImmutableList<R> rowKeyList() {
      return this.rowList;
   }

   public ImmutableList<C> columnKeyList() {
      return this.columnList;
   }

   @CheckForNull
   public V at(int rowIndex, int columnIndex) {
      Preconditions.checkElementIndex(rowIndex, this.rowList.size());
      Preconditions.checkElementIndex(columnIndex, this.columnList.size());
      return this.array[rowIndex][columnIndex];
   }

   @CheckForNull
   @CanIgnoreReturnValue
   public V set(int rowIndex, int columnIndex, @CheckForNull V value) {
      Preconditions.checkElementIndex(rowIndex, this.rowList.size());
      Preconditions.checkElementIndex(columnIndex, this.columnList.size());
      V oldValue = this.array[rowIndex][columnIndex];
      this.array[rowIndex][columnIndex] = value;
      return oldValue;
   }

   @GwtIncompatible
   public V[][] toArray(Class<V> valueClass) {
      V[][] copy = (Object[][])Array.newInstance(valueClass, new int[]{this.rowList.size(), this.columnList.size()});

      for(int i = 0; i < this.rowList.size(); ++i) {
         System.arraycopy(this.array[i], 0, copy[i], 0, this.array[i].length);
      }

      return copy;
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public void clear() {
      throw new UnsupportedOperationException();
   }

   public void eraseAll() {
      Object[][] var1 = this.array;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         V[] row = var1[var3];
         Arrays.fill(row, (Object)null);
      }

   }

   public boolean contains(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
      return this.containsRow(rowKey) && this.containsColumn(columnKey);
   }

   public boolean containsColumn(@CheckForNull Object columnKey) {
      return this.columnKeyToIndex.containsKey(columnKey);
   }

   public boolean containsRow(@CheckForNull Object rowKey) {
      return this.rowKeyToIndex.containsKey(rowKey);
   }

   public boolean containsValue(@CheckForNull Object value) {
      Object[][] var2 = this.array;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         V[] row = var2[var4];
         Object[] var6 = row;
         int var7 = row.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            V element = var6[var8];
            if (Objects.equal(value, element)) {
               return true;
            }
         }
      }

      return false;
   }

   @CheckForNull
   public V get(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
      Integer rowIndex = (Integer)this.rowKeyToIndex.get(rowKey);
      Integer columnIndex = (Integer)this.columnKeyToIndex.get(columnKey);
      return rowIndex != null && columnIndex != null ? this.at(rowIndex, columnIndex) : null;
   }

   public boolean isEmpty() {
      return this.rowList.isEmpty() || this.columnList.isEmpty();
   }

   @CheckForNull
   @CanIgnoreReturnValue
   public V put(R rowKey, C columnKey, @CheckForNull V value) {
      Preconditions.checkNotNull(rowKey);
      Preconditions.checkNotNull(columnKey);
      Integer rowIndex = (Integer)this.rowKeyToIndex.get(rowKey);
      Preconditions.checkArgument(rowIndex != null, "Row %s not in %s", rowKey, this.rowList);
      Integer columnIndex = (Integer)this.columnKeyToIndex.get(columnKey);
      Preconditions.checkArgument(columnIndex != null, "Column %s not in %s", columnKey, this.columnList);
      return this.set(rowIndex, columnIndex, value);
   }

   public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
      super.putAll(table);
   }

   /** @deprecated */
   @Deprecated
   @CheckForNull
   @DoNotCall("Always throws UnsupportedOperationException")
   @CanIgnoreReturnValue
   public V remove(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
      throw new UnsupportedOperationException();
   }

   @CheckForNull
   @CanIgnoreReturnValue
   public V erase(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
      Integer rowIndex = (Integer)this.rowKeyToIndex.get(rowKey);
      Integer columnIndex = (Integer)this.columnKeyToIndex.get(columnKey);
      return rowIndex != null && columnIndex != null ? this.set(rowIndex, columnIndex, (Object)null) : null;
   }

   public int size() {
      return this.rowList.size() * this.columnList.size();
   }

   public Set<Table.Cell<R, C, V>> cellSet() {
      return super.cellSet();
   }

   Iterator<Table.Cell<R, C, V>> cellIterator() {
      return new AbstractIndexedListIterator<Table.Cell<R, C, V>>(this.size()) {
         protected Table.Cell<R, C, V> get(int index) {
            return ArrayTable.this.getCell(index);
         }
      };
   }

   Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
      return CollectSpliterators.indexed(this.size(), 273, this::getCell);
   }

   private Table.Cell<R, C, V> getCell(final int index) {
      return new Tables.AbstractCell<R, C, V>() {
         final int rowIndex;
         final int columnIndex;

         {
            this.rowIndex = index / ArrayTable.this.columnList.size();
            this.columnIndex = index % ArrayTable.this.columnList.size();
         }

         public R getRowKey() {
            return ArrayTable.this.rowList.get(this.rowIndex);
         }

         public C getColumnKey() {
            return ArrayTable.this.columnList.get(this.columnIndex);
         }

         @CheckForNull
         public V getValue() {
            return ArrayTable.this.at(this.rowIndex, this.columnIndex);
         }
      };
   }

   @CheckForNull
   private V getValue(int index) {
      int rowIndex = index / this.columnList.size();
      int columnIndex = index % this.columnList.size();
      return this.at(rowIndex, columnIndex);
   }

   public Map<R, V> column(C columnKey) {
      Preconditions.checkNotNull(columnKey);
      Integer columnIndex = (Integer)this.columnKeyToIndex.get(columnKey);
      return (Map)(columnIndex == null ? Collections.emptyMap() : new ArrayTable.Column(columnIndex));
   }

   public ImmutableSet<C> columnKeySet() {
      return this.columnKeyToIndex.keySet();
   }

   public Map<C, Map<R, V>> columnMap() {
      ArrayTable<R, C, V>.ColumnMap map = this.columnMap;
      return map == null ? (this.columnMap = new ArrayTable.ColumnMap()) : map;
   }

   public Map<C, V> row(R rowKey) {
      Preconditions.checkNotNull(rowKey);
      Integer rowIndex = (Integer)this.rowKeyToIndex.get(rowKey);
      return (Map)(rowIndex == null ? Collections.emptyMap() : new ArrayTable.Row(rowIndex));
   }

   public ImmutableSet<R> rowKeySet() {
      return this.rowKeyToIndex.keySet();
   }

   public Map<R, Map<C, V>> rowMap() {
      ArrayTable<R, C, V>.RowMap map = this.rowMap;
      return map == null ? (this.rowMap = new ArrayTable.RowMap()) : map;
   }

   public Collection<V> values() {
      return super.values();
   }

   Iterator<V> valuesIterator() {
      return new AbstractIndexedListIterator<V>(this.size()) {
         @CheckForNull
         protected V get(int index) {
            return ArrayTable.this.getValue(index);
         }
      };
   }

   Spliterator<V> valuesSpliterator() {
      return CollectSpliterators.indexed(this.size(), 16, this::getValue);
   }

   private class RowMap extends ArrayTable.ArrayMap<R, Map<C, V>> {
      private RowMap() {
         super(ArrayTable.this.rowKeyToIndex, null);
      }

      String getKeyRole() {
         return "Row";
      }

      Map<C, V> getValue(int index) {
         return ArrayTable.this.new Row(index);
      }

      Map<C, V> setValue(int index, Map<C, V> newValue) {
         throw new UnsupportedOperationException();
      }

      @CheckForNull
      public Map<C, V> put(R key, Map<C, V> value) {
         throw new UnsupportedOperationException();
      }

      // $FF: synthetic method
      RowMap(Object x1) {
         this();
      }
   }

   private class Row extends ArrayTable.ArrayMap<C, V> {
      final int rowIndex;

      Row(int rowIndex) {
         super(ArrayTable.this.columnKeyToIndex, null);
         this.rowIndex = rowIndex;
      }

      String getKeyRole() {
         return "Column";
      }

      @CheckForNull
      V getValue(int index) {
         return ArrayTable.this.at(this.rowIndex, index);
      }

      @CheckForNull
      V setValue(int index, @CheckForNull V newValue) {
         return ArrayTable.this.set(this.rowIndex, index, newValue);
      }
   }

   private class ColumnMap extends ArrayTable.ArrayMap<C, Map<R, V>> {
      private ColumnMap() {
         super(ArrayTable.this.columnKeyToIndex, null);
      }

      String getKeyRole() {
         return "Column";
      }

      Map<R, V> getValue(int index) {
         return ArrayTable.this.new Column(index);
      }

      Map<R, V> setValue(int index, Map<R, V> newValue) {
         throw new UnsupportedOperationException();
      }

      @CheckForNull
      public Map<R, V> put(C key, Map<R, V> value) {
         throw new UnsupportedOperationException();
      }

      // $FF: synthetic method
      ColumnMap(Object x1) {
         this();
      }
   }

   private class Column extends ArrayTable.ArrayMap<R, V> {
      final int columnIndex;

      Column(int columnIndex) {
         super(ArrayTable.this.rowKeyToIndex, null);
         this.columnIndex = columnIndex;
      }

      String getKeyRole() {
         return "Row";
      }

      @CheckForNull
      V getValue(int index) {
         return ArrayTable.this.at(index, this.columnIndex);
      }

      @CheckForNull
      V setValue(int index, @CheckForNull V newValue) {
         return ArrayTable.this.set(index, this.columnIndex, newValue);
      }
   }

   private abstract static class ArrayMap<K, V> extends Maps.IteratorBasedAbstractMap<K, V> {
      private final ImmutableMap<K, Integer> keyIndex;

      private ArrayMap(ImmutableMap<K, Integer> keyIndex) {
         this.keyIndex = keyIndex;
      }

      public Set<K> keySet() {
         return this.keyIndex.keySet();
      }

      K getKey(int index) {
         return this.keyIndex.keySet().asList().get(index);
      }

      abstract String getKeyRole();

      @ParametricNullness
      abstract V getValue(int var1);

      @ParametricNullness
      abstract V setValue(int var1, @ParametricNullness V var2);

      public int size() {
         return this.keyIndex.size();
      }

      public boolean isEmpty() {
         return this.keyIndex.isEmpty();
      }

      Entry<K, V> getEntry(final int index) {
         Preconditions.checkElementIndex(index, this.size());
         return new AbstractMapEntry<K, V>() {
            public K getKey() {
               return ArrayMap.this.getKey(index);
            }

            @ParametricNullness
            public V getValue() {
               return ArrayMap.this.getValue(index);
            }

            @ParametricNullness
            public V setValue(@ParametricNullness V value) {
               return ArrayMap.this.setValue(index, value);
            }
         };
      }

      Iterator<Entry<K, V>> entryIterator() {
         return new AbstractIndexedListIterator<Entry<K, V>>(this.size()) {
            protected Entry<K, V> get(int index) {
               return ArrayMap.this.getEntry(index);
            }
         };
      }

      Spliterator<Entry<K, V>> entrySpliterator() {
         return CollectSpliterators.indexed(this.size(), 16, this::getEntry);
      }

      public boolean containsKey(@CheckForNull Object key) {
         return this.keyIndex.containsKey(key);
      }

      @CheckForNull
      public V get(@CheckForNull Object key) {
         Integer index = (Integer)this.keyIndex.get(key);
         return index == null ? null : this.getValue(index);
      }

      @CheckForNull
      public V put(K key, @ParametricNullness V value) {
         Integer index = (Integer)this.keyIndex.get(key);
         if (index == null) {
            String var4 = this.getKeyRole();
            String var5 = String.valueOf(key);
            String var6 = String.valueOf(this.keyIndex.keySet());
            throw new IllegalArgumentException((new StringBuilder(9 + String.valueOf(var4).length() + String.valueOf(var5).length() + String.valueOf(var6).length())).append(var4).append(" ").append(var5).append(" not in ").append(var6).toString());
         } else {
            return this.setValue(index, value);
         }
      }

      @CheckForNull
      public V remove(@CheckForNull Object key) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         throw new UnsupportedOperationException();
      }

      // $FF: synthetic method
      ArrayMap(ImmutableMap x0, Object x1) {
         this(x0);
      }
   }
}
