/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.CloneFailedException;
import org.apache.commons.lang3.function.Suppliers;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.stream.Streams;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.time.DurationUtils;

public class ObjectUtils {
    private static final char AT_SIGN = '@';
    public static final Null NULL = new Null();

    public static boolean allNotNull(Object ... objectArray) {
        return objectArray != null && Stream.of(objectArray).noneMatch(Objects::isNull);
    }

    public static boolean allNull(Object ... objectArray) {
        return !ObjectUtils.anyNotNull(objectArray);
    }

    public static boolean anyNotNull(Object ... objectArray) {
        return ObjectUtils.firstNonNull(objectArray) != null;
    }

    public static boolean anyNull(Object ... objectArray) {
        return !ObjectUtils.allNotNull(objectArray);
    }

    public static <T> T clone(T t) {
        if (t instanceof Cloneable) {
            Object object;
            Class<?> clazz = t.getClass();
            if (ObjectUtils.isArray(t)) {
                Class<?> clazz2 = clazz.getComponentType();
                if (clazz2.isPrimitive()) {
                    int n = Array.getLength(t);
                    object = Array.newInstance(clazz2, n);
                    while (n-- > 0) {
                        Array.set(object, n, Array.get(t, n));
                    }
                } else {
                    object = ((Object[])t).clone();
                }
            } else {
                try {
                    object = clazz.getMethod("clone", new Class[0]).invoke(t, new Object[0]);
                } catch (ReflectiveOperationException reflectiveOperationException) {
                    throw new CloneFailedException("Exception cloning Cloneable type " + clazz.getName(), reflectiveOperationException);
                }
            }
            return (T)object;
        }
        return null;
    }

    public static <T> T cloneIfPossible(T t) {
        T t2 = ObjectUtils.clone(t);
        return t2 == null ? t : t2;
    }

    public static <T extends Comparable<? super T>> int compare(T t, T t2) {
        return ObjectUtils.compare(t, t2, false);
    }

    public static <T extends Comparable<? super T>> int compare(T t, T t2, boolean bl) {
        if (t == t2) {
            return 0;
        }
        if (t == null) {
            return bl ? 1 : -1;
        }
        if (t2 == null) {
            return bl ? -1 : 1;
        }
        return t.compareTo(t2);
    }

    public static boolean CONST(boolean bl) {
        return bl;
    }

    public static byte CONST(byte by) {
        return by;
    }

    public static char CONST(char c2) {
        return c2;
    }

    public static double CONST(double d) {
        return d;
    }

    public static float CONST(float f) {
        return f;
    }

    public static int CONST(int n) {
        return n;
    }

    public static long CONST(long l) {
        return l;
    }

    public static short CONST(short s) {
        return s;
    }

    public static <T> T CONST(T t) {
        return t;
    }

    public static byte CONST_BYTE(int n) {
        if (n < -128 || n > 127) {
            throw new IllegalArgumentException("Supplied value must be a valid byte literal between -128 and 127: [" + n + "]");
        }
        return (byte)n;
    }

    public static short CONST_SHORT(int n) {
        if (n < Short.MIN_VALUE || n > Short.MAX_VALUE) {
            throw new IllegalArgumentException("Supplied value must be a valid byte literal between -32768 and 32767: [" + n + "]");
        }
        return (short)n;
    }

    public static <T> T defaultIfNull(T t, T t2) {
        return t != null ? t : t2;
    }

    @Deprecated
    public static boolean equals(Object object, Object object2) {
        return Objects.equals(object, object2);
    }

    @SafeVarargs
    public static <T> T firstNonNull(T ... TArray) {
        return Streams.of(TArray).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public static <T> Class<T> getClass(T t) {
        return t == null ? null : t.getClass();
    }

    @SafeVarargs
    public static <T> T getFirstNonNull(Supplier<T> ... supplierArray) {
        return Streams.of(supplierArray).filter(Objects::nonNull).map(Supplier::get).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public static <T> T getIfNull(T t, Supplier<T> supplier) {
        return t != null ? t : Suppliers.get(supplier);
    }

    @Deprecated
    public static int hashCode(Object object) {
        return Objects.hashCode(object);
    }

    public static String hashCodeHex(Object object) {
        return Integer.toHexString(Objects.hashCode(object));
    }

    @Deprecated
    public static int hashCodeMulti(Object ... objectArray) {
        int n = 1;
        if (objectArray != null) {
            for (Object object : objectArray) {
                int n2 = Objects.hashCode(object);
                n = n * 31 + n2;
            }
        }
        return n;
    }

    public static String identityHashCodeHex(Object object) {
        return Integer.toHexString(System.identityHashCode(object));
    }

    public static void identityToString(Appendable appendable, Object object) {
        Objects.requireNonNull(object, "object");
        appendable.append(object.getClass().getName()).append('@').append(ObjectUtils.identityHashCodeHex(object));
    }

    public static String identityToString(Object object) {
        if (object == null) {
            return null;
        }
        String string = object.getClass().getName();
        String string2 = ObjectUtils.identityHashCodeHex(object);
        StringBuilder stringBuilder = new StringBuilder(string.length() + 1 + string2.length());
        stringBuilder.append(string).append('@').append(string2);
        return stringBuilder.toString();
    }

    @Deprecated
    public static void identityToString(StrBuilder strBuilder, Object object) {
        Objects.requireNonNull(object, "object");
        String string = object.getClass().getName();
        String string2 = ObjectUtils.identityHashCodeHex(object);
        strBuilder.ensureCapacity(strBuilder.length() + string.length() + 1 + string2.length());
        strBuilder.append(string).append('@').append(string2);
    }

    public static void identityToString(StringBuffer stringBuffer, Object object) {
        Objects.requireNonNull(object, "object");
        String string = object.getClass().getName();
        String string2 = ObjectUtils.identityHashCodeHex(object);
        stringBuffer.ensureCapacity(stringBuffer.length() + string.length() + 1 + string2.length());
        stringBuffer.append(string).append('@').append(string2);
    }

    public static void identityToString(StringBuilder stringBuilder, Object object) {
        Objects.requireNonNull(object, "object");
        String string = object.getClass().getName();
        String string2 = ObjectUtils.identityHashCodeHex(object);
        stringBuilder.ensureCapacity(stringBuilder.length() + string.length() + 1 + string2.length());
        stringBuilder.append(string).append('@').append(string2);
    }

    public static boolean isArray(Object object) {
        return object != null && object.getClass().isArray();
    }

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof CharSequence) {
            return ((CharSequence)object).length() == 0;
        }
        if (ObjectUtils.isArray(object)) {
            return Array.getLength(object) == 0;
        }
        if (object instanceof Collection) {
            return ((Collection)object).isEmpty();
        }
        if (object instanceof Map) {
            return ((Map)object).isEmpty();
        }
        if (object instanceof Optional) {
            return !((Optional)object).isPresent();
        }
        return false;
    }

