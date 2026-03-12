package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Objects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.j2objc.annotations.RetainedWith;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
abstract class AbstractBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable {
   private transient Map<K, V> delegate;
   @RetainedWith
   transient AbstractBiMap<V, K> inverse;
   @CheckForNull
   private transient Set<K> keySet;
   @CheckForNull
   private transient Set<V> valueSet;
   @CheckForNull
   private transient Set<Entry<K, V>> entrySet;
   @GwtIncompatible
   private static final long serialVersionUID = 0L;

   AbstractBiMap(Map<K, V> forward, Map<V, K> backward) {
      this.setDelegates(forward, backward);
   }

   private AbstractBiMap(Map<K, V> backward, AbstractBiMap<V, K> forward) {
      this.delegate = backward;
      this.inverse = forward;
   }

   protected Map<K, V> delegate() {
      return this.delegate;
   }

   @ParametricNullness
   @CanIgnoreReturnValue
   K checkKey(@ParametricNullness K key) {
      return key;
   }

   @ParametricNullness
   @CanIgnoreReturnValue
   V checkValue(@ParametricNullness V value) {
      return value;
   }

   void setDelegates(Map<K, V> forward, Map<V, K> backward) {
      Preconditions.checkState(this.delegate == null);
      Preconditions.checkState(this.inverse == null);
      Preconditions.checkArgument(forward.isEmpty());
      Preconditions.checkArgument(backward.isEmpty());
      Preconditions.checkArgument(forward != backward);
      this.delegate = forward;
      this.inverse = this.makeInverse(backward);
   }

   AbstractBiMap<V, K> makeInverse(Map<V, K> backward) {
      return new AbstractBiMap.Inverse(backward, this);
   }

   void setInverse(AbstractBiMap<V, K> inverse) {
      this.inverse = inverse;
   }

   public boolean containsValue(@CheckForNull Object value) {
      return this.inverse.containsKey(value);
   }

   @CheckForNull
   @CanIgnoreReturnValue
   public V put(@ParametricNullness K key, @ParametricNullness V value) {
      return this.putInBothMaps(key, value, false);
   }

   @CheckForNull
   @CanIgnoreReturnValue
   public V forcePut(@ParametricNullness K key, @ParametricNullness V value) {
      return this.putInBothMaps(key, value, true);
   }

   @CheckForNull
   private V putInBothMaps(@ParametricNullness K key, @ParametricNullness V value, boolean force) {
      this.checkKey(key);
      this.checkValue(value);
      boolean containedKey = this.containsKey(key);
      if (containedKey && Objects.equal(value, this.get(key))) {
         return value;
      } else {
         if (force) {
            this.inverse().remove(value);
         } else {
            Preconditions.checkArgument(!this.containsValue(value), "value already present: %s", value);
         }

         V oldValue = this.delegate.put(key, value);
         this.updateInverseMap(key, containedKey, oldValue, value);
         return oldValue;
      }
   }

   private void updateInverseMap(@ParametricNullness K key, boolean containedKey, @CheckForNull V oldValue, @ParametricNullness V newValue) {
      if (containedKey) {
         this.removeFromInverseMap(NullnessCasts.uncheckedCastNullableTToT(oldValue));
      }

      this.inverse.delegate.put(newValue, key);
   }

   @CheckForNull
   @CanIgnoreReturnValue
   public V remove(@CheckForNull Object key) {
      return this.containsKey(key) ? this.removeFromBothMaps(key) : null;
   }

   @ParametricNullness
   @CanIgnoreReturnValue
   private V removeFromBothMaps(@CheckForNull Object key) {
      V oldValue = NullnessCasts.uncheckedCastNullableTToT(this.delegate.remove(key));
      this.removeFromInverseMap(oldValue);
      return oldValue;
   }

   private void removeFromInverseMap(@ParametricNullness V oldValue) {
      this.inverse.delegate.remove(oldValue);
   }

