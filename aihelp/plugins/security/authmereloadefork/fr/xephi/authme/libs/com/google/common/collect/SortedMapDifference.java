package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.util.SortedMap;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface SortedMapDifference<K, V> extends MapDifference<K, V> {
   SortedMap<K, V> entriesOnlyOnLeft();

   SortedMap<K, V> entriesOnlyOnRight();

   SortedMap<K, V> entriesInCommon();

   SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering();
}
