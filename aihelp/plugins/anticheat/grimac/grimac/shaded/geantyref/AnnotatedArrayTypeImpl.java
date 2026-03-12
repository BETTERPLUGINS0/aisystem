package ac.grim.grimac.shaded.geantyref;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

class AnnotatedArrayTypeImpl extends AnnotatedTypeImpl implements AnnotatedArrayType {
   private final AnnotatedType componentType;

   AnnotatedArrayTypeImpl(Type type, Annotation[] annotations, AnnotatedType componentType) {
      super(type, annotations);
      this.componentType = componentType;
   }

   static AnnotatedArrayType createArrayType(AnnotatedType componentType, Annotation[] annotations) {
      return new AnnotatedArrayTypeImpl(GenericArrayTypeImpl.createArrayType(componentType.getType()), annotations, componentType);
   }

   public AnnotatedType getAnnotatedGenericComponentType() {
      return this.componentType;
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else {
         return other instanceof AnnotatedArrayType && super.equals(other) && this.componentType.equals(((AnnotatedArrayType)other).getAnnotatedGenericComponentType());
      }
   }

   public int hashCode() {
      return 127 * super.hashCode() ^ this.componentType.hashCode();
   }

   public String toString() {
      return this.componentType.toString() + " " + this.annotationsString() + "[]";
   }
}
