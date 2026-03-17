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

public class ByteMemberValue
extends MemberValue {
    int valueIndex;

    public ByteMemberValue(int n, ConstPool constPool) {
        super('B', constPool);
        this.valueIndex = n;
    }

    public ByteMemberValue(byte by, ConstPool constPool) {
        super('B', constPool);
        this.setValue(by);
    }

    public ByteMemberValue(ConstPool constPool) {
        super('B', constPool);
        this.setValue((byte)0);
    }

    @Override
    Object getValue(ClassLoader classLoader, ClassPool classPool, Method method) {
        return this.getValue();
    }

    @Override
    Class<?> getType(ClassLoader classLoader) {
        return Byte.TYPE;
    }

    public byte getValue() {
        return (byte)this.cp.getIntegerInfo(this.valueIndex);
    }

    public void setValue(byte by) {
        this.valueIndex = this.cp.addIntegerInfo(by);
    }

    public String toString() {
        return Byte.toString(this.getValue());
    }

    @Override
    public void write(AnnotationsWriter annotationsWriter) {
        annotationsWriter.constValueIndex(this.getValue());
    }

    @Override
    public void accept(MemberValueVisitor memberValueVisitor) {
        memberValueVisitor.visitByteMemberValue(this);
    }
}

