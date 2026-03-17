/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm.objects;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.AbstractMemberReflectedObject;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;

final class ReflectedObjectConstructor
extends AbstractMemberReflectedObject {
    private final Constructor<?> delegate;

    ReflectedObjectConstructor(Constructor<?> constructor) {
        this.delegate = constructor;
    }

    @Override
    public ReflectedObject.Type type() {
        return ReflectedObject.Type.CONSTRUCTOR;
    }

    @Override
    public String name() {
        return "<init>";
    }

    @Override
    public Constructor<?> unreflect() {
        return this.delegate;
    }

    @Override
    protected Member member() {
        return this.delegate;
    }
}

