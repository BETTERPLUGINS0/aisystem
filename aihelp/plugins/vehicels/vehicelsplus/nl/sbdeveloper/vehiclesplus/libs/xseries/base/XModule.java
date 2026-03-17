/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.base;

import java.util.Arrays;
import nl.sbdeveloper.vehiclesplus.libs.xseries.base.XBase;
import nl.sbdeveloper.vehiclesplus.libs.xseries.base.XRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class XModule<XForm extends XModule<XForm, BukkitForm>, BukkitForm>
implements XBase<XForm, BukkitForm> {
    private final BukkitForm bukkitForm;
    private final String[] names;

    protected XModule(BukkitForm BukkitForm, String[] stringArray) {
        this.bukkitForm = BukkitForm;
        this.names = stringArray;
    }

    @Override
    @NotNull
    public final String name() {
        return this.names[0];
    }

    @ApiStatus.Experimental
    protected void setEnumName(XRegistry<XForm, BukkitForm> xRegistry, String string) {
        if (this.names[0] != null) {
            throw new IllegalStateException("Enum name already set " + string + " -> " + Arrays.toString(this.names));
        }
        this.names[0] = string;
        BukkitForm BukkitForm = xRegistry.getBukkit(this.names);
        if (this.bukkitForm != BukkitForm) {
            xRegistry.std(this);
        }
    }

    @Override
    @ApiStatus.Internal
    public String[] getNames() {
        return this.names;
    }

    @Override
    @Nullable
    public final BukkitForm get() {
        return this.bukkitForm;
    }

    public final String toString() {
        return (this.isSupported() ? "" : "!") + this.getClass().getSimpleName() + '(' + this.name() + ')';
    }

    public final int hashCode() {
        return super.hashCode();
    }

    @Deprecated
    public final boolean equals(Object object) {
        return super.equals(object);
    }
}

