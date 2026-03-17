/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.api.events.impl;

import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.events.Event;
import nl.sbdeveloper.vehiclesplus.api.vehicles.movement.MovementInput;
import org.bukkit.entity.Player;

public class KeyPressEvent
extends Event {
    private final Player player;
    private final MovementInput input;

    public KeyPressEvent(Player player, MovementInput movementInput) {
        this.player = player;
        this.input = movementInput;
    }

    @Generated
    public Player getPlayer() {
        return this.player;
    }

    @Generated
    public MovementInput getInput() {
        return this.input;
    }
}

