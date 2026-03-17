/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationImpl;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationsWriter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.ArrayMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.BooleanMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.ByteMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.CharMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.ClassMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.DoubleMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.EnumMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.FloatMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.IntegerMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.LongMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.MemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.ShortMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.StringMemberValue;

public class Annotation {
    ConstPool pool;
    int typeIndex;
    Map<String, Pair> members;

    public Annotation(int n, ConstPool constPool) {
        this.pool = constPool;
        this.typeIndex = n;
        this.members = null;
    }

    public Annotation(String string, ConstPool constPool) {
        this(constPool.addUtf8Info(Descriptor.of(string)), constPool);
    }

    public Annotation(ConstPool constPool, CtClass ctClass) {
        this(constPool.addUtf8Info(Descriptor.of(ctClass.getName())), constPool);
        if (!ctClass.isInterface()) {
            throw new RuntimeException("Only interfaces are allowed for Annotation creation.");
        }
        CtMethod[] ctMethodArray = ctClass.getDeclaredMethods();
        if (ctMethodArray.length > 0) {
            this.members = new LinkedHashMap<String, Pair>();
        }
        for (CtMethod ctMethod : ctMethodArray) {
            this.addMemberValue(ctMethod.getName(), Annotation.createMemberValue(constPool, ctMethod.getReturnType()));
        }
    }

    public static MemberValue createMemberValue(ConstPool constPool, CtClass ctClass) {
        if (ctClass == CtClass.booleanType) {
            return new BooleanMemberValue(constPool);
        }
        if (ctClass == CtClass.byteType) {
            return new ByteMemberValue(constPool);
        }
        if (ctClass == CtClass.charType) {
            return new CharMemberValue(constPool);
        }
        if (ctClass == CtClass.shortType) {
            return new ShortMemberValue(constPool);
        }
        if (ctClass == CtClass.intType) {
            return new IntegerMemberValue(constPool);
        }
        if (ctClass == CtClass.longType) {
            return new LongMemberValue(constPool);
        }
        if (ctClass == CtClass.floatType) {
            return new FloatMemberValue(constPool);
        }
        if (ctClass == CtClass.doubleType) {
            return new DoubleMemberValue(constPool);
        }
        if (ctClass.getName().equals("java.lang.Class")) {
            return new ClassMemberValue(constPool);
        }
        if (ctClass.getName().equals("java.lang.String")) {
            return new StringMemberValue(constPool);
        }
        if (ctClass.isArray()) {
            CtClass ctClass2 = ctClass.getComponentType();
            MemberValue memberValue = Annotation.createMemberValue(constPool, ctClass2);
            return new ArrayMemberValue(memberValue, constPool);
        }
        if (ctClass.isInterface()) {
            Annotation annotation = new Annotation(constPool, ctClass);
            return new AnnotationMemberValue(annotation, constPool);
        }
        EnumMemberValue enumMemberValue = new EnumMemberValue(constPool);
        enumMemberValue.setType(ctClass.getName());
        return enumMemberValue;
    }

    public void addMemberValue(int n, MemberValue memberValue) {
        Pair pair = new Pair();
        pair.name = n;
        pair.value = memberValue;
        this.addMemberValue(pair);
    }

    public void addMemberValue(String string, MemberValue memberValue) {
        Pair pair = new Pair();
        pair.name = this.pool.addUtf8Info(string);
        pair.value = memberValue;
        if (this.members == null) {
            this.members = new LinkedHashMap<String, Pair>();
        }
        this.members.put(string, pair);
    }

    private void addMemberValue(Pair pair) {
        String string = this.pool.getUtf8Info(pair.name);
        if (this.members == null) {
            this.members = new LinkedHashMap<String, Pair>();
        }
        this.members.put(string, pair);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('@');
        stringBuilder.append(this.getTypeName());
        if (this.members != null) {
            stringBuilder.append('(');
            for (String string : this.members.keySet()) {
                stringBuilder.append(string).append('=').append(this.getMemberValue(string)).append(", ");
            }
            stringBuilder.setLength(stringBuilder.length() - 2);
            stringBuilder.append(')');
        }
        return stringBuilder.toString();
    }

    public String getTypeName() {
        return Descriptor.toClassName(this.pool.getUtf8Info(this.typeIndex));
    }

    public Set<String> getMemberNames() {
        if (this.members == null) {
            return null;
        }
        return this.members.keySet();
    }

    public MemberValue getMemberValue(String string) {
        if (this.members == null || this.members.get(string) == null) {
            return null;
        }
        return this.members.get((Object)string).value;
    }

    public Object toAnnotationType(ClassLoader classLoader, ClassPool classPool) {
        Class<?> clazz = MemberValue.loadClass(classLoader, this.getTypeName());
        try {
            return AnnotationImpl.make(classLoader, clazz, classPool, this);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new ClassNotFoundException(clazz.getName(), illegalArgumentException);
        } catch (IllegalAccessError illegalAccessError) {
            throw new ClassNotFoundException(clazz.getName(), illegalAccessError);
        }
    }

    public void write(AnnotationsWriter annotationsWriter) {
        String string = this.pool.getUtf8Info(this.typeIndex);
        if (this.members == null) {
            annotationsWriter.annotation(string, 0);
            return;
        }
        annotationsWriter.annotation(string, this.members.size());
        for (Pair pair : this.members.values()) {
            annotationsWriter.memberValuePair(pair.name);
            pair.value.write(annotationsWriter);
        }
    }

    public int hashCode() {
        return this.getTypeName().hashCode() + (this.members == null ? 0 : this.members.hashCode());
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || !(object instanceof Annotation)) {
            return false;
        }
        Annotation annotation = (Annotation)object;
        if (!this.getTypeName().equals(annotation.getTypeName())) {
            return false;
        }
        Map<String, Pair> map = annotation.members;
        if (this.members == map) {
            return true;
        }
        if (this.members == null) {
            return map == null;
        }
        if (map == null) {
            return false;
        }
        return this.members.equals(map);
    }

    static class Pair {
        int name;
        MemberValue value;

        Pair() {
        }
    }
}

