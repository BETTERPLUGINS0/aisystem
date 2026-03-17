/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.statics.strategies;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.MovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.MovementTypeAssociation;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.MovementType;

@MovementTypeAssociation(value=MovementType.AIR)
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE, setterVisibility=JsonAutoDetect.Visibility.ANY, isGetterVisibility=JsonAutoDetect.Visibility.NONE)
@JsonInclude(value=JsonInclude.Include.NON_DEFAULT)
@JsonTypeName(value="air")
public class AirMovementStrategy
implements MovementStrategy {
    private float liftoffSpeed;
    @JsonIgnore
    private int lift;

    public AirMovementStrategy(VehicleModel vehicleModel) {
        this.liftoffSpeed = (float)vehicleModel.getMaxSpeed().getBase().intValue() / 2.0f;
    }

    public AirMovementStrategy(float f) {
        this.liftoffSpeed = f;
    }

    @Generated
    public AirMovementStrategy() {
    }

    @Generated
    public float getLiftoffSpeed() {
        return this.liftoffSpeed;
    }

    @Generated
    public int getLift() {
        return this.lift;
    }

    @Generated
    public void setLiftoffSpeed(float f) {
        this.liftoffSpeed = f;
    }

    @JsonIgnore
    @Generated
    public void setLift(int n) {
        this.lift = n;
    }

    @Generated
    public String toString() {
        return "AirMovementStrategy(liftoffSpeed=" + this.getLiftoffSpeed() + ", lift=" + this.getLift() + ")";
    }
}

