package com.nisovin.shopkeepers.util.data.container.value;

import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public class DelegateDataValue extends AbstractDataValue {
   protected final DataValue dataValue;

   public DelegateDataValue(DataValue dataValue) {
      Validate.notNull(dataValue, (String)"dataValue is null");
      this.dataValue = dataValue;
   }

   @Nullable
   public Object getOrDefault(@Nullable Object defaultValue) {
      return this.dataValue.getOrDefault(defaultValue);
   }

   public void set(@Nullable Object value) {
      this.dataValue.set(value);
   }

   protected void internalSet(Object value) {
      throw new IllegalStateException("This method is not expected to be called!");
   }

   public void clear() {
      this.dataValue.clear();
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getClass().getName());
      builder.append(" [dataValue=");
      builder.append(this.dataValue);
      builder.append("]");
      return builder.toString();
   }
}
