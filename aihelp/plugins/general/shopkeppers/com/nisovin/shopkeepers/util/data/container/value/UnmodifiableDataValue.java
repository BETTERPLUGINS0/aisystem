package com.nisovin.shopkeepers.util.data.container.value;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class UnmodifiableDataValue extends DelegateDataValue {
   public UnmodifiableDataValue(DataValue dataValue) {
      super(dataValue);
   }

   private UnsupportedOperationException unmodifiableException() {
      return new UnsupportedOperationException("This DataValue is unmodifiable!");
   }

   public void set(@Nullable Object value) {
      throw this.unmodifiableException();
   }

   public void clear() {
      throw this.unmodifiableException();
   }

   public DataValue asView() {
      return this;
   }
}
