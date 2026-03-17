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
 *  org.bukkit.Location
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
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="MTV Vehicle's location")
@Description(value={"Get the vehicle location"})
@Examples(value={"set {_loc} to vehicle location of license plate \"DF-4J-2R\""})
public class ExprLicensePlateVehicleLocation
extends SimpleExpression<Location> {
    private Expression<String> license;

    public Class<? extends Location> getReturnType() {
        return Location.class;
    }

    public boolean isSingle() {
        return true;
    }

    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.license = expressions[0];
        return true;
    }

    public String toString(@Nullable Event event, boolean debug) {
        return "MTVehicle's location";
    }

    protected Location[] get(Event event) {
        if (this.license.getSingle(event) == null) {
            return null;
        }
        return new Location[]{VehicleUtils.getLocation((String)this.license.getSingle(event))};
    }

    static {
        Skript.registerExpression(ExprLicensePlateVehicleLocation.class, Location.class, (ExpressionType)ExpressionType.PROPERTY, (String[])new String[]{"[the] [mtv] vehicle location of license [plate] %string%"});
    }
}

