/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation;

import java.lang.reflect.Method;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationsWriter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.MemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.MemberValueVisitor;

public class BooleanMemberValue
extends MemberValue {
    int valueIndex;

    public BooleanMemberValue(int n, ConstPool constPool) {
        super('Z', constPool);
        this.valueIndex = n;
    }

    public BooleanMemberValue(boolean bl, ConstPool constPool) {
        super('Z', constPool);
        this.setValue(bl);
    }

    public BooleanMemberValue(ConstPool constPool) {
        super('Z', constPool);
        this.setValue(false);
    }

    @Override
    Object getValue(ClassLoader classLoader, ClassPool classPool, Method method) {
        return this.getValue();
    }

    @Override
    Class<?> getType(ClassLoader classLoader) {
        return Boolean.TYPE;
    }

    public boolean getValue() {
        return this.cp.getIntegerInfo(this.valueIndex) != 0;
    }

    public void setValue(boolean bl) {
        this.valueIndex = this.cp.addIntegerInfo(bl ? 1 : 0);
    }

    public String toString() {
        return this.getValue() ? "true" : "false";
    }

    @Override
    public void write(AnnotationsWriter annotationsWriter) {
        annotationsWriter.constValueIndex(this.getValue());
    }

    @Override
    public void accept(MemberValueVisitor memberValueVisitor) {
        memberValueVisitor.visitBooleanMemberValue(this);
    }
}

