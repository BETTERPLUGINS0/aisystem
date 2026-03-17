/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.utils.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ReflectionUtil {
    private static final Map<String, Constructor<?>> constructorCache = new HashMap();
    private static final Map<String, Method> methodCache = new HashMap<String, Method>();
    private static final Map<String, Field> fieldCache = new HashMap<String, Field>();

    private static Class<?> wrapperToPrimitive(Class<?> clazz) {
        if (clazz == Boolean.class) {
            return Boolean.TYPE;
        }
        if (clazz == Integer.class) {
            return Integer.TYPE;
        }
        if (clazz == Double.class) {
            return Double.TYPE;
        }
        if (clazz == Float.class) {
            return Float.TYPE;
        }
        if (clazz == Long.class) {
            return Long.TYPE;
        }
        if (clazz == Short.class) {
            return Short.TYPE;
        }
        if (clazz == Byte.class) {
            return Byte.TYPE;
        }
        if (clazz == Void.class) {
            return Void.TYPE;
        }
        if (clazz == Character.class) {
            return Character.TYPE;
        }
        if (clazz == CollectionParam.class) {
            return Collection.class;
        }
        if (clazz == ListParam.class) {
            return List.class;
        }
        if (clazz == ArrayList.class) {
            return Collection.class;
        }
        if (clazz == HashMap.class) {
            return Map.class;
        }
        return clazz;
    }

    private static Class<?>[] toParamTypes(Object ... objectArray) {
        return (Class[])Arrays.stream(objectArray).map(object -> object != null ? ReflectionUtil.wrapperToPrimitive(object.getClass()) : null).toArray(Class[]::new);
    }

    @Nullable
    public static Class<?> getClass(@NotNull String string) {
        try {
            return Class.forName(string);
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Object callConstructorNull(Class<?> clazz, Class<?> clazz2) {
        try {
            String string = "ConstructorNull:" + clazz.getName() + ":" + clazz2.getName();
            if (constructorCache.containsKey(string)) {
                Constructor<?> constructor = constructorCache.get(string);
                return constructor.newInstance(clazz.cast(null));
            }
            Constructor<?> constructor = clazz.getConstructor(clazz2);
            constructor.setAccessible(true);
            constructorCache.put(string, constructor);
            return constructor.newInstance(clazz.cast(null));
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Object callFirstConstructor(Class<?> clazz, Object ... objectArray) {
        try {
            String string = "FirstConstructor:" + clazz.getName();
            if (constructorCache.containsKey(string)) {
                Constructor<?> constructor = constructorCache.get(string);
                return constructor.newInstance(objectArray);
            }
            Constructor<?> constructor = clazz.getConstructors()[0];
            constructor.setAccessible(true);
            constructorCache.put(string, constructor);
            return constructor.newInstance(objectArray);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Object callConstructor(Class<?> clazz, Object ... objectArray) {
        try {
            String string = "Constructor:" + clazz.getName() + ":" + Arrays.hashCode(objectArray);
            if (constructorCache.containsKey(string)) {
                Constructor<?> constructor = constructorCache.get(string);
                return constructor.newInstance(objectArray);
            }
            Constructor<?> constructor = clazz.getConstructor(ReflectionUtil.toParamTypes(objectArray));
            constructor.setAccessible(true);
            constructorCache.put(string, constructor);
            return constructor.newInstance(objectArray);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Object callDeclaredConstructor(Class<?> clazz, Object ... objectArray) {
        try {
            String string = "DeclaredConstructor:" + clazz.getName() + ":" + Arrays.hashCode(objectArray);
            if (constructorCache.containsKey(string)) {
                Constructor<?> constructor = constructorCache.get(string);
                return constructor.newInstance(objectArray);
            }
            Constructor<?> constructor = clazz.getDeclaredConstructor(ReflectionUtil.toParamTypes(objectArray));
            constructor.setAccessible(true);
            constructorCache.put(string, constructor);
            return constructor.newInstance(objectArray);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Object callMethod(Class<?> clazz, String string, Object ... objectArray) {
        try {
            String string2 = "Method:" + clazz.getName() + ":" + string + ":" + Arrays.hashCode(objectArray);
            if (methodCache.containsKey(string2)) {
                Method method = methodCache.get(string2);
                return method.invoke(null, objectArray);
            }
            Method method = clazz.getMethod(string, ReflectionUtil.toParamTypes(objectArray));
            method.setAccessible(true);
            methodCache.put(string2, method);
            return method.invoke(null, objectArray);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Object callMethod(Object object, String string, Object ... objectArray) {
        try {
            String string2 = "Method:" + object.getClass().getName() + ":" + string + ":" + Arrays.hashCode(objectArray);
            if (methodCache.containsKey(string2)) {
                Method method = methodCache.get(string2);
                return method.invoke(object, objectArray);
            }
            Method method = object.getClass().getMethod(string, ReflectionUtil.toParamTypes(objectArray));
            method.setAccessible(true);
            methodCache.put(string2, method);
            return method.invoke(object, objectArray);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Object callDeclaredMethod(Object object, String string, Object ... objectArray) {
        try {
            String string2 = "DeclaredMethod:" + object.getClass().getName() + ":" + string + ":" + Arrays.hashCode(objectArray);
            if (methodCache.containsKey(string2)) {
                Method method = methodCache.get(string2);
                return method.invoke(object, objectArray);
            }
            Method method = object.getClass().getDeclaredMethod(string, ReflectionUtil.toParamTypes(objectArray));
            method.setAccessible(true);
            methodCache.put(string2, method);
            return method.invoke(object, objectArray);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return null;
        }
    }

    public static boolean hasField(Object object, String string) {
        try {
            String string2 = "HasField:" + object.getClass().getName() + ":" + string;
            if (fieldCache.containsKey(string2)) {
                return true;
            }
            object.getClass().getDeclaredField(string);
            fieldCache.put(string2, null);
            return true;
        } catch (NoSuchFieldException noSuchFieldException) {
            return false;
        }
    }

    @Nullable
    public static Object getField(Object object, String string) {
        try {
            String string2 = "Field:" + object.getClass().getName() + ":" + string;
            if (fieldCache.containsKey(string2)) {
                Field field = fieldCache.get(string2);
                return field.get(object);
            }
            Field field = object.getClass().getField(string);
            field.setAccessible(true);
            fieldCache.put(string2, field);
            return field.get(object);
        } catch (IllegalAccessException | NoSuchFieldException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Object getDeclaredField(Class<?> clazz, String string) {
        try {
            String string2 = "DeclaredField:" + clazz.getName() + ":" + string;
            if (fieldCache.containsKey(string2)) {
                Field field = fieldCache.get(string2);
                return field.get(null);
            }
            Field field = clazz.getDeclaredField(string);
            field.setAccessible(true);
            fieldCache.put(string2, field);
            return field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Object getDeclaredField(Object object, String string) {
        try {
            String string2 = "DeclaredField:" + object.getClass().getName() + ":" + string;
            if (fieldCache.containsKey(string2)) {
                Field field = fieldCache.get(string2);
                return field.get(object);
            }
            Field field = object.getClass().getDeclaredField(string);
            field.setAccessible(true);
            fieldCache.put(string2, field);
            return field.get(object);
        } catch (IllegalAccessException | NoSuchFieldException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Object getDeclaredField(Class<?> clazz, Object object, String string) {
        try {
            String string2 = "DeclaredField:" + clazz.getName() + ":" + string;
            if (fieldCache.containsKey(string2)) {
                Field field = fieldCache.get(string2);
                return field.get(object);
            }
            Field field = clazz.getDeclaredField(string);
            field.setAccessible(true);
            fieldCache.put(string2, field);
            return field.get(object);
        } catch (IllegalAccessException | NoSuchFieldException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return null;
        }
    }

    public static void setDeclaredField(Object object, String string, Object object2) {
        try {
            String string2 = "DeclaredField:" + object.getClass().getName() + ":" + string;
            if (fieldCache.containsKey(string2)) {
                Field field = fieldCache.get(string2);
                field.set(object, object2);
            } else {
                Field field = object.getClass().getDeclaredField(string);
                field.setAccessible(true);
                fieldCache.put(string2, field);
                field.set(object, object2);
            }
        } catch (IllegalAccessException | NoSuchFieldException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
        }
    }

    @Generated
    private ReflectionUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static class CollectionParam<E>
    extends ArrayList<E> {
    }

    public static class ListParam<E>
    extends ArrayList<E> {
    }
}