    public static boolean isNotEmpty(Object object) {
        return !ObjectUtils.isEmpty(object);
    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> T max(T ... TArray) {
        T t = null;
        if (TArray != null) {
            for (T t2 : TArray) {
                if (ObjectUtils.compare(t2, t, false) <= 0) continue;
                t = t2;
            }
        }
        return t;
    }

    @SafeVarargs
    public static <T> T median(Comparator<T> comparator, T ... TArray) {
        Validate.notEmpty(TArray, "null/empty items", new Object[0]);
        Validate.noNullElements(TArray);
        Objects.requireNonNull(comparator, "comparator");
        TreeSet<T> treeSet = new TreeSet<T>(comparator);
        Collections.addAll(treeSet, TArray);
        return (T)treeSet.toArray()[(treeSet.size() - 1) / 2];
    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> T median(T ... TArray) {
        Validate.notEmpty(TArray);
        Validate.noNullElements(TArray);
        TreeSet treeSet = new TreeSet();
        Collections.addAll(treeSet, TArray);
        return (T)((Comparable)treeSet.toArray()[(treeSet.size() - 1) / 2]);
    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> T min(T ... TArray) {
        T t = null;
        if (TArray != null) {
            for (T t2 : TArray) {
                if (ObjectUtils.compare(t2, t, true) >= 0) continue;
                t = t2;
            }
        }
        return t;
    }

    /*
     * WARNING - void declaration
     */
    @SafeVarargs
    public static <T> T mode(T ... TArray) {
        if (ArrayUtils.isNotEmpty(TArray)) {
            void var2_4;
            HashMap<T, MutableInt> hashMap = new HashMap<T, MutableInt>(TArray.length);
            for (T t : TArray) {
                MutableInt mutableInt = (MutableInt)hashMap.get(t);
                if (mutableInt == null) {
                    hashMap.put(t, new MutableInt(1));
                    continue;
                }
                mutableInt.increment();
            }
            Object var2_3 = null;
            int n = 0;
            for (Map.Entry entry : hashMap.entrySet()) {
                int n2 = ((MutableInt)entry.getValue()).intValue();
                if (n2 == n) {
                    Object var2_5 = null;
                    continue;
                }
                if (n2 <= n) continue;
                n = n2;
                Object k = entry.getKey();
            }
            return var2_4;
        }
        return null;
    }

    public static boolean notEqual(Object object, Object object2) {
        return !Objects.equals(object, object2);
    }

    public static <T> T requireNonEmpty(T t) {
        return ObjectUtils.requireNonEmpty(t, "object");
    }

    public static <T> T requireNonEmpty(T t, String string) {
        Objects.requireNonNull(t, string);
        if (ObjectUtils.isEmpty(t)) {
            throw new IllegalArgumentException(string);
        }
        return t;
    }

    @Deprecated
    public static String toString(Object object) {
        return Objects.toString(object, "");
    }

    @Deprecated
    public static String toString(Object object, String string) {
        return Objects.toString(object, string);
    }

    public static String toString(Supplier<Object> supplier, Supplier<String> supplier2) {
        return supplier == null ? Suppliers.get(supplier2) : ObjectUtils.toString(supplier.get(), supplier2);
    }

    public static <T> String toString(T t, Supplier<String> supplier) {
        return t == null ? Suppliers.get(supplier) : t.toString();
    }

    public static void wait(Object object, Duration duration) {
        DurationUtils.accept(object::wait, DurationUtils.zeroIfNull(duration));
    }

    @Deprecated
    public ObjectUtils() {
    }

    public static class Null
    implements Serializable {
        private static final long serialVersionUID = 7092611880189329093L;

        Null() {
        }

        private Object readResolve() {
            return NULL;
        }
    }
}

