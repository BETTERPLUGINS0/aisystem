package es.outlook.adriansrj.nbt.nbt.tag;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

class NonNullEntrySet<K, V> implements Set<Entry<K, V>> {
   private Set<Entry<K, V>> set;

   NonNullEntrySet(Set<Entry<K, V>> var1) {
      this.set = var1;
   }

   public int size() {
      return this.set.size();
   }

   public boolean isEmpty() {
      return this.set.isEmpty();
   }

   public boolean contains(Object var1) {
      return this.set.contains(var1);
   }

   public Iterator<Entry<K, V>> iterator() {
      return new NonNullEntrySet.NonNullEntrySetIterator(this.set.iterator());
   }

   public Object[] toArray() {
      return this.set.toArray();
   }

   public <T> T[] toArray(T[] var1) {
      return this.set.toArray(var1);
   }

   public boolean add(Entry<K, V> var1) {
      return this.set.add(var1);
   }

   public boolean remove(Object var1) {
      return this.set.remove(var1);
   }

   public boolean containsAll(Collection<?> var1) {
      return this.set.containsAll(var1);
   }

   public boolean addAll(Collection<? extends Entry<K, V>> var1) {
      return this.set.addAll(var1);
   }

   public boolean retainAll(Collection<?> var1) {
      return this.set.retainAll(var1);
   }

   public boolean removeAll(Collection<?> var1) {
      return this.set.removeAll(var1);
   }

   public void clear() {
      this.set.clear();
   }

   class NonNullEntry implements Entry<K, V> {
      private Entry<K, V> entry;

      NonNullEntry(Entry<K, V> var2) {
         this.entry = var2;
      }

      public K getKey() {
         return this.entry.getKey();
      }

      public V getValue() {
         return this.entry.getValue();
      }

      public V setValue(V var1) {
         if (var1 == null) {
            throw new NullPointerException(this.getClass().getSimpleName() + " does not allow setting null");
         } else {
            return this.entry.setValue(var1);
         }
      }

      public boolean equals(Object var1) {
         return this.entry.equals(var1);
      }

      public int hashCode() {
         return this.entry.hashCode();
      }
   }

   class NonNullEntrySetIterator implements Iterator<Entry<K, V>> {
      private Iterator<Entry<K, V>> iterator;

      NonNullEntrySetIterator(Iterator<Entry<K, V>> var2) {
         this.iterator = var2;
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      public Entry<K, V> next() {
         return NonNullEntrySet.this.new NonNullEntry((Entry)this.iterator.next());
      }
   }
}
