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
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="Despawn an MTV vehicle")
@Description(value={"Despawn a vehicle from all locations where it may be placed"})
@Examples(value={"despawn mtv vehicle {_car}"})
public class EffDespawnVehicle
extends Effect {
    private Expression<Vehicle> vehicle;

    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.vehicle = expressions[0];
        return true;
    }

    public String toString(@Nullable Event event, boolean debug) {
        return "Despawn vehicle.";
    }

    protected void execute(Event event) {
        if (this.vehicle.getSingle(event) == null) {
            return;
        }
        VehicleUtils.despawnVehicle(((Vehicle)this.vehicle.getSingle(event)).getLicensePlate());
    }

    static {
        Skript.registerEffect(EffDespawnVehicle.class, (String[])new String[]{"(despawn|hide) [mtv] vehicle %mtvehicle%"});
    }
}

