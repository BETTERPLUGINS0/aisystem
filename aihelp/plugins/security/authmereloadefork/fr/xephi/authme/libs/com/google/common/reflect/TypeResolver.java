package fr.xephi.authme.libs.com.google.common.reflect;

import fr.xephi.authme.libs.com.google.common.base.Joiner;
import fr.xephi.authme.libs.com.google.common.base.Objects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableMap;
import fr.xephi.authme.libs.com.google.common.collect.Maps;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
public final class TypeResolver {
   private final TypeResolver.TypeTable typeTable;

   public TypeResolver() {
      this.typeTable = new TypeResolver.TypeTable();
   }

   private TypeResolver(TypeResolver.TypeTable typeTable) {
      this.typeTable = typeTable;
   }

   static TypeResolver covariantly(Type contextType) {
      return (new TypeResolver()).where(TypeResolver.TypeMappingIntrospector.getTypeMappings(contextType));
   }

   static TypeResolver invariantly(Type contextType) {
      Type invariantContext = TypeResolver.WildcardCapturer.INSTANCE.capture(contextType);
      return (new TypeResolver()).where(TypeResolver.TypeMappingIntrospector.getTypeMappings(invariantContext));
   }

   public TypeResolver where(Type formal, Type actual) {
      Map<TypeResolver.TypeVariableKey, Type> mappings = Maps.newHashMap();
      populateTypeMappings(mappings, (Type)Preconditions.checkNotNull(formal), (Type)Preconditions.checkNotNull(actual));
      return this.where(mappings);
   }

   TypeResolver where(Map<TypeResolver.TypeVariableKey, ? extends Type> mappings) {
      return new TypeResolver(this.typeTable.where(mappings));
   }

   private static void populateTypeMappings(final Map<TypeResolver.TypeVariableKey, Type> mappings, Type from, final Type to) {
      if (!from.equals(to)) {
         (new TypeVisitor() {
            void visitTypeVariable(TypeVariable<?> typeVariable) {
               mappings.put(new TypeResolver.TypeVariableKey(typeVariable), to);
            }

            void visitWildcardType(WildcardType fromWildcardType) {
               if (to instanceof WildcardType) {
                  WildcardType toWildcardType = (WildcardType)to;
                  Type[] fromUpperBounds = fromWildcardType.getUpperBounds();
                  Type[] toUpperBounds = toWildcardType.getUpperBounds();
                  Type[] fromLowerBounds = fromWildcardType.getLowerBounds();
                  Type[] toLowerBounds = toWildcardType.getLowerBounds();
                  Preconditions.checkArgument(fromUpperBounds.length == toUpperBounds.length && fromLowerBounds.length == toLowerBounds.length, "Incompatible type: %s vs. %s", fromWildcardType, to);

                  int i;
                  for(i = 0; i < fromUpperBounds.length; ++i) {
                     TypeResolver.populateTypeMappings(mappings, fromUpperBounds[i], toUpperBounds[i]);
                  }

                  for(i = 0; i < fromLowerBounds.length; ++i) {
                     TypeResolver.populateTypeMappings(mappings, fromLowerBounds[i], toLowerBounds[i]);
                  }

               }
            }

            void visitParameterizedType(ParameterizedType fromParameterizedType) {
               if (!(to instanceof WildcardType)) {
                  ParameterizedType toParameterizedType = (ParameterizedType)TypeResolver.expectArgument(ParameterizedType.class, to);
                  if (fromParameterizedType.getOwnerType() != null && toParameterizedType.getOwnerType() != null) {
                     TypeResolver.populateTypeMappings(mappings, fromParameterizedType.getOwnerType(), toParameterizedType.getOwnerType());
                  }

                  Preconditions.checkArgument(fromParameterizedType.getRawType().equals(toParameterizedType.getRawType()), "Inconsistent raw type: %s vs. %s", fromParameterizedType, to);
                  Type[] fromArgs = fromParameterizedType.getActualTypeArguments();
                  Type[] toArgs = toParameterizedType.getActualTypeArguments();
                  Preconditions.checkArgument(fromArgs.length == toArgs.length, "%s not compatible with %s", fromParameterizedType, toParameterizedType);

                  for(int i = 0; i < fromArgs.length; ++i) {
                     TypeResolver.populateTypeMappings(mappings, fromArgs[i], toArgs[i]);
                  }

               }
            }

            void visitGenericArrayType(GenericArrayType fromArrayType) {
               if (!(to instanceof WildcardType)) {
                  Type componentType = Types.getComponentType(to);
                  Preconditions.checkArgument(componentType != null, "%s is not an array type.", (Object)to);
                  TypeResolver.populateTypeMappings(mappings, fromArrayType.getGenericComponentType(), componentType);
               }
            }

            void visitClass(Class<?> fromClass) {
               if (!(to instanceof WildcardType)) {
                  String var2 = String.valueOf(fromClass);
                  String var3 = String.valueOf(to);
                  throw new IllegalArgumentException((new StringBuilder(25 + String.valueOf(var2).length() + String.valueOf(var3).length())).append("No type mapping from ").append(var2).append(" to ").append(var3).toString());
               }
            }
         }).visit(new Type[]{from});
      }
   }

