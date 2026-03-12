package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Objects;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingMap<K, V> extends ForwardingObject implements Map<K, V> {
   protected ForwardingMap() {
   }

   protected abstract Map<K, V> delegate();

   public int size() {
      return this.delegate().size();
   }

   public boolean isEmpty() {
      return this.delegate().isEmpty();
   }

   @CheckForNull
   @CanIgnoreReturnValue
   public V remove(@CheckForNull Object key) {
      return this.delegate().remove(key);
   }

   public void clear() {
      this.delegate().clear();
   }

   public boolean containsKey(@CheckForNull Object key) {
      return this.delegate().containsKey(key);
   }

   public boolean containsValue(@CheckForNull Object value) {
      return this.delegate().containsValue(value);
   }

   @CheckForNull
   public V get(@CheckForNull Object key) {
      return this.delegate().get(key);
   }

   @CheckForNull
   @CanIgnoreReturnValue
   public V put(@ParametricNullness K key, @ParametricNullness V value) {
      return this.delegate().put(key, value);
   }

   public void putAll(Map<? extends K, ? extends V> map) {
      this.delegate().putAll(map);
   }

   public Set<K> keySet() {
      return this.delegate().keySet();
   }

   public Collection<V> values() {
      return this.delegate().values();
   }

   public Set<Entry<K, V>> entrySet() {
      return this.delegate().entrySet();
   }

   public boolean equals(@CheckForNull Object object) {
      return object == this || this.delegate().equals(object);
   }

   public int hashCode() {
      return this.delegate().hashCode();
   }

   protected void standardPutAll(Map<? extends K, ? extends V> map) {
      Maps.putAllImpl(this, map);
   }

   @CheckForNull
   @Beta
   protected V standardRemove(@CheckForNull Object key) {
      Iterator entryIterator = this.entrySet().iterator();

      Entry entry;
      do {
         if (!entryIterator.hasNext()) {
            return null;
         }

         entry = (Entry)entryIterator.next();
      } while(!Objects.equal(entry.getKey(), key));

      V value = entry.getValue();
      entryIterator.remove();
      return value;
   }

   protected void standardClear() {
      Iterators.clear(this.entrySet().iterator());
   }

   @Beta
   protected boolean standardContainsKey(@CheckForNull Object key) {
      return Maps.containsKeyImpl(this, key);
   }

   protected boolean standardContainsValue(@CheckForNull Object value) {
      return Maps.containsValueImpl(this, value);
   }

   protected boolean standardIsEmpty() {
      return !this.entrySet().iterator().hasNext();
   }

   protected boolean standardEquals(@CheckForNull Object object) {
      return Maps.equalsImpl(this, object);
   }

   protected int standardHashCode() {
      return Sets.hashCodeImpl(this.entrySet());
   }

   protected String standardToString() {
      return Maps.toStringImpl(this);
   }

   @Beta
   protected abstract class StandardEntrySet extends Maps.EntrySet<K, V> {
      public StandardEntrySet() {
      }

      Map<K, V> map() {
         return ForwardingMap.this;
      }
   }

   @Beta
   protected class StandardValues extends Maps.Values<K, V> {
      public StandardValues(ForwardingMap this$0) {
         super(this$0);
      }
   }

   @Beta
   protected class StandardKeySet extends Maps.KeySet<K, V> {
      public StandardKeySet(ForwardingMap this$0) {
         super(this$0);
      }
   }
}
