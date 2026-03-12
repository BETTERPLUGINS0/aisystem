package com.nisovin.shopkeepers.util.data.container;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.data.container.value.DataValue;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;

public class DataValueContainer extends AbstractDataContainer {
   private final DataValue dataValue;
   @Nullable
   private Object dataSourceCache = null;
   @Nullable
   private DataContainer dataContainerCache = null;

   @PolyNull
   public static DataContainer of(@PolyNull DataValue dataValue) {
      return dataValue == null ? null : new DataValueContainer(dataValue);
   }

   public static DataContainer ofNonNull(DataValue dataValue) {
      Validate.notNull(dataValue, (String)"dataValue is null");
      return (DataContainer)Unsafe.assertNonNull(of(dataValue));
   }

   protected DataValueContainer(DataValue dataValue) {
      Validate.notNull(dataValue, (String)"dataValue is null");
      this.dataValue = dataValue;
   }

   @Nullable
   private DataContainer getDataContainer(boolean createIfMissing) {
      Object dataSource = this.dataValue.get();
      if (dataSource != this.dataSourceCache) {
         this.dataSourceCache = dataSource;
         this.dataContainerCache = DataContainer.of(dataSource);
      }

      if (createIfMissing && this.dataContainerCache == null) {
         this.dataContainerCache = this.dataValue.createContainer();
         this.dataSourceCache = this.dataValue.get();
      }

      return this.dataContainerCache;
   }

   @Nullable
   protected final DataContainer getDataContainer() {
      return this.getDataContainer(false);
   }

   protected final DataContainer getDataContainerOrEmpty() {
      DataContainer dataContainer = this.getDataContainer(false);
      return dataContainer != null ? dataContainer : DataContainer.EMPTY;
   }

   protected final DataContainer getOrCreateDataContainer() {
      DataContainer dataContainer = this.getDataContainer(true);
      return (DataContainer)Unsafe.assertNonNull(dataContainer);
   }

   @Nullable
   public Object getOrDefault(String key, @Nullable Object defaultValue) {
      return this.getDataContainerOrEmpty().getOrDefault(key, defaultValue);
   }

   public void set(String key, @Nullable Object value) {
      this.getOrCreateDataContainer().set(key, value);
   }

   protected void internalSet(String key, Object value) {
      throw new IllegalStateException("This method is not expected to be called!");
   }

   public void remove(String key) {
      this.getOrCreateDataContainer().remove(key);
   }

   public void clear() {
      this.getOrCreateDataContainer().clear();
   }

   public int size() {
      return this.getDataContainerOrEmpty().size();
   }

   public Set<? extends String> getKeys() {
      return this.getDataContainerOrEmpty().getKeys();
   }

   public Map<? extends String, ?> getValues() {
      return this.getDataContainerOrEmpty().getValues();
   }

   public Map<String, Object> getValuesCopy() {
      return this.getDataContainerOrEmpty().getValuesCopy();
   }

   @Nullable
   public Object serialize() {
      return this.dataValue.get();
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getClass().getName());
      builder.append(" [dataValue=");
      builder.append(this.dataValue);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      return this.getDataContainerOrEmpty().hashCode();
   }

   public boolean equals(@Nullable Object obj) {
      return obj == this ? true : this.getDataContainerOrEmpty().equals(obj);
   }
}
