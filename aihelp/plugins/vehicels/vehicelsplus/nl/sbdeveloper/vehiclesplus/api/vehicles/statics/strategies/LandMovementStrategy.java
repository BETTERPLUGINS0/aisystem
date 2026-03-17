/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.statics.strategies;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.MovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.MovementTypeAssociation;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.MovementType;

@MovementTypeAssociation(value=MovementType.LAND)
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE, setterVisibility=JsonAutoDetect.Visibility.ANY, isGetterVisibility=JsonAutoDetect.Visibility.NONE)
@JsonTypeName(value="land")
public class LandMovementStrategy
implements MovementStrategy {
    public LandMovementStrategy(VehicleModel vehicleModel) {
    }

    @Generated
    public LandMovementStrategy() {
    }

    @Generated
    public String toString() {
        return "LandMovementStrategy()";
    }
}

