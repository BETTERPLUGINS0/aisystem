/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm.objects;

import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;
import org.jetbrains.annotations.NotNull;

public final class ReflectedObjectHandle
implements ReflectiveHandle<ReflectedObject> {
    private final ReflectiveOperation jvmGetter;

    public ReflectedObjectHandle(ReflectiveOperation reflectiveOperation) {
        this.jvmGetter = reflectiveOperation;
    }

    @Override
    public ReflectiveHandle<ReflectedObject> copy() {
        return new ReflectedObjectHandle(this.jvmGetter);
    }

    @Override
    @NotNull
    public ReflectedObject reflect() {
        return this.jvmGetter.get();
    }

    @Override
    @NotNull
    public ReflectiveHandle<ReflectedObject> jvm() {
        return this;
    }

    @FunctionalInterface
    public static interface ReflectiveOperation {
        public ReflectedObject get() throws ReflectiveOperationException;
    }
}

