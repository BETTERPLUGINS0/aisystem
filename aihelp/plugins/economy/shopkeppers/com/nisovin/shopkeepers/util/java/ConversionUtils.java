package com.nisovin.shopkeepers.util.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ConversionUtils {
   public static final Map<? extends String, ? extends Boolean> BOOLEAN_VALUES;
   public static final Map<? extends String, ? extends Trilean> TRILEAN_VALUES;

   @Nullable
   public static Integer parseInt(@Nullable String string) {
      if (string == null) {
         return null;
      } else {
         try {
            return Integer.parseInt(string);
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   @Nullable
   public static Long parseLong(@Nullable String string) {
      if (string == null) {
         return null;
      } else {
         try {
            return Long.parseLong(string);
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   @Nullable
   public static Double parseDouble(@Nullable String string) {
      if (string == null) {
         return null;
      } else {
         try {
            return Double.parseDouble(string);
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   @Nullable
   public static Float parseFloat(@Nullable String string) {
      if (string == null) {
         return null;
      } else {
         try {
            return Float.parseFloat(string);
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   @Nullable
   public static Boolean parseBoolean(@Nullable String string) {
      return string == null ? null : (Boolean)BOOLEAN_VALUES.get(string.toLowerCase(Locale.ROOT));
   }

   @Nullable
   public static Trilean parseTrilean(@Nullable String string) {
      return string == null ? null : (Trilean)TRILEAN_VALUES.get(string.toLowerCase(Locale.ROOT));
   }

   @Nullable
   public static UUID parseUUID(@Nullable String string) {
      if (string == null) {
         return null;
      } else {
         String uuidString = string;
         if (string.length() == 32) {
            String var10000 = string.substring(0, 8);
            uuidString = var10000 + "-" + string.substring(8, 12) + "-" + string.substring(12, 16) + "-" + string.substring(16, 20) + "-" + string.substring(20, 32);
         }

         if (uuidString.length() == 36) {
            try {
               return UUID.fromString(uuidString);
            } catch (IllegalArgumentException var3) {
            }
         }

         return null;
      }
   }

   public static <E extends Enum<E>> E parseEnum(Class<E> enumType, String enumName) {
      Validate.notNull(enumType, (String)"enumType is null");
      if (enumName != null && !enumName.isEmpty()) {
         E enumValue = EnumUtils.valueOf(enumType, enumName);
         if (enumValue != null) {
            return enumValue;
         } else {
            String normalizedEnumName = EnumUtils.normalizeEnumName(enumName);
            return EnumUtils.valueOf(enumType, normalizedEnumName);
         }
      } else {
         return null;
      }
   }

   public static List<Integer> parseIntList(Collection<? extends String> strings) {
      Validate.notNull(strings, (String)"strings is null");
      List<Integer> result = new ArrayList(strings.size());
      strings.forEach((string) -> {
         Integer value = parseInt(string);
         if (value != null) {
            result.add(value);
         }

      });
      return result;
   }

   public static List<Long> parseLongList(Collection<? extends String> strings) {
      Validate.notNull(strings, (String)"strings is null");
      List<Long> result = new ArrayList(strings.size());
      strings.forEach((string) -> {
         Long value = parseLong(string);
         if (value != null) {
            result.add(value);
         }

      });
      return result;
   }

   public static List<Double> parseDoubleList(Collection<? extends String> strings) {
      Validate.notNull(strings, (String)"strings is null");
      List<Double> result = new ArrayList(strings.size());
      strings.forEach((string) -> {
         Double value = parseDouble(string);
         if (value != null) {
            result.add(value);
         }

      });
      return result;
   }

   public static List<Float> parseFloatList(Collection<? extends String> strings) {
      Validate.notNull(strings, (String)"strings is null");
      List<Float> result = new ArrayList(strings.size());
      strings.forEach((string) -> {
         Float value = parseFloat(string);
         if (value != null) {
            result.add(value);
         }

      });
      return result;
   }

   public static <E extends Enum<E>> List<E> parseEnumList(Class<E> enumType, Collection<? extends String> strings) {
      Validate.notNull(enumType, (String)"enumType is null");
      Validate.notNull(strings, (String)"strings is null");
      List<E> result = new ArrayList(strings.size());
      strings.forEach((string) -> {
         E value = parseEnum(enumType, string);
         if (value != null) {
            result.add(value);
         }

      });
      return result;
   }

   @Nullable
   public static String toString(@Nullable Object object) {
      return object != null ? object.toString() : null;
   }

   public static Boolean toBoolean(Object object) {
      Trilean value = toTrilean(object);
      return value == null ? null : value.toBoolean();
   }

   public static Trilean toTrilean(Object object) {
      if (object != null && !(object instanceof Boolean)) {
         if (object instanceof Number) {
            int i = ((Number)object).intValue();
            if (i == 1) {
               return Trilean.TRUE;
            }

            if (i == 0) {
               return Trilean.FALSE;
            }
         } else if (object instanceof String) {
            return parseTrilean((String)object);
         }

         return null;
      } else {
         return Trilean.fromNullableBoolean((Boolean)object);
      }
   }

   @Nullable
   public static Integer toInteger(@Nullable Object object) {
      if (object instanceof Integer) {
         return (Integer)object;
      } else if (object instanceof Number) {
         return ((Number)object).intValue();
      } else {
         return object instanceof String ? parseInt((String)object) : null;
      }
   }

   @Nullable
   public static Long toLong(@Nullable Object object) {
      if (object instanceof Long) {
         return (Long)object;
      } else if (object instanceof Number) {
         return ((Number)object).longValue();
      } else {
         return object instanceof String ? parseLong((String)object) : null;
      }
   }

   @Nullable
   public static Double toDouble(@Nullable Object object) {
      if (object instanceof Double) {
         return (Double)object;
      } else if (object instanceof Number) {
         return ((Number)object).doubleValue();
      } else {
         return object instanceof String ? parseDouble((String)object) : null;
      }
   }

   @Nullable
   public static Float toFloat(@Nullable Object object) {
      if (object instanceof Float) {
         return (Float)object;
      } else if (object instanceof Number) {
         return ((Number)object).floatValue();
      } else {
         return object instanceof String ? parseFloat((String)object) : null;
      }
   }

   @Nullable
   public static <E extends Enum<E>> E toEnum(Class<E> enumType, @Nullable Object object) {
      if (enumType.isInstance(object)) {
         return (Enum)object;
      } else if (object instanceof String) {
         String enumName = (String)object;
         return parseEnum(enumType, enumName);
      } else {
         return null;
      }
   }

   @Nullable
   public static List<Integer> toIntegerList(@Nullable List<?> list) {
      if (list == null) {
         return null;
      } else {
         List<Integer> result = new ArrayList(list.size());
         list.forEach((value) -> {
            Integer integerValue = toInteger(value);
            if (integerValue != null) {
               result.add(integerValue);
            }

         });
         return result;
      }
   }

   @Nullable
   public static List<Double> toDoubleList(@Nullable List<?> list) {
      if (list == null) {
         return null;
      } else {
         List<Double> result = new ArrayList(list.size());
         list.forEach((value) -> {
            Double doubleValue = toDouble(value);
            if (doubleValue != null) {
               result.add(doubleValue);
            }

         });
         return result;
      }
   }

   @Nullable
   public static List<Float> toFloatList(@Nullable List<?> list) {
      if (list == null) {
         return null;
      } else {
         List<Float> result = new ArrayList(list.size());
         list.forEach((value) -> {
            Float floatValue = toFloat(value);
            if (floatValue != null) {
               result.add(floatValue);
            }

         });
         return result;
      }
   }

   @Nullable
   public static List<Long> toLongList(@Nullable List<?> list) {
      if (list == null) {
         return null;
      } else {
         List<Long> result = new ArrayList(list.size());
         list.forEach((value) -> {
            Long longValue = toLong(value);
            if (longValue != null) {
               result.add(longValue);
            }

         });
         return result;
      }
   }

   @Nullable
   public static List<Boolean> toBooleanList(@Nullable List<?> list) {
      if (list == null) {
         return null;
      } else {
         List<Boolean> result = new ArrayList(list.size());
         list.forEach((value) -> {
            Boolean booleanValue = toBoolean(value);
            if (booleanValue != null) {
               result.add(booleanValue);
            }

         });
         return result;
      }
   }

   @Nullable
   public static List<Trilean> toTrileanList(@Nullable List<?> list) {
      if (list == null) {
         return null;
      } else {
         List<Trilean> result = new ArrayList(list.size());
         list.forEach((value) -> {
            Trilean trileanValue = toTrilean(value);
            if (trileanValue != null) {
               result.add(trileanValue);
            }

         });
         return result;
      }
   }

   @Nullable
   public static List<String> toStringList(@Nullable List<?> list) {
      if (list == null) {
         return null;
      } else {
         List<String> result = new ArrayList(list.size());
         list.forEach((value) -> {
            String stringValue = toString(value);
            if (stringValue != null) {
               result.add(stringValue);
            }

         });
         return result;
      }
   }

   public static <E extends Enum<E>> List<E> toEnumList(Class<E> enumType, List<?> list) {
      if (list == null) {
         return null;
      } else {
         List<E> result = new ArrayList(list.size());
         list.forEach((value) -> {
            E enumValue = toEnum(enumType, value);
            if (enumValue != null) {
               result.add(enumValue);
            }

         });
         return result;
      }
   }

   private ConversionUtils() {
   }

   static {
      Map<String, Boolean> booleanValues = new HashMap();
      booleanValues.put("true", true);
      booleanValues.put("t", true);
      booleanValues.put("1", true);
      booleanValues.put("yes", true);
      booleanValues.put("y", true);
      booleanValues.put("on", true);
      booleanValues.put("enabled", true);
      booleanValues.put("false", false);
      booleanValues.put("f", false);
      booleanValues.put("0", false);
      booleanValues.put("no", false);
      booleanValues.put("n", false);
      booleanValues.put("off", false);
      booleanValues.put("disabled", false);
      BOOLEAN_VALUES = Collections.unmodifiableMap(booleanValues);
      Map<String, Trilean> trileanValues = new HashMap();
      BOOLEAN_VALUES.entrySet().forEach((entry) -> {
         trileanValues.put((String)entry.getKey(), Trilean.fromBoolean((Boolean)entry.getValue()));
      });
      trileanValues.put("undefined", Trilean.UNDEFINED);
      trileanValues.put("null", Trilean.UNDEFINED);
      trileanValues.put("none", Trilean.UNDEFINED);
      trileanValues.put("unset", Trilean.UNDEFINED);
      trileanValues.put("default", Trilean.UNDEFINED);
      TRILEAN_VALUES = Collections.unmodifiableMap(trileanValues);
   }
}
