/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.reflect;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.AppendableJoiner;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.Builder;
import org.apache.commons.lang3.reflect.Typed;

public class TypeUtils {
    private static final AppendableJoiner<Type> AMP_JOINER = AppendableJoiner.builder().setDelimiter(" & ").setElementAppender((appendable, type) -> appendable.append(TypeUtils.toString(type))).get();
    private static final AppendableJoiner<TypeVariable<Class<?>>> CTJ_JOINER = AppendableJoiner.builder().setDelimiter(", ").setElementAppender((appendable, typeVariable) -> appendable.append(TypeUtils.anyToString(typeVariable))).get();
    private static final AppendableJoiner<Object> GT_JOINER = AppendableJoiner.builder().setPrefix("<").setSuffix(">").setDelimiter(", ").setElementAppender((appendable, object) -> appendable.append(TypeUtils.anyToString(object))).get();
    public static final WildcardType WILDCARD_ALL = TypeUtils.wildcardType().withUpperBounds(new Type[]{Object.class}).build();

    private static <T> String anyToString(T t) {
        return t instanceof Type ? TypeUtils.toString((Type)t) : t.toString();
    }

    private static void appendRecursiveTypes(StringBuilder stringBuilder, int[] nArray, Type[] typeArray) {
        for (int i = 0; i < nArray.length; ++i) {
            GT_JOINER.join(stringBuilder, (Object[])new Object[]{typeArray[i].toString()});
        }
        Type[] typeArray2 = ArrayUtils.removeAll(typeArray, nArray);
        if (typeArray2.length > 0) {
            GT_JOINER.join(stringBuilder, (Object[])typeArray2);
        }
    }

