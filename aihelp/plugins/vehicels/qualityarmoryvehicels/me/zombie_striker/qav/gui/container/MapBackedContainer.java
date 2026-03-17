/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.container;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import me.zombie_striker.qav.gui.click.handler.ClickHandler;
import me.zombie_striker.qav.gui.container.GuiContainer;
import me.zombie_striker.qav.gui.container.type.GuiContainerType;
import me.zombie_striker.qav.gui.item.GuiItem;
import me.zombie_striker.qav.gui.item.RenderedGuiItem;
import me.zombie_striker.qav.gui.slot.Slot;
import org.jetbrains.annotations.NotNull;

public final class MapBackedContainer<P, I>
implements GuiContainer<P, I> {
    private final Map<Integer, RenderedGuiItem<P, I>> backing = new HashMap<Integer, RenderedGuiItem<P, I>>(100);
    private final ClickHandler<P> clickHandler;
    private final GuiContainerType containerType;

    public MapBackedContainer(@NotNull ClickHandler<P> clickHandler, @NotNull GuiContainerType guiContainerType) {
        this.clickHandler = clickHandler;
        this.containerType = guiContainerType;
    }

    @Override
    @NotNull
    public GuiContainerType containerType() {
        return null;
    }

    @Override
    public void set(int n, int n2, @NotNull @NotNull GuiItem<@NotNull P, @NotNull I> guiItem) {
        this.set(this.containerType.mapSlot(Slot.of(n, n2)), guiItem);
    }

    @Override
    public void set(@NotNull Slot slot, @NotNull @NotNull GuiItem<@NotNull P, @NotNull I> guiItem) {
        this.set(this.containerType.mapSlot(slot), guiItem);
    }

    @Override
    public void set(int n, @NotNull @NotNull GuiItem<@NotNull P, @NotNull I> guiItem) {
        RenderedGuiItem<P, I> renderedGuiItem = new RenderedGuiItem<P, I>(guiItem.render(), this.clickHandler, guiItem.getClickAction());
        this.backing.put(n, renderedGuiItem);
    }

    @NotNull
    public Map<Integer, RenderedGuiItem<P, I>> complete() {
        return Collections.unmodifiableMap(this.backing);
    }
}

