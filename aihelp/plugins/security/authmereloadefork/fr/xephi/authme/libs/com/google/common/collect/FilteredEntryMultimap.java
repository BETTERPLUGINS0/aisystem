package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.MoreObjects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Predicate;
import fr.xephi.authme.libs.com.google.common.base.Predicates;
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
class FilteredEntryMultimap<K, V> extends AbstractMultimap<K, V> implements FilteredMultimap<K, V> {
   final Multimap<K, V> unfiltered;
   final Predicate<? super Entry<K, V>> predicate;

   FilteredEntryMultimap(Multimap<K, V> unfiltered, Predicate<? super Entry<K, V>> predicate) {
      this.unfiltered = (Multimap)Preconditions.checkNotNull(unfiltered);
      this.predicate = (Predicate)Preconditions.checkNotNull(predicate);
   }

   public Multimap<K, V> unfiltered() {
      return this.unfiltered;
   }

   public Predicate<? super Entry<K, V>> entryPredicate() {
      return this.predicate;
   }

   public int size() {
      return this.entries().size();
   }

   private boolean satisfies(@ParametricNullness K key, @ParametricNullness V value) {
      return this.predicate.apply(Maps.immutableEntry(key, value));
   }

   static <E> Collection<E> filterCollection(Collection<E> collection, Predicate<? super E> predicate) {
      return (Collection)(collection instanceof Set ? Sets.filter((Set)collection, predicate) : Collections2.filter(collection, predicate));
   }

   public boolean containsKey(@CheckForNull Object key) {
      return this.asMap().get(key) != null;
   }

   public Collection<V> removeAll(@CheckForNull Object key) {
      return (Collection)MoreObjects.firstNonNull((Collection)this.asMap().remove(key), this.unmodifiableEmptyCollection());
   }

   Collection<V> unmodifiableEmptyCollection() {
      return (Collection)(this.unfiltered instanceof SetMultimap ? Collections.emptySet() : Collections.emptyList());
   }

   public void clear() {
      this.entries().clear();
   }

   public Collection<V> get(@ParametricNullness K key) {
      return filterCollection(this.unfiltered.get(key), new FilteredEntryMultimap.ValuePredicate(key));
   }

   Collection<Entry<K, V>> createEntries() {
      return filterCollection(this.unfiltered.entries(), this.predicate);
   }

   Collection<V> createValues() {
      return new FilteredMultimapValues(this);
   }

   Iterator<Entry<K, V>> entryIterator() {
      throw new AssertionError("should never be called");
   }

   Map<K, Collection<V>> createAsMap() {
      return new FilteredEntryMultimap.AsMap();
   }

   Set<K> createKeySet() {
      return this.asMap().keySet();
   }

   boolean removeEntriesIf(Predicate<? super Entry<K, Collection<V>>> predicate) {
      Iterator<Entry<K, Collection<V>>> entryIterator = this.unfiltered.asMap().entrySet().iterator();
      boolean changed = false;

      while(entryIterator.hasNext()) {
         Entry<K, Collection<V>> entry = (Entry)entryIterator.next();
         K key = entry.getKey();
         Collection<V> collection = filterCollection((Collection)entry.getValue(), new FilteredEntryMultimap.ValuePredicate(key));
         if (!collection.isEmpty() && predicate.apply(Maps.immutableEntry(key, collection))) {
            if (collection.size() == ((Collection)entry.getValue()).size()) {
               entryIterator.remove();
            } else {
               collection.clear();
            }

            changed = true;
         }
      }

      return changed;
   }

   Multiset<K> createKeys() {
      return new FilteredEntryMultimap.Keys();
   }

   class Keys extends Multimaps.Keys<K, V> {
      Keys() {
         super(FilteredEntryMultimap.this);
      }

      public int remove(@CheckForNull Object key, int occurrences) {
         CollectPreconditions.checkNonnegative(occurrences, "occurrences");
         if (occurrences == 0) {
            return this.count(key);
         } else {
            Collection<V> collection = (Collection)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
            if (collection == null) {
               return 0;
            } else {
               K k = key;
               int oldCount = 0;
               Iterator itr = collection.iterator();

               while(itr.hasNext()) {
                  V v = itr.next();
                  if (FilteredEntryMultimap.this.satisfies(k, v)) {
                     ++oldCount;
                     if (oldCount <= occurrences) {
                        itr.remove();
                     }
                  }
               }

               return oldCount;
            }
         }
      }

