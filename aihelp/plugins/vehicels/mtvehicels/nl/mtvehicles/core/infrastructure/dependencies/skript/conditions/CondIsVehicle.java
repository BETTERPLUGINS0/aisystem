/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  ch.njol.skript.Skript
 *  ch.njol.skript.doc.Description
 *  ch.njol.skript.doc.Examples
 *  ch.njol.skript.doc.Name
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
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="Condition - Is variable a vehicle")
@Description(value={"Check if a variable is an MTV Vehicle"})
@Examples(value={"if {_car} is an mtv vehicle:"})
@Deprecated
public class CondIsVehicle
extends Condition {
    private Expression<Object> vehicle;

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.setNegated(matchedPattern == 1);
        this.vehicle = exprs[0];
        return true;
    }

    public boolean check(Event event) {
        return this.vehicle.check(event, CondIsVehicle::isVehicle, this.isNegated());
    }

    private static boolean isVehicle(Object var) {
        return var instanceof Vehicle;
    }

    public String toString(@Nullable Event e, boolean d) {
        String neg = this.isNegated() ? " not" : "";
        return "Check if variable is" + neg + " an MTV vehicle.";
    }

    static {
        Skript.registerCondition(CondIsVehicle.class, (String[])new String[]{"%object% is [(a|an)] [mtv] vehicle", "%object% (isn't|is not) [(a|an)] [mtv] vehicle"});
    }
}

