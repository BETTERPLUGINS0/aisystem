package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import javax.annotation.CheckForNull;

@DoNotMock("Use ImmutableRangeMap or TreeRangeMap")
@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public interface RangeMap<K extends Comparable, V> {
   @CheckForNull
   V get(K var1);

   @CheckForNull
   Entry<Range<K>, V> getEntry(K var1);

   Range<K> span();

   void put(Range<K> var1, V var2);

   void putCoalescing(Range<K> var1, V var2);

   void putAll(RangeMap<K, V> var1);

   void clear();

   void remove(Range<K> var1);

   void merge(Range<K> var1, @CheckForNull V var2, BiFunction<? super V, ? super V, ? extends V> var3);

   Map<Range<K>, V> asMapOfRanges();

   Map<Range<K>, V> asDescendingMapOfRanges();

   RangeMap<K, V> subRangeMap(Range<K> var1);

   boolean equals(@CheckForNull Object var1);

   int hashCode();

   String toString();
}
