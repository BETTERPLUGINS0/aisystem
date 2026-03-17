/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  ch.njol.skript.classes.Changer$ChangeMode
 *  ch.njol.skript.doc.Description
 *  ch.njol.skript.doc.Examples
 *  ch.njol.skript.doc.Name
 *  ch.njol.skript.expressions.base.SimplePropertyExpression
 *  org.bukkit.event.Event
 */
package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name(value="MTV Vehicle's license plate")
@Description(value={"Get the vehicle's license plate"})
@Examples(value={"set {_licensePlate} to {_car}'s vehicle license plate", "set {_licensePlate} to vehicle license plate of (player's driven mtv vehicle)", "set {_car}'s vehicle license plate to \"RW-2K-7I\""})
public class ExprLicensePlate
extends SimplePropertyExpression<Vehicle, String> {
    protected String getPropertyName() {
        return "[mtv] vehicle license plate";
    }

    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Nullable
    public String convert(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return vehicle.getLicensePlate();
    }

    public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return new Class[]{String.class};
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
        String newLicense = delta[0].toString();
        vehicle.setLicensePlate(newLicense);
        vehicle.save();
    }

    static {
        ExprLicensePlate.register(ExprLicensePlate.class, String.class, (String)"[mtv] vehicle license [plate]", (String)"mtvehicles");
    }
}

