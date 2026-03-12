package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AbstractMultimap<K, V> implements Multimap<K, V> {
   @LazyInit
   @CheckForNull
   private transient Collection<Entry<K, V>> entries;
   @LazyInit
   @CheckForNull
   private transient Set<K> keySet;
   @LazyInit
   @CheckForNull
   private transient Multiset<K> keys;
   @LazyInit
   @CheckForNull
   private transient Collection<V> values;
   @LazyInit
   @CheckForNull
   private transient Map<K, Collection<V>> asMap;

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean containsValue(@CheckForNull Object value) {
      Iterator var2 = this.asMap().values().iterator();

      Collection collection;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         collection = (Collection)var2.next();
      } while(!collection.contains(value));

      return true;
   }

   public boolean containsEntry(@CheckForNull Object key, @CheckForNull Object value) {
      Collection<V> collection = (Collection)this.asMap().get(key);
      return collection != null && collection.contains(value);
   }

   @CanIgnoreReturnValue
   public boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
      Collection<V> collection = (Collection)this.asMap().get(key);
      return collection != null && collection.remove(value);
   }

   @CanIgnoreReturnValue
   public boolean put(@ParametricNullness K key, @ParametricNullness V value) {
      return this.get(key).add(value);
   }

   @CanIgnoreReturnValue
   public boolean putAll(@ParametricNullness K key, Iterable<? extends V> values) {
      Preconditions.checkNotNull(values);
      if (values instanceof Collection) {
         Collection<? extends V> valueCollection = (Collection)values;
         return !valueCollection.isEmpty() && this.get(key).addAll(valueCollection);
      } else {
         Iterator<? extends V> valueItr = values.iterator();
         return valueItr.hasNext() && Iterators.addAll(this.get(key), valueItr);
      }
   }

   @CanIgnoreReturnValue
   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
      boolean changed = false;

      Entry entry;
      for(Iterator var3 = multimap.entries().iterator(); var3.hasNext(); changed |= this.put(entry.getKey(), entry.getValue())) {
         entry = (Entry)var3.next();
      }

      return changed;
   }

   @CanIgnoreReturnValue
   public Collection<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
      Preconditions.checkNotNull(values);
      Collection<V> result = this.removeAll(key);
      this.putAll(key, values);
      return result;
   }

   public Collection<Entry<K, V>> entries() {
      Collection<Entry<K, V>> result = this.entries;
      return result == null ? (this.entries = this.createEntries()) : result;
   }

   abstract Collection<Entry<K, V>> createEntries();

   abstract Iterator<Entry<K, V>> entryIterator();

   Spliterator<Entry<K, V>> entrySpliterator() {
      return Spliterators.spliterator(this.entryIterator(), (long)this.size(), this instanceof SetMultimap ? 1 : 0);
   }

   public Set<K> keySet() {
      Set<K> result = this.keySet;
      return result == null ? (this.keySet = this.createKeySet()) : result;
   }

   abstract Set<K> createKeySet();

   public Multiset<K> keys() {
      Multiset<K> result = this.keys;
      return result == null ? (this.keys = this.createKeys()) : result;
   }

   abstract Multiset<K> createKeys();

   public Collection<V> values() {
      Collection<V> result = this.values;
      return result == null ? (this.values = this.createValues()) : result;
   }

   abstract Collection<V> createValues();

   Iterator<V> valueIterator() {
      return Maps.valueIterator(this.entries().iterator());
   }

   Spliterator<V> valueSpliterator() {
      return Spliterators.spliterator(this.valueIterator(), (long)this.size(), 0);
   }

   public Map<K, Collection<V>> asMap() {
      Map<K, Collection<V>> result = this.asMap;
      return result == null ? (this.asMap = this.createAsMap()) : result;
   }

   abstract Map<K, Collection<V>> createAsMap();

   public boolean equals(@CheckForNull Object object) {
      return Multimaps.equalsImpl(this, object);
   }

   public int hashCode() {
      return this.asMap().hashCode();
   }

   public String toString() {
      return this.asMap().toString();
   }

   class Values extends AbstractCollection<V> {
      public Iterator<V> iterator() {
         return AbstractMultimap.this.valueIterator();
      }

      public Spliterator<V> spliterator() {
         return AbstractMultimap.this.valueSpliterator();
      }

      public int size() {
         return AbstractMultimap.this.size();
      }

      public boolean contains(@CheckForNull Object o) {
         return AbstractMultimap.this.containsValue(o);
      }

      public void clear() {
         AbstractMultimap.this.clear();
      }
   }

   class EntrySet extends AbstractMultimap<K, V>.Entries implements Set<Entry<K, V>> {
      EntrySet(AbstractMultimap this$0) {
         super();
      }

      public int hashCode() {
         return Sets.hashCodeImpl(this);
      }

      public boolean equals(@CheckForNull Object obj) {
         return Sets.equalsImpl(this, obj);
      }
   }

   class Entries extends Multimaps.Entries<K, V> {
      Multimap<K, V> multimap() {
         return AbstractMultimap.this;
      }

      public Iterator<Entry<K, V>> iterator() {
         return AbstractMultimap.this.entryIterator();
      }

      public Spliterator<Entry<K, V>> spliterator() {
         return AbstractMultimap.this.entrySpliterator();
      }
   }
}
