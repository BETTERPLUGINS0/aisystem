/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.container;

import me.zombie_striker.qav.gui.container.type.GuiContainerType;
import me.zombie_striker.qav.gui.item.GuiItem;
import me.zombie_striker.qav.gui.slot.Slot;
import org.jetbrains.annotations.NotNull;

public interface GuiContainer<P, I> {
    @NotNull
    public GuiContainerType containerType();

    public void set(int var1, @NotNull @NotNull GuiItem<@NotNull P, @NotNull I> var2);

    public void set(int var1, int var2, @NotNull @NotNull GuiItem<@NotNull P, @NotNull I> var3);

    public void set(@NotNull Slot var1, @NotNull @NotNull GuiItem<@NotNull P, @NotNull I> var2);
}

