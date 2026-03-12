package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface ListMultimap<K, V> extends Multimap<K, V> {
   List<V> get(@ParametricNullness K var1);

   @CanIgnoreReturnValue
   List<V> removeAll(@CheckForNull Object var1);

   @CanIgnoreReturnValue
   List<V> replaceValues(@ParametricNullness K var1, Iterable<? extends V> var2);

   Map<K, Collection<V>> asMap();

   boolean equals(@CheckForNull Object var1);
}
