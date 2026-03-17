/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm.objects;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.AbstractMemberReflectedObject;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;

final class ReflectedObjectField
extends AbstractMemberReflectedObject {
    private final Field delegate;

    ReflectedObjectField(Field field) {
        this.delegate = field;
    }

    @Override
    public ReflectedObject.Type type() {
        return ReflectedObject.Type.FIELD;
    }

    @Override
    public Field unreflect() {
        return this.delegate;
    }

    @Override
    protected Member member() {
        return this.delegate;
    }
}

