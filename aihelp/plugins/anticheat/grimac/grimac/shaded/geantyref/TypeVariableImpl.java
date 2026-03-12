package ac.grim.grimac.shaded.geantyref;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TypeVariableImpl<D extends GenericDeclaration> implements TypeVariable<D> {
   private final Map<Class<? extends Annotation>, Annotation> annotations;
   private final D genericDeclaration;
   private final String name;
   private final AnnotatedType[] bounds;

   TypeVariableImpl(TypeVariable<D> variable, AnnotatedType[] bounds) {
      this(variable, variable.getAnnotations(), bounds);
   }

   TypeVariableImpl(TypeVariable<D> variable, Annotation[] annotations, AnnotatedType[] bounds) {
      Objects.requireNonNull(variable);
      this.genericDeclaration = variable.getGenericDeclaration();
      this.name = variable.getName();
      this.annotations = new HashMap();
      Annotation[] var4 = annotations;
      int var5 = annotations.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Annotation annotation = var4[var6];
         this.annotations.put(annotation.annotationType(), annotation);
      }

      if (bounds != null && bounds.length != 0) {
         this.bounds = bounds;
      } else {
         throw new IllegalArgumentException("There must be at least one bound. For an unbound variable, the bound must be Object");
      }
   }

   public Type[] getBounds() {
      return (Type[])Arrays.stream(this.bounds).map(AnnotatedType::getType).toArray((x$0) -> {
         return new Type[x$0];
      });
   }

   public D getGenericDeclaration() {
      return this.genericDeclaration;
   }

   public String getName() {
      return this.name;
   }

   public AnnotatedType[] getAnnotatedBounds() {
      return this.bounds;
   }

   public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
      return (Annotation)this.annotations.get(annotationClass);
   }

   public Annotation[] getAnnotations() {
      return (Annotation[])this.annotations.values().toArray(new Annotation[0]);
   }

   public Annotation[] getDeclaredAnnotations() {
      return this.getAnnotations();
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof TypeVariable)) {
         return false;
      } else {
         TypeVariable<?> that = (TypeVariable)other;
         return Objects.equals(this.genericDeclaration, that.getGenericDeclaration()) && Objects.equals(this.name, that.getName());
      }
   }

   public int hashCode() {
      return this.genericDeclaration.hashCode() ^ this.name.hashCode();
   }

   public String toString() {
      return this.annotationsString() + this.getName();
   }

   private String annotationsString() {
      return this.annotations.isEmpty() ? "" : (String)this.annotations.values().stream().map(Annotation::toString).collect(Collectors.joining(", ")) + " ";
   }
}
