/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  dev.triumphteam.nova.MutableState
 *  dev.triumphteam.nova.State
 *  dev.triumphteam.nova.builtin.SimpleMutableState
 *  dev.triumphteam.nova.policy.StateMutationPolicy$StructuralEquality
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.component.components;

import dev.triumphteam.nova.MutableState;
import dev.triumphteam.nova.State;
import dev.triumphteam.nova.builtin.SimpleMutableState;
import dev.triumphteam.nova.policy.StateMutationPolicy;
import java.util.List;
import me.zombie_striker.qav.gui.component.ReactiveGuiComponent;
import me.zombie_striker.qav.gui.container.GuiContainer;
import me.zombie_striker.qav.gui.item.GuiItem;
import me.zombie_striker.qav.gui.item.items.SimpleGuiItem;
import me.zombie_striker.qav.gui.layout.GuiLayout;
import me.zombie_striker.qav.gui.slot.Slot;
import org.jetbrains.annotations.NotNull;

public final class PaginatedComponent<P, I>
implements ReactiveGuiComponent<P, I> {
    private final Slot back;
    private final I backItem;
    private final Slot forward;
    private final I forwardItem;
    private final List<GuiItem<P, I>> items;
    private final GuiLayout layout;
    private final MutableState<Integer> pageState = new SimpleMutableState((Object)0, StateMutationPolicy.StructuralEquality.INSTANCE);

    public PaginatedComponent(@NotNull Slot slot, @NotNull I i, @NotNull Slot slot2, @NotNull I i2, @NotNull List<GuiItem<P, I>> list, @NotNull GuiLayout guiLayout) {
        this.back = slot;
        this.backItem = i;
        this.forward = slot2;
        this.forwardItem = i2;
        this.items = list;
        this.layout = guiLayout;
    }

    @Override
    @NotNull
    public @NotNull List<@NotNull State> states() {
        return List.of(this.pageState);
    }

    @Override
    public void render(@NotNull GuiContainer<P, I> guiContainer, @NotNull P p) {
        Integer n = (Integer)this.pageState.getValue();
        guiContainer.set(this.back, new SimpleGuiItem<Object, I>(this.backItem, (object, clickContext) -> this.pageState.setValue((Object)(n - 1))));
        guiContainer.set(this.forward, new SimpleGuiItem<Object, I>(this.forwardItem, (object, clickContext) -> this.pageState.setValue((Object)(n + 1))));
        List<Slot> list = this.layout.generatePositions();
        int n2 = list.size();
        int n3 = n * n2;
        for (int i = 0; i < n2; ++i) {
            int n4 = n3 + i;
            Slot slot = list.get(i);
            GuiItem<P, I> guiItem = this.items.get(n4);
            guiContainer.set(slot, guiItem);
        }
    }
}

