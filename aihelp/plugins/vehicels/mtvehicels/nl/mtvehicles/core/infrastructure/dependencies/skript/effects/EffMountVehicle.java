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
 *  org.bukkit.entity.Player
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
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="Mount an MTV vehicle")
@Description(value={"Make player mount (start driving) a vehicle"})
@Examples(value={"make player mount mtv vehicle {_car}"})
public class EffMountVehicle
extends Effect {
    private Expression<Vehicle> vehicle;
    private Expression<Player> player;

    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.vehicle = expressions[1];
        this.player = expressions[0];
        return true;
    }

    public String toString(@Nullable Event event, boolean debug) {
        return String.format("Make player %s mount vehicle.", this.player.toString(event, debug));
    }

    protected void execute(Event event) {
        if (this.vehicle.getSingle(event) == null) {
            return;
        }
        if (!((Vehicle)this.vehicle.getSingle(event)).isOccupied()) {
            VehicleUtils.enterVehicle(((Vehicle)this.vehicle.getSingle(event)).getLicensePlate(), (Player)this.player.getSingle(event));
        }
    }

    static {
        Skript.registerEffect(EffMountVehicle.class, (String[])new String[]{"make [player] %player% (mount|drive|ride) [mtv] vehicle %mtvehicle%"});
    }
}

