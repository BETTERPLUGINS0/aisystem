/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.events.inventory;

import nl.mtvehicles.core.events.interfaces.HasInventory;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;

public class InventoryClickEvent
extends MTVEvent
implements IsCancellable,
HasInventory {
    private int clickedSlot;
    private final InventoryTitle title;

    public InventoryClickEvent(InventoryTitle title) {
        this.title = title;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public int getClickedSlot() {
        return this.clickedSlot;
    }

    public void setClickedSlot(int clickedSlot) {
        this.clickedSlot = clickedSlot;
    }

    @Override
    public InventoryTitle getTitle() {
        return this.title;
    }
}

