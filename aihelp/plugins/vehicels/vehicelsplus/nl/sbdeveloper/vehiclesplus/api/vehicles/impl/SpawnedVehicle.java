/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleDespawnEvent;
import nl.sbdeveloper.vehiclesplus.api.stands.ArmorStandBuilder;
import nl.sbdeveloper.vehiclesplus.api.stands.ArmorStandName;
import nl.sbdeveloper.vehiclesplus.api.vehicles.Vehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.CollisionHandler;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.PersistentVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.StorageVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.Part;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.MovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.StrategyFactory;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.VehicleStatics;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.MovementType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SpawnedVehicle
extends Vehicle {
    protected final StorageVehicle storageVehicle;
    protected ArmorStand holder;
    private Location lastKnownLocation;
    private final CollisionHandler collisionHandler;

    SpawnedVehicle(StorageVehicle storageVehicle, Location location, boolean bl) {
        this.storageVehicle = storageVehicle;
        ArmorStandBuilder armorStandBuilder = new ArmorStandBuilder(location).setVisible(false).setInvulnerable(true).setCustomName(ArmorStandName.VP_HOLDER);
        if (bl) {
            armorStandBuilder = armorStandBuilder.setPersistent(true).setRemoveWhenFarAway(false);
        }
        this.holder = armorStandBuilder.getArmorStand();
        this.storageVehicle.getParts().forEach(part -> part.spawnStand(location, this, bl));
        this.collisionHandler = new CollisionHandler(this);
        if (this.getVehicleModel().getType().hasMovementType(MovementType.WATER)) {
            while (this.holder.getLocation().clone().add(0.0, 0.9, 0.0).getBlock().getType().name().contains("WATER")) {
                this.holder.teleport(this.holder.getLocation().clone().add(0.0, 0.1, 0.0));
            }
        }
    }

    @JsonIgnore
    public boolean isPersistent() {
        return this instanceof PersistentVehicle;
    }

    @JsonIgnore
    public boolean isDrivable() {
        return this instanceof DrivableVehicle;
    }

    @JsonIgnore
    @Nullable
    public DrivableVehicle getAsDrivableVehicle() {
        if (!this.isDrivable()) {
            return null;
        }
        return (DrivableVehicle)this;
    }

    @JsonIgnore
    @Nullable
    public PersistentVehicle getAsPersistentVehicle() {
        if (!this.isPersistent()) {
            return null;
        }
        return (PersistentVehicle)this;
    }

    public StorageVehicle despawn(VehicleDespawnEvent.DespawnReason despawnReason) {
        return this.despawn(despawnReason, false);
    }

    public StorageVehicle despawn(VehicleDespawnEvent.DespawnReason despawnReason, boolean bl) {
        VehicleDespawnEvent vehicleDespawnEvent = new VehicleDespawnEvent(this, despawnReason, bl);
        Bukkit.getPluginManager().callEvent((Event)vehicleDespawnEvent);
        if (vehicleDespawnEvent.isCancelled() && !bl) {
            return null;
        }
        return this.despawnVehicle();
    }

    public StorageVehicle despawn(Player player) {
        return this.despawn(false, player);
    }

    public StorageVehicle despawn(boolean bl, Player player) {
        VehicleDespawnEvent vehicleDespawnEvent = new VehicleDespawnEvent(this, bl, player);
        Bukkit.getPluginManager().callEvent((Event)vehicleDespawnEvent);
        if (vehicleDespawnEvent.isCancelled() && !bl) {
            return null;
        }
        return this.despawnVehicle();
    }

    protected StorageVehicle despawnVehicle() {
        if (this.holder != null) {
            this.lastKnownLocation = this.holder.getLocation().clone();
            this.holder.getLocation().getChunk().load();
            this.holder.remove();
        }
        this.storageVehicle.getParts().forEach(Part::despawnStand);
        this.storageVehicle.getStatics().resetMovingValues();
        VehiclesPlusAPI.getVehicles().put(this.storageVehicle.getUuid(), this.storageVehicle);
        return this.storageVehicle;
    }

    @Override
    @NotNull
    public List<Part> getParts() {
        return this.storageVehicle.getParts();
    }

    @Override
    @NotNull
    public <V> List<V> getParts(Class<V> clazz) {
        return this.storageVehicle.getParts(clazz);
    }

    @Override
    public <V> V getPart(@NotNull Class<V> clazz) {
        return this.storageVehicle.getPart(clazz);
    }

    public <V> List<V> getParts(Class<V> clazz, Predicate<? super V> predicate) {
        return this.storageVehicle.getParts(clazz).stream().filter(predicate).collect(Collectors.toList());
    }

    public <V> Optional<V> getPart(Class<V> clazz, Predicate<? super V> predicate) {
        return this.storageVehicle.getParts(clazz).stream().filter(predicate).findFirst();
    }

    @Override
    public Part getPart(ArmorStand armorStand) {
        return this.storageVehicle.getPart(armorStand);
    }

    @Override
    public boolean hasAddedParts() {
        return this.storageVehicle.hasAddedParts();
    }

    @Override
    public VehicleStatics getStatics() {
        return this.storageVehicle.getStatics();
    }

    @Override
    public UUID getUuid() {
        return this.storageVehicle.getUuid();
    }

    @Override
    public VehicleModel getVehicleModel() {
        return this.storageVehicle.getVehicleModel();
    }

    @Override
    public List<String> getInfoList() {
        return this.storageVehicle.getInfoList();
    }

    public void update() {
        this.collisionHandler.checkCollision();
    }

    @Override
    public <T extends MovementStrategy, R> R getFromStrategy(MovementType movementType, Function<T, R> function, @NotNull R r) {
        return this.storageVehicle.getFromStrategy(movementType, function, r);
    }

    public <T extends MovementStrategy> void applyToStrategy(MovementType movementType, Consumer<T> consumer) {
        Class clazz = StrategyFactory.getStrategyClass(movementType);
        for (MovementStrategy movementStrategy : this.storageVehicle.getVehicleModel().getTypeStrategies()) {
            if (!clazz.isInstance(movementStrategy)) continue;
            consumer.accept((MovementStrategy)clazz.cast(movementStrategy));
        }
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof SpawnedVehicle)) {
            return false;
        }
        SpawnedVehicle spawnedVehicle = (SpawnedVehicle)object;
        if (!spawnedVehicle.canEqual(this)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        StorageVehicle storageVehicle = this.getStorageVehicle();
        StorageVehicle storageVehicle2 = spawnedVehicle.getStorageVehicle();
        if (storageVehicle == null ? storageVehicle2 != null : !((Object)storageVehicle).equals(storageVehicle2)) {
            return false;
        }
        ArmorStand armorStand = this.getHolder();
        ArmorStand armorStand2 = spawnedVehicle.getHolder();
        if (armorStand == null ? armorStand2 != null : !armorStand.equals(armorStand2)) {
            return false;
        }
        Location location = this.getLastKnownLocation();
        Location location2 = spawnedVehicle.getLastKnownLocation();
        if (location == null ? location2 != null : !location.equals(location2)) {
            return false;
        }
        CollisionHandler collisionHandler = this.getCollisionHandler();
        CollisionHandler collisionHandler2 = spawnedVehicle.getCollisionHandler();
        return !(collisionHandler == null ? collisionHandler2 != null : !collisionHandler.equals(collisionHandler2));
    }

    @Generated
    protected boolean canEqual(Object object) {
        return object instanceof SpawnedVehicle;
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = super.hashCode();
        StorageVehicle storageVehicle = this.getStorageVehicle();
        n2 = n2 * 59 + (storageVehicle == null ? 43 : ((Object)storageVehicle).hashCode());
        ArmorStand armorStand = this.getHolder();
        n2 = n2 * 59 + (armorStand == null ? 43 : armorStand.hashCode());
        Location location = this.getLastKnownLocation();
        n2 = n2 * 59 + (location == null ? 43 : location.hashCode());
        CollisionHandler collisionHandler = this.getCollisionHandler();
        n2 = n2 * 59 + (collisionHandler == null ? 43 : collisionHandler.hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "SpawnedVehicle(storageVehicle=" + String.valueOf(this.storageVehicle) + ", holder=" + String.valueOf(this.holder) + ", lastKnownLocation=" + String.valueOf(this.lastKnownLocation) + ", collisionHandler=" + String.valueOf(this.collisionHandler) + ")";
    }

    @Override
    @Generated
    public StorageVehicle getStorageVehicle() {
        return this.storageVehicle;
    }

    @Generated
    public ArmorStand getHolder() {
        return this.holder;
    }

    @Generated
    public void setHolder(ArmorStand armorStand) {
        this.holder = armorStand;
    }

    @Generated
    public Location getLastKnownLocation() {
        return this.lastKnownLocation;
    }

    @Generated
    public CollisionHandler getCollisionHandler() {
        return this.collisionHandler;
    }
}

