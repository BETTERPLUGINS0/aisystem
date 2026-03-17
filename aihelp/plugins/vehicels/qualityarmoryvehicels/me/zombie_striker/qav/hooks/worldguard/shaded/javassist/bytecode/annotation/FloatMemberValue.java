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

public class FloatMemberValue
extends MemberValue {
    int valueIndex;

    public FloatMemberValue(int n, ConstPool constPool) {
        super('F', constPool);
        this.valueIndex = n;
    }

    public FloatMemberValue(float f, ConstPool constPool) {
        super('F', constPool);
        this.setValue(f);
    }

    public FloatMemberValue(ConstPool constPool) {
        super('F', constPool);
        this.setValue(0.0f);
    }

    @Override
    Object getValue(ClassLoader classLoader, ClassPool classPool, Method method) {
        return Float.valueOf(this.getValue());
    }

    @Override
    Class<?> getType(ClassLoader classLoader) {
        return Float.TYPE;
    }

    public float getValue() {
        return this.cp.getFloatInfo(this.valueIndex);
    }

    public void setValue(float f) {
        this.valueIndex = this.cp.addFloatInfo(f);
    }

    public String toString() {
        return Float.toString(this.getValue());
    }

    @Override
    public void write(AnnotationsWriter annotationsWriter) {
        annotationsWriter.constValueIndex(this.getValue());
    }

    @Override
    public void accept(MemberValueVisitor memberValueVisitor) {
        memberValueVisitor.visitFloatMemberValue(this);
    }
}

