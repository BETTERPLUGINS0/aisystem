/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  ch.njol.skript.Skript
 *  ch.njol.skript.doc.Description
 *  ch.njol.skript.doc.Examples
 *  ch.njol.skript.doc.Name
 *  ch.njol.skript.doc.Since
 *  ch.njol.skript.lang.Literal
 *  ch.njol.skript.lang.SkriptEvent
 *  ch.njol.skript.lang.SkriptParser$ParseResult
 *  ch.njol.skript.registrations.EventValues
 *  ch.njol.skript.util.Getter
 *  org.bukkit.entity.Entity
 *  org.bukkit.event.Event
 */
package nl.mtvehicles.core.infrastructure.dependencies.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import nl.mtvehicles.core.events.VehicleDamageEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="Vehicle Damage Event")
@Description(value={"Called when a vehicle is damaged"})
@Examples(value={"on mtv vehicle damage:", "set {_damager} to event-entity", "set {_licensePlate} to event-text", "set {_damage} to event-number"})
@Since(value={"2.5.6"})
public class EvtVehicleDamage
extends SkriptEvent {
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        return true;
    }

    public boolean check(Event e) {
        return true;
    }

    public String toString(@Nullable Event e, boolean debug) {
        return "Vehicle damage event";
    }

    static {
        Skript.registerEvent((String)"VehicleDamageEvent", EvtVehicleDamage.class, VehicleDamageEvent.class, (String[])new String[]{"[mtv] vehicle damage"});
        EventValues.registerEventValue(VehicleDamageEvent.class, Entity.class, (Getter)new Getter<Entity, VehicleDamageEvent>(){

            public Entity get(VehicleDamageEvent event) {
                return event.getDamager();
            }
        }, (int)0);
        EventValues.registerEventValue(VehicleDamageEvent.class, String.class, (Getter)new Getter<String, VehicleDamageEvent>(){

            public String get(VehicleDamageEvent event) {
                return event.getLicensePlate();
            }
        }, (int)0);
        EventValues.registerEventValue(VehicleDamageEvent.class, Double.class, (Getter)new Getter<Double, VehicleDamageEvent>(){

            public Double get(VehicleDamageEvent event) {
                return event.getDamage();
            }
        }, (int)0);
    }
}

