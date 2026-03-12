package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingSortedMap<K, V> extends ForwardingMap<K, V> implements SortedMap<K, V> {
   protected ForwardingSortedMap() {
   }

   protected abstract SortedMap<K, V> delegate();

   @CheckForNull
   public Comparator<? super K> comparator() {
      return this.delegate().comparator();
   }

   @ParametricNullness
   public K firstKey() {
      return this.delegate().firstKey();
   }

   public SortedMap<K, V> headMap(@ParametricNullness K toKey) {
      return this.delegate().headMap(toKey);
   }

   @ParametricNullness
   public K lastKey() {
      return this.delegate().lastKey();
   }

   public SortedMap<K, V> subMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
      return this.delegate().subMap(fromKey, toKey);
   }

   public SortedMap<K, V> tailMap(@ParametricNullness K fromKey) {
      return this.delegate().tailMap(fromKey);
   }

   static int unsafeCompare(@CheckForNull Comparator<?> comparator, @CheckForNull Object o1, @CheckForNull Object o2) {
      return comparator == null ? ((Comparable)o1).compareTo(o2) : comparator.compare(o1, o2);
   }

   @Beta
   protected boolean standardContainsKey(@CheckForNull Object key) {
      try {
         Object ceilingKey = this.tailMap(key).firstKey();
         return unsafeCompare(this.comparator(), ceilingKey, key) == 0;
      } catch (NoSuchElementException | NullPointerException | ClassCastException var4) {
         return false;
      }
   }

   @Beta
   protected SortedMap<K, V> standardSubMap(K fromKey, K toKey) {
      Preconditions.checkArgument(unsafeCompare(this.comparator(), fromKey, toKey) <= 0, "fromKey must be <= toKey");
      return this.tailMap(fromKey).headMap(toKey);
   }

   @Beta
   protected class StandardKeySet extends Maps.SortedKeySet<K, V> {
      public StandardKeySet(ForwardingSortedMap this$0) {
         super(this$0);
      }
   }
}
