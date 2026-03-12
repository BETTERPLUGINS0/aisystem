package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.util.Comparator;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingSortedSetMultimap<K, V> extends ForwardingSetMultimap<K, V> implements SortedSetMultimap<K, V> {
   protected ForwardingSortedSetMultimap() {
   }

   protected abstract SortedSetMultimap<K, V> delegate();

   public SortedSet<V> get(@ParametricNullness K key) {
      return this.delegate().get(key);
   }

   public SortedSet<V> removeAll(@CheckForNull Object key) {
      return this.delegate().removeAll(key);
   }

   public SortedSet<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
      return this.delegate().replaceValues(key, values);
   }

   @CheckForNull
   public Comparator<? super V> valueComparator() {
      return this.delegate().valueComparator();
   }
}
