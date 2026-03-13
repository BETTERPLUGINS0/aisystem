/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableObject;

public class ClassUtils {
    private static final Comparator<Class<?>> COMPARATOR = (clazz, clazz2) -> Objects.compare(ClassUtils.getName(clazz), ClassUtils.getName(clazz2), String::compareTo);
    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    public static final String PACKAGE_SEPARATOR = String.valueOf('.');
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    public static final String INNER_CLASS_SEPARATOR = String.valueOf('$');
    private static final Map<String, Class<?>> namePrimitiveMap = new HashMap();
    private static final Map<Class<?>, Class<?>> primitiveWrapperMap;
    private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap;
    private static final Map<String, String> abbreviationMap;
    private static final Map<String, String> reverseAbbreviationMap;

    public static Comparator<Class<?>> comparator() {
        return COMPARATOR;
    }

    public static List<String> convertClassesToClassNames(List<Class<?>> list) {
        return list == null ? null : list.stream().map(clazz -> ClassUtils.getName(clazz, null)).collect(Collectors.toList());
    }

    public static List<Class<?>> convertClassNamesToClasses(List<String> list) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        list.forEach(string -> {
            try {
                arrayList.add(Class.forName(string));
            } catch (Exception exception) {
                arrayList.add(null);
            }
        });
        return arrayList;
    }

    public static String getAbbreviatedName(Class<?> clazz, int n) {
        if (clazz == null) {
            return "";
        }
        return ClassUtils.getAbbreviatedName(clazz.getName(), n);
    }

    public static String getAbbreviatedName(String string, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("len must be > 0");
        }
        if (string == null) {
            return "";
        }
        if (string.length() <= n) {
            return string;
        }
        char[] cArray = string.toCharArray();
        int n2 = 0;
        int n3 = 0;
        while (n3 < cArray.length) {
            int n4 = n2;
            while (n3 < cArray.length && cArray[n3] != '.') {
                cArray[n4++] = cArray[n3++];
            }
            if (ClassUtils.useFull(n4, n3, cArray.length, n) || ++n2 > n4) {
                n2 = n4;
            }
            if (n3 >= cArray.length) continue;
            cArray[n2++] = cArray[n3++];
        }
        return new String(cArray, 0, n2);
    }

    public static List<Class<?>> getAllInterfaces(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        ClassUtils.getAllInterfaces(clazz, linkedHashSet);
        return new ArrayList(linkedHashSet);
    }

    private static void getAllInterfaces(Class<?> clazz, HashSet<Class<?>> hashSet) {
        while (clazz != null) {
            Class<?>[] classArray;
            for (Class<?> clazz2 : classArray = clazz.getInterfaces()) {
                if (!hashSet.add(clazz2)) continue;
                ClassUtils.getAllInterfaces(clazz2, hashSet);
            }
            clazz = clazz.getSuperclass();
        }
    }

    public static List<Class<?>> getAllSuperclasses(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (Class<?> clazz2 = clazz.getSuperclass(); clazz2 != null; clazz2 = clazz2.getSuperclass()) {
            arrayList.add(clazz2);
        }
        return arrayList;
    }

    public static String getCanonicalName(Class<?> clazz) {
        return ClassUtils.getCanonicalName(clazz, "");
    }

    public static String getCanonicalName(Class<?> clazz, String string) {
        if (clazz == null) {
            return string;
        }
        String string2 = clazz.getCanonicalName();
        return string2 == null ? string : string2;
    }

    public static String getCanonicalName(Object object) {
        return ClassUtils.getCanonicalName(object, "");
    }

    public static String getCanonicalName(Object object, String string) {
        if (object == null) {
            return string;
        }
        String string2 = object.getClass().getCanonicalName();
        return string2 == null ? string : string2;
    }

    private static String getCanonicalName(String string) {
        if ((string = StringUtils.deleteWhitespace(string)) == null) {
            return null;
        }
        int n = 0;
        while (string.startsWith("[")) {
            ++n;
            string = string.substring(1);
        }
        if (n < 1) {
            return string;
        }
        if (string.startsWith("L")) {
            string = string.substring(1, string.endsWith(";") ? string.length() - 1 : string.length());
        } else if (!string.isEmpty()) {
            string = reverseAbbreviationMap.get(string.substring(0, 1));
        }
        StringBuilder stringBuilder = new StringBuilder(string);
        for (int i = 0; i < n; ++i) {
            stringBuilder.append("[]");
        }
        return stringBuilder.toString();
    }

    public static Class<?> getClass(ClassLoader classLoader, String string) {
        return ClassUtils.getClass(classLoader, string, true);
    }

    public static Class<?> getClass(ClassLoader classLoader, String string, boolean bl) {
        try {
            Class<?> clazz = ClassUtils.getPrimitiveClass(string);
            return clazz != null ? clazz : Class.forName(ClassUtils.toCanonicalName(string), bl, classLoader);
        } catch (ClassNotFoundException classNotFoundException) {
            int n = string.lastIndexOf(46);
            if (n != -1) {
                try {
                    return ClassUtils.getClass(classLoader, string.substring(0, n) + '$' + string.substring(n + 1), bl);
                } catch (ClassNotFoundException classNotFoundException2) {
                    // empty catch block
                }
            }
            throw classNotFoundException;
        }
    }

    public static Class<?> getClass(String string) {
        return ClassUtils.getClass(string, true);
    }

    public static Class<?> getClass(String string, boolean bl) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader classLoader2 = classLoader == null ? ClassUtils.class.getClassLoader() : classLoader;
        return ClassUtils.getClass(classLoader2, string, bl);
    }

    public static <T> Class<T> getComponentType(Class<T[]> clazz) {
        return clazz == null ? null : clazz.getComponentType();
    }

    public static String getName(Class<?> clazz) {
        return ClassUtils.getName(clazz, "");
    }

    public static String getName(Class<?> clazz, String string) {
        return clazz == null ? string : clazz.getName();
    }

    public static String getName(Object object) {
        return ClassUtils.getName(object, "");
    }

    public static String getName(Object object, String string) {
        return object == null ? string : object.getClass().getName();
    }

    public static String getPackageCanonicalName(Class<?> clazz) {
        if (clazz == null) {
            return "";
        }
        return ClassUtils.getPackageCanonicalName(clazz.getName());
    }

    public static String getPackageCanonicalName(Object object, String string) {
        if (object == null) {
            return string;
        }
        return ClassUtils.getPackageCanonicalName(object.getClass().getName());
    }

    public static String getPackageCanonicalName(String string) {
        return ClassUtils.getPackageName(ClassUtils.getCanonicalName(string));
    }

    public static String getPackageName(Class<?> clazz) {
        if (clazz == null) {
            return "";
        }
        return ClassUtils.getPackageName(clazz.getName());
    }

    public static String getPackageName(Object object, String string) {
        if (object == null) {
            return string;
        }
        return ClassUtils.getPackageName(object.getClass());
    }

    public static String getPackageName(String string) {
        int n;
        if (StringUtils.isEmpty(string)) {
            return "";
        }
        while (string.charAt(0) == '[') {
            string = string.substring(1);
        }
        if (string.charAt(0) == 'L' && string.charAt(string.length() - 1) == ';') {
            string = string.substring(1);
        }
        if ((n = string.lastIndexOf(46)) == -1) {
            return "";
        }
        return string.substring(0, n);
    }

    static Class<?> getPrimitiveClass(String string) {
        return namePrimitiveMap.get(string);
    }

    public static Method getPublicMethod(Class<?> clazz, String string, Class<?> ... classArray) {
        Method method = clazz.getMethod(string, classArray);
        if (ClassUtils.isPublic(method.getDeclaringClass())) {
            return method;
        }
        ArrayList arrayList = new ArrayList(ClassUtils.getAllInterfaces(clazz));
        arrayList.addAll(ClassUtils.getAllSuperclasses(clazz));
        for (Class clazz2 : arrayList) {
            Method method2;
            if (!ClassUtils.isPublic(clazz2)) continue;
            try {
                method2 = clazz2.getMethod(string, classArray);
            } catch (NoSuchMethodException noSuchMethodException) {
                continue;
            }
            if (!Modifier.isPublic(method2.getDeclaringClass().getModifiers())) continue;
            return method2;
        }
        throw new NoSuchMethodException("Can't find a public method for " + string + " " + ArrayUtils.toString(classArray));
    }

    public static String getShortCanonicalName(Class<?> clazz) {
        return clazz == null ? "" : ClassUtils.getShortCanonicalName(clazz.getCanonicalName());
    }

    public static String getShortCanonicalName(Object object, String string) {
        return object == null ? string : ClassUtils.getShortCanonicalName(object.getClass().getCanonicalName());
    }

    public static String getShortCanonicalName(String string) {
        return ClassUtils.getShortClassName(ClassUtils.getCanonicalName(string));
    }

    public static String getShortClassName(Class<?> clazz) {
        if (clazz == null) {
            return "";
        }
        return ClassUtils.getShortClassName(clazz.getName());
    }

    public static String getShortClassName(Object object, String string) {
        if (object == null) {
            return string;
        }
        return ClassUtils.getShortClassName(object.getClass());
    }

    public static String getShortClassName(String string) {
        int n;
        if (StringUtils.isEmpty(string)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (string.startsWith("[")) {
            while (string.charAt(0) == '[') {
                string = string.substring(1);
                stringBuilder.append("[]");
            }
            if (string.charAt(0) == 'L' && string.charAt(string.length() - 1) == ';') {
                string = string.substring(1, string.length() - 1);
            }
            if (reverseAbbreviationMap.containsKey(string)) {
                string = reverseAbbreviationMap.get(string);
            }
        }
        int n2 = string.indexOf(36, (n = string.lastIndexOf(46)) == -1 ? 0 : n + 1);
        String string2 = string.substring(n + 1);
        if (n2 != -1) {
            string2 = string2.replace('$', '.');
        }
        return string2 + stringBuilder;
    }

    public static String getSimpleName(Class<?> clazz) {
        return ClassUtils.getSimpleName(clazz, "");
    }

    public static String getSimpleName(Class<?> clazz, String string) {
        return clazz == null ? string : clazz.getSimpleName();
    }

    public static String getSimpleName(Object object) {
        return ClassUtils.getSimpleName(object, "");
    }

    public static String getSimpleName(Object object, String string) {
        return object == null ? string : object.getClass().getSimpleName();
    }

    public static Iterable<Class<?>> hierarchy(Class<?> clazz) {
        return ClassUtils.hierarchy(clazz, Interfaces.EXCLUDE);
    }

    public static Iterable<Class<?>> hierarchy(Class<?> clazz, Interfaces interfaces) {
        Iterable<Class<?>> iterable = () -> {
            final MutableObject<Class> mutableObject = new MutableObject<Class>(clazz);
            return new Iterator<Class<?>>(){

                @Override
                public boolean hasNext() {
                    return mutableObject.getValue() != null;
                }

                @Override
                public Class<?> next() {
                    Class clazz = (Class)mutableObject.getValue();
                    mutableObject.setValue(clazz.getSuperclass());
                    return clazz;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        };
        if (interfaces != Interfaces.INCLUDE) {
            return iterable;
        }
        return () -> {
            final HashSet hashSet = new HashSet();
            final Iterator iterator = iterable.iterator();
            return new Iterator<Class<?>>(){
                Iterator<Class<?>> interfaces = Collections.emptyIterator();

                @Override
                public boolean hasNext() {
                    return this.interfaces.hasNext() || iterator.hasNext();
                }

                @Override
                public Class<?> next() {
                    if (this.interfaces.hasNext()) {
                        Class<?> clazz = this.interfaces.next();
                        hashSet.add(clazz);
                        return clazz;
                    }
                    Class clazz = (Class)iterator.next();
                    LinkedHashSet linkedHashSet = new LinkedHashSet();
                    this.walkInterfaces(linkedHashSet, clazz);
                    this.interfaces = linkedHashSet.iterator();
                    return clazz;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }

                private void walkInterfaces(Set<Class<?>> set, Class<?> clazz) {
                    for (Class<?> clazz2 : clazz.getInterfaces()) {
                        if (!hashSet.contains(clazz2)) {
                            set.add(clazz2);
                        }
                        this.walkInterfaces(set, clazz2);
                    }
                }
            };
        };
    }

    public static boolean isAssignable(Class<?> clazz, Class<?> clazz2) {
        return ClassUtils.isAssignable(clazz, clazz2, true);
    }

    public static boolean isAssignable(Class<?> clazz, Class<?> clazz2, boolean bl) {
        if (clazz2 == null) {
            return false;
        }
        if (clazz == null) {
            return !clazz2.isPrimitive();
        }
        if (bl) {
            if (clazz.isPrimitive() && !clazz2.isPrimitive() && (clazz = ClassUtils.primitiveToWrapper(clazz)) == null) {
                return false;
            }
            if (clazz2.isPrimitive() && !clazz.isPrimitive() && (clazz = ClassUtils.wrapperToPrimitive(clazz)) == null) {
                return false;
            }
        }
        if (clazz.equals(clazz2)) {
            return true;
        }
        if (clazz.isPrimitive()) {
            if (!clazz2.isPrimitive()) {
                return false;
            }
            if (Integer.TYPE.equals(clazz)) {
                return Long.TYPE.equals(clazz2) || Float.TYPE.equals(clazz2) || Double.TYPE.equals(clazz2);
            }
            if (Long.TYPE.equals(clazz)) {
                return Float.TYPE.equals(clazz2) || Double.TYPE.equals(clazz2);
            }
            if (Boolean.TYPE.equals(clazz)) {
                return false;
            }
            if (Double.TYPE.equals(clazz)) {
                return false;
            }
            if (Float.TYPE.equals(clazz)) {
                return Double.TYPE.equals(clazz2);
            }
            if (Character.TYPE.equals(clazz) || Short.TYPE.equals(clazz)) {
                return Integer.TYPE.equals(clazz2) || Long.TYPE.equals(clazz2) || Float.TYPE.equals(clazz2) || Double.TYPE.equals(clazz2);
            }
            if (Byte.TYPE.equals(clazz)) {
                return Short.TYPE.equals(clazz2) || Integer.TYPE.equals(clazz2) || Long.TYPE.equals(clazz2) || Float.TYPE.equals(clazz2) || Double.TYPE.equals(clazz2);
            }
            return false;
        }
        return clazz2.isAssignableFrom(clazz);
    }

    public static boolean isAssignable(Class<?>[] classArray, Class<?> ... classArray2) {
        return ClassUtils.isAssignable(classArray, classArray2, true);
    }

    public static boolean isAssignable(Class<?>[] classArray, Class<?>[] classArray2, boolean bl) {
        if (!ArrayUtils.isSameLength(classArray, classArray2)) {
            return false;
        }
        classArray = ArrayUtils.nullToEmpty(classArray);
        classArray2 = ArrayUtils.nullToEmpty(classArray2);
        for (int i = 0; i < classArray.length; ++i) {
            if (ClassUtils.isAssignable(classArray[i], classArray2[i], bl)) continue;
            return false;
        }
        return true;
    }

    public static boolean isInnerClass(Class<?> clazz) {
        return clazz != null && clazz.getEnclosingClass() != null;
    }

    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return clazz.isPrimitive() || ClassUtils.isPrimitiveWrapper(clazz);
    }

    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        return wrapperPrimitiveMap.containsKey(clazz);
    }

    public static boolean isPublic(Class<?> clazz) {
        return Modifier.isPublic(clazz.getModifiers());
    }

    public static Class<?>[] primitivesToWrappers(Class<?> ... classArray) {
        if (classArray == null) {
            return null;
        }
        if (classArray.length == 0) {
            return classArray;
        }
        Class[] classArray2 = new Class[classArray.length];
        Arrays.setAll(classArray2, n -> ClassUtils.primitiveToWrapper(classArray[n]));
        return classArray2;
    }

    public static Class<?> primitiveToWrapper(Class<?> clazz) {
        Class<?> clazz2 = clazz;
        if (clazz != null && clazz.isPrimitive()) {
            clazz2 = primitiveWrapperMap.get(clazz);
        }
        return clazz2;
    }

    private static String toCanonicalName(String string) {
        String string2 = StringUtils.deleteWhitespace(string);
        Objects.requireNonNull(string2, "className");
        if (string2.endsWith("[]")) {
            StringBuilder stringBuilder = new StringBuilder();
            while (string2.endsWith("[]")) {
                string2 = string2.substring(0, string2.length() - 2);
                stringBuilder.append("[");
            }
            String string3 = abbreviationMap.get(string2);
            if (string3 != null) {
                stringBuilder.append(string3);
            } else {
                stringBuilder.append("L").append(string2).append(";");
            }
            string2 = stringBuilder.toString();
        }
        return string2;
    }

    public static Class<?>[] toClass(Object ... objectArray) {
        if (objectArray == null) {
            return null;
        }
        if (objectArray.length == 0) {
            return ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        Class[] classArray = new Class[objectArray.length];
        Arrays.setAll(classArray, n -> objectArray[n] == null ? null : objectArray[n].getClass());
        return classArray;
    }

    private static boolean useFull(int n, int n2, int n3, int n4) {
        return n2 >= n3 || n + n3 - n2 <= n4;
    }

    public static Class<?>[] wrappersToPrimitives(Class<?> ... classArray) {
        if (classArray == null) {
            return null;
        }
        if (classArray.length == 0) {
            return classArray;
        }
        Class[] classArray2 = new Class[classArray.length];
        Arrays.setAll(classArray2, n -> ClassUtils.wrapperToPrimitive(classArray[n]));
        return classArray2;
    }

    public static Class<?> wrapperToPrimitive(Class<?> clazz) {
        return wrapperPrimitiveMap.get(clazz);
    }

    @Deprecated
    public ClassUtils() {
    }

    static {
        namePrimitiveMap.put(Boolean.TYPE.getSimpleName(), Boolean.TYPE);
        namePrimitiveMap.put(Byte.TYPE.getSimpleName(), Byte.TYPE);
        namePrimitiveMap.put(Character.TYPE.getSimpleName(), Character.TYPE);
        namePrimitiveMap.put(Double.TYPE.getSimpleName(), Double.TYPE);
        namePrimitiveMap.put(Float.TYPE.getSimpleName(), Float.TYPE);
        namePrimitiveMap.put(Integer.TYPE.getSimpleName(), Integer.TYPE);
        namePrimitiveMap.put(Long.TYPE.getSimpleName(), Long.TYPE);
        namePrimitiveMap.put(Short.TYPE.getSimpleName(), Short.TYPE);
        namePrimitiveMap.put(Void.TYPE.getSimpleName(), Void.TYPE);
        primitiveWrapperMap = new HashMap();
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
        primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
        wrapperPrimitiveMap = new HashMap();
        primitiveWrapperMap.forEach((clazz, clazz2) -> {
            if (!clazz.equals(clazz2)) {
                wrapperPrimitiveMap.put((Class<?>)clazz2, (Class<?>)clazz);
            }
        });
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("int", "I");
        hashMap.put("boolean", "Z");
        hashMap.put("float", "F");
        hashMap.put("long", "J");
        hashMap.put("short", "S");
        hashMap.put("byte", "B");
        hashMap.put("double", "D");
        hashMap.put("char", "C");
        abbreviationMap = Collections.unmodifiableMap(hashMap);
        reverseAbbreviationMap = Collections.unmodifiableMap(hashMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)));
    }

    public static enum Interfaces {
        INCLUDE,
        EXCLUDE;

    }
}

