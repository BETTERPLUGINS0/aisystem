/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.component.renderer;

import me.zombie_striker.qav.gui.AbstractGuiView;
import me.zombie_striker.qav.gui.component.GuiComponent;
import org.jetbrains.annotations.NotNull;

public interface GuiComponentRenderer<P, I> {
    public void renderComponent(@NotNull P var1, @NotNull GuiComponent<P, I> var2, @NotNull AbstractGuiView<P, I> var3);
}

