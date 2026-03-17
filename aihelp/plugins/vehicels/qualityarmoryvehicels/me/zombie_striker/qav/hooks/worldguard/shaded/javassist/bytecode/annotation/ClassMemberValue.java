/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation;

import java.lang.reflect.Method;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.SignatureAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationsWriter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.MemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.MemberValueVisitor;

public class ClassMemberValue
extends MemberValue {
    int valueIndex;

    public ClassMemberValue(int n, ConstPool constPool) {
        super('c', constPool);
        this.valueIndex = n;
    }

    public ClassMemberValue(String string, ConstPool constPool) {
        super('c', constPool);
        this.setValue(string);
    }

    public ClassMemberValue(ConstPool constPool) {
        super('c', constPool);
        this.setValue("java.lang.Class");
    }

    @Override
    Object getValue(ClassLoader classLoader, ClassPool classPool, Method method) {
        String string = this.getValue();
        if (string.equals("void")) {
            return Void.TYPE;
        }
        if (string.equals("int")) {
            return Integer.TYPE;
        }
        if (string.equals("byte")) {
            return Byte.TYPE;
        }
        if (string.equals("long")) {
            return Long.TYPE;
        }
        if (string.equals("double")) {
            return Double.TYPE;
        }
        if (string.equals("float")) {
            return Float.TYPE;
        }
        if (string.equals("char")) {
            return Character.TYPE;
        }
        if (string.equals("short")) {
            return Short.TYPE;
        }
        if (string.equals("boolean")) {
            return Boolean.TYPE;
        }
        return ClassMemberValue.loadClass(classLoader, string);
    }

    @Override
    Class<?> getType(ClassLoader classLoader) {
        return ClassMemberValue.loadClass(classLoader, "java.lang.Class");
    }

    @Override
    public void renameClass(String string, String string2) {
        String string3 = this.cp.getUtf8Info(this.valueIndex);
        String string4 = Descriptor.rename(string3, string, string2);
        this.setValue(Descriptor.toClassName(string4));
    }

    @Override
    public void renameClass(Map<String, String> map) {
        String string = this.cp.getUtf8Info(this.valueIndex);
        String string2 = Descriptor.rename(string, map);
        this.setValue(Descriptor.toClassName(string2));
    }

    public String getValue() {
        String string = this.cp.getUtf8Info(this.valueIndex);
        try {
            return SignatureAttribute.toTypeSignature(string).jvmTypeName();
        } catch (BadBytecode badBytecode) {
            throw new RuntimeException(badBytecode);
        }
    }

    public void setValue(String string) {
        String string2 = Descriptor.of(string);
        this.valueIndex = this.cp.addUtf8Info(string2);
    }

    public String toString() {
        return this.getValue().replace('$', '.') + ".class";
    }

    @Override
    public void write(AnnotationsWriter annotationsWriter) {
        annotationsWriter.classInfoIndex(this.cp.getUtf8Info(this.valueIndex));
    }

    @Override
    public void accept(MemberValueVisitor memberValueVisitor) {
        memberValueVisitor.visitClassMemberValue(this);
    }
}

