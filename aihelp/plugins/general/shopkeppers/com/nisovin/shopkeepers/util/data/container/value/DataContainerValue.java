package com.nisovin.shopkeepers.util.data.container.value;

import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public class DataContainerValue extends AbstractDataValue {
   protected final DataContainer dataContainer;
   protected final String dataKey;

   public DataContainerValue(DataContainer dataContainer, String dataKey) {
      Validate.notNull(dataContainer, (String)"dataContainer is null");
      Validate.notEmpty(dataKey, "dataKey is empty");
      this.dataContainer = dataContainer;
      this.dataKey = dataKey;
   }

   @Nullable
   public Object getOrDefault(@Nullable Object defaultValue) {
      return this.dataContainer.getOrDefault(this.dataKey, defaultValue);
   }

   protected void internalSet(Object value) {
      this.dataContainer.set(this.dataKey, value);
   }

   public void clear() {
      this.dataContainer.remove(this.dataKey);
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("DataContainerValue [dataContainer=");
      builder.append(this.dataContainer);
      builder.append(", dataKey=");
      builder.append(this.dataKey);
      builder.append(", value=");
      builder.append(this.get());
      builder.append("]");
      return builder.toString();
   }
}
