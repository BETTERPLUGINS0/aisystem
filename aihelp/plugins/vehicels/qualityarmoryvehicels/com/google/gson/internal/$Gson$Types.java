/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.google.gson.internal;

import com.google.gson.internal.$Gson$Preconditions;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

public final class $Gson$Types {
    static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

    private $Gson$Types() {
        throw new UnsupportedOperationException();
    }

    public static ParameterizedType newParameterizedTypeWithOwner(Type type, Type type2, Type ... typeArray) {
        return new ParameterizedTypeImpl(type, type2, typeArray);
    }

    public static GenericArrayType arrayOf(Type type) {
        return new GenericArrayTypeImpl(type);
    }

    public static WildcardType subtypeOf(Type type) {
        return new WildcardTypeImpl(new Type[]{type}, EMPTY_TYPE_ARRAY);
    }

    public static WildcardType supertypeOf(Type type) {
        return new WildcardTypeImpl(new Type[]{Object.class}, new Type[]{type});
    }

    public static Type canonicalize(Type type) {
        if (type instanceof Class) {
            Class clazz = (Class)type;
            return clazz.isArray() ? new GenericArrayTypeImpl($Gson$Types.canonicalize(clazz.getComponentType())) : clazz;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            return new ParameterizedTypeImpl(parameterizedType.getOwnerType(), parameterizedType.getRawType(), parameterizedType.getActualTypeArguments());
        }
        if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType)type;
            return new GenericArrayTypeImpl(genericArrayType.getGenericComponentType());
        }
        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType)type;
            return new WildcardTypeImpl(wildcardType.getUpperBounds(), wildcardType.getLowerBounds());
        }
        return type;
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class)type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            Type type2 = parameterizedType.getRawType();
            $Gson$Preconditions.checkArgument(type2 instanceof Class);
            return (Class)type2;
        }
        if (type instanceof GenericArrayType) {
            Type type3 = ((GenericArrayType)type).getGenericComponentType();
            return Array.newInstance($Gson$Types.getRawType(type3), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            return Object.class;
        }
        if (type instanceof WildcardType) {
            return $Gson$Types.getRawType(((WildcardType)type).getUpperBounds()[0]);
        }
        String string = type == null ? "null" : type.getClass().getName();
        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + string);
    }

    static boolean equal(Object object, Object object2) {
        return object == object2 || object != null && object.equals(object2);
    }

    public static boolean equals(Type type, Type type2) {
        if (type == type2) {
            return true;
        }
        if (type instanceof Class) {
            return type.equals(type2);
        }
        if (type instanceof ParameterizedType) {
            if (!(type2 instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType parameterizedType = (ParameterizedType)type;
            ParameterizedType parameterizedType2 = (ParameterizedType)type2;
            return $Gson$Types.equal(parameterizedType.getOwnerType(), parameterizedType2.getOwnerType()) && parameterizedType.getRawType().equals(parameterizedType2.getRawType()) && Arrays.equals(parameterizedType.getActualTypeArguments(), parameterizedType2.getActualTypeArguments());
        }
        if (type instanceof GenericArrayType) {
            if (!(type2 instanceof GenericArrayType)) {
                return false;
            }
            GenericArrayType genericArrayType = (GenericArrayType)type;
            GenericArrayType genericArrayType2 = (GenericArrayType)type2;
            return $Gson$Types.equals(genericArrayType.getGenericComponentType(), genericArrayType2.getGenericComponentType());
        }
        if (type instanceof WildcardType) {
            if (!(type2 instanceof WildcardType)) {
                return false;
            }
            WildcardType wildcardType = (WildcardType)type;
            WildcardType wildcardType2 = (WildcardType)type2;
            return Arrays.equals(wildcardType.getUpperBounds(), wildcardType2.getUpperBounds()) && Arrays.equals(wildcardType.getLowerBounds(), wildcardType2.getLowerBounds());
        }
        if (type instanceof TypeVariable) {
            if (!(type2 instanceof TypeVariable)) {
                return false;
            }
            TypeVariable typeVariable = (TypeVariable)type;
            TypeVariable typeVariable2 = (TypeVariable)type2;
            return typeVariable.getGenericDeclaration() == typeVariable2.getGenericDeclaration() && typeVariable.getName().equals(typeVariable2.getName());
        }
        return false;
    }

    static int hashCodeOrZero(Object object) {
        return object != null ? object.hashCode() : 0;
    }

    public static String typeToString(Type type) {
        return type instanceof Class ? ((Class)type).getName() : type.toString();
    }

    static Type getGenericSupertype(Type type, Class<?> object, Class<?> clazz) {
        Object object2;
        if (clazz == object) {
            return type;
        }
        if (clazz.isInterface()) {
            object2 = ((Class)object).getInterfaces();
            int n = ((Class<?>[])object2).length;
            for (int i = 0; i < n; ++i) {
                if (object2[i] == clazz) {
                    return ((Class)object).getGenericInterfaces()[i];
                }
                if (!clazz.isAssignableFrom(object2[i])) continue;
                return $Gson$Types.getGenericSupertype(((Class)object).getGenericInterfaces()[i], object2[i], clazz);
            }
        }
        if (!((Class)object).isInterface()) {
            while (object != Object.class) {
                object2 = ((Class)object).getSuperclass();
                if (object2 == clazz) {
                    return ((Class)object).getGenericSuperclass();
                }
                if (clazz.isAssignableFrom((Class<?>)object2)) {
                    return $Gson$Types.getGenericSupertype(((Class)object).getGenericSuperclass(), object2, clazz);
                }
                object = object2;
            }
        }
        return clazz;
    }

    static Type getSupertype(Type type, Class<?> clazz, Class<?> clazz2) {
        $Gson$Preconditions.checkArgument(clazz2.isAssignableFrom(clazz));
        return $Gson$Types.resolve(type, clazz, $Gson$Types.getGenericSupertype(type, clazz, clazz2));
    }

    public static Type getArrayComponentType(Type type) {
        return type instanceof GenericArrayType ? ((GenericArrayType)type).getGenericComponentType() : ((Class)type).getComponentType();
    }

    public static Type getCollectionElementType(Type type, Class<?> clazz) {
        Type type2 = $Gson$Types.getSupertype(type, clazz, Collection.class);
        if (type2 instanceof WildcardType) {
            type2 = ((WildcardType)type2).getUpperBounds()[0];
        }
        if (type2 instanceof ParameterizedType) {
            return ((ParameterizedType)type2).getActualTypeArguments()[0];
        }
        return Object.class;
    }

    public static Type[] getMapKeyAndValueTypes(Type type, Class<?> clazz) {
        if (type == Properties.class) {
            return new Type[]{String.class, String.class};
        }
        Type type2 = $Gson$Types.getSupertype(type, clazz, Map.class);
        if (type2 instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type2;
            return parameterizedType.getActualTypeArguments();
        }
        return new Type[]{Object.class, Object.class};
    }

    public static Type resolve(Type type, Class<?> clazz, Type type2) {
        Type type3;
        while (type2 instanceof TypeVariable) {
            type3 = (TypeVariable)type2;
            if ((type2 = $Gson$Types.resolveTypeVariable(type, clazz, type3)) != type3) continue;
            return type2;
        }
        if (type2 instanceof Class && ((Class)type2).isArray()) {
            Type type4;
            type3 = (Class)type2;
            Class<?> clazz2 = ((Class)type3).getComponentType();
            return clazz2 == (type4 = $Gson$Types.resolve(type, clazz, clazz2)) ? type3 : $Gson$Types.arrayOf(type4);
        }
        if (type2 instanceof GenericArrayType) {
            Type type5;
            type3 = (GenericArrayType)type2;
            Type type6 = type3.getGenericComponentType();
            return type6 == (type5 = $Gson$Types.resolve(type, clazz, type6)) ? type3 : $Gson$Types.arrayOf(type5);
        }
        if (type2 instanceof ParameterizedType) {
            type3 = (ParameterizedType)type2;
            Type type7 = type3.getOwnerType();
            Type type8 = $Gson$Types.resolve(type, clazz, type7);
            boolean bl = type8 != type7;
            Type[] typeArray = type3.getActualTypeArguments();
            int n = typeArray.length;
            for (int i = 0; i < n; ++i) {
                Type type9 = $Gson$Types.resolve(type, clazz, typeArray[i]);
                if (type9 == typeArray[i]) continue;
                if (!bl) {
                    typeArray = (Type[])typeArray.clone();
                    bl = true;
                }
                typeArray[i] = type9;
            }
            return bl ? $Gson$Types.newParameterizedTypeWithOwner(type8, type3.getRawType(), typeArray) : type3;
        }
        if (type2 instanceof WildcardType) {
            Type type10;
            type3 = (WildcardType)type2;
            Type[] typeArray = type3.getLowerBounds();
            Type[] typeArray2 = type3.getUpperBounds();
            if (typeArray.length == 1) {
                Type type11 = $Gson$Types.resolve(type, clazz, typeArray[0]);
                if (type11 != typeArray[0]) {
                    return $Gson$Types.supertypeOf(type11);
                }
            } else if (typeArray2.length == 1 && (type10 = $Gson$Types.resolve(type, clazz, typeArray2[0])) != typeArray2[0]) {
                return $Gson$Types.subtypeOf(type10);
            }
            return type3;
        }
        return type2;
    }

    static Type resolveTypeVariable(Type type, Class<?> clazz, TypeVariable<?> typeVariable) {
        Class<?> clazz2 = $Gson$Types.declaringClassOf(typeVariable);
        if (clazz2 == null) {
            return typeVariable;
        }
        Type type2 = $Gson$Types.getGenericSupertype(type, clazz, clazz2);
        if (type2 instanceof ParameterizedType) {
            int n = $Gson$Types.indexOf(clazz2.getTypeParameters(), typeVariable);
            return ((ParameterizedType)type2).getActualTypeArguments()[n];
        }
        return typeVariable;
    }

    private static int indexOf(Object[] objectArray, Object object) {
        for (int i = 0; i < objectArray.length; ++i) {
            if (!object.equals(objectArray[i])) continue;
            return i;
        }
        throw new NoSuchElementException();
    }

    private static Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
        Object obj = typeVariable.getGenericDeclaration();
        return obj instanceof Class ? (Class)obj : null;
    }

    static void checkNotPrimitive(Type type) {
        $Gson$Preconditions.checkArgument(!(type instanceof Class) || !((Class)type).isPrimitive());
    }

    private static final class WildcardTypeImpl
    implements WildcardType,
    Serializable {
        private final Type upperBound;
        private final Type lowerBound;
        private static final long serialVersionUID = 0L;

        public WildcardTypeImpl(Type[] typeArray, Type[] typeArray2) {
            $Gson$Preconditions.checkArgument(typeArray2.length <= 1);
            $Gson$Preconditions.checkArgument(typeArray.length == 1);
            if (typeArray2.length == 1) {
                $Gson$Preconditions.checkNotNull(typeArray2[0]);
                $Gson$Types.checkNotPrimitive(typeArray2[0]);
                $Gson$Preconditions.checkArgument(typeArray[0] == Object.class);
                this.lowerBound = $Gson$Types.canonicalize(typeArray2[0]);
                this.upperBound = Object.class;
            } else {
                $Gson$Preconditions.checkNotNull(typeArray[0]);
                $Gson$Types.checkNotPrimitive(typeArray[0]);
                this.lowerBound = null;
                this.upperBound = $Gson$Types.canonicalize(typeArray[0]);
            }
        }

        @Override
        public Type[] getUpperBounds() {
            return new Type[]{this.upperBound};
        }

        @Override
        public Type[] getLowerBounds() {
            Type[] typeArray;
            if (this.lowerBound != null) {
                Type[] typeArray2 = new Type[1];
                typeArray = typeArray2;
                typeArray2[0] = this.lowerBound;
            } else {
                typeArray = EMPTY_TYPE_ARRAY;
            }
            return typeArray;
        }

        public boolean equals(Object object) {
            return object instanceof WildcardType && $Gson$Types.equals(this, (WildcardType)object);
        }

        public int hashCode() {
            return (this.lowerBound != null ? 31 + this.lowerBound.hashCode() : 1) ^ 31 + this.upperBound.hashCode();
        }

        public String toString() {
            if (this.lowerBound != null) {
                return "? super " + $Gson$Types.typeToString(this.lowerBound);
            }
            if (this.upperBound == Object.class) {
                return "?";
            }
            return "? extends " + $Gson$Types.typeToString(this.upperBound);
        }
    }

    private static final class GenericArrayTypeImpl
    implements GenericArrayType,
    Serializable {
        private final Type componentType;
        private static final long serialVersionUID = 0L;

        public GenericArrayTypeImpl(Type type) {
            this.componentType = $Gson$Types.canonicalize(type);
        }

        @Override
        public Type getGenericComponentType() {
            return this.componentType;
        }

        public boolean equals(Object object) {
            return object instanceof GenericArrayType && $Gson$Types.equals(this, (GenericArrayType)object);
        }

        public int hashCode() {
            return this.componentType.hashCode();
        }

        public String toString() {
            return $Gson$Types.typeToString(this.componentType) + "[]";
        }
    }

    private static final class ParameterizedTypeImpl
    implements ParameterizedType,
    Serializable {
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;
        private static final long serialVersionUID = 0L;

        public ParameterizedTypeImpl(Type type, Type type2, Type ... typeArray) {
            if (type2 instanceof Class) {
                Class clazz = (Class)type2;
                boolean bl = Modifier.isStatic(clazz.getModifiers()) || clazz.getEnclosingClass() == null;
                $Gson$Preconditions.checkArgument(type != null || bl);
            }
            this.ownerType = type == null ? null : $Gson$Types.canonicalize(type);
            this.rawType = $Gson$Types.canonicalize(type2);
            this.typeArguments = (Type[])typeArray.clone();
            for (int i = 0; i < this.typeArguments.length; ++i) {
                $Gson$Preconditions.checkNotNull(this.typeArguments[i]);
                $Gson$Types.checkNotPrimitive(this.typeArguments[i]);
                this.typeArguments[i] = $Gson$Types.canonicalize(this.typeArguments[i]);
            }
        }

        @Override
        public Type[] getActualTypeArguments() {
            return (Type[])this.typeArguments.clone();
        }

        @Override
        public Type getRawType() {
            return this.rawType;
        }

        @Override
        public Type getOwnerType() {
            return this.ownerType;
        }

        public boolean equals(Object object) {
            return object instanceof ParameterizedType && $Gson$Types.equals(this, (ParameterizedType)object);
        }

        public int hashCode() {
            return Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode() ^ $Gson$Types.hashCodeOrZero(this.ownerType);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(30 * (this.typeArguments.length + 1));
            stringBuilder.append($Gson$Types.typeToString(this.rawType));
            if (this.typeArguments.length == 0) {
                return stringBuilder.toString();
            }
            stringBuilder.append("<").append($Gson$Types.typeToString(this.typeArguments[0]));
            for (int i = 1; i < this.typeArguments.length; ++i) {
                stringBuilder.append(", ").append($Gson$Types.typeToString(this.typeArguments[i]));
            }
            return stringBuilder.append(">").toString();
        }
    }
}

