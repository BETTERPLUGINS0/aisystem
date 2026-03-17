/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  ch.njol.skript.Skript
 *  ch.njol.skript.doc.Description
 *  ch.njol.skript.doc.Examples
 *  ch.njol.skript.doc.Name
 *  ch.njol.skript.lang.Literal
 *  ch.njol.skript.lang.SkriptEvent
 *  ch.njol.skript.lang.SkriptParser$ParseResult
 *  ch.njol.skript.registrations.EventValues
 *  ch.njol.skript.util.Getter
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 */
package nl.mtvehicles.core.infrastructure.dependencies.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import nl.mtvehicles.core.events.VehiclePickUpEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="Vehicle Pick Up Event")
@Description(value={"Called when a vehicle is picked up"})
@Examples(value={"on vehicle pick up:", "set {_player} to event-player", "set {_licensePlate} to event-text"})
public class EvtVehiclePickUp
extends SkriptEvent {
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        return true;
    }

    public boolean check(Event e) {
        return true;
    }

    public String toString(@Nullable Event e, boolean debug) {
        return "Vehicle pick up event";
    }

    static {
        Skript.registerEvent((String)"VehiclePickUpEvent", EvtVehiclePickUp.class, VehiclePickUpEvent.class, (String[])new String[]{"[mtv] vehicle pick up"});
        EventValues.registerEventValue(VehiclePickUpEvent.class, Player.class, (Getter)new Getter<Player, VehiclePickUpEvent>(){

            public Player get(VehiclePickUpEvent event) {
                return event.getPlayer();
            }
        }, (int)0);
        EventValues.registerEventValue(VehiclePickUpEvent.class, String.class, (Getter)new Getter<String, VehiclePickUpEvent>(){

            public String get(VehiclePickUpEvent event) {
                return event.getLicensePlate();
            }
        }, (int)0);
    }
}

