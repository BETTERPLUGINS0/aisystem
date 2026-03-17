/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  dev.triumphteam.nova.State
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.component;

import dev.triumphteam.nova.State;
import java.util.List;
import me.zombie_striker.qav.gui.component.GuiComponent;
import org.jetbrains.annotations.NotNull;

public interface StatefulGuiComponent<P, I>
extends GuiComponent<P, I> {
    @NotNull
    public List<State> states();
}

