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

@Name(value="MTV Vehicle's vehicle speed")
@Description(value={"Get the vehicle's vehicle speed"})
@Examples(value={"set {_licensePlate} to {_car}'s vehicle speed", "add 0.5 to vehicle speed of (player's driven mtv vehicle)", "set mtv vehicle speed of {_helicopter} to 3"})
@Since(value={"2.5.5"})
public class ExprSpeed
extends SimplePropertyExpression<Vehicle, Double> {
    protected String getPropertyName() {
        return "[mtv] vehicle speed";
    }

    @Nullable
    public Double convert(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return VehicleData.speed.get(vehicle.getLicensePlate());
    }

    public Class<? extends Double> getReturnType() {
        return Double.class;
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
        if (!Main.isNotNull(delta, delta[0], vehicle)) {
            return;
        }
        String licensePlate = vehicle.getLicensePlate();
        double changeValue = ((Number)delta[0]).doubleValue();
        switch (changeMode) {
            case SET: {
                VehicleData.speed.put(licensePlate, changeValue);
                break;
            }
            case ADD: {
                VehicleData.speed.put(licensePlate, VehicleData.speed.get(licensePlate) + changeValue);
                break;
            }
            case REMOVE: {
                VehicleData.speed.put(licensePlate, VehicleData.speed.get(licensePlate) - changeValue);
                break;
            }
        }
    }

    static {
        ExprSpeed.register(ExprSpeed.class, Double.class, (String)"[mtv] vehicle speed", (String)"mtvehicles");
    }
}

