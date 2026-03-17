/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package me.zombie_striker.qav.util.xseries.reflection.proxy;

import java.util.Map;
import java.util.function.Supplier;
import me.zombie_striker.qav.util.xseries.reflection.proxy.OverloadedMethod;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ClassOverloadedMethods<T> {
    private final Map<String, OverloadedMethod<T>> mapped;

    public ClassOverloadedMethods(Map<String, OverloadedMethod<T>> map) {
        this.mapped = map;
    }

    public Map<String, OverloadedMethod<T>> mappings() {
        return this.mapped;
    }

    public T get(String string, Supplier<String> supplier) {
        return this.get(string, supplier, false);
    }

    public T get(String string, Supplier<String> supplier, boolean bl) {
        OverloadedMethod<T> overloadedMethod = this.mapped.get(string);
        if (overloadedMethod == null) {
            if (bl) {
                return null;
            }
            throw new IllegalArgumentException("Failed to find any method named '" + string + "' with descriptor '" + supplier.get() + "' " + this);
        }
        T t = overloadedMethod.get(supplier);
        if (t == null) {
            if (bl) {
                return null;
            }
            throw new IllegalArgumentException("Failed to find overloaded method named '" + string + " with descriptor: '" + supplier.get() + "' " + this);
        }
        return t;
    }

    public String toString() {
        return this.getClass().getSimpleName() + '(' + this.mapped + ')';
    }
}

