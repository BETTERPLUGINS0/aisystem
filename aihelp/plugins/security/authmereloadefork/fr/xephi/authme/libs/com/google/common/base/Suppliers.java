package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Suppliers {
   private Suppliers() {
   }

   public static <F, T> Supplier<T> compose(Function<? super F, T> function, Supplier<F> supplier) {
      return new Suppliers.SupplierComposition(function, supplier);
   }

   public static <T> Supplier<T> memoize(Supplier<T> delegate) {
      if (!(delegate instanceof Suppliers.NonSerializableMemoizingSupplier) && !(delegate instanceof Suppliers.MemoizingSupplier)) {
         return (Supplier)(delegate instanceof Serializable ? new Suppliers.MemoizingSupplier(delegate) : new Suppliers.NonSerializableMemoizingSupplier(delegate));
      } else {
         return delegate;
      }
   }

   public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> delegate, long duration, TimeUnit unit) {
      return new Suppliers.ExpiringMemoizingSupplier(delegate, duration, unit);
   }

   public static <T> Supplier<T> ofInstance(@ParametricNullness T instance) {
      return new Suppliers.SupplierOfInstance(instance);
   }

   public static <T> Supplier<T> synchronizedSupplier(Supplier<T> delegate) {
      return new Suppliers.ThreadSafeSupplier(delegate);
   }

   public static <T> Function<Supplier<T>, T> supplierFunction() {
      Suppliers.SupplierFunction<T> sf = Suppliers.SupplierFunctionImpl.INSTANCE;
      return sf;
   }

   private static enum SupplierFunctionImpl implements Suppliers.SupplierFunction<Object> {
      INSTANCE;

      @CheckForNull
      public Object apply(Supplier<Object> input) {
         return input.get();
      }

      public String toString() {
         return "Suppliers.supplierFunction()";
      }

      // $FF: synthetic method
      private static Suppliers.SupplierFunctionImpl[] $values() {
         return new Suppliers.SupplierFunctionImpl[]{INSTANCE};
      }
   }

   private interface SupplierFunction<T> extends Function<Supplier<T>, T> {
   }

   private static class ThreadSafeSupplier<T> implements Supplier<T>, Serializable {
      final Supplier<T> delegate;
      private static final long serialVersionUID = 0L;

      ThreadSafeSupplier(Supplier<T> delegate) {
         this.delegate = (Supplier)Preconditions.checkNotNull(delegate);
      }

      @ParametricNullness
      public T get() {
         synchronized(this.delegate) {
            return this.delegate.get();
         }
      }

      public String toString() {
         String var1 = String.valueOf(this.delegate);
         return (new StringBuilder(32 + String.valueOf(var1).length())).append("Suppliers.synchronizedSupplier(").append(var1).append(")").toString();
      }
   }

   private static class SupplierOfInstance<T> implements Supplier<T>, Serializable {
      @ParametricNullness
      final T instance;
      private static final long serialVersionUID = 0L;

      SupplierOfInstance(@ParametricNullness T instance) {
         this.instance = instance;
      }

      @ParametricNullness
      public T get() {
         return this.instance;
      }

      public boolean equals(@CheckForNull Object obj) {
         if (obj instanceof Suppliers.SupplierOfInstance) {
            Suppliers.SupplierOfInstance<?> that = (Suppliers.SupplierOfInstance)obj;
            return Objects.equal(this.instance, that.instance);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.instance);
      }

      public String toString() {
         String var1 = String.valueOf(this.instance);
         return (new StringBuilder(22 + String.valueOf(var1).length())).append("Suppliers.ofInstance(").append(var1).append(")").toString();
      }
   }

   @VisibleForTesting
   static class ExpiringMemoizingSupplier<T> implements Supplier<T>, Serializable {
      final Supplier<T> delegate;
      final long durationNanos;
      @CheckForNull
      transient volatile T value;
      transient volatile long expirationNanos;
      private static final long serialVersionUID = 0L;

      ExpiringMemoizingSupplier(Supplier<T> delegate, long duration, TimeUnit unit) {
         this.delegate = (Supplier)Preconditions.checkNotNull(delegate);
         this.durationNanos = unit.toNanos(duration);
         Preconditions.checkArgument(duration > 0L, "duration (%s %s) must be > 0", duration, unit);
      }

      @ParametricNullness
      public T get() {
         long nanos = this.expirationNanos;
         long now = Platform.systemNanoTime();
         if (nanos == 0L || now - nanos >= 0L) {
            synchronized(this) {
               if (nanos == this.expirationNanos) {
                  T t = this.delegate.get();
                  this.value = t;
                  nanos = now + this.durationNanos;
                  this.expirationNanos = nanos == 0L ? 1L : nanos;
                  return t;
               }
            }
         }

         return NullnessCasts.uncheckedCastNullableTToT(this.value);
      }

      public String toString() {
         String var1 = String.valueOf(this.delegate);
         long var2 = this.durationNanos;
         return (new StringBuilder(62 + String.valueOf(var1).length())).append("Suppliers.memoizeWithExpiration(").append(var1).append(", ").append(var2).append(", NANOS)").toString();
      }
   }

   @VisibleForTesting
   static class NonSerializableMemoizingSupplier<T> implements Supplier<T> {
      @CheckForNull
      volatile Supplier<T> delegate;
      volatile boolean initialized;
      @CheckForNull
      T value;

      NonSerializableMemoizingSupplier(Supplier<T> delegate) {
         this.delegate = (Supplier)Preconditions.checkNotNull(delegate);
      }

      @ParametricNullness
      public T get() {
         if (!this.initialized) {
            synchronized(this) {
               if (!this.initialized) {
                  T t = ((Supplier)java.util.Objects.requireNonNull(this.delegate)).get();
                  this.value = t;
                  this.initialized = true;
                  this.delegate = null;
                  return t;
               }
            }
         }

         return NullnessCasts.uncheckedCastNullableTToT(this.value);
      }

      public String toString() {
         Supplier<T> delegate = this.delegate;
         Object var10000;
         String var2;
         if (delegate == null) {
            var2 = String.valueOf(this.value);
            var10000 = (new StringBuilder(25 + String.valueOf(var2).length())).append("<supplier that returned ").append(var2).append(">").toString();
         } else {
            var10000 = delegate;
         }

         var2 = String.valueOf(var10000);
         return (new StringBuilder(19 + String.valueOf(var2).length())).append("Suppliers.memoize(").append(var2).append(")").toString();
      }
   }

   @VisibleForTesting
   static class MemoizingSupplier<T> implements Supplier<T>, Serializable {
      final Supplier<T> delegate;
      transient volatile boolean initialized;
      @CheckForNull
      transient T value;
      private static final long serialVersionUID = 0L;

      MemoizingSupplier(Supplier<T> delegate) {
         this.delegate = (Supplier)Preconditions.checkNotNull(delegate);
      }

      @ParametricNullness
      public T get() {
         if (!this.initialized) {
            synchronized(this) {
               if (!this.initialized) {
                  T t = this.delegate.get();
                  this.value = t;
                  this.initialized = true;
                  return t;
               }
            }
         }

         return NullnessCasts.uncheckedCastNullableTToT(this.value);
      }

      public String toString() {
         Object var10000;
         String var1;
         if (this.initialized) {
            var1 = String.valueOf(this.value);
            var10000 = (new StringBuilder(25 + String.valueOf(var1).length())).append("<supplier that returned ").append(var1).append(">").toString();
         } else {
            var10000 = this.delegate;
         }

         var1 = String.valueOf(var10000);
         return (new StringBuilder(19 + String.valueOf(var1).length())).append("Suppliers.memoize(").append(var1).append(")").toString();
      }
   }

   private static class SupplierComposition<F, T> implements Supplier<T>, Serializable {
      final Function<? super F, T> function;
      final Supplier<F> supplier;
      private static final long serialVersionUID = 0L;

      SupplierComposition(Function<? super F, T> function, Supplier<F> supplier) {
         this.function = (Function)Preconditions.checkNotNull(function);
         this.supplier = (Supplier)Preconditions.checkNotNull(supplier);
      }

      @ParametricNullness
      public T get() {
         return this.function.apply(this.supplier.get());
      }

      public boolean equals(@CheckForNull Object obj) {
         if (!(obj instanceof Suppliers.SupplierComposition)) {
            return false;
         } else {
            Suppliers.SupplierComposition<?, ?> that = (Suppliers.SupplierComposition)obj;
            return this.function.equals(that.function) && this.supplier.equals(that.supplier);
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.function, this.supplier);
      }

      public String toString() {
         String var1 = String.valueOf(this.function);
         String var2 = String.valueOf(this.supplier);
         return (new StringBuilder(21 + String.valueOf(var1).length() + String.valueOf(var2).length())).append("Suppliers.compose(").append(var1).append(", ").append(var2).append(")").toString();
      }
   }
}
