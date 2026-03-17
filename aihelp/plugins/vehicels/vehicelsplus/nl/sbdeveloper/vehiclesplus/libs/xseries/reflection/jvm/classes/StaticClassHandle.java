/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.ReflectiveNamespace;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes.ClassHandle;
import org.jetbrains.annotations.NotNull;

public class StaticClassHandle
extends ClassHandle {
    @NotNull
    protected Class<?> clazz;

    public StaticClassHandle(ReflectiveNamespace reflectiveNamespace, @NotNull Class<?> clazz) {
        super(reflectiveNamespace);
        this.clazz = Objects.requireNonNull(clazz);
    }

    private Class<?> purifyClass() {
        Class<?> clazz;
        Class<?> clazz2 = this.clazz;
        while ((clazz = clazz2.getComponentType()) != null) {
            clazz2 = clazz;
        }
        return Objects.requireNonNull(clazz2);
    }

    @Override
    public StaticClassHandle asArray(int n) {
        Class<?> clazz = this.purifyClass();
        if (n > 0) {
            for (int i = 0; i < n; ++i) {
                clazz = Array.newInstance(clazz, 0).getClass();
            }
        }
        this.clazz = clazz;
        return this;
    }

    @Override
    public Class<?> reflect() {
        return this.clazz;
    }

    @Override
    public boolean isArray() {
        return this.clazz.isArray();
    }

    @Override
    public Set<String> getPossibleNames() {
        return Collections.singleton(this.clazz.getSimpleName());
    }

    @Override
    public StaticClassHandle clone() {
        return new StaticClassHandle(this.namespace, this.clazz);
    }

    public String toString() {
        return "StaticClassHandle(" + this.clazz + ')';
    }
}