    private static <T> String classToString(Class<T> clazz) {
        if (clazz.isArray()) {
            return TypeUtils.toString(clazz.getComponentType()) + "[]";
        }
        if (TypeUtils.isCyclical(clazz)) {
            return clazz.getSimpleName() + "(cycle)";
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (clazz.getEnclosingClass() != null) {
            stringBuilder.append(TypeUtils.classToString(clazz.getEnclosingClass())).append('.').append(clazz.getSimpleName());
        } else {
            stringBuilder.append(clazz.getName());
        }
        if (clazz.getTypeParameters().length > 0) {
            CTJ_JOINER.join(stringBuilder, clazz.getTypeParameters());
        }
        return stringBuilder.toString();
    }

    public static boolean containsTypeVariables(Type type) {
        if (type instanceof TypeVariable) {
            return true;
        }
        if (type instanceof Class) {
            return ((Class)type).getTypeParameters().length > 0;
        }
        if (type instanceof ParameterizedType) {
            for (Type type2 : ((ParameterizedType)type).getActualTypeArguments()) {
                if (!TypeUtils.containsTypeVariables(type2)) continue;
                return true;
            }
            return false;
        }
        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType)type;
            return TypeUtils.containsTypeVariables(TypeUtils.getImplicitLowerBounds(wildcardType)[0]) || TypeUtils.containsTypeVariables(TypeUtils.getImplicitUpperBounds(wildcardType)[0]);
        }
        if (type instanceof GenericArrayType) {
            return TypeUtils.containsTypeVariables(((GenericArrayType)type).getGenericComponentType());
        }
        return false;
    }

    private static boolean containsVariableTypeSameParametrizedTypeBound(TypeVariable<?> typeVariable, ParameterizedType parameterizedType) {
        return ArrayUtils.contains(typeVariable.getBounds(), parameterizedType);
    }

    public static Map<TypeVariable<?>, Type> determineTypeArguments(Class<?> clazz, ParameterizedType parameterizedType) {
        Objects.requireNonNull(clazz, "cls");
        Objects.requireNonNull(parameterizedType, "superParameterizedType");
        Class<?> clazz2 = TypeUtils.getRawType(parameterizedType);
        if (!TypeUtils.isAssignable(clazz, clazz2)) {
            return null;
        }
        if (clazz.equals(clazz2)) {
            return TypeUtils.getTypeArguments(parameterizedType, clazz2, null);
        }
        Type type = TypeUtils.getClosestParentType(clazz, clazz2);
        if (type instanceof Class) {
            return TypeUtils.determineTypeArguments((Class)type, parameterizedType);
        }
        ParameterizedType parameterizedType2 = (ParameterizedType)type;
        Class<?> clazz3 = TypeUtils.getRawType(parameterizedType2);
        Map<TypeVariable<?>, Type> map = TypeUtils.determineTypeArguments(clazz3, parameterizedType);
        TypeUtils.mapTypeVariablesToArguments(clazz, parameterizedType2, map);
        return map;
    }

    private static boolean equals(GenericArrayType genericArrayType, Type type) {
        return type instanceof GenericArrayType && TypeUtils.equals(genericArrayType.getGenericComponentType(), ((GenericArrayType)type).getGenericComponentType());
    }

    private static boolean equals(ParameterizedType parameterizedType, Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType2 = (ParameterizedType)type;
            if (TypeUtils.equals(parameterizedType.getRawType(), parameterizedType2.getRawType()) && TypeUtils.equals(parameterizedType.getOwnerType(), parameterizedType2.getOwnerType())) {
                return TypeUtils.equals(parameterizedType.getActualTypeArguments(), parameterizedType2.getActualTypeArguments());
            }
        }
        return false;
    }

    public static boolean equals(Type type, Type type2) {
        if (Objects.equals(type, type2)) {
            return true;
        }
        if (type instanceof ParameterizedType) {
            return TypeUtils.equals((ParameterizedType)type, type2);
        }
        if (type instanceof GenericArrayType) {
            return TypeUtils.equals((GenericArrayType)type, type2);
        }
        if (type instanceof WildcardType) {
            return TypeUtils.equals((WildcardType)type, type2);
        }
        return false;
    }

    private static boolean equals(Type[] typeArray, Type[] typeArray2) {
        if (typeArray.length == typeArray2.length) {
            for (int i = 0; i < typeArray.length; ++i) {
                if (TypeUtils.equals(typeArray[i], typeArray2[i])) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    private static boolean equals(WildcardType wildcardType, Type type) {
        if (type instanceof WildcardType) {
            WildcardType wildcardType2 = (WildcardType)type;
            return TypeUtils.equals(TypeUtils.getImplicitLowerBounds(wildcardType), TypeUtils.getImplicitLowerBounds(wildcardType2)) && TypeUtils.equals(TypeUtils.getImplicitUpperBounds(wildcardType), TypeUtils.getImplicitUpperBounds(wildcardType2));
        }
        return false;
    }

    private static Type[] extractTypeArgumentsFrom(Map<TypeVariable<?>, Type> map, TypeVariable<?>[] typeVariableArray) {
        Type[] typeArray = new Type[typeVariableArray.length];
        int n = 0;
        for (TypeVariable<?> typeVariable : typeVariableArray) {
            Validate.isTrue(map.containsKey(typeVariable), "missing argument mapping for %s", TypeUtils.toString(typeVariable));
            typeArray[n++] = map.get(typeVariable);
        }
        return typeArray;
    }

    private static int[] findRecursiveTypes(ParameterizedType parameterizedType) {
        Type[] typeArray = Arrays.copyOf(parameterizedType.getActualTypeArguments(), parameterizedType.getActualTypeArguments().length);
        int[] nArray = new int[]{};
        for (int i = 0; i < typeArray.length; ++i) {
            if (!(typeArray[i] instanceof TypeVariable) || !TypeUtils.containsVariableTypeSameParametrizedTypeBound((TypeVariable)typeArray[i], parameterizedType)) continue;
            nArray = ArrayUtils.add(nArray, i);
        }
        return nArray;
    }

    public static GenericArrayType genericArrayType(Type type) {
        return new GenericArrayTypeImpl(Objects.requireNonNull(type, "componentType"));
    }

    private static String genericArrayTypeToString(GenericArrayType genericArrayType) {
        return String.format("%s[]", TypeUtils.toString(genericArrayType.getGenericComponentType()));
    }

    public static Type getArrayComponentType(Type type) {
        if (type instanceof Class) {
            Class clazz = (Class)type;
            return clazz.isArray() ? clazz.getComponentType() : null;
        }
        if (type instanceof GenericArrayType) {
            return ((GenericArrayType)type).getGenericComponentType();
        }
        return null;
    }

    private static Type getClosestParentType(Class<?> clazz, Class<?> clazz2) {
        if (clazz2.isInterface()) {
            Type[] typeArray = clazz.getGenericInterfaces();
            Type type = null;
            for (Type type2 : typeArray) {
                Class clazz3;
                if (type2 instanceof ParameterizedType) {
                    clazz3 = TypeUtils.getRawType((ParameterizedType)type2);
                } else if (type2 instanceof Class) {
                    clazz3 = (Class)type2;
                } else {
                    throw new IllegalStateException("Unexpected generic interface type found: " + type2);
                }
                if (!TypeUtils.isAssignable((Type)clazz3, clazz2) || !TypeUtils.isAssignable(type, (Type)clazz3)) continue;
                type = type2;
            }
            if (type != null) {
                return type;
            }
        }
        return clazz.getGenericSuperclass();
    }

    public static Type[] getImplicitBounds(TypeVariable<?> typeVariable) {
        Type[] typeArray;
        Objects.requireNonNull(typeVariable, "typeVariable");
        Type[] typeArray2 = typeVariable.getBounds();
        if (typeArray2.length == 0) {
            Type[] typeArray3 = new Type[1];
            typeArray = typeArray3;
            typeArray3[0] = Object.class;
        } else {
            typeArray = TypeUtils.normalizeUpperBounds(typeArray2);
        }
        return typeArray;
    }

    public static Type[] getImplicitLowerBounds(WildcardType wildcardType) {
        Type[] typeArray;
        Objects.requireNonNull(wildcardType, "wildcardType");
        Type[] typeArray2 = wildcardType.getLowerBounds();
        if (typeArray2.length == 0) {
            Type[] typeArray3 = new Type[1];
            typeArray = typeArray3;
            typeArray3[0] = null;
        } else {
            typeArray = typeArray2;
        }
        return typeArray;
    }

    public static Type[] getImplicitUpperBounds(WildcardType wildcardType) {
        Type[] typeArray;
        Objects.requireNonNull(wildcardType, "wildcardType");
        Type[] typeArray2 = wildcardType.getUpperBounds();
        if (typeArray2.length == 0) {
            Type[] typeArray3 = new Type[1];
            typeArray = typeArray3;
            typeArray3[0] = Object.class;
        } else {
            typeArray = TypeUtils.normalizeUpperBounds(typeArray2);
        }
        return typeArray;
    }

    private static Class<?> getRawType(ParameterizedType parameterizedType) {
        Type type = parameterizedType.getRawType();
        if (!(type instanceof Class)) {
            throw new IllegalStateException("Wait... What!? Type of rawType: " + type);
        }
        return (Class)type;
    }

    public static Class<?> getRawType(Type type, Type type2) {
        if (type instanceof Class) {
            return (Class)type;
        }
        if (type instanceof ParameterizedType) {
            return TypeUtils.getRawType((ParameterizedType)type);
        }
        if (type instanceof TypeVariable) {
            if (type2 == null) {
                return null;
            }
            Object d = ((TypeVariable)type).getGenericDeclaration();
            if (!(d instanceof Class)) {
                return null;
            }
            Map<TypeVariable<?>, Type> map = TypeUtils.getTypeArguments(type2, (Class)d);
            if (map == null) {
                return null;
            }
            Type type3 = map.get(type);
            if (type3 == null) {
                return null;
            }
            return TypeUtils.getRawType(type3, type2);
        }
        if (type instanceof GenericArrayType) {
            Class<?> clazz = TypeUtils.getRawType(((GenericArrayType)type).getGenericComponentType(), type2);
            return clazz != null ? Array.newInstance(clazz, 0).getClass() : null;
        }
        if (type instanceof WildcardType) {
            return null;
        }
        throw new IllegalArgumentException("unknown type: " + type);
    }

    private static Map<TypeVariable<?>, Type> getTypeArguments(Class<?> clazz, Class<?> clazz2, Map<TypeVariable<?>, Type> map) {
        HashMap hashMap;
        if (!TypeUtils.isAssignable(clazz, clazz2)) {
            return null;
        }
        if (clazz.isPrimitive()) {
            if (clazz2.isPrimitive()) {
                return new HashMap();
            }
            clazz = ClassUtils.primitiveToWrapper(clazz);
        }
        HashMap hashMap2 = hashMap = map == null ? new HashMap() : new HashMap(map);
        if (clazz2.equals(clazz)) {
            return hashMap;
        }
        return TypeUtils.getTypeArguments(TypeUtils.getClosestParentType(clazz, clazz2), clazz2, hashMap);
    }

    public static Map<TypeVariable<?>, Type> getTypeArguments(ParameterizedType parameterizedType) {
        return TypeUtils.getTypeArguments(parameterizedType, TypeUtils.getRawType(parameterizedType), null);
    }

    private static Map<TypeVariable<?>, Type> getTypeArguments(ParameterizedType parameterizedType, Class<?> clazz, Map<TypeVariable<?>, Type> map) {
        HashMap<TypeVariable<?>, Type> hashMap;
        Type[] typeArray;
        Class<?> clazz2 = TypeUtils.getRawType(parameterizedType);
        if (!TypeUtils.isAssignable(clazz2, clazz)) {
            return null;
        }
        Type type = parameterizedType.getOwnerType();
        if (type instanceof ParameterizedType) {
            typeArray = (Type[])type;
            hashMap = TypeUtils.getTypeArguments((ParameterizedType)typeArray, TypeUtils.getRawType((ParameterizedType)typeArray), map);
        } else {
            hashMap = map == null ? new HashMap() : new HashMap(map);
        }
        typeArray = parameterizedType.getActualTypeArguments();
        TypeVariable<Class<?>>[] typeVariableArray = clazz2.getTypeParameters();
        for (int i = 0; i < typeVariableArray.length; ++i) {
            Type type2 = typeArray[i];
            hashMap.put(typeVariableArray[i], hashMap.getOrDefault(type2, type2));
        }
        if (clazz.equals(clazz2)) {
            return hashMap;
        }
        return TypeUtils.getTypeArguments(TypeUtils.getClosestParentType(clazz2, clazz), clazz, hashMap);
    }

    public static Map<TypeVariable<?>, Type> getTypeArguments(Type type, Class<?> clazz) {
        return TypeUtils.getTypeArguments(type, clazz, null);
    }

    private static Map<TypeVariable<?>, Type> getTypeArguments(Type type, Class<?> clazz, Map<TypeVariable<?>, Type> map) {
        if (type instanceof Class) {
            return TypeUtils.getTypeArguments((Class)type, clazz, map);
        }
        if (type instanceof ParameterizedType) {
            return TypeUtils.getTypeArguments((ParameterizedType)type, clazz, map);
        }
        if (type instanceof GenericArrayType) {
            return TypeUtils.getTypeArguments(((GenericArrayType)type).getGenericComponentType(), clazz.isArray() ? clazz.getComponentType() : clazz, map);
        }
        if (type instanceof WildcardType) {
            for (Type type2 : TypeUtils.getImplicitUpperBounds((WildcardType)type)) {
                if (!TypeUtils.isAssignable(type2, clazz)) continue;
                return TypeUtils.getTypeArguments(type2, clazz, map);
            }
            return null;
        }
        if (type instanceof TypeVariable) {
            for (Type type3 : TypeUtils.getImplicitBounds((TypeVariable)type)) {
                if (!TypeUtils.isAssignable(type3, clazz)) continue;
                return TypeUtils.getTypeArguments(type3, clazz, map);
            }
            return null;
        }
        throw new IllegalStateException("found an unhandled type: " + type);
    }

    public static boolean isArrayType(Type type) {
        return type instanceof GenericArrayType || type instanceof Class && ((Class)type).isArray();
    }

    private static boolean isAssignable(Type type, Class<?> clazz) {
        if (type == null) {
            return clazz == null || !clazz.isPrimitive();
        }
        if (clazz == null) {
            return false;
        }
        if (clazz.equals(type)) {
            return true;
        }
        if (type instanceof Class) {
            return ClassUtils.isAssignable((Class)type, clazz);
        }
        if (type instanceof ParameterizedType) {
            return TypeUtils.isAssignable(TypeUtils.getRawType((ParameterizedType)type), clazz);
        }
        if (type instanceof TypeVariable) {
            for (Type type2 : ((TypeVariable)type).getBounds()) {
                if (!TypeUtils.isAssignable(type2, clazz)) continue;
                return true;
            }
            return false;
        }
        if (type instanceof GenericArrayType) {
            return clazz.equals(Object.class) || clazz.isArray() && TypeUtils.isAssignable(((GenericArrayType)type).getGenericComponentType(), clazz.getComponentType());
        }
        if (type instanceof WildcardType) {
            return false;
        }
        throw new IllegalStateException("found an unhandled type: " + type);
    }

    private static boolean isAssignable(Type type, GenericArrayType genericArrayType, Map<TypeVariable<?>, Type> map) {
        if (type == null) {
            return true;
        }
        if (genericArrayType == null) {
            return false;
        }
        if (genericArrayType.equals(type)) {
            return true;
        }
        Type type2 = genericArrayType.getGenericComponentType();
        if (type instanceof Class) {
            Class clazz = (Class)type;
            return clazz.isArray() && TypeUtils.isAssignable(clazz.getComponentType(), type2, map);
        }
        if (type instanceof GenericArrayType) {
            return TypeUtils.isAssignable(((GenericArrayType)type).getGenericComponentType(), type2, map);
        }
        if (type instanceof WildcardType) {
            for (Type type3 : TypeUtils.getImplicitUpperBounds((WildcardType)type)) {
                if (!TypeUtils.isAssignable(type3, genericArrayType)) continue;
                return true;
            }
            return false;
        }
        if (type instanceof TypeVariable) {
            for (Type type4 : TypeUtils.getImplicitBounds((TypeVariable)type)) {
                if (!TypeUtils.isAssignable(type4, genericArrayType)) continue;
                return true;
            }
            return false;
        }
        if (type instanceof ParameterizedType) {
            return false;
        }
        throw new IllegalStateException("found an unhandled type: " + type);
    }

    private static boolean isAssignable(Type type, ParameterizedType parameterizedType, Map<TypeVariable<?>, Type> map) {
        if (type == null) {
            return true;
        }
        if (parameterizedType == null) {
            return false;
        }
        if (type instanceof GenericArrayType) {
            return false;
        }
        if (parameterizedType.equals(type)) {
            return true;
        }
        Class<?> clazz = TypeUtils.getRawType(parameterizedType);
        Map<TypeVariable<?>, Type> map2 = TypeUtils.getTypeArguments(type, clazz, null);
        if (map2 == null) {
            return false;
        }
        if (map2.isEmpty()) {
            return true;
        }
        Map<TypeVariable<?>, Type> map3 = TypeUtils.getTypeArguments(parameterizedType, clazz, map);
        for (TypeVariable<?> typeVariable : map3.keySet()) {
            Type type2 = TypeUtils.unrollVariableAssignments(typeVariable, map3);
            Type type3 = TypeUtils.unrollVariableAssignments(typeVariable, map2);
            if (type2 == null && type3 instanceof Class || type3 == null || type2 == null || type2.equals(type3) || type2 instanceof WildcardType && TypeUtils.isAssignable(type3, type2, map)) continue;
            return false;
        }
        return true;
    }

    public static boolean isAssignable(Type type, Type type2) {
        return TypeUtils.isAssignable(type, type2, null);
    }

    private static boolean isAssignable(Type type, Type type2, Map<TypeVariable<?>, Type> map) {
        if (type2 == null || type2 instanceof Class) {
            return TypeUtils.isAssignable(type, (Class)type2);
        }
        if (type2 instanceof ParameterizedType) {
            return TypeUtils.isAssignable(type, (ParameterizedType)type2, map);
        }
        if (type2 instanceof GenericArrayType) {
            return TypeUtils.isAssignable(type, (GenericArrayType)type2, map);
        }
        if (type2 instanceof WildcardType) {
            return TypeUtils.isAssignable(type, (WildcardType)type2, map);
        }
        if (type2 instanceof TypeVariable) {
            return TypeUtils.isAssignable(type, (TypeVariable)type2, map);
        }
        throw new IllegalStateException("found an unhandled type: " + type2);
    }

    private static boolean isAssignable(Type type, TypeVariable<?> typeVariable, Map<TypeVariable<?>, Type> map) {
        if (type == null) {
            return true;
        }
        if (typeVariable == null) {
            return false;
        }
        if (typeVariable.equals(type)) {
            return true;
        }
        if (type instanceof TypeVariable) {
            Type[] typeArray;
            for (Type type2 : typeArray = TypeUtils.getImplicitBounds((TypeVariable)type)) {
                if (!TypeUtils.isAssignable(type2, typeVariable, map)) continue;
                return true;
            }
        }
        if (type instanceof Class || type instanceof ParameterizedType || type instanceof GenericArrayType || type instanceof WildcardType) {
            return false;
        }
        throw new IllegalStateException("found an unhandled type: " + type);
    }

    private static boolean isAssignable(Type type, WildcardType wildcardType, Map<TypeVariable<?>, Type> map) {
        if (type == null) {
            return true;
        }
        if (wildcardType == null) {
            return false;
        }
        if (wildcardType.equals(type)) {
            return true;
        }
        Type[] typeArray = TypeUtils.getImplicitUpperBounds(wildcardType);
        Type[] typeArray2 = TypeUtils.getImplicitLowerBounds(wildcardType);
        if (type instanceof WildcardType) {
            WildcardType wildcardType2 = (WildcardType)type;
            Type[] typeArray3 = TypeUtils.getImplicitUpperBounds(wildcardType2);
            Type[] typeArray4 = TypeUtils.getImplicitLowerBounds(wildcardType2);
            for (Type type2 : typeArray) {
                type2 = TypeUtils.substituteTypeVariables(type2, map);
                for (Type type3 : typeArray3) {
                    if (TypeUtils.isAssignable(type3, type2, map)) continue;
                    return false;
                }
            }
            for (Type type2 : typeArray2) {
                type2 = TypeUtils.substituteTypeVariables(type2, map);
                for (Type type3 : typeArray4) {
                    if (TypeUtils.isAssignable(type2, type3, map)) continue;
                    return false;
                }
            }
            return true;
        }
        for (Type type4 : typeArray) {
            if (TypeUtils.isAssignable(type, TypeUtils.substituteTypeVariables(type4, map), map)) continue;
            return false;
        }
        for (Type type4 : typeArray2) {
            if (TypeUtils.isAssignable(TypeUtils.substituteTypeVariables(type4, map), type, map)) continue;
            return false;
        }
        return true;
    }

    private static boolean isCyclical(Class<?> clazz) {
        for (TypeVariable<Class<?>> typeVariable : clazz.getTypeParameters()) {
            for (AnnotatedType annotatedType : typeVariable.getAnnotatedBounds()) {
                if (!annotatedType.getType().getTypeName().contains(clazz.getName())) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean isInstance(Object object, Type type) {
        if (type == null) {
            return false;
        }
        return object == null ? !(type instanceof Class) || !((Class)type).isPrimitive() : TypeUtils.isAssignable(object.getClass(), type, null);
    }

    private static <T> void mapTypeVariablesToArguments(Class<T> clazz, ParameterizedType parameterizedType, Map<TypeVariable<?>, Type> map) {
        Type type = parameterizedType.getOwnerType();
        if (type instanceof ParameterizedType) {
            TypeUtils.mapTypeVariablesToArguments(clazz, (ParameterizedType)type, map);
        }
        Type[] typeArray = parameterizedType.getActualTypeArguments();
        TypeVariable<Class<?>>[] typeVariableArray = TypeUtils.getRawType(parameterizedType).getTypeParameters();
        List<TypeVariable<Class<T>>> list = Arrays.asList(clazz.getTypeParameters());
        for (int i = 0; i < typeArray.length; ++i) {
            TypeVariable<Class<?>> typeVariable = typeVariableArray[i];
            Type type2 = typeArray[i];
            if (!list.contains(type2) || !map.containsKey(typeVariable)) continue;
            map.put((TypeVariable)type2, map.get(typeVariable));
        }
    }

    public static Type[] normalizeUpperBounds(Type[] typeArray) {
        Objects.requireNonNull(typeArray, "bounds");
        if (typeArray.length < 2) {
            return typeArray;
        }
        HashSet<Type> hashSet = new HashSet<Type>(typeArray.length);
        for (Type type : typeArray) {
            boolean bl = false;
            for (Type type2 : typeArray) {
                if (type == type2 || !TypeUtils.isAssignable(type2, type, null)) continue;
                bl = true;
                break;
            }
            if (bl) continue;
            hashSet.add(type);
        }
        return hashSet.toArray(ArrayUtils.EMPTY_TYPE_ARRAY);
    }

    public static final ParameterizedType parameterize(Class<?> clazz, Map<TypeVariable<?>, Type> map) {
        Objects.requireNonNull(clazz, "rawClass");
        Objects.requireNonNull(map, "typeVariableMap");
        return TypeUtils.parameterizeWithOwner(null, clazz, TypeUtils.extractTypeArgumentsFrom(map, clazz.getTypeParameters()));
    }

    public static final ParameterizedType parameterize(Class<?> clazz, Type ... typeArray) {
        return TypeUtils.parameterizeWithOwner(null, clazz, typeArray);
    }

    private static String parameterizedTypeToString(ParameterizedType parameterizedType) {
        StringBuilder stringBuilder = new StringBuilder();
        Type type = parameterizedType.getOwnerType();
        Class clazz = (Class)parameterizedType.getRawType();
        if (type == null) {
            stringBuilder.append(clazz.getName());
        } else {
            if (type instanceof Class) {
                stringBuilder.append(((Class)type).getName());
            } else {
                stringBuilder.append(type);
            }
            stringBuilder.append('.').append(clazz.getSimpleName());
        }
        int[] nArray = TypeUtils.findRecursiveTypes(parameterizedType);
        if (nArray.length > 0) {
            TypeUtils.appendRecursiveTypes(stringBuilder, nArray, parameterizedType.getActualTypeArguments());
        } else {
            GT_JOINER.join(stringBuilder, (Object[])parameterizedType.getActualTypeArguments());
        }
        return stringBuilder.toString();
    }

    public static final ParameterizedType parameterizeWithOwner(Type type, Class<?> clazz, Map<TypeVariable<?>, Type> map) {
        Objects.requireNonNull(clazz, "rawClass");
        Objects.requireNonNull(map, "typeVariableMap");
        return TypeUtils.parameterizeWithOwner(type, clazz, TypeUtils.extractTypeArgumentsFrom(map, clazz.getTypeParameters()));
    }

    public static final ParameterizedType parameterizeWithOwner(Type type, Class<?> clazz, Type ... typeArray) {
        Type type2;
        Objects.requireNonNull(clazz, "rawClass");
        if (clazz.getEnclosingClass() == null) {
            Validate.isTrue(type == null, "no owner allowed for top-level %s", clazz);
            type2 = null;
        } else if (type == null) {
            type2 = clazz.getEnclosingClass();
        } else {
            Validate.isTrue(TypeUtils.isAssignable(type, clazz.getEnclosingClass()), "%s is invalid owner type for parameterized %s", type, clazz);
            type2 = type;
        }
        Validate.noNullElements(typeArray, "null type argument at index %s", new Object[0]);
        Validate.isTrue(clazz.getTypeParameters().length == typeArray.length, "invalid number of type parameters specified: expected %d, got %d", clazz.getTypeParameters().length, typeArray.length);
        return new ParameterizedTypeImpl(clazz, type2, typeArray);
    }

    private static Type substituteTypeVariables(Type type, Map<TypeVariable<?>, Type> map) {
        if (type instanceof TypeVariable && map != null) {
            Type type2 = map.get(type);
            if (type2 == null) {
                throw new IllegalArgumentException("missing assignment type for type variable " + type);
            }
            return type2;
        }
        return type;
    }

    public static String toLongString(TypeVariable<?> typeVariable) {
        StringBuilder stringBuilder;
        block5: {
            Objects.requireNonNull(typeVariable, "typeVariable");
            stringBuilder = new StringBuilder();
            Object obj = typeVariable.getGenericDeclaration();
            if (obj instanceof Class) {
                Class<?> clazz = (Class<?>)obj;
                while (true) {
                    if (clazz.getEnclosingClass() == null) {
                        stringBuilder.insert(0, clazz.getName());
                        break block5;
                    }
                    stringBuilder.insert(0, clazz.getSimpleName()).insert(0, '.');
                    clazz = clazz.getEnclosingClass();
                }
            }
            if (obj instanceof Type) {
                stringBuilder.append(TypeUtils.toString((Type)obj));
            } else {
                stringBuilder.append(obj);
            }
        }
        return stringBuilder.append(':').append(TypeUtils.typeVariableToString(typeVariable)).toString();
    }

    public static String toString(Type type) {
        Objects.requireNonNull(type, "type");
        if (type instanceof Class) {
            return TypeUtils.classToString((Class)type);
        }
        if (type instanceof ParameterizedType) {
            return TypeUtils.parameterizedTypeToString((ParameterizedType)type);
        }
        if (type instanceof WildcardType) {
            return TypeUtils.wildcardTypeToString((WildcardType)type);
        }
        if (type instanceof TypeVariable) {
            return TypeUtils.typeVariableToString((TypeVariable)type);
        }
        if (type instanceof GenericArrayType) {
            return TypeUtils.genericArrayTypeToString((GenericArrayType)type);
        }
        throw new IllegalArgumentException(ObjectUtils.identityToString(type));
    }

    public static boolean typesSatisfyVariables(Map<TypeVariable<?>, Type> map) {
        Objects.requireNonNull(map, "typeVariableMap");
        for (Map.Entry<TypeVariable<?>, Type> entry : map.entrySet()) {
            TypeVariable<?> typeVariable = entry.getKey();
            Type type = entry.getValue();
            for (Type type2 : TypeUtils.getImplicitBounds(typeVariable)) {
                if (TypeUtils.isAssignable(type, TypeUtils.substituteTypeVariables(type2, map), map)) continue;
                return false;
            }
        }
        return true;
    }

    private static String typeVariableToString(TypeVariable<?> typeVariable) {
        StringBuilder stringBuilder = new StringBuilder(typeVariable.getName());
        Type[] typeArray = typeVariable.getBounds();
        if (!(typeArray.length <= 0 || typeArray.length == 1 && Object.class.equals((Object)typeArray[0]))) {
            stringBuilder.append(" extends ");
            AMP_JOINER.join(stringBuilder, (Type[])typeVariable.getBounds());
        }
        return stringBuilder.toString();
    }

    private static Type[] unrollBounds(Map<TypeVariable<?>, Type> map, Type[] typeArray) {
        Type[] typeArray2 = typeArray;
        for (int i = 0; i < typeArray2.length; ++i) {
            Type type = TypeUtils.unrollVariables(map, typeArray2[i]);
            if (type == null) {
                typeArray2 = ArrayUtils.remove(typeArray2, i--);
                continue;
            }
            typeArray2[i] = type;
        }
        return typeArray2;
    }

    private static Type unrollVariableAssignments(TypeVariable<?> typeVariable, Map<TypeVariable<?>, Type> map) {
        Type type;
        while ((type = map.get(typeVariable)) instanceof TypeVariable && !type.equals(typeVariable)) {
            typeVariable = (TypeVariable)type;
        }
        return type;
    }

    public static Type unrollVariables(Map<TypeVariable<?>, Type> map, Type type) {
        if (map == null) {
            map = Collections.emptyMap();
        }
        if (TypeUtils.containsTypeVariables(type)) {
            if (type instanceof TypeVariable) {
                return TypeUtils.unrollVariables(map, map.get(type));
            }
            if (type instanceof ParameterizedType) {
                Map<TypeVariable<?>, Type> map2;
                ParameterizedType parameterizedType = (ParameterizedType)type;
                if (parameterizedType.getOwnerType() == null) {
                    map2 = map;
                } else {
                    map2 = new HashMap(map);
                    map2.putAll(TypeUtils.getTypeArguments(parameterizedType));
                }
                Type[] typeArray = parameterizedType.getActualTypeArguments();
                for (int i = 0; i < typeArray.length; ++i) {
                    Type type2 = TypeUtils.unrollVariables(map2, typeArray[i]);
                    if (type2 == null) continue;
                    typeArray[i] = type2;
                }
                return TypeUtils.parameterizeWithOwner(parameterizedType.getOwnerType(), (Class)parameterizedType.getRawType(), typeArray);
            }
            if (type instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType)type;
                return TypeUtils.wildcardType().withUpperBounds(TypeUtils.unrollBounds(map, wildcardType.getUpperBounds())).withLowerBounds(TypeUtils.unrollBounds(map, wildcardType.getLowerBounds())).build();
            }
        }
        return type;
    }

    public static WildcardTypeBuilder wildcardType() {
        return new WildcardTypeBuilder();
    }

    private static String wildcardTypeToString(WildcardType wildcardType) {
        StringBuilder stringBuilder = new StringBuilder().append('?');
        Type[] typeArray = wildcardType.getLowerBounds();
        Type[] typeArray2 = wildcardType.getUpperBounds();
        if (typeArray.length > 1 || typeArray.length == 1 && typeArray[0] != null) {
            AMP_JOINER.join(stringBuilder.append(" super "), (Type[])typeArray);
        } else if (typeArray2.length > 1 || typeArray2.length == 1 && !Object.class.equals((Object)typeArray2[0])) {
            AMP_JOINER.join(stringBuilder.append(" extends "), (Type[])typeArray2);
        }
        return stringBuilder.toString();
    }

    public static <T> Typed<T> wrap(Class<T> clazz) {
        return TypeUtils.wrap(clazz);
    }

    public static <T> Typed<T> wrap(Type type) {
        return () -> type;
    }

    @Deprecated
    public TypeUtils() {
    }

    private static final class GenericArrayTypeImpl
    implements GenericArrayType {
        private final Type componentType;

        private GenericArrayTypeImpl(Type type) {
            this.componentType = type;
        }

        public boolean equals(Object object) {
            return object == this || object instanceof GenericArrayType && TypeUtils.equals(this, (GenericArrayType)object);
        }

        @Override
        public Type getGenericComponentType() {
            return this.componentType;
        }

        public int hashCode() {
            int n = 1072;
            return n |= this.componentType.hashCode();
        }

        public String toString() {
            return TypeUtils.toString(this);
        }
    }

    private static final class ParameterizedTypeImpl
    implements ParameterizedType {
        private final Class<?> raw;
        private final Type useOwner;
        private final Type[] typeArguments;

        private ParameterizedTypeImpl(Class<?> clazz, Type type, Type[] typeArray) {
            this.raw = clazz;
            this.useOwner = type;
            this.typeArguments = (Type[])Arrays.copyOf(typeArray, typeArray.length, Type[].class);
        }

        public boolean equals(Object object) {
            return object == this || object instanceof ParameterizedType && TypeUtils.equals(this, (ParameterizedType)object);
        }

        @Override
        public Type[] getActualTypeArguments() {
            return (Type[])this.typeArguments.clone();
        }

        @Override
        public Type getOwnerType() {
            return this.useOwner;
        }

        @Override
        public Type getRawType() {
            return this.raw;
        }

        public int hashCode() {
            int n = 1136;
            n |= this.raw.hashCode();
            n <<= 4;
            n |= Objects.hashCode(this.useOwner);
            n <<= 8;
            return n |= Arrays.hashCode(this.typeArguments);
        }

        public String toString() {
            return TypeUtils.toString(this);
        }
    }

    public static class WildcardTypeBuilder
    implements Builder<WildcardType> {
        private Type[] upperBounds;
        private Type[] lowerBounds;

        private WildcardTypeBuilder() {
        }

        @Override
        public WildcardType build() {
            return new WildcardTypeImpl(this.upperBounds, this.lowerBounds);
        }

        public WildcardTypeBuilder withLowerBounds(Type ... typeArray) {
            this.lowerBounds = typeArray;
            return this;
        }

        public WildcardTypeBuilder withUpperBounds(Type ... typeArray) {
            this.upperBounds = typeArray;
            return this;
        }
    }

    private static final class WildcardTypeImpl
    implements WildcardType {
        private final Type[] upperBounds;
        private final Type[] lowerBounds;

        private WildcardTypeImpl(Type[] typeArray, Type[] typeArray2) {
            this.upperBounds = ObjectUtils.defaultIfNull(typeArray, ArrayUtils.EMPTY_TYPE_ARRAY);
            this.lowerBounds = ObjectUtils.defaultIfNull(typeArray2, ArrayUtils.EMPTY_TYPE_ARRAY);
        }

        public boolean equals(Object object) {
            return object == this || object instanceof WildcardType && TypeUtils.equals(this, (WildcardType)object);
        }

        @Override
        public Type[] getLowerBounds() {
            return (Type[])this.lowerBounds.clone();
        }

        @Override
        public Type[] getUpperBounds() {
            return (Type[])this.upperBounds.clone();
        }

        public int hashCode() {
            int n = 18688;
            n |= Arrays.hashCode(this.upperBounds);
            n <<= 8;
            return n |= Arrays.hashCode(this.lowerBounds);
        }

        public String toString() {
            return TypeUtils.toString(this);
        }
    }
}

