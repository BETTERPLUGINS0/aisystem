package com.nisovin.shopkeepers.util.java;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Validate {
   public static void error(String errorMessage) {
      throw illegalArgumentException(errorMessage);
   }

   public static void error(Supplier<String> errorMessageSupplier) {
      throw illegalArgumentException(errorMessageSupplier);
   }

   private static IllegalArgumentException illegalArgumentException(String errorMessage) {
      return new IllegalArgumentException(errorMessage);
   }

   private static IllegalArgumentException illegalArgumentException(Supplier<String> errorMessageSupplier) {
      return new IllegalArgumentException((String)errorMessageSupplier.get());
   }

   @EnsuresNonNull({"#1"})
   @NonNull
   public static <T> T notNull(@UnknownInitialization @Nullable T object) {
      return notNull(object, "object is null");
   }

   @EnsuresNonNull({"#1"})
   @NonNull
   public static <T> T notNull(@UnknownInitialization @Nullable T object, String errorMessage) {
      if (object == null) {
         throw illegalArgumentException(errorMessage);
      } else {
         return Unsafe.initialized(object);
      }
   }

   @EnsuresNonNull({"#1"})
   @NonNull
   public static <T> T notNull(@UnknownInitialization @Nullable T object, Supplier<String> errorMessageSupplier) {
      if (object == null) {
         throw illegalArgumentException(errorMessageSupplier);
      } else {
         return Unsafe.initialized(object);
      }
   }

   @EnsuresNonNull({"#1"})
   public static String notEmpty(@Nullable String string) {
      return notEmpty(string, "string is null or empty");
   }

   @EnsuresNonNull({"#1"})
   public static String notEmpty(@Nullable String string, String errorMessage) {
      if (string != null && !string.isEmpty()) {
         return string;
      } else {
         throw illegalArgumentException(errorMessage);
      }
   }

   @EnsuresNonNull({"#1"})
   public static String notEmpty(@Nullable String string, Supplier<String> errorMessageSupplier) {
      if (string != null && !string.isEmpty()) {
         return string;
      } else {
         throw illegalArgumentException(errorMessageSupplier);
      }
   }

   public static boolean isTrue(boolean expression) {
      return isTrue(expression, "expression evaluates to false");
   }

   public static boolean isTrue(boolean expression, String errorMessage) {
      if (!expression) {
         throw illegalArgumentException(errorMessage);
      } else {
         return expression;
      }
   }

   public static boolean isTrue(boolean expression, Supplier<String> errorMessageSupplier) {
      if (!expression) {
         error(errorMessageSupplier);
      }

      return expression;
   }

   public static <T> T isTrue(T value, Predicate<T> predicate) {
      return isTrue(value, predicate, "predicate evaluates to false");
   }

   public static <T> T isTrue(T value, Predicate<T> predicate, String errorMessage) {
      if (!predicate.test(value)) {
         error(errorMessage);
      }

      return value;
   }

   public static <T> T isTrue(T value, Predicate<T> predicate, Supplier<String> errorMessageSupplier) {
      if (!predicate.test(value)) {
         error(errorMessageSupplier);
      }

      return value;
   }

   public static double isFinite(double value) {
      return isFinite(value, "value is not finite");
   }

   public static double isFinite(double value, String errorMessage) {
      if (!Double.isFinite(value)) {
         error(errorMessage);
      }

      return value;
   }

   public static double isFinite(double value, Supplier<String> errorMessageSupplier) {
      if (!Double.isFinite(value)) {
         error(errorMessageSupplier);
      }

      return value;
   }

   public static double notNaN(double value) {
      return notNaN(value, "value is NaN");
   }

   public static double notNaN(double value, String errorMessage) {
      if (Double.isNaN(value)) {
         error(errorMessage);
      }

      return value;
   }

   public static double notNaN(double value, Supplier<String> errorMessageSupplier) {
      if (Double.isNaN(value)) {
         error(errorMessageSupplier);
      }

      return value;
   }

   public static float isFinite(float value) {
      return isFinite(value, "value is not finite");
   }

   public static float isFinite(float value, String errorMessage) {
      if (!Float.isFinite(value)) {
         error(errorMessage);
      }

      return value;
   }

   public static float isFinite(float value, Supplier<String> errorMessageSupplier) {
      if (!Float.isFinite(value)) {
         error(errorMessageSupplier);
      }

      return value;
   }

   public static float notNaN(float value) {
      return notNaN(value, "value is NaN");
   }

   public static float notNaN(float value, String errorMessage) {
      if (Float.isNaN(value)) {
         error(errorMessage);
      }

      return value;
   }

   public static float notNaN(float value, Supplier<String> errorMessageSupplier) {
      if (Float.isNaN(value)) {
         error(errorMessageSupplier);
      }

      return value;
   }

   public static <T extends Iterable<?>> T noNullElements(T iterable, String errorMessage) {
      T nonNullIterable = (Iterable)notNull(iterable, (String)errorMessage);
      Iterator var3 = nonNullIterable.iterator();

      while(var3.hasNext()) {
         Object object = var3.next();
         notNull(object, errorMessage);
      }

      return nonNullIterable;
   }

   public static <T extends Iterable<?>> T noNullElements(T iterable, Supplier<String> errorMessageSupplier) {
      T nonNullIterable = (Iterable)notNull(iterable, (Supplier)errorMessageSupplier);
      Iterator var3 = nonNullIterable.iterator();

      while(var3.hasNext()) {
         Object object = var3.next();
         notNull(object, errorMessageSupplier);
      }

      return nonNullIterable;
   }

   private Validate() {
   }

   public static final class State {
      public static IllegalStateException error(String errorMessage) {
         return new IllegalStateException(errorMessage);
      }

      public static IllegalStateException error(Supplier<String> errorMessageSupplier) {
         throw new IllegalStateException((String)errorMessageSupplier.get());
      }

      @EnsuresNonNull({"#1"})
      @NonNull
      public static <T> T notNull(@UnknownInitialization @Nullable T object) {
         return notNull(object, "object is null");
      }

      @EnsuresNonNull({"#1"})
      @NonNull
      public static <T> T notNull(@UnknownInitialization @Nullable T object, String errorMessage) {
         if (object == null) {
            throw error(errorMessage);
         } else {
            return Unsafe.initialized(object);
         }
      }

      @EnsuresNonNull({"#1"})
      @NonNull
      public static <T> T notNull(@UnknownInitialization @Nullable T object, Supplier<String> errorMessageSupplier) {
         if (object == null) {
            throw error(errorMessageSupplier);
         } else {
            return Unsafe.initialized(object);
         }
      }

      @EnsuresNonNull({"#1"})
      public static String notEmpty(@Nullable String string) {
         return notEmpty(string, "string is null or empty");
      }

      @EnsuresNonNull({"#1"})
      public static String notEmpty(@Nullable String string, String errorMessage) {
         if (string != null && !string.isEmpty()) {
            return string;
         } else {
            throw error(errorMessage);
         }
      }

      @EnsuresNonNull({"#1"})
      public static String notEmpty(@Nullable String string, Supplier<String> errorMessageSupplier) {
         if (string != null && !string.isEmpty()) {
            return string;
         } else {
            throw error(errorMessageSupplier);
         }
      }

      public static boolean isTrue(boolean expression) {
         return isTrue(expression, "expression evaluates to false");
      }

      public static boolean isTrue(boolean expression, String errorMessage) {
         if (!expression) {
            throw error(errorMessage);
         } else {
            return expression;
         }
      }

      public static boolean isTrue(boolean expression, Supplier<String> errorMessageSupplier) {
         if (!expression) {
            throw error(errorMessageSupplier);
         } else {
            return expression;
         }
      }

      public static <T> T isTrue(T value, Predicate<T> predicate) {
         return isTrue(value, predicate, "predicate evaluates to false");
      }

      public static <T> T isTrue(T value, Predicate<T> predicate, String errorMessage) {
         if (!predicate.test(value)) {
            throw error(errorMessage);
         } else {
            return value;
         }
      }

      public static <T> T isTrue(T value, Predicate<T> predicate, Supplier<String> errorMessageSupplier) {
         if (!predicate.test(value)) {
            throw error(errorMessageSupplier);
         } else {
            return value;
         }
      }

      public static double isFinite(double value) {
         return isFinite(value, "value is not finite");
      }

      public static double isFinite(double value, String errorMessage) {
         if (!Double.isFinite(value)) {
            throw error(errorMessage);
         } else {
            return value;
         }
      }

      public static double isFinite(double value, Supplier<String> errorMessageSupplier) {
         if (!Double.isFinite(value)) {
            throw error(errorMessageSupplier);
         } else {
            return value;
         }
      }

      public static double notNaN(double value) {
         return notNaN(value, "value is NaN");
      }

      public static double notNaN(double value, String errorMessage) {
         if (Double.isNaN(value)) {
            throw error(errorMessage);
         } else {
            return value;
         }
      }

      public static double notNaN(double value, Supplier<String> errorMessageSupplier) {
         if (Double.isNaN(value)) {
            throw error(errorMessageSupplier);
         } else {
            return value;
         }
      }

      public static float isFinite(float value) {
         return isFinite(value, "value is not finite");
      }

      public static float isFinite(float value, String errorMessage) {
         if (!Float.isFinite(value)) {
            throw error(errorMessage);
         } else {
            return value;
         }
      }

      public static float isFinite(float value, Supplier<String> errorMessageSupplier) {
         if (!Float.isFinite(value)) {
            throw error(errorMessageSupplier);
         } else {
            return value;
         }
      }

      public static float notNaN(float value) {
         return notNaN(value, "value is NaN");
      }

      public static float notNaN(float value, String errorMessage) {
         if (Float.isNaN(value)) {
            throw error(errorMessage);
         } else {
            return value;
         }
      }

      public static float notNaN(float value, Supplier<String> errorMessageSupplier) {
         if (Float.isNaN(value)) {
            throw error(errorMessageSupplier);
         } else {
            return value;
         }
      }

      public static <T extends Iterable<?>> T noNullElements(T iterable, String errorMessage) {
         T nonNullIterable = (Iterable)notNull(iterable, (String)errorMessage);
         Iterator var3 = nonNullIterable.iterator();

         while(var3.hasNext()) {
            Object object = var3.next();
            notNull(object, errorMessage);
         }

         return nonNullIterable;
      }

      public static <T extends Iterable<?>> T noNullElements(T iterable, Supplier<String> errorMessageSupplier) {
         T nonNullIterable = (Iterable)notNull(iterable, (Supplier)errorMessageSupplier);
         Iterator var3 = nonNullIterable.iterator();

         while(var3.hasNext()) {
            Object object = var3.next();
            notNull(object, errorMessageSupplier);
         }

         return nonNullIterable;
      }

      private State() {
      }
   }
}
