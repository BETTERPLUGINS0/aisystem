/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.click.action;

import me.zombie_striker.qav.gui.click.ClickContext;
import me.zombie_striker.qav.gui.click.action.GuiClickAction;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface RunnableGuiClickAction<P>
extends GuiClickAction<P> {
    public void run(@NotNull P var1, @NotNull ClickContext var2);
}

