/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldguard.protection.flags.Flag
 */
package me.zombie_striker.qav.hooks.worldguard.implementation.v7.flag;

import com.sk89q.worldguard.protection.flags.Flag;
import java.util.Optional;
import me.zombie_striker.qav.hooks.worldguard.flag.IWrappedFlag;

public abstract class AbstractWrappedFlag<T>
implements IWrappedFlag<T> {
    private final Flag<?> handle;

    @Override
    public String getName() {
        return this.handle.getName();
    }

    public abstract Optional<T> fromWGValue(Object var1);

    public abstract Optional<Object> fromWrapperValue(T var1);

    @Override
    public Optional<T> getDefaultValue() {
        return this.fromWGValue(this.handle.getDefault());
    }

    public AbstractWrappedFlag(Flag<?> flag) {
        this.handle = flag;
    }

    public Flag<?> getHandle() {
        return this.handle;
    }
}