   public Type resolveType(Type type) {
      Preconditions.checkNotNull(type);
      if (type instanceof TypeVariable) {
         return this.typeTable.resolve((TypeVariable)type);
      } else if (type instanceof ParameterizedType) {
         return this.resolveParameterizedType((ParameterizedType)type);
      } else if (type instanceof GenericArrayType) {
         return this.resolveGenericArrayType((GenericArrayType)type);
      } else {
         return (Type)(type instanceof WildcardType ? this.resolveWildcardType((WildcardType)type) : type);
      }
   }

   Type[] resolveTypesInPlace(Type[] types) {
      for(int i = 0; i < types.length; ++i) {
         types[i] = this.resolveType(types[i]);
      }

      return types;
   }

   private Type[] resolveTypes(Type[] types) {
      Type[] result = new Type[types.length];

      for(int i = 0; i < types.length; ++i) {
         result[i] = this.resolveType(types[i]);
      }

      return result;
   }

   private WildcardType resolveWildcardType(WildcardType type) {
      Type[] lowerBounds = type.getLowerBounds();
      Type[] upperBounds = type.getUpperBounds();
      return new Types.WildcardTypeImpl(this.resolveTypes(lowerBounds), this.resolveTypes(upperBounds));
   }

   private Type resolveGenericArrayType(GenericArrayType type) {
      Type componentType = type.getGenericComponentType();
      Type resolvedComponentType = this.resolveType(componentType);
      return Types.newArrayType(resolvedComponentType);
   }

   private ParameterizedType resolveParameterizedType(ParameterizedType type) {
      Type owner = type.getOwnerType();
      Type resolvedOwner = owner == null ? null : this.resolveType(owner);
      Type resolvedRawType = this.resolveType(type.getRawType());
      Type[] args = type.getActualTypeArguments();
      Type[] resolvedArgs = this.resolveTypes(args);
      return Types.newParameterizedTypeWithOwner(resolvedOwner, (Class)resolvedRawType, resolvedArgs);
   }

   private static <T> T expectArgument(Class<T> type, Object arg) {
      try {
         return type.cast(arg);
      } catch (ClassCastException var5) {
         String var3 = String.valueOf(arg);
         String var4 = type.getSimpleName();
         throw new IllegalArgumentException((new StringBuilder(10 + String.valueOf(var3).length() + String.valueOf(var4).length())).append(var3).append(" is not a ").append(var4).toString());
      }
   }

   // $FF: synthetic method
   TypeResolver(TypeResolver.TypeTable x0, Object x1) {
      this(x0);
   }

   static final class TypeVariableKey {
      private final TypeVariable<?> var;

      TypeVariableKey(TypeVariable<?> var) {
         this.var = (TypeVariable)Preconditions.checkNotNull(var);
      }

      public int hashCode() {
         return Objects.hashCode(this.var.getGenericDeclaration(), this.var.getName());
      }

      public boolean equals(@CheckForNull Object obj) {
         if (obj instanceof TypeResolver.TypeVariableKey) {
            TypeResolver.TypeVariableKey that = (TypeResolver.TypeVariableKey)obj;
            return this.equalsTypeVariable(that.var);
         } else {
            return false;
         }
      }

