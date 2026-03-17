/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.component;

import me.zombie_striker.qav.gui.component.GuiComponent;
import org.jetbrains.annotations.NotNull;

public interface GuiComponentProducer<P, I> {
    @NotNull
    public GuiComponent<P, I> asGuiComponent();
}

