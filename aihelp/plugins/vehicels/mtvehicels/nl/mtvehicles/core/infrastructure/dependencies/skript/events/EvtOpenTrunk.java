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
import nl.mtvehicles.core.events.VehicleOpenTrunkEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="Vehicle Trunk Open Event")
@Description(value={"Called when a vehicle's trunk is opened'"})
@Examples(value={"on vehicle trunk open:", "set {_player} to event-player", "set {_licensePlate} to event-text"})
public class EvtOpenTrunk
extends SkriptEvent {
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        return true;
    }

    public boolean check(Event e) {
        return true;
    }

    public String toString(@Nullable Event e, boolean debug) {
        return "Vehicle open trunk event";
    }

    static {
        Skript.registerEvent((String)"VehicleOpenTrunkEvent", EvtOpenTrunk.class, VehicleOpenTrunkEvent.class, (String[])new String[]{"[mtv] vehicle trunk open"});
        EventValues.registerEventValue(VehicleOpenTrunkEvent.class, Player.class, (Getter)new Getter<Player, VehicleOpenTrunkEvent>(){

            public Player get(VehicleOpenTrunkEvent event) {
                return event.getPlayer();
            }
        }, (int)0);
        EventValues.registerEventValue(VehicleOpenTrunkEvent.class, String.class, (Getter)new Getter<String, VehicleOpenTrunkEvent>(){

            public String get(VehicleOpenTrunkEvent event) {
                return event.getLicensePlate();
            }
        }, (int)0);
    }
}