      public Set<Multiset.Entry<K>> entrySet() {
         return new Multisets.EntrySet<K>() {
            Multiset<K> multiset() {
               return Keys.this;
            }

            public Iterator<Multiset.Entry<K>> iterator() {
               return Keys.this.entryIterator();
            }

            public int size() {
               return FilteredEntryMultimap.this.keySet().size();
            }

            private boolean removeEntriesIf(Predicate<? super Multiset.Entry<K>> predicate) {
               return FilteredEntryMultimap.this.removeEntriesIf((entry) -> {
                  return predicate.apply(Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size()));
               });
            }

            public boolean removeAll(Collection<?> c) {
               return this.removeEntriesIf(Predicates.in(c));
            }

            public boolean retainAll(Collection<?> c) {
               return this.removeEntriesIf(Predicates.not(Predicates.in(c)));
            }
         };
      }
   }

   class AsMap extends Maps.ViewCachingAbstractMap<K, Collection<V>> {
      public boolean containsKey(@CheckForNull Object key) {
         return this.get(key) != null;
      }

      public void clear() {
         FilteredEntryMultimap.this.clear();
      }

      @CheckForNull
      public Collection<V> get(@CheckForNull Object key) {
         Collection<V> result = (Collection)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
         if (result == null) {
            return null;
         } else {
            result = FilteredEntryMultimap.filterCollection(result, FilteredEntryMultimap.this.new ValuePredicate(key));
            return result.isEmpty() ? null : result;
         }
      }

      @CheckForNull
      public Collection<V> remove(@CheckForNull Object key) {
         Collection<V> collection = (Collection)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
         if (collection == null) {
            return null;
         } else {
            K k = key;
            List<V> result = Lists.newArrayList();
            Iterator itr = collection.iterator();

            while(itr.hasNext()) {
               V v = itr.next();
               if (FilteredEntryMultimap.this.satisfies(k, v)) {
                  itr.remove();
                  result.add(v);
               }
            }

            if (result.isEmpty()) {
               return null;
            } else if (FilteredEntryMultimap.this.unfiltered instanceof SetMultimap) {
               return Collections.unmodifiableSet(Sets.newLinkedHashSet(result));
            } else {
               return Collections.unmodifiableList(result);
            }
         }
      }

      Set<K> createKeySet() {
         class KeySetImpl extends Maps.KeySet<K, Collection<V>> {
            KeySetImpl() {
               super(AsMap.this);
            }

            public boolean removeAll(Collection<?> c) {
               return FilteredEntryMultimap.this.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.in(c)));
            }

            public boolean retainAll(Collection<?> c) {
               return FilteredEntryMultimap.this.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c))));
            }

            public boolean remove(@CheckForNull Object o) {
               return AsMap.this.remove(o) != null;
            }
         }

         return new KeySetImpl();
      }

      Set<Entry<K, Collection<V>>> createEntrySet() {
         class EntrySetImpl extends Maps.EntrySet<K, Collection<V>> {
            Map<K, Collection<V>> map() {
               return AsMap.this;
            }

            public Iterator<Entry<K, Collection<V>>> iterator() {
               return new AbstractIterator<Entry<K, Collection<V>>>() {
                  final Iterator<Entry<K, Collection<V>>> backingIterator;

                  {
                     this.backingIterator = FilteredEntryMultimap.this.unfiltered.asMap().entrySet().iterator();
                  }

                  @CheckForNull
                  protected Entry<K, Collection<V>> computeNext() {
                     while(true) {
                        if (this.backingIterator.hasNext()) {
                           Entry<K, Collection<V>> entry = (Entry)this.backingIterator.next();
                           K key = entry.getKey();
                           Collection<V> collection = FilteredEntryMultimap.filterCollection((Collection)entry.getValue(), FilteredEntryMultimap.this.new ValuePredicate(key));
                           if (collection.isEmpty()) {
                              continue;
                           }

                           return Maps.immutableEntry(key, collection);
                        }

                        return (Entry)this.endOfData();
                     }
                  }
               };
            }

            public boolean removeAll(Collection<?> c) {
               return FilteredEntryMultimap.this.removeEntriesIf(Predicates.in(c));
            }

            public boolean retainAll(Collection<?> c) {
               return FilteredEntryMultimap.this.removeEntriesIf(Predicates.not(Predicates.in(c)));
            }

            public int size() {
               return Iterators.size(this.iterator());
            }
         }

         return new EntrySetImpl();
      }

      Collection<Collection<V>> createValues() {
         class ValuesImpl extends Maps.Values<K, Collection<V>> {
            ValuesImpl() {
               super(AsMap.this);
            }

            public boolean remove(@CheckForNull Object o) {
               if (o instanceof Collection) {
                  Collection<?> c = (Collection)o;
                  Iterator entryIterator = FilteredEntryMultimap.this.unfiltered.asMap().entrySet().iterator();

                  while(entryIterator.hasNext()) {
                     Entry<K, Collection<V>> entry = (Entry)entryIterator.next();
                     K key = entry.getKey();
                     Collection<V> collection = FilteredEntryMultimap.filterCollection((Collection)entry.getValue(), FilteredEntryMultimap.this.new ValuePredicate(key));
                     if (!collection.isEmpty() && c.equals(collection)) {
                        if (collection.size() == ((Collection)entry.getValue()).size()) {
                           entryIterator.remove();
                        } else {
                           collection.clear();
                        }

                        return true;
                     }
                  }
               }

               return false;
            }

            public boolean removeAll(Collection<?> c) {
               return FilteredEntryMultimap.this.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.in(c)));
            }

            public boolean retainAll(Collection<?> c) {
               return FilteredEntryMultimap.this.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c))));
            }
         }

         return new ValuesImpl();
      }
   }

   final class ValuePredicate implements Predicate<V> {
      @ParametricNullness
      private final K key;

      ValuePredicate(@ParametricNullness K key) {
         this.key = key;
      }

      public boolean apply(@ParametricNullness V value) {
         return FilteredEntryMultimap.this.satisfies(this.key, value);
      }
   }
}
