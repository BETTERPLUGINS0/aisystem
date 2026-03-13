package com.nisovin.shopkeepers.util.java;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class EnumUtils {
   @NonNull
   public static <T extends Enum<T>> T cycleEnumConstant(Class<? extends T> enumClass, @NonNull T current, boolean backwards) {
      return cycleEnumConstant(enumClass, current, backwards, PredicateUtils.alwaysTrue());
   }

   public static <T extends Enum<T>> T cycleEnumConstant(Class<? extends T> enumClass, T current, boolean backwards, Predicate<? super T> predicate) {
      return cycleEnumConstant(enumClass, false, current, backwards, predicate);
   }

   public static <T extends Enum<T>> T cycleEnumConstantNullable(Class<? extends T> enumClass, T current, boolean backwards) {
      return cycleEnumConstantNullable(enumClass, current, backwards, PredicateUtils.alwaysTrue());
   }

   public static <T extends Enum<T>> T cycleEnumConstantNullable(Class<? extends T> enumClass, T current, boolean backwards, Predicate<? super T> predicate) {
      return cycleEnumConstant(enumClass, true, current, backwards, predicate);
   }

   private static <E extends Enum<E>, T extends E> T cycleEnumConstant(Class<? extends T> enumClass, boolean nullable, T current, boolean backwards, Predicate<? super T> predicate) {
      Validate.notNull(enumClass, (String)"enumClass is null");
      T[] values = (Enum[])Unsafe.assertNonNull((Enum[])enumClass.getEnumConstants());
      List<T> valuesList = Arrays.asList(values);
      return (Enum)CollectionUtils.cycleValue(valuesList, nullable, current, backwards, predicate);
   }

   @Nullable
   public static <E extends Enum<E>> E valueOf(Class<E> enumType, @Nullable String enumName) {
      if (enumName == null) {
         return null;
      } else {
         try {
            return Enum.valueOf(enumType, enumName);
         } catch (IllegalArgumentException var3) {
            return null;
         }
      }
   }

   public static String formatEnumName(String enumName) {
      Validate.notNull(enumName, (String)"enumName is null");
      String formatted = enumName.toLowerCase(Locale.ROOT);
      formatted = formatted.replace('_', ' ');
      formatted = StringUtils.capitalizeAll(formatted);
      return formatted;
   }

   public static String normalizeEnumName(String enumName) {
      Validate.notNull(enumName, (String)"enumName is null");
      String normalized = enumName.trim();
      normalized = normalized.replace('-', '_');
      normalized = normalized.replace('.', '_');
      normalized = StringUtils.replaceWhitespace(normalized, "_");
      normalized = normalized.toUpperCase(Locale.ROOT);
      return normalized;
   }

   private EnumUtils() {
   }
}
