/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.click.handler;

import me.zombie_striker.qav.gui.click.ClickContext;
import me.zombie_striker.qav.gui.click.action.GuiClickAction;
import me.zombie_striker.qav.gui.click.controller.ClickController;
import org.jetbrains.annotations.NotNull;

public interface ClickHandler<P> {
    public void handle(@NotNull P var1, @NotNull ClickContext var2, @NotNull GuiClickAction<P> var3, @NotNull ClickController var4);
}

