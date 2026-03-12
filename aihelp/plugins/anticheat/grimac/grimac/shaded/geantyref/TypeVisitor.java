package ac.grim.grimac.shaded.geantyref;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public abstract class TypeVisitor {
   private final Map<TypeVariable, AnnotatedTypeVariable> varCache = new IdentityHashMap();
   private final Map<TypeVisitor.AnnotatedCaptureCacheKey, AnnotatedType> captureCache = new HashMap();

   protected AnnotatedType visitParameterizedType(AnnotatedParameterizedType type) {
      AnnotatedType[] params = (AnnotatedType[])Arrays.stream(type.getAnnotatedActualTypeArguments()).map((param) -> {
         return GenericTypeReflector.transform(param, this);
      }).toArray((x$0) -> {
         return new AnnotatedType[x$0];
      });
      return GenericTypeReflector.replaceParameters(type, params);
   }

   protected AnnotatedType visitWildcardType(AnnotatedWildcardType type) {
      AnnotatedType[] lowerBounds = (AnnotatedType[])Arrays.stream(type.getAnnotatedLowerBounds()).map((bound) -> {
         return GenericTypeReflector.transform(bound, this);
      }).toArray((x$0) -> {
         return new AnnotatedType[x$0];
      });
      AnnotatedType[] upperBounds = (AnnotatedType[])Arrays.stream(type.getAnnotatedUpperBounds()).map((bound) -> {
         return GenericTypeReflector.transform(bound, this);
      }).toArray((x$0) -> {
         return new AnnotatedType[x$0];
      });
      WildcardType inner = new WildcardTypeImpl(upperBounds.length > 0 ? (Type[])Arrays.stream(upperBounds).map(AnnotatedType::getType).toArray((x$0) -> {
         return new Type[x$0];
      }) : new Type[]{Object.class}, (Type[])Arrays.stream(lowerBounds).map(AnnotatedType::getType).toArray((x$0) -> {
         return new Type[x$0];
      }));
      return new AnnotatedWildcardTypeImpl(inner, type.getAnnotations(), lowerBounds, upperBounds);
   }

   protected AnnotatedType visitVariable(AnnotatedTypeVariable type) {
      TypeVariable var = (TypeVariable)type.getType();
      if (this.varCache.containsKey(var)) {
         return (AnnotatedType)this.varCache.get(var);
      } else {
         AnnotatedTypeVariableImpl variable = new AnnotatedTypeVariableImpl(var, type.getAnnotations());
         this.varCache.put(var, variable);
         AnnotatedType[] bounds = (AnnotatedType[])Arrays.stream(type.getAnnotatedBounds()).map((bound) -> {
            return GenericTypeReflector.transform(bound, this);
         }).toArray((x$0) -> {
            return new AnnotatedType[x$0];
         });
         variable.init(bounds);
         return variable;
      }
   }

   protected AnnotatedType visitArray(AnnotatedArrayType type) {
      AnnotatedType componentType = GenericTypeReflector.transform(type.getAnnotatedGenericComponentType(), this);
      return new AnnotatedArrayTypeImpl(GenericArrayTypeImpl.createArrayType(componentType.getType()), type.getAnnotations(), componentType);
   }

   protected AnnotatedType visitCaptureType(AnnotatedCaptureType type) {
      TypeVisitor.AnnotatedCaptureCacheKey key = new TypeVisitor.AnnotatedCaptureCacheKey(type);
      if (this.captureCache.containsKey(key)) {
         return (AnnotatedType)this.captureCache.get(key);
      } else {
         AnnotatedType[] lowerBounds = type.getAnnotatedLowerBounds();
         if (lowerBounds != null) {
            lowerBounds = (AnnotatedType[])Arrays.stream(lowerBounds).map((bound) -> {
               return GenericTypeReflector.transform(bound, this);
            }).toArray((x$0) -> {
               return new AnnotatedType[x$0];
            });
         }

         AnnotatedCaptureType annotatedCapture = new AnnotatedCaptureTypeImpl((CaptureType)type.getType(), type.getAnnotatedWildcardType(), type.getAnnotatedTypeVariable(), lowerBounds, (AnnotatedType[])null, type.getAnnotations());
         this.captureCache.put(key, annotatedCapture);
         AnnotatedType[] upperBounds = (AnnotatedType[])Arrays.stream(type.getAnnotatedUpperBounds()).map((bound) -> {
            return GenericTypeReflector.transform(bound, this);
         }).toArray((x$0) -> {
            return new AnnotatedType[x$0];
         });
         annotatedCapture.setAnnotatedUpperBounds(upperBounds);
         return annotatedCapture;
      }
   }

   protected AnnotatedType visitClass(AnnotatedType type) {
      return type;
   }

   protected AnnotatedType visitUnmatched(AnnotatedType type) {
      return type;
   }

   private static class AnnotatedCaptureCacheKey {
      AnnotatedCaptureType capture;
      CaptureType raw;

      AnnotatedCaptureCacheKey(AnnotatedCaptureType capture) {
         this.capture = capture;
         this.raw = (CaptureType)capture.getType();
      }

      public int hashCode() {
         return 127 * this.raw.getWildcardType().hashCode() ^ this.raw.getTypeVariable().hashCode() ^ GenericTypeReflector.hashCode(Arrays.stream(this.capture.getAnnotations()));
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof TypeVisitor.AnnotatedCaptureCacheKey)) {
            return false;
         } else {
            TypeVisitor.AnnotatedCaptureCacheKey that = (TypeVisitor.AnnotatedCaptureCacheKey)obj;
            return this.capture == that.capture || (new GenericTypeReflector.CaptureCacheKey(this.raw)).equals(new GenericTypeReflector.CaptureCacheKey(that.raw)) && Arrays.equals(this.capture.getAnnotations(), that.capture.getAnnotations());
         }
      }
   }
}
