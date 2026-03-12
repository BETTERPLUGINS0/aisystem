package com.nisovin.shopkeepers.util.data.container;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.data.container.value.DataValue;
import com.nisovin.shopkeepers.util.data.serialization.DataLoader;
import com.nisovin.shopkeepers.util.data.serialization.DataSaver;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface DataContainer {
   DataContainer EMPTY = create().asView();

   static DataContainer create() {
      return new MapBasedDataContainer();
   }

   @Nullable
   static DataContainer of(@Nullable Object dataSource) {
      if (dataSource == null) {
         return null;
      } else if (dataSource instanceof ConfigurationSection) {
         ConfigurationSection configSection = (ConfigurationSection)dataSource;
         return new ConfigBasedDataContainer(configSection);
      } else if (dataSource instanceof Map) {
         Map<String, Object> map = (Map)Unsafe.castNonNull(dataSource);
         return new MapBasedDataContainer(map);
      } else {
         return dataSource instanceof DataContainer ? (DataContainer)dataSource : null;
      }
   }

   static DataContainer ofNonNull(Object dataSource) {
      DataContainer container = of(dataSource);
      Validate.notNull(container, (String)"dataSource is not a valid DataContainer");
      return (DataContainer)Unsafe.assertNonNull(container);
   }

   static boolean isDataContainer(@Nullable Object dataSource) {
      if (dataSource == null) {
         return false;
      } else {
         return dataSource instanceof ConfigurationSection || dataSource instanceof Map || dataSource instanceof DataContainer;
      }
   }

   default boolean contains(String key) {
      return this.get(key) != null;
   }

   DataValue getDataValue(String var1);

   @Nullable
   default Object get(String key) {
      return this.getOrDefault(key, (Object)null);
   }

   @Nullable
   Object getOrDefault(String var1, @Nullable Object var2);

   boolean isOfType(String var1, Class<?> var2);

   @Nullable
   default <T> T getOfType(String key, Class<T> type) {
      return this.getOfTypeOrDefault(key, type, (Object)null);
   }

   @Nullable
   <T> T getOfTypeOrDefault(String var1, Class<T> var2, @Nullable T var3);

   default boolean isString(String key) {
      Object value = this.get(key);
      return value instanceof String;
   }

   @Nullable
   default String getString(String key) {
      return this.getStringOrDefault(key, (String)null);
   }

   @Nullable
   String getStringOrDefault(String var1, @Nullable String var2);

   default boolean isNumber(String key) {
      Object value = this.get(key);
      return value instanceof Number;
   }

   default int getInt(String key) {
      return this.getIntOrDefault(key, 0);
   }

   int getIntOrDefault(String var1, int var2);

   default long getLong(String key) {
      return this.getLongOrDefault(key, 0L);
   }

   long getLongOrDefault(String var1, long var2);

   default float getFloat(String key) {
      return this.getFloatOrDefault(key, 0.0F);
   }

   float getFloatOrDefault(String var1, float var2);

   default double getDouble(String key) {
      return this.getDoubleOrDefault(key, 0.0D);
   }

   double getDoubleOrDefault(String var1, double var2);

   default boolean isBoolean(String key) {
      Object value = this.get(key);
      return value instanceof Boolean;
   }

   default boolean getBoolean(String key) {
      return this.getBooleanOrDefault(key, false);
   }

   boolean getBooleanOrDefault(String var1, boolean var2);

   default boolean isList(String key) {
      Object value = this.get(key);
      return value instanceof List;
   }

   @Nullable
   default List<?> getList(String key) {
      return this.getListOrDefault(key, (List)null);
   }

   @Nullable
   default List<?> getListOrDefault(String key, @Nullable List<?> defaultValue) {
      Object value = this.get(key);
      return value instanceof List ? (List)value : defaultValue;
   }

   default boolean isContainer(String key) {
      Object value = this.get(key);
      return isDataContainer(value);
   }

   @Nullable
   default DataContainer getContainer(String key) {
      Object value = this.get(key);
      return of(value);
   }

   default DataContainer getDataValueContainer(String key) {
      return DataValueContainer.ofNonNull(this.getDataValue(key));
   }

   default DataContainer createContainer(String key) {
      DataContainer container = create();
      this.set(key, container.serialize());
      return container;
   }

   void set(String var1, @Nullable Object var2);

   void setAll(Map<?, ?> var1);

   void remove(String var1);

   void clear();

   <T> T get(DataLoader<? extends T> var1) throws InvalidDataException;

   @Nullable
   <T> T getOrNullIfMissing(DataLoader<? extends T> var1) throws InvalidDataException;

   @Nullable
   <T> T getOrNull(DataLoader<? extends T> var1);

   <T> void set(DataSaver<? super T> var1, @Nullable T var2);

   int size();

   default boolean isEmpty() {
      return this.size() == 0;
   }

   Set<? extends String> getKeys();

   Map<? extends String, ?> getValues();

   Map<String, Object> getValuesCopy();

   DataContainer asView();

   @Nullable
   Object serialize();
}
