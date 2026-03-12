package ac.grim.grimac.shaded.fastutil.objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public final class Object2DoubleFunctions {
   public static final Object2DoubleFunctions.EmptyFunction EMPTY_FUNCTION = new Object2DoubleFunctions.EmptyFunction();

   private Object2DoubleFunctions() {
   }

   public static <K> Object2DoubleFunction<K> singleton(K key, double value) {
      return new Object2DoubleFunctions.Singleton(key, value);
   }

   public static <K> Object2DoubleFunction<K> singleton(K key, Double value) {
      return new Object2DoubleFunctions.Singleton(key, value);
   }

   public static <K> Object2DoubleFunction<K> synchronize(Object2DoubleFunction<K> f) {
      return new Object2DoubleFunctions.SynchronizedFunction(f);
   }

   public static <K> Object2DoubleFunction<K> synchronize(Object2DoubleFunction<K> f, Object sync) {
      return new Object2DoubleFunctions.SynchronizedFunction(f, sync);
   }

   public static <K> Object2DoubleFunction<K> unmodifiable(Object2DoubleFunction<? extends K> f) {
      return new Object2DoubleFunctions.UnmodifiableFunction(f);
   }

   public static <K> Object2DoubleFunction<K> primitive(Function<? super K, ? extends Double> f) {
      Objects.requireNonNull(f);
      if (f instanceof Object2DoubleFunction) {
         return (Object2DoubleFunction)f;
      } else {
         return (Object2DoubleFunction)(f instanceof ToDoubleFunction ? (key) -> {
            return ((ToDoubleFunction)f).applyAsDouble(key);
         } : new Object2DoubleFunctions.PrimitiveFunction(f));
      }
   }

   public static class Singleton<K> extends AbstractObject2DoubleFunction<K> implements Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final K key;
      protected final double value;

      protected Singleton(K key, double value) {
         this.key = key;
         this.value = value;
      }

      public boolean containsKey(Object k) {
         return Objects.equals(this.key, k);
      }

      public double getDouble(Object k) {
         return Objects.equals(this.key, k) ? this.value : this.defRetValue;
      }

      public double getOrDefault(Object k, double defaultValue) {
         return Objects.equals(this.key, k) ? this.value : defaultValue;
      }

      public int size() {
         return 1;
      }

      public Object clone() {
         return this;
      }
   }

   public static class SynchronizedFunction<K> implements Object2DoubleFunction<K>, Serializable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Object2DoubleFunction<K> function;
      protected final Object sync;

      protected SynchronizedFunction(Object2DoubleFunction<K> f, Object sync) {
         if (f == null) {
            throw new NullPointerException();
         } else {
            this.function = f;
            this.sync = sync;
         }
      }

      protected SynchronizedFunction(Object2DoubleFunction<K> f) {
         if (f == null) {
            throw new NullPointerException();
         } else {
            this.function = f;
            this.sync = this;
         }
      }

      public double applyAsDouble(K operand) {
         synchronized(this.sync) {
            return this.function.applyAsDouble(operand);
         }
      }

      /** @deprecated */
      @Deprecated
      public Double apply(K key) {
         synchronized(this.sync) {
            return (Double)this.function.apply(key);
         }
      }

      public int size() {
         synchronized(this.sync) {
            return this.function.size();
         }
      }

      public double defaultReturnValue() {
         synchronized(this.sync) {
            return this.function.defaultReturnValue();
         }
      }

      public void defaultReturnValue(double defRetValue) {
         synchronized(this.sync) {
            this.function.defaultReturnValue(defRetValue);
         }
      }

      public boolean containsKey(Object k) {
         synchronized(this.sync) {
            return this.function.containsKey(k);
         }
      }

      public double put(K k, double v) {
         synchronized(this.sync) {
            return this.function.put(k, v);
         }
      }

      public double getDouble(Object k) {
         synchronized(this.sync) {
            return this.function.getDouble(k);
         }
      }

      public double getOrDefault(Object k, double defaultValue) {
         synchronized(this.sync) {
            return this.function.getOrDefault(k, defaultValue);
         }
      }

      public double removeDouble(Object k) {
         synchronized(this.sync) {
            return this.function.removeDouble(k);
         }
      }

      public void clear() {
         synchronized(this.sync) {
            this.function.clear();
         }
      }

      /** @deprecated */
      @Deprecated
      public Double put(K k, Double v) {
         synchronized(this.sync) {
            return this.function.put(k, v);
         }
      }

      /** @deprecated */
      @Deprecated
      public Double get(Object k) {
         synchronized(this.sync) {
            return this.function.get(k);
         }
      }

      /** @deprecated */
      @Deprecated
      public Double getOrDefault(Object k, Double defaultValue) {
         synchronized(this.sync) {
            return this.function.getOrDefault(k, defaultValue);
         }
      }

      /** @deprecated */
      @Deprecated
      public Double remove(Object k) {
         synchronized(this.sync) {
            return this.function.remove(k);
         }
      }

      public int hashCode() {
         synchronized(this.sync) {
            return this.function.hashCode();
         }
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else {
            synchronized(this.sync) {
               return this.function.equals(o);
            }
         }
      }

      public String toString() {
         synchronized(this.sync) {
            return this.function.toString();
         }
      }

      private void writeObject(ObjectOutputStream s) throws IOException {
         synchronized(this.sync) {
            s.defaultWriteObject();
         }
      }
   }

   public static class UnmodifiableFunction<K> extends AbstractObject2DoubleFunction<K> implements Serializable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Object2DoubleFunction<? extends K> function;

      protected UnmodifiableFunction(Object2DoubleFunction<? extends K> f) {
         if (f == null) {
            throw new NullPointerException();
         } else {
            this.function = f;
         }
      }

      public int size() {
         return this.function.size();
      }

      public double defaultReturnValue() {
         return this.function.defaultReturnValue();
      }

      public void defaultReturnValue(double defRetValue) {
         throw new UnsupportedOperationException();
      }

      public boolean containsKey(Object k) {
         return this.function.containsKey(k);
      }

      public double put(K k, double v) {
         throw new UnsupportedOperationException();
      }

      public double getDouble(Object k) {
         return this.function.getDouble(k);
      }

      public double getOrDefault(Object k, double defaultValue) {
         return this.function.getOrDefault(k, defaultValue);
      }

      public double removeDouble(Object k) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Double put(K k, Double v) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Double get(Object k) {
         return this.function.get(k);
      }

      /** @deprecated */
      @Deprecated
      public Double getOrDefault(Object k, Double defaultValue) {
         return this.function.getOrDefault(k, defaultValue);
      }

      /** @deprecated */
      @Deprecated
      public Double remove(Object k) {
         throw new UnsupportedOperationException();
      }

      public int hashCode() {
         return this.function.hashCode();
      }

      public boolean equals(Object o) {
         return o == this || this.function.equals(o);
      }

      public String toString() {
         return this.function.toString();
      }
   }

   public static class PrimitiveFunction<K> implements Object2DoubleFunction<K> {
      protected final Function<? super K, ? extends Double> function;

      protected PrimitiveFunction(Function<? super K, ? extends Double> function) {
         this.function = function;
      }

      public boolean containsKey(Object key) {
         return this.function.apply(key) != null;
      }

      public double getDouble(Object key) {
         Double v = (Double)this.function.apply(key);
         return v == null ? this.defaultReturnValue() : v;
      }

      public double getOrDefault(Object key, double defaultValue) {
         Double v = (Double)this.function.apply(key);
         return v == null ? defaultValue : v;
      }

      /** @deprecated */
      @Deprecated
      public Double get(Object key) {
         return (Double)this.function.apply(key);
      }

      /** @deprecated */
      @Deprecated
      public Double getOrDefault(Object key, Double defaultValue) {
         Double v;
         return (v = (Double)this.function.apply(key)) == null ? defaultValue : v;
      }

      /** @deprecated */
      @Deprecated
      public Double put(K key, Double value) {
         throw new UnsupportedOperationException();
      }
   }

   public static class EmptyFunction<K> extends AbstractObject2DoubleFunction<K> implements Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptyFunction() {
      }

      public double getDouble(Object k) {
         return 0.0D;
      }

      public double getOrDefault(Object k, double defaultValue) {
         return defaultValue;
      }

      public boolean containsKey(Object k) {
         return false;
      }

      public double defaultReturnValue() {
         return 0.0D;
      }

      public void defaultReturnValue(double defRetValue) {
         throw new UnsupportedOperationException();
      }

      public int size() {
         return 0;
      }

      public void clear() {
      }

      public Object clone() {
         return Object2DoubleFunctions.EMPTY_FUNCTION;
      }

      public int hashCode() {
         return 0;
      }

      public boolean equals(Object o) {
         if (!(o instanceof ac.grim.grimac.shaded.fastutil.Function)) {
            return false;
         } else {
            return ((ac.grim.grimac.shaded.fastutil.Function)o).size() == 0;
         }
      }

      public String toString() {
         return "{}";
      }

      private Object readResolve() {
         return Object2DoubleFunctions.EMPTY_FUNCTION;
      }
   }
}
