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
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
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
import java.util.Objects;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Name(value="Give an MTV vehicle")
@Description(value={"Give a vehicle to a player"})
@Examples(value={"give mtv vehicle {_boat} to player", "give {_player} mtv vehicle with license plate \"DF-4J-2R\"", "give {_player} mtv vehicle with uuid \"XN9MKB\""})
public class EffGiveVehicle
extends Effect {
    private Expression<Vehicle> vehicle;
    private Expression<String> text;
    private Expression<Player> player;
    private int pattern;

    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        if (matchedPattern == 0) {
            this.text = expressions[0];
            this.player = expressions[1];
            this.pattern = 2;
        } else if (matchedPattern == 1) {
            this.text = expressions[1];
            this.player = expressions[0];
            this.pattern = 2;
        } else if (matchedPattern == 2) {
            this.text = expressions[0];
            this.player = expressions[1];
            this.pattern = 3;
        } else if (matchedPattern == 3) {
            this.text = expressions[1];
            this.player = expressions[0];
            this.pattern = 3;
        } else if (matchedPattern == 4) {
            this.vehicle = expressions[0];
            this.player = expressions[1];
            this.pattern = 1;
        } else {
            this.vehicle = expressions[1];
            this.player = expressions[0];
            this.pattern = 1;
        }
        return true;
    }

    public String toString(@Nullable Event event, boolean debug) {
        return String.format("Give a vehicle to player %s", this.player.toString(event, debug));
    }

    protected void execute(Event event) {
        if (this.player.getSingle(event) == null) {
            return;
        }
        if (this.pattern == 1) {
            if (this.vehicle.getSingle(event) == null) {
                return;
            }
            ((Player)this.player.getSingle(event)).getInventory().addItem(new ItemStack[]{ItemUtils.getVehicleItem(((Vehicle)this.vehicle.getSingle(event)).getLicensePlate())});
        } else if (this.pattern == 2) {
            ((Player)this.player.getSingle(event)).getInventory().addItem(new ItemStack[]{ItemUtils.getVehicleItem((String)this.text.getSingle(event))});
        } else {
            if (!VehicleUtils.vehicleUUIDExists((String)this.text.getSingle(event))) {
                Main.logSevere("Skript error: Provided UUID does not exist (\"give %player% [mtv] vehicle (by|with) (UUID|uuid) %string%\").");
                return;
            }
            ((Player)this.player.getSingle(event)).getInventory().addItem(new ItemStack[]{Objects.requireNonNull(VehicleUtils.createAndGetItemByUUID((OfflinePlayer)this.player.getSingle(event), (String)this.text.getSingle(event)))});
        }
    }

    static {
        Skript.registerEffect(EffGiveVehicle.class, (String[])new String[]{"give [mtv] vehicle (by|with) license [plate] %string% to %player%", "give %player% [mtv] vehicle (by|with) license [plate] %string%", "give [mtv] vehicle (by|with) (UUID|uuid) %string% to %player%", "give %player% [mtv] vehicle (by|with) (UUID|uuid) %string%", "give [mtv] vehicle %mtvehicle% to %player%", "give %player% [mtv] vehicle %mtvehicle%"});
    }
}

