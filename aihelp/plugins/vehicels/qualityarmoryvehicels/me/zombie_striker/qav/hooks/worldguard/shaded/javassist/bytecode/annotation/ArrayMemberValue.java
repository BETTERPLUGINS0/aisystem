/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationsWriter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.MemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.MemberValueVisitor;

public class ArrayMemberValue
extends MemberValue {
    MemberValue type;
    MemberValue[] values;

    public ArrayMemberValue(ConstPool constPool) {
        super('[', constPool);
        this.type = null;
        this.values = null;
    }

    public ArrayMemberValue(MemberValue memberValue, ConstPool constPool) {
        super('[', constPool);
        this.type = memberValue;
        this.values = null;
    }

    @Override
    Object getValue(ClassLoader classLoader, ClassPool classPool, Method method) {
        Class<?> clazz;
        if (this.values == null) {
            throw new ClassNotFoundException("no array elements found: " + method.getName());
        }
        int n = this.values.length;
        if (this.type == null) {
            clazz = method.getReturnType().getComponentType();
            if (clazz == null || n > 0) {
                throw new ClassNotFoundException("broken array type: " + method.getName());
            }
        } else {
            clazz = this.type.getType(classLoader);
        }
        Object object = Array.newInstance(clazz, n);
        for (int i = 0; i < n; ++i) {
            Array.set(object, i, this.values[i].getValue(classLoader, classPool, method));
        }
        return object;
    }

    @Override
    Class<?> getType(ClassLoader classLoader) {
        if (this.type == null) {
            throw new ClassNotFoundException("no array type specified");
        }
        Object object = Array.newInstance(this.type.getType(classLoader), 0);
        return object.getClass();
    }

    @Override
    public void renameClass(String string, String string2) {
        if (this.type != null) {
            this.type.renameClass(string, string2);
        }
        if (this.values != null) {
            for (MemberValue memberValue : this.values) {
                memberValue.renameClass(string, string2);
            }
        }
    }

    @Override
    public void renameClass(Map<String, String> map) {
        if (this.type != null) {
            this.type.renameClass(map);
        }
        if (this.values != null) {
            for (MemberValue memberValue : this.values) {
                memberValue.renameClass(map);
            }
        }
    }

    public MemberValue getType() {
        return this.type;
    }

    public MemberValue[] getValue() {
        return this.values;
    }

    public void setValue(MemberValue[] memberValueArray) {
        this.values = memberValueArray;
        if (memberValueArray != null && memberValueArray.length > 0) {
            this.type = memberValueArray[0];
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');
        if (this.values != null) {
            for (int i = 0; i < this.values.length; ++i) {
                stringBuilder.append(this.values[i].toString());
                if (i + 1 >= this.values.length) continue;
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @Override
    public void write(AnnotationsWriter annotationsWriter) {
        int n = this.values == null ? 0 : this.values.length;
        annotationsWriter.arrayValue(n);
        for (int i = 0; i < n; ++i) {
            this.values[i].write(annotationsWriter);
        }
    }

    @Override
    public void accept(MemberValueVisitor memberValueVisitor) {
        memberValueVisitor.visitArrayMemberValue(this);
    }
}

