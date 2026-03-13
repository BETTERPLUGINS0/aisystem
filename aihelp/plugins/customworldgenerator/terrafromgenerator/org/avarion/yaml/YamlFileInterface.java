package org.avarion.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.avarion.yaml.exceptions.DuplicateKey;
import org.avarion.yaml.exceptions.FinalAttribute;
import org.avarion.yaml.exceptions.YamlException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class YamlFileInterface {
   static final Object UNKNOWN = new Object();
   private static final YamlWrapper yaml = YamlWrapperFactory.create();
   private static final Set<String> TRUE_VALUES = new HashSet(Arrays.asList("yes", "y", "true", "1"));

   @Nullable
   private static Object getConvertedValue(@NotNull Field field, Object value, boolean isLenient) throws IOException {
      return getConvertedValue(field, field.getType(), value, isLenient);
   }

   @Nullable
   private static Object getConvertedValue(@Nullable Field field, @NotNull Class<?> expectedType, Object value, boolean isLenient) throws IOException {
      if (value == null) {
         return handleNullValue(expectedType, field);
      } else if (expectedType.isEnum() && value instanceof String) {
         return stringToEnum(expectedType, (String)value);
      } else if (value instanceof List) {
         return handleListValue(field, expectedType, (List)value, isLenient);
      } else if (expectedType.isInstance(value)) {
         return value;
      } else if (isBooleanType(expectedType)) {
         return convertToBoolean(value);
      } else if (Number.class.isAssignableFrom(value.getClass())) {
         return convertToNumber((Number)value, expectedType, isLenient);
      } else if (isCharacterType(expectedType)) {
         return convertToCharacter(String.valueOf(value), isLenient);
      } else {
         try {
            Constructor<?> constructor = expectedType.getConstructor(String.class);
            return constructor.newInstance(value.toString());
         } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException var5) {
            throw new IOException("'" + expectedType.getSimpleName() + "' doesn't accept a single String argument to create the object.");
         }
      }
   }

   @Nullable
   private static Object handleNullValue(@NotNull Class<?> expectedType, Field field) throws IOException {
      if (expectedType.isPrimitive()) {
         String message = "Cannot assign null to primitive type " + expectedType.getSimpleName();
         if (field != null) {
            message = message + " (field: " + field.getName() + ")";
         }

         throw new IOException(message);
      } else {
         return null;
      }
   }

   @NotNull
   private static Object handleListValue(@Nullable Field field, @NotNull Class<?> expectedType, List<?> list, boolean isLenient) throws IOException {
      if (!List.class.isAssignableFrom(expectedType)) {
         throw new IOException("Expected a List, but got " + expectedType.getSimpleName());
      } else {
         Class elementType;
         if (field != null) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
               elementType = (Class)((ParameterizedType)genericType).getActualTypeArguments()[0];
            } else {
               elementType = Object.class;
            }
         } else {
            elementType = Object.class;
         }

         List<Object> result = new ArrayList();
         Iterator var6 = list.iterator();

         while(var6.hasNext()) {
            Object item = var6.next();
            Object convertedValue = getConvertedValue((Field)null, elementType, item, isLenient);
            result.add(convertedValue);
         }

         return result;
      }
   }

   private static boolean isBooleanType(Class<?> type) {
      return type == Boolean.TYPE || type == Boolean.class;
   }

   @NotNull
   private static Boolean convertToBoolean(Object value) {
      if (value instanceof Boolean) {
         return (Boolean)value;
      } else {
         String strValue = value.toString().toLowerCase().trim();
         return TRUE_VALUES.contains(strValue);
      }
   }

   private static Object convertToNumber(Number numValue, Class<?> expectedType, boolean isLenient) throws IOException {
      if (expectedType != Integer.TYPE && expectedType != Integer.class) {
         if (expectedType != Double.TYPE && expectedType != Double.class) {
            if (expectedType != Float.TYPE && expectedType != Float.class) {
               if (expectedType != Long.TYPE && expectedType != Long.class) {
                  if (expectedType != Short.TYPE && expectedType != Short.class) {
                     if (expectedType != Byte.TYPE && expectedType != Byte.class) {
                        String var10002 = numValue.getClass().getSimpleName();
                        throw new IOException("Cannot convert " + var10002 + " to " + expectedType.getSimpleName());
                     } else {
                        return numValue.byteValue();
                     }
                  } else {
                     return numValue.shortValue();
                  }
               } else {
                  return numValue.longValue();
               }
            } else {
               return convertToFloat(numValue, isLenient);
            }
         } else {
            return numValue.doubleValue();
         }
      } else {
         return numValue.intValue();
      }
   }

   private static float convertToFloat(@NotNull Number numValue, boolean isLenient) throws IOException {
      double doubleValue = numValue.doubleValue();
      if (!isLenient && Math.abs(doubleValue - (double)((float)doubleValue)) >= 1.0E-9D) {
         throw new IOException("Double value " + doubleValue + " cannot be precisely represented as a float");
      } else {
         return numValue.floatValue();
      }
   }

   private static boolean isCharacterType(Class<?> type) {
      return type == Character.TYPE || type == Character.class;
   }

   @NotNull
   private static Character convertToCharacter(@NotNull String value, boolean isLenient) throws IOException {
      if (value.length() != 1 && !isLenient) {
         throw new IOException("Cannot convert String of length " + value.length() + " to Character");
      } else {
         return value.charAt(0);
      }
   }

   @NotNull
   private static <E extends Enum<E>> E stringToEnum(Class<E> enumClass, @NotNull String value) {
      return Enum.valueOf(enumClass, value.toUpperCase());
   }

   public <T extends YamlFileInterface> T load(@NotNull File file) throws IOException {
      if (!file.exists()) {
         this.save(file);
         return this;
      } else {
         FileInputStream inputStream = new FileInputStream(file);

         String content;
         try {
            content = new String(inputStream.readAllBytes());
         } catch (Throwable var10) {
            try {
               inputStream.close();
            } catch (Throwable var8) {
               var10.addSuppressed(var8);
            }

            throw var10;
         }

         inputStream.close();
         Map data = (Map)yaml.load(content);
         Class var4 = this.getClass();
         YamlFile yamlFileAnnotation = (YamlFile)var4.getAnnotation(YamlFile.class);
         boolean isLenientByDefault = yamlFileAnnotation != null && yamlFileAnnotation.lenient() == Leniency.LENIENT;

         try {
            this.loadFields(data, isLenientByDefault);
            return this;
         } catch (IllegalArgumentException | NullPointerException | FinalAttribute | IllegalAccessException var9) {
            throw new IOException(var9);
         }
      }
   }

   private void loadFields(Map<String, Object> data, boolean isLenientByDefault) throws FinalAttribute, IllegalAccessException, IOException {
      if (data == null) {
         data = new HashMap();
      }

      for(Class clazz = this.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
         Field[] var4 = clazz.getDeclaredFields();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            YamlKey keyAnnotation = (YamlKey)field.getAnnotation(YamlKey.class);
            YamlMap mapAnnotation = (YamlMap)field.getAnnotation(YamlMap.class);
            if (keyAnnotation != null && mapAnnotation != null) {
               throw new IllegalStateException("Field " + field.getName() + " cannot have both @YamlKey and @YamlMap annotations");
            }

            if (keyAnnotation != null && !keyAnnotation.value().trim().isEmpty()) {
               this.readYamlKeyField((Map)data, field, keyAnnotation, isLenientByDefault);
            } else if (mapAnnotation != null && !mapAnnotation.value().trim().isEmpty()) {
               this.readYamlMapField((Map)data, field, mapAnnotation);
            }
         }
      }

   }

   public <T extends YamlFileInterface> T load(@NotNull String file) throws IOException {
      return this.load(new File(file));
   }

   @Nullable
   private static Object getNestedValue(@NotNull Map<String, Object> map, @NotNull String[] keys) {
      return getNestedValue(map, (List)(new ArrayList(Arrays.asList(keys))));
   }

   @Nullable
   private static Object getNestedValue(@NotNull Map<String, Object> map, @NotNull List<String> keys) {
      String key = (String)keys.remove(0);
      if (!map.containsKey(key)) {
         return UNKNOWN;
      } else {
         Object tmp = map.get(key);
         if (keys.isEmpty()) {
            return tmp;
         } else {
            return !(tmp instanceof Map) ? UNKNOWN : getNestedValue((Map)tmp, keys);
         }
      }
   }

   @NotNull
   private String buildYamlContents() throws IllegalAccessException, FinalAttribute, DuplicateKey {
      StringBuilder result = new StringBuilder();
      Class<?> clazz = this.getClass();
      YamlFile yamlFileAnnotation = (YamlFile)clazz.getAnnotation(YamlFile.class);
      if (yamlFileAnnotation != null && !yamlFileAnnotation.header().trim().isEmpty()) {
         this.splitAndAppend(result, yamlFileAnnotation.header(), "", "# ");
         result.append("\n");
      }

      NestedMap nestedMap = new NestedMap();
      Field[] var5 = clazz.getDeclaredFields();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Field field = var5[var7];
         YamlKey keyAnnotation = (YamlKey)field.getAnnotation(YamlKey.class);
         YamlMap mapAnnotation = (YamlMap)field.getAnnotation(YamlMap.class);
         if (keyAnnotation != null && !keyAnnotation.value().trim().isEmpty()) {
            if (Modifier.isFinal(field.getModifiers())) {
               throw new FinalAttribute(field.getName());
            }

            Object value = field.get(this);
            YamlComment comment = (YamlComment)field.getAnnotation(YamlComment.class);
            nestedMap.put(keyAnnotation.value(), comment == null ? null : comment.value(), value);
         } else if (mapAnnotation != null && !mapAnnotation.value().trim().isEmpty()) {
            this.writeYamlMapField(nestedMap, this, field, mapAnnotation);
         }
      }

      this.convertNestedMapToYaml(result, nestedMap.getMap(), 0);
      return result.toString();
   }

   private void splitAndAppend(@NotNull StringBuilder yaml, @Nullable String data, @NotNull String indentStr, @NotNull String extra) {
      if (data != null) {
         String[] var5 = data.split("\\r?\\n");
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String line = var5[var7];
            yaml.append(indentStr).append(extra).append(line.replace("\\s*$", "")).append("\n");
         }

      }
   }

   private void convertNestedMapToYaml(StringBuilder yaml, @NotNull Map<String, Object> map, int indent) {
      StringBuilder tmp = new StringBuilder();

      for(int i = 0; i < indent; ++i) {
         tmp.append("  ");
      }

      String indentStr = tmp.toString();
      Iterator var6 = map.entrySet().iterator();

      while(true) {
         while(var6.hasNext()) {
            Entry<String, Object> entry = (Entry)var6.next();
            String key = (String)entry.getKey();
            Object value = entry.getValue();
            if (value instanceof NestedMap.NestedNode) {
               NestedMap.NestedNode node = (NestedMap.NestedNode)value;
               value = node.value;
               this.splitAndAppend(yaml, node.comment, indentStr, "# ");
            }

            yaml.append(indentStr).append(key).append(":");
            if (value instanceof Map) {
               yaml.append("\n");
               this.convertNestedMapToYaml(yaml, (Map)value, indent + 1);
            } else if (value instanceof List) {
               yaml.append("\n");
               Iterator var12 = ((List)value).iterator();

               while(var12.hasNext()) {
                  Object item = var12.next();
                  this.splitAndAppend(yaml, this.formatValue(item), indentStr + "  ", "- ");
               }
            } else {
               yaml.append(' ').append(this.formatValue(value)).append('\n');
            }
         }

         return;
      }
   }

   @NotNull
   private String formatValue(Object value) {
      String yamlContent = yaml.dump(value).trim();
      if (value instanceof Enum) {
         yamlContent = yamlContent.replaceAll("^!!\\S+\\s+", "");
      }

      return yamlContent;
   }

   public void save(@NotNull File file) throws IOException {
      File newFile = file.getAbsoluteFile();
      newFile.getParentFile().mkdirs();

      try {
         FileWriter writer = new FileWriter(newFile);

         try {
            writer.write(this.buildYamlContents());
         } catch (Throwable var7) {
            try {
               writer.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }

            throw var7;
         }

         writer.close();
      } catch (YamlException | IllegalAccessException var8) {
         throw new IOException(var8.getMessage());
      }
   }

   public void save(@NotNull String target) throws IOException {
      this.save(new File(target));
   }

   private void readYamlKeyField(Map<String, Object> data, @NotNull Field field, @NotNull YamlKey annotation, boolean isLenientByDefault) throws FinalAttribute, IllegalAccessException, IOException {
      if (Modifier.isFinal(field.getModifiers())) {
         throw new FinalAttribute(field.getName());
      } else {
         String key = annotation.value();
         boolean isLenient = isLenient(annotation.lenient(), isLenientByDefault);
         Object value = getNestedValue(data, key.split("\\."));
         if (value != UNKNOWN) {
            field.set(this, getConvertedValue(field, value, isLenient));
         }

      }
   }

   @Contract(
      pure = true
   )
   private static boolean isLenient(@NotNull Leniency leniency, boolean isLenientByDefault) {
      switch(leniency) {
      case LENIENT:
         return true;
      case UNDEFINED:
         return isLenientByDefault;
      default:
         return false;
      }
   }

   private void readYamlMapField(Map<String, Object> data, @NotNull Field field, @NotNull YamlMap annotation) throws IllegalAccessException, FinalAttribute {
      if (Modifier.isFinal(field.getModifiers())) {
         throw new FinalAttribute(field.getName());
      } else {
         String mapKey = annotation.value();
         Object mapValue = getNestedValue(data, mapKey.split("\\."));
         if (mapValue != UNKNOWN && mapValue != null) {
            try {
               Map<String, Object> fieldMap = (Map)mapValue;
               YamlMap.YamlMapProcessor<YamlFileInterface> processor = (YamlMap.YamlMapProcessor)annotation.processor().getDeclaredConstructor().newInstance();
               field.set(this, new LinkedHashMap());
               Iterator var8 = fieldMap.entrySet().iterator();

               while(var8.hasNext()) {
                  Entry<String, Object> entry = (Entry)var8.next();
                  if (entry.getValue() instanceof Map) {
                     processor.read(this, (String)entry.getKey(), (Map)entry.getValue());
                  }
               }

            } catch (NoSuchMethodException | InvocationTargetException | ClassCastException | InstantiationException var10) {
               throw new IllegalStateException("Failed to instantiate YamlMapProcessor", var10);
            }
         }
      }
   }

   private void writeYamlMapField(NestedMap nestedMap, Object obj, @NotNull Field field, @NotNull YamlMap annotation) throws IllegalAccessException, DuplicateKey {
      String mapKey = annotation.value();
      Object fieldValue = field.get(obj);
      if (fieldValue instanceof Map) {
         try {
            YamlMap.YamlMapProcessor<YamlFileInterface> processor = (YamlMap.YamlMapProcessor)annotation.processor().getDeclaredConstructor().newInstance();
            Map<String, Object> processedMap = new HashMap();
            Iterator var9 = ((Map)fieldValue).entrySet().iterator();

            while(var9.hasNext()) {
               Entry<?, ?> entry = (Entry)var9.next();
               String key = entry.getKey().toString();
               Map<String, Object> value = processor.write((YamlFileInterface)obj, key, entry.getValue());
               processedMap.put(key, value);
            }

            nestedMap.put(mapKey, (String)null, processedMap);
         } catch (NoSuchMethodException | InvocationTargetException | InstantiationException var13) {
            throw new IllegalStateException("Failed to instantiate YamlMapProcessor", var13);
         }
      }

   }
}
