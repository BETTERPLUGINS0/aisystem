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

@Name(value="MTV Vehicle's vehicle fuel")
@Description(value={"Get the vehicle's vehicle fuel (from VehicleData or VehicleData.yml \u2013 whatever is lower)"})
@Examples(value={"set {_fuel} to {_car}'s vehicle fuel level", "set {_fuel} to vehicle fuel level of (player's driven mtv vehicle)", "set vehicle fuel level of {_car} to 96", "remove 10 from {_car}'s vehicle fuel level"})
@Since(value={"2.5.6"})
public class ExprFuel
extends SimplePropertyExpression<Vehicle, Double> {
    protected String getPropertyName() {
        return "[mtv] vehicle fuel level";
    }

    public Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Nullable
    public Double convert(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        String license = vehicle.getLicensePlate();
        Double dataFuel = VehicleData.fuel.get(license);
        if (dataFuel == null) {
            dataFuel = 100.0;
        }
        double configFuel = vehicle.getFuel();
        return Math.min(dataFuel, configFuel);
    }

    public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE) {
            return new Class[]{Double.class, Number.class};
        }
        return null;
    }

    public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull Changer.ChangeMode changeMode) {
        Vehicle vehicle = (Vehicle)this.getExpr().getSingle(event);
        if (!Main.isNotNull(delta, delta[0], ((Number)delta[0]).doubleValue())) {
            return;
        }
        if (!Main.isNotNull(vehicle.getLicensePlate())) {
            return;
        }
        double changeValue = ((Number)delta[0]).doubleValue();
        double currentFuel = Math.min(VehicleData.fuel.get(vehicle.getLicensePlate()), vehicle.getFuel());
        switch (changeMode) {
            case SET: {
                this.setFuel(vehicle, currentFuel, changeValue);
                break;
            }
            case ADD: {
                this.setFuel(vehicle, currentFuel, currentFuel + changeValue);
                break;
            }
            case REMOVE: {
                this.setFuel(vehicle, currentFuel, currentFuel - changeValue);
                break;
            }
        }
    }

    private void setFuel(Vehicle vehicle, double currentFuel, double newFuel) {
        String licensePlate = vehicle.getLicensePlate();
        if (licensePlate == null) {
            return;
        }
        double finalFuel = Math.max(0.0, Math.min(100.0, newFuel));
        vehicle.setFuel(finalFuel);
        vehicle.save();
        VehicleData.fuel.put(licensePlate, finalFuel);
        if (VehicleData.fallDamage.get(vehicle.getLicensePlate()) != null && finalFuel > currentFuel) {
            VehicleData.fallDamage.remove(vehicle.getLicensePlate());
        }
    }

    static {
        ExprFuel.register(ExprFuel.class, Double.class, (String)"[mtv] vehicle fuel level", (String)"mtvehicles");
    }
}

