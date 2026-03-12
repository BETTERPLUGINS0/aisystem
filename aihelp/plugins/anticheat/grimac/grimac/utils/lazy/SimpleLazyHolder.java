package ac.grim.grimac.utils.lazy;

import java.util.function.Supplier;

final class SimpleLazyHolder<T> implements LazyHolder<T> {
   private T value;
   private Supplier<T> supplier;

   SimpleLazyHolder(Supplier<T> supplier) {
      this.supplier = supplier;
   }

   public T get() {
      if (this.supplier != null) {
         this.value = this.supplier.get();
         this.supplier = null;
      }

      return this.value;
   }
}
