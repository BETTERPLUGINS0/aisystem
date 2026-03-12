package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Functions {
   private Functions() {
   }

   public static Function<Object, String> toStringFunction() {
      return Functions.ToStringFunction.INSTANCE;
   }

   public static <E> Function<E, E> identity() {
      return Functions.IdentityFunction.INSTANCE;
   }

   public static <K, V> Function<K, V> forMap(Map<K, V> map) {
      return new Functions.FunctionForMapNoDefault(map);
   }

   public static <K, V> Function<K, V> forMap(Map<K, ? extends V> map, @ParametricNullness V defaultValue) {
      return new Functions.ForMapWithDefault(map, defaultValue);
   }

   public static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, ? extends B> f) {
      return new Functions.FunctionComposition(g, f);
   }

   public static <T> Function<T, Boolean> forPredicate(Predicate<T> predicate) {
      return new Functions.PredicateFunction(predicate);
   }

   public static <E> Function<Object, E> constant(@ParametricNullness E value) {
      return new Functions.ConstantFunction(value);
   }

   public static <F, T> Function<F, T> forSupplier(Supplier<T> supplier) {
      return new Functions.SupplierFunction(supplier);
   }

   private static class SupplierFunction<F, T> implements Function<F, T>, Serializable {
      private final Supplier<T> supplier;
      private static final long serialVersionUID = 0L;

      private SupplierFunction(Supplier<T> supplier) {
         this.supplier = (Supplier)Preconditions.checkNotNull(supplier);
      }

      @ParametricNullness
      public T apply(@ParametricNullness F input) {
         return this.supplier.get();
      }

      public boolean equals(@CheckForNull Object obj) {
         if (obj instanceof Functions.SupplierFunction) {
            Functions.SupplierFunction<?, ?> that = (Functions.SupplierFunction)obj;
            return this.supplier.equals(that.supplier);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.supplier.hashCode();
      }

      public String toString() {
         String var1 = String.valueOf(this.supplier);
         return (new StringBuilder(23 + String.valueOf(var1).length())).append("Functions.forSupplier(").append(var1).append(")").toString();
      }

      // $FF: synthetic method
      SupplierFunction(Supplier x0, Object x1) {
         this(x0);
      }
   }

   private static class ConstantFunction<E> implements Function<Object, E>, Serializable {
      @ParametricNullness
      private final E value;
      private static final long serialVersionUID = 0L;

      public ConstantFunction(@ParametricNullness E value) {
         this.value = value;
      }

      @ParametricNullness
      public E apply(@CheckForNull Object from) {
         return this.value;
      }

      public boolean equals(@CheckForNull Object obj) {
         if (obj instanceof Functions.ConstantFunction) {
            Functions.ConstantFunction<?> that = (Functions.ConstantFunction)obj;
            return Objects.equal(this.value, that.value);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.value == null ? 0 : this.value.hashCode();
      }

      public String toString() {
         String var1 = String.valueOf(this.value);
         return (new StringBuilder(20 + String.valueOf(var1).length())).append("Functions.constant(").append(var1).append(")").toString();
      }
   }

   private static class PredicateFunction<T> implements Function<T, Boolean>, Serializable {
      private final Predicate<T> predicate;
      private static final long serialVersionUID = 0L;

      private PredicateFunction(Predicate<T> predicate) {
         this.predicate = (Predicate)Preconditions.checkNotNull(predicate);
      }

      public Boolean apply(@ParametricNullness T t) {
         return this.predicate.apply(t);
      }

      public boolean equals(@CheckForNull Object obj) {
         if (obj instanceof Functions.PredicateFunction) {
            Functions.PredicateFunction<?> that = (Functions.PredicateFunction)obj;
            return this.predicate.equals(that.predicate);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.predicate.hashCode();
      }

      public String toString() {
         String var1 = String.valueOf(this.predicate);
         return (new StringBuilder(24 + String.valueOf(var1).length())).append("Functions.forPredicate(").append(var1).append(")").toString();
      }

      // $FF: synthetic method
      PredicateFunction(Predicate x0, Object x1) {
         this(x0);
      }
   }

   private static class FunctionComposition<A, B, C> implements Function<A, C>, Serializable {
      private final Function<B, C> g;
      private final Function<A, ? extends B> f;
      private static final long serialVersionUID = 0L;

      public FunctionComposition(Function<B, C> g, Function<A, ? extends B> f) {
         this.g = (Function)Preconditions.checkNotNull(g);
         this.f = (Function)Preconditions.checkNotNull(f);
      }

      @ParametricNullness
      public C apply(@ParametricNullness A a) {
         return this.g.apply(this.f.apply(a));
      }

      public boolean equals(@CheckForNull Object obj) {
         if (!(obj instanceof Functions.FunctionComposition)) {
            return false;
         } else {
            Functions.FunctionComposition<?, ?, ?> that = (Functions.FunctionComposition)obj;
            return this.f.equals(that.f) && this.g.equals(that.g);
         }
      }

      public int hashCode() {
         return this.f.hashCode() ^ this.g.hashCode();
      }

      public String toString() {
         String var1 = String.valueOf(this.g);
         String var2 = String.valueOf(this.f);
         return (new StringBuilder(2 + String.valueOf(var1).length() + String.valueOf(var2).length())).append(var1).append("(").append(var2).append(")").toString();
      }
   }

   private static class ForMapWithDefault<K, V> implements Function<K, V>, Serializable {
      final Map<K, ? extends V> map;
      @ParametricNullness
      final V defaultValue;
      private static final long serialVersionUID = 0L;

      ForMapWithDefault(Map<K, ? extends V> map, @ParametricNullness V defaultValue) {
         this.map = (Map)Preconditions.checkNotNull(map);
         this.defaultValue = defaultValue;
      }

      @ParametricNullness
      public V apply(@ParametricNullness K key) {
         V result = this.map.get(key);
         return result == null && !this.map.containsKey(key) ? this.defaultValue : NullnessCasts.uncheckedCastNullableTToT(result);
      }

      public boolean equals(@CheckForNull Object o) {
         if (!(o instanceof Functions.ForMapWithDefault)) {
            return false;
         } else {
            Functions.ForMapWithDefault<?, ?> that = (Functions.ForMapWithDefault)o;
            return this.map.equals(that.map) && Objects.equal(this.defaultValue, that.defaultValue);
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.map, this.defaultValue);
      }

      public String toString() {
         String var1 = String.valueOf(this.map);
         String var2 = String.valueOf(this.defaultValue);
         return (new StringBuilder(33 + String.valueOf(var1).length() + String.valueOf(var2).length())).append("Functions.forMap(").append(var1).append(", defaultValue=").append(var2).append(")").toString();
      }
   }

   private static class FunctionForMapNoDefault<K, V> implements Function<K, V>, Serializable {
      final Map<K, V> map;
      private static final long serialVersionUID = 0L;

      FunctionForMapNoDefault(Map<K, V> map) {
         this.map = (Map)Preconditions.checkNotNull(map);
      }

      @ParametricNullness
      public V apply(@ParametricNullness K key) {
         V result = this.map.get(key);
         Preconditions.checkArgument(result != null || this.map.containsKey(key), "Key '%s' not present in map", key);
         return NullnessCasts.uncheckedCastNullableTToT(result);
      }

      public boolean equals(@CheckForNull Object o) {
         if (o instanceof Functions.FunctionForMapNoDefault) {
            Functions.FunctionForMapNoDefault<?, ?> that = (Functions.FunctionForMapNoDefault)o;
            return this.map.equals(that.map);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.map.hashCode();
      }

      public String toString() {
         String var1 = String.valueOf(this.map);
         return (new StringBuilder(18 + String.valueOf(var1).length())).append("Functions.forMap(").append(var1).append(")").toString();
      }
   }

   private static enum IdentityFunction implements Function<Object, Object> {
      INSTANCE;

      @CheckForNull
      public Object apply(@CheckForNull Object o) {
         return o;
      }

      public String toString() {
         return "Functions.identity()";
      }

      // $FF: synthetic method
      private static Functions.IdentityFunction[] $values() {
         return new Functions.IdentityFunction[]{INSTANCE};
      }
   }

   private static enum ToStringFunction implements Function<Object, String> {
      INSTANCE;

      public String apply(Object o) {
         Preconditions.checkNotNull(o);
         return o.toString();
      }

      public String toString() {
         return "Functions.toStringFunction()";
      }

      // $FF: synthetic method
      private static Functions.ToStringFunction[] $values() {
         return new Functions.ToStringFunction[]{INSTANCE};
      }
   }
}
