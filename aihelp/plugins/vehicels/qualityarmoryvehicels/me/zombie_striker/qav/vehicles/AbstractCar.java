/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.zombie_striker.qg.guns.utils.WeaponSounds
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 */
package me.zombie_striker.qav.vehicles;

import java.util.HashMap;
import java.util.UUID;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.api.events.VehicleChangeSpeedEvent;
import me.zombie_striker.qav.api.events.VehicleTurnEvent;
import me.zombie_striker.qav.util.HeadPoseUtil;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import me.zombie_striker.qg.guns.utils.WeaponSounds;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class AbstractCar
extends AbstractVehicle {
    private HashMap<UUID, Long> lastSoundBreak = new HashMap();
    private HashMap<UUID, Long> lastSoundDrive = new HashMap();

    public AbstractCar(String string, int n) {
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
        VehicleChangeSpeedEvent vehicleChangeSpeedEvent = new VehicleChangeSpeedEvent(vehicleEntity, vehicleEntity.getSpeed(), Math.min(vehicleEntity.getSpeed() + 0.1, vehicleEntity.getType().getMaxSpeed()));
        Bukkit.getPluginManager().callEvent((Event)vehicleChangeSpeedEvent);
        if (vehicleChangeSpeedEvent.isCanceled()) {
            return;
        }
        if (!this.handleFuel(vehicleEntity, player)) {
            return;
        }
        vehicleEntity.setSpeed(Math.min(vehicleEntity.getSpeed() + 0.1, vehicleEntity.getType().getMaxSpeed()));
        if (!this.lastSoundDrive.containsKey(player.getUniqueId()) || System.currentTimeMillis() - this.lastSoundDrive.get(player.getUniqueId()) > 900L) {
            this.lastSoundDrive.put(player.getUniqueId(), System.currentTimeMillis());
            vehicleEntity.getDriverSeat().getLocation().getWorld().playSound(vehicleEntity.getDriverSeat().getLocation(), this.getSound(), (float)this.getSoundVolume(), 1.0f);
        }
    }

    @Override
    public void handleSpeedDecrease(VehicleEntity vehicleEntity, Player player) {
        VehicleChangeSpeedEvent vehicleChangeSpeedEvent = new VehicleChangeSpeedEvent(vehicleEntity, vehicleEntity.getSpeed(), Math.max(vehicleEntity.getSpeed() - 0.1, -vehicleEntity.getType().getMaxSpeed()));
        Bukkit.getPluginManager().callEvent((Event)vehicleChangeSpeedEvent);
        if (vehicleChangeSpeedEvent.isCanceled()) {
            return;
        }
        if (!this.handleFuel(vehicleEntity, player)) {
            return;
        }
        vehicleEntity.setSpeed(Math.max(vehicleEntity.getSpeed() - 0.1, -vehicleEntity.getType().getMaxBackupSpeed()));
    }

    @Override
    public void handleSpace(VehicleEntity vehicleEntity, Player player) {
        if (vehicleEntity.getSpeed() > 0.0) {
            vehicleEntity.setSpeed(Math.max(vehicleEntity.getSpeed() - 0.1, -vehicleEntity.getType().getMaxBackupSpeed()));
        } else {
            vehicleEntity.setSpeed(Math.max(vehicleEntity.getSpeed() + 0.1, -vehicleEntity.getType().getMaxSpeed()));
        }
        if (this.canPlaySkidSounds() && (vehicleEntity.getSpeed() > 0.2 || vehicleEntity.getSpeed() < -0.2) && (!this.lastSoundBreak.containsKey(player.getUniqueId()) || System.currentTimeMillis() - this.lastSoundBreak.get(player.getUniqueId()) > 2000L)) {
            this.lastSoundBreak.put(player.getUniqueId(), System.currentTimeMillis());
            try {
                player.getWorld().playSound(player.getLocation(), WeaponSounds.CARSKID.getSoundName(), (float)vehicleEntity.getType().getSoundVolume(), 1.3f);
            } catch (Error | Exception throwable) {
                player.getWorld().playSound(player.getLocation(), "carskid", 0.7f, 2.3f);
            }
        }
    }

    @Override
    public void tick(VehicleEntity vehicleEntity) {
        if (vehicleEntity.getDriverSeat() == null) {
            return;
        }
        this.basicDirections(vehicleEntity, this.canJump(), false);
        if (Main.destroyOnWater && vehicleEntity.isSubmerged()) {
            vehicleEntity.deconstruct(null, "UnderWater");
            if (Main.enableGarage) {
                QualityArmoryVehicles.removeUnlockedVehicle((OfflinePlayer)Bukkit.getPlayer((UUID)vehicleEntity.getOwner()), vehicleEntity.getType());
            }
        }
    }
}

