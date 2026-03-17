/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.item;

import me.zombie_striker.qav.gui.click.action.GuiClickAction;
import me.zombie_striker.qav.gui.click.handler.ClickHandler;
import org.jetbrains.annotations.NotNull;

public record RenderedGuiItem<P, I>(@NotNull I item, @NotNull ClickHandler<P> clickHandler, @NotNull GuiClickAction<P> action) {
    @NotNull
    public I item() {
        return this.item;
    }

    @NotNull
    public ClickHandler<P> clickHandler() {
        return this.clickHandler;
    }

    @NotNull
    public GuiClickAction<P> action() {
        return this.action;
    }
}

