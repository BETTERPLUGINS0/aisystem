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
public abstract class ForwardingMultimap<K, V> extends ForwardingObject implements Multimap<K, V> {
   protected ForwardingMultimap() {
   }

   protected abstract Multimap<K, V> delegate();

   public Map<K, Collection<V>> asMap() {
      return this.delegate().asMap();
   }

   public void clear() {
      this.delegate().clear();
   }

   public boolean containsEntry(@CheckForNull Object key, @CheckForNull Object value) {
      return this.delegate().containsEntry(key, value);
   }

   public boolean containsKey(@CheckForNull Object key) {
      return this.delegate().containsKey(key);
   }

   public boolean containsValue(@CheckForNull Object value) {
      return this.delegate().containsValue(value);
   }

   public Collection<Entry<K, V>> entries() {
      return this.delegate().entries();
   }

   public Collection<V> get(@ParametricNullness K key) {
      return this.delegate().get(key);
   }

   public boolean isEmpty() {
      return this.delegate().isEmpty();
   }

   public Multiset<K> keys() {
      return this.delegate().keys();
   }

   public Set<K> keySet() {
      return this.delegate().keySet();
   }

   @CanIgnoreReturnValue
   public boolean put(@ParametricNullness K key, @ParametricNullness V value) {
      return this.delegate().put(key, value);
   }

   @CanIgnoreReturnValue
   public boolean putAll(@ParametricNullness K key, Iterable<? extends V> values) {
      return this.delegate().putAll(key, values);
   }

   @CanIgnoreReturnValue
   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
      return this.delegate().putAll(multimap);
   }

   @CanIgnoreReturnValue
   public boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
      return this.delegate().remove(key, value);
   }

   @CanIgnoreReturnValue
   public Collection<V> removeAll(@CheckForNull Object key) {
      return this.delegate().removeAll(key);
   }

   @CanIgnoreReturnValue
   public Collection<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
      return this.delegate().replaceValues(key, values);
   }

   public int size() {
      return this.delegate().size();
   }

   public Collection<V> values() {
      return this.delegate().values();
   }

   public boolean equals(@CheckForNull Object object) {
      return object == this || this.delegate().equals(object);
   }

   public int hashCode() {
      return this.delegate().hashCode();
   }
}
