package com.nisovin.shopkeepers.util.java;

import java.util.concurrent.Callable;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ThrowableUtils {
   public static Throwable getRootCause(Throwable throwable) {
      Validate.notNull(throwable, (String)"throwable is null");

      Throwable current;
      Throwable cause;
      for(current = throwable; (cause = current.getCause()) != null; current = cause) {
      }

      return current;
   }

   public static String getDescription(Throwable throwable) {
      String message = getMessageRecursively(throwable);
      return message != null ? message : throwable.getClass().getName();
   }

   @Nullable
   public static String getMessageRecursively(Throwable throwable) {
      String message = throwable.getMessage();
      if (!StringUtils.isEmpty(message)) {
         return message;
      } else {
         Throwable cause = throwable.getCause();
         return cause != null ? getMessageRecursively(cause) : null;
      }
   }

   public static <T extends Throwable> Error rethrow(Throwable throwable) throws T {
      throw throwable;
   }

   public static <T> T callUnchecked(Callable<? extends T> callable) {
      try {
         return callable.call();
      } catch (Throwable var2) {
         throw rethrow(var2);
      }
   }

   private ThrowableUtils() {
   }
}
