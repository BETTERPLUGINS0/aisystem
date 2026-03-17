/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text;

import java.util.Objects;
import net.kyori.adventure.text.AbstractComponentBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.NBTComponentBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class AbstractNBTComponentBuilder<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>>
extends AbstractComponentBuilder<C, B>
implements NBTComponentBuilder<C, B> {
    @Nullable
    protected String nbtPath;
    protected boolean interpret = false;
    @Nullable
    protected Component separator;

    AbstractNBTComponentBuilder() {
    }

    AbstractNBTComponentBuilder(@NotNull C c) {
        super(c);
        this.nbtPath = c.nbtPath();
        this.interpret = c.interpret();
        this.separator = c.separator();
    }

    @Override
    @NotNull
    public B nbtPath(@NotNull String string) {
        this.nbtPath = Objects.requireNonNull(string, "nbtPath");
        return (B)this;
    }

    @Override
    @NotNull
    public B interpret(boolean bl) {
        this.interpret = bl;
        return (B)this;
    }

    @Override
    @NotNull
    public B separator(@Nullable ComponentLike componentLike) {
        this.separator = ComponentLike.unbox(componentLike);
        return (B)this;
    }
}

