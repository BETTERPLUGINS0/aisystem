/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.events.inventory;

import nl.mtvehicles.core.events.interfaces.HasInventory;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;

public class InventoryCloseEvent
extends MTVEvent
implements HasInventory {
    private final InventoryTitle title;

    public InventoryCloseEvent(InventoryTitle title) {
        this.title = title;
    }

    @Override
    public InventoryTitle getTitle() {
        return this.title;
    }
}

