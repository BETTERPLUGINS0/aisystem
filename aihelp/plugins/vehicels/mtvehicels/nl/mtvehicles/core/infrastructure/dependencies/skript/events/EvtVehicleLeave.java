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
import nl.mtvehicles.core.events.VehicleLeaveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="Vehicle Leave Event")
@Description(value={"Called when a vehicle is left"})
@Examples(value={"on vehicle leave:", "set {_player} to event-player", "set {_licensePlate} to event-text"})
public class EvtVehicleLeave
extends SkriptEvent {
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        return true;
    }

    public boolean check(Event e) {
        return true;
    }

    public String toString(@Nullable Event e, boolean debug) {
        return "Vehicle leave event";
    }

    static {
        Skript.registerEvent((String)"VehicleLeaveEvent", EvtVehicleLeave.class, VehicleLeaveEvent.class, (String[])new String[]{"[mtv] vehicle leave"});
        EventValues.registerEventValue(VehicleLeaveEvent.class, Player.class, (Getter)new Getter<Player, VehicleLeaveEvent>(){

            public Player get(VehicleLeaveEvent event) {
                return event.getPlayer();
            }
        }, (int)0);
        EventValues.registerEventValue(VehicleLeaveEvent.class, String.class, (Getter)new Getter<String, VehicleLeaveEvent>(){

            public String get(VehicleLeaveEvent event) {
                return event.getLicensePlate();
            }
        }, (int)0);
    }
}

