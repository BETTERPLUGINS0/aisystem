package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Predicate;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class FilteredKeySetMultimap<K, V> extends FilteredKeyMultimap<K, V> implements FilteredSetMultimap<K, V> {
   FilteredKeySetMultimap(SetMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
      super(unfiltered, keyPredicate);
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

   public Set<Entry<K, V>> entries() {
      return (Set)super.entries();
   }

   Set<Entry<K, V>> createEntries() {
      return new FilteredKeySetMultimap.EntrySet(this);
   }

   class EntrySet extends FilteredKeyMultimap<K, V>.Entries implements Set<Entry<K, V>> {
      EntrySet(FilteredKeySetMultimap this$0) {
         super();
      }

      public int hashCode() {
         return Sets.hashCodeImpl(this);
      }

      public boolean equals(@CheckForNull Object o) {
         return Sets.equalsImpl(this, o);
      }
   }
}
