package fr.xephi.authme.libs.com.google.common.reflect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
public abstract class Invokable<T, R> implements AnnotatedElement, Member {
   private final AccessibleObject accessibleObject;
   private final Member member;

   <M extends AccessibleObject & Member> Invokable(M member) {
      Preconditions.checkNotNull(member);
      this.accessibleObject = member;
      this.member = (Member)member;
   }

   public static Invokable<?, Object> from(Method method) {
      return new Invokable.MethodInvokable(method);
   }

   public static <T> Invokable<T, T> from(Constructor<T> constructor) {
      return new Invokable.ConstructorInvokable(constructor);
   }

   public final boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
      return this.accessibleObject.isAnnotationPresent(annotationClass);
   }

   @CheckForNull
   public final <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
      return this.accessibleObject.getAnnotation(annotationClass);
   }

   public final Annotation[] getAnnotations() {
      return this.accessibleObject.getAnnotations();
   }

   public final Annotation[] getDeclaredAnnotations() {
      return this.accessibleObject.getDeclaredAnnotations();
   }

   public abstract TypeVariable<?>[] getTypeParameters();

   public final void setAccessible(boolean flag) {
      this.accessibleObject.setAccessible(flag);
   }

   public final boolean trySetAccessible() {
      try {
         this.accessibleObject.setAccessible(true);
         return true;
      } catch (RuntimeException var2) {
         return false;
      }
   }

   public final boolean isAccessible() {
      return this.accessibleObject.isAccessible();
   }

   public final String getName() {
      return this.member.getName();
   }

   public final int getModifiers() {
      return this.member.getModifiers();
   }

   public final boolean isSynthetic() {
      return this.member.isSynthetic();
   }

   public final boolean isPublic() {
      return Modifier.isPublic(this.getModifiers());
   }

   public final boolean isProtected() {
      return Modifier.isProtected(this.getModifiers());
   }

   public final boolean isPackagePrivate() {
      return !this.isPrivate() && !this.isPublic() && !this.isProtected();
   }

   public final boolean isPrivate() {
      return Modifier.isPrivate(this.getModifiers());
   }

   public final boolean isStatic() {
      return Modifier.isStatic(this.getModifiers());
   }

   public final boolean isFinal() {
      return Modifier.isFinal(this.getModifiers());
   }

   public final boolean isAbstract() {
      return Modifier.isAbstract(this.getModifiers());
   }

   public final boolean isNative() {
      return Modifier.isNative(this.getModifiers());
   }

   public final boolean isSynchronized() {
      return Modifier.isSynchronized(this.getModifiers());
   }

   final boolean isVolatile() {
      return Modifier.isVolatile(this.getModifiers());
   }

   final boolean isTransient() {
      return Modifier.isTransient(this.getModifiers());
   }

   public boolean equals(@CheckForNull Object obj) {
      if (!(obj instanceof Invokable)) {
         return false;
      } else {
         Invokable<?, ?> that = (Invokable)obj;
         return this.getOwnerType().equals(that.getOwnerType()) && this.member.equals(that.member);
      }
   }

   public int hashCode() {
      return this.member.hashCode();
   }

   public String toString() {
      return this.member.toString();
   }

   public abstract boolean isOverridable();

   public abstract boolean isVarArgs();

   @CheckForNull
   @CanIgnoreReturnValue
   public final R invoke(@CheckForNull T receiver, Object... args) throws InvocationTargetException, IllegalAccessException {
      return this.invokeInternal(receiver, (Object[])Preconditions.checkNotNull(args));
   }

   public final TypeToken<? extends R> getReturnType() {
      return TypeToken.of(this.getGenericReturnType());
   }

   public final ImmutableList<Parameter> getParameters() {
      Type[] parameterTypes = this.getGenericParameterTypes();
      Annotation[][] annotations = this.getParameterAnnotations();
      AnnotatedType[] annotatedTypes = this.getAnnotatedParameterTypes();
      ImmutableList.Builder<Parameter> builder = ImmutableList.builder();

      for(int i = 0; i < parameterTypes.length; ++i) {
         builder.add((Object)(new Parameter(this, i, TypeToken.of(parameterTypes[i]), annotations[i], annotatedTypes[i])));
      }

      return builder.build();
   }

   public final ImmutableList<TypeToken<? extends Throwable>> getExceptionTypes() {
      ImmutableList.Builder<TypeToken<? extends Throwable>> builder = ImmutableList.builder();
      Type[] var2 = this.getGenericExceptionTypes();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Type type = var2[var4];
         TypeToken<? extends Throwable> exceptionType = TypeToken.of(type);
         builder.add((Object)exceptionType);
      }

      return builder.build();
   }

   public final <R1 extends R> Invokable<T, R1> returning(Class<R1> returnType) {
      return this.returning(TypeToken.of(returnType));
   }

   public final <R1 extends R> Invokable<T, R1> returning(TypeToken<R1> returnType) {
      if (!returnType.isSupertypeOf(this.getReturnType())) {
         String var2 = String.valueOf(this.getReturnType());
         String var3 = String.valueOf(returnType);
         throw new IllegalArgumentException((new StringBuilder(35 + String.valueOf(var2).length() + String.valueOf(var3).length())).append("Invokable is known to return ").append(var2).append(", not ").append(var3).toString());
      } else {
         return this;
      }
   }

   public final Class<? super T> getDeclaringClass() {
      return this.member.getDeclaringClass();
   }

   public TypeToken<T> getOwnerType() {
      return TypeToken.of(this.getDeclaringClass());
   }

   @CheckForNull
   abstract Object invokeInternal(@CheckForNull Object var1, Object[] var2) throws InvocationTargetException, IllegalAccessException;

   abstract Type[] getGenericParameterTypes();

   abstract AnnotatedType[] getAnnotatedParameterTypes();

   abstract Type[] getGenericExceptionTypes();

   abstract Annotation[][] getParameterAnnotations();

   abstract Type getGenericReturnType();

   public abstract AnnotatedType getAnnotatedReturnType();

   static class ConstructorInvokable<T> extends Invokable<T, T> {
      final Constructor<?> constructor;

      ConstructorInvokable(Constructor<?> constructor) {
         super(constructor);
         this.constructor = constructor;
      }

      final Object invokeInternal(@CheckForNull Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
         try {
            return this.constructor.newInstance(args);
         } catch (InstantiationException var5) {
            String var4 = String.valueOf(this.constructor);
            throw new RuntimeException((new StringBuilder(8 + String.valueOf(var4).length())).append(var4).append(" failed.").toString(), var5);
         }
      }

      Type getGenericReturnType() {
         Class<?> declaringClass = this.getDeclaringClass();
         TypeVariable<?>[] typeParams = declaringClass.getTypeParameters();
         return (Type)(typeParams.length > 0 ? Types.newParameterizedType(declaringClass, typeParams) : declaringClass);
      }

      Type[] getGenericParameterTypes() {
         Type[] types = this.constructor.getGenericParameterTypes();
         if (types.length > 0 && this.mayNeedHiddenThis()) {
            Class<?>[] rawParamTypes = this.constructor.getParameterTypes();
            if (types.length == rawParamTypes.length && rawParamTypes[0] == this.getDeclaringClass().getEnclosingClass()) {
               return (Type[])Arrays.copyOfRange(types, 1, types.length);
            }
         }

         return types;
      }

      AnnotatedType[] getAnnotatedParameterTypes() {
         return this.constructor.getAnnotatedParameterTypes();
      }

      public AnnotatedType getAnnotatedReturnType() {
         return this.constructor.getAnnotatedReturnType();
      }

      Type[] getGenericExceptionTypes() {
         return this.constructor.getGenericExceptionTypes();
      }

      final Annotation[][] getParameterAnnotations() {
         return this.constructor.getParameterAnnotations();
      }

      public final TypeVariable<?>[] getTypeParameters() {
         TypeVariable<?>[] declaredByClass = this.getDeclaringClass().getTypeParameters();
         TypeVariable<?>[] declaredByConstructor = this.constructor.getTypeParameters();
         TypeVariable<?>[] result = new TypeVariable[declaredByClass.length + declaredByConstructor.length];
         System.arraycopy(declaredByClass, 0, result, 0, declaredByClass.length);
         System.arraycopy(declaredByConstructor, 0, result, declaredByClass.length, declaredByConstructor.length);
         return result;
      }

      public final boolean isOverridable() {
         return false;
      }

      public final boolean isVarArgs() {
         return this.constructor.isVarArgs();
      }

      private boolean mayNeedHiddenThis() {
         Class<?> declaringClass = this.constructor.getDeclaringClass();
         if (declaringClass.getEnclosingConstructor() != null) {
            return true;
         } else {
            Method enclosingMethod = declaringClass.getEnclosingMethod();
            if (enclosingMethod != null) {
               return !Modifier.isStatic(enclosingMethod.getModifiers());
            } else {
               return declaringClass.getEnclosingClass() != null && !Modifier.isStatic(declaringClass.getModifiers());
            }
         }
      }
   }

   static class MethodInvokable<T> extends Invokable<T, Object> {
      final Method method;

      MethodInvokable(Method method) {
         super(method);
         this.method = method;
      }

      @CheckForNull
      final Object invokeInternal(@CheckForNull Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
         return this.method.invoke(receiver, args);
      }

      Type getGenericReturnType() {
         return this.method.getGenericReturnType();
      }

      Type[] getGenericParameterTypes() {
         return this.method.getGenericParameterTypes();
      }

      AnnotatedType[] getAnnotatedParameterTypes() {
         return this.method.getAnnotatedParameterTypes();
      }

      public AnnotatedType getAnnotatedReturnType() {
         return this.method.getAnnotatedReturnType();
      }

      Type[] getGenericExceptionTypes() {
         return this.method.getGenericExceptionTypes();
      }

      final Annotation[][] getParameterAnnotations() {
         return this.method.getParameterAnnotations();
      }

      public final TypeVariable<?>[] getTypeParameters() {
         return this.method.getTypeParameters();
      }

      public final boolean isOverridable() {
         return !this.isFinal() && !this.isPrivate() && !this.isStatic() && !Modifier.isFinal(this.getDeclaringClass().getModifiers());
      }

      public final boolean isVarArgs() {
         return this.method.isVarArgs();
      }
   }
}
