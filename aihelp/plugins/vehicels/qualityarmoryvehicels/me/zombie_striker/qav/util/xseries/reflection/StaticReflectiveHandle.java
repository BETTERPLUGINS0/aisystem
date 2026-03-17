/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.util.xseries.reflection;

import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;
import org.jetbrains.annotations.NotNull;

public class StaticReflectiveHandle<T>
implements ReflectiveHandle<T> {
    private final T reflected;
    private final ReflectiveHandle<ReflectedObject> jvm;

    public StaticReflectiveHandle(T t, ReflectedObject reflectedObject) {
        this.reflected = t;
        this.jvm = new StaticReflectiveHandle<ReflectedObject>(reflectedObject);
    }

    private StaticReflectiveHandle(T t) {
        this.reflected = t;
        this.jvm = null;
    }

    @Override
    public ReflectiveHandle<T> copy() {
        return this;
    }

    @Override
    @NotNull
    public T reflect() {
        return this.reflected;
    }

    @Override
    @NotNull
    public ReflectiveHandle<ReflectedObject> jvm() {
        return this.jvm == null ? this : this.jvm;
    }
}

