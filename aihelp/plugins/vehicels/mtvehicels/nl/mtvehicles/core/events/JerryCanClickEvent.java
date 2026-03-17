/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasJerryCan;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;

public class JerryCanClickEvent
extends MTVEvent
implements IsCancellable,
HasJerryCan {
    private final int jerryCanFuel;
    private final int jerryCanSize;

    public JerryCanClickEvent(int jerryCanFuel, int jerryCanSize) {
        this.jerryCanFuel = jerryCanFuel;
        this.jerryCanSize = jerryCanSize;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public int getJerryCanFuel() {
        return this.jerryCanFuel;
    }

    @Override
    public int getJerryCanSize() {
        return this.jerryCanSize;
    }
}

