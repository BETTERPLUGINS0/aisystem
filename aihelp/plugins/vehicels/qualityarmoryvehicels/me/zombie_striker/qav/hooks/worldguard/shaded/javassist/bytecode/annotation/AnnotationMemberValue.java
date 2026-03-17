/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation;

import java.lang.reflect.Method;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.Annotation;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationImpl;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationsWriter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.MemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.MemberValueVisitor;

public class AnnotationMemberValue
extends MemberValue {
    Annotation value;

    public AnnotationMemberValue(ConstPool constPool) {
        this(null, constPool);
    }

    public AnnotationMemberValue(Annotation annotation, ConstPool constPool) {
        super('@', constPool);
        this.value = annotation;
    }

    @Override
    Object getValue(ClassLoader classLoader, ClassPool classPool, Method method) {
        return AnnotationImpl.make(classLoader, this.getType(classLoader), classPool, this.value);
    }

    @Override
    Class<?> getType(ClassLoader classLoader) {
        if (this.value == null) {
            throw new ClassNotFoundException("no type specified");
        }
        return AnnotationMemberValue.loadClass(classLoader, this.value.getTypeName());
    }

    public Annotation getValue() {
        return this.value;
    }

    public void setValue(Annotation annotation) {
        this.value = annotation;
    }

    public String toString() {
        return this.value.toString();
    }

    @Override
    public void write(AnnotationsWriter annotationsWriter) {
        annotationsWriter.annotationValue();
        this.value.write(annotationsWriter);
    }

    @Override
    public void accept(MemberValueVisitor memberValueVisitor) {
        memberValueVisitor.visitAnnotationMemberValue(this);
    }
}

