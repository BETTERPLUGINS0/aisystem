/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.EulerAngle
 *  org.bukkit.util.Vector
 *  org.joml.Math
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleLockStateChangeEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.StorageVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.EquipablePart;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.Part;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Rotor;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Skin;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.MovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.strategies.AirMovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.MovementType;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.storage.db.exceptions.DataStorageException;
import nl.sbdeveloper.vehiclesplus.utils.LocationUtil;
import nl.sbdeveloper.vehiclesplus.utils.inventory.TrunkMapper;
import nl.sbdeveloper.vehiclesplus.utils.nms.MovementUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;

public class DrivableVehicle
extends SpawnedVehicle {
    private final List<Vector> momentum = new ArrayList<Vector>();
    private boolean locked;
    private boolean configurator;
    private final Inventory trunk;
    private final int updateTaskID;

    DrivableVehicle(StorageVehicle storageVehicle, Location location) {
        super(storageVehicle, location, false);
        if (this.getVehicleModel().getTrunkSize() > 0) {
            this.trunk = Bukkit.createInventory(null, (int)this.getVehicleModel().getTrunkSize(), (String)Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_TRUNK_TITLE));
            this.loadTrunkFromStorageSparse();
        } else {
            this.trunk = null;
        }
        this.updateTaskID = Bukkit.getScheduler().runTaskTimer((Plugin)VehiclesPlusPluginManager.getVehiclesPlusPlugin(), this::update, 0L, 1L).getTaskId();
        this.locked = VehiclesPlusPluginManager.getConfig().isSpawnLocked();
    }

    public boolean hasTrunk() {
        return this.trunk != null;
    }

    public void openTrunk(Player player) {
        if (this.trunk != null) {
            player.openInventory(this.trunk);
        }
    }

    @Override
    public void update() {
        EulerAngle eulerAngle;
        Location location;
        super.update();
        if (this.getVehicleModel().getType().hasMovementType(MovementType.WATER) && (location = this.holder.getLocation().clone()).add(0.0, 0.9, 0.0).getBlock().getType().name().contains("WATER")) {
            this.holder.teleport(location.subtract(0.0, 0.8, 0.0));
        }
        if (this.getVehicleModel().getType().hasMovementType(MovementType.AIR) && this.getStatics().getCurrentFuel() <= 0.0 && this.holder.getVelocity().lengthSquared() < 0.01) {
            int n = this.getFromStrategy(MovementType.AIR, AirMovementStrategy::getLift, 0);
            int n2 = n = Math.max((int)0, (int)(n - 2));
            this.applyToStrategy(MovementType.AIR, movementStrategy -> ((AirMovementStrategy)movementStrategy).setLift(n2));
            if (!this.getVehicleModel().getType().hasMovementType(MovementType.LAND)) {
                this.holder.setVelocity(new Vector(0.0, -1.0 + (double)n / 20.0, 0.0));
            } else {
                this.holder.setVelocity(new Vector(0.0, -0.5 + (double)n / 20.0, 0.0));
            }
        }
        location = this.holder.getLocation();
        double d = Math.toRadians((double)location.getYaw());
        Vector vector = location.getDirection().setY(0).normalize();
        double d2 = Math.toRadians((double)location.getPitch());
        Iterator<Part> iterator = this.getParts(Skin.class).iterator();
        if (iterator.hasNext()) {
            Part part = iterator.next();
            switch (((EquipablePart)part).getPosition()) {
                case HEAD: {
                    eulerAngle = part.getHolder().getHeadPose();
                    break;
                }
                case MAIN_HAND: {
                    eulerAngle = part.getHolder().getRightArmPose();
                    break;
                }
                case OFF_HAND: {
                    eulerAngle = part.getHolder().getLeftArmPose();
                    break;
                }
                default: {
                    throw new IncompatibleClassChangeError();
                }
            }
            EulerAngle eulerAngle2 = eulerAngle;
            d2 = eulerAngle2.getX();
        }
        for (Part part : this.storageVehicle.getParts()) {
            double d3;
            if (!part.isSpawned()) continue;
            eulerAngle = LocationUtil.calculateOffset(location, part.getXOffset(), part.getYOffset(), part.getZOffset());
            eulerAngle = part.applyExtraOffset((Location)eulerAngle);
            if (part instanceof Rotor && (d3 = Math.abs((double)part.getYOffset())) > 1.0E-6) {
                double d4 = d3 * Math.sin((double)d2);
                double d5 = d3 * (1.0 - Math.cos((double)d2));
                Vector vector2 = vector.clone().multiply(d4);
                eulerAngle.add(vector2);
                eulerAngle.add(0.0, -d5, 0.0);
            }
            MovementUtil.setPosition(part.getHolder(), (Location)eulerAngle);
        }
    }

    @Override
    protected StorageVehicle despawnVehicle() {
        Bukkit.getScheduler().cancelTask(this.updateTaskID);
        this.saveTrunkToStorageSparse(true);
        this.momentum.clear();
        this.locked = VehiclesPlusPluginManager.getConfig().isSpawnLocked();
        this.configurator = false;
        return super.despawnVehicle();
    }

    public boolean setLocked(boolean bl) {
        return this.setLocked(bl, null);
    }

    public boolean setLocked(boolean bl, Player player) {
        VehicleLockStateChangeEvent vehicleLockStateChangeEvent = new VehicleLockStateChangeEvent(this, bl, player);
        Bukkit.getPluginManager().callEvent((Event)vehicleLockStateChangeEvent);
        if (vehicleLockStateChangeEvent.isCancelled()) {
            return false;
        }
        this.locked = vehicleLockStateChangeEvent.isLocked();
        return bl == this.locked;
    }

    private void loadTrunkFromStorageSparse() {
        if (this.trunk != null && this.getStorageVehicle().getTrunkSlots() != null && !this.getStorageVehicle().getTrunkSlots().isEmpty()) {
            TrunkMapper.applySparse(this.trunk, this.getStorageVehicle().getTrunkSlots());
        }
    }

    public void saveTrunkToStorageSparse(boolean bl) {
        if (this.trunk != null) {
            Map<Integer, ItemStack> map = TrunkMapper.toSparse(this.trunk);
            this.getStorageVehicle().setTrunkSlots(map);
            if (bl) {
                try {
                    this.getStorageVehicle().forceSave();
                } catch (DataStorageException dataStorageException) {
                    dataStorageException.printStackTrace();
                }
            }
        }
    }

    @Override
    public <T extends MovementStrategy, R> R getFromStrategy(MovementType movementType, Function<T, R> function, @NotNull R r) {
        return this.getVehicleModel().getFromStrategy(movementType, function, r);
    }

    @Override
    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof DrivableVehicle)) {
            return false;
        }
        DrivableVehicle drivableVehicle = (DrivableVehicle)object;
        if (!drivableVehicle.canEqual(this)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        if (this.isLocked() != drivableVehicle.isLocked()) {
            return false;
        }
        if (this.isConfigurator() != drivableVehicle.isConfigurator()) {
            return false;
        }
        if (this.updateTaskID != drivableVehicle.updateTaskID) {
            return false;
        }
        List<Vector> list = this.getMomentum();
        List<Vector> list2 = drivableVehicle.getMomentum();
        if (list == null ? list2 != null : !((Object)list).equals(list2)) {
            return false;
        }
        Inventory inventory = this.getTrunk();
        Inventory inventory2 = drivableVehicle.getTrunk();
        return !(inventory == null ? inventory2 != null : !inventory.equals(inventory2));
    }

    @Override
    @Generated
    protected boolean canEqual(Object object) {
        return object instanceof DrivableVehicle;
    }

    @Override
    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = super.hashCode();
        n2 = n2 * 59 + (this.isLocked() ? 79 : 97);
        n2 = n2 * 59 + (this.isConfigurator() ? 79 : 97);
        n2 = n2 * 59 + this.updateTaskID;
        List<Vector> list = this.getMomentum();
        n2 = n2 * 59 + (list == null ? 43 : ((Object)list).hashCode());
        Inventory inventory = this.getTrunk();
        n2 = n2 * 59 + (inventory == null ? 43 : inventory.hashCode());
        return n2;
    }

    @Override
    @Generated
    public String toString() {
        return "DrivableVehicle(momentum=" + String.valueOf(this.momentum) + ", locked=" + this.locked + ", configurator=" + this.configurator + ", trunk=" + String.valueOf(this.trunk) + ", updateTaskID=" + this.updateTaskID + ")";
    }

    @Generated
    public List<Vector> getMomentum() {
        return this.momentum;
    }

    @Generated
    public boolean isLocked() {
        return this.locked;
    }

    @Generated
    public boolean isConfigurator() {
        return this.configurator;
    }

    @Generated
    public void setConfigurator(boolean bl) {
        this.configurator = bl;
    }

    @Generated
    public Inventory getTrunk() {
        return this.trunk;
    }
}

