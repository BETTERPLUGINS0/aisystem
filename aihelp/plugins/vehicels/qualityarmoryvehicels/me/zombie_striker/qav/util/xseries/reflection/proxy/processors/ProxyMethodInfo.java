/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package me.zombie_striker.qav.util.xseries.reflection.proxy.processors;

import java.lang.reflect.Method;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.proxy.processors.MappedType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ProxyMethodInfo {
    public final ReflectiveHandle<?> handle;
    public final Method interfaceMethod;
    public final MappedType rType;
    public final MappedType[] pTypes;

    public ProxyMethodInfo(ReflectiveHandle<?> reflectiveHandle, Method method, MappedType mappedType, MappedType[] mappedTypeArray) {
        this.handle = reflectiveHandle;
        this.interfaceMethod = method;
        this.rType = mappedType;
        this.pTypes = mappedTypeArray;
    }

    public String toString() {
        return this.getClass().getSimpleName() + '(' + this.interfaceMethod + ')';
    }
}

