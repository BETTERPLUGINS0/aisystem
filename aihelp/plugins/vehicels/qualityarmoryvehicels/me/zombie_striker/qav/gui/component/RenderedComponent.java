/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.component;

import java.util.Map;
import me.zombie_striker.qav.gui.component.GuiComponent;
import me.zombie_striker.qav.gui.item.RenderedGuiItem;
import org.jetbrains.annotations.NotNull;

public record RenderedComponent<P, I>(@NotNull GuiComponent<P, I> component, @NotNull Map<Integer, RenderedGuiItem<P, I>> renderedItems) {
    @NotNull
    public GuiComponent<P, I> component() {
        return this.component;
    }

    @NotNull
    public Map<Integer, RenderedGuiItem<P, I>> renderedItems() {
        return this.renderedItems;
    }
}

