/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.base;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface XBase<XForm extends XBase<XForm, BukkitForm>, BukkitForm> {
    @NotNull
    public String name();

    @ApiStatus.Internal
    public String[] getNames();

    default public String friendlyName() {
        return Arrays.stream(this.name().split("_")).map(t -> t.charAt(0) + t.substring(1).toLowerCase(Locale.ENGLISH)).collect(Collectors.joining(" "));
    }

    @Nullable
    public BukkitForm get();

    default public boolean isSupported() {
        return this.get() != null;
    }

    @NotNull
    default public XForm or(XForm other) {
        return (XForm)(this.isSupported() ? this : other);
    }
}

