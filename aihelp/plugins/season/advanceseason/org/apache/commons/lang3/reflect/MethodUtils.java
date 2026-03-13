/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.MemberUtils;
import org.apache.commons.lang3.reflect.TypeUtils;

public class MethodUtils {
    private static final Comparator<Method> METHOD_BY_SIGNATURE = Comparator.comparing(Method::toString);

    private static int distance(Class<?>[] classArray, Class<?>[] classArray2) {
        int n = 0;
        if (!ClassUtils.isAssignable(classArray, classArray2, true)) {
            return -1;
        }
        for (int i = 0; i < classArray.length; ++i) {
            Class<?> clazz = classArray[i];
            Class<?> clazz2 = classArray2[i];
            if (clazz == null || clazz.equals(clazz2)) continue;
            if (ClassUtils.isAssignable(clazz, clazz2, true) && !ClassUtils.isAssignable(clazz, clazz2, false)) {
                ++n;
                continue;
            }
            n += 2;
        }
        return n;
    }

    public static Method getAccessibleMethod(Class<?> clazz, String string, Class<?> ... classArray) {
        return MethodUtils.getAccessibleMethod(MethodUtils.getMethodObject(clazz, string, classArray));
    }

    public static Method getAccessibleMethod(Method method) {
        Class<?>[] classArray;
        if (!MemberUtils.isAccessible(method)) {
            return null;
        }
        Class<?> clazz = method.getDeclaringClass();
        if (ClassUtils.isPublic(clazz)) {
            return method;
        }
        String string = method.getName();
        if ((method = MethodUtils.getAccessibleMethodFromInterfaceNest(clazz, string, classArray = method.getParameterTypes())) == null) {
            method = MethodUtils.getAccessibleMethodFromSuperclass(clazz, string, classArray);
        }
        return method;
    }

