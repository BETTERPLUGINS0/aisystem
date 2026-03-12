package ac.grim.grimac.shaded.geantyref;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeToken<T> {
   private final AnnotatedType type;
   private volatile AnnotatedType canonical;

   protected TypeToken() {
      this.type = this.extractType();
   }

   private TypeToken(AnnotatedType type) {
      this.type = type;
   }

   public static <T> TypeToken<T> get(Class<T> type) {
      return new TypeToken<T>(GenericTypeReflector.annotate(type)) {
      };
   }

   public static TypeToken<?> get(Type type) {
      return new TypeToken<Object>(GenericTypeReflector.annotate(type)) {
      };
   }

   public Type getType() {
      return this.type.getType();
   }

   public AnnotatedType getAnnotatedType() {
      return this.type;
   }

   public AnnotatedType getCanonicalType() {
      if (this.canonical == null) {
         this.canonical = GenericTypeReflector.toCanonical(this.type);
      }

      return this.canonical;
   }

   private AnnotatedType extractType() {
      AnnotatedType t = this.getClass().getAnnotatedSuperclass();
      if (!(t instanceof AnnotatedParameterizedType)) {
         throw new RuntimeException("Invalid TypeToken; must specify type parameters");
      } else {
         AnnotatedParameterizedType pt = (AnnotatedParameterizedType)t;
         if (((ParameterizedType)pt.getType()).getRawType() != TypeToken.class) {
            throw new RuntimeException("Invalid TypeToken; must directly extend TypeToken");
         } else {
            return pt.getAnnotatedActualTypeArguments()[0];
         }
      }
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else {
         return obj instanceof TypeToken && this.getCanonicalType().equals(((TypeToken)obj).type);
      }
   }

   public int hashCode() {
      return this.getType().hashCode();
   }

   // $FF: synthetic method
   TypeToken(AnnotatedType x0, Object x1) {
      this(x0);
   }
}
