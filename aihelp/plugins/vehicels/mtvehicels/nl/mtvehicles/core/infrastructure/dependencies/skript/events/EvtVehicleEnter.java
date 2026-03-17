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
import nl.mtvehicles.core.events.VehicleEnterEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="Vehicle Enter Event")
@Description(value={"Called when a vehicle is entered"})
@Examples(value={"on mtv vehicle enter:", "set {_player} to event-player", "set {_licensePlate} to event-text", "set {_vehicleLocation} to event-location"})
public class EvtVehicleEnter
extends SkriptEvent {
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        return true;
    }

    public boolean check(Event e) {
        return true;
    }

    public String toString(@Nullable Event e, boolean debug) {
        return "Vehicle enter event";
    }

    static {
        Skript.registerEvent((String)"VehicleEnterEvent", EvtVehicleEnter.class, VehicleEnterEvent.class, (String[])new String[]{"[mtv] vehicle enter"});
        EventValues.registerEventValue(VehicleEnterEvent.class, Player.class, (Getter)new Getter<Player, VehicleEnterEvent>(){

            public Player get(VehicleEnterEvent event) {
                return event.getPlayer();
            }
        }, (int)0);
        EventValues.registerEventValue(VehicleEnterEvent.class, String.class, (Getter)new Getter<String, VehicleEnterEvent>(){

            public String get(VehicleEnterEvent event) {
                return event.getLicensePlate();
            }
        }, (int)0);
        EventValues.registerEventValue(VehicleEnterEvent.class, Location.class, (Getter)new Getter<Location, VehicleEnterEvent>(){

            public Location get(VehicleEnterEvent event) {
                return event.getLocation();
            }
        }, (int)0);
    }
}

