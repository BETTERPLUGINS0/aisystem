package me.PM2.infinitevehicles.commands;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.regex.Pattern;

abstract class AnnotationLookups {
   boolean hasAnnotation(AnnotatedElement object, Class<? extends Annotation> annoClass) {
      return this.getAnnotationValue(var1, var2, 0) != null;
   }

   boolean hasAnnotation(AnnotatedElement object, Class<? extends Annotation> annoClass, boolean allowEmpty) {
      return this.getAnnotationValue(var1, var2, 0 | (var3 ? 0 : 8)) != null;
   }

   String[] getAnnotationValues(AnnotatedElement object, Class<? extends Annotation> annoClass) {
      return this.getAnnotationValues(var1, var2, ACFPatterns.PIPE, 1);
   }

   String[] getAnnotationValues(AnnotatedElement object, Class<? extends Annotation> annoClass, Pattern pattern) {
      return this.getAnnotationValues(var1, var2, var3, 1);
   }

   String[] getAnnotationValues(AnnotatedElement object, Class<? extends Annotation> annoClass, int options) {
      return this.getAnnotationValues(var1, var2, ACFPatterns.PIPE, var3);
   }

   String[] getAnnotationValues(AnnotatedElement object, Class<? extends Annotation> annoClass, Pattern pattern, int options) {
      String var5 = this.getAnnotationValue(var1, var2, var4);
      return var5 == null ? null : var3.split(var5);
   }

   String getAnnotationValue(AnnotatedElement object, Class<? extends Annotation> annoClass) {
      return this.getAnnotationValue(var1, var2, 1);
   }

   abstract String getAnnotationValue(AnnotatedElement object, Class<? extends Annotation> annoClass, int options);

   <T extends Annotation> T getAnnotationFromClass(Class<?> clazz, Class<T> annoClass) {
      while(var1 != null && BaseCommand.class.isAssignableFrom(var1)) {
         Annotation var3 = var1.getAnnotation(var2);
         if (var3 != null) {
            return var3;
         }

         for(Class var4 = var1.getSuperclass(); var4 != null && BaseCommand.class.isAssignableFrom(var4); var4 = var4.getSuperclass()) {
            var3 = var4.getAnnotation(var2);
            if (var3 != null) {
               return var3;
            }
         }

         var1 = var1.getEnclosingClass();
      }

      return null;
   }
}
