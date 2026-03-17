/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.statics;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.strategies.AirMovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.strategies.LandMovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.strategies.WaterMovementStrategy;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="movementType")
@JsonSubTypes(value={@JsonSubTypes.Type(value=AirMovementStrategy.class, name="air"), @JsonSubTypes.Type(value=LandMovementStrategy.class, name="land"), @JsonSubTypes.Type(value=WaterMovementStrategy.class, name="water")})
public interface MovementStrategy {
}

