package fr.xephi.authme.libs.com.google.common.reflect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.FluentIterable;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.libs.com.google.common.collect.UnmodifiableIterator;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
public final class Parameter implements AnnotatedElement {
   private final Invokable<?, ?> declaration;
   private final int position;
   private final TypeToken<?> type;
   private final ImmutableList<Annotation> annotations;
   private final AnnotatedType annotatedType;

   Parameter(Invokable<?, ?> declaration, int position, TypeToken<?> type, Annotation[] annotations, AnnotatedType annotatedType) {
      this.declaration = declaration;
      this.position = position;
      this.type = type;
      this.annotations = ImmutableList.copyOf((Object[])annotations);
      this.annotatedType = annotatedType;
   }

   public TypeToken<?> getType() {
      return this.type;
   }

   public Invokable<?, ?> getDeclaringInvokable() {
      return this.declaration;
   }

   public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
      return this.getAnnotation(annotationType) != null;
   }

   @CheckForNull
   public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
      Preconditions.checkNotNull(annotationType);
      UnmodifiableIterator var2 = this.annotations.iterator();

      Annotation annotation;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         annotation = (Annotation)var2.next();
      } while(!annotationType.isInstance(annotation));

      return (Annotation)annotationType.cast(annotation);
   }

   public Annotation[] getAnnotations() {
      return this.getDeclaredAnnotations();
   }

   public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
      return this.getDeclaredAnnotationsByType(annotationType);
   }

   public Annotation[] getDeclaredAnnotations() {
      return (Annotation[])this.annotations.toArray(new Annotation[0]);
   }

   @CheckForNull
   public <A extends Annotation> A getDeclaredAnnotation(Class<A> annotationType) {
      Preconditions.checkNotNull(annotationType);
      return (Annotation)FluentIterable.from((Iterable)this.annotations).filter(annotationType).first().orNull();
   }

   public <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> annotationType) {
      A[] result = (Annotation[])FluentIterable.from((Iterable)this.annotations).filter(annotationType).toArray(annotationType);
      return result;
   }

   public AnnotatedType getAnnotatedType() {
      return this.annotatedType;
   }

   public boolean equals(@CheckForNull Object obj) {
      if (!(obj instanceof Parameter)) {
         return false;
      } else {
         Parameter that = (Parameter)obj;
         return this.position == that.position && this.declaration.equals(that.declaration);
      }
   }

   public int hashCode() {
      return this.position;
   }

   public String toString() {
      String var1 = String.valueOf(this.type);
      int var2 = this.position;
      return (new StringBuilder(15 + String.valueOf(var1).length())).append(var1).append(" arg").append(var2).toString();
   }
}
