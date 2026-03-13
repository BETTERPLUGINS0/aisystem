/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.ArraySorter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.Builder;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.apache.commons.lang3.builder.IDKey;
import org.apache.commons.lang3.builder.Reflection;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class HashCodeBuilder
implements Builder<Integer> {
    private static final int DEFAULT_INITIAL_VALUE = 17;
    private static final int DEFAULT_MULTIPLIER_VALUE = 37;
    private static final ThreadLocal<Set<IDKey>> REGISTRY = ThreadLocal.withInitial(HashSet::new);
    private final int iConstant;
    private int iTotal;

    static Set<IDKey> getRegistry() {
        return REGISTRY.get();
    }

    static boolean isRegistered(Object object) {
        Set<IDKey> set = HashCodeBuilder.getRegistry();
        return set != null && set.contains(new IDKey(object));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void reflectionAppend(Object object, Class<?> clazz, HashCodeBuilder hashCodeBuilder, boolean bl, String[] stringArray) {
        if (HashCodeBuilder.isRegistered(object)) {
            return;
        }
        try {
            HashCodeBuilder.register(object);
            AccessibleObject[] accessibleObjectArray = ArraySorter.sort(clazz.getDeclaredFields(), Comparator.comparing(Field::getName));
            AccessibleObject.setAccessible(accessibleObjectArray, true);
            for (AccessibleObject accessibleObject : accessibleObjectArray) {
                if (ArrayUtils.contains(stringArray, ((Field)accessibleObject).getName()) || ((Field)accessibleObject).getName().contains("$") || !bl && Modifier.isTransient(((Field)accessibleObject).getModifiers()) || Modifier.isStatic(((Field)accessibleObject).getModifiers()) || accessibleObject.isAnnotationPresent(HashCodeExclude.class)) continue;
                hashCodeBuilder.append(Reflection.getUnchecked((Field)accessibleObject, object));
            }
        } finally {
            HashCodeBuilder.unregister(object);
        }
    }

    public static int reflectionHashCode(int n, int n2, Object object) {
        return HashCodeBuilder.reflectionHashCode(n, n2, object, false, null, new String[0]);
    }

    public static int reflectionHashCode(int n, int n2, Object object, boolean bl) {
        return HashCodeBuilder.reflectionHashCode(n, n2, object, bl, null, new String[0]);
    }

    public static <T> int reflectionHashCode(int n, int n2, T t, boolean bl, Class<? super T> clazz, String ... stringArray) {
        Class<?> clazz2;
        Objects.requireNonNull(t, "object");
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(n, n2);
        HashCodeBuilder.reflectionAppend(t, clazz2, hashCodeBuilder, bl, stringArray);
        for (clazz2 = t.getClass(); clazz2.getSuperclass() != null && clazz2 != clazz; clazz2 = clazz2.getSuperclass()) {
            HashCodeBuilder.reflectionAppend(t, clazz2, hashCodeBuilder, bl, stringArray);
        }
        return hashCodeBuilder.toHashCode();
    }

    public static int reflectionHashCode(Object object, boolean bl) {
        return HashCodeBuilder.reflectionHashCode(17, 37, object, bl, null, new String[0]);
    }

    public static int reflectionHashCode(Object object, Collection<String> collection) {
        return HashCodeBuilder.reflectionHashCode(object, ReflectionToStringBuilder.toNoNullStringArray(collection));
    }

    public static int reflectionHashCode(Object object, String ... stringArray) {
        return HashCodeBuilder.reflectionHashCode(17, 37, object, false, null, stringArray);
    }

    private static void register(Object object) {
        HashCodeBuilder.getRegistry().add(new IDKey(object));
    }

    private static void unregister(Object object) {
        Set<IDKey> set = HashCodeBuilder.getRegistry();
        set.remove(new IDKey(object));
        if (set.isEmpty()) {
            REGISTRY.remove();
        }
    }

    public HashCodeBuilder() {
        this.iConstant = 37;
        this.iTotal = 17;
    }

    public HashCodeBuilder(int n, int n2) {
        Validate.isTrue(n % 2 != 0, "HashCodeBuilder requires an odd initial value", new Object[0]);
        Validate.isTrue(n2 % 2 != 0, "HashCodeBuilder requires an odd multiplier", new Object[0]);
        this.iConstant = n2;
        this.iTotal = n;
    }

    public HashCodeBuilder append(boolean bl) {
        this.iTotal = this.iTotal * this.iConstant + (bl ? 0 : 1);
        return this;
    }

    public HashCodeBuilder append(boolean[] blArray) {
        if (blArray == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (boolean bl : blArray) {
                this.append(bl);
            }
        }
        return this;
    }

    public HashCodeBuilder append(byte by) {
        this.iTotal = this.iTotal * this.iConstant + by;
        return this;
    }

    public HashCodeBuilder append(byte[] byArray) {
        if (byArray == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (byte by : byArray) {
                this.append(by);
            }
        }
        return this;
    }

    public HashCodeBuilder append(char c2) {
        this.iTotal = this.iTotal * this.iConstant + c2;
        return this;
    }

    public HashCodeBuilder append(char[] cArray) {
        if (cArray == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (char c2 : cArray) {
                this.append(c2);
            }
        }
        return this;
    }

    public HashCodeBuilder append(double d) {
        return this.append(Double.doubleToLongBits(d));
    }

    public HashCodeBuilder append(double[] dArray) {
        if (dArray == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (double d : dArray) {
                this.append(d);
            }
        }
        return this;
    }

    public HashCodeBuilder append(float f) {
        this.iTotal = this.iTotal * this.iConstant + Float.floatToIntBits(f);
        return this;
    }

    public HashCodeBuilder append(float[] fArray) {
        if (fArray == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (float f : fArray) {
                this.append(f);
            }
        }
        return this;
    }

    public HashCodeBuilder append(int n) {
        this.iTotal = this.iTotal * this.iConstant + n;
        return this;
    }

    public HashCodeBuilder append(int[] nArray) {
        if (nArray == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (int n : nArray) {
                this.append(n);
            }
        }
        return this;
    }

    public HashCodeBuilder append(long l) {
        this.iTotal = this.iTotal * this.iConstant + (int)(l ^ l >> 32);
        return this;
    }

    public HashCodeBuilder append(long[] lArray) {
        if (lArray == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (long l : lArray) {
                this.append(l);
            }
        }
        return this;
    }

    public HashCodeBuilder append(Object object) {
        if (object == null) {
            this.iTotal *= this.iConstant;
        } else if (ObjectUtils.isArray(object)) {
            this.appendArray(object);
        } else {
            this.iTotal = this.iTotal * this.iConstant + object.hashCode();
        }
        return this;
    }

    public HashCodeBuilder append(Object[] objectArray) {
        if (objectArray == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (Object object : objectArray) {
                this.append(object);
            }
        }
        return this;
    }

    public HashCodeBuilder append(short s) {
        this.iTotal = this.iTotal * this.iConstant + s;
        return this;
    }

    public HashCodeBuilder append(short[] sArray) {
        if (sArray == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (short s : sArray) {
                this.append(s);
            }
        }
        return this;
    }

    private void appendArray(Object object) {
        if (object instanceof long[]) {
            this.append((long[])object);
        } else if (object instanceof int[]) {
            this.append((int[])object);
        } else if (object instanceof short[]) {
            this.append((short[])object);
        } else if (object instanceof char[]) {
            this.append((char[])object);
        } else if (object instanceof byte[]) {
            this.append((byte[])object);
        } else if (object instanceof double[]) {
            this.append((double[])object);
        } else if (object instanceof float[]) {
            this.append((float[])object);
        } else if (object instanceof boolean[]) {
            this.append((boolean[])object);
        } else {
            this.append((Object[])object);
        }
    }

    public HashCodeBuilder appendSuper(int n) {
        this.iTotal = this.iTotal * this.iConstant + n;
        return this;
    }

    @Override
    public Integer build() {
        return this.toHashCode();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof HashCodeBuilder)) {
            return false;
        }
        HashCodeBuilder hashCodeBuilder = (HashCodeBuilder)object;
        return this.iTotal == hashCodeBuilder.iTotal;
    }

    public int hashCode() {
        return this.toHashCode();
    }

    public int toHashCode() {
        return this.iTotal;
    }
}

