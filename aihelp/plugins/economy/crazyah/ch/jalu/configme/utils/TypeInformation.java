package ch.jalu.configme.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TypeInformation {
   @Nullable
   private final Type type;

   public TypeInformation(@Nullable Type type) {
      this.type = type;
   }

   @NotNull
   public static TypeInformation fromField(@NotNull Field field) {
      return new TypeInformation(field.getGenericType());
   }

   @Nullable
   public Type getType() {
      return this.type;
   }

   @Nullable
   public Class<?> getSafeToWriteClass() {
      return this.getSafeToWriteClassInternal(this.type);
   }

   @NotNull
   public Class<?> getSafeToReadClass() {
      Class<?> safeToReadClass = this.getSafeToReadClassInternal(this.type);
      return safeToReadClass == null ? Object.class : safeToReadClass;
   }

   @Nullable
   public TypeInformation getGenericType(int index) {
      if (this.type instanceof ParameterizedType) {
         ParameterizedType pt = (ParameterizedType)this.type;
         return pt.getActualTypeArguments().length > index ? new TypeInformation(pt.getActualTypeArguments()[index]) : null;
      } else {
         return null;
      }
   }

   @Nullable
   public Class<?> getGenericTypeAsClass(int index) {
      TypeInformation genericType = this.getGenericType(index);
      return genericType == null ? null : genericType.getSafeToWriteClass();
   }

   @Nullable
   private Class<?> getSafeToWriteClassInternal(@Nullable Type type) {
      if (type instanceof Class) {
         return (Class)type;
      } else if (type instanceof ParameterizedType) {
         ParameterizedType pt = (ParameterizedType)type;
         return (Class)pt.getRawType();
      } else {
         return null;
      }
   }

   @Nullable
   private Class<?> getSafeToReadClassInternal(@Nullable Type type) {
      Class<?> safeToWriteClass = this.getSafeToWriteClassInternal(type);
      if (safeToWriteClass != null) {
         return safeToWriteClass;
      } else {
         Type[] bounds = null;
         if (type instanceof WildcardType) {
            bounds = ((WildcardType)type).getUpperBounds();
         } else if (type instanceof TypeVariable) {
            bounds = ((TypeVariable)type).getBounds();
         }

         return bounds != null ? (Class)Arrays.stream(bounds).map(this::getSafeToReadClassInternal).filter(Objects::nonNull).findFirst().orElse((Object)null) : null;
      }
   }

   @NotNull
   public String toString() {
      return "TypeInformation[type=" + this.type + "]";
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj instanceof TypeInformation) {
         TypeInformation other = (TypeInformation)obj;
         return Objects.equals(this.type, other.type);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.type == null ? 0 : this.type.hashCode();
   }
}
