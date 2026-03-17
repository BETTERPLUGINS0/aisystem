/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.item.items;

import me.zombie_striker.qav.gui.click.action.GuiClickAction;
import me.zombie_striker.qav.gui.item.GuiItem;
import org.jetbrains.annotations.NotNull;

public final class SimpleGuiItem<P, I>
implements GuiItem<P, I> {
    private final I item;
    private final GuiClickAction<P> clickAction;

    public SimpleGuiItem(@NotNull I i, @NotNull GuiClickAction<P> guiClickAction) {
        this.item = i;
        this.clickAction = guiClickAction;
    }

    @Override
    @NotNull
    public I render() {
        return this.item;
    }

    @Override
    @NotNull
    public GuiClickAction<P> getClickAction() {
        return this.clickAction;
    }
}

