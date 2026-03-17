/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm.objects;

import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.AbstractReflectedObject;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;

final class ReflectedObjectClass
extends AbstractReflectedObject {
    private final Class<?> delegate;

    ReflectedObjectClass(Class<?> clazz) {
        this.delegate = clazz;
    }

    @Override
    public ReflectedObject.Type type() {
        return ReflectedObject.Type.CLASS;
    }

    @Override
    public Class<?> unreflect() {
        return this.delegate;
    }

    @Override
    public String name() {
        return this.delegate.getSimpleName();
    }

    @Override
    public Class<?> getDeclaringClass() {
        return this.delegate.getDeclaringClass();
    }

    @Override
    public int getModifiers() {
        return this.delegate.getModifiers();
    }
}

