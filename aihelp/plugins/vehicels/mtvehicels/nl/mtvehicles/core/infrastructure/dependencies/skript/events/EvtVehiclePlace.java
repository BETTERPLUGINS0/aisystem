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
 *  org.bukkit.Location
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
import nl.mtvehicles.core.events.VehiclePlaceEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="Vehicle Place Event")
@Description(value={"Called when a vehicle is placed"})
@Examples(value={"on vehicle place:", "set {_driver} to event-player", "set {_licensePlate} to event-text", "set {_placeLocation} to event-location"})
public class EvtVehiclePlace
extends SkriptEvent {
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        return true;
    }

    public boolean check(Event e) {
        return true;
    }

    public String toString(@Nullable Event e, boolean debug) {
        return "Vehicle place event";
    }

    static {
        Skript.registerEvent((String)"VehiclePlaceEvent", EvtVehiclePlace.class, VehiclePlaceEvent.class, (String[])new String[]{"[mtv] vehicle place"});
        EventValues.registerEventValue(VehiclePlaceEvent.class, Player.class, (Getter)new Getter<Player, VehiclePlaceEvent>(){

            public Player get(VehiclePlaceEvent event) {
                return event.getPlayer();
            }
        }, (int)0);
        EventValues.registerEventValue(VehiclePlaceEvent.class, String.class, (Getter)new Getter<String, VehiclePlaceEvent>(){

            public String get(VehiclePlaceEvent event) {
                return event.getLicensePlate();
            }
        }, (int)0);
        EventValues.registerEventValue(VehiclePlaceEvent.class, Location.class, (Getter)new Getter<Location, VehiclePlaceEvent>(){

            public Location get(VehiclePlaceEvent event) {
                return event.getLocation();
            }
        }, (int)0);
    }
}

