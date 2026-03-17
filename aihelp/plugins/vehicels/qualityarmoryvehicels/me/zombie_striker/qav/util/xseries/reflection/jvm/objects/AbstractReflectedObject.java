/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm.objects;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;

abstract class AbstractReflectedObject
implements ReflectedObject {
    AbstractReflectedObject() {
    }

    @Override
    public abstract AnnotatedElement unreflect();

    public final <A extends Annotation> A getAnnotation(Class<A> clazz) {
        return this.unreflect().getAnnotation(clazz);
    }

    @Override
    public final boolean isAnnotationPresent(Class<? extends Annotation> clazz) {
        return this.unreflect().isAnnotationPresent(clazz);
    }

    public final <A extends Annotation> A[] getAnnotationsByType(Class<A> clazz) {
        return this.unreflect().getAnnotationsByType(clazz);
    }

    @Override
    public final Annotation[] getAnnotations() {
        return this.unreflect().getAnnotations();
    }

    public final <A extends Annotation> A getDeclaredAnnotation(Class<A> clazz) {
        return this.unreflect().getDeclaredAnnotation(clazz);
    }

    public final <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> clazz) {
        return this.unreflect().getDeclaredAnnotationsByType(clazz);
    }

    @Override
    public final Annotation[] getDeclaredAnnotations() {
        return this.unreflect().getDeclaredAnnotations();
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (object instanceof ReflectedObject) {
            return this.unreflect().equals(((ReflectedObject)object).unreflect());
        }
        return this.unreflect().equals(object);
    }

    public final int hashCode() {
        return this.unreflect().hashCode();
    }

    public final String toString() {
        return this.getClass().getSimpleName() + '(' + this.unreflect() + ')';
    }
}

