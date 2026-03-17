/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  ch.njol.skript.Skript
 *  ch.njol.skript.doc.Description
 *  ch.njol.skript.doc.Examples
 *  ch.njol.skript.doc.Name
 *  ch.njol.skript.lang.Effect
 *  ch.njol.skript.lang.Expression
 *  ch.njol.skript.lang.SkriptParser$ParseResult
 *  ch.njol.util.Kleenean
 *  org.bukkit.Location
 *  org.bukkit.event.Event
 */
package nl.mtvehicles.core.infrastructure.dependencies.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="Teleport vehicle")
@Description(value={"Teleport a vehicle to a location"})
@Examples(value={"teleport mtv vehicle {_car} to location {_loc}"})
public class EffTeleportVehicle
extends Effect {
    private Expression<Vehicle> vehicle;
    private Expression<Location> location;

    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.location = expressions[1];
        this.vehicle = expressions[0];
        return true;
    }

    public String toString(@Nullable Event event, boolean debug) {
        return String.format("Teleport vehicle to location %s.", this.location.toString(event, debug));
    }

    protected void execute(Event event) {
        if (this.vehicle.getSingle(event) == null) {
            return;
        }
        VehicleUtils.teleportVehicle(((Vehicle)this.vehicle.getSingle(event)).getLicensePlate(), (Location)this.location.getSingle(event));
    }

    static {
        Skript.registerEffect(EffTeleportVehicle.class, (String[])new String[]{"(teleport|tp) [a[n]] [mtv] vehicle %mtvehicle% to [location] %location%"});
    }
}

