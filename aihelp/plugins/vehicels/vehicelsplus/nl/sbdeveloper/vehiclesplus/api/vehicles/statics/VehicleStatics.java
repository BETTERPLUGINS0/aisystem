/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.statics;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE, setterVisibility=JsonAutoDetect.Visibility.ANY, isGetterVisibility=JsonAutoDetect.Visibility.NONE)
public class VehicleStatics {
    private final int maxSpeed;
    private final float turningRadius;
    private final int fuelTank;
    private final float acceleration;
    private final float brakeForce;
    private int maxSpeedModifier;
    private float turningRadiusModifier;
    private int fuelTankModifier;
    private float accelerationModifier;
    private float brakeForceModifier;
    private int currentHealth;
    private double currentFuel;
    private boolean broken = false;
    @JsonIgnore
    private float currentSpeed = 0.0f;
    @JsonIgnore
    private float currentSteering = 0.0f;
    @JsonIgnore
    private Integer tempMaxSpeedOverride = null;

    public VehicleStatics(VehicleModel vehicleModel) {
        this.currentHealth = vehicleModel.getHealth();
        this.maxSpeed = vehicleModel.getMaxSpeed().getBase().intValue();
        this.turningRadius = vehicleModel.getTurningRadius().getBase().floatValue();
        this.fuelTank = vehicleModel.getFuelTank().getBase().intValue();
        this.acceleration = vehicleModel.getAcceleration().getBase().floatValue();
        this.currentFuel = vehicleModel.getFuelTank().getBase().doubleValue();
        this.brakeForce = this.acceleration / 50.0f * 2.0f;
    }

    @JsonCreator
    private VehicleStatics(@JsonProperty(required=true, value="maxSpeed") int n, @JsonProperty(required=true, value="turningRadius") float f, @JsonProperty(required=true, value="fuelTank") int n2, @JsonProperty(required=true, value="acceleration") float f2, @JsonProperty(required=true, value="brakeForce") float f3, @JsonProperty(value="maxSpeedModifier") int n3, @JsonProperty(value="turningRadiusModifier") float f4, @JsonProperty(value="fuelTankModifier") int n4, @JsonProperty(value="accelerationModifier") float f5, @JsonProperty(value="brakeForceModifier") float f6, @JsonProperty(value="currentHealth") int n5, @JsonProperty(value="currentFuel") double d, @JsonProperty(value="broken") boolean bl) {
        this.maxSpeed = n;
        this.turningRadius = f;
        this.fuelTank = n2;
        this.acceleration = f2;
        this.brakeForce = f3;
        this.maxSpeedModifier = n3;
        this.turningRadiusModifier = f4;
        this.fuelTankModifier = n4;
        this.accelerationModifier = f5;
        this.brakeForceModifier = f6;
        this.currentHealth = n5;
        this.currentFuel = d;
        this.broken = bl;
    }

    public float getTurningRadius() {
        return this.turningRadius + this.turningRadiusModifier;
    }

    public int getFuelTank() {
        return this.fuelTank + this.fuelTankModifier;
    }

    public float getAcceleration() {
        return this.acceleration + this.accelerationModifier;
    }

    public float getBrakeForce() {
        return this.brakeForce + this.brakeForceModifier;
    }

    public void forceSetMaxSpeed(int n) {
        this.maxSpeedModifier = n - this.maxSpeed;
    }

    public void forceSetCurrentHealth(int n) {
        this.currentHealth = n;
    }

    public void forceSetTurningRadius(float f) {
        this.turningRadiusModifier = f - this.turningRadius;
    }

    public void forceSetFuelTank(int n) {
        this.fuelTankModifier = n - this.fuelTank;
    }

    public void forceSetAcceleration(float f) {
        this.accelerationModifier = f - this.acceleration;
    }

    public void forceSetBrakeForce(float f) {
        this.brakeForceModifier = f - this.brakeForce;
    }

