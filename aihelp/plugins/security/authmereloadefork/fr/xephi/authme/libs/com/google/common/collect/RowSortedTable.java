package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface RowSortedTable<R, C, V> extends Table<R, C, V> {
   SortedSet<R> rowKeySet();

   SortedMap<R, Map<C, V>> rowMap();
}
