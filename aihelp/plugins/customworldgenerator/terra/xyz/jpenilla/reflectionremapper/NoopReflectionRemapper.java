package xyz.jpenilla.reflectionremapper;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
final class NoopReflectionRemapper implements ReflectionRemapper {
   static NoopReflectionRemapper INSTANCE = new NoopReflectionRemapper();

   private NoopReflectionRemapper() {
   }

   public String remapClassName(final String className) {
      return className;
   }

   public String remapFieldName(final Class<?> holdingClass, final String fieldName) {
      return fieldName;
   }

   public String remapMethodName(final Class<?> holdingClass, final String methodName, final Class<?>... paramTypes) {
      return methodName;
   }

   public String remapClassOrArrayName(final String name) {
      return name;
   }
}
