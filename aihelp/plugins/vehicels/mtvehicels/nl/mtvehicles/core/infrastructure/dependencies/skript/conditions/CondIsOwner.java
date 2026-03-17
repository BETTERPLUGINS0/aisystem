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
 *  org.bukkit.OfflinePlayer
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
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="Condition - Is player the owner of a vehicle")
@Description(value={"Check if a player is the owner of an MTV Vehicle"})
@Examples(value={"if player is the vehicle owner of {_car}:"})
@Since(value={"2.5.5"})
public class CondIsOwner
extends Condition {
    private Expression<Vehicle> vehicle;
    private Expression<Player> player;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.setNegated(matchedPattern == 1);
        this.vehicle = exprs[1];
        this.player = exprs[0];
        return true;
    }

    public boolean check(Event event) {
        if (!Main.isNotNull(this.vehicle.getSingle(event), this.player.getSingle(event))) {
            return this.isNegated();
        }
        boolean check = ((Vehicle)this.vehicle.getSingle(event)).isOwner((OfflinePlayer)this.player.getSingle(event));
        if (!this.isNegated()) {
            return check;
        }
        return !check;
    }

    public String toString(@Nullable Event e, boolean d) {
        String neg = this.isNegated() ? " not" : "";
        return "Check if player is " + neg + " the owner of an MTV vehicle.";
    }

    static {
        Skript.registerCondition(CondIsOwner.class, (String[])new String[]{"[player] %player% is [the] [mtv] vehicle owner of %mtvehicle%", "[player] %player% (isn't|is not) [the] [mtv] vehicle owner of %mtvehicle%"});
    }
}

