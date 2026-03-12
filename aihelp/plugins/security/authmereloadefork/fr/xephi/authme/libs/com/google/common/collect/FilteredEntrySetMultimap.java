package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Predicate;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class FilteredEntrySetMultimap<K, V> extends FilteredEntryMultimap<K, V> implements FilteredSetMultimap<K, V> {
   FilteredEntrySetMultimap(SetMultimap<K, V> unfiltered, Predicate<? super Entry<K, V>> predicate) {
      super(unfiltered, predicate);
   }

   public SetMultimap<K, V> unfiltered() {
      return (SetMultimap)this.unfiltered;
   }

   public Set<V> get(@ParametricNullness K key) {
      return (Set)super.get(key);
   }

   public Set<V> removeAll(@CheckForNull Object key) {
      return (Set)super.removeAll(key);
   }

   public Set<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
      return (Set)super.replaceValues(key, values);
   }

   Set<Entry<K, V>> createEntries() {
      return Sets.filter(this.unfiltered().entries(), this.entryPredicate());
   }

   public Set<Entry<K, V>> entries() {
      return (Set)super.entries();
   }
}
