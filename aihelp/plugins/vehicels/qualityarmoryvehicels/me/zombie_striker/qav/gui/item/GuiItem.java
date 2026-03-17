/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.item;

import me.zombie_striker.qav.gui.click.action.GuiClickAction;
import org.jetbrains.annotations.NotNull;

public interface GuiItem<P, I> {
    @NotNull
    public I render();

    @NotNull
    public GuiClickAction<P> getClickAction();
}

