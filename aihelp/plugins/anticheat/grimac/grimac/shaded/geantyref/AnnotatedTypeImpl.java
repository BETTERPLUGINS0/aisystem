package ac.grim.grimac.shaded.geantyref;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class AnnotatedTypeImpl implements AnnotatedType {
   protected Type type;
   protected Map<Class<? extends Annotation>, Annotation> annotations;

   AnnotatedTypeImpl(Type type) {
      this(type, new Annotation[0]);
   }

   AnnotatedTypeImpl(Type type, Annotation[] annotations) {
      this.type = (Type)Objects.requireNonNull(type);
      this.annotations = this.toMap(annotations);
   }

   public Type getType() {
      return this.type;
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
      } else if (!(other instanceof AnnotatedType)) {
         return false;
      } else {
         AnnotatedType that = (AnnotatedType)other;
         return this.getType().equals(that.getType()) && Arrays.equals(this.getAnnotations(), that.getAnnotations());
      }
   }

   public int hashCode() {
      return 127 * this.getType().hashCode() ^ Arrays.hashCode(this.getAnnotations());
   }

   public String toString() {
      return this.annotationsString() + GenericTypeReflector.getTypeName(this.type);
   }

   String annotationsString() {
      return this.annotations.isEmpty() ? "" : (String)this.annotations.values().stream().map(Annotation::toString).collect(Collectors.joining(", ")) + " ";
   }

   String typesString(AnnotatedType[] types) {
      return (String)Arrays.stream(types).map(Object::toString).collect(Collectors.joining(", "));
   }

   protected Map<Class<? extends Annotation>, Annotation> toMap(Annotation[] annotations) {
      Map<Class<? extends Annotation>, Annotation> map = new LinkedHashMap();
      Annotation[] var3 = annotations;
      int var4 = annotations.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Annotation annotation = var3[var5];
         map.put(annotation.annotationType(), annotation);
      }

      return Collections.unmodifiableMap(map);
   }
}
