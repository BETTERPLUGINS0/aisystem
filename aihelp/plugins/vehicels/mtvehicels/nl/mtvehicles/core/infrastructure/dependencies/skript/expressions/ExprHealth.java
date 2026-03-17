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

@Name(value="MTV Vehicle's vehicle health")
@Description(value={"Get the vehicle's vehicle fuel usage"})
@Examples(value={"set {_currentHealth} to {_car}'s vehicle health", "set {_health} to vehicle health of (player's driven mtv vehicle)", "set vehicle health of {_car} to 100", "remove 55.5 from {_car}'s vehicle health", "add 0.5 to {_car}'s vehicle health"})
@Since(value={"2.5.6"})
public class ExprHealth
extends SimplePropertyExpression<Vehicle, Double> {
    protected String getPropertyName() {
        return "[mtv] vehicle health";
    }

    public Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Nullable
    public Double convert(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return vehicle.getHealth();
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
        double changeValue = ((Number)delta[0]).doubleValue();
        double currentHealth = vehicle.getHealth();
        switch (changeMode) {
            case SET: {
                this.setHealth(vehicle, changeValue);
                break;
            }
            case ADD: {
                this.setHealth(vehicle, currentHealth + changeValue);
                break;
            }
            case REMOVE: {
                this.setHealth(vehicle, currentHealth - changeValue);
                break;
            }
        }
    }

    private void setHealth(@NotNull Vehicle vehicle, double newHealth) {
        double health;
        if (vehicle == null) {
            return;
        }
        double d = health = newHealth < 0.0 ? 0.0 : newHealth;
        if (health > 0.0) {
            VehicleData.markVehicleAsRepaired(vehicle.getLicensePlate());
        } else {
            VehicleData.markVehicleAsDestroyed(vehicle.getLicensePlate());
        }
        vehicle.setHealth(health);
        vehicle.save();
    }

    static {
        ExprHealth.register(ExprHealth.class, Double.class, (String)"[mtv] vehicle health", (String)"mtvehicles");
    }
}

