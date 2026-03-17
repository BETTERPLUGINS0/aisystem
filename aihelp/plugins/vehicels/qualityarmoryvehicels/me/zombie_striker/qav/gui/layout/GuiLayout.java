/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.layout;

import java.util.List;
import me.zombie_striker.qav.gui.slot.Slot;
import org.jetbrains.annotations.NotNull;

public interface GuiLayout {
    @NotNull
    public @NotNull List<@NotNull Slot> generatePositions();
}

