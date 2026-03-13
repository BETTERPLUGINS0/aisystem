/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.ClassUtils;

final class MemberUtils {
    private static final int ACCESS_TEST = 7;
    private static final Class<?>[] ORDERED_PRIMITIVE_TYPES = new Class[]{Byte.TYPE, Short.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};

    MemberUtils() {
    }

    static int compareConstructorFit(Constructor<?> constructor, Constructor<?> constructor2, Class<?>[] classArray) {
        return MemberUtils.compareParameterTypes(Executable.of(constructor), Executable.of(constructor2), classArray);
    }

    static int compareMethodFit(Method method, Method method2, Class<?>[] classArray) {
        return MemberUtils.compareParameterTypes(Executable.of(method), Executable.of(method2), classArray);
    }

    private static int compareParameterTypes(Executable executable, Executable executable2, Class<?>[] classArray) {
        float f = MemberUtils.getTotalTransformationCost(classArray, executable);
        float f2 = MemberUtils.getTotalTransformationCost(classArray, executable2);
        return Float.compare(f, f2);
    }

    private static float getObjectTransformationCost(Class<?> clazz, Class<?> clazz2) {
        if (clazz2.isPrimitive()) {
            return MemberUtils.getPrimitivePromotionCost(clazz, clazz2);
        }
        float f = 0.0f;
        while (clazz != null && !clazz2.equals(clazz)) {
            if (clazz2.isInterface() && ClassUtils.isAssignable(clazz, clazz2)) {
                f += 0.25f;
                break;
            }
            f += 1.0f;
            clazz = clazz.getSuperclass();
        }
        if (clazz == null) {
            f += 1.5f;
        }
        return f;
    }

    private static float getPrimitivePromotionCost(Class<?> clazz, Class<?> clazz2) {
        if (clazz == null) {
            return 1.5f;
        }
        float f = 0.0f;
        Class<?> clazz3 = clazz;
        if (!clazz3.isPrimitive()) {
            f += 0.1f;
            clazz3 = ClassUtils.wrapperToPrimitive(clazz3);
        }
        for (int i = 0; clazz3 != clazz2 && i < ORDERED_PRIMITIVE_TYPES.length; ++i) {
            if (clazz3 != ORDERED_PRIMITIVE_TYPES[i]) continue;
            f += 0.1f;
            if (i >= ORDERED_PRIMITIVE_TYPES.length - 1) continue;
            clazz3 = ORDERED_PRIMITIVE_TYPES[i + 1];
        }
        return f;
    }

    private static float getTotalTransformationCost(Class<?>[] classArray, Executable executable) {
        long l;
        Class<?>[] classArray2 = executable.getParameterTypes();
        boolean bl = executable.isVarArgs();
        float f = 0.0f;
        long l2 = l = bl ? (long)(classArray2.length - 1) : (long)classArray2.length;
        if ((long)classArray.length < l) {
            return Float.MAX_VALUE;
        }
        int n = 0;
        while ((long)n < l) {
            f += MemberUtils.getObjectTransformationCost(classArray[n], classArray2[n]);
            ++n;
        }
        if (bl) {
            n = classArray.length < classArray2.length ? 1 : 0;
            boolean bl2 = classArray.length == classArray2.length && classArray[classArray.length - 1] != null && classArray[classArray.length - 1].isArray();
            float f2 = 0.001f;
            Class<?> clazz = classArray2[classArray2.length - 1].getComponentType();
            if (n != 0) {
                f += MemberUtils.getObjectTransformationCost(clazz, Object.class) + 0.001f;
            } else if (bl2) {
                Class<?> clazz2 = classArray[classArray.length - 1].getComponentType();
                f += MemberUtils.getObjectTransformationCost(clazz2, clazz) + 0.001f;
            } else {
                for (int i = classArray2.length - 1; i < classArray.length; ++i) {
                    Class<?> clazz3 = classArray[i];
                    f += MemberUtils.getObjectTransformationCost(clazz3, clazz) + 0.001f;
                }
            }
        }
        return f;
    }

    static boolean isAccessible(Member member) {
        return MemberUtils.isPublic(member) && !member.isSynthetic();
    }

    static boolean isMatchingConstructor(Constructor<?> constructor, Class<?>[] classArray) {
        return MemberUtils.isMatchingExecutable(Executable.of(constructor), classArray);
    }

    private static boolean isMatchingExecutable(Executable executable, Class<?>[] classArray) {
        Class<?>[] classArray2 = executable.getParameterTypes();
        if (ClassUtils.isAssignable(classArray, classArray2, true)) {
            return true;
        }
        if (executable.isVarArgs()) {
            int n;
            for (n = 0; n < classArray2.length - 1 && n < classArray.length; ++n) {
                if (ClassUtils.isAssignable(classArray[n], classArray2[n], true)) continue;
                return false;
            }
            Class<?> clazz = classArray2[classArray2.length - 1].getComponentType();
            while (n < classArray.length) {
                if (!ClassUtils.isAssignable(classArray[n], clazz, true)) {
                    return false;
                }
                ++n;
            }
            return true;
        }
        return false;
    }

    static boolean isMatchingMethod(Method method, Class<?>[] classArray) {
        return MemberUtils.isMatchingExecutable(Executable.of(method), classArray);
    }

    static boolean isPackageAccess(int n) {
        return (n & 7) == 0;
    }

    static boolean isPublic(Member member) {
        return member != null && Modifier.isPublic(member.getModifiers());
    }

    static boolean isStatic(Member member) {
        return member != null && Modifier.isStatic(member.getModifiers());
    }

    static <T extends AccessibleObject> T setAccessibleWorkaround(T t) {
        if (t == null || t.isAccessible()) {
            return t;
        }
        Member member = (Member)((Object)t);
        if (!t.isAccessible() && MemberUtils.isPublic(member) && MemberUtils.isPackageAccess(member.getDeclaringClass().getModifiers())) {
            try {
                t.setAccessible(true);
                return t;
            } catch (SecurityException securityException) {
                // empty catch block
            }
        }
        return t;
    }

    private static final class Executable {
        private final Class<?>[] parameterTypes;
        private final boolean isVarArgs;

        private static Executable of(Constructor<?> constructor) {
            return new Executable(constructor);
        }

        private static Executable of(Method method) {
            return new Executable(method);
        }

        private Executable(Constructor<?> constructor) {
            this.parameterTypes = constructor.getParameterTypes();
            this.isVarArgs = constructor.isVarArgs();
        }

        private Executable(Method method) {
            this.parameterTypes = method.getParameterTypes();
            this.isVarArgs = method.isVarArgs();
        }

        public Class<?>[] getParameterTypes() {
            return this.parameterTypes;
        }

        public boolean isVarArgs() {
            return this.isVarArgs;
        }
    }
}

