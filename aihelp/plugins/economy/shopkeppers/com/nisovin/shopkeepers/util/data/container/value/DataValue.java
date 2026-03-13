package com.nisovin.shopkeepers.util.data.container.value;

import com.nisovin.shopkeepers.util.data.container.DataContainer;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface DataValue {
   static DataValue create() {
      return of((Object)null);
   }

   static DataValue of(@Nullable Object value) {
      return new SimpleDataValue(value);
   }

   default boolean isPresent() {
      return this.get() != null;
   }

   @Nullable
   default Object get() {
      return this.getOrDefault((Object)null);
   }

   @Nullable
   Object getOrDefault(@Nullable Object var1);

   boolean isOfType(Class<?> var1);

   @Nullable
   default <T> T getOfType(Class<T> type) {
      return this.getOfTypeOrDefault(type, (Object)null);
   }

   @Nullable
   <T> T getOfTypeOrDefault(Class<T> var1, @Nullable T var2);

   default boolean isString() {
      Object value = this.get();
      return value instanceof String;
   }

   @Nullable
   default String getString() {
      return this.getStringOrDefault((String)null);
   }

   @Nullable
   String getStringOrDefault(@Nullable String var1);

   default boolean isNumber() {
      Object value = this.get();
      return value instanceof Number;
   }

   default int getInt() {
      return this.getIntOrDefault(0);
   }

   int getIntOrDefault(int var1);

   default long getLong() {
      return this.getLongOrDefault(0L);
   }

   long getLongOrDefault(long var1);

   default float getFloat() {
      return this.getFloatOrDefault(0.0F);
   }

   float getFloatOrDefault(float var1);

   default double getDouble() {
      return this.getDoubleOrDefault(0.0D);
   }

   double getDoubleOrDefault(double var1);

   default boolean isBoolean() {
      Object value = this.get();
      return value instanceof Boolean;
   }

   default boolean getBoolean() {
      return this.getBooleanOrDefault(false);
   }

   boolean getBooleanOrDefault(boolean var1);

   default boolean isList() {
      Object value = this.get();
      return value instanceof List;
   }

   @Nullable
   default List<?> getList() {
      return this.getListOrDefault((List)null);
   }

   @Nullable
   default List<?> getListOrDefault(@Nullable List<?> defaultValue) {
      Object value = this.get();
      return value instanceof List ? (List)value : defaultValue;
   }

   default boolean isContainer() {
      Object value = this.get();
      return DataContainer.isDataContainer(value);
   }

   @Nullable
   default DataContainer getContainer() {
      Object value = this.get();
      return DataContainer.of(value);
   }

   default DataContainer createContainer() {
      DataContainer container = DataContainer.create();
      this.set(container.serialize());
      return container;
   }

   void set(@Nullable Object var1);

   void clear();

   DataValue asView();
}
