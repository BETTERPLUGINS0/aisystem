package fr.xephi.authme.libs.com.google.common.reflect;

import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.Joiner;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Predicates;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableMap;
import fr.xephi.authme.libs.com.google.common.collect.Iterables;
import fr.xephi.authme.libs.com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.AccessControlException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
final class Types {
   private static final Joiner COMMA_JOINER = Joiner.on(", ").useForNull("null");

   static Type newArrayType(Type componentType) {
      if (componentType instanceof WildcardType) {
         WildcardType wildcard = (WildcardType)componentType;
         Type[] lowerBounds = wildcard.getLowerBounds();
         Preconditions.checkArgument(lowerBounds.length <= 1, "Wildcard cannot have more than one lower bounds.");
         if (lowerBounds.length == 1) {
            return supertypeOf(newArrayType(lowerBounds[0]));
         } else {
            Type[] upperBounds = wildcard.getUpperBounds();
            Preconditions.checkArgument(upperBounds.length == 1, "Wildcard should have only one upper bound.");
            return subtypeOf(newArrayType(upperBounds[0]));
         }
      } else {
         return Types.JavaVersion.CURRENT.newArrayType(componentType);
      }
   }

   static ParameterizedType newParameterizedTypeWithOwner(@CheckForNull Type ownerType, Class<?> rawType, Type... arguments) {
      if (ownerType == null) {
         return newParameterizedType(rawType, arguments);
      } else {
         Preconditions.checkNotNull(arguments);
         Preconditions.checkArgument(rawType.getEnclosingClass() != null, "Owner type for unenclosed %s", (Object)rawType);
         return new Types.ParameterizedTypeImpl(ownerType, rawType, arguments);
      }
   }

   static ParameterizedType newParameterizedType(Class<?> rawType, Type... arguments) {
      return new Types.ParameterizedTypeImpl(Types.ClassOwnership.JVM_BEHAVIOR.getOwnerType(rawType), rawType, arguments);
   }

   static <D extends GenericDeclaration> TypeVariable<D> newArtificialTypeVariable(D declaration, String name, Type... bounds) {
      return newTypeVariableImpl(declaration, name, bounds.length == 0 ? new Type[]{Object.class} : bounds);
   }

   @VisibleForTesting
   static WildcardType subtypeOf(Type upperBound) {
      return new Types.WildcardTypeImpl(new Type[0], new Type[]{upperBound});
   }

   @VisibleForTesting
   static WildcardType supertypeOf(Type lowerBound) {
      return new Types.WildcardTypeImpl(new Type[]{lowerBound}, new Type[]{Object.class});
   }

   static String toString(Type type) {
      return type instanceof Class ? ((Class)type).getName() : type.toString();
   }

   @CheckForNull
   static Type getComponentType(Type type) {
      Preconditions.checkNotNull(type);
      final AtomicReference<Type> result = new AtomicReference();
      (new TypeVisitor() {
         void visitTypeVariable(TypeVariable<?> t) {
            result.set(Types.subtypeOfComponentType(t.getBounds()));
         }

         void visitWildcardType(WildcardType t) {
            result.set(Types.subtypeOfComponentType(t.getUpperBounds()));
         }

         void visitGenericArrayType(GenericArrayType t) {
            result.set(t.getGenericComponentType());
         }

         void visitClass(Class<?> t) {
            result.set(t.getComponentType());
         }
      }).visit(new Type[]{type});
      return (Type)result.get();
   }

   @CheckForNull
   private static Type subtypeOfComponentType(Type[] bounds) {
      Type[] var1 = bounds;
      int var2 = bounds.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Type bound = var1[var3];
         Type componentType = getComponentType(bound);
         if (componentType != null) {
            if (componentType instanceof Class) {
               Class<?> componentClass = (Class)componentType;
               if (componentClass.isPrimitive()) {
                  return componentClass;
               }
            }

            return subtypeOf(componentType);
         }
      }

