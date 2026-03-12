package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.NavigableSet;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AbstractSortedSetMultimap<K, V> extends AbstractSetMultimap<K, V> implements SortedSetMultimap<K, V> {
   private static final long serialVersionUID = 430848587173315748L;

   protected AbstractSortedSetMultimap(Map<K, Collection<V>> map) {
      super(map);
   }

   abstract SortedSet<V> createCollection();

   SortedSet<V> createUnmodifiableEmptyCollection() {
      return this.unmodifiableCollectionSubclass(this.createCollection());
   }

   <E> SortedSet<E> unmodifiableCollectionSubclass(Collection<E> collection) {
      return (SortedSet)(collection instanceof NavigableSet ? Sets.unmodifiableNavigableSet((NavigableSet)collection) : Collections.unmodifiableSortedSet((SortedSet)collection));
   }

   Collection<V> wrapCollection(@ParametricNullness K key, Collection<V> collection) {
      return (Collection)(collection instanceof NavigableSet ? new AbstractMapBasedMultimap.WrappedNavigableSet(key, (NavigableSet)collection, (AbstractMapBasedMultimap.WrappedCollection)null) : new AbstractMapBasedMultimap.WrappedSortedSet(key, (SortedSet)collection, (AbstractMapBasedMultimap.WrappedCollection)null));
   }

   public SortedSet<V> get(@ParametricNullness K key) {
      return (SortedSet)super.get(key);
   }

   @CanIgnoreReturnValue
   public SortedSet<V> removeAll(@CheckForNull Object key) {
      return (SortedSet)super.removeAll(key);
   }

   @CanIgnoreReturnValue
   public SortedSet<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
      return (SortedSet)super.replaceValues(key, values);
   }

   public Map<K, Collection<V>> asMap() {
      return super.asMap();
   }

   public Collection<V> values() {
      return super.values();
   }
}
