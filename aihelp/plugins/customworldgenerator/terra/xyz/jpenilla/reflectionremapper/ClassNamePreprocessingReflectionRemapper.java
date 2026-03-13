package xyz.jpenilla.reflectionremapper;

import java.util.function.UnaryOperator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
final class ClassNamePreprocessingReflectionRemapper implements ReflectionRemapper {
   private final ReflectionRemapper delegate;
   private final UnaryOperator<String> processor;

   ClassNamePreprocessingReflectionRemapper(final ReflectionRemapper delegate, final UnaryOperator<String> processor) {
      this.delegate = delegate;
      this.processor = processor;
   }

   public String remapClassName(final String className) {
      return this.delegate.remapClassName((String)this.processor.apply(className));
   }

   public String remapFieldName(final Class<?> holdingClass, final String fieldName) {
      return this.delegate.remapFieldName(holdingClass, fieldName);
   }

   public String remapMethodName(final Class<?> holdingClass, final String methodName, final Class<?>... paramTypes) {
      return this.delegate.remapMethodName(holdingClass, methodName, paramTypes);
   }
}
