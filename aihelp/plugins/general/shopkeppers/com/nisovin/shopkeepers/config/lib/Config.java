package com.nisovin.shopkeepers.config.lib;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.config.lib.annotation.Colored;
import com.nisovin.shopkeepers.config.lib.annotation.Uncolored;
import com.nisovin.shopkeepers.config.lib.annotation.WithDefaultValueType;
import com.nisovin.shopkeepers.config.lib.annotation.WithValueType;
import com.nisovin.shopkeepers.config.lib.annotation.WithValueTypeProvider;
import com.nisovin.shopkeepers.config.lib.setting.FieldSetting;
import com.nisovin.shopkeepers.config.lib.setting.Setting;
import com.nisovin.shopkeepers.config.lib.value.DefaultValueTypes;
import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.config.lib.value.ValueTypeProvider;
import com.nisovin.shopkeepers.config.lib.value.ValueTypeRegistry;
import com.nisovin.shopkeepers.config.lib.value.types.ColoredStringListValue;
import com.nisovin.shopkeepers.config.lib.value.types.ColoredStringValue;
import com.nisovin.shopkeepers.config.lib.value.types.StringListValue;
import com.nisovin.shopkeepers.config.lib.value.types.StringValue;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Config {
   @Nullable
   private ValueTypeRegistry customDefaultValueTypes = null;
   @Nullable
   private Map<String, FieldSetting<?>> settings = null;
   @Nullable
   private Collection<? extends FieldSetting<?>> settingsView = null;

   protected Config() {
   }

   public String getLogPrefix() {
      return "Config: ";
   }

   private void setupSettings() {
      Map<String, FieldSetting<?>> settings = this.settings;
      if (settings == null) {
         assert this.settingsView == null;

         Map<String, FieldSetting<?>> settings = new LinkedHashMap();
         this.settings = settings;
         this.settingsView = Collections.unmodifiableCollection(settings.values());
         Iterator var2 = CollectionUtils.toIterable(this.streamSettingFields()).iterator();

         while(var2.hasNext()) {
            Field field = (Field)var2.next();
            String configKey = this.getConfigKey(field);
            ValueType<?> valueType = this.setupValueType(field);

            assert valueType != null;

            FieldSetting<?> setting = new FieldSetting(this, field, configKey, valueType);
            settings.put(configKey, setting);
         }

         this.customDefaultValueTypes = null;
      }
   }

   private Stream<Field> streamSettingFields() {
      Class<?> configClass = this.getClass();
      Stream<Field> settings = this.streamSettingFields(configClass);
      Class parentClass = (Class)Unsafe.assertNonNull(configClass.getSuperclass());

      do {
         if (parentClass == Config.class) {
            return settings;
         }

         settings = Stream.concat(settings, this.streamSettingFields(parentClass));
         parentClass = configClass.getSuperclass();
      } while($assertionsDisabled || parentClass != null);

      throw new AssertionError();
   }

   private final Stream<Field> streamSettingFields(Class<?> configClass) {
      List<Field> fields = Arrays.asList(configClass.getDeclaredFields());
      return fields.stream().filter((field) -> {
         if (field.isSynthetic()) {
            return false;
         } else if (Modifier.isFinal(field.getModifiers())) {
            return false;
         } else {
            return this.isSetting(field);
         }
      });
   }

   protected boolean isSetting(Field field) {
      return Modifier.isPublic(field.getModifiers());
   }

   protected String getConfigKey(Field field) {
      return ConfigHelper.toConfigKey(field.getName());
   }

   protected <T> ValueType<T> setupValueType(Field field) {
      ValueType<T> valueType = this.getValueTypeByAnnotation(field);
      if (valueType != null) {
         return valueType;
      } else {
         valueType = this.getValueTypeByColoredAnnotation(field);
         if (valueType != null) {
            return valueType;
         } else {
            valueType = this.getValueTypeByUncoloredAnnotation(field);
            if (valueType != null) {
               return valueType;
            } else {
               valueType = this.getValueTypeByCustomDefaults(field);
               if (valueType != null) {
                  return valueType;
               } else {
                  Type fieldType = field.getGenericType();
                  valueType = DefaultValueTypes.get(fieldType);
                  if (valueType != null) {
                     return valueType;
                  } else {
                     String configKey = this.getConfigKey(field);
                     throw new IllegalStateException("Setting '" + configKey + "' is of unsupported type: " + fieldType.getTypeName());
                  }
               }
            }
         }
      }
   }

   @Nullable
   protected final <T> ValueType<T> getValueTypeByAnnotation(Field field) {
      WithValueType valueTypeAnnotation = (WithValueType)field.getAnnotation(WithValueType.class);
      if (valueTypeAnnotation != null) {
         Class<? extends ValueType<?>> valueTypeClass = valueTypeAnnotation.value();

         assert valueTypeClass != null;

         return instantiateValueType(valueTypeClass);
      } else {
         return null;
      }
   }

   private static ValueType<?> instantiateValueType(Class<? extends ValueType<?>> valueTypeClass) {
      assert valueTypeClass != null;

      try {
         return (ValueType)valueTypeClass.getDeclaredConstructor().newInstance();
      } catch (Exception var2) {
         throw new IllegalArgumentException("Could not instantiate ValueType: " + valueTypeClass.getName(), var2);
      }
   }

   @Nullable
   protected final <T> ValueType<T> getValueTypeByColoredAnnotation(Field field) {
      Colored coloredAnnotation = (Colored)field.getAnnotation(Colored.class);
      if (coloredAnnotation != null) {
         Type fieldType = field.getGenericType();
         if (fieldType == String.class) {
            return ColoredStringValue.INSTANCE;
         } else if (ColoredStringListValue.TYPE_PATTERN.matches(fieldType)) {
            return ColoredStringListValue.INSTANCE;
         } else {
            throw new IllegalArgumentException("The Colored annotation is not supported for settings of type " + fieldType.getTypeName());
         }
      } else {
         return null;
      }
   }

   @Nullable
   protected final <T> ValueType<T> getValueTypeByUncoloredAnnotation(Field field) {
      Uncolored uncoloredAnnotation = (Uncolored)field.getAnnotation(Uncolored.class);
      if (uncoloredAnnotation != null) {
         Type fieldType = field.getGenericType();
         if (fieldType == String.class) {
            return StringValue.INSTANCE;
         } else if (ColoredStringListValue.TYPE_PATTERN.matches(fieldType)) {
            return StringListValue.INSTANCE;
         } else {
            throw new IllegalArgumentException("The Uncolored annotation is not supported for settings of type " + fieldType.getTypeName());
         }
      } else {
         return null;
      }
   }

   @Nullable
   protected final <T> ValueType<T> getValueTypeByCustomDefaults(Field field) {
      this.setupCustomDefaultValueTypes();
      Type fieldType = field.getGenericType();
      return ((ValueTypeRegistry)Unsafe.assertNonNull(this.customDefaultValueTypes)).getValueType(fieldType);
   }

   private void setupCustomDefaultValueTypes() {
      if (this.customDefaultValueTypes == null) {
         this.customDefaultValueTypes = new ValueTypeRegistry();
         Class<?> configClass = this.getClass();
         this.setupCustomDefaultValueTypes(configClass);
         Class parentClass = (Class)Unsafe.assertNonNull(configClass.getSuperclass());

         do {
            if (parentClass == Config.class) {
               return;
            }

            this.setupCustomDefaultValueTypes(parentClass);
            parentClass = configClass.getSuperclass();
         } while($assertionsDisabled || parentClass != null);

         throw new AssertionError();
      }
   }

   private void setupCustomDefaultValueTypes(Class<?> configClass) {
      ValueTypeRegistry customDefaultValueTypes = (ValueTypeRegistry)Unsafe.assertNonNull(this.customDefaultValueTypes);
      WithDefaultValueType[] defaultValueTypeAnnotations = (WithDefaultValueType[])configClass.getAnnotationsByType(WithDefaultValueType.class);

      assert defaultValueTypeAnnotations != null;

      WithDefaultValueType[] var4 = defaultValueTypeAnnotations;
      int var5 = defaultValueTypeAnnotations.length;

      int var6;
      Class valueTypeProviderClass;
      for(var6 = 0; var6 < var5; ++var6) {
         WithDefaultValueType defaultValueTypeAnnotation = var4[var6];
         Class<?> fieldType = defaultValueTypeAnnotation.fieldType();
         if (!customDefaultValueTypes.hasCachedValueType(fieldType)) {
            valueTypeProviderClass = defaultValueTypeAnnotation.valueType();

            assert valueTypeProviderClass != null;

            ValueType<?> valueType = instantiateValueType(valueTypeProviderClass);

            assert valueType != null;

            customDefaultValueTypes.register((Type)fieldType, valueType);
         }
      }

      WithValueTypeProvider[] valueTypeProviderAnnotations = (WithValueTypeProvider[])configClass.getAnnotationsByType(WithValueTypeProvider.class);

      assert valueTypeProviderAnnotations != null;

      WithValueTypeProvider[] var12 = valueTypeProviderAnnotations;
      var6 = valueTypeProviderAnnotations.length;

      for(int var13 = 0; var13 < var6; ++var13) {
         WithValueTypeProvider valueTypeProviderAnnotation = var12[var13];
         valueTypeProviderClass = valueTypeProviderAnnotation.value();

         assert valueTypeProviderClass != null;

         ValueTypeProvider valueTypeProvider = instantiateValueTypeProvider(valueTypeProviderClass);

         assert valueTypeProvider != null;

         customDefaultValueTypes.register(valueTypeProvider);
      }

   }

   private static ValueTypeProvider instantiateValueTypeProvider(Class<? extends ValueTypeProvider> valueTypeProviderClass) {
      assert valueTypeProviderClass != null;

      try {
         return (ValueTypeProvider)valueTypeProviderClass.getDeclaredConstructor().newInstance();
      } catch (Exception var2) {
         throw new IllegalArgumentException("Could not instantiate ValueTypeProvider: " + valueTypeProviderClass.getName(), var2);
      }
   }

   public final Collection<? extends Setting<?>> getSettings() {
      this.setupSettings();
      return (Collection)Unsafe.assertNonNull(this.settingsView);
   }

   @Nullable
   protected final Setting<?> getSetting(String configKey) {
      this.setupSettings();
      return (Setting)((Map)Unsafe.assertNonNull(this.settings)).get(configKey);
   }

   public void save(DataContainer dataContainer) {
      Validate.notNull(dataContainer, (String)"dataContainer is null");
      Iterator var2 = this.getSettings().iterator();

      while(var2.hasNext()) {
         Setting<?> setting = (Setting)var2.next();
         this.saveSetting(dataContainer, setting);
      }

   }

   protected <T> void saveSetting(DataContainer dataContainer, Setting<T> setting) {
      assert setting.getConfig() == this;

      String configKey = setting.getConfigKey();
      ValueType<T> valueType = setting.getValueType();
      T value = setting.getValue();
      valueType.save(dataContainer, configKey, value);
   }

   public void load(ConfigData configData) throws ConfigLoadException {
      Validate.notNull(configData, (String)"configData is null");
      Iterator var2 = this.getSettings().iterator();

      while(var2.hasNext()) {
         Setting<?> setting = (Setting)var2.next();
         this.loadSetting(configData, setting);
      }

      this.validateSettings();
   }

   protected <T> void loadSetting(ConfigData configData, Setting<T> setting) throws ConfigLoadException {
      assert setting.getConfig() == this;

      String configKey = setting.getConfigKey();
      ValueType valueType = setting.getValueType();

      try {
         Object value;
         if (!configData.contains(configKey)) {
            value = this.getDefaultValue(configData, setting);
            this.onValueMissing(configData, setting, value);
         } else {
            value = valueType.load(configData, configKey);

            assert value != null;
         }

         if (value != null) {
            setting.setValue(value);
         }
      } catch (ValueLoadException var6) {
         this.onValueLoadException(configData, setting, var6);
      }

   }

   protected <T> void onValueMissing(ConfigData configData, Setting<T> setting, @Nullable T defaultValue) throws ConfigLoadException {
      String configKey = setting.getConfigKey();
      if (defaultValue == null) {
         Log.warning(this.msgMissingValue(configKey));
      } else {
         Log.warning(this.msgUsingDefaultForMissingValue(configKey, defaultValue));
      }

   }

   protected String msgMissingValue(String configKey) {
      String var10000 = this.getLogPrefix();
      return var10000 + "Missing config entry: " + configKey;
   }

   protected String msgUsingDefaultForMissingValue(String configKey, Object defaultValue) {
      String var10000 = this.getLogPrefix();
      return var10000 + "Using default value for missing config entry: " + configKey;
   }

   protected <T> void onValueLoadException(ConfigData configData, Setting<T> setting, ValueLoadException e) throws ConfigLoadException {
      String configKey = setting.getConfigKey();
      Log.warning(this.msgValueLoadException(configKey, e));
      Iterator var5 = e.getExtraMessages().iterator();

      while(var5.hasNext()) {
         String extraMessage = (String)var5.next();
         String var10000 = this.getLogPrefix();
         Log.warning(var10000 + extraMessage);
      }

      Log.debug((String)(this.getLogPrefix() + "Error details: "), (Throwable)e);
   }

   protected String msgValueLoadException(String configKey, ValueLoadException e) {
      return this.getLogPrefix() + "Could not load setting '" + configKey + "': " + e.getMessage();
   }

   protected void validateSettings() {
   }

   protected boolean hasDefaultValues(ConfigData configData) {
      return configData.getDefaults() != null;
   }

   @Nullable
   protected <T> T getDefaultValue(ConfigData configData, Setting<T> setting) {
      assert setting.getConfig() == this;

      DataContainer defaults = configData.getDefaults();
      if (defaults == null) {
         return null;
      } else {
         String configKey = setting.getConfigKey();
         ValueType valueType = setting.getValueType();

         try {
            return valueType.load(defaults, configKey);
         } catch (ValueLoadException var7) {
            Log.warning(this.msgDefaultValueLoadException(configKey, var7));
            Log.debug((String)(this.getLogPrefix() + "Error details: "), (Throwable)var7);
            return null;
         }
      }
   }

   protected String msgDefaultValueLoadException(String configKey, ValueLoadException e) {
      return this.getLogPrefix() + "Could not load default value for setting '" + configKey + "': " + e.getMessage();
   }

   protected boolean insertMissingDefaultValues(ConfigData configData) {
      Validate.notNull(configData, (String)"configData is null");
      if (!this.hasDefaultValues(configData)) {
         return false;
      } else {
         boolean configChanged = false;
         Iterator var3 = this.getSettings().iterator();

         while(var3.hasNext()) {
            Setting<?> setting = (Setting)var3.next();
            if (this.insertMissingDefaultValue(configData, setting)) {
               configChanged = true;
            }
         }

         return configChanged;
      }
   }

   protected <T> boolean insertMissingDefaultValue(ConfigData configData, Setting<T> setting) {
      assert setting.getConfig() == this;

      assert this.hasDefaultValues(configData);

      String configKey = setting.getConfigKey();
      if (configData.contains(configKey)) {
         return false;
      } else {
         Log.warning(this.msgInsertingDefault(configKey));
         T defaultValue = this.getDefaultValue(configData, setting);
         if (defaultValue == null) {
            Log.warning(this.msgMissingDefault(configKey));
            return false;
         } else {
            ValueType<T> valueType = setting.getValueType();
            valueType.save(configData, configKey, defaultValue);
            return true;
         }
      }
   }

   protected String msgInsertingDefault(String configKey) {
      String var10000 = this.getLogPrefix();
      return var10000 + "Inserting default value for missing config entry: " + configKey;
   }

   protected String msgMissingDefault(String configKey) {
      String var10000 = this.getLogPrefix();
      return var10000 + "Missing default value for setting: " + configKey;
   }

   protected static String c(String text) {
      return TextUtils.colorize(text);
   }

   protected static List<String> c(List<? extends String> texts) {
      return TextUtils.colorize(texts);
   }
}
