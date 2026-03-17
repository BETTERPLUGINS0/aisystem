/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Experimental
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Unmodifiable
 */
package me.zombie_striker.qav.util.xseries.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

@ApiStatus.Experimental
public enum XAccessFlag {
    PUBLIC(1, true, JVMLocation.CLASS, JVMLocation.FIELD, JVMLocation.METHOD, JVMLocation.INNER_CLASS),
    PRIVATE(2, true, JVMLocation.FIELD, JVMLocation.METHOD, JVMLocation.INNER_CLASS),
    PROTECTED(4, true, JVMLocation.FIELD, JVMLocation.METHOD, JVMLocation.INNER_CLASS),
    STATIC(8, true, JVMLocation.FIELD, JVMLocation.METHOD, JVMLocation.INNER_CLASS),
    FINAL(16, true, JVMLocation.CLASS, JVMLocation.FIELD, JVMLocation.METHOD, JVMLocation.INNER_CLASS, JVMLocation.METHOD_PARAMETER),
    SUPER(32, false, JVMLocation.CLASS),
    OPEN(32, false, JVMLocation.MODULE),
    TRANSITIVE(32, false, JVMLocation.MODULE_REQUIRES),
    SYNCHRONIZED(32, true, JVMLocation.METHOD),
    STATIC_PHASE(64, false, JVMLocation.MODULE_REQUIRES),
    VOLATILE(64, true, JVMLocation.FIELD),
    BRIDGE(XAccessFlag.getPrivateMod("BRIDGE"), false, JVMLocation.METHOD),
    TRANSIENT(128, true, JVMLocation.FIELD),
    VARARGS(XAccessFlag.getPrivateMod("VARARGS"), false, JVMLocation.METHOD),
    NATIVE(256, true, JVMLocation.METHOD),
    INTERFACE(512, false, JVMLocation.CLASS, JVMLocation.INNER_CLASS),
    ABSTRACT(1024, true, JVMLocation.CLASS, JVMLocation.METHOD, JVMLocation.INNER_CLASS),
    STRICT(2048, true, new JVMLocation[0]),
    SYNTHETIC(XAccessFlag.getPrivateMod("SYNTHETIC"), false, JVMLocation.CLASS, JVMLocation.FIELD, JVMLocation.METHOD, JVMLocation.INNER_CLASS, JVMLocation.METHOD_PARAMETER, JVMLocation.MODULE, JVMLocation.MODULE_REQUIRES, JVMLocation.MODULE_EXPORTS, JVMLocation.MODULE_OPENS),
    ANNOTATION(XAccessFlag.getPrivateMod("ANNOTATION"), false, JVMLocation.CLASS, JVMLocation.INNER_CLASS),
    ENUM(XAccessFlag.getPrivateMod("ENUM"), false, JVMLocation.CLASS, JVMLocation.FIELD, JVMLocation.INNER_CLASS),
    MANDATED(XAccessFlag.getPrivateMod("MANDATED"), false, JVMLocation.METHOD_PARAMETER, JVMLocation.MODULE, JVMLocation.MODULE_REQUIRES, JVMLocation.MODULE_EXPORTS, JVMLocation.MODULE_OPENS),
    MODULE(32768, false, JVMLocation.CLASS);

    private static final XAccessFlag[] VALUES;
    private final int mask;
    private final boolean sourceModifier;
    private final Set<JVMLocation> locations;

    private XAccessFlag(int n2, boolean bl, JVMLocation ... jVMLocationArray) {
        this.mask = n2;
        this.sourceModifier = bl;
        this.locations = Collections.unmodifiableSet(jVMLocationArray.length == 0 ? EnumSet.noneOf(JVMLocation.class) : EnumSet.copyOf(Arrays.asList(jVMLocationArray)));
    }

    private static int getPrivateMod(String string) {
        try {
            Class<?> clazz = Class.forName("java.lang.reflect.AccessFlag");
            Field field = clazz.getDeclaredField(string);
            Object object = field.get(null);
            Method method = clazz.getDeclaredMethod("mask", new Class[0]);
            return (Integer)method.invoke(object, new Object[0]);
        } catch (Throwable throwable) {
            try {
                return (Integer)Modifier.class.getDeclaredField(string).get(null);
            } catch (IllegalAccessException | NoSuchFieldException reflectiveOperationException) {
                return 0;
            }
        }
    }

    @Contract(pure=true)
    public int mask() {
        return this.mask;
    }

    @Contract(pure=true)
    public boolean sourceModifier() {
        return this.sourceModifier;
    }

    @NotNull
    @Contract(pure=true)
    public @Unmodifiable Set<JVMLocation> locations() {
        return this.locations;
    }

    @Contract(pure=true)
    public static Set<XAccessFlag> of(int n) {
        EnumSet<XAccessFlag> enumSet = EnumSet.noneOf(XAccessFlag.class);
        for (XAccessFlag xAccessFlag : VALUES) {
            if (!xAccessFlag.isSet(n)) continue;
            enumSet.add(xAccessFlag);
        }
        return enumSet;
    }

    @Contract(pure=true)
    public int add(int n) {
        return n | this.mask;
    }

    @Contract(pure=true)
    public int remove(int n) {
        return n & ~this.mask;
    }

    @Contract(pure=true)
    public boolean isSet(int n) {
        return (n & this.mask) != 0;
    }

    @Contract(pure=true)
    public static boolean isSet(int n, XAccessFlag ... xAccessFlagArray) {
        for (XAccessFlag xAccessFlag : xAccessFlagArray) {
            if (xAccessFlag.isSet(n)) continue;
            return false;
        }
        return true;
    }

    @Contract(pure=true)
    public static Optional<Integer> getModifiers(Object object) {
        if (object instanceof Class) {
            return Optional.of(((Class)object).getModifiers());
        }
        if (object instanceof Member) {
            return Optional.of(((Member)object).getModifiers());
        }
        return Optional.empty();
    }

    @Contract(pure=true)
    public static String toString(int n) {
        StringJoiner stringJoiner = new StringJoiner(" ", "Flags::" + n + '(', ")");
        for (XAccessFlag xAccessFlag : VALUES) {
            if (!xAccessFlag.isSet(n)) continue;
            stringJoiner.add(xAccessFlag.name().toLowerCase(Locale.ENGLISH));
        }
        return stringJoiner.toString();
    }

    static {
        VALUES = XAccessFlag.values();
    }

    public static enum JVMLocation {
        CLASS,
        FIELD,
        METHOD,
        INNER_CLASS,
        METHOD_PARAMETER,
        MODULE,
        MODULE_REQUIRES,
        MODULE_EXPORTS,
        MODULE_OPENS;

    }
}