    private static Method getAccessibleMethodFromInterfaceNest(Class<?> clazz, String string, Class<?> ... classArray) {
        while (clazz != null) {
            Class<?>[] classArray2;
            for (Class<?> clazz2 : classArray2 = clazz.getInterfaces()) {
                if (!ClassUtils.isPublic(clazz2)) continue;
                try {
                    return clazz2.getDeclaredMethod(string, classArray);
                } catch (NoSuchMethodException noSuchMethodException) {
                    Method method = MethodUtils.getAccessibleMethodFromInterfaceNest(clazz2, string, classArray);
                    if (method == null) continue;
                    return method;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    private static Method getAccessibleMethodFromSuperclass(Class<?> clazz, String string, Class<?> ... classArray) {
        for (Class<?> clazz2 = clazz.getSuperclass(); clazz2 != null; clazz2 = clazz2.getSuperclass()) {
            if (!ClassUtils.isPublic(clazz2)) continue;
            return MethodUtils.getMethodObject(clazz2, string, classArray);
        }
        return null;
    }

    private static List<Class<?>> getAllSuperclassesAndInterfaces(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        List<Class<?>> list = ClassUtils.getAllSuperclasses(clazz);
        int n = 0;
        List<Class<?>> list2 = ClassUtils.getAllInterfaces(clazz);
        int n2 = 0;
        while (n2 < list2.size() || n < list.size()) {
            Class<?> clazz2 = n2 >= list2.size() ? list.get(n++) : (n >= list.size() || n >= n2 ? list2.get(n2++) : list.get(n++));
            arrayList.add(clazz2);
        }
        return arrayList;
    }

    public static <A extends Annotation> A getAnnotation(Method method, Class<A> clazz, boolean bl, boolean bl2) {
        Objects.requireNonNull(method, "method");
        Objects.requireNonNull(clazz, "annotationCls");
        if (!bl2 && !MemberUtils.isAccessible(method)) {
            return null;
        }
        A a2 = method.getAnnotation(clazz);
        if (a2 == null && bl) {
            Class<?> clazz2 = method.getDeclaringClass();
            List<Class<?>> list = MethodUtils.getAllSuperclassesAndInterfaces(clazz2);
            for (Class<?> clazz3 : list) {
                Method method2 = bl2 ? MethodUtils.getMatchingMethod(clazz3, method.getName(), method.getParameterTypes()) : MethodUtils.getMatchingAccessibleMethod(clazz3, method.getName(), method.getParameterTypes());
                if (method2 == null || (a2 = method2.getAnnotation(clazz)) == null) continue;
                break;
            }
        }
        return a2;
    }

    public static Method getMatchingAccessibleMethod(Class<?> clazz, String string, Class<?> ... classArray) {
        Object object;
        Method method2 = MethodUtils.getMethodObject(clazz, string, classArray);
        if (method2 != null) {
            return MemberUtils.setAccessibleWorkaround(method2);
        }
        Method[] methodArray = clazz.getMethods();
        List list = Stream.of(methodArray).filter(method -> method.getName().equals(string) && MemberUtils.isMatchingMethod(method, classArray)).collect(Collectors.toList());
        list.sort(METHOD_BY_SIGNATURE);
        Method method3 = null;
        for (GenericDeclaration genericDeclaration : list) {
            object = MethodUtils.getAccessibleMethod(genericDeclaration);
            if (object == null || method3 != null && MemberUtils.compareMethodFit((Method)object, method3, classArray) >= 0) continue;
            method3 = object;
        }
        if (method3 != null) {
            MemberUtils.setAccessibleWorkaround(method3);
        }
        if (method3 != null && method3.isVarArgs() && method3.getParameterTypes().length > 0 && classArray.length > 0) {
            String string2;
            GenericDeclaration genericDeclaration;
            Class<?>[] classArray2 = method3.getParameterTypes();
            genericDeclaration = classArray2[classArray2.length - 1].getComponentType();
            object = ClassUtils.primitiveToWrapper(genericDeclaration).getName();
            Class<?> clazz2 = classArray[classArray.length - 1];
            String string3 = clazz2 == null ? null : clazz2.getName();
            String string4 = string2 = clazz2 == null ? null : clazz2.getSuperclass().getName();
            if (string3 != null && string2 != null && !((String)object).equals(string3) && !((String)object).equals(string2)) {
                return null;
            }
        }
        return method3;
    }

    public static Method getMatchingMethod(Class<?> clazz, String string, Class<?> ... classArray) {
        Object object2;
        Objects.requireNonNull(clazz, "cls");
        Validate.notEmpty(string, "methodName", new Object[0]);
        List list = Stream.of(clazz.getDeclaredMethods()).filter(method -> method.getName().equals(string)).collect(Collectors.toList());
        ClassUtils.getAllSuperclasses(clazz).stream().map(Class::getDeclaredMethods).flatMap(Stream::of).filter(method -> method.getName().equals(string)).forEach(list::add);
        for (Object object2 : list) {
            if (!Arrays.deepEquals(((Method)object2).getParameterTypes(), classArray)) continue;
            return object2;
        }
        TreeMap treeMap = new TreeMap();
        list.stream().filter(method -> ClassUtils.isAssignable(classArray, method.getParameterTypes(), true)).forEach(method -> {
            int n2 = MethodUtils.distance(classArray, method.getParameterTypes());
            List list = treeMap.computeIfAbsent(n2, n -> new ArrayList());
            list.add(method);
        });
        if (treeMap.isEmpty()) {
            return null;
        }
        object2 = (List)treeMap.values().iterator().next();
        if (object2.size() == 1 || !Objects.equals(((Method)object2.get(0)).getDeclaringClass(), ((Method)object2.get(1)).getDeclaringClass())) {
            return (Method)object2.get(0);
        }
        throw new IllegalStateException(String.format("Found multiple candidates for method %s on class %s : %s", string + Stream.of(classArray).map(String::valueOf).collect(Collectors.joining(",", "(", ")")), clazz.getName(), object2.stream().map(Method::toString).collect(Collectors.joining(",", "[", "]"))));
    }

    public static Method getMethodObject(Class<?> clazz, String string, Class<?> ... classArray) {
        try {
            return clazz.getMethod(string, classArray);
        } catch (NoSuchMethodException | SecurityException exception) {
            return null;
        }
    }

    public static List<Method> getMethodsListWithAnnotation(Class<?> clazz, Class<? extends Annotation> clazz2) {
        return MethodUtils.getMethodsListWithAnnotation(clazz, clazz2, false, false);
    }

    public static List<Method> getMethodsListWithAnnotation(Class<?> clazz, Class<? extends Annotation> clazz3, boolean bl, boolean bl2) {
        Objects.requireNonNull(clazz, "cls");
        Objects.requireNonNull(clazz3, "annotationCls");
        ArrayList arrayList = bl ? MethodUtils.getAllSuperclassesAndInterfaces(clazz) : new ArrayList();
        arrayList.add(0, clazz);
        ArrayList<Method> arrayList2 = new ArrayList<Method>();
        arrayList.forEach(clazz2 -> {
            Method[] methodArray = bl2 ? clazz2.getDeclaredMethods() : clazz2.getMethods();
            Stream.of(methodArray).filter(method -> method.isAnnotationPresent(clazz3)).forEachOrdered(arrayList2::add);
        });
        return arrayList2;
    }

    public static Method[] getMethodsWithAnnotation(Class<?> clazz, Class<? extends Annotation> clazz2) {
        return MethodUtils.getMethodsWithAnnotation(clazz, clazz2, false, false);
    }

    public static Method[] getMethodsWithAnnotation(Class<?> clazz, Class<? extends Annotation> clazz2, boolean bl, boolean bl2) {
        return MethodUtils.getMethodsListWithAnnotation(clazz, clazz2, bl, bl2).toArray(ArrayUtils.EMPTY_METHOD_ARRAY);
    }

    public static Set<Method> getOverrideHierarchy(Method method, ClassUtils.Interfaces interfaces) {
        Objects.requireNonNull(method, "method");
        LinkedHashSet<Method> linkedHashSet = new LinkedHashSet<Method>();
        linkedHashSet.add(method);
        Object[] objectArray = method.getParameterTypes();
        Class<?> clazz = method.getDeclaringClass();
        Iterator<Class<?>> iterator = ClassUtils.hierarchy(clazz, interfaces).iterator();
        iterator.next();
        block0: while (iterator.hasNext()) {
            Class<?> clazz2 = iterator.next();
            Method method2 = MethodUtils.getMatchingAccessibleMethod(clazz2, method.getName(), objectArray);
            if (method2 == null) continue;
            if (Arrays.equals(method2.getParameterTypes(), objectArray)) {
                linkedHashSet.add(method2);
                continue;
            }
            Map<TypeVariable<?>, Type> map = TypeUtils.getTypeArguments(clazz, method2.getDeclaringClass());
            for (int i = 0; i < objectArray.length; ++i) {
                Type type;
                Type type2 = TypeUtils.unrollVariables(map, method.getGenericParameterTypes()[i]);
                if (!TypeUtils.equals(type2, type = TypeUtils.unrollVariables(map, method2.getGenericParameterTypes()[i]))) continue block0;
            }
            linkedHashSet.add(method2);
        }
        return linkedHashSet;
    }

    static Object[] getVarArgs(Object[] objectArray, Class<?>[] classArray) {
        if (objectArray.length == classArray.length && (objectArray[objectArray.length - 1] == null || objectArray[objectArray.length - 1].getClass().equals(classArray[classArray.length - 1]))) {
            return objectArray;
        }
        Object[] objectArray2 = ArrayUtils.arraycopy(objectArray, 0, 0, classArray.length - 1, () -> new Object[classArray.length]);
        Class<?> clazz = classArray[classArray.length - 1].getComponentType();
        int n = objectArray.length - classArray.length + 1;
        Object object = ArrayUtils.arraycopy(objectArray, classArray.length - 1, 0, n, n2 -> Array.newInstance(ClassUtils.primitiveToWrapper(clazz), n));
        if (clazz.isPrimitive()) {
            object = ArrayUtils.toPrimitive(object);
        }
        objectArray2[classArray.length - 1] = object;
        return objectArray2;
    }

    public static Object invokeExactMethod(Object object, String string) {
        return MethodUtils.invokeExactMethod(object, string, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
    }

    public static Object invokeExactMethod(Object object, String string, Object ... objectArray) {
        objectArray = ArrayUtils.nullToEmpty(objectArray);
        return MethodUtils.invokeExactMethod(object, string, objectArray, ClassUtils.toClass(objectArray));
    }

    public static Object invokeExactMethod(Object object, String string, Object[] objectArray, Class<?>[] classArray) {
        Objects.requireNonNull(object, "object");
        objectArray = ArrayUtils.nullToEmpty(objectArray);
        classArray = ArrayUtils.nullToEmpty(classArray);
        Class<?> clazz = object.getClass();
        Method method = MethodUtils.getAccessibleMethod(clazz, string, classArray);
        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + string + "() on object: " + clazz.getName());
        }
        return method.invoke(object, objectArray);
    }

    public static Object invokeExactStaticMethod(Class<?> clazz, String string, Object ... objectArray) {
        objectArray = ArrayUtils.nullToEmpty(objectArray);
        return MethodUtils.invokeExactStaticMethod(clazz, string, objectArray, ClassUtils.toClass(objectArray));
    }

    public static Object invokeExactStaticMethod(Class<?> clazz, String string, Object[] objectArray, Class<?>[] classArray) {
        objectArray = ArrayUtils.nullToEmpty(objectArray);
        Method method = MethodUtils.getAccessibleMethod(clazz, string, classArray = ArrayUtils.nullToEmpty(classArray));
        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + string + "() on class: " + clazz.getName());
        }
        return method.invoke(null, objectArray);
    }

    public static Object invokeMethod(Object object, boolean bl, String string) {
        return MethodUtils.invokeMethod(object, bl, string, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
    }

    public static Object invokeMethod(Object object, boolean bl, String string, Object ... objectArray) {
        objectArray = ArrayUtils.nullToEmpty(objectArray);
        return MethodUtils.invokeMethod(object, bl, string, objectArray, ClassUtils.toClass(objectArray));
    }

    public static Object invokeMethod(Object object, boolean bl, String string, Object[] objectArray, Class<?>[] classArray) {
        Method method;
        String string2;
        Objects.requireNonNull(object, "object");
        classArray = ArrayUtils.nullToEmpty(classArray);
        objectArray = ArrayUtils.nullToEmpty(objectArray);
        Class<?> clazz = object.getClass();
        if (bl) {
            string2 = "No such method: ";
            method = MethodUtils.getMatchingMethod(clazz, string, classArray);
            if (method != null && !method.isAccessible()) {
                method.setAccessible(true);
            }
        } else {
            string2 = "No such accessible method: ";
            method = MethodUtils.getMatchingAccessibleMethod(clazz, string, classArray);
        }
        if (method == null) {
            throw new NoSuchMethodException(string2 + string + "() on object: " + clazz.getName());
        }
        objectArray = MethodUtils.toVarArgs(method, objectArray);
        return method.invoke(object, objectArray);
    }

    public static Object invokeMethod(Object object, String string) {
        return MethodUtils.invokeMethod(object, string, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
    }

    public static Object invokeMethod(Object object, String string, Object ... objectArray) {
        objectArray = ArrayUtils.nullToEmpty(objectArray);
        return MethodUtils.invokeMethod(object, string, objectArray, ClassUtils.toClass(objectArray));
    }

    public static Object invokeMethod(Object object, String string, Object[] objectArray, Class<?>[] classArray) {
        return MethodUtils.invokeMethod(object, false, string, objectArray, classArray);
    }

    public static Object invokeStaticMethod(Class<?> clazz, String string, Object ... objectArray) {
        objectArray = ArrayUtils.nullToEmpty(objectArray);
        return MethodUtils.invokeStaticMethod(clazz, string, objectArray, ClassUtils.toClass(objectArray));
    }

    public static Object invokeStaticMethod(Class<?> clazz, String string, Object[] objectArray, Class<?>[] classArray) {
        objectArray = ArrayUtils.nullToEmpty(objectArray);
        Method method = MethodUtils.getMatchingAccessibleMethod(clazz, string, classArray = ArrayUtils.nullToEmpty(classArray));
        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + string + "() on class: " + clazz.getName());
        }
        objectArray = MethodUtils.toVarArgs(method, objectArray);
        return method.invoke(null, objectArray);
    }

    private static Object[] toVarArgs(Method method, Object[] objectArray) {
        if (method.isVarArgs()) {
            Class<?>[] classArray = method.getParameterTypes();
            objectArray = MethodUtils.getVarArgs(objectArray, classArray);
        }
        return objectArray;
    }

    @Deprecated
    public MethodUtils() {
    }
}

