package com.nisovin.shopkeepers.util.java;

import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class PredicateUtils {
   private static final Predicate<Object> ALWAYS_TRUE = (object) -> {
      return true;
   };
   private static final Predicate<Object> ALWAYS_FALSE = (object) -> {
      return false;
   };

   public static <T> Predicate<T> alwaysTrue() {
      return ALWAYS_TRUE;
   }

   public static <T> Predicate<T> alwaysFalse() {
      return ALWAYS_FALSE;
   }

   public static <T> Predicate<T> orAlwaysTrue(@Nullable Predicate<T> predicate) {
      return predicate != null ? predicate : alwaysTrue();
   }

   public static <T> Predicate<T> orAlwaysFalse(@Nullable Predicate<T> predicate) {
      return predicate != null ? predicate : alwaysFalse();
   }

   private PredicateUtils() {
   }
}
