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

public class StringMemberValue
extends MemberValue {
    int valueIndex;

    public StringMemberValue(int n, ConstPool constPool) {
        super('s', constPool);
        this.valueIndex = n;
    }

    public StringMemberValue(String string, ConstPool constPool) {
        super('s', constPool);
        this.setValue(string);
    }

    public StringMemberValue(ConstPool constPool) {
        super('s', constPool);
        this.setValue("");
    }

    @Override
    Object getValue(ClassLoader classLoader, ClassPool classPool, Method method) {
        return this.getValue();
    }

    @Override
    Class<?> getType(ClassLoader classLoader) {
        return String.class;
    }

    public String getValue() {
        return this.cp.getUtf8Info(this.valueIndex);
    }

    public void setValue(String string) {
        this.valueIndex = this.cp.addUtf8Info(string);
    }

    public String toString() {
        return "\"" + this.getValue() + "\"";
    }

    @Override
    public void write(AnnotationsWriter annotationsWriter) {
        annotationsWriter.constValueIndex(this.getValue());
    }

    @Override
    public void accept(MemberValueVisitor memberValueVisitor) {
        memberValueVisitor.visitStringMemberValue(this);
    }
}

