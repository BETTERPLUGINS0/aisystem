/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm.objects;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.AbstractReflectedObject;

abstract class AbstractMemberReflectedObject
extends AbstractReflectedObject {
    AbstractMemberReflectedObject() {
    }

    @Override
    public abstract AnnotatedElement unreflect();

    protected abstract Member member();

    @Override
    public String name() {
        return this.member().getName();
    }

    @Override
    public final Class<?> getDeclaringClass() {
        return this.member().getDeclaringClass();
    }

    @Override
    public final int getModifiers() {
        return this.member().getModifiers();
    }
}

