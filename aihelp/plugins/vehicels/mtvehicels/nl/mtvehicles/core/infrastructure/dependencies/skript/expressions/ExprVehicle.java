/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  ch.njol.skript.Skript
 *  ch.njol.skript.doc.Description
 *  ch.njol.skript.doc.Examples
 *  ch.njol.skript.doc.Name
 *  ch.njol.skript.lang.Expression
 *  ch.njol.skript.lang.ExpressionType
 *  ch.njol.skript.lang.SkriptParser$ParseResult
 *  ch.njol.skript.lang.util.SimpleExpression
 *  ch.njol.util.Kleenean
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 */
package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="MTV Vehicle")
@Description(value={"Get the MTV's vehicle instance"})
@Examples(value={"set {_car} to mtv vehicle with license plate \"DF-4J-2R\"", "set {_helicopter} to player's driven mtv vehicle", "set {_car} to a new mtv vehicle with UUID \"C4UQZJ\" and owner player"})
public class ExprVehicle
extends SimpleExpression<Vehicle> {
    private Expression<String> licensePlate;
    private Expression<Player> player;
    private Expression<OfflinePlayer> offlinePlayer;
    private Expression<String> uuid;
    private int pattern;

    public Class<? extends Vehicle> getReturnType() {
        return Vehicle.class;
    }

    public boolean isSingle() {
        return true;
    }

    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        if (matchedPattern == 0) {
            this.licensePlate = expressions[0];
        } else if (matchedPattern == 1) {
            this.player = expressions[0];
        } else if (matchedPattern == 2) {
            this.uuid = expressions[0];
            this.offlinePlayer = expressions[1];
        } else if (matchedPattern == 3) {
            this.uuid = expressions[1];
            this.offlinePlayer = expressions[0];
        }
        this.pattern = matchedPattern;
        return true;
    }

    public String toString(@Nullable Event event, boolean debug) {
        return "MTVehicles vehicle";
    }

    protected Vehicle[] get(Event event) {
        if (this.pattern == 0) {
            if (this.licensePlate == null) {
                return null;
            }
            return new Vehicle[]{VehicleUtils.getVehicle((String)this.licensePlate.getSingle(event))};
        }
        if (this.pattern == 1) {
            return new Vehicle[]{VehicleUtils.getDrivenVehicle((Player)this.player.getSingle(event))};
        }
        if (!VehicleUtils.vehicleUUIDExists((String)this.uuid.getSingle(event))) {
            Main.logSevere("Skript error: Provided UUID does not exist (\"a new mtv vehicle with UUID %string% and owner %offlineplayer%\").");
            return null;
        }
        return new Vehicle[]{VehicleUtils.getVehicle(VehicleUtils.createAndGetItemByUUID((OfflinePlayer)this.offlinePlayer.getSingle(event), (String)this.uuid.getSingle(event)))};
    }

    static {
        Skript.registerExpression(ExprVehicle.class, Vehicle.class, (ExpressionType)ExpressionType.SIMPLE, (String[])new String[]{"[a[n]] mtv vehicle [with license [plate] %-string%]", "%player%'s driven mtv vehicle", "[a] new[ly] [created] mtv vehicle (by|with) (uuid|UUID) %string% [and] [(by|with)] owner %offlineplayer%", "[a] new[ly] [created] mtv vehicle (by|with) (uuid|UUID) owner %offlineplayer% [and] [(by|with)] (uuid|UUID) %string%"});
    }
}

