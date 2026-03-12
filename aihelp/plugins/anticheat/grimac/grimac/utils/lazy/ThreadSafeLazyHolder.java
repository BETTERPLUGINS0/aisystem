package ac.grim.grimac.utils.lazy;

import java.util.function.Supplier;

final class ThreadSafeLazyHolder<T> implements LazyHolder<T> {
   private final Supplier<T> supplier;
   private volatile T value;

   ThreadSafeLazyHolder(Supplier<T> supplier) {
      this.supplier = supplier;
   }

   public T get() {
      T result = this.value;
      if (result == null) {
         synchronized(this) {
            result = this.value;
            if (result == null) {
               this.value = result = this.supplier.get();
            }
         }
      }

      return result;
   }
}
