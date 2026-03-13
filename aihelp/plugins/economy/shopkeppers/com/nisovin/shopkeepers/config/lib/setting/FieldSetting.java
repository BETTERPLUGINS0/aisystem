package com.nisovin.shopkeepers.config.lib.setting;

import com.nisovin.shopkeepers.config.lib.Config;
import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.util.java.ClassUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.lang.reflect.Field;
import org.checkerframework.checker.nullness.qual.Nullable;

public class FieldSetting<T> implements Setting<T> {
   private final Config config;
   private final Field field;
   private final String configKey;
   private final ValueType<T> valueType;

   public FieldSetting(Config config, Field field, String configKey, ValueType<T> valueType) {
      Validate.notNull(config, (String)"config is null");
      Validate.notNull(field, (String)"field is null");
      Validate.notEmpty(configKey, "configKey is null or empty");
      Validate.notNull(valueType, (String)"valueType is null");
      this.config = config;
      this.field = field;
      this.configKey = configKey;
      this.valueType = valueType;
      field.setAccessible(true);
   }

   public Config getConfig() {
      return this.config;
   }

   public Field getField() {
      return this.field;
   }

   public String getConfigKey() {
      return this.configKey;
   }

   public ValueType<T> getValueType() {
      return this.valueType;
   }

   public T getValue() {
      try {
         return this.field.get(this.config);
      } catch (IllegalAccessException | IllegalArgumentException var2) {
         throw new RuntimeException("Could not get the value from the setting's field!", var2);
      }
   }

   public void setValue(@Nullable T value) throws ValueLoadException {
      Class<?> fieldType = this.field.getType();
      if (!ClassUtils.isAssignableFrom(fieldType, value)) {
         String var10002 = value != null ? value.getClass().getName() : "null";
         throw new ValueLoadException("Value is of wrong type: Got " + var10002 + ", expected " + fieldType.getName());
      } else {
         try {
            this.field.set(this.config, value);
         } catch (Exception var4) {
            throw new ValueLoadException("Could not set the value of the setting's field!", var4);
         }
      }
   }
}
