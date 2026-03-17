/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  ch.njol.skript.Skript
 *  ch.njol.skript.doc.Description
 *  ch.njol.skript.doc.Examples
 *  ch.njol.skript.doc.Name
 *  ch.njol.skript.doc.Since
 *  ch.njol.skript.lang.Condition
 *  ch.njol.skript.lang.Expression
 *  ch.njol.skript.lang.SkriptParser$ParseResult
 *  ch.njol.util.Kleenean
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 */
package nl.mtvehicles.core.infrastructure.dependencies.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="Condition - Is inside a vehicle")
@Description(value={"Check if a player is seated in an MTV Vehicle"})
@Examples(value={"if player {_p} is seated in an mtv vehicle:", "if player is not inside mtv vehicle:"})
@Since(value={"2.5.6"})
public class CondIsInsideVehicle
extends Condition {
    private Expression<Player> player;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.setNegated(matchedPattern == 1);
        this.player = exprs[0];
        return true;
    }

    public boolean check(Event event) {
        boolean check;
        boolean bl = check = this.player.getSingle(event) != null && CondIsInsideVehicle.isInsideVehicle((Player)this.player.getSingle(event));
        if (!this.isNegated()) {
            return check;
        }
        return !check;
    }

    private static boolean isInsideVehicle(Player p) {
        return VehicleUtils.isInsideVehicle(p);
    }

    public String toString(@Nullable Event e, boolean d) {
        String neg = this.isNegated() ? " not" : "";
        return "Check if player is" + neg + " seated in MTV vehicle.";
    }

    static {
        Skript.registerCondition(CondIsInsideVehicle.class, (String[])new String[]{"[player] %player% is [(seated in|inside)] [(a|an)] mtv vehicle", "[player] %player% (isn't|is not) [(seated in|inside)] [(a|an)] mtv vehicle"});
    }
}

