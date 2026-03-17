/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text;

import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.AbstractComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.NBTComponentBuilder;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class NBTComponentImpl<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>>
extends AbstractComponent
implements NBTComponent<C, B> {
    static final boolean INTERPRET_DEFAULT = false;
    final String nbtPath;
    final boolean interpret;
    @Nullable
    final Component separator;

    NBTComponentImpl(@NotNull List<Component> list, @NotNull Style style, String string, boolean bl, @Nullable Component component) {
        super(list, style);
        this.nbtPath = string;
        this.interpret = bl;
        this.separator = component;
    }

    @Override
    @NotNull
    public String nbtPath() {
        return this.nbtPath;
    }

    @Override
    public boolean interpret() {
        return this.interpret;
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof NBTComponent)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        NBTComponent nBTComponent = (NBTComponent)object;
        return Objects.equals(this.nbtPath, nBTComponent.nbtPath()) && this.interpret == nBTComponent.interpret() && Objects.equals(this.separator, nBTComponent.separator());
    }

    @Override
    public int hashCode() {
        int n = super.hashCode();
        n = 31 * n + this.nbtPath.hashCode();
        n = 31 * n + Boolean.hashCode(this.interpret);
        n = 31 * n + Objects.hashCode(this.separator);
        return n;
    }
}

