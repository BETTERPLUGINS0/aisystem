package github.nighter.smartspawner.libs.hikari.util;

import github.nighter.smartspawner.libs.hikari.HikariConfig;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PropertyElf {
   private static final char ESCAPE_CHAR = '\\';
   private static final char SEPARATOR_CHAR = ',';
   private static final Pattern DURATION_PATTERN = Pattern.compile("^(?<number>\\d+)(?<unit>ms|s|m|h|d)$");

   private PropertyElf() {
   }

   public static void setTargetFromProperties(Object target, Properties properties) {
      if (target != null && properties != null) {
         List<Method> methods = Arrays.asList(target.getClass().getMethods());
         properties.forEach((key, value) -> {
            String keyName = key.toString();
            if (target instanceof HikariConfig && keyName.startsWith("dataSource.")) {
               ((HikariConfig)target).addDataSourceProperty(keyName.substring("dataSource.".length()), value);
            } else {
               setProperty(target, keyName, value, methods);
            }

         });
      }
   }

   public static Set<String> getPropertyNames(Class<?> targetClass) {
      HashSet<String> set = new HashSet();
      Method[] var2 = targetClass.getMethods();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method method = var2[var4];
         String name = propertyNameFromGetterName(method.getName());

         try {
            if (method.getParameterTypes().length == 0 && name != null) {
               targetClass.getMethod("set" + capitalizedPropertyName(name), method.getReturnType());
               set.add(name);
            }
         } catch (Exception var8) {
         }
      }

      return set;
   }

   public static Object getProperty(String propName, Object target) {
      try {
         String capitalized = "get" + capitalizedPropertyName(propName);
         Method method = target.getClass().getMethod(capitalized);
         return method.invoke(target);
      } catch (Exception var6) {
         try {
            String capitalized = "is" + capitalizedPropertyName(propName);
            Method method = target.getClass().getMethod(capitalized);
            return method.invoke(target);
         } catch (Exception var5) {
            return null;
         }
      }
   }

   public static Properties copyProperties(Properties props) {
      Properties copy = new Properties();
      props.forEach((key, value) -> {
         copy.setProperty(key.toString(), value.toString());
      });
      return copy;
   }

   private static String propertyNameFromGetterName(String methodName) {
      String name = null;
      if (methodName.startsWith("get") && methodName.length() > 3) {
         name = methodName.substring(3);
      } else if (methodName.startsWith("is") && methodName.length() > 2) {
         name = methodName.substring(2);
      }

      if (name != null) {
         char var10000 = Character.toLowerCase(name.charAt(0));
         return var10000 + name.substring(1);
      } else {
         return null;
      }
   }

   private static void setProperty(Object target, String propName, Object propValue, List<Method> methods) {
      Logger logger = LoggerFactory.getLogger(PropertyElf.class);
      String var10000 = propName.substring(0, 1).toUpperCase(Locale.ENGLISH);
      String methodName = "set" + var10000 + propName.substring(1);
      Method writeMethod = (Method)methods.stream().filter((m) -> {
         return m.getName().equals(methodName) && m.getParameterCount() == 1;
      }).findFirst().orElse((Object)null);
      if (writeMethod == null) {
         String methodName2 = "set" + propName.toUpperCase(Locale.ENGLISH);
         writeMethod = (Method)methods.stream().filter((m) -> {
            return m.getName().equals(methodName2) && m.getParameterCount() == 1;
         }).findFirst().orElse((Object)null);
      }

      if (writeMethod == null) {
         logger.error("Property {} does not exist on target {}", propName, target.getClass());
         throw new RuntimeException(String.format("Property %s does not exist on target %s", propName, target.getClass()));
      } else {
         try {
            Class<?> paramClass = writeMethod.getParameterTypes()[0];
            String value = propValue.toString();
            if (paramClass == Integer.TYPE) {
               writeMethod.invoke(target, Integer.parseInt(propValue.toString()));
            } else if (paramClass == Long.TYPE) {
               writeMethod.invoke(target, parseDuration(value).map(Duration::toMillis).orElseGet(() -> {
                  return Long.parseLong(value);
               }));
            } else if (paramClass == Short.TYPE) {
               writeMethod.invoke(target, Short.parseShort(value));
            } else if (paramClass != Boolean.TYPE && paramClass != Boolean.class) {
               if (paramClass.isArray() && Character.TYPE.isAssignableFrom(paramClass.getComponentType())) {
                  writeMethod.invoke(target, value.toCharArray());
               } else if (paramClass.isArray() && Integer.TYPE.isAssignableFrom(paramClass.getComponentType())) {
                  writeMethod.invoke(target, parseIntArray(value));
               } else if (paramClass.isArray() && String.class.isAssignableFrom(paramClass.getComponentType())) {
                  writeMethod.invoke(target, parseStringArray(value));
               } else if (paramClass == String.class) {
                  writeMethod.invoke(target, value);
               } else {
                  try {
                     logger.debug("Try to create a new instance of \"{}\"", propValue);
                     writeMethod.invoke(target, Class.forName(propValue.toString()).getDeclaredConstructor().newInstance());
                  } catch (ClassNotFoundException | InstantiationException var10) {
                     logger.debug("Class \"{}\" not found or could not instantiate it (Default constructor)", propValue);
                     writeMethod.invoke(target, propValue);
                  }
               }
            } else {
               writeMethod.invoke(target, Boolean.parseBoolean(value));
            }

         } catch (Exception var11) {
            logger.error("Failed to set property {} on target {}", new Object[]{propName, target.getClass(), var11});
            throw new RuntimeException(var11);
         }
      }
   }

   private static String capitalizedPropertyName(String propertyName) {
      String var10000 = propertyName.substring(0, 1).toUpperCase(Locale.ENGLISH);
      return var10000 + propertyName.substring(1);
   }

   private static int[] parseIntArray(String value) {
      if (value != null && !value.isEmpty()) {
         String[] split = value.split(",");
         int[] intArray = new int[split.length];

         for(int i = 0; i < split.length; ++i) {
            intArray[i] = Integer.parseInt(split[i]);
         }

         return intArray;
      } else {
         return new int[0];
      }
   }

   private static String[] parseStringArray(String value) {
      if (value != null && !value.isEmpty()) {
         ArrayList<String> resultList = new ArrayList();
         boolean inEscape = false;
         StringBuilder currentField = new StringBuilder();
         char[] var4 = value.toCharArray();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            char c = var4[var6];
            if (inEscape) {
               currentField.append(c);
               inEscape = false;
            } else if (c == '\\') {
               inEscape = true;
            } else if (c == ',') {
               resultList.add(currentField.toString());
               currentField.setLength(0);
            } else {
               currentField.append(c);
            }
         }

         if (inEscape) {
            throw new IllegalArgumentException(String.format("Unterminated escape sequence in property value: %s", value));
         } else {
            resultList.add(currentField.toString());
            return (String[])resultList.toArray(new String[0]);
         }
      } else {
         return new String[0];
      }
   }

   private static Optional<Duration> parseDuration(String value) {
      Matcher matcher = DURATION_PATTERN.matcher(value);
      if (matcher.matches()) {
         long number = Long.parseLong(matcher.group("number"));
         String unit = matcher.group("unit");
         byte var6 = -1;
         switch(unit.hashCode()) {
         case 100:
            if (unit.equals("d")) {
               var6 = 4;
            }
            break;
         case 104:
            if (unit.equals("h")) {
               var6 = 3;
            }
            break;
         case 109:
            if (unit.equals("m")) {
               var6 = 2;
            }
            break;
         case 115:
            if (unit.equals("s")) {
               var6 = 1;
            }
            break;
         case 3494:
            if (unit.equals("ms")) {
               var6 = 0;
            }
         }

         switch(var6) {
         case 0:
            return Optional.of(Duration.ofMillis(number));
         case 1:
            return Optional.of(Duration.ofSeconds(number));
         case 2:
            return Optional.of(Duration.ofMinutes(number));
         case 3:
            return Optional.of(Duration.ofHours(number));
         case 4:
            return Optional.of(Duration.ofDays(number));
         default:
            throw new IllegalStateException(String.format("Could not match unit, got %s (from given value %s)", unit, value));
         }
      } else {
         return Optional.empty();
      }
   }
}
