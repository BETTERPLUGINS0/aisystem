package ac.grim.grimac.shaded.fastutil.objects;

import ac.grim.grimac.shaded.fastutil.HashCommon;
import ac.grim.grimac.shaded.fastutil.doubles.AbstractDoubleCollection;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleArrays;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleCollection;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleIterator;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleSpliterator;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleSpliterators;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public class Object2DoubleArrayMap<K> extends AbstractObject2DoubleMap<K> implements Serializable, Cloneable {
   private static final long serialVersionUID = 1L;
   protected transient Object[] key;
   protected transient double[] value;
   protected int size;
   protected transient Object2DoubleMap.FastEntrySet<K> entries;
   protected transient ObjectSet<K> keys;
   protected transient DoubleCollection values;

   public Object2DoubleArrayMap(Object[] key, double[] value) {
      this.key = key;
      this.value = value;
      this.size = key.length;
      if (key.length != value.length) {
         throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
      }
   }

   public Object2DoubleArrayMap() {
      this.key = ObjectArrays.EMPTY_ARRAY;
      this.value = DoubleArrays.EMPTY_ARRAY;
   }

   public Object2DoubleArrayMap(int capacity) {
      this.key = new Object[capacity];
      this.value = new double[capacity];
   }

   public Object2DoubleArrayMap(Object2DoubleMap<K> m) {
      this(m.size());
      int i = 0;

      for(ObjectIterator var3 = m.object2DoubleEntrySet().iterator(); var3.hasNext(); ++i) {
         Object2DoubleMap.Entry<K> e = (Object2DoubleMap.Entry)var3.next();
         this.key[i] = e.getKey();
         this.value[i] = e.getDoubleValue();
      }

      this.size = i;
   }

   public Object2DoubleArrayMap(Map<? extends K, ? extends Double> m) {
      this(m.size());
      int i = 0;

      for(Iterator var3 = m.entrySet().iterator(); var3.hasNext(); ++i) {
         java.util.Map.Entry<? extends K, ? extends Double> e = (java.util.Map.Entry)var3.next();
         this.key[i] = e.getKey();
         this.value[i] = (Double)e.getValue();
      }

      this.size = i;
   }

   public Object2DoubleArrayMap(Object[] key, double[] value, int size) {
      this.key = key;
      this.value = value;
      this.size = size;
      if (key.length != value.length) {
         throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
      } else if (size > key.length) {
         throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the backing-arrays size (" + key.length + ")");
      }
   }

   public Object2DoubleMap.FastEntrySet<K> object2DoubleEntrySet() {
      if (this.entries == null) {
         this.entries = new Object2DoubleArrayMap.EntrySet();
      }

      return this.entries;
   }

   private int findKey(Object k) {
      Object[] key = this.key;
      int i = this.size;

      do {
         if (i-- == 0) {
            return -1;
         }
      } while(!Objects.equals(key[i], k));

      return i;
   }

   public double getDouble(Object k) {
      Object[] key = this.key;
      int i = this.size;

      do {
         if (i-- == 0) {
            return this.defRetValue;
         }
      } while(!Objects.equals(key[i], k));

      return this.value[i];
   }

   public int size() {
      return this.size;
   }

   public void clear() {
      Object[] key = this.key;

      for(int i = this.size; i-- != 0; key[i] = null) {
      }

      this.size = 0;
   }

   public boolean containsKey(Object k) {
      return this.findKey(k) != -1;
   }

   public boolean containsValue(double v) {
      double[] value = this.value;
      int i = this.size;

      do {
         if (i-- == 0) {
            return false;
         }
      } while(Double.doubleToLongBits(value[i]) != Double.doubleToLongBits(v));

      return true;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public double put(K k, double v) {
      int oldKey = this.findKey(k);
      if (oldKey != -1) {
         double oldValue = this.value[oldKey];
         this.value[oldKey] = v;
         return oldValue;
      } else {
         if (this.size == this.key.length) {
            Object[] newKey = new Object[this.size == 0 ? 2 : this.size * 2];
            double[] newValue = new double[this.size == 0 ? 2 : this.size * 2];

            for(int i = this.size; i-- != 0; newValue[i] = this.value[i]) {
               newKey[i] = this.key[i];
            }

            this.key = newKey;
            this.value = newValue;
         }

         this.key[this.size] = k;
         this.value[this.size] = v;
         ++this.size;
         return this.defRetValue;
      }
   }

   public double removeDouble(Object k) {
      int oldPos = this.findKey(k);
      if (oldPos == -1) {
         return this.defRetValue;
      } else {
         double oldValue = this.value[oldPos];
         int tail = this.size - oldPos - 1;
         System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
         System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
         --this.size;
         this.key[this.size] = null;
         return oldValue;
      }
   }

   public ObjectSet<K> keySet() {
      if (this.keys == null) {
         this.keys = new Object2DoubleArrayMap.KeySet();
      }

      return this.keys;
   }

   public DoubleCollection values() {
      if (this.values == null) {
         this.values = new Object2DoubleArrayMap.ValuesCollection();
      }

      return this.values;
   }

   public Object2DoubleArrayMap<K> clone() {
      Object2DoubleArrayMap c;
      try {
         c = (Object2DoubleArrayMap)super.clone();
      } catch (CloneNotSupportedException var3) {
         throw new InternalError();
      }

      c.key = (Object[])this.key.clone();
      c.value = (double[])this.value.clone();
      c.entries = null;
      c.keys = null;
      c.values = null;
      return c;
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();
      Object[] key = this.key;
      double[] value = this.value;
      int i = 0;

      for(int max = this.size; i < max; ++i) {
         s.writeObject(key[i]);
         s.writeDouble(value[i]);
      }

   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      Object[] key = this.key = new Object[this.size];
      double[] value = this.value = new double[this.size];

      for(int i = 0; i < this.size; ++i) {
         key[i] = s.readObject();
         value[i] = s.readDouble();
      }

   }

   private final class EntrySet extends AbstractObjectSet<Object2DoubleMap.Entry<K>> implements Object2DoubleMap.FastEntrySet<K> {
      private EntrySet() {
      }

      public ObjectIterator<Object2DoubleMap.Entry<K>> iterator() {
         return new ObjectIterator<Object2DoubleMap.Entry<K>>() {
            private Object2DoubleArrayMap<K>.MapEntry entry;
            int curr = -1;
            int next = 0;

            public boolean hasNext() {
               return this.next < Object2DoubleArrayMap.this.size;
            }

            public Object2DoubleMap.Entry<K> next() {
               if (!this.hasNext()) {
                  throw new NoSuchElementException();
               } else {
                  return this.entry = Object2DoubleArrayMap.this.new MapEntry(this.curr = this.next++);
               }
            }

            public void remove() {
               if (this.curr == -1) {
                  throw new IllegalStateException();
               } else {
                  this.curr = -1;
                  int tail = Object2DoubleArrayMap.this.size-- - this.next--;
                  System.arraycopy(Object2DoubleArrayMap.this.key, this.next + 1, Object2DoubleArrayMap.this.key, this.next, tail);
                  System.arraycopy(Object2DoubleArrayMap.this.value, this.next + 1, Object2DoubleArrayMap.this.value, this.next, tail);
                  this.entry.index = -1;
                  Object2DoubleArrayMap.this.key[Object2DoubleArrayMap.this.size] = null;
               }
            }

            public int skip(int n) {
               if (n < 0) {
                  throw new IllegalArgumentException("Argument must be nonnegative: " + n);
               } else {
                  n = Math.min(n, Object2DoubleArrayMap.this.size - this.next);
                  this.next += n;
                  if (n != 0) {
                     this.curr = this.next - 1;
                  }

                  return n;
               }
            }

            public void forEachRemaining(Consumer<? super Object2DoubleMap.Entry<K>> action) {
               int max = Object2DoubleArrayMap.this.size;

               while(this.next < max) {
                  this.entry = Object2DoubleArrayMap.this.new MapEntry(this.curr = this.next++);
                  action.accept(this.entry);
               }

            }
         };
      }

      public ObjectIterator<Object2DoubleMap.Entry<K>> fastIterator() {
         return new ObjectIterator<Object2DoubleMap.Entry<K>>() {
            private Object2DoubleArrayMap<K>.MapEntry entry = Object2DoubleArrayMap.this.new MapEntry();
            int next = 0;
            int curr = -1;

            public boolean hasNext() {
               return this.next < Object2DoubleArrayMap.this.size;
            }

            public Object2DoubleMap.Entry<K> next() {
               if (!this.hasNext()) {
                  throw new NoSuchElementException();
               } else {
                  this.entry.index = this.curr = this.next++;
                  return this.entry;
               }
            }

            public void remove() {
               if (this.curr == -1) {
                  throw new IllegalStateException();
               } else {
                  this.curr = -1;
                  int tail = Object2DoubleArrayMap.this.size-- - this.next--;
                  System.arraycopy(Object2DoubleArrayMap.this.key, this.next + 1, Object2DoubleArrayMap.this.key, this.next, tail);
                  System.arraycopy(Object2DoubleArrayMap.this.value, this.next + 1, Object2DoubleArrayMap.this.value, this.next, tail);
                  this.entry.index = -1;
                  Object2DoubleArrayMap.this.key[Object2DoubleArrayMap.this.size] = null;
               }
            }

            public int skip(int n) {
               if (n < 0) {
                  throw new IllegalArgumentException("Argument must be nonnegative: " + n);
               } else {
                  n = Math.min(n, Object2DoubleArrayMap.this.size - this.next);
                  this.next += n;
                  if (n != 0) {
                     this.curr = this.next - 1;
                  }

                  return n;
               }
            }

            public void forEachRemaining(Consumer<? super Object2DoubleMap.Entry<K>> action) {
               int max = Object2DoubleArrayMap.this.size;

               while(this.next < max) {
                  this.entry.index = this.curr = this.next++;
                  action.accept(this.entry);
               }

            }
         };
      }

      public ObjectSpliterator<Object2DoubleMap.Entry<K>> spliterator() {
         return new Object2DoubleArrayMap.EntrySet.EntrySetSpliterator(0, Object2DoubleArrayMap.this.size);
      }

      public void forEach(Consumer<? super Object2DoubleMap.Entry<K>> action) {
         int i = 0;

         for(int max = Object2DoubleArrayMap.this.size; i < max; ++i) {
            action.accept(Object2DoubleArrayMap.this.new MapEntry(i));
         }

      }

      public void fastForEach(Consumer<? super Object2DoubleMap.Entry<K>> action) {
         Object2DoubleArrayMap<K>.MapEntry entry = Object2DoubleArrayMap.this.new MapEntry();
         int i = 0;

         for(int max = Object2DoubleArrayMap.this.size; i < max; ++i) {
            entry.index = i;
            action.accept(entry);
         }

      }

      public int size() {
         return Object2DoubleArrayMap.this.size;
      }

      public boolean contains(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else {
            java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
            if (e.getValue() != null && e.getValue() instanceof Double) {
               K k = e.getKey();
               return Object2DoubleArrayMap.this.containsKey(k) && Double.doubleToLongBits(Object2DoubleArrayMap.this.getDouble(k)) == Double.doubleToLongBits((Double)e.getValue());
            } else {
               return false;
            }
         }
      }

      public boolean remove(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else {
            java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
            if (e.getValue() != null && e.getValue() instanceof Double) {
               K k = e.getKey();
               double v = (Double)e.getValue();
               int oldPos = Object2DoubleArrayMap.this.findKey(k);
               if (oldPos != -1 && Double.doubleToLongBits(v) == Double.doubleToLongBits(Object2DoubleArrayMap.this.value[oldPos])) {
                  int tail = Object2DoubleArrayMap.this.size - oldPos - 1;
                  System.arraycopy(Object2DoubleArrayMap.this.key, oldPos + 1, Object2DoubleArrayMap.this.key, oldPos, tail);
                  System.arraycopy(Object2DoubleArrayMap.this.value, oldPos + 1, Object2DoubleArrayMap.this.value, oldPos, tail);
                  --Object2DoubleArrayMap.this.size;
                  Object2DoubleArrayMap.this.key[Object2DoubleArrayMap.this.size] = null;
                  return true;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         }
      }

      // $FF: synthetic method
      EntrySet(Object x1) {
         this();
      }

      final class EntrySetSpliterator extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<Object2DoubleMap.Entry<K>> implements ObjectSpliterator<Object2DoubleMap.Entry<K>> {
         EntrySetSpliterator(int param2, int param3) {
            super(pos, maxPos);
         }

         public int characteristics() {
            return 16465;
         }

         protected final Object2DoubleMap.Entry<K> get(int location) {
            return Object2DoubleArrayMap.this.new MapEntry(location);
         }

         protected final Object2DoubleArrayMap<K>.EntrySet.EntrySetSpliterator makeForSplit(int pos, int maxPos) {
            return EntrySet.this.new EntrySetSpliterator(pos, maxPos);
         }
      }
   }

   private final class KeySet extends AbstractObjectSet<K> {
      private KeySet() {
      }

      public boolean contains(Object k) {
         return Object2DoubleArrayMap.this.findKey(k) != -1;
      }

      public boolean remove(Object k) {
         int oldPos = Object2DoubleArrayMap.this.findKey(k);
         if (oldPos == -1) {
            return false;
         } else {
            int tail = Object2DoubleArrayMap.this.size - oldPos - 1;
            System.arraycopy(Object2DoubleArrayMap.this.key, oldPos + 1, Object2DoubleArrayMap.this.key, oldPos, tail);
            System.arraycopy(Object2DoubleArrayMap.this.value, oldPos + 1, Object2DoubleArrayMap.this.value, oldPos, tail);
            --Object2DoubleArrayMap.this.size;
            Object2DoubleArrayMap.this.key[Object2DoubleArrayMap.this.size] = null;
            return true;
         }
      }

      public ObjectIterator<K> iterator() {
         return new ObjectIterator<K>() {
            int pos = 0;

            public boolean hasNext() {
               return this.pos < Object2DoubleArrayMap.this.size;
            }

            public K next() {
               if (!this.hasNext()) {
                  throw new NoSuchElementException();
               } else {
                  return Object2DoubleArrayMap.this.key[this.pos++];
               }
            }

            public void remove() {
               if (this.pos == 0) {
                  throw new IllegalStateException();
               } else {
                  int tail = Object2DoubleArrayMap.this.size - this.pos;
                  System.arraycopy(Object2DoubleArrayMap.this.key, this.pos, Object2DoubleArrayMap.this.key, this.pos - 1, tail);
                  System.arraycopy(Object2DoubleArrayMap.this.value, this.pos, Object2DoubleArrayMap.this.value, this.pos - 1, tail);
                  --Object2DoubleArrayMap.this.size;
                  --this.pos;
                  Object2DoubleArrayMap.this.key[Object2DoubleArrayMap.this.size] = null;
               }
            }

            public void forEachRemaining(Consumer<? super K> action) {
               Object[] key = Object2DoubleArrayMap.this.key;
               int max = Object2DoubleArrayMap.this.size;

               while(this.pos < max) {
                  action.accept(key[this.pos++]);
               }

            }
         };
      }

      public ObjectSpliterator<K> spliterator() {
         return new Object2DoubleArrayMap.KeySet.KeySetSpliterator(0, Object2DoubleArrayMap.this.size);
      }

      public void forEach(Consumer<? super K> action) {
         Object[] key = Object2DoubleArrayMap.this.key;
         int i = 0;

         for(int max = Object2DoubleArrayMap.this.size; i < max; ++i) {
            action.accept(key[i]);
         }

      }

      public int size() {
         return Object2DoubleArrayMap.this.size;
      }

      public void clear() {
         Object2DoubleArrayMap.this.clear();
      }

      // $FF: synthetic method
      KeySet(Object x1) {
         this();
      }

      final class KeySetSpliterator extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<K> implements ObjectSpliterator<K> {
         KeySetSpliterator(int param2, int param3) {
            super(pos, maxPos);
         }

         public int characteristics() {
            return 16465;
         }

         protected final K get(int location) {
            return Object2DoubleArrayMap.this.key[location];
         }

         protected final Object2DoubleArrayMap<K>.KeySet.KeySetSpliterator makeForSplit(int pos, int maxPos) {
            return KeySet.this.new KeySetSpliterator(pos, maxPos);
         }

         public void forEachRemaining(Consumer<? super K> action) {
            Object[] key = Object2DoubleArrayMap.this.key;
            int max = Object2DoubleArrayMap.this.size;

            while(this.pos < max) {
               action.accept(key[this.pos++]);
            }

         }
      }
   }

   private final class ValuesCollection extends AbstractDoubleCollection {
      private ValuesCollection() {
      }

      public boolean contains(double v) {
         return Object2DoubleArrayMap.this.containsValue(v);
      }

      public DoubleIterator iterator() {
         return new DoubleIterator() {
            int pos = 0;

            public boolean hasNext() {
               return this.pos < Object2DoubleArrayMap.this.size;
            }

            public double nextDouble() {
               if (!this.hasNext()) {
                  throw new NoSuchElementException();
               } else {
                  return Object2DoubleArrayMap.this.value[this.pos++];
               }
            }

            public void remove() {
               if (this.pos == 0) {
                  throw new IllegalStateException();
               } else {
                  int tail = Object2DoubleArrayMap.this.size - this.pos;
                  System.arraycopy(Object2DoubleArrayMap.this.key, this.pos, Object2DoubleArrayMap.this.key, this.pos - 1, tail);
                  System.arraycopy(Object2DoubleArrayMap.this.value, this.pos, Object2DoubleArrayMap.this.value, this.pos - 1, tail);
                  --Object2DoubleArrayMap.this.size;
                  --this.pos;
                  Object2DoubleArrayMap.this.key[Object2DoubleArrayMap.this.size] = null;
               }
            }

            public void forEachRemaining(DoubleConsumer action) {
               double[] value = Object2DoubleArrayMap.this.value;
               int max = Object2DoubleArrayMap.this.size;

               while(this.pos < max) {
                  action.accept(value[this.pos++]);
               }

            }
         };
      }

      public DoubleSpliterator spliterator() {
         return new Object2DoubleArrayMap.ValuesCollection.ValuesSpliterator(0, Object2DoubleArrayMap.this.size);
      }

      public void forEach(DoubleConsumer action) {
         double[] value = Object2DoubleArrayMap.this.value;
         int i = 0;

         for(int max = Object2DoubleArrayMap.this.size; i < max; ++i) {
            action.accept(value[i]);
         }

      }

      public int size() {
         return Object2DoubleArrayMap.this.size;
      }

      public void clear() {
         Object2DoubleArrayMap.this.clear();
      }

      // $FF: synthetic method
      ValuesCollection(Object x1) {
         this();
      }

      final class ValuesSpliterator extends DoubleSpliterators.EarlyBindingSizeIndexBasedSpliterator implements DoubleSpliterator {
         ValuesSpliterator(int param2, int param3) {
            super(pos, maxPos);
         }

         public int characteristics() {
            return 16720;
         }

         protected final double get(int location) {
            return Object2DoubleArrayMap.this.value[location];
         }

         protected final Object2DoubleArrayMap<K>.ValuesCollection.ValuesSpliterator makeForSplit(int pos, int maxPos) {
            return ValuesCollection.this.new ValuesSpliterator(pos, maxPos);
         }

         public void forEachRemaining(DoubleConsumer action) {
            double[] value = Object2DoubleArrayMap.this.value;
            int max = Object2DoubleArrayMap.this.size;

            while(this.pos < max) {
               action.accept(value[this.pos++]);
            }

         }
      }
   }

   private final class MapEntry implements Object2DoubleMap.Entry<K>, java.util.Map.Entry<K, Double>, ObjectDoublePair<K> {
      int index;

      MapEntry() {
      }

      MapEntry(final int param2) {
         this.index = index;
      }

      public K getKey() {
         return Object2DoubleArrayMap.this.key[this.index];
      }

      public K left() {
         return Object2DoubleArrayMap.this.key[this.index];
      }

      public double getDoubleValue() {
         return Object2DoubleArrayMap.this.value[this.index];
      }

      public double rightDouble() {
         return Object2DoubleArrayMap.this.value[this.index];
      }

      public double setValue(double v) {
         double oldValue = Object2DoubleArrayMap.this.value[this.index];
         Object2DoubleArrayMap.this.value[this.index] = v;
         return oldValue;
      }

      public ObjectDoublePair<K> right(double v) {
         Object2DoubleArrayMap.this.value[this.index] = v;
         return this;
      }

      /** @deprecated */
      @Deprecated
      public Double getValue() {
         return Object2DoubleArrayMap.this.value[this.index];
      }

      /** @deprecated */
      @Deprecated
      public Double setValue(Double v) {
         return this.setValue(v);
      }

      public boolean equals(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else {
            java.util.Map.Entry<K, Double> e = (java.util.Map.Entry)o;
            return Objects.equals(Object2DoubleArrayMap.this.key[this.index], e.getKey()) && Double.doubleToLongBits(Object2DoubleArrayMap.this.value[this.index]) == Double.doubleToLongBits((Double)e.getValue());
         }
      }

      public int hashCode() {
         return (Object2DoubleArrayMap.this.key[this.index] == null ? 0 : Object2DoubleArrayMap.this.key[this.index].hashCode()) ^ HashCommon.double2int(Object2DoubleArrayMap.this.value[this.index]);
      }

      public String toString() {
         return Object2DoubleArrayMap.this.key[this.index] + "=>" + Object2DoubleArrayMap.this.value[this.index];
      }
   }
}