   public void putAll(Map<? extends K, ? extends V> map) {
      Iterator var2 = map.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<? extends K, ? extends V> entry = (Entry)var2.next();
         this.put(entry.getKey(), entry.getValue());
      }

   }

   public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
      this.delegate.replaceAll(function);
      this.inverse.delegate.clear();
      Entry<K, V> broken = null;
      Iterator itr = this.delegate.entrySet().iterator();

      while(itr.hasNext()) {
         Entry<K, V> entry = (Entry)itr.next();
         K k = entry.getKey();
         V v = entry.getValue();
         K conflict = this.inverse.delegate.putIfAbsent(v, k);
         if (conflict != null) {
            broken = entry;
            itr.remove();
         }
      }

      if (broken != null) {
         String var8 = String.valueOf(broken.getValue());
         throw new IllegalArgumentException((new StringBuilder(23 + String.valueOf(var8).length())).append("value already present: ").append(var8).toString());
      }
   }

   public void clear() {
      this.delegate.clear();
      this.inverse.delegate.clear();
   }

   public BiMap<V, K> inverse() {
      return this.inverse;
   }

   public Set<K> keySet() {
      Set<K> result = this.keySet;
      return result == null ? (this.keySet = new AbstractBiMap.KeySet()) : result;
   }

   public Set<V> values() {
      Set<V> result = this.valueSet;
      return result == null ? (this.valueSet = new AbstractBiMap.ValueSet()) : result;
   }

   public Set<Entry<K, V>> entrySet() {
      Set<Entry<K, V>> result = this.entrySet;
      return result == null ? (this.entrySet = new AbstractBiMap.EntrySet()) : result;
   }

   Iterator<Entry<K, V>> entrySetIterator() {
      final Iterator<Entry<K, V>> iterator = this.delegate.entrySet().iterator();
      return new Iterator<Entry<K, V>>() {
         @CheckForNull
         Entry<K, V> entry;

         public boolean hasNext() {
            return iterator.hasNext();
         }

         public Entry<K, V> next() {
            this.entry = (Entry)iterator.next();
            return AbstractBiMap.this.new BiMapEntry(this.entry);
         }

         public void remove() {
            if (this.entry == null) {
               throw new IllegalStateException("no calls to next() since the last call to remove()");
            } else {
               V value = this.entry.getValue();
               iterator.remove();
               AbstractBiMap.this.removeFromInverseMap(value);
               this.entry = null;
            }
         }
      };
   }

   // $FF: synthetic method
   AbstractBiMap(Map x0, AbstractBiMap x1, Object x2) {
      this(x0, x1);
   }

   static class Inverse<K, V> extends AbstractBiMap<K, V> {
      @GwtIncompatible
      private static final long serialVersionUID = 0L;

      Inverse(Map<K, V> backward, AbstractBiMap<V, K> forward) {
         super(backward, forward, null);
      }

      @ParametricNullness
      K checkKey(@ParametricNullness K key) {
         return this.inverse.checkValue(key);
      }

      @ParametricNullness
      V checkValue(@ParametricNullness V value) {
         return this.inverse.checkKey(value);
      }

      @GwtIncompatible
      private void writeObject(ObjectOutputStream stream) throws IOException {
         stream.defaultWriteObject();
         stream.writeObject(this.inverse());
      }

      @GwtIncompatible
      private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
         stream.defaultReadObject();
         this.setInverse((AbstractBiMap)stream.readObject());
      }

      @GwtIncompatible
      Object readResolve() {
         return this.inverse().inverse();
      }
   }

   private class EntrySet extends ForwardingSet<Entry<K, V>> {
      final Set<Entry<K, V>> esDelegate;

      private EntrySet() {
         this.esDelegate = AbstractBiMap.this.delegate.entrySet();
      }

      protected Set<Entry<K, V>> delegate() {
         return this.esDelegate;
      }

      public void clear() {
         AbstractBiMap.this.clear();
      }

      public boolean remove(@CheckForNull Object object) {
         if (this.esDelegate.contains(object) && object instanceof Entry) {
            Entry<?, ?> entry = (Entry)object;
            AbstractBiMap.this.inverse.delegate.remove(entry.getValue());
            this.esDelegate.remove(entry);
            return true;
         } else {
            return false;
         }
      }

      public Iterator<Entry<K, V>> iterator() {
         return AbstractBiMap.this.entrySetIterator();
      }

      public Object[] toArray() {
         Object[] result = this.standardToArray();
         return result;
      }

      public <T> T[] toArray(T[] array) {
         return this.standardToArray(array);
      }

      public boolean contains(@CheckForNull Object o) {
         return Maps.containsEntryImpl(this.delegate(), o);
      }

      public boolean containsAll(Collection<?> c) {
         return this.standardContainsAll(c);
      }

      public boolean removeAll(Collection<?> c) {
         return this.standardRemoveAll(c);
      }

      public boolean retainAll(Collection<?> c) {
         return this.standardRetainAll(c);
      }

      // $FF: synthetic method
      EntrySet(Object x1) {
         this();
      }
   }

   class BiMapEntry extends ForwardingMapEntry<K, V> {
      private final Entry<K, V> delegate;

      BiMapEntry(Entry<K, V> delegate) {
         this.delegate = delegate;
      }

      protected Entry<K, V> delegate() {
         return this.delegate;
      }

      public V setValue(V value) {
         AbstractBiMap.this.checkValue(value);
         Preconditions.checkState(AbstractBiMap.this.entrySet().contains(this), "entry no longer in map");
         if (Objects.equal(value, this.getValue())) {
            return value;
         } else {
            Preconditions.checkArgument(!AbstractBiMap.this.containsValue(value), "value already present: %s", value);
            V oldValue = this.delegate.setValue(value);
            Preconditions.checkState(Objects.equal(value, AbstractBiMap.this.get(this.getKey())), "entry no longer in map");
            AbstractBiMap.this.updateInverseMap(this.getKey(), true, oldValue, value);
            return oldValue;
         }
      }
   }

   private class ValueSet extends ForwardingSet<V> {
      final Set<V> valuesDelegate;

      private ValueSet() {
         this.valuesDelegate = AbstractBiMap.this.inverse.keySet();
      }

      protected Set<V> delegate() {
         return this.valuesDelegate;
      }

      public Iterator<V> iterator() {
         return Maps.valueIterator(AbstractBiMap.this.entrySet().iterator());
      }

      public Object[] toArray() {
         return this.standardToArray();
      }

      public <T> T[] toArray(T[] array) {
         return this.standardToArray(array);
      }

      public String toString() {
         return this.standardToString();
      }

      // $FF: synthetic method
      ValueSet(Object x1) {
         this();
      }
   }

   private class KeySet extends ForwardingSet<K> {
      private KeySet() {
      }

      protected Set<K> delegate() {
         return AbstractBiMap.this.delegate.keySet();
      }

      public void clear() {
         AbstractBiMap.this.clear();
      }

      public boolean remove(@CheckForNull Object key) {
         if (!this.contains(key)) {
            return false;
         } else {
            AbstractBiMap.this.removeFromBothMaps(key);
            return true;
         }
      }

      public boolean removeAll(Collection<?> keysToRemove) {
         return this.standardRemoveAll(keysToRemove);
      }

      public boolean retainAll(Collection<?> keysToRetain) {
         return this.standardRetainAll(keysToRetain);
      }

      public Iterator<K> iterator() {
         return Maps.keyIterator(AbstractBiMap.this.entrySet().iterator());
      }

      // $FF: synthetic method
      KeySet(Object x1) {
         this();
      }
   }
}
