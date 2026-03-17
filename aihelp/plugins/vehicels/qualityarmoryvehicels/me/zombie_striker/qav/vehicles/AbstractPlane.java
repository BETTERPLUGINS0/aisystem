/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 */
package me.zombie_striker.qav.vehicles;

import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.events.VehicleTurnEvent;
import me.zombie_striker.qav.util.HeadPoseUtil;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class AbstractPlane
extends AbstractVehicle {
    public AbstractPlane(String string, int n) {
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
        vehicleEntity.setSpeed(Math.max(0.0, vehicleEntity.getSpeed() - 0.05));
    }

    @Override
    public void handleSpace(VehicleEntity vehicleEntity, Player player) {
    }

    @Override
    public void tick(VehicleEntity vehicleEntity) {
        this.basicDirections(vehicleEntity, this.canJump(), false, true, true);
    }
}

