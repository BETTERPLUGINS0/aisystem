package com.nisovin.shopkeepers.config.lib.value;

import com.nisovin.shopkeepers.util.data.container.DataContainer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class ValueType<T> {
   public T load(DataContainer dataContainer, String key) throws ValueLoadException {
      Object configValue = dataContainer.get(key);
      return this.load(configValue);
   }

   public T load(DataContainer dataContainer, String key, T defaultValue) {
      Object value = null;

      try {
         value = this.load(dataContainer, key);
      } catch (ValueLoadException var6) {
      }

      return value == null ? defaultValue : value;
   }

   @Nullable
   public abstract T load(@Nullable Object var1) throws ValueLoadException;

   public void save(DataContainer dataContainer, String key, @Nullable T value) {
      Object configValue = this.save(value);
      dataContainer.set(key, configValue);
   }

   @Nullable
   public abstract Object save(@Nullable T var1);

   public String format(@Nullable T value) {
      return String.valueOf(value);
   }

   @NonNull
   public T parse(String input) throws ValueParseException {
      throw new UnsupportedOperationException("This ValueType does not support the parsing of values.");
   }
}
