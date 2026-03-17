/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  lombok.NonNull
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.impl;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Generated;
import lombok.NonNull;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehiclePreSpawnEvent;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleSpawnedEvent;
import nl.sbdeveloper.vehiclesplus.api.exceptions.UnsupportedTrunkSizeException;
import nl.sbdeveloper.vehiclesplus.api.vehicles.Vehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.PersistentVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.Part;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.MovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.VehicleStatics;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.MovementType;
import nl.sbdeveloper.vehiclesplus.handlers.EconomyAdapter;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.storage.db.DataStorage;
import nl.sbdeveloper.vehiclesplus.storage.db.QueuedSavable;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.MainUtil;
import nl.sbdeveloper.vehiclesplus.utils.jackson.itemstackbyte.ItemStackByteJacksonSerializer;
import nl.sbdeveloper.vehiclesplus.utils.jackson.itemstackbyte.TrunkSlotsDeserializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StorageVehicle
extends Vehicle
implements QueuedSavable {
    @JsonProperty
    private final UUID uuid;
    @JsonProperty
    @NotNull
    private String displayName;
    @JsonProperty
    @NotNull
    private final String vehicleModel;
    @JsonProperty
    @NotNull
    private final List<Part> parts;
    @JsonSerialize(contentUsing=ItemStackByteJacksonSerializer.class)
    @JsonDeserialize(using=TrunkSlotsDeserializer.class)
    @JsonProperty
    private Map<Integer, ItemStack> trunkSlots = new HashMap<Integer, ItemStack>();
    @JsonProperty
    private final VehicleStatics statics;
    @JsonProperty
    private String actionBarMessage;
    @JsonProperty
    private final boolean persistent;
    @JsonProperty
    @Nullable
    private Location persistentLocation = null;

    @ApiStatus.Internal
    public StorageVehicle(@NotNull UUID uUID, @NotNull VehicleModel vehicleModel, boolean bl) {
        this.uuid = uUID;
        this.displayName = vehicleModel.getDisplayName();
        this.vehicleModel = vehicleModel.getId();
        this.actionBarMessage = Locale.getMessage(PluginMessage.GENERAL_VEHICLES_ACTIONBAR_NORMAL);
        this.persistent = bl;
        this.statics = new VehicleStatics(this.getVehicleModel());
        if (this.getVehicleModel().getTrunkSize() > 0) {
            if (this.getVehicleModel().getTrunkSize() % 9 != 0) {
                throw new UnsupportedTrunkSizeException("BaseVehicle " + this.getVehicleModel().getId() + " has an invalid trunk size.");
            }
            this.trunkSlots = new HashMap<Integer, ItemStack>(this.getVehicleModel().getTrunkSize());
        } else {
            this.trunkSlots = null;
        }
        this.parts = new ArrayList<Part>();
        for (Part part : vehicleModel.getParts()) {
            this.parts.add(part.clone());
        }
    }

    @JsonCreator
    private StorageVehicle(@JsonProperty(required=true, value="uuid") UUID uUID, @JsonProperty(required=true, value="displayName") @NotNull String string, @JsonProperty(required=true, value="vehicleModel") @NotNull String string2, @JsonProperty(required=true, value="parts") @NotNull List<Part> list, @JsonProperty(required=true, value="trunkSlots") @JsonAlias(value={"trunkItems"}) @JsonDeserialize(using=TrunkSlotsDeserializer.class) Map<Integer, ItemStack> map, @JsonProperty(required=true, value="statics") VehicleStatics vehicleStatics, @JsonProperty(required=true, value="actionBarMessage") String string3, @JsonProperty(required=true, value="persistent") boolean bl) {
        this.uuid = uUID;
        this.displayName = string;
        this.vehicleModel = string2;
        this.parts = list;
        this.trunkSlots = map;
        this.statics = vehicleStatics;
        this.actionBarMessage = string3;
        this.persistent = bl;
    }

    @JsonIgnore
    @NotNull
    public String getDisplayNameColored() {
        return ColorUtil.__(this.displayName);
    }

    @Override
    public void forceSave() {
        if (VehiclesPlusPluginManager.getConfig().getDataSettings().isVerbose()) {
            VehiclesPlusPluginManager.getVehiclesPlusPlugin().getLogger().info("Saving the vehicle " + String.valueOf(this.uuid) + " to data storage...");
        }
        DataStorage.getInstance().saveVehicle(this);
    }

    @Override
    public String getSaveIdentifier() {
        return this.uuid.toString();
    }

    @Override
    public String getSaveError() {
        return "An error occurred while saving the vehicle " + String.valueOf(this.uuid) + " to data storage.";
    }

    public PersistentVehicle spawnPersistent(Location location) {
        VehiclePreSpawnEvent vehiclePreSpawnEvent = new VehiclePreSpawnEvent(this);
        Bukkit.getPluginManager().callEvent((Event)vehiclePreSpawnEvent);
        if (vehiclePreSpawnEvent.isCancelled()) {
            return null;
        }
        this.setPersistentLocation(location);
        PersistentVehicle persistentVehicle = new PersistentVehicle(this, location);
        VehiclesPlusAPI.getVehicles().put(this.uuid, persistentVehicle);
        VehicleSpawnedEvent vehicleSpawnedEvent = new VehicleSpawnedEvent(persistentVehicle);
        Bukkit.getPluginManager().callEvent((Event)vehicleSpawnedEvent);
        return persistentVehicle;
    }

    public DrivableVehicle spawn(@NonNull Player player, boolean bl) {
        if (player == null) {
            throw new NullPointerException("spawner is marked non-null but is null");
        }
        VehiclePreSpawnEvent vehiclePreSpawnEvent = new VehiclePreSpawnEvent(this, player);
        Bukkit.getPluginManager().callEvent((Event)vehiclePreSpawnEvent);
        if (vehiclePreSpawnEvent.isCancelled() && !bl) {
            return null;
        }
        DrivableVehicle drivableVehicle = new DrivableVehicle(this, player.getLocation());
        VehiclesPlusAPI.getVehicles().put(this.uuid, drivableVehicle);
        VehicleSpawnedEvent vehicleSpawnedEvent = new VehicleSpawnedEvent(drivableVehicle, player);
        Bukkit.getPluginManager().callEvent((Event)vehicleSpawnedEvent);
        return drivableVehicle;
    }

    public DrivableVehicle spawn(@NonNull Location location, boolean bl) {
        if (location == null) {
            throw new NullPointerException("loc is marked non-null but is null");
        }
        VehiclePreSpawnEvent vehiclePreSpawnEvent = new VehiclePreSpawnEvent(this);
        Bukkit.getPluginManager().callEvent((Event)vehiclePreSpawnEvent);
        if (vehiclePreSpawnEvent.isCancelled() && !bl) {
            return null;
        }
        DrivableVehicle drivableVehicle = new DrivableVehicle(this, location);
        VehiclesPlusAPI.getVehicles().put(this.uuid, drivableVehicle);
        VehicleSpawnedEvent vehicleSpawnedEvent = new VehicleSpawnedEvent(drivableVehicle);
        Bukkit.getPluginManager().callEvent((Event)vehicleSpawnedEvent);
        return drivableVehicle;
    }

    @Override
    public VehicleModel getVehicleModel() {
        return VehiclesPlusAPI.getVehicleModel(this.vehicleModel).orElseThrow(() -> new IllegalArgumentException("VehicleModel " + this.vehicleModel + " not found!"));
    }

    @Override
    public <V> V getPart(@NotNull Class<V> clazz) {
        return this.parts.stream().filter(part -> clazz.isAssignableFrom(part.getClass())).map(clazz::cast).findFirst().orElse(null);
    }

    @Override
    @NotNull
    public <V> List<V> getParts(@NotNull Class<V> clazz) {
        return this.parts.stream().filter(part -> clazz.isAssignableFrom(part.getClass())).map(clazz::cast).collect(Collectors.toList());
    }

    @Override
    @Nullable
    public Part getPart(ArmorStand armorStand) {
        return this.parts.stream().filter(part -> part.getHolder().getUniqueId().equals(armorStand.getUniqueId())).findFirst().orElse(null);
    }

    @Override
    public boolean hasAddedParts() {
        return this.parts.stream().anyMatch(Part::isAddon);
    }

    @Override
    public List<String> getInfoList() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(ColorUtil.__("&cMax speed: &a" + this.statics.getMaxSpeed()));
        arrayList.add(ColorUtil.__("&cAcceleration: &a" + this.statics.getAcceleration()));
        arrayList.add(ColorUtil.__("&cTank Capacity: &a" + MainUtil.formatDouble(this.statics.getCurrentFuel(), 2) + "/" + this.statics.getFuelTank()));
        arrayList.add(ColorUtil.__("&cTurning Radius: &a" + this.statics.getTurningRadius()));
        if (!this.statics.isBroken()) {
            arrayList.add(ColorUtil.__("&cHealth: &a" + this.statics.getCurrentHealth()));
        }
        arrayList.add("");
        arrayList.add(ColorUtil.__("&a&lLeft click &ato spawn"));
        arrayList.add(ColorUtil.__("&c&lRight click &cto delete"));
        arrayList.add("");
        if (this.statics.isBroken()) {
            arrayList.add(ColorUtil.__("&aVehicle is broken!"));
            if (EconomyAdapter.isLoaded()) {
                double d = VehiclesPlusPluginManager.getConfig().getRepairCostDivision();
                double d2 = this.getVehicleModel().getPrice() / d;
                arrayList.add(ColorUtil.__("&cRepair cost: &a" + MainUtil.formatDouble(d2, 2)));
            }
        }
        return arrayList;
    }

    @Override
    public <T extends MovementStrategy, R> R getFromStrategy(MovementType movementType, Function<T, R> function, @NotNull R r) {
        return this.getVehicleModel().getFromStrategy(movementType, function, r);
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof StorageVehicle)) {
            return false;
        }
        StorageVehicle storageVehicle = (StorageVehicle)object;
        if (!storageVehicle.canEqual(this)) {
            return false;
        }
        if (this.isPersistent() != storageVehicle.isPersistent()) {
            return false;
        }
        UUID uUID = this.getUuid();
        UUID uUID2 = storageVehicle.getUuid();
        if (uUID == null ? uUID2 != null : !((Object)uUID).equals(uUID2)) {
            return false;
        }
        String string = this.getDisplayName();
        String string2 = storageVehicle.getDisplayName();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        VehicleModel vehicleModel = this.getVehicleModel();
        VehicleModel vehicleModel2 = storageVehicle.getVehicleModel();
        if (vehicleModel == null ? vehicleModel2 != null : !vehicleModel.equals(vehicleModel2)) {
            return false;
        }
        List<Part> list = this.getParts();
        List<Part> list2 = storageVehicle.getParts();
        if (list == null ? list2 != null : !((Object)list).equals(list2)) {
            return false;
        }
        Map<Integer, ItemStack> map = this.getTrunkSlots();
        Map<Integer, ItemStack> map2 = storageVehicle.getTrunkSlots();
        if (map == null ? map2 != null : !((Object)map).equals(map2)) {
            return false;
        }
        VehicleStatics vehicleStatics = this.getStatics();
        VehicleStatics vehicleStatics2 = storageVehicle.getStatics();
        if (vehicleStatics == null ? vehicleStatics2 != null : !vehicleStatics.equals(vehicleStatics2)) {
            return false;
        }
        String string3 = this.getActionBarMessage();
        String string4 = storageVehicle.getActionBarMessage();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        Location location = this.getPersistentLocation();
        Location location2 = storageVehicle.getPersistentLocation();
        return !(location == null ? location2 != null : !location.equals(location2));
    }

    @Generated
    protected boolean canEqual(Object object) {
        return object instanceof StorageVehicle;
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        n2 = n2 * 59 + (this.isPersistent() ? 79 : 97);
        UUID uUID = this.getUuid();
        n2 = n2 * 59 + (uUID == null ? 43 : ((Object)uUID).hashCode());
        String string = this.getDisplayName();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        VehicleModel vehicleModel = this.getVehicleModel();
        n2 = n2 * 59 + (vehicleModel == null ? 43 : vehicleModel.hashCode());
        List<Part> list = this.getParts();
        n2 = n2 * 59 + (list == null ? 43 : ((Object)list).hashCode());
        Map<Integer, ItemStack> map = this.getTrunkSlots();
        n2 = n2 * 59 + (map == null ? 43 : ((Object)map).hashCode());
        VehicleStatics vehicleStatics = this.getStatics();
        n2 = n2 * 59 + (vehicleStatics == null ? 43 : vehicleStatics.hashCode());
        String string2 = this.getActionBarMessage();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        Location location = this.getPersistentLocation();
        n2 = n2 * 59 + (location == null ? 43 : location.hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "StorageVehicle(uuid=" + String.valueOf(this.uuid) + ", displayName=" + this.displayName + ", vehicleModel=" + this.vehicleModel + ", parts=" + String.valueOf(this.parts) + ", trunkSlots=" + String.valueOf(this.trunkSlots) + ", statics=" + String.valueOf(this.statics) + ", actionBarMessage=" + this.actionBarMessage + ", persistent=" + this.persistent + ", persistentLocation=" + String.valueOf(this.persistentLocation) + ")";
    }

    @Override
    @Generated
    public UUID getUuid() {
        return this.uuid;
    }

    @NotNull
    @Generated
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    @NotNull
    @Generated
    public List<Part> getParts() {
        return this.parts;
    }

    @Generated
    public Map<Integer, ItemStack> getTrunkSlots() {
        return this.trunkSlots;
    }

    @Override
    @Generated
    public VehicleStatics getStatics() {
        return this.statics;
    }

    @Generated
    public String getActionBarMessage() {
        return this.actionBarMessage;
    }

    @JsonProperty
    @Generated
    public void setDisplayName(@NotNull String string) {
        if (string == null) {
            throw new NullPointerException("displayName is marked non-null but is null");
        }
        this.displayName = string;
    }

    @JsonDeserialize(using=TrunkSlotsDeserializer.class)
    @JsonProperty
    @Generated
    public void setTrunkSlots(Map<Integer, ItemStack> map) {
        this.trunkSlots = map;
    }

    @JsonProperty
    @Generated
    public void setActionBarMessage(String string) {
        this.actionBarMessage = string;
    }

    @Generated
    public boolean isPersistent() {
        return this.persistent;
    }

    @Nullable
    @Generated
    public Location getPersistentLocation() {
        return this.persistentLocation;
    }

    @JsonProperty
    @Generated
    public void setPersistentLocation(@Nullable Location location) {
        this.persistentLocation = location;
    }
}

