package com.nisovin.shopkeepers.util.java;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import java.util.function.Supplier;

public final class Lazy<T> {
   private final Supplier<? extends T> supplier;
   private boolean calculated = false;
   private T value = Unsafe.uncheckedNull();

   public Lazy(Supplier<? extends T> supplier) {
      Validate.notNull(supplier, (String)"supplier is null");
      this.supplier = supplier;
   }

   public T get() {
      if (!this.calculated) {
         this.value = this.supplier.get();
         this.calculated = true;
      }

      return Unsafe.cast(this.value);
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Lazy [calculated=");
      builder.append(this.calculated);
      builder.append(", value=");
      builder.append(this.value);
      builder.append("]");
      return builder.toString();
   }
}
