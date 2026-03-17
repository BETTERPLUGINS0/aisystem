/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  ch.njol.skript.Skript
 *  ch.njol.skript.doc.Description
 *  ch.njol.skript.doc.Examples
 *  ch.njol.skript.doc.Name
 *  ch.njol.skript.doc.Since
 *  ch.njol.skript.lang.Expression
 *  ch.njol.skript.lang.ExpressionType
 *  ch.njol.skript.lang.SkriptParser$ParseResult
 *  ch.njol.skript.lang.util.SimpleExpression
 *  ch.njol.util.Kleenean
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
 */
package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Name(value="MTV Vehicle Item")
@Description(value={"Get the vehicle item (functional by license plate, or only aesthetic by UUID)."})
@Examples(value={"set {_car} to mtv vehicle with license plate \"DF-4J-2R\"", "set {_helicopter} to player's driven mtv vehicle", "set {_car} to a new mtv vehicle with UUID \"C4UQZJ\" and owner player"})
@Since(value={"2.5.6"})
public class ExprVehicleItem
extends SimpleExpression<ItemStack> {
    private Expression<String> identifier;
    private int pattern;

    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    public boolean isSingle() {
        return true;
    }

    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.identifier = expressions[0];
        this.pattern = matchedPattern;
        return true;
    }

    public String toString(@Nullable Event event, boolean debug) {
        return "MTVehicles (aesthetic) vehicle item";
    }

    protected ItemStack[] get(Event event) {
        if (this.pattern == 0) {
            return new ItemStack[]{ItemUtils.getVehicleItem((String)this.identifier.getSingle(event))};
        }
        if (!VehicleUtils.vehicleUUIDExists((String)this.identifier.getSingle(event))) {
            Main.logSevere("Skript error: Provided UUID does not exist (\"an aesthetic mtv vehicle item with uuid|UUID %string%\").");
            return null;
        }
        return new ItemStack[]{VehicleUtils.getItem((String)this.identifier.getSingle(event))};
    }

    static {
        Skript.registerExpression(ExprVehicleItem.class, ItemStack.class, (ExpressionType)ExpressionType.SIMPLE, (String[])new String[]{"[a[n]] mtv vehicle item (by|with) license [plate] %string%", "[a[n]] aesthetic mtv vehicle item (by|with) (uuid|UUID) %string%"});
    }
}

