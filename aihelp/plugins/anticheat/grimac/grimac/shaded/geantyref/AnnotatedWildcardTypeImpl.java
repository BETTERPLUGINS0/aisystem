package ac.grim.grimac.shaded.geantyref;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

class AnnotatedWildcardTypeImpl extends AnnotatedTypeImpl implements AnnotatedWildcardType {
   private final AnnotatedType[] lowerBounds;
   private final AnnotatedType[] upperBounds;

   AnnotatedWildcardTypeImpl(WildcardType type, Annotation[] annotations, AnnotatedType[] lowerBounds, AnnotatedType[] upperBounds) {
      super(type, annotations);
      if (lowerBounds == null || lowerBounds.length == 0) {
         lowerBounds = new AnnotatedType[0];
      }

      if (upperBounds == null || upperBounds.length == 0) {
         upperBounds = new AnnotatedType[]{GenericTypeReflector.annotate(Object.class)};
      }

      validateBounds(type, lowerBounds, upperBounds);
      this.lowerBounds = lowerBounds;
      this.upperBounds = upperBounds;
   }

   public AnnotatedType[] getAnnotatedLowerBounds() {
      return this.lowerBounds;
   }

   public AnnotatedType[] getAnnotatedUpperBounds() {
      return this.upperBounds;
   }

   public boolean equals(Object other) {
      if (other instanceof AnnotatedWildcardType && super.equals(other)) {
         return GenericTypeReflector.typeArraysEqual(this.lowerBounds, ((AnnotatedWildcardType)other).getAnnotatedLowerBounds()) && GenericTypeReflector.typeArraysEqual(this.upperBounds, ((AnnotatedWildcardType)other).getAnnotatedUpperBounds());
      } else {
         return false;
      }
   }

   public int hashCode() {
      return 127 * super.hashCode() ^ GenericTypeReflector.hashCode(this.lowerBounds) + GenericTypeReflector.hashCode(this.upperBounds);
   }

   public String toString() {
      if (this.lowerBounds.length > 0) {
         return this.annotationsString() + "? super " + this.typesString(this.lowerBounds);
      } else {
         return this.upperBounds.length != 0 && this.upperBounds[0].getType() != Object.class ? this.annotationsString() + "? extends " + this.typesString(this.upperBounds) : this.annotationsString() + "?";
      }
   }

   private static void validateBounds(WildcardType type, AnnotatedType[] lowerBounds, AnnotatedType[] upperBounds) {
      if (type.getLowerBounds().length != lowerBounds.length) {
         throw new IllegalArgumentException("Incompatible lower bounds " + Arrays.toString(lowerBounds) + " for type " + type);
      } else if (type.getUpperBounds().length != upperBounds.length) {
         throw new IllegalArgumentException("Incompatible upper bounds " + Arrays.toString(upperBounds) + " for type " + type);
      } else {
         int i;
         for(i = 0; i < type.getLowerBounds().length; ++i) {
            if (GenericTypeReflector.erase(type.getLowerBounds()[i]) != GenericTypeReflector.erase(lowerBounds[i].getType())) {
               throw new IllegalArgumentException("Bound " + lowerBounds[i].getType() + " incompatible with " + type.getLowerBounds()[i] + " in type " + type);
            }
         }

         for(i = 0; i < type.getUpperBounds().length; ++i) {
            if (GenericTypeReflector.erase(type.getUpperBounds()[i]) != GenericTypeReflector.erase(upperBounds[i].getType())) {
               throw new IllegalArgumentException("Bound " + upperBounds[i].getType() + " incompatible with " + type.getUpperBounds()[i] + " in type " + type);
            }
         }

      }
   }
}
