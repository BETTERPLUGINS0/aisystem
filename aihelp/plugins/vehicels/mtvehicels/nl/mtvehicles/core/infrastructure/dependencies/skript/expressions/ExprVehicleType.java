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
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="MTV Vehicle's type")
@Description(value={"Get the vehicle's type as String"})
@Examples(value={"set {_type} to {_car}'s vehicle type", "set {_type} to vehicle type of (player's driven mtv vehicle)"})
public class ExprVehicleType
extends SimpleExpression<String> {
    private Expression<Vehicle> vehicle;

    public Class<? extends String> getReturnType() {
        return String.class;
    }

    public boolean isSingle() {
        return true;
    }

    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.vehicle = expressions[0];
        return true;
    }

    public String toString(@Nullable Event event, boolean debug) {
        return "MTVehicles vehicle type";
    }

    protected String[] get(Event event) {
        if (this.vehicle.getSingle(event) == null) {
            return null;
        }
        return new String[]{((Vehicle)this.vehicle.getSingle(event)).getVehicleType().toString()};
    }

    static {
        Skript.registerExpression(ExprVehicleType.class, String.class, (ExpressionType)ExpressionType.PROPERTY, (String[])new String[]{"%mtvehicle%'s [mtv] vehicle type", "[mtv] vehicle type of %mtvehicle%"});
    }
}

