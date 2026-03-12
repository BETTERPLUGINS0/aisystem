package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface SortedSetMultimap<K, V> extends SetMultimap<K, V> {
   SortedSet<V> get(@ParametricNullness K var1);

   @CanIgnoreReturnValue
   SortedSet<V> removeAll(@CheckForNull Object var1);

   @CanIgnoreReturnValue
   SortedSet<V> replaceValues(@ParametricNullness K var1, Iterable<? extends V> var2);

   Map<K, Collection<V>> asMap();

   @CheckForNull
   Comparator<? super V> valueComparator();
}