      public String toString() {
         return this.var.toString();
      }

      @CheckForNull
      static TypeResolver.TypeVariableKey forLookup(Type t) {
         return t instanceof TypeVariable ? new TypeResolver.TypeVariableKey((TypeVariable)t) : null;
      }

      boolean equalsType(Type type) {
         return type instanceof TypeVariable ? this.equalsTypeVariable((TypeVariable)type) : false;
      }

      private boolean equalsTypeVariable(TypeVariable<?> that) {
         return this.var.getGenericDeclaration().equals(that.getGenericDeclaration()) && this.var.getName().equals(that.getName());
      }
   }

   private static class WildcardCapturer {
      static final TypeResolver.WildcardCapturer INSTANCE = new TypeResolver.WildcardCapturer();
      private final AtomicInteger id;

      private WildcardCapturer() {
         this(new AtomicInteger());
      }

      private WildcardCapturer(AtomicInteger id) {
         this.id = id;
      }

      final Type capture(Type type) {
         Preconditions.checkNotNull(type);
         if (type instanceof Class) {
            return type;
         } else if (type instanceof TypeVariable) {
            return type;
         } else if (type instanceof GenericArrayType) {
            GenericArrayType arrayType = (GenericArrayType)type;
            return Types.newArrayType(this.notForTypeVariable().capture(arrayType.getGenericComponentType()));
         } else if (!(type instanceof ParameterizedType)) {
            if (type instanceof WildcardType) {
               WildcardType wildcardType = (WildcardType)type;
               Type[] lowerBounds = wildcardType.getLowerBounds();
               return (Type)(lowerBounds.length == 0 ? this.captureAsTypeVariable(wildcardType.getUpperBounds()) : type);
            } else {
               throw new AssertionError("must have been one of the known types");
            }
         } else {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            Class<?> rawType = (Class)parameterizedType.getRawType();
            TypeVariable<?>[] typeVars = rawType.getTypeParameters();
            Type[] typeArgs = parameterizedType.getActualTypeArguments();

            for(int i = 0; i < typeArgs.length; ++i) {
               typeArgs[i] = this.forTypeVariable(typeVars[i]).capture(typeArgs[i]);
            }

            return Types.newParameterizedTypeWithOwner(this.notForTypeVariable().captureNullable(parameterizedType.getOwnerType()), rawType, typeArgs);
         }
      }

      TypeVariable<?> captureAsTypeVariable(Type[] upperBounds) {
         int var3 = this.id.incrementAndGet();
         String var4 = Joiner.on('&').join((Object[])upperBounds);
         String name = (new StringBuilder(33 + String.valueOf(var4).length())).append("capture#").append(var3).append("-of ? extends ").append(var4).toString();
         return Types.newArtificialTypeVariable(TypeResolver.WildcardCapturer.class, name, upperBounds);
      }

      private TypeResolver.WildcardCapturer forTypeVariable(final TypeVariable<?> typeParam) {
         return new TypeResolver.WildcardCapturer(this, this.id) {
            TypeVariable<?> captureAsTypeVariable(Type[] upperBounds) {
               Set<Type> combined = new LinkedHashSet(Arrays.asList(upperBounds));
               combined.addAll(Arrays.asList(typeParam.getBounds()));
               if (combined.size() > 1) {
                  combined.remove(Object.class);
               }

               return super.captureAsTypeVariable((Type[])combined.toArray(new Type[0]));
            }
         };
      }

      private TypeResolver.WildcardCapturer notForTypeVariable() {
         return new TypeResolver.WildcardCapturer(this.id);
      }

      @CheckForNull
      private Type captureNullable(@CheckForNull Type type) {
         return type == null ? null : this.capture(type);
      }

      // $FF: synthetic method
      WildcardCapturer(AtomicInteger x0, Object x1) {
         this(x0);
      }
   }

   private static final class TypeMappingIntrospector extends TypeVisitor {
      private final Map<TypeResolver.TypeVariableKey, Type> mappings = Maps.newHashMap();

