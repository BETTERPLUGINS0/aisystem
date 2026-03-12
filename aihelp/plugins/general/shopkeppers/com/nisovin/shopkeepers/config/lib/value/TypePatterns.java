package com.nisovin.shopkeepers.config.lib.value;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.Validate;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypePatterns {
   public static TypePattern forClass(Class<?> clazz) {
      return new TypePatterns.ClassTypePattern(clazz);
   }

   public static TypePattern forBaseType(Class<?> baseType) {
      return new TypePatterns.BaseTypePattern(baseType);
   }

   public static TypePattern parameterized(Class<?> clazz, TypePattern... typeParameters) {
      return new TypePatterns.ParameterizedTypePattern(clazz, typeParameters);
   }

   public static TypePattern parameterized(Class<?> clazz, Class<?>... typeParameters) {
      Validate.notNull(typeParameters, (String)"typeParameters is null");
      TypePattern[] typePatterns = new TypePattern[typeParameters.length];

      for(int i = 0; i < typeParameters.length; ++i) {
         Class<?> typeParameter = typeParameters[i];
         Validate.notNull(typeParameter, (String)"typeParameters contains null");
         typePatterns[i] = forClass(typeParameter);
      }

      return parameterized(clazz, (TypePattern[])Unsafe.cast(typePatterns));
   }

   public static TypePattern any() {
      return TypePatterns.AnyTypePattern.INSTANCE;
   }

   private TypePatterns() {
   }

   private static class ClassTypePattern implements TypePattern {
      private final Class<?> clazz;

      public ClassTypePattern(Class<?> clazz) {
         Validate.notNull(clazz, (String)"clazz is null");
         this.clazz = clazz;
      }

      public boolean matches(Type type) {
         if (this.clazz == type) {
            return true;
         } else {
            return type instanceof ParameterizedType && this.clazz == ((ParameterizedType)type).getRawType();
         }
      }
   }

   private static class BaseTypePattern implements TypePattern {
      private final Class<?> baseType;

      public BaseTypePattern(Class<?> baseType) {
         Validate.notNull(baseType, (String)"baseType is null");
         this.baseType = baseType;
      }

      public boolean matches(Type type) {
         Type actualType = type;
         if (type instanceof ParameterizedType) {
            actualType = ((ParameterizedType)type).getRawType();
         }

         if (!(actualType instanceof Class)) {
            return false;
         } else {
            Class<?> actualClass = (Class)actualType;
            return this.baseType.isAssignableFrom(actualClass);
         }
      }
   }

   private static class ParameterizedTypePattern extends TypePatterns.ClassTypePattern {
      private final TypePattern[] typeParameters;

      public ParameterizedTypePattern(Class<?> clazz, TypePattern... typeParameters) {
         super(clazz);
         Validate.notNull(typeParameters, (String)"typeParameters is null");
         Validate.isTrue(typeParameters.length > 0, "typeParameters is empty");
         this.typeParameters = (TypePattern[])typeParameters.clone();
      }

      public boolean matches(Type type) {
         if (!super.matches(type)) {
            return false;
         } else if (!(type instanceof ParameterizedType)) {
            return false;
         } else {
            Type[] typeArguments = ((ParameterizedType)type).getActualTypeArguments();

            assert typeArguments != null;

            if (typeArguments.length != this.typeParameters.length) {
               return false;
            } else {
               for(int i = 0; i < this.typeParameters.length; ++i) {
                  if (!this.typeParameters[i].matches(typeArguments[i])) {
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }

   private static class AnyTypePattern implements TypePattern {
      public static final TypePatterns.AnyTypePattern INSTANCE = new TypePatterns.AnyTypePattern();

      public AnyTypePattern() {
      }

      public boolean matches(Type type) {
         return true;
      }
   }
}
