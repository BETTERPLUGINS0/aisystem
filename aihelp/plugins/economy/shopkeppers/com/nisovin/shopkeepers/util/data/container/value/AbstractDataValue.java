package com.nisovin.shopkeepers.util.data.container.value;

import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractDataValue implements DataValue {
   @Nullable
   private DataValue view = null;

   public boolean isOfType(Class<?> type) {
      Validate.notNull(type, (String)"type is null");
      Object value = this.get();
      return type.isInstance(value);
   }

   @Nullable
   public <T> T getOfTypeOrDefault(Class<T> type, @Nullable T defaultValue) {
      Validate.notNull(type, (String)"type is null");
      Object value = this.get();
      return type.isInstance(value) ? value : defaultValue;
   }

   @Nullable
   public String getStringOrDefault(@Nullable String defaultValue) {
      String value = ConversionUtils.toString(this.get());
      return value != null ? value : defaultValue;
   }

   public int getIntOrDefault(int defaultValue) {
      Integer value = ConversionUtils.toInteger(this.get());
      return value != null ? value : defaultValue;
   }

   public long getLongOrDefault(long defaultValue) {
      Long value = ConversionUtils.toLong(this.get());
      return value != null ? value : defaultValue;
   }

   public float getFloatOrDefault(float defaultValue) {
      Float value = ConversionUtils.toFloat(this.get());
      return value != null ? value : defaultValue;
   }

   public double getDoubleOrDefault(double defaultValue) {
      Double value = ConversionUtils.toDouble(this.get());
      return value != null ? value : defaultValue;
   }

   public boolean getBooleanOrDefault(boolean defaultValue) {
      Boolean value = ConversionUtils.toBoolean(this.get());
      return value != null ? value : defaultValue;
   }

   public void set(@Nullable Object value) {
      if (value == null) {
         this.clear();
      } else {
         Validate.isTrue(!(value instanceof DataContainer), "Cannot insert DataContainer!");
         Validate.isTrue(!(value instanceof DataValue), "Cannot insert DataValue!");
         this.internalSet(value);
      }

   }

   protected abstract void internalSet(Object var1);

   public DataValue asView() {
      if (this.view == null) {
         this.view = new UnmodifiableDataValue(this);
      }

      assert this.view != null;

      return this.view;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getClass().getName());
      builder.append(" [value=");
      builder.append(this.get());
      builder.append("]");
      return builder.toString();
   }

   public final int hashCode() {
      return Objects.hashCode(this.get());
   }

   public final boolean equals(@Nullable Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof DataValue)) {
         return false;
      } else {
         DataValue other = (DataValue)obj;
         return Objects.equals(this.get(), other.get());
      }
   }
}
