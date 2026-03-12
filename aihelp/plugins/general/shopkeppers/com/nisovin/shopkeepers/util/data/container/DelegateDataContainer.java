package com.nisovin.shopkeepers.util.data.container;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.Nullable;

public class DelegateDataContainer extends AbstractDataContainer {
   protected final DataContainer dataContainer;

   public DelegateDataContainer(DataContainer dataContainer) {
      Validate.notNull(dataContainer, (String)"dataContainer is null");
      this.dataContainer = dataContainer;
   }

   @Nullable
   public Object getOrDefault(String key, @Nullable Object defaultValue) {
      return this.dataContainer.getOrDefault(key, defaultValue);
   }

   public void set(String key, @Nullable Object value) {
      this.dataContainer.set(key, value);
   }

   protected void internalSet(String key, Object value) {
      throw new IllegalStateException("This method is not expected to be called!");
   }

   public void remove(String key) {
      this.dataContainer.remove(key);
   }

   public void clear() {
      this.dataContainer.clear();
   }

   public int size() {
      return this.dataContainer.size();
   }

   public Set<? extends String> getKeys() {
      return this.dataContainer.getKeys();
   }

   public Map<? extends String, ?> getValues() {
      return this.dataContainer.getValues();
   }

   public Map<String, Object> getValuesCopy() {
      return this.dataContainer.getValuesCopy();
   }

   @Nullable
   public Object serialize() {
      return this.dataContainer.serialize();
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getClass().getName());
      builder.append(" [dataContainer=");
      builder.append(this.dataContainer);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      return this.dataContainer.hashCode();
   }

   public boolean equals(@Nullable Object obj) {
      return this.dataContainer.equals(obj);
   }
}
