/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Experimental
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.Unmodifiable
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm.objects;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import me.zombie_striker.qav.util.xseries.reflection.XAccessFlag;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObjectClass;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObjectConstructor;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObjectField;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObjectMethod;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

@ApiStatus.Experimental
public interface ReflectedObject
extends AnnotatedElement {
    @NotNull
    @Contract(value="_ -> new", pure=true)
    public static ReflectedObject of(@NotNull Class<?> clazz) {
        return new ReflectedObjectClass(Objects.requireNonNull(clazz));
    }

    @NotNull
    @Contract(value="_ -> new", pure=true)
    public static ReflectedObject of(@NotNull Constructor<?> constructor) {
        return new ReflectedObjectConstructor(Objects.requireNonNull(constructor));
    }

    @NotNull
    @Contract(value="_ -> new", pure=true)
    public static ReflectedObject of(@NotNull Method method) {
        return new ReflectedObjectMethod(Objects.requireNonNull(method));
    }

    @NotNull
    @Contract(value="_ -> new", pure=true)
    public static ReflectedObject of(@NotNull Field field) {
        return new ReflectedObjectField(Objects.requireNonNull(field));
    }

    @NotNull
    @Contract(pure=true)
    public Object unreflect();

    @NotNull
    @Contract(pure=true)
    public Type type();

    @NotNull
    @Contract(pure=true)
    public String name();

    @Nullable
    @Contract(pure=true)
    public Class<?> getDeclaringClass();

    @Contract(pure=true)
    public int getModifiers();

    @NotNull
    @Contract(value="-> new", pure=true)
    default public @Unmodifiable Set<XAccessFlag> accessFlags() {
        return Collections.unmodifiableSet(XAccessFlag.of(this.getModifiers()));
    }

    public static enum Type {
        CLASS,
        CONSTRUCTOR,
        METHOD,
        FIELD;

    }
}

