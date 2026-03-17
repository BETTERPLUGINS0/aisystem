/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.util.Vector
 */
package me.zombie_striker.qav.vehicles;

import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.events.VehicleTurnEvent;
import me.zombie_striker.qav.util.HeadPoseUtil;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

public class AbstractHelicopter
extends AbstractVehicle {
    private double descentSpeed = -0.1;

    public AbstractHelicopter(String string, int n) {
        super(string, n);
    }

    @Override
    public void handleTurnLeft(VehicleEntity vehicleEntity, Player player) {
        VehicleTurnEvent vehicleTurnEvent = new VehicleTurnEvent(vehicleEntity, vehicleEntity.getAngleRotation(), vehicleEntity.getAngleRotation() + vehicleEntity.getType().getRotationDelta());
        Bukkit.getPluginManager().callEvent((Event)vehicleTurnEvent);
        if (vehicleTurnEvent.isCanceled()) {
            return;
        }
        vehicleEntity.setAngle((vehicleEntity.getAngleRotation() + vehicleEntity.getType().getRotationDelta()) * this.getRotationMultiplier());
        HeadPoseUtil.setHeadPoseUsingReflection(vehicleEntity);
    }

    @Override
    public void handleTurnRight(VehicleEntity vehicleEntity, Player player) {
        VehicleTurnEvent vehicleTurnEvent = new VehicleTurnEvent(vehicleEntity, vehicleEntity.getAngleRotation(), vehicleEntity.getAngleRotation() - vehicleEntity.getType().getRotationDelta());
        Bukkit.getPluginManager().callEvent((Event)vehicleTurnEvent);
        if (vehicleTurnEvent.isCanceled()) {
            return;
        }
        vehicleEntity.setAngle((vehicleEntity.getAngleRotation() - vehicleEntity.getType().getRotationDelta()) * this.getRotationMultiplier());
        HeadPoseUtil.setHeadPoseUsingReflection(vehicleEntity);
    }

    @Override
    public void handleSpeedIncrease(VehicleEntity vehicleEntity, Player player) {
        if (!this.handleFuel(vehicleEntity, player)) {
            return;
        }
        vehicleEntity.setSpeed(Math.min(vehicleEntity.getSpeed() + 0.1, vehicleEntity.getType().getMaxSpeed()));
    }

    @Override
    public void handleSpeedDecrease(VehicleEntity vehicleEntity, Player player) {
        if (!this.handleFuel(vehicleEntity, player)) {
            return;
        }
        vehicleEntity.setSpeed(Math.max(vehicleEntity.getSpeed() - 0.1, -vehicleEntity.getType().getMaxBackupSpeed()));
    }

    @Override
    public void handleSpace(VehicleEntity vehicleEntity, Player player) {
        if (player.getLocation().getPitch() > 0.0f) {
            vehicleEntity.setDirectionYHeight(-1.0);
        } else {
            vehicleEntity.setDirectionYHeight(1.0);
        }
    }

    @Override
    public void tick(VehicleEntity vehicleEntity) {
        if (vehicleEntity.getDriverSeat().getPassenger() == null || !this.hasFuel(vehicleEntity)) {
            vehicleEntity.setDirectionYHeight(-1.0);
        }
        this.basicDirections(vehicleEntity, false, false, false, false);
    }

    @Override
    public void applyModifiers(VehicleEntity vehicleEntity, Vector vector) {
        super.applyModifiers(vehicleEntity, vector);
    }

    public double getDescentSpeed() {
        return this.descentSpeed;
    }

    public void setDescentSpeed(double d) {
        this.descentSpeed = d;
    }
}