    public int getMaxSpeed() {
        if (this.tempMaxSpeedOverride != null) {
            return this.tempMaxSpeedOverride;
        }
        return this.maxSpeed + this.maxSpeedModifier;
    }

    public int getCurrentSpeedKMPH() {
        if (this.currentSpeed >= 0.0f) {
            return (int)this.currentSpeed;
        }
        return (int)(this.currentSpeed * -1.0f);
    }

    public boolean isMoving() {
        return this.currentSpeed != 0.0f;
    }

    public void resetMovingValues() {
        this.currentSpeed = 0.0f;
        this.currentSteering = 0.0f;
    }

    @Generated
    public int getMaxSpeedModifier() {
        return this.maxSpeedModifier;
    }

    @Generated
    public float getTurningRadiusModifier() {
        return this.turningRadiusModifier;
    }

    @Generated
    public int getFuelTankModifier() {
        return this.fuelTankModifier;
    }

    @Generated
    public float getAccelerationModifier() {
        return this.accelerationModifier;
    }

    @Generated
    public float getBrakeForceModifier() {
        return this.brakeForceModifier;
    }

    @Generated
    public int getCurrentHealth() {
        return this.currentHealth;
    }

    @Generated
    public double getCurrentFuel() {
        return this.currentFuel;
    }

    @Generated
    public boolean isBroken() {
        return this.broken;
    }

    @Generated
    public float getCurrentSpeed() {
        return this.currentSpeed;
    }

    @Generated
    public float getCurrentSteering() {
        return this.currentSteering;
    }

    @Generated
    public Integer getTempMaxSpeedOverride() {
        return this.tempMaxSpeedOverride;
    }

    @Generated
    public void setMaxSpeedModifier(int n) {
        this.maxSpeedModifier = n;
    }

    @Generated
    public void setTurningRadiusModifier(float f) {
        this.turningRadiusModifier = f;
    }

    @Generated
    public void setFuelTankModifier(int n) {
        this.fuelTankModifier = n;
    }

    @Generated
    public void setAccelerationModifier(float f) {
        this.accelerationModifier = f;
    }

    @Generated
    public void setBrakeForceModifier(float f) {
        this.brakeForceModifier = f;
    }

    @Generated
    public void setCurrentHealth(int n) {
        this.currentHealth = n;
    }

    @Generated
    public void setCurrentFuel(double d) {
        this.currentFuel = d;
    }

    @Generated
    public void setBroken(boolean bl) {
        this.broken = bl;
    }

    @JsonIgnore
    @Generated
    public void setCurrentSpeed(float f) {
        this.currentSpeed = f;
    }

    @JsonIgnore
    @Generated
    public void setCurrentSteering(float f) {
        this.currentSteering = f;
    }

    @JsonIgnore
    @Generated
    public void setTempMaxSpeedOverride(Integer n) {
        this.tempMaxSpeedOverride = n;
    }

    @Generated
    public String toString() {
        return "VehicleStatics(maxSpeed=" + this.getMaxSpeed() + ", turningRadius=" + this.getTurningRadius() + ", fuelTank=" + this.getFuelTank() + ", acceleration=" + this.getAcceleration() + ", brakeForce=" + this.getBrakeForce() + ", maxSpeedModifier=" + this.getMaxSpeedModifier() + ", turningRadiusModifier=" + this.getTurningRadiusModifier() + ", fuelTankModifier=" + this.getFuelTankModifier() + ", accelerationModifier=" + this.getAccelerationModifier() + ", brakeForceModifier=" + this.getBrakeForceModifier() + ", currentHealth=" + this.getCurrentHealth() + ", currentFuel=" + this.getCurrentFuel() + ", broken=" + this.isBroken() + ", currentSpeed=" + this.getCurrentSpeed() + ", currentSteering=" + this.getCurrentSteering() + ", tempMaxSpeedOverride=" + this.getTempMaxSpeedOverride() + ")";
    }
}

