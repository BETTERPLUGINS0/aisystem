package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Predicate;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
class FilteredKeyMultimap<K, V> extends AbstractMultimap<K, V> implements FilteredMultimap<K, V> {
   final Multimap<K, V> unfiltered;
   final Predicate<? super K> keyPredicate;

   FilteredKeyMultimap(Multimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
      this.unfiltered = (Multimap)Preconditions.checkNotNull(unfiltered);
      this.keyPredicate = (Predicate)Preconditions.checkNotNull(keyPredicate);
   }

   public Multimap<K, V> unfiltered() {
      return this.unfiltered;
   }

   public Predicate<? super Entry<K, V>> entryPredicate() {
      return Maps.keyPredicateOnEntries(this.keyPredicate);
   }

   public int size() {
      int size = 0;

      Collection collection;
      for(Iterator var2 = this.asMap().values().iterator(); var2.hasNext(); size += collection.size()) {
         collection = (Collection)var2.next();
      }

      return size;
   }

   public boolean containsKey(@CheckForNull Object key) {
      return this.unfiltered.containsKey(key) ? this.keyPredicate.apply(key) : false;
   }

   public Collection<V> removeAll(@CheckForNull Object key) {
      return this.containsKey(key) ? this.unfiltered.removeAll(key) : this.unmodifiableEmptyCollection();
   }

   Collection<V> unmodifiableEmptyCollection() {
      return (Collection)(this.unfiltered instanceof SetMultimap ? Collections.emptySet() : Collections.emptyList());
   }

   public void clear() {
      this.keySet().clear();
   }

   Set<K> createKeySet() {
      return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
   }

   public Collection<V> get(@ParametricNullness K key) {
      if (this.keyPredicate.apply(key)) {
         return this.unfiltered.get(key);
      } else {
         return (Collection)(this.unfiltered instanceof SetMultimap ? new FilteredKeyMultimap.AddRejectingSet(key) : new FilteredKeyMultimap.AddRejectingList(key));
      }
   }

   Iterator<Entry<K, V>> entryIterator() {
      throw new AssertionError("should never be called");
   }

   Collection<Entry<K, V>> createEntries() {
      return new FilteredKeyMultimap.Entries();
   }

   Collection<V> createValues() {
      return new FilteredMultimapValues(this);
   }

   Map<K, Collection<V>> createAsMap() {
      return Maps.filterKeys(this.unfiltered.asMap(), this.keyPredicate);
   }

   Multiset<K> createKeys() {
      return Multisets.filter(this.unfiltered.keys(), this.keyPredicate);
   }

   class Entries extends ForwardingCollection<Entry<K, V>> {
      protected Collection<Entry<K, V>> delegate() {
         return Collections2.filter(FilteredKeyMultimap.this.unfiltered.entries(), FilteredKeyMultimap.this.entryPredicate());
      }

      public boolean remove(@CheckForNull Object o) {
         if (o instanceof Entry) {
            Entry<?, ?> entry = (Entry)o;
            if (FilteredKeyMultimap.this.unfiltered.containsKey(entry.getKey()) && FilteredKeyMultimap.this.keyPredicate.apply(entry.getKey())) {
               return FilteredKeyMultimap.this.unfiltered.remove(entry.getKey(), entry.getValue());
            }
         }

         return false;
      }
   }

   static class AddRejectingList<K, V> extends ForwardingList<V> {
      @ParametricNullness
      final K key;

      AddRejectingList(@ParametricNullness K key) {
         this.key = key;
      }

      public boolean add(@ParametricNullness V v) {
         this.add(0, v);
         return true;
      }

      public void add(int index, @ParametricNullness V element) {
         Preconditions.checkPositionIndex(index, 0);
         String var3 = String.valueOf(this.key);
         throw new IllegalArgumentException((new StringBuilder(32 + String.valueOf(var3).length())).append("Key does not satisfy predicate: ").append(var3).toString());
      }

      public boolean addAll(Collection<? extends V> collection) {
         this.addAll(0, collection);
         return true;
      }

      @CanIgnoreReturnValue
      public boolean addAll(int index, Collection<? extends V> elements) {
         Preconditions.checkNotNull(elements);
         Preconditions.checkPositionIndex(index, 0);
         String var3 = String.valueOf(this.key);
         throw new IllegalArgumentException((new StringBuilder(32 + String.valueOf(var3).length())).append("Key does not satisfy predicate: ").append(var3).toString());
      }

      protected List<V> delegate() {
         return Collections.emptyList();
      }
   }

   static class AddRejectingSet<K, V> extends ForwardingSet<V> {
      @ParametricNullness
      final K key;

      AddRejectingSet(@ParametricNullness K key) {
         this.key = key;
      }

      public boolean add(@ParametricNullness V element) {
         String var2 = String.valueOf(this.key);
         throw new IllegalArgumentException((new StringBuilder(32 + String.valueOf(var2).length())).append("Key does not satisfy predicate: ").append(var2).toString());
      }

      public boolean addAll(Collection<? extends V> collection) {
         Preconditions.checkNotNull(collection);
         String var2 = String.valueOf(this.key);
         throw new IllegalArgumentException((new StringBuilder(32 + String.valueOf(var2).length())).append("Key does not satisfy predicate: ").append(var2).toString());
      }

      protected Set<V> delegate() {
         return Collections.emptySet();
      }
   }
}
