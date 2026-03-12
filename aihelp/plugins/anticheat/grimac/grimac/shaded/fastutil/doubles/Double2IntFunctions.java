package ac.grim.grimac.shaded.fastutil.doubles;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.DoubleToIntFunction;
import java.util.function.Function;

public final class Double2IntFunctions {
   public static final Double2IntFunctions.EmptyFunction EMPTY_FUNCTION = new Double2IntFunctions.EmptyFunction();

   private Double2IntFunctions() {
   }

   public static Double2IntFunction singleton(double key, int value) {
      return new Double2IntFunctions.Singleton(key, value);
   }

   public static Double2IntFunction singleton(Double key, Integer value) {
      return new Double2IntFunctions.Singleton(key, value);
   }

   public static Double2IntFunction synchronize(Double2IntFunction f) {
      return new Double2IntFunctions.SynchronizedFunction(f);
   }

   public static Double2IntFunction synchronize(Double2IntFunction f, Object sync) {
      return new Double2IntFunctions.SynchronizedFunction(f, sync);
   }

   public static Double2IntFunction unmodifiable(Double2IntFunction f) {
      return new Double2IntFunctions.UnmodifiableFunction(f);
   }

   public static Double2IntFunction primitive(Function<? super Double, ? extends Integer> f) {
      Objects.requireNonNull(f);
      if (f instanceof Double2IntFunction) {
         return (Double2IntFunction)f;
      } else if (f instanceof DoubleToIntFunction) {
         DoubleToIntFunction var10000 = (DoubleToIntFunction)f;
         Objects.requireNonNull((DoubleToIntFunction)f);
         return var10000::applyAsInt;
      } else {
         return new Double2IntFunctions.PrimitiveFunction(f);
      }
   }

   public static class Singleton extends AbstractDouble2IntFunction implements Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final double key;
      protected final int value;

      protected Singleton(double key, int value) {
         this.key = key;
         this.value = value;
      }

      public boolean containsKey(double k) {
         return Double.doubleToLongBits(this.key) == Double.doubleToLongBits(k);
      }

      public int get(double k) {
         return Double.doubleToLongBits(this.key) == Double.doubleToLongBits(k) ? this.value : this.defRetValue;
      }

      public int getOrDefault(double k, int defaultValue) {
         return Double.doubleToLongBits(this.key) == Double.doubleToLongBits(k) ? this.value : defaultValue;
      }

      public int size() {
         return 1;
      }