      return null;
   }

   private static <D extends GenericDeclaration> TypeVariable<D> newTypeVariableImpl(D genericDeclaration, String name, Type[] bounds) {
      Types.TypeVariableImpl<D> typeVariableImpl = new Types.TypeVariableImpl(genericDeclaration, name, bounds);
      TypeVariable<D> typeVariable = (TypeVariable)Reflection.newProxy(TypeVariable.class, new Types.TypeVariableInvocationHandler(typeVariableImpl));
      return typeVariable;
   }

   private static Type[] toArray(Collection<Type> types) {
      return (Type[])types.toArray(new Type[0]);
   }

   private static Iterable<Type> filterUpperBounds(Iterable<Type> bounds) {
      return Iterables.filter(bounds, Predicates.not(Predicates.equalTo(Object.class)));
   }

   private static void disallowPrimitiveType(Type[] types, String usedAs) {
      Type[] var2 = types;
      int var3 = types.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Type type = var2[var4];
         if (type instanceof Class) {
            Class<?> cls = (Class)type;
            Preconditions.checkArgument(!cls.isPrimitive(), "Primitive type '%s' used as %s", cls, usedAs);
         }
      }

   }

   static Class<?> getArrayClass(Class<?> componentType) {
      return Array.newInstance(componentType, 0).getClass();
   }

   private Types() {
   }

   static final class NativeTypeVariableEquals<X> {
      static final boolean NATIVE_TYPE_VARIABLE_ONLY = !Types.NativeTypeVariableEquals.class.getTypeParameters()[0].equals(Types.newArtificialTypeVariable(Types.NativeTypeVariableEquals.class, "X"));
   }

   static enum JavaVersion {
      JAVA6 {
         GenericArrayType newArrayType(Type componentType) {
            return new Types.GenericArrayTypeImpl(componentType);
         }

         Type usedInGenericType(Type type) {
            Preconditions.checkNotNull(type);
            if (type instanceof Class) {
               Class<?> cls = (Class)type;
               if (cls.isArray()) {
                  return new Types.GenericArrayTypeImpl(cls.getComponentType());
               }
            }

            return type;
         }
      },
      JAVA7 {
         Type newArrayType(Type componentType) {
            return (Type)(componentType instanceof Class ? Types.getArrayClass((Class)componentType) : new Types.GenericArrayTypeImpl(componentType));
         }

         Type usedInGenericType(Type type) {
            return (Type)Preconditions.checkNotNull(type);
         }
      },
      JAVA8 {
         Type newArrayType(Type componentType) {
            return JAVA7.newArrayType(componentType);
         }

         Type usedInGenericType(Type type) {
            return JAVA7.usedInGenericType(type);
         }

         String typeName(Type type) {
            try {
               Method getTypeName = Type.class.getMethod("getTypeName");
               return (String)getTypeName.invoke(type);
            } catch (NoSuchMethodException var3) {
               throw new AssertionError("Type.getTypeName should be available in Java 8");
            } catch (InvocationTargetException var4) {
               throw new RuntimeException(var4);
            } catch (IllegalAccessException var5) {
               throw new RuntimeException(var5);
            }
         }
      },
      JAVA9 {
         Type newArrayType(Type componentType) {
            return JAVA8.newArrayType(componentType);
         }

         Type usedInGenericType(Type type) {
            return JAVA8.usedInGenericType(type);
         }

         String typeName(Type type) {
            return JAVA8.typeName(type);
         }

         boolean jdkTypeDuplicatesOwnerName() {
            return false;
         }
      };

      static final Types.JavaVersion CURRENT;

      private JavaVersion() {
      }

      abstract Type newArrayType(Type var1);

      abstract Type usedInGenericType(Type var1);

      final ImmutableList<Type> usedInGenericType(Type[] types) {
         ImmutableList.Builder<Type> builder = ImmutableList.builder();
         Type[] var3 = types;
         int var4 = types.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Type type = var3[var5];
            builder.add((Object)this.usedInGenericType(type));
         }

         return builder.build();
      }

      String typeName(Type type) {
         return Types.toString(type);
      }

      boolean jdkTypeDuplicatesOwnerName() {
         return true;
      }

      // $FF: synthetic method
      private static Types.JavaVersion[] $values() {
         return new Types.JavaVersion[]{JAVA6, JAVA7, JAVA8, JAVA9};
      }

      // $FF: synthetic method
      JavaVersion(Object x2) {
         this();
      }

      static {
         if (AnnotatedElement.class.isAssignableFrom(TypeVariable.class)) {
            if ((new TypeCapture<Entry<String, int[][]>>() {
            }).capture().toString().contains("java.util.Map.java.util.Map")) {
               CURRENT = JAVA8;
            } else {
               CURRENT = JAVA9;
            }
         } else if ((new TypeCapture<int[]>() {
         }).capture() instanceof Class) {
            CURRENT = JAVA7;
         } else {
            CURRENT = JAVA6;
         }

      }
   }

   static final class WildcardTypeImpl implements WildcardType, Serializable {
      private final ImmutableList<Type> lowerBounds;
      private final ImmutableList<Type> upperBounds;
      private static final long serialVersionUID = 0L;

      WildcardTypeImpl(Type[] lowerBounds, Type[] upperBounds) {
         Types.disallowPrimitiveType(lowerBounds, "lower bound for wildcard");
         Types.disallowPrimitiveType(upperBounds, "upper bound for wildcard");
         this.lowerBounds = Types.JavaVersion.CURRENT.usedInGenericType(lowerBounds);
         this.upperBounds = Types.JavaVersion.CURRENT.usedInGenericType(upperBounds);
      }

      public Type[] getLowerBounds() {
         return Types.toArray(this.lowerBounds);
      }

      public Type[] getUpperBounds() {
         return Types.toArray(this.upperBounds);
      }

      public boolean equals(@CheckForNull Object obj) {
         if (!(obj instanceof WildcardType)) {
            return false;
         } else {
            WildcardType that = (WildcardType)obj;
            return this.lowerBounds.equals(Arrays.asList(that.getLowerBounds())) && this.upperBounds.equals(Arrays.asList(that.getUpperBounds()));
         }
      }

      public int hashCode() {
         return this.lowerBounds.hashCode() ^ this.upperBounds.hashCode();
      }

      public String toString() {
         StringBuilder builder = new StringBuilder("?");
         UnmodifiableIterator var2 = this.lowerBounds.iterator();

         Type upperBound;
         while(var2.hasNext()) {
            upperBound = (Type)var2.next();
            builder.append(" super ").append(Types.JavaVersion.CURRENT.typeName(upperBound));
         }

         Iterator var4 = Types.filterUpperBounds(this.upperBounds).iterator();

         while(var4.hasNext()) {
            upperBound = (Type)var4.next();
            builder.append(" extends ").append(Types.JavaVersion.CURRENT.typeName(upperBound));
         }

         return builder.toString();
      }
   }

   private static final class TypeVariableImpl<D extends GenericDeclaration> {
      private final D genericDeclaration;
      private final String name;
      private final ImmutableList<Type> bounds;

      TypeVariableImpl(D genericDeclaration, String name, Type[] bounds) {
         Types.disallowPrimitiveType(bounds, "bound for type variable");
         this.genericDeclaration = (GenericDeclaration)Preconditions.checkNotNull(genericDeclaration);
         this.name = (String)Preconditions.checkNotNull(name);
         this.bounds = ImmutableList.copyOf((Object[])bounds);
      }

      public Type[] getBounds() {
         return Types.toArray(this.bounds);
      }

      public D getGenericDeclaration() {
         return this.genericDeclaration;
      }

      public String getName() {
         return this.name;
      }

      public String getTypeName() {
         return this.name;
      }

      public String toString() {
         return this.name;
      }

      public int hashCode() {
         return this.genericDeclaration.hashCode() ^ this.name.hashCode();
      }

      public boolean equals(@CheckForNull Object obj) {
         if (Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) {
            if (obj != null && Proxy.isProxyClass(obj.getClass()) && Proxy.getInvocationHandler(obj) instanceof Types.TypeVariableInvocationHandler) {
               Types.TypeVariableInvocationHandler typeVariableInvocationHandler = (Types.TypeVariableInvocationHandler)Proxy.getInvocationHandler(obj);
               Types.TypeVariableImpl<?> that = typeVariableInvocationHandler.typeVariableImpl;
               return this.name.equals(that.getName()) && this.genericDeclaration.equals(that.getGenericDeclaration()) && this.bounds.equals(that.bounds);
            } else {
               return false;
            }
         } else if (!(obj instanceof TypeVariable)) {
            return false;
         } else {
            TypeVariable<?> that = (TypeVariable)obj;
            return this.name.equals(that.getName()) && this.genericDeclaration.equals(that.getGenericDeclaration());
         }
      }
   }

   private static final class TypeVariableInvocationHandler implements InvocationHandler {
      private static final ImmutableMap<String, Method> typeVariableMethods;
      private final Types.TypeVariableImpl<?> typeVariableImpl;

      TypeVariableInvocationHandler(Types.TypeVariableImpl<?> typeVariableImpl) {
         this.typeVariableImpl = typeVariableImpl;
      }

      @CheckForNull
      public Object invoke(Object proxy, Method method, @CheckForNull Object[] args) throws Throwable {
         String methodName = method.getName();
         Method typeVariableMethod = (Method)typeVariableMethods.get(methodName);
         if (typeVariableMethod == null) {
            throw new UnsupportedOperationException(methodName);
         } else {
            try {
               return typeVariableMethod.invoke(this.typeVariableImpl, args);
            } catch (InvocationTargetException var7) {
               throw var7.getCause();
            }
         }
      }

      static {
         ImmutableMap.Builder<String, Method> builder = ImmutableMap.builder();
         Method[] var1 = Types.TypeVariableImpl.class.getMethods();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Method method = var1[var3];
            if (method.getDeclaringClass().equals(Types.TypeVariableImpl.class)) {
               try {
                  method.setAccessible(true);
               } catch (AccessControlException var6) {
               }

               builder.put(method.getName(), method);
            }
         }

         typeVariableMethods = builder.buildKeepingLast();
      }
   }

   private static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
      @CheckForNull
      private final Type ownerType;
      private final ImmutableList<Type> argumentsList;
      private final Class<?> rawType;
      private static final long serialVersionUID = 0L;

      ParameterizedTypeImpl(@CheckForNull Type ownerType, Class<?> rawType, Type[] typeArguments) {
         Preconditions.checkNotNull(rawType);
         Preconditions.checkArgument(typeArguments.length == rawType.getTypeParameters().length);
         Types.disallowPrimitiveType(typeArguments, "type parameter");
         this.ownerType = ownerType;
         this.rawType = rawType;
         this.argumentsList = Types.JavaVersion.CURRENT.usedInGenericType(typeArguments);
      }

      public Type[] getActualTypeArguments() {
         return Types.toArray(this.argumentsList);
      }

      public Type getRawType() {
         return this.rawType;
      }

      @CheckForNull
      public Type getOwnerType() {
         return this.ownerType;
      }

      public String toString() {
         StringBuilder builder = new StringBuilder();
         if (this.ownerType != null && Types.JavaVersion.CURRENT.jdkTypeDuplicatesOwnerName()) {
            builder.append(Types.JavaVersion.CURRENT.typeName(this.ownerType)).append('.');
         }

         StringBuilder var10000 = builder.append(this.rawType.getName()).append('<');
         Joiner var10001 = Types.COMMA_JOINER;
         ImmutableList var10002 = this.argumentsList;
         Types.JavaVersion var10003 = Types.JavaVersion.CURRENT;
         Objects.requireNonNull(var10003);
         return var10000.append(var10001.join(Iterables.transform(var10002, var10003::typeName))).append('>').toString();
      }

      public int hashCode() {
         return (this.ownerType == null ? 0 : this.ownerType.hashCode()) ^ this.argumentsList.hashCode() ^ this.rawType.hashCode();
      }

      public boolean equals(@CheckForNull Object other) {
         if (!(other instanceof ParameterizedType)) {
            return false;
         } else {
            ParameterizedType that = (ParameterizedType)other;
            return this.getRawType().equals(that.getRawType()) && fr.xephi.authme.libs.com.google.common.base.Objects.equal(this.getOwnerType(), that.getOwnerType()) && Arrays.equals(this.getActualTypeArguments(), that.getActualTypeArguments());
         }
      }
   }

   private static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
      private final Type componentType;
      private static final long serialVersionUID = 0L;

      GenericArrayTypeImpl(Type componentType) {
         this.componentType = Types.JavaVersion.CURRENT.usedInGenericType(componentType);
      }

      public Type getGenericComponentType() {
         return this.componentType;
      }

      public String toString() {
         return String.valueOf(Types.toString(this.componentType)).concat("[]");
      }

      public int hashCode() {
         return this.componentType.hashCode();
      }

      public boolean equals(@CheckForNull Object obj) {
         if (obj instanceof GenericArrayType) {
            GenericArrayType that = (GenericArrayType)obj;
            return fr.xephi.authme.libs.com.google.common.base.Objects.equal(this.getGenericComponentType(), that.getGenericComponentType());
         } else {
            return false;
         }
      }
   }

   private static enum ClassOwnership {
      OWNED_BY_ENCLOSING_CLASS {
         @CheckForNull
         Class<?> getOwnerType(Class<?> rawType) {
            return rawType.getEnclosingClass();
         }
      },
      LOCAL_CLASS_HAS_NO_OWNER {
         @CheckForNull
         Class<?> getOwnerType(Class<?> rawType) {
            return rawType.isLocalClass() ? null : rawType.getEnclosingClass();
         }
      };

      static final Types.ClassOwnership JVM_BEHAVIOR = detectJvmBehavior();

      private ClassOwnership() {
      }

      @CheckForNull
      abstract Class<?> getOwnerType(Class<?> var1);

      private static Types.ClassOwnership detectJvmBehavior() {
         class LocalClass<T> {
         }

         Class<?> subclass = (new LocalClass<String>() {
         }).getClass();
         ParameterizedType parameterizedType = (ParameterizedType)Objects.requireNonNull((ParameterizedType)subclass.getGenericSuperclass());
         Types.ClassOwnership[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Types.ClassOwnership behavior = var2[var4];
            if (behavior.getOwnerType(LocalClass.class) == parameterizedType.getOwnerType()) {
               return behavior;
            }
         }

         throw new AssertionError();
      }

      // $FF: synthetic method
      private static Types.ClassOwnership[] $values() {
         return new Types.ClassOwnership[]{OWNED_BY_ENCLOSING_CLASS, LOCAL_CLASS_HAS_NO_OWNER};
      }

      // $FF: synthetic method
      ClassOwnership(Object x2) {
         this();
      }
   }
}
