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

public class DoubleMemberValue
extends MemberValue {
    int valueIndex;

    public DoubleMemberValue(int n, ConstPool constPool) {
        super('D', constPool);
        this.valueIndex = n;
    }

    public DoubleMemberValue(double d, ConstPool constPool) {
        super('D', constPool);
        this.setValue(d);
    }

    public DoubleMemberValue(ConstPool constPool) {
        super('D', constPool);
        this.setValue(0.0);
    }

    @Override
    Object getValue(ClassLoader classLoader, ClassPool classPool, Method method) {
        return this.getValue();
    }

    @Override
    Class<?> getType(ClassLoader classLoader) {
        return Double.TYPE;
    }

    public double getValue() {
        return this.cp.getDoubleInfo(this.valueIndex);
    }

    public void setValue(double d) {
        this.valueIndex = this.cp.addDoubleInfo(d);
    }

    public String toString() {
        return Double.toString(this.getValue());
    }

    @Override
    public void write(AnnotationsWriter annotationsWriter) {
        annotationsWriter.constValueIndex(this.getValue());
    }

    @Override
    public void accept(MemberValueVisitor memberValueVisitor) {
        memberValueVisitor.visitDoubleMemberValue(this);
    }
}

