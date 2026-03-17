/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Experimental
 *  org.jetbrains.annotations.ApiStatus$NonExtendable
 *  org.jetbrains.annotations.ApiStatus$OverrideOnly
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.Range
 */
package me.zombie_striker.qav.util.xseries.reflection.proxy;

import java.lang.reflect.Array;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Ignore;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

@ApiStatus.Experimental
public interface ReflectiveProxyObject {
    @Ignore
    @NotNull
    @ApiStatus.NonExtendable
    @Contract(pure=true)
    public Object instance();

    @Ignore
    @NotNull
    @ApiStatus.NonExtendable
    @Contract(pure=true)
    public Class<?> getTargetClass();

    @Ignore
    @NotNull
    @ApiStatus.NonExtendable
    @Contract(pure=true)
    default public boolean isInstance(@Nullable Object object) {
        return this.getTargetClass().isInstance(object.getClass());
    }

    @Ignore
    @NotNull
    @ApiStatus.NonExtendable
    @Contract(pure=true)
    default public Object[] newArray(@Range(from=0L, to=0x7FFFFFFFL) int length) {
        return (Object[])Array.newInstance(this.getTargetClass(), length);
    }

    @Ignore
    @NotNull
    @ApiStatus.NonExtendable
    @Contract(pure=true)
    default public Object[] newArray(@Range(from=0L, to=0x7FFFFFFFL) int... dimensions) {
        return (Object[])Array.newInstance(this.getTargetClass(), dimensions);
    }

    @Ignore
    @NotNull
    @ApiStatus.OverrideOnly
    @Contract(value="_ -> new", pure=true)
    public ReflectiveProxyObject bindTo(@NotNull Object var1);
}

