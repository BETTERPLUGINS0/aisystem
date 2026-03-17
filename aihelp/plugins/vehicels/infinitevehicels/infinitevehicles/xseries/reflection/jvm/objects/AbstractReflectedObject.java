package me.PM2.infinitevehicles.xseries.reflection.jvm.objects;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

abstract class AbstractReflectedObject implements ReflectedObject {
   public abstract AnnotatedElement unreflect();

   public final <A extends Annotation> A getAnnotation(Class<A> var1) {
      return this.unreflect().getAnnotation(var1);
   }

   public final boolean isAnnotationPresent(Class<? extends Annotation> var1) {
      return this.unreflect().isAnnotationPresent(var1);
   }

   public final <A extends Annotation> A[] getAnnotationsByType(Class<A> var1) {
      return this.unreflect().getAnnotationsByType(var1);
   }

   public final Annotation[] getAnnotations() {
      return this.unreflect().getAnnotations();
   }

   public final <A extends Annotation> A getDeclaredAnnotation(Class<A> var1) {
      return this.unreflect().getDeclaredAnnotation(var1);
   }

   public final <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> var1) {
      return this.unreflect().getDeclaredAnnotationsByType(var1);
   }

   public final Annotation[] getDeclaredAnnotations() {
      return this.unreflect().getDeclaredAnnotations();
   }

   public final boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else {
         return var1 instanceof ReflectedObject ? this.unreflect().equals(((ReflectedObject)var1).unreflect()) : this.unreflect().equals(var1);
      }
   }

   public final int hashCode() {
      return this.unreflect().hashCode();
   }

   public final String toString() {
      return this.getClass().getSimpleName() + '(' + this.unreflect() + ')';
   }
}
