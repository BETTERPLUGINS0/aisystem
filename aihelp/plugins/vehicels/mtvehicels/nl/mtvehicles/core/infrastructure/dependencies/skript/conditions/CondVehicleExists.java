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
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="Condition - Vehicle exists")
@Description(value={"Check if an MTV Vehicle exists (is not deleted)"})
@Examples(value={"if the vehicle {_car} exists:", "if the vehicle {_car} is not deleted:"})
@Since(value={"2.5.6"})
public class CondVehicleExists
extends Condition {
    private Expression<Vehicle> vehicle;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.vehicle = exprs[0];
        return true;
    }

    public boolean check(Event event) {
        return this.vehicle.getSingle(event) != null;
    }

    public String toString(@Nullable Event e, boolean d) {
        return "Check if vehicle exists.";
    }

    static {
        Skript.registerCondition(CondVehicleExists.class, (String[])new String[]{"[the] [mtv] vehicle %mtvehicle% exist[s]", "[the] [mtv] vehicle %mtvehicle% (isn't|is not) deleted"});
    }
}

