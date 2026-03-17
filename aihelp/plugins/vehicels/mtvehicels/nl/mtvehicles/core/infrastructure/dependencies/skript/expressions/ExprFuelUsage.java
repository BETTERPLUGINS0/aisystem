/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  ch.njol.skript.classes.Changer$ChangeMode
 *  ch.njol.skript.doc.Description
 *  ch.njol.skript.doc.Examples
 *  ch.njol.skript.doc.Name
 *  ch.njol.skript.doc.Since
 *  ch.njol.skript.expressions.base.SimplePropertyExpression
 *  org.bukkit.event.Event
 */
package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name(value="MTV Vehicle's vehicle fuel usage")
@Description(value={"Get the vehicle's vehicle fuel usage"})
@Examples(value={"set {_fuel} to {_car}'s vehicle fuel usage", "set {_fuel} to vehicle fuel of (player's driven mtv vehicle)", "set vehicle fuel usage of {_car} to 0.5"})
@Since(value={"2.5.6"})
public class ExprFuelUsage
extends SimplePropertyExpression<Vehicle, Double> {
    protected String getPropertyName() {
        return "[mtv] vehicle fuel usage";
    }

    public Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Nullable
    public Double convert(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return vehicle.getFuelUsage();
    }

    public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return new Class[]{Double.class, Number.class};
        }
        return null;
    }

    public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull Changer.ChangeMode changeMode) {
        if (changeMode != Changer.ChangeMode.SET) {
            return;
        }
        Vehicle vehicle = (Vehicle)this.getExpr().getSingle(event);
        if (!Main.isNotNull(delta, delta[0], vehicle)) {
            return;
        }
        double changeValue = ((Number)delta[0]).doubleValue();
        vehicle.setFuelUsage(Math.max(0.0, changeValue));
        vehicle.save();
        if (VehicleData.fuelUsage.containsKey(vehicle.getLicensePlate())) {
            VehicleData.fuelUsage.put(vehicle.getLicensePlate(), Math.max(0.0, changeValue));
        }
    }

    static {
        ExprFuelUsage.register(ExprFuelUsage.class, Double.class, (String)"[mtv] vehicle fuel usage", (String)"mtvehicles");
    }
}

