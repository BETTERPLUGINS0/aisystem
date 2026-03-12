package com.nisovin.shopkeepers.util.data.container;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class UnmodifiableDataContainer extends DelegateDataContainer {
   public UnmodifiableDataContainer(DataContainer dataContainer) {
      super(dataContainer);
   }

   private UnsupportedOperationException unmodifiableException() {
      return new UnsupportedOperationException("This DataContainer is unmodifiable!");
   }

   public void set(String key, @Nullable Object value) {
      throw this.unmodifiableException();
   }

   public void clear() {
      throw this.unmodifiableException();
   }

   public DataContainer asView() {
      return this;
   }
}
