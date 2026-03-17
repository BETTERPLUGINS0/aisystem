/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation;

import java.lang.reflect.Method;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationsWriter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.MemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.MemberValueVisitor;

public class EnumMemberValue
extends MemberValue {
    int typeIndex;
    int valueIndex;

    public EnumMemberValue(int n, int n2, ConstPool constPool) {
        super('e', constPool);
        this.typeIndex = n;
        this.valueIndex = n2;
    }

    public EnumMemberValue(ConstPool constPool) {
        super('e', constPool);
        this.valueIndex = 0;
        this.typeIndex = 0;
    }

    @Override
    Object getValue(ClassLoader classLoader, ClassPool classPool, Method method) {
        try {
            return this.getType(classLoader).getField(this.getValue()).get(null);
        } catch (NoSuchFieldException noSuchFieldException) {
            throw new ClassNotFoundException(this.getType() + "." + this.getValue());
        } catch (IllegalAccessException illegalAccessException) {
            throw new ClassNotFoundException(this.getType() + "." + this.getValue());
        }
    }

    @Override
    Class<?> getType(ClassLoader classLoader) {
        return EnumMemberValue.loadClass(classLoader, this.getType());
    }

    @Override
    public void renameClass(String string, String string2) {
        String string3 = this.cp.getUtf8Info(this.typeIndex);
        String string4 = Descriptor.rename(string3, string, string2);
        this.setType(Descriptor.toClassName(string4));
    }

    @Override
    public void renameClass(Map<String, String> map) {
        String string = this.cp.getUtf8Info(this.typeIndex);
        String string2 = Descriptor.rename(string, map);
        this.setType(Descriptor.toClassName(string2));
    }

    public String getType() {
        return Descriptor.toClassName(this.cp.getUtf8Info(this.typeIndex));
    }

    public void setType(String string) {
        this.typeIndex = this.cp.addUtf8Info(Descriptor.of(string));
    }

    public String getValue() {
        return this.cp.getUtf8Info(this.valueIndex);
    }

    public void setValue(String string) {
        this.valueIndex = this.cp.addUtf8Info(string);
    }

    public String toString() {
        return this.getType() + "." + this.getValue();
    }

    @Override
    public void write(AnnotationsWriter annotationsWriter) {
        annotationsWriter.enumConstValue(this.cp.getUtf8Info(this.typeIndex), this.getValue());
    }

    @Override
    public void accept(MemberValueVisitor memberValueVisitor) {
        memberValueVisitor.visitEnumMemberValue(this);
    }
}

