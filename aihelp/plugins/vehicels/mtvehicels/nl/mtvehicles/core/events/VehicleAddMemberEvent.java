/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 */
package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.CanEditLicensePlate;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class VehicleAddMemberEvent
extends MTVEvent
implements IsCancellable,
Cancellable,
CanEditLicensePlate {
    private String licensePlate;
    private Player addedPlayer;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String getLicensePlate() {
        return this.licensePlate;
    }

    @Override
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Player getAdder() {
        return super.getPlayer();
    }

    public Player getAdded() {
        return this.addedPlayer;
    }

    public void setAdded(Player player) {
        this.addedPlayer = player;
    }
}

