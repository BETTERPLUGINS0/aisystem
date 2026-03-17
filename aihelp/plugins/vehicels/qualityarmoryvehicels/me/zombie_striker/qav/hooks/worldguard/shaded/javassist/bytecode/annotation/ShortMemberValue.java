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

public class ShortMemberValue
extends MemberValue {
    int valueIndex;

    public ShortMemberValue(int n, ConstPool constPool) {
        super('S', constPool);
        this.valueIndex = n;
    }

    public ShortMemberValue(short s, ConstPool constPool) {
        super('S', constPool);
        this.setValue(s);
    }

    public ShortMemberValue(ConstPool constPool) {
        super('S', constPool);
        this.setValue((short)0);
    }

    @Override
    Object getValue(ClassLoader classLoader, ClassPool classPool, Method method) {
        return this.getValue();
    }

    @Override
    Class<?> getType(ClassLoader classLoader) {
        return Short.TYPE;
    }

    public short getValue() {
        return (short)this.cp.getIntegerInfo(this.valueIndex);
    }

    public void setValue(short s) {
        this.valueIndex = this.cp.addIntegerInfo(s);
    }

    public String toString() {
        return Short.toString(this.getValue());
    }

    @Override
    public void write(AnnotationsWriter annotationsWriter) {
        annotationsWriter.constValueIndex(this.getValue());
    }

    @Override
    public void accept(MemberValueVisitor memberValueVisitor) {
        memberValueVisitor.visitShortMemberValue(this);
    }
}

