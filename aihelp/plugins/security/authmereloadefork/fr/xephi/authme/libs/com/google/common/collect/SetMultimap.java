package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface SetMultimap<K, V> extends Multimap<K, V> {
   Set<V> get(@ParametricNullness K var1);

   @CanIgnoreReturnValue
   Set<V> removeAll(@CheckForNull Object var1);

   @CanIgnoreReturnValue
   Set<V> replaceValues(@ParametricNullness K var1, Iterable<? extends V> var2);

   Set<Entry<K, V>> entries();

   Map<K, Collection<V>> asMap();

   boolean equals(@CheckForNull Object var1);
}
