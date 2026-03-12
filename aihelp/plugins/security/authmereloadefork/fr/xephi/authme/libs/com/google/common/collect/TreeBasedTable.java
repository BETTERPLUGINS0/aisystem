package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true
)
public class TreeBasedTable<R, C, V> extends StandardRowSortedTable<R, C, V> {
   private final Comparator<? super C> columnComparator;
   private static final long serialVersionUID = 0L;

   public static <R extends Comparable, C extends Comparable, V> TreeBasedTable<R, C, V> create() {
      return new TreeBasedTable(Ordering.natural(), Ordering.natural());
   }

   public static <R, C, V> TreeBasedTable<R, C, V> create(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
      Preconditions.checkNotNull(rowComparator);
      Preconditions.checkNotNull(columnComparator);
      return new TreeBasedTable(rowComparator, columnComparator);
   }

   public static <R, C, V> TreeBasedTable<R, C, V> create(TreeBasedTable<R, C, ? extends V> table) {
      TreeBasedTable<R, C, V> result = new TreeBasedTable(table.rowComparator(), table.columnComparator());
      result.putAll(table);
      return result;
   }

   TreeBasedTable(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
      super(new TreeMap(rowComparator), new TreeBasedTable.Factory(columnComparator));
      this.columnComparator = columnComparator;
   }

   /** @deprecated */
   @Deprecated
   public Comparator<? super R> rowComparator() {
      return (Comparator)Objects.requireNonNull(this.rowKeySet().comparator());
   }

   /** @deprecated */
   @Deprecated
   public Comparator<? super C> columnComparator() {
      return this.columnComparator;
   }

   public SortedMap<C, V> row(R rowKey) {
      return new TreeBasedTable.TreeRow(rowKey);
   }

   public SortedSet<R> rowKeySet() {
      return super.rowKeySet();
   }

   public SortedMap<R, Map<C, V>> rowMap() {
      return super.rowMap();
   }

   Iterator<C> createColumnKeyIterator() {
      final Comparator<? super C> comparator = this.columnComparator();
      final Iterator<C> merged = Iterators.mergeSorted(Iterables.transform(this.backingMap.values(), (input) -> {
         return input.keySet().iterator();
      }), comparator);
      return new AbstractIterator<C>(this) {
         @CheckForNull
         C lastValue;

         @CheckForNull
         protected C computeNext() {
            Object next;
            boolean duplicate;
            do {
               if (!merged.hasNext()) {
                  this.lastValue = null;
                  return this.endOfData();
               }

               next = merged.next();
               duplicate = this.lastValue != null && comparator.compare(next, this.lastValue) == 0;
            } while(duplicate);

            this.lastValue = next;
            return this.lastValue;
         }
      };
   }

   private class TreeRow extends StandardTable<R, C, V>.Row implements SortedMap<C, V> {
      @CheckForNull
      final C lowerBound;
      @CheckForNull
      final C upperBound;
      @CheckForNull
      transient SortedMap<C, V> wholeRow;

      TreeRow(R rowKey) {
         this(rowKey, (Object)null, (Object)null);
      }

      TreeRow(R rowKey, @CheckForNull C lowerBound, @CheckForNull C upperBound) {
         super(rowKey);
         this.lowerBound = lowerBound;
         this.upperBound = upperBound;
         Preconditions.checkArgument(lowerBound == null || upperBound == null || this.compare(lowerBound, upperBound) <= 0);
      }

      public SortedSet<C> keySet() {
         return new Maps.SortedKeySet(this);
      }

      public Comparator<? super C> comparator() {
         return TreeBasedTable.this.columnComparator();
      }

      int compare(Object a, Object b) {
         Comparator<Object> cmp = this.comparator();
         return cmp.compare(a, b);
      }

      boolean rangeContains(@CheckForNull Object o) {
         return o != null && (this.lowerBound == null || this.compare(this.lowerBound, o) <= 0) && (this.upperBound == null || this.compare(this.upperBound, o) > 0);
      }

      public SortedMap<C, V> subMap(C fromKey, C toKey) {
         Preconditions.checkArgument(this.rangeContains(Preconditions.checkNotNull(fromKey)) && this.rangeContains(Preconditions.checkNotNull(toKey)));
         return TreeBasedTable.this.new TreeRow(this.rowKey, fromKey, toKey);
      }

      public SortedMap<C, V> headMap(C toKey) {
         Preconditions.checkArgument(this.rangeContains(Preconditions.checkNotNull(toKey)));
         return TreeBasedTable.this.new TreeRow(this.rowKey, this.lowerBound, toKey);
      }

      public SortedMap<C, V> tailMap(C fromKey) {
         Preconditions.checkArgument(this.rangeContains(Preconditions.checkNotNull(fromKey)));
         return TreeBasedTable.this.new TreeRow(this.rowKey, fromKey, this.upperBound);
      }

      public C firstKey() {
         this.updateBackingRowMapField();
         if (this.backingRowMap == null) {
            throw new NoSuchElementException();
         } else {
            return ((SortedMap)this.backingRowMap).firstKey();
         }
      }

      public C lastKey() {
         this.updateBackingRowMapField();
         if (this.backingRowMap == null) {
            throw new NoSuchElementException();
         } else {
            return ((SortedMap)this.backingRowMap).lastKey();
         }
      }

      void updateWholeRowField() {
         if (this.wholeRow == null || this.wholeRow.isEmpty() && TreeBasedTable.this.backingMap.containsKey(this.rowKey)) {
            this.wholeRow = (SortedMap)TreeBasedTable.this.backingMap.get(this.rowKey);
         }

      }

      @CheckForNull
      SortedMap<C, V> computeBackingRowMap() {
         this.updateWholeRowField();
         SortedMap<C, V> map = this.wholeRow;
         if (map != null) {
            if (this.lowerBound != null) {
               map = map.tailMap(this.lowerBound);
            }

            if (this.upperBound != null) {
               map = map.headMap(this.upperBound);
            }

            return map;
         } else {
            return null;
         }
      }

      void maintainEmptyInvariant() {
         this.updateWholeRowField();
         if (this.wholeRow != null && this.wholeRow.isEmpty()) {
            TreeBasedTable.this.backingMap.remove(this.rowKey);
            this.wholeRow = null;
            this.backingRowMap = null;
         }

      }

      public boolean containsKey(@CheckForNull Object key) {
         return this.rangeContains(key) && super.containsKey(key);
      }

      @CheckForNull
      public V put(C key, V value) {
         Preconditions.checkArgument(this.rangeContains(Preconditions.checkNotNull(key)));
         return super.put(key, value);
      }
   }

   private static class Factory<C, V> implements Supplier<TreeMap<C, V>>, Serializable {
      final Comparator<? super C> comparator;
      private static final long serialVersionUID = 0L;

      Factory(Comparator<? super C> comparator) {
         this.comparator = comparator;
      }

      public TreeMap<C, V> get() {
         return new TreeMap(this.comparator);
      }
   }
}
