package ac.grim.grimac.shaded.geantyref;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public class GenericTypeReflector {
   private static final WildcardType UNBOUND_WILDCARD = new WildcardTypeImpl(new Type[]{Object.class}, new Type[0]);
   private static final Map<Class<?>, Class<?>> BOX_TYPES;

   public static Class<?> erase(Type type) {
      if (type instanceof Class) {
         return (Class)type;
      } else if (type instanceof ParameterizedType) {
         return (Class)((ParameterizedType)type).getRawType();
      } else if (type instanceof TypeVariable) {
         TypeVariable<?> tv = (TypeVariable)type;
         return tv.getBounds().length == 0 ? Object.class : erase(tv.getBounds()[0]);
      } else if (type instanceof GenericArrayType) {
         GenericArrayType aType = (GenericArrayType)type;
         return GenericArrayTypeImpl.createArrayType(erase(aType.getGenericComponentType()));
      } else {
         Type[] lowerBounds;
         if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType)type;
            lowerBounds = wildcardType.getLowerBounds();
            return erase(lowerBounds.length > 0 ? lowerBounds[0] : wildcardType.getUpperBounds()[0]);
         } else if (type instanceof CaptureType) {
            CaptureType captureType = (CaptureType)type;
            lowerBounds = captureType.getLowerBounds();
            return erase(lowerBounds.length > 0 ? lowerBounds[0] : captureType.getUpperBounds()[0]);
         } else {
            throw new RuntimeException("not supported: " + type.getClass());
         }
      }
   }

   public static Type box(Type type) {
      Class<?> boxed = (Class)BOX_TYPES.get(type);
      return (Type)(boxed != null ? boxed : type);
   }

   public static boolean isBoxType(Type type) {
      return BOX_TYPES.containsValue(type);
   }

   public static boolean isFullyBound(Type type) {
      if (type instanceof Class) {
         return true;
      } else if (type instanceof ParameterizedType) {
         return Arrays.stream(((ParameterizedType)type).getActualTypeArguments()).allMatch(GenericTypeReflector::isFullyBound);
      } else {
         return type instanceof GenericArrayType ? isFullyBound(((GenericArrayType)type).getGenericComponentType()) : false;
      }
   }

   private static AnnotatedType mapTypeParameters(AnnotatedType toMapType, AnnotatedType typeAndParams) {
      return mapTypeParameters(toMapType, typeAndParams, VarMap.MappingMode.EXACT);
   }

   private static AnnotatedType mapTypeParameters(AnnotatedType toMapType, AnnotatedType typeAndParams, VarMap.MappingMode mappingMode) {
      if (isMissingTypeParameters(typeAndParams.getType())) {
         return new AnnotatedTypeImpl(erase(toMapType.getType()), toMapType.getAnnotations());
      } else {
         VarMap varMap = new VarMap();

         Type owner;
         for(AnnotatedType handlingTypeAndParams = typeAndParams; handlingTypeAndParams instanceof AnnotatedParameterizedType; handlingTypeAndParams = owner == null ? null : annotate(owner)) {
            AnnotatedParameterizedType pType = (AnnotatedParameterizedType)handlingTypeAndParams;
            Class<?> clazz = (Class)((ParameterizedType)pType.getType()).getRawType();
            TypeVariable<?>[] vars = clazz.getTypeParameters();
            varMap.addAll(vars, pType.getAnnotatedActualTypeArguments());
            owner = ((ParameterizedType)pType.getType()).getOwnerType();
         }

         return varMap.map(toMapType, mappingMode);
      }
   }

   public static AnnotatedType resolveExactType(AnnotatedType unresolved, AnnotatedType typeAndParams) {
      return resolveType(unresolved, expandGenerics(typeAndParams), VarMap.MappingMode.EXACT);
   }

   public static Type resolveExactType(Type unresolved, Type typeAndParams) {
      return resolveType(annotate(unresolved), annotate(typeAndParams, true), VarMap.MappingMode.EXACT).getType();
   }

   public static AnnotatedType resolveType(AnnotatedType unresolved, AnnotatedType typeAndParams) {
      return resolveType(unresolved, expandGenerics(typeAndParams), VarMap.MappingMode.ALLOW_INCOMPLETE);
   }

   public static Type resolveType(Type unresolved, Type typeAndParams) {
      return resolveType(annotate(unresolved), annotate(typeAndParams, true), VarMap.MappingMode.ALLOW_INCOMPLETE).getType();
   }

   private static AnnotatedType resolveType(AnnotatedType unresolved, AnnotatedType typeAndParams, VarMap.MappingMode mappingMode) {
      AnnotatedType[] upper;
      if (unresolved instanceof AnnotatedParameterizedType) {
         AnnotatedParameterizedType parameterizedType = (AnnotatedParameterizedType)unresolved;
         upper = (AnnotatedType[])mapArray(parameterizedType.getAnnotatedActualTypeArguments(), (x$0) -> {
            return new AnnotatedType[x$0];
         }, (p) -> {
            return resolveType(p, typeAndParams, mappingMode);
         });
         return replaceParameters(parameterizedType, upper);
      } else if (unresolved instanceof AnnotatedWildcardType) {
         AnnotatedType[] lower = (AnnotatedType[])mapArray(((AnnotatedWildcardType)unresolved).getAnnotatedLowerBounds(), (x$0) -> {
            return new AnnotatedType[x$0];
         }, (b) -> {
            return resolveType(b, typeAndParams, mappingMode);
         });
         upper = (AnnotatedType[])mapArray(((AnnotatedWildcardType)unresolved).getAnnotatedUpperBounds(), (x$0) -> {
            return new AnnotatedType[x$0];
         }, (b) -> {
            return resolveType(b, typeAndParams, mappingMode);
         });
         return new AnnotatedWildcardTypeImpl((WildcardType)unresolved.getType(), unresolved.getAnnotations(), lower, upper);
      } else if (unresolved instanceof AnnotatedTypeVariable) {
         TypeVariable<?> var = (TypeVariable)unresolved.getType();
         if (var.getGenericDeclaration() instanceof Class) {
            AnnotatedType resolved = getTypeParameter(typeAndParams, var);
            if (resolved != null) {
               return updateAnnotations(resolved, unresolved.getAnnotations());
            }
         }

         if (mappingMode.equals(VarMap.MappingMode.ALLOW_INCOMPLETE)) {
            return unresolved;
         } else {
            throw new IllegalArgumentException("Variable " + var.getName() + " is not declared by the given type " + typeAndParams.getType().getTypeName() + " or its super types");
         }
      } else if (unresolved instanceof AnnotatedArrayType) {
         AnnotatedType componentType = resolveType(((AnnotatedArrayType)unresolved).getAnnotatedGenericComponentType(), typeAndParams, mappingMode);
         return new AnnotatedArrayTypeImpl(TypeFactory.arrayOf(componentType.getType()), unresolved.getAnnotations(), componentType);
      } else {
         return unresolved;
      }
   }

   public static boolean isMissingTypeParameters(Type type) {
      if (type instanceof Class) {
         Class<?> clazz = (Class)type;
         if (Modifier.isStatic(clazz.getModifiers())) {
            return clazz.getTypeParameters().length != 0;
         } else {
            for(Class enclosing = clazz; enclosing != null; enclosing = enclosing.getEnclosingClass()) {
               if (enclosing.getTypeParameters().length != 0) {
                  return true;
               }
            }

            return false;
         }
      } else if (type instanceof ParameterizedType) {
         return false;
      } else {
         throw new AssertionError("Unexpected type " + type.getClass());
      }
   }

   public static Type addWildcardParameters(Class<?> clazz) {
      if (clazz.isArray()) {
         return GenericArrayTypeImpl.createArrayType(addWildcardParameters(clazz.getComponentType()));
      } else if (isMissingTypeParameters(clazz)) {
         TypeVariable<?>[] vars = clazz.getTypeParameters();
         Type[] arguments = new Type[vars.length];
         Arrays.fill(arguments, UNBOUND_WILDCARD);
         Type owner = clazz.getDeclaringClass() == null ? null : addWildcardParameters(clazz.getDeclaringClass());
         return new ParameterizedTypeImpl(clazz, arguments, owner);
      } else {
         return clazz;
      }
   }

   public static AnnotatedType getExactSuperType(AnnotatedType subType, Class<?> searchSuperClass) {
      if (subType instanceof AnnotatedParameterizedType || subType.getType() instanceof Class || subType instanceof AnnotatedArrayType) {
         Class<?> superClass = erase(subType.getType());
         if (searchSuperClass == superClass) {
            return subType;
         }

         if (!searchSuperClass.isAssignableFrom(superClass)) {
            return null;
         }
      }

      AnnotatedType[] var7 = getExactDirectSuperTypes(subType);
      int var3 = var7.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         AnnotatedType superType = var7[var4];
         AnnotatedType result = getExactSuperType(superType, searchSuperClass);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public static Type getExactSuperType(Type subType, Class<?> searchSuperClass) {
      AnnotatedType superType = getExactSuperType(annotate(subType), searchSuperClass);
      return superType == null ? null : superType.getType();
   }

   public static AnnotatedType getExactSubType(AnnotatedType superType, Class<?> searchSubClass) {
      Type subType = searchSubClass;
      if (searchSubClass.getTypeParameters().length > 0) {
         subType = TypeFactory.parameterizedClass(searchSubClass, searchSubClass.getTypeParameters());
      }

      AnnotatedType annotatedSubType = annotate((Type)subType);
      Class<?> rawSuperType = erase(superType.getType());
      if (searchSubClass.isArray() && superType instanceof AnnotatedArrayType) {
         return rawSuperType.isAssignableFrom(searchSubClass) ? AnnotatedArrayTypeImpl.createArrayType(getExactSubType(((AnnotatedArrayType)superType).getAnnotatedGenericComponentType(), searchSubClass.getComponentType()), new Annotation[0]) : null;
      } else if (searchSubClass.getTypeParameters().length == 0) {
         return annotatedSubType;
      } else if (!(superType instanceof AnnotatedParameterizedType)) {
         return annotate(searchSubClass);
      } else {
         AnnotatedParameterizedType parameterizedSuperType = (AnnotatedParameterizedType)superType;
         AnnotatedParameterizedType matched = (AnnotatedParameterizedType)getExactSuperType(annotatedSubType, rawSuperType);
         if (matched == null) {
            return null;
         } else {
            VarMap varMap = new VarMap();

            try {
               extractVariables(parameterizedSuperType, matched, searchSubClass, varMap);
               return varMap.map(annotatedSubType);
            } catch (UnresolvedTypeVariableException var9) {
               return annotate(searchSubClass);
            } catch (IllegalArgumentException var10) {
               return null;
            }
         }
      }
   }

   public static Type getExactSubType(Type superType, Class<?> searchSubClass) {
      AnnotatedType resolvedSubtype = getExactSubType(annotate(superType), searchSubClass);
      return resolvedSubtype == null ? null : resolvedSubtype.getType();
   }

   public static AnnotatedType getTypeParameter(AnnotatedType type, TypeVariable<? extends Class<?>> variable) {
      Class<?> clazz = (Class)variable.getGenericDeclaration();
      AnnotatedType superType = getExactSuperType(type, clazz);
      if (superType instanceof AnnotatedParameterizedType) {
         int index = Arrays.asList(clazz.getTypeParameters()).indexOf(variable);
         AnnotatedType resolvedVarType = ((AnnotatedParameterizedType)superType).getAnnotatedActualTypeArguments()[index];
         return updateAnnotations(resolvedVarType, variable.getAnnotations());
      } else {
         return null;
      }
   }

   public static Type getTypeParameter(Type type, TypeVariable<? extends Class<?>> variable) {
      AnnotatedType typeParameter = getTypeParameter(annotate(type), variable);
      return typeParameter == null ? null : typeParameter.getType();
   }

   public static boolean isSuperType(Type superType, Type subType) {
      Type superComponentType;
      if (!(superType instanceof ParameterizedType) && !(superType instanceof Class) && !(superType instanceof GenericArrayType)) {
         if (superType instanceof CaptureType) {
            if (superType.equals(subType)) {
               return true;
            } else {
               Type[] var10 = ((CaptureType)superType).getLowerBounds();
               int var11 = var10.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  superComponentType = var10[var12];
                  if (isSuperType(superComponentType, subType)) {
                     return true;
                  }
               }

               return false;
            }
         } else {
            throw new RuntimeException("Type not supported: " + superType.getClass());
         }
      } else {
         Class<?> superClass = erase(superType);
         AnnotatedType annotatedMappedSubType = getExactSuperType(capture(annotate(subType)), superClass);
         Type mappedSubType = annotatedMappedSubType == null ? null : annotatedMappedSubType.getType();
         if (mappedSubType == null) {
            return false;
         } else if (superType instanceof Class) {
            return true;
         } else if (mappedSubType instanceof Class) {
            return true;
         } else if (mappedSubType instanceof GenericArrayType) {
            superComponentType = getArrayComponentType(superType);

            assert superComponentType != null;

            Type mappedSubComponentType = getArrayComponentType(mappedSubType);

            assert mappedSubComponentType != null;

            return isSuperType(superComponentType, mappedSubComponentType);
         } else {
            assert mappedSubType instanceof ParameterizedType;

            assert superType instanceof ParameterizedType;

            ParameterizedType pMappedSubType = (ParameterizedType)mappedSubType;

            assert pMappedSubType.getRawType() == superClass;

            ParameterizedType pSuperType = (ParameterizedType)superType;
            Type[] superTypeArgs = pSuperType.getActualTypeArguments();
            Type[] subTypeArgs = pMappedSubType.getActualTypeArguments();

            assert superTypeArgs.length == subTypeArgs.length;

            for(int i = 0; i < superTypeArgs.length; ++i) {
               if (!contains(superTypeArgs[i], subTypeArgs[i])) {
                  return false;
               }
            }

            return pSuperType.getOwnerType() == null || isSuperType(pSuperType.getOwnerType(), pMappedSubType.getOwnerType());
         }
      }
   }

   private static boolean isArraySupertype(Type arraySuperType, Type subType) {
      Type superTypeComponent = getArrayComponentType(arraySuperType);

      assert superTypeComponent != null;

      Type subTypeComponent = getArrayComponentType(subType);
      return subTypeComponent == null ? false : isSuperType(superTypeComponent, subTypeComponent);
   }

   public static AnnotatedType getArrayComponentType(AnnotatedType type) {
      if (type.getType() instanceof Class) {
         Class<?> clazz = (Class)type.getType();
         return new AnnotatedTypeImpl(clazz.getComponentType(), clazz.getAnnotations());
      } else if (type instanceof AnnotatedArrayType) {
         AnnotatedArrayType aType = (AnnotatedArrayType)type;
         return aType.getAnnotatedGenericComponentType();
      } else {
         return null;
      }
   }

   public static Type getArrayComponentType(Type type) {
      AnnotatedType componentType = getArrayComponentType(annotate(type));
      return componentType == null ? null : componentType.getType();
   }

   private static boolean contains(Type containingType, Type containedType) {
      if (containingType instanceof WildcardType) {
         WildcardType wContainingType = (WildcardType)containingType;
         Type[] var3 = wContainingType.getUpperBounds();
         int var4 = var3.length;

         int var5;
         Type lowerBound;
         for(var5 = 0; var5 < var4; ++var5) {
            lowerBound = var3[var5];
            if (!isSuperType(lowerBound, containedType)) {
               return false;
            }
         }

         var3 = wContainingType.getLowerBounds();
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            lowerBound = var3[var5];
            if (!isSuperType(containedType, lowerBound)) {
               return false;
            }
         }

         return true;
      } else {
         return containingType.equals(containedType);
      }
   }

   private static void extractVariables(AnnotatedParameterizedType resolvedTyped, AnnotatedParameterizedType unresolvedType, Class<?> declaringClass, VarMap variables) {
      for(int i = 0; i < resolvedTyped.getAnnotatedActualTypeArguments().length; ++i) {
         AnnotatedType unresolvedParam = unresolvedType.getAnnotatedActualTypeArguments()[i];
         AnnotatedType resolvedParam = resolvedTyped.getAnnotatedActualTypeArguments()[i];
         Type var = unresolvedParam.getType();
         if (var instanceof TypeVariable && ((TypeVariable)var).getGenericDeclaration() == declaringClass) {
            variables.add((TypeVariable)var, resolvedParam);
         } else if (unresolvedParam instanceof AnnotatedParameterizedType) {
            if (!(resolvedParam instanceof AnnotatedParameterizedType) || !erase(unresolvedParam.getType()).equals(erase(resolvedParam.getType()))) {
               throw new IllegalArgumentException("The provided types do not match in shape");
            }

            extractVariables((AnnotatedParameterizedType)resolvedParam, (AnnotatedParameterizedType)unresolvedParam, declaringClass, variables);
         }
      }

   }

   private static AnnotatedType[] getExactDirectSuperTypes(AnnotatedType type) {
      if (!(type instanceof AnnotatedParameterizedType) && (type == null || !(type.getType() instanceof Class))) {
         if (type instanceof AnnotatedTypeVariable) {
            AnnotatedTypeVariable tv = (AnnotatedTypeVariable)type;
            return tv.getAnnotatedBounds();
         } else if (type instanceof AnnotatedWildcardType) {
            return ((AnnotatedWildcardType)type).getAnnotatedUpperBounds();
         } else if (type instanceof AnnotatedCaptureTypeImpl) {
            return ((AnnotatedCaptureTypeImpl)type).getAnnotatedUpperBounds();
         } else if (type instanceof AnnotatedArrayType) {
            return getArrayExactDirectSuperTypes(type);
         } else if (type == null) {
            throw new NullPointerException();
         } else {
            throw new RuntimeException("not implemented type: " + type);
         }
      } else {
         Class clazz;
         if (type instanceof AnnotatedParameterizedType) {
            clazz = (Class)((ParameterizedType)type.getType()).getRawType();
         } else {
            clazz = (Class)type.getType();
            if (clazz.isArray()) {
               return getArrayExactDirectSuperTypes(annotate(clazz));
            }
         }

         AnnotatedType[] superInterfaces = clazz.getAnnotatedInterfaces();
         AnnotatedType superClass = clazz.getAnnotatedSuperclass();
         if (superClass == null && superInterfaces.length == 0 && clazz.isInterface()) {
            return new AnnotatedType[]{new AnnotatedTypeImpl(Object.class)};
         } else {
            AnnotatedType[] result;
            int resultIndex;
            if (superClass == null) {
               result = new AnnotatedType[superInterfaces.length];
               resultIndex = 0;
            } else {
               result = new AnnotatedType[superInterfaces.length + 1];
               resultIndex = 1;
               result[0] = mapTypeParameters(superClass, type);
            }

            AnnotatedType[] var6 = superInterfaces;
            int var7 = superInterfaces.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               AnnotatedType superInterface = var6[var8];
               result[resultIndex++] = mapTypeParameters(superInterface, type);
            }

            return result;
         }
      }
   }

   private static AnnotatedType[] getArrayExactDirectSuperTypes(AnnotatedType arrayType) {
      AnnotatedType typeComponent = getArrayComponentType(arrayType);
      AnnotatedType[] result;
      int resultIndex;
      if (typeComponent != null && typeComponent.getType() instanceof Class && ((Class)typeComponent.getType()).isPrimitive()) {
         resultIndex = 0;
         result = new AnnotatedType[3];
      } else {
         AnnotatedType[] componentSupertypes = getExactDirectSuperTypes(typeComponent);
         result = new AnnotatedType[componentSupertypes.length + 3];

         for(resultIndex = 0; resultIndex < componentSupertypes.length; ++resultIndex) {
            result[resultIndex] = AnnotatedArrayTypeImpl.createArrayType(componentSupertypes[resultIndex], new Annotation[0]);
         }
      }

      result[resultIndex++] = new AnnotatedTypeImpl(Object.class);
      result[resultIndex++] = new AnnotatedTypeImpl(Cloneable.class);
      result[resultIndex++] = new AnnotatedTypeImpl(Serializable.class);
      return result;
   }

   public static AnnotatedType getExactReturnType(Method m, AnnotatedType declaringType) {
      return getReturnType(m, declaringType, VarMap.MappingMode.EXACT);
   }

   public static Type getExactReturnType(Method m, Type declaringType) {
      return getExactReturnType(m, annotate(declaringType)).getType();
   }

   public static AnnotatedType getReturnType(Method m, AnnotatedType declaringType) {
      return getReturnType(m, declaringType, VarMap.MappingMode.ALLOW_INCOMPLETE);
   }

   public static Type getReturnType(Method m, Type declaringType) {
      return getReturnType(m, annotate(declaringType)).getType();
   }

   private static AnnotatedType getReturnType(Method m, AnnotatedType declaringType, VarMap.MappingMode mappingMode) {
      AnnotatedType returnType = m.getAnnotatedReturnType();
      AnnotatedType exactDeclaringType = getExactSuperType(capture(declaringType), m.getDeclaringClass());
      if (exactDeclaringType == null) {
         throw new IllegalArgumentException("The method " + m + " is not a member of type " + declaringType);
      } else {
         return mapTypeParameters(returnType, exactDeclaringType, mappingMode);
      }
   }

   public static AnnotatedType getExactFieldType(Field f, AnnotatedType declaringType) {
      return getFieldType(f, declaringType, VarMap.MappingMode.EXACT);
   }

   public static Type getExactFieldType(Field f, Type type) {
      return getExactFieldType(f, annotate(type)).getType();
   }

   public static AnnotatedType getFieldType(Field f, AnnotatedType declaringType) {
      return getFieldType(f, declaringType, VarMap.MappingMode.ALLOW_INCOMPLETE);
   }

   public static Type getFieldType(Field f, Type type) {
      return getFieldType(f, annotate(type)).getType();
   }

   private static AnnotatedType getFieldType(Field f, AnnotatedType declaringType, VarMap.MappingMode mappingMode) {
      AnnotatedType returnType = f.getAnnotatedType();
      AnnotatedType exactDeclaringType = getExactSuperType(capture(declaringType), f.getDeclaringClass());
      if (exactDeclaringType == null) {
         throw new IllegalArgumentException("The field " + f + " is not a member of type " + declaringType);
      } else {
         return mapTypeParameters(returnType, exactDeclaringType, mappingMode);
      }
   }

   public static AnnotatedType[] getExactParameterTypes(Executable exe, AnnotatedType declaringType) {
      return getParameterTypes(exe, declaringType, VarMap.MappingMode.EXACT);
   }

   public static Type[] getExactParameterTypes(Executable exe, Type declaringType) {
      return (Type[])mapArray(getExactParameterTypes(exe, annotate(declaringType)), (x$0) -> {
         return new Type[x$0];
      }, AnnotatedType::getType);
   }

   public static AnnotatedType[] getParameterTypes(Executable exe, AnnotatedType declaringType) {
      return getParameterTypes(exe, declaringType, VarMap.MappingMode.ALLOW_INCOMPLETE);
   }

   public static Type[] getParameterTypes(Executable exe, Type declaringType) {
      return (Type[])mapArray(getParameterTypes(exe, annotate(declaringType)), (x$0) -> {
         return new Type[x$0];
      }, AnnotatedType::getType);
   }

   private static AnnotatedType[] getParameterTypes(Executable exe, AnnotatedType declaringType, VarMap.MappingMode mappingMode) {
      AnnotatedType[] parameterTypes = exe.getAnnotatedParameterTypes();
      AnnotatedType exactDeclaringType = getExactSuperType(capture(declaringType), exe.getDeclaringClass());
      if (exactDeclaringType == null) {
         throw new IllegalArgumentException("The method/constructor " + exe + " is not a member of type " + declaringType);
      } else {
         AnnotatedType[] result = new AnnotatedType[parameterTypes.length];

         for(int i = 0; i < parameterTypes.length; ++i) {
            result[i] = mapTypeParameters(parameterTypes[i], exactDeclaringType, mappingMode);
         }

         return result;
      }
   }

   public static AnnotatedType capture(AnnotatedType type) {
      return (AnnotatedType)(type instanceof AnnotatedParameterizedType ? capture((AnnotatedParameterizedType)type) : type);
   }

   public static AnnotatedParameterizedType capture(AnnotatedParameterizedType type) {
      VarMap varMap = new VarMap();
      List<AnnotatedCaptureTypeImpl> toInit = new ArrayList();
      Class<?> clazz = (Class)((ParameterizedType)type.getType()).getRawType();
      AnnotatedType[] arguments = type.getAnnotatedActualTypeArguments();
      TypeVariable<?>[] vars = clazz.getTypeParameters();
      AnnotatedType[] capturedArguments = new AnnotatedType[arguments.length];

      assert arguments.length == vars.length;

      for(int i = 0; i < arguments.length; ++i) {
         AnnotatedType argument = arguments[i];
         if (argument instanceof AnnotatedWildcardType) {
            AnnotatedCaptureTypeImpl captured = new AnnotatedCaptureTypeImpl((AnnotatedWildcardType)argument, new AnnotatedTypeVariableImpl(vars[i]));
            argument = captured;
            toInit.add(captured);
         }

         capturedArguments[i] = (AnnotatedType)argument;
         varMap.add(vars[i], (AnnotatedType)argument);
      }

      Iterator var11 = toInit.iterator();

      while(var11.hasNext()) {
         AnnotatedCaptureTypeImpl captured = (AnnotatedCaptureTypeImpl)var11.next();
         captured.init(varMap);
      }

      ParameterizedType inner = (ParameterizedType)type.getType();
      AnnotatedType ownerType = inner.getOwnerType() == null ? null : capture(annotate(inner.getOwnerType()));
      Type[] rawArgs = (Type[])mapArray(capturedArguments, (x$0) -> {
         return new Type[x$0];
      }, AnnotatedType::getType);
      ParameterizedType nn = new ParameterizedTypeImpl(clazz, rawArgs, ownerType == null ? null : ownerType.getType());
      return new AnnotatedParameterizedTypeImpl(nn, type.getAnnotations(), capturedArguments);
   }

   public static String getTypeName(Type type) {
      if (type instanceof Class) {
         Class<?> clazz = (Class)type;
         return clazz.isArray() ? getTypeName(clazz.getComponentType()) + "[]" : clazz.getName();
      } else {
         return type.toString();
      }
   }

   public static List<Class<?>> getUpperBoundClassAndInterfaces(Type type) {
      LinkedHashSet<Class<?>> result = new LinkedHashSet();
      buildUpperBoundClassAndInterfaces(type, result);
      return new ArrayList(result);
   }

   private static AnnotatedType annotate(Type type, boolean expandGenerics) {
      return annotate(type, expandGenerics, new HashMap());
   }

   public static AnnotatedType annotate(Type type) {
      return annotate(type, false);
   }

   public static AnnotatedType annotate(Type type, Annotation[] annotations) {
      return updateAnnotations(annotate(type), annotations);
   }

   private static AnnotatedType annotate(Type type, boolean expandGenerics, Map<GenericTypeReflector.CaptureCacheKey, AnnotatedType> cache) {
      AnnotatedType[] lowerBounds;
      if (!(type instanceof ParameterizedType)) {
         if (type instanceof CaptureType) {
            GenericTypeReflector.CaptureCacheKey key = new GenericTypeReflector.CaptureCacheKey((CaptureType)type);
            if (cache.containsKey(key)) {
               return (AnnotatedType)cache.get(key);
            } else {
               CaptureType capture = (CaptureType)type;
               AnnotatedCaptureType annotatedCapture = new AnnotatedCaptureTypeImpl(capture, (AnnotatedWildcardType)annotate(capture.getWildcardType(), expandGenerics, cache), (AnnotatedTypeVariable)annotate(capture.getTypeVariable(), expandGenerics, cache));
               cache.put(new GenericTypeReflector.CaptureCacheKey(capture), annotatedCapture);
               AnnotatedType[] upperBounds = (AnnotatedType[])mapArray(capture.getUpperBounds(), (x$0) -> {
                  return new AnnotatedType[x$0];
               }, (bound) -> {
                  return annotate(bound, expandGenerics, cache);
               });
               annotatedCapture.setAnnotatedUpperBounds(upperBounds);
               return annotatedCapture;
            }
         } else if (type instanceof WildcardType) {
            WildcardType wildcard = (WildcardType)type;
            lowerBounds = (AnnotatedType[])mapArray(wildcard.getLowerBounds(), (x$0) -> {
               return new AnnotatedType[x$0];
            }, (bound) -> {
               return annotate(bound, expandGenerics, cache);
            });
            AnnotatedType[] upperBounds = (AnnotatedType[])mapArray(wildcard.getUpperBounds(), (x$0) -> {
               return new AnnotatedType[x$0];
            }, (bound) -> {
               return annotate(bound, expandGenerics, cache);
            });
            return new AnnotatedWildcardTypeImpl(wildcard, erase(type).getAnnotations(), lowerBounds, upperBounds);
         } else if (type instanceof TypeVariable) {
            return new AnnotatedTypeVariableImpl((TypeVariable)type);
         } else if (type instanceof GenericArrayType) {
            GenericArrayType genArray = (GenericArrayType)type;
            return new AnnotatedArrayTypeImpl(genArray, new Annotation[0], annotate(genArray.getGenericComponentType(), expandGenerics, cache));
         } else if (type instanceof Class) {
            Class<?> clazz = (Class)type;
            if (clazz.isArray()) {
               Class<?> componentClass = clazz.getComponentType();
               return AnnotatedArrayTypeImpl.createArrayType(new AnnotatedTypeImpl(componentClass, componentClass.getAnnotations()), new Annotation[0]);
            } else {
               return (AnnotatedType)(clazz.getTypeParameters().length > 0 && expandGenerics ? expandClassGenerics(clazz) : new AnnotatedTypeImpl(clazz, clazz.getAnnotations()));
            }
         } else {
            throw new IllegalArgumentException("Unrecognized type: " + type.getTypeName());
         }
      } else {
         ParameterizedType parameterized = (ParameterizedType)type;
         lowerBounds = new AnnotatedType[parameterized.getActualTypeArguments().length];

         for(int i = 0; i < lowerBounds.length; ++i) {
            AnnotatedType param = annotate(parameterized.getActualTypeArguments()[i], expandGenerics, cache);
            lowerBounds[i] = updateAnnotations(param, erase(type).getTypeParameters()[i].getAnnotations());
         }

         return new AnnotatedParameterizedTypeImpl(parameterized, erase(type).getAnnotations(), lowerBounds);
      }
   }

   public static <T extends AnnotatedType> T replaceAnnotations(T original, Annotation[] annotations) {
      if (original instanceof AnnotatedParameterizedType) {
         return new AnnotatedParameterizedTypeImpl((ParameterizedType)original.getType(), annotations, ((AnnotatedParameterizedType)original).getAnnotatedActualTypeArguments());
      } else if (original instanceof AnnotatedCaptureType) {
         AnnotatedCaptureTypeImpl capture = (AnnotatedCaptureTypeImpl)original;
         return capture.setAnnotations(annotations);
      } else if (original instanceof AnnotatedWildcardType) {
         return new AnnotatedWildcardTypeImpl((WildcardType)original.getType(), annotations, ((AnnotatedWildcardType)original).getAnnotatedLowerBounds(), ((AnnotatedWildcardType)original).getAnnotatedUpperBounds());
      } else if (original instanceof AnnotatedTypeVariable) {
         return new AnnotatedTypeVariableImpl((TypeVariable)original.getType(), annotations);
      } else {
         return (AnnotatedType)(original instanceof AnnotatedArrayType ? new AnnotatedArrayTypeImpl(original.getType(), annotations, ((AnnotatedArrayType)original).getAnnotatedGenericComponentType()) : new AnnotatedTypeImpl(original.getType(), annotations));
      }
   }

   public static <T extends AnnotatedType> T updateAnnotations(T original, Annotation[] annotations) {
      return annotations != null && annotations.length != 0 && !Arrays.equals(original.getAnnotations(), annotations) ? replaceAnnotations(original, merge(original.getAnnotations(), annotations)) : original;
   }

   public static <T extends AnnotatedType> T mergeAnnotations(T t1, T t2) {
      Annotation[] merged = merge(t1.getAnnotations(), t2.getAnnotations());
      AnnotatedType[] l1;
      AnnotatedType[] l2;
      AnnotatedType[] lowerBounds;
      int i;
      if (t1 instanceof AnnotatedParameterizedType) {
         l1 = ((AnnotatedParameterizedType)t1).getAnnotatedActualTypeArguments();
         l2 = ((AnnotatedParameterizedType)t2).getAnnotatedActualTypeArguments();
         lowerBounds = new AnnotatedType[l1.length];

         for(i = 0; i < l1.length; ++i) {
            lowerBounds[i] = mergeAnnotations(l1[i], l2[i]);
         }

         return new AnnotatedParameterizedTypeImpl((ParameterizedType)t1.getType(), merged, lowerBounds);
      } else if (!(t1 instanceof AnnotatedWildcardType)) {
         if (t1 instanceof AnnotatedTypeVariable) {
            return new AnnotatedTypeVariableImpl((TypeVariable)t1.getType(), merged);
         } else if (t1 instanceof AnnotatedArrayType) {
            AnnotatedType componentType = mergeAnnotations(((AnnotatedArrayType)t1).getAnnotatedGenericComponentType(), ((AnnotatedArrayType)t2).getAnnotatedGenericComponentType());
            return new AnnotatedArrayTypeImpl(t1.getType(), merged, componentType);
         } else {
            return new AnnotatedTypeImpl(t1.getType(), merged);
         }
      } else {
         l1 = ((AnnotatedWildcardType)t1).getAnnotatedLowerBounds();
         l2 = ((AnnotatedWildcardType)t2).getAnnotatedLowerBounds();
         lowerBounds = new AnnotatedType[l1.length];

         for(i = 0; i < l1.length; ++i) {
            lowerBounds[i] = mergeAnnotations(l1[i], l2[i]);
         }

         AnnotatedType[] u1 = ((AnnotatedWildcardType)t1).getAnnotatedUpperBounds();
         AnnotatedType[] u2 = ((AnnotatedWildcardType)t2).getAnnotatedUpperBounds();
         AnnotatedType[] upperBounds = new AnnotatedType[u1.length];

         for(int i = 0; i < u1.length; ++i) {
            upperBounds[i] = mergeAnnotations(u1[i], u2[i]);
         }

         return new AnnotatedWildcardTypeImpl((WildcardType)t1.getType(), merged, lowerBounds, upperBounds);
      }
   }

   public static AnnotatedParameterizedType replaceParameters(AnnotatedParameterizedType type, AnnotatedType[] typeParameters) {
      return replaceParameters(type, new Annotation[0], typeParameters);
   }

   private static AnnotatedParameterizedType replaceParameters(AnnotatedParameterizedType type, Annotation[] annotations, AnnotatedType[] typeParameters) {
      Type[] rawArguments = (Type[])mapArray(typeParameters, (x$0) -> {
         return new Type[x$0];
      }, AnnotatedType::getType);
      ParameterizedType inner = (ParameterizedType)type.getType();
      ParameterizedType rawType = (ParameterizedType)TypeFactory.parameterizedInnerClass(inner.getOwnerType(), erase(inner), rawArguments);
      return new AnnotatedParameterizedTypeImpl(rawType, merge(type.getAnnotations(), annotations), typeParameters);
   }

   public static <T extends AnnotatedType> T toCanonical(T type) {
      return toCanonical(type, Function.identity());
   }

   public static <T extends AnnotatedType> T toCanonicalBoxed(T type) {
      return toCanonical(type, GenericTypeReflector::box);
   }

   private static <T extends AnnotatedType> T toCanonical(T type, final Function<Type, Type> leafTransformer) {
      return transform(type, new TypeVisitor() {
         protected AnnotatedType visitClass(AnnotatedType type) {
            Annotation[] annotations = type.getAnnotations();
            Class<?> raw = (Class)type.getType();
            annotations = GenericTypeReflector.merge(annotations, raw.getAnnotations());
            return new AnnotatedTypeImpl((Type)leafTransformer.apply(type.getType()), annotations);
         }

         protected AnnotatedType visitArray(AnnotatedArrayType type) {
            return new AnnotatedArrayTypeImpl((Type)leafTransformer.apply(type.getType()), type.getAnnotations(), GenericTypeReflector.transform(type.getAnnotatedGenericComponentType(), this));
         }

         protected AnnotatedType visitParameterizedType(AnnotatedParameterizedType type) {
            AnnotatedType[] params = (AnnotatedType[])Arrays.stream(type.getAnnotatedActualTypeArguments()).map((param) -> {
               return GenericTypeReflector.transform(param, this);
            }).toArray((x$0) -> {
               return new AnnotatedType[x$0];
            });
            Class<?> raw = (Class)((ParameterizedType)type.getType()).getRawType();
            return GenericTypeReflector.replaceParameters(type, raw.getAnnotations(), params);
         }
      });
   }

   private static AnnotatedType expandGenerics(AnnotatedType type) {
      return transform(type, new TypeVisitor() {
         public AnnotatedType visitClass(AnnotatedType type) {
            Class<?> clazz = (Class)type.getType();
            return (AnnotatedType)(clazz.getTypeParameters().length > 0 ? GenericTypeReflector.expandClassGenerics(clazz) : type);
         }
      });
   }

   public static AnnotatedType transform(AnnotatedType type, TypeVisitor visitor) {
      if (type instanceof AnnotatedParameterizedType) {
         return visitor.visitParameterizedType((AnnotatedParameterizedType)type);
      } else if (type instanceof AnnotatedWildcardType) {
         return visitor.visitWildcardType((AnnotatedWildcardType)type);
      } else if (type instanceof AnnotatedTypeVariable) {
         return visitor.visitVariable((AnnotatedTypeVariable)type);
      } else if (type instanceof AnnotatedArrayType) {
         return visitor.visitArray((AnnotatedArrayType)type);
      } else if (type instanceof AnnotatedCaptureType) {
         return visitor.visitCaptureType((AnnotatedCaptureType)type);
      } else {
         return type.getType() instanceof Class ? visitor.visitClass(type) : visitor.visitUnmatched(type);
      }
   }

   public static AnnotatedType reduceBounded(AnnotatedType type) {
      AnnotatedType capture = capture(type);
      return transform(capture, new TypeVisitor() {
         protected AnnotatedType visitVariable(AnnotatedTypeVariable type) {
            return GenericTypeReflector.updateAnnotations(GenericTypeReflector.transform(type.getAnnotatedBounds()[0], this), type.getAnnotations());
         }

         protected AnnotatedType visitWildcardType(AnnotatedWildcardType type) {
            return type.getAnnotatedLowerBounds().length > 0 ? GenericTypeReflector.updateAnnotations(GenericTypeReflector.transform(type.getAnnotatedLowerBounds()[0], this), type.getAnnotations()) : GenericTypeReflector.updateAnnotations(GenericTypeReflector.transform(type.getAnnotatedUpperBounds()[0], this), type.getAnnotations());
         }

         protected AnnotatedType visitCaptureType(AnnotatedCaptureType type) {
            AnnotatedType bound = type.getAnnotatedLowerBounds().length > 0 ? type.getAnnotatedLowerBounds()[0] : type.getAnnotatedUpperBounds()[0];
            if (bound instanceof AnnotatedParameterizedType) {
               AnnotatedType[] typeArguments = ((AnnotatedParameterizedType)bound).getAnnotatedActualTypeArguments();
               AnnotatedType[] var4 = typeArguments;
               int var5 = typeArguments.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  AnnotatedType typeArgument = var4[var6];
                  if (type.equals(typeArgument)) {
                     ParameterizedType parameterizedType = (ParameterizedType)bound.getType();
                     return GenericTypeReflector.annotate(parameterizedType.getRawType(), GenericTypeReflector.merge(type.getAnnotations(), bound.getAnnotations()));
                  }
               }
            }

            return GenericTypeReflector.updateAnnotations(GenericTypeReflector.transform(bound, this), type.getAnnotations());
         }
      });
   }

   private static AnnotatedParameterizedType expandClassGenerics(Class<?> type) {
      ParameterizedType inner = new ParameterizedTypeImpl(type, type.getTypeParameters(), type.getDeclaringClass());
      AnnotatedType[] params = (AnnotatedType[])mapArray(type.getTypeParameters(), (x$0) -> {
         return new AnnotatedType[x$0];
      }, GenericTypeReflector::annotate);
      return new AnnotatedParameterizedTypeImpl(inner, type.getAnnotations(), params);
   }

   public static Annotation[] merge(Annotation[]... annotations) {
      Set<Annotation> result = new LinkedHashSet();
      Annotation[][] var2 = annotations;
      int var3 = annotations.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Annotation[] annos = var2[var4];
         Annotation[] var6 = annos;
         int var7 = annos.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Annotation anno = var6[var8];
            result.add(anno);
         }
      }

      return (Annotation[])result.toArray(new Annotation[0]);
   }

   static boolean typeArraysEqual(AnnotatedType[] t1, AnnotatedType[] t2) {
      if (t1 == t2) {
         return true;
      } else if (t1 == null) {
         return false;
      } else if (t2 == null) {
         return false;
      } else if (t1.length != t2.length) {
         return false;
      } else {
         for(int i = 0; i < t1.length; ++i) {
            if (!t1[i].getType().equals(t2[i].getType()) || !Arrays.equals(t1[i].getAnnotations(), t2[i].getAnnotations())) {
               return false;
            }
         }

         return true;
      }
   }

   public static int hashCode(AnnotatedType... types) {
      int typeHash = Arrays.stream(types).mapToInt((t) -> {
         return t.getType().hashCode();
      }).reduce(0, (x, y) -> {
         return 127 * x ^ y;
      });
      int annotationHash = hashCode(Arrays.stream(types).flatMap((t) -> {
         return Arrays.stream(t.getAnnotations());
      }));
      return 31 * typeHash ^ annotationHash;
   }

   static int hashCode(Stream<Annotation> annotations) {
      return annotations.mapToInt((a) -> {
         return 31 * a.annotationType().hashCode() ^ a.hashCode();
      }).reduce(0, (x, y) -> {
         return 127 * x ^ y;
      });
   }

   public static boolean equals(AnnotatedType t1, AnnotatedType t2) {
      Objects.requireNonNull(t1);
      Objects.requireNonNull(t2);
      t1 = toCanonical(t1);
      t2 = toCanonical(t2);
      return t1.equals(t2);
   }

   private static void buildUpperBoundClassAndInterfaces(Type type, Set<Class<?>> result) {
      if (!(type instanceof ParameterizedType) && !(type instanceof Class)) {
         AnnotatedType[] var2 = getExactDirectSuperTypes(annotate(type));
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            AnnotatedType superType = var2[var4];
            buildUpperBoundClassAndInterfaces(superType.getType(), result);
         }

      } else {
         result.add(erase(type));
      }
   }

   private static <I, O> O[] mapArray(I[] array, IntFunction<O[]> resultCtor, Function<I, O> mapper) {
      O[] result = (Object[])resultCtor.apply(array.length);

      for(int i = 0; i < array.length; ++i) {
         result[i] = mapper.apply(array[i]);
      }

      return result;
   }

   static {
      Map<Class<?>, Class<?>> boxTypes = new HashMap();
      boxTypes.put(Boolean.TYPE, Boolean.class);
      boxTypes.put(Byte.TYPE, Byte.class);
      boxTypes.put(Character.TYPE, Character.class);
      boxTypes.put(Double.TYPE, Double.class);
      boxTypes.put(Float.TYPE, Float.class);
      boxTypes.put(Integer.TYPE, Integer.class);
      boxTypes.put(Long.TYPE, Long.class);
      boxTypes.put(Short.TYPE, Short.class);
      boxTypes.put(Void.TYPE, Void.class);
      BOX_TYPES = Collections.unmodifiableMap(boxTypes);
   }

   static class CaptureCacheKey {
      CaptureType capture;

      CaptureCacheKey(CaptureType capture) {
         this.capture = capture;
      }

      public int hashCode() {
         return 127 * this.capture.getWildcardType().hashCode() ^ this.capture.getTypeVariable().hashCode();
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof GenericTypeReflector.CaptureCacheKey)) {
            return false;
         } else {
            CaptureType that = ((GenericTypeReflector.CaptureCacheKey)obj).capture;
            return this.capture == that || this.capture.getWildcardType().equals(that.getWildcardType()) && this.capture.getTypeVariable().equals(that.getTypeVariable()) && Arrays.equals(this.capture.getUpperBounds(), that.getUpperBounds());
         }
      }
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
         } else if (!(obj instanceof GenericTypeReflector.AnnotatedCaptureCacheKey)) {
            return false;
         } else {
            GenericTypeReflector.AnnotatedCaptureCacheKey that = (GenericTypeReflector.AnnotatedCaptureCacheKey)obj;
            return this.capture == that.capture || (new GenericTypeReflector.CaptureCacheKey(this.raw)).equals(new GenericTypeReflector.CaptureCacheKey(that.raw)) && Arrays.equals(this.capture.getAnnotations(), that.capture.getAnnotations());
         }
      }
   }
}
