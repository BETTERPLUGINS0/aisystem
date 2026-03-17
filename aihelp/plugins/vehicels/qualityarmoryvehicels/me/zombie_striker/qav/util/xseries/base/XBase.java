/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries.base;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface XBase<XForm extends XBase<XForm, BukkitForm>, BukkitForm> {
    @NotNull
    @Contract(pure=true)
    public String name();

    @ApiStatus.Internal
    @Contract(pure=true)
    public String[] getNames();

    @NotNull
    @Contract(pure=true)
    default public String friendlyName() {
        return Arrays.stream(this.name().split("_")).map(t -> t.charAt(0) + t.substring(1).toLowerCase(Locale.ENGLISH)).collect(Collectors.joining(" "));
    }

    @Nullable
    @Contract(pure=true)
    public BukkitForm get();

    @Contract(pure=true)
    default public boolean isSupported() {
        return this.get() != null;
    }

    @NotNull
    @Contract(pure=true)
    default public XForm or(XForm other) {
        return (XForm)(this.isSupported() ? this : other);
    }
}

