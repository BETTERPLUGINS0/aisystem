package ac.grim.grimac.shaded.fastutil.objects;

import ac.grim.grimac.shaded.fastutil.HashCommon;
import ac.grim.grimac.shaded.fastutil.Size64;
import ac.grim.grimac.shaded.fastutil.doubles.AbstractDoubleCollection;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleBinaryOperator;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleCollection;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleIterator;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleSpliterator;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleSpliterators;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public abstract class AbstractObject2DoubleMap<K> extends AbstractObject2DoubleFunction<K> implements Object2DoubleMap<K>, Serializable {
   private static final long serialVersionUID = -4940583368468432370L;

   protected AbstractObject2DoubleMap() {
   }

   public boolean containsKey(Object k) {
      ObjectIterator i = this.object2DoubleEntrySet().iterator();

      do {
         if (!i.hasNext()) {
            return false;
         }
      } while(((Object2DoubleMap.Entry)i.next()).getKey() != k);

      return true;
   }

   public boolean containsValue(double v) {
      ObjectIterator i = this.object2DoubleEntrySet().iterator();

      do {
         if (!i.hasNext()) {
            return false;
         }
      } while(((Object2DoubleMap.Entry)i.next()).getDoubleValue() != v);

      return true;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public final double mergeDouble(K key, double value, DoubleBinaryOperator remappingFunction) {
      return this.mergeDouble(key, value, remappingFunction);
   }

   public ObjectSet<K> keySet() {
      return new AbstractObjectSet<K>() {
         public boolean contains(Object k) {
            return AbstractObject2DoubleMap.this.containsKey(k);
         }

         public int size() {
            return AbstractObject2DoubleMap.this.size();
         }

         public void clear() {
            AbstractObject2DoubleMap.this.clear();
         }

         public ObjectIterator<K> iterator() {
            return new ObjectIterator<K>() {
               private final ObjectIterator<Object2DoubleMap.Entry<K>> i = Object2DoubleMaps.fastIterator(AbstractObject2DoubleMap.this);

               public K next() {
                  return ((Object2DoubleMap.Entry)this.i.next()).getKey();
               }

               public boolean hasNext() {
                  return this.i.hasNext();
               }

               public void remove() {
                  this.i.remove();
               }

               public void forEachRemaining(Consumer<? super K> action) {
                  this.i.forEachRemaining((entry) -> {
                     action.accept(entry.getKey());
                  });
               }
            };
         }

         public ObjectSpliterator<K> spliterator() {
            return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Map)AbstractObject2DoubleMap.this), 65);
         }
      };
   }

   public DoubleCollection values() {
      return new AbstractDoubleCollection() {
         public boolean contains(double k) {
            return AbstractObject2DoubleMap.this.containsValue(k);
         }

         public int size() {
            return AbstractObject2DoubleMap.this.size();
         }

         public void clear() {
            AbstractObject2DoubleMap.this.clear();
         }

         public DoubleIterator iterator() {
            return new DoubleIterator() {
               private final ObjectIterator<Object2DoubleMap.Entry<K>> i = Object2DoubleMaps.fastIterator(AbstractObject2DoubleMap.this);

               public double nextDouble() {
                  return ((Object2DoubleMap.Entry)this.i.next()).getDoubleValue();
               }

               public boolean hasNext() {
                  return this.i.hasNext();
               }

               public void remove() {
                  this.i.remove();
               }

               public void forEachRemaining(DoubleConsumer action) {
                  this.i.forEachRemaining((entry) -> {
                     action.accept(entry.getDoubleValue());
                  });
               }
            };
         }

         public DoubleSpliterator spliterator() {
            return DoubleSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Map)AbstractObject2DoubleMap.this), 320);
         }
      };
   }

   public void putAll(Map<? extends K, ? extends Double> m) {
      if (m instanceof Object2DoubleMap) {
         ObjectIterator i = Object2DoubleMaps.fastIterator((Object2DoubleMap)m);

         while(i.hasNext()) {
            Object2DoubleMap.Entry<? extends K> e = (Object2DoubleMap.Entry)i.next();
            this.put(e.getKey(), e.getDoubleValue());
         }
      } else {
         int n = m.size();
         Iterator i = m.entrySet().iterator();

         while(n-- != 0) {
            java.util.Map.Entry<? extends K, ? extends Double> e = (java.util.Map.Entry)i.next();
            this.put(e.getKey(), (Double)e.getValue());
         }
      }

   }

   public int hashCode() {
      int h = 0;
      int n = this.size();

      for(ObjectIterator i = Object2DoubleMaps.fastIterator(this); n-- != 0; h += ((Object2DoubleMap.Entry)i.next()).hashCode()) {
      }

      return h;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Map)) {
         return false;
      } else {
         Map<?, ?> m = (Map)o;
         return m.size() != this.size() ? false : this.object2DoubleEntrySet().containsAll(m.entrySet());
      }
   }

   public String toString() {
      StringBuilder s = new StringBuilder();
      ObjectIterator<Object2DoubleMap.Entry<K>> i = Object2DoubleMaps.fastIterator(this);
      int n = this.size();
      boolean first = true;
      s.append("{");

      while(n-- != 0) {
         if (first) {
            first = false;
         } else {
            s.append(", ");
         }

         Object2DoubleMap.Entry<K> e = (Object2DoubleMap.Entry)i.next();
         if (this == e.getKey()) {
            s.append("(this map)");
         } else {
            s.append(String.valueOf(e.getKey()));
         }

         s.append("=>");
         s.append(String.valueOf(e.getDoubleValue()));
      }

      s.append("}");
      return s.toString();
   }

   public abstract static class BasicEntrySet<K> extends AbstractObjectSet<Object2DoubleMap.Entry<K>> {
      protected final Object2DoubleMap<K> map;

      public BasicEntrySet(Object2DoubleMap<K> map) {
         this.map = map;
      }

      public boolean contains(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else {
            Object k;
            if (o instanceof Object2DoubleMap.Entry) {
               Object2DoubleMap.Entry<K> e = (Object2DoubleMap.Entry)o;
               k = e.getKey();
               return this.map.containsKey(k) && Double.doubleToLongBits(this.map.getDouble(k)) == Double.doubleToLongBits(e.getDoubleValue());
            } else {
               java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
               k = e.getKey();
               Object value = e.getValue();
               if (value != null && value instanceof Double) {
                  return this.map.containsKey(k) && Double.doubleToLongBits(this.map.getDouble(k)) == Double.doubleToLongBits((Double)value);
               } else {
                  return false;
               }
            }
         }
      }

      public boolean remove(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else if (o instanceof Object2DoubleMap.Entry) {
            Object2DoubleMap.Entry<K> e = (Object2DoubleMap.Entry)o;
            return this.map.remove(e.getKey(), e.getDoubleValue());
         } else {
            java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
            Object k = e.getKey();
            Object value = e.getValue();
            if (value != null && value instanceof Double) {
               double v = (Double)value;
               return this.map.remove(k, v);
            } else {
               return false;
            }
         }
      }

      public int size() {
         return this.map.size();
      }

      public ObjectSpliterator<Object2DoubleMap.Entry<K>> spliterator() {
         return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Map)this.map), 65);
      }
   }

   public static class BasicEntry<K> implements Object2DoubleMap.Entry<K> {
      protected K key;
      protected double value;

      public BasicEntry() {
      }

      public BasicEntry(K key, Double value) {
         this.key = key;
         this.value = value;
      }

      public BasicEntry(K key, double value) {
         this.key = key;
         this.value = value;
      }

      public K getKey() {
         return this.key;
      }

      public double getDoubleValue() {
         return this.value;
      }

      public double setValue(double value) {
         throw new UnsupportedOperationException();
      }

      public boolean equals(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else if (o instanceof Object2DoubleMap.Entry) {
            Object2DoubleMap.Entry<K> e = (Object2DoubleMap.Entry)o;
            return Objects.equals(this.key, e.getKey()) && Double.doubleToLongBits(this.value) == Double.doubleToLongBits(e.getDoubleValue());
         } else {
            java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
            Object key = e.getKey();
            Object value = e.getValue();
            if (value != null && value instanceof Double) {
               return Objects.equals(this.key, key) && Double.doubleToLongBits(this.value) == Double.doubleToLongBits((Double)value);
            } else {
               return false;
            }
         }
      }

      public int hashCode() {
         return (this.key == null ? 0 : this.key.hashCode()) ^ HashCommon.double2int(this.value);
      }

      public String toString() {
         return this.key + "->" + this.value;
      }
   }
}