      static ImmutableMap<TypeResolver.TypeVariableKey, Type> getTypeMappings(Type contextType) {
         Preconditions.checkNotNull(contextType);
         TypeResolver.TypeMappingIntrospector introspector = new TypeResolver.TypeMappingIntrospector();
         introspector.visit(new Type[]{contextType});
         return ImmutableMap.copyOf(introspector.mappings);
      }

      void visitClass(Class<?> clazz) {
         this.visit(new Type[]{clazz.getGenericSuperclass()});
         this.visit(clazz.getGenericInterfaces());
      }

      void visitParameterizedType(ParameterizedType parameterizedType) {
         Class<?> rawClass = (Class)parameterizedType.getRawType();
         TypeVariable<?>[] vars = rawClass.getTypeParameters();
         Type[] typeArgs = parameterizedType.getActualTypeArguments();
         Preconditions.checkState(vars.length == typeArgs.length);

         for(int i = 0; i < vars.length; ++i) {
            this.map(new TypeResolver.TypeVariableKey(vars[i]), typeArgs[i]);
         }

         this.visit(new Type[]{rawClass});
         this.visit(new Type[]{parameterizedType.getOwnerType()});
      }

      void visitTypeVariable(TypeVariable<?> t) {
         this.visit(t.getBounds());
      }

      void visitWildcardType(WildcardType t) {
         this.visit(t.getUpperBounds());
      }

      private void map(TypeResolver.TypeVariableKey var, Type arg) {
         if (!this.mappings.containsKey(var)) {
            for(Type t = arg; t != null; t = (Type)this.mappings.get(TypeResolver.TypeVariableKey.forLookup(t))) {
               if (var.equalsType(t)) {
                  for(Type x = arg; x != null; x = (Type)this.mappings.remove(TypeResolver.TypeVariableKey.forLookup(x))) {
                  }

                  return;
               }
            }

            this.mappings.put(var, arg);
         }
      }
   }

   private static class TypeTable {
      private final ImmutableMap<TypeResolver.TypeVariableKey, Type> map;

      TypeTable() {
         this.map = ImmutableMap.of();
      }

      private TypeTable(ImmutableMap<TypeResolver.TypeVariableKey, Type> map) {
         this.map = map;
      }

      final TypeResolver.TypeTable where(Map<TypeResolver.TypeVariableKey, ? extends Type> mappings) {
         ImmutableMap.Builder<TypeResolver.TypeVariableKey, Type> builder = ImmutableMap.builder();
         builder.putAll((Map)this.map);
         Iterator var3 = mappings.entrySet().iterator();

         while(var3.hasNext()) {
            Entry<TypeResolver.TypeVariableKey, ? extends Type> mapping = (Entry)var3.next();
            TypeResolver.TypeVariableKey variable = (TypeResolver.TypeVariableKey)mapping.getKey();
            Type type = (Type)mapping.getValue();
            Preconditions.checkArgument(!variable.equalsType(type), "Type variable %s bound to itself", (Object)variable);
            builder.put(variable, type);
         }

         return new TypeResolver.TypeTable(builder.buildOrThrow());
      }

      final Type resolve(final TypeVariable<?> var) {
         TypeResolver.TypeTable guarded = new TypeResolver.TypeTable(this) {
            public Type resolveInternal(TypeVariable<?> intermediateVar, TypeResolver.TypeTable forDependent) {
               return (Type)(intermediateVar.getGenericDeclaration().equals(var.getGenericDeclaration()) ? intermediateVar : TypeTable.this.resolveInternal(intermediateVar, forDependent));
            }
         };
         return this.resolveInternal(var, guarded);
      }

      Type resolveInternal(TypeVariable<?> var, TypeResolver.TypeTable forDependants) {
         Type type = (Type)this.map.get(new TypeResolver.TypeVariableKey(var));
         if (type == null) {
            Type[] bounds = var.getBounds();
            if (bounds.length == 0) {
               return var;
            } else {
               Type[] resolvedBounds = (new TypeResolver(forDependants)).resolveTypes(bounds);
               return Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY && Arrays.equals(bounds, resolvedBounds) ? var : Types.newArtificialTypeVariable(var.getGenericDeclaration(), var.getName(), resolvedBounds);
            }
         } else {
            return (new TypeResolver(forDependants)).resolveType(type);
         }
      }
   }
}
