package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true
)
public class HashBasedTable<R, C, V> extends StandardTable<R, C, V> {
   private static final long serialVersionUID = 0L;

   public static <R, C, V> HashBasedTable<R, C, V> create() {
      return new HashBasedTable(new LinkedHashMap(), new HashBasedTable.Factory(0));
   }

   public static <R, C, V> HashBasedTable<R, C, V> create(int expectedRows, int expectedCellsPerRow) {
      CollectPreconditions.checkNonnegative(expectedCellsPerRow, "expectedCellsPerRow");
      Map<R, Map<C, V>> backingMap = Maps.newLinkedHashMapWithExpectedSize(expectedRows);
      return new HashBasedTable(backingMap, new HashBasedTable.Factory(expectedCellsPerRow));
   }

   public static <R, C, V> HashBasedTable<R, C, V> create(Table<? extends R, ? extends C, ? extends V> table) {
      HashBasedTable<R, C, V> result = create();
      result.putAll(table);
      return result;
   }

   HashBasedTable(Map<R, Map<C, V>> backingMap, HashBasedTable.Factory<C, V> factory) {
      super(backingMap, factory);
   }

   private static class Factory<C, V> implements Supplier<Map<C, V>>, Serializable {
      final int expectedSize;
      private static final long serialVersionUID = 0L;

      Factory(int expectedSize) {
         this.expectedSize = expectedSize;
      }

      public Map<C, V> get() {
         return Maps.newLinkedHashMapWithExpectedSize(this.expectedSize);
      }
   }
}