      public Object clone() {
         return this;
      }
   }

   public static class SynchronizedFunction implements Double2IntFunction, Serializable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Double2IntFunction function;
      protected final Object sync;

      protected SynchronizedFunction(Double2IntFunction f, Object sync) {
         if (f == null) {
            throw new NullPointerException();
         } else {
            this.function = f;
            this.sync = sync;
         }
      }

      protected SynchronizedFunction(Double2IntFunction f) {
         if (f == null) {
            throw new NullPointerException();
         } else {
            this.function = f;
            this.sync = this;
         }
      }

      public int applyAsInt(double operand) {
         synchronized(this.sync) {
            return this.function.applyAsInt(operand);
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer apply(Double key) {
         synchronized(this.sync) {
            return (Integer)this.function.apply(key);
         }
      }

      public int size() {
         synchronized(this.sync) {
            return this.function.size();
         }
      }

      public int defaultReturnValue() {
         synchronized(this.sync) {
            return this.function.defaultReturnValue();
         }
      }

      public void defaultReturnValue(int defRetValue) {
         synchronized(this.sync) {
            this.function.defaultReturnValue(defRetValue);
         }
      }

      public boolean containsKey(double k) {
         synchronized(this.sync) {
            return this.function.containsKey(k);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean containsKey(Object k) {
         synchronized(this.sync) {
            return this.function.containsKey(k);
         }
      }

      public int put(double k, int v) {
         synchronized(this.sync) {
            return this.function.put(k, v);
         }
      }

      public int get(double k) {
         synchronized(this.sync) {
            return this.function.get(k);
         }
      }

      public int getOrDefault(double k, int defaultValue) {
         synchronized(this.sync) {
            return this.function.getOrDefault(k, defaultValue);
         }
      }

      public int remove(double k) {
         synchronized(this.sync) {
            return this.function.remove(k);
         }
      }

      public void clear() {
         synchronized(this.sync) {
            this.function.clear();
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer put(Double k, Integer v) {
         synchronized(this.sync) {
            return this.function.put(k, v);
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer get(Object k) {
         synchronized(this.sync) {
            return this.function.get(k);
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer getOrDefault(Object k, Integer defaultValue) {
         synchronized(this.sync) {
            return this.function.getOrDefault(k, defaultValue);
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer remove(Object k) {
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

   public static class UnmodifiableFunction extends AbstractDouble2IntFunction implements Serializable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Double2IntFunction function;

      protected UnmodifiableFunction(Double2IntFunction f) {
         if (f == null) {
            throw new NullPointerException();
         } else {
            this.function = f;
         }
      }

      public int size() {
         return this.function.size();
      }

      public int defaultReturnValue() {
         return this.function.defaultReturnValue();
      }

      public void defaultReturnValue(int defRetValue) {
         throw new UnsupportedOperationException();
      }

      public boolean containsKey(double k) {
         return this.function.containsKey(k);
      }

      public int put(double k, int v) {
         throw new UnsupportedOperationException();
      }

      public int get(double k) {
         return this.function.get(k);
      }

      public int getOrDefault(double k, int defaultValue) {
         return this.function.getOrDefault(k, defaultValue);
      }

      public int remove(double k) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Integer put(Double k, Integer v) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Integer get(Object k) {
         return this.function.get(k);
      }

      /** @deprecated */
      @Deprecated
      public Integer getOrDefault(Object k, Integer defaultValue) {
         return this.function.getOrDefault(k, defaultValue);
      }

      /** @deprecated */
      @Deprecated
      public Integer remove(Object k) {
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

   public static class PrimitiveFunction implements Double2IntFunction {
      protected final Function<? super Double, ? extends Integer> function;

      protected PrimitiveFunction(Function<? super Double, ? extends Integer> function) {
         this.function = function;
      }

      public boolean containsKey(double key) {
         return this.function.apply(key) != null;
      }

      /** @deprecated */
      @Deprecated
      public boolean containsKey(Object key) {
         if (key == null) {
            return false;
         } else {
            return this.function.apply((Double)key) != null;
         }
      }

      public int get(double key) {
         Integer v = (Integer)this.function.apply(key);
         return v == null ? this.defaultReturnValue() : v;
      }

      public int getOrDefault(double key, int defaultValue) {
         Integer v = (Integer)this.function.apply(key);
         return v == null ? defaultValue : v;
      }

      /** @deprecated */
      @Deprecated
      public Integer get(Object key) {
         return key == null ? null : (Integer)this.function.apply((Double)key);
      }

      /** @deprecated */
      @Deprecated
      public Integer getOrDefault(Object key, Integer defaultValue) {
         if (key == null) {
            return defaultValue;
         } else {
            Integer v;
            return (v = (Integer)this.function.apply((Double)key)) == null ? defaultValue : v;
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer put(Double key, Integer value) {
         throw new UnsupportedOperationException();
      }
   }

   public static class EmptyFunction extends AbstractDouble2IntFunction implements Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptyFunction() {
      }

      public int get(double k) {
         return 0;
      }

      public int getOrDefault(double k, int defaultValue) {
         return defaultValue;
      }

      public boolean containsKey(double k) {
         return false;
      }

      public int defaultReturnValue() {
         return 0;
      }

      public void defaultReturnValue(int defRetValue) {
         throw new UnsupportedOperationException();
      }

      public int size() {
         return 0;
      }

      public void clear() {
      }

      public Object clone() {
         return Double2IntFunctions.EMPTY_FUNCTION;
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
         return Double2IntFunctions.EMPTY_FUNCTION;
      }
   }
}
