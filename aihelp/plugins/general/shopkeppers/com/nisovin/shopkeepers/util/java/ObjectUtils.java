package com.nisovin.shopkeepers.util.java;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class ObjectUtils {
   @Nullable
   public static <T> T castOrNull(@Nullable Object object, Class<? extends T> clazz) {
      return clazz.isInstance(object) ? object : null;
   }

   private ObjectUtils() {
   }
}
