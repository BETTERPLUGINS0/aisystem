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

public class CharMemberValue
extends MemberValue {
    int valueIndex;

    public CharMemberValue(int n, ConstPool constPool) {
        super('C', constPool);
        this.valueIndex = n;
    }

    public CharMemberValue(char c, ConstPool constPool) {
        super('C', constPool);
        this.setValue(c);
    }

    public CharMemberValue(ConstPool constPool) {
        super('C', constPool);
        this.setValue('\u0000');
    }

    @Override
    Object getValue(ClassLoader classLoader, ClassPool classPool, Method method) {
        return Character.valueOf(this.getValue());
    }

    @Override
    Class<?> getType(ClassLoader classLoader) {
        return Character.TYPE;
    }

    public char getValue() {
        return (char)this.cp.getIntegerInfo(this.valueIndex);
    }

    public void setValue(char c) {
        this.valueIndex = this.cp.addIntegerInfo(c);
    }

    public String toString() {
        return Character.toString(this.getValue());
    }

    @Override
    public void write(AnnotationsWriter annotationsWriter) {
        annotationsWriter.constValueIndex(this.getValue());
    }

    @Override
    public void accept(MemberValueVisitor memberValueVisitor) {
        memberValueVisitor.visitCharMemberValue(this);
    }
}

