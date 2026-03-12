package ac.grim.grimac.shaded.geantyref;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Objects;

class GenericArrayTypeImpl implements GenericArrayType {
   private final Type componentType;

   GenericArrayTypeImpl(Type componentType) {
      this.componentType = componentType;
   }

   static Class<?> createArrayType(Class<?> componentType) {
      return Array.newInstance(componentType, 0).getClass();
   }

   static Type createArrayType(Type componentType) {
      return (Type)(componentType instanceof Class ? createArrayType((Class)componentType) : new GenericArrayTypeImpl(componentType));
   }

   public Type getGenericComponentType() {
      return this.componentType;
   }

   public boolean equals(Object other) {
      return other instanceof GenericArrayType && Objects.equals(this.componentType, ((GenericArrayType)other).getGenericComponentType());
   }

   public int hashCode() {
      return Objects.hashCode(this.componentType);
   }

   public String toString() {
      return this.componentType + "[]";
   }
}
