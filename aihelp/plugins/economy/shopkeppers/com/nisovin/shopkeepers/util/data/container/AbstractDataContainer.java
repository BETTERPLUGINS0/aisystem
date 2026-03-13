package com.nisovin.shopkeepers.util.data.container;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.data.container.value.DataContainerValue;
import com.nisovin.shopkeepers.util.data.container.value.DataValue;
import com.nisovin.shopkeepers.util.data.serialization.DataLoader;
import com.nisovin.shopkeepers.util.data.serialization.DataSaver;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.MissingDataException;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractDataContainer implements DataContainer {
   @Nullable
   private DataContainer view = null;

   public DataValue getDataValue(String key) {
      return new DataContainerValue(this, key);
   }

   public boolean isOfType(String key, Class<?> type) {
      Validate.notNull(type, (String)"type is null");
      Object value = this.get(key);
      return type.isInstance(value);
   }

   @Nullable
   public <T> T getOfTypeOrDefault(String key, Class<T> type, @Nullable T defaultValue) {
      Validate.notNull(type, (String)"type is null");
      Object value = this.get(key);
      return type.isInstance(value) ? value : defaultValue;
   }

   @Nullable
   public String getStringOrDefault(String key, @Nullable String defaultValue) {
      String value = ConversionUtils.toString(this.get(key));
      return value != null ? value : defaultValue;
   }

   public int getIntOrDefault(String key, int defaultValue) {
      Integer value = ConversionUtils.toInteger(this.get(key));
      return value != null ? value : defaultValue;
   }

   public long getLongOrDefault(String key, long defaultValue) {
      Long value = ConversionUtils.toLong(this.get(key));
      return value != null ? value : defaultValue;
   }

   public float getFloatOrDefault(String key, float defaultValue) {
      Float value = ConversionUtils.toFloat(this.get(key));
      return value != null ? value : defaultValue;
   }

   public double getDoubleOrDefault(String key, double defaultValue) {
      Double value = ConversionUtils.toDouble(this.get(key));
      return value != null ? value : defaultValue;
   }

   public boolean getBooleanOrDefault(String key, boolean defaultValue) {
      Boolean value = ConversionUtils.toBoolean(this.get(key));
      return value != null ? value : defaultValue;
   }

   public void set(String key, @Nullable Object value) {
      Validate.notEmpty(key, "key is empty");
      if (value == null) {
         this.remove(key);
      } else {
         Validate.isTrue(!(value instanceof DataContainer), "Cannot insert DataContainer!");
         Validate.isTrue(!(value instanceof DataValue), "Cannot insert DataValue!");
         this.internalSet(key, value);
      }

   }

   protected abstract void internalSet(String var1, Object var2);

   public void setAll(Map<?, ?> values) {
      Validate.notNull(values, (String)"values is null");
      values.forEach((key, value) -> {
         String stringKey = ConversionUtils.toString(key);
         Validate.notEmpty(stringKey, "key is empty");
         this.set((String)Unsafe.assertNonNull(stringKey), value);
      });
   }

   public <T> T get(DataLoader<? extends T> loader) throws InvalidDataException {
      Validate.notNull(loader, (String)"loader is null");
      return loader.load(this);
   }

   @Nullable
   public <T> T getOrNullIfMissing(DataLoader<? extends T> loader) throws InvalidDataException {
      try {
         return this.get(loader);
      } catch (MissingDataException var3) {
         return null;
      }
   }

   @Nullable
   public <T> T getOrNull(DataLoader<? extends T> loader) {
      Validate.notNull(loader, (String)"loader is null");

      try {
         return this.get(loader);
      } catch (InvalidDataException var3) {
         return null;
      }
   }

   public <T> void set(DataSaver<? super T> saver, @Nullable T value) {
      Validate.notNull(saver, (String)"saver is null");
      saver.save(this, value);
   }

   public DataContainer asView() {
      if (this.view == null) {
         this.view = new UnmodifiableDataContainer(this);
      }

      assert this.view != null;

      return this.view;
   }
}
