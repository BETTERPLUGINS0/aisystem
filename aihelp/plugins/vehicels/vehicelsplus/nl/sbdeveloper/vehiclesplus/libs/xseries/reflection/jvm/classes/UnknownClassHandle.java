/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes;

import java.util.Collections;
import java.util.Set;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.ReflectiveNamespace;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes.ClassHandle;
import org.jetbrains.annotations.NotNull;

public class UnknownClassHandle
extends ClassHandle {
    private final String name;

    public UnknownClassHandle(ReflectiveNamespace reflectiveNamespace, String string) {
        super(reflectiveNamespace);
        this.name = string;
    }

    @Override
    public Set<String> getPossibleNames() {
        return Collections.singleton(this.name);
    }

    @Override
    public UnknownClassHandle asArray(int n) {
        return new UnknownClassHandle(this.namespace, this.name + "[]");
    }

    @Override
    public boolean isArray() {
        return this.name.endsWith("[]");
    }

    @Override
    public UnknownClassHandle clone() {
        return new UnknownClassHandle(this.namespace, this.name);
    }

    @Override
    @NotNull
    public Class<?> reflect() {
        throw new ReflectiveOperationException("Unknown class: " + this.name);
    }

    public String toString() {
        return "UnknownClassHandle(" + this.name + ')';
    }
}

