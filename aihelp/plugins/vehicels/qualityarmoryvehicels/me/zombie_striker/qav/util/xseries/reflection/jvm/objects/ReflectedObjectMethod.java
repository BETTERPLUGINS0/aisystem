/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm.objects;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.AbstractMemberReflectedObject;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;

final class ReflectedObjectMethod
extends AbstractMemberReflectedObject {
    private final Method delegate;

    ReflectedObjectMethod(Method method) {
        this.delegate = method;
    }

    @Override
    public ReflectedObject.Type type() {
        return ReflectedObject.Type.METHOD;
    }

    @Override
    public Method unreflect() {
        return this.delegate;
    }

    @Override
    protected Member member() {
        return this.delegate;
    }
}

