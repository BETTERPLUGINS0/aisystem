package fr.xephi.authme.libs.ch.jalu.injector.utils;

import fr.xephi.authme.libs.ch.jalu.injector.exceptions.InjectorException;
import java.lang.reflect.Modifier;
import javax.annotation.Nullable;

public final class InjectorUtils {
   private InjectorUtils() {
   }

   public static void checkNotNull(Object o) {
      checkNotNull(o, "Object may not be null");
   }

   public static void checkNotNull(Object o, String errorMessage) {
      if (o == null) {
         throw new InjectorException(errorMessage);
      }
   }

   @SafeVarargs
   public static <T> void checkNoNullValues(T... arr) {
      checkNotNull(arr);
      Object[] var1 = arr;
      int var2 = arr.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Object o = var1[var3];
         checkNotNull(o);
      }

   }

   public static boolean containsNullValue(Object... arr) {
      Object[] var1 = arr;
      int var2 = arr.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Object o = var1[var3];
         if (o == null) {
            return true;
         }
      }

      return false;
   }

   public static void checkArgument(boolean expression, String errorMessage) {
      if (!expression) {
         throw new InjectorException(errorMessage);
      }
   }

   public static void rethrowException(Exception e) throws InjectorException {
      throw e instanceof InjectorException ? (InjectorException)e : new InjectorException("An error occurred (see cause)", e);
   }

   @Nullable
   public static <T> T firstNotNull(T obj1, T obj2) {
      return obj1 == null ? obj2 : obj1;
   }

   public static boolean canInstantiate(Class<?> clazz) {
      return !clazz.isEnum() && !clazz.isInterface() && !clazz.isArray() && !Modifier.isAbstract(clazz.getModifiers());
   }
}
