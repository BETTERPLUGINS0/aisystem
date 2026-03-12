package com.nisovin.shopkeepers.api.internal.util;

import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Unsafe {
   public static <T> T cast(@UnknownInitialization @Nullable Object object) {
      return object;
   }

   @EnsuresNonNull({"#1"})
   @NonNull
   public static <T> T castNonNull(@UnknownInitialization @Nullable Object object) {
      assert object != null : "@AssumeAssertion(nullness)";

      return cast(object);
   }

   @EnsuresNonNull({"#1"})
   @NonNull
   public static <T> T assertNonNull(@Nullable T object) {
      assert object != null : "@AssumeAssertion(nullness)";

      return cast(object);
   }

   @EnsuresNonNull({"#1"})
   @Initialized
   @NonNull
   public static <T> T initialized(@UnknownInitialization @Nullable T object) {
      assert object != null : "@AssumeAssertion(nullness)";

      return cast(object);
   }

   @Nullable
   public static <T> T nullable(@Nullable T object) {
      return object;
   }

   @NonNull
   public static <T> T uncheckedNull() {
      return cast((Object)null);
   }

   @NonNull
   public static <T> T nullableAsNonNull(@Nullable T object) {
      return cast(object);
   }

   private Unsafe() {
   }
}
