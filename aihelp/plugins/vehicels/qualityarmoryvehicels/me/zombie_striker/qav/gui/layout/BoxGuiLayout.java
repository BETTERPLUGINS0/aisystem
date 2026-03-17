/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.layout;

import java.util.ArrayList;
import java.util.List;
import me.zombie_striker.qav.gui.layout.GuiLayout;
import me.zombie_striker.qav.gui.slot.Slot;
import org.jetbrains.annotations.NotNull;

public final class BoxGuiLayout
implements GuiLayout {
    private final List<Slot> slots = new ArrayList<Slot>();

    public BoxGuiLayout(@NotNull Slot slot, @NotNull Slot slot2) {
        for (int i = slot.row(); i <= slot2.row(); ++i) {
            for (int j = slot.column(); j <= slot2.column(); ++j) {
                this.slots.add(Slot.of(i, j));
            }
        }
    }

    @Override
    @NotNull
    public @NotNull List<@NotNull Slot> generatePositions() {
        return this.slots;
    }
}

