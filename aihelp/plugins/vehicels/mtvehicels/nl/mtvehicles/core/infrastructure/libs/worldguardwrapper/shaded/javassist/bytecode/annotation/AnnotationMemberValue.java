/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.ClassPool;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.ConstPool;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.annotation.Annotation;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.annotation.AnnotationImpl;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.annotation.AnnotationsWriter;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.annotation.MemberValue;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.annotation.MemberValueVisitor;

public class AnnotationMemberValue
extends MemberValue {
    Annotation value;

    public AnnotationMemberValue(ConstPool cp) {
        this(null, cp);
    }

    public AnnotationMemberValue(Annotation a, ConstPool cp) {
        super('@', cp);
        this.value = a;
    }

    @Override
    Object getValue(ClassLoader cl, ClassPool cp, Method m) throws ClassNotFoundException {
        return AnnotationImpl.make(cl, this.getType(cl), cp, this.value);
    }

    @Override
    Class<?> getType(ClassLoader cl) throws ClassNotFoundException {
        if (this.value == null) {
            throw new ClassNotFoundException("no type specified");
        }
        return AnnotationMemberValue.loadClass(cl, this.value.getTypeName());
    }

    public Annotation getValue() {
        return this.value;
    }

    public void setValue(Annotation newValue) {
        this.value = newValue;
    }

    public String toString() {
        return this.value.toString();
    }

    @Override
    public void write(AnnotationsWriter writer) throws IOException {
        writer.annotationValue();
        this.value.write(writer);
    }

    @Override
    public void accept(MemberValueVisitor visitor) {
        visitor.visitAnnotationMemberValue(this);
    }
}

