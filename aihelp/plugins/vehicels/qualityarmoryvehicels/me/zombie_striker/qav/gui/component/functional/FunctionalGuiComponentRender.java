/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.component.functional;

import me.zombie_striker.qav.gui.container.GuiContainer;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface FunctionalGuiComponentRender<P, I> {
    public void render(@NotNull @NotNull GuiContainer<@NotNull P, @NotNull I> var1, @NotNull P var2);
}

