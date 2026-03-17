/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles;

import java.util.function.Function;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.MovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.MovementType;
import org.jetbrains.annotations.NotNull;

public interface IVehicle {
    public <T extends MovementStrategy, R> R getFromStrategy(MovementType var1, Function<T, R> var2, @NotNull R var3);
}

