package com.nisovin.shopkeepers.util.data.container.value;

import org.checkerframework.checker.nullness.qual.Nullable;

public class SimpleDataValue extends AbstractDataValue {
   @Nullable
   private Object value;

   public SimpleDataValue() {
      this((Object)null);
   }

   public SimpleDataValue(@Nullable Object value) {
      this.value = value;
   }

   @Nullable
   public Object getOrDefault(@Nullable Object defaultValue) {
      return this.value != null ? this.value : defaultValue;
   }

   protected void internalSet(Object value) {
      this.value = value;
   }

   public void clear() {
      this.value = null;
   }
}
