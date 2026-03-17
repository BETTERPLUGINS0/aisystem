/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.api.events.impl;

import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.events.CancellableEvent;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import org.bukkit.entity.Player;

public class GarageOpenEvent
extends CancellableEvent {
    private final Player player;
    private Garage garage;

    public GarageOpenEvent(Player player, Garage garage) {
        this.player = player;
        this.garage = garage;
    }

    @Generated
    public Player getPlayer() {
        return this.player;
    }

    @Generated
    public Garage getGarage() {
        return this.garage;
    }

    @Generated
    public void setGarage(Garage garage) {
        this.garage = garage;
    }
}

