/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.MetadataValue
 */
package nl.sbdeveloper.vehiclesplus.api;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.nbt.NBTDataType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.Vehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.fuel.FuelType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.StorageVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.Part;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.PartTypeName;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Seat;
import nl.sbdeveloper.vehiclesplus.api.vehicles.rims.RimDesign;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.VehicleType;
import nl.sbdeveloper.vehiclesplus.storage.db.DataStorage;
import nl.sbdeveloper.vehiclesplus.storage.db.exceptions.DataStorageException;
import nl.sbdeveloper.vehiclesplus.utils.jackson.JacksonHelper;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;

public class VehiclesPlusAPI {
    private static final Map<String, FuelType> fuelTypes = new HashMap<String, FuelType>();
    private static final Map<String, RimDesign> rimDesigns = new HashMap<String, RimDesign>();
    private static final Map<String, VehicleType> vehicleTypes = new HashMap<String, VehicleType>();
    private static final Map<String, VehicleModel> vehicleModels = new HashMap<String, VehicleModel>();
    private static final Map<UUID, Vehicle> vehicles = new HashMap<UUID, Vehicle>();
    private static final Map<String, Garage> garages = new HashMap<String, Garage>();
    private static final List<Runnable> hooks = new ArrayList<Runnable>();

    private VehiclesPlusAPI() {
    }

    @NotNull
    public static Optional<FuelType> getFuelType(@NotNull String string) {
        return Optional.ofNullable(fuelTypes.get(string));
    }

    @NotNull
    public static Optional<RimDesign> getRimDesign(@NotNull String string) {
        return Optional.ofNullable(rimDesigns.get(string));
    }

    @NotNull
    public static Optional<VehicleType> getVehicleType(@NotNull String string) {
        return Optional.ofNullable(vehicleTypes.get(string));
    }

    @NotNull
    public static Optional<VehicleModel> getVehicleModel(@NotNull String string) {
        return Optional.ofNullable(vehicleModels.get(string));
    }

    @Nullable
    public static StorageVehicle createVehicle(@NotNull String string) {
        return VehiclesPlusAPI.getVehicleModel(string).map(vehicleModel -> Objects.requireNonNull(VehiclesPlusAPI.createVehicle(vehicleModel, null, null))).orElse(null);
    }

    @Nullable
    public static StorageVehicle createVehicle(@NotNull String string, @NotNull Garage garage) {
        return VehiclesPlusAPI.getVehicleModel(string).map(vehicleModel -> Objects.requireNonNull(VehiclesPlusAPI.createVehicle(vehicleModel, garage, null))).orElse(null);
    }

    @Nullable
    public static StorageVehicle createVehicle(@NotNull String string, @NotNull Function<StorageVehicle, StorageVehicle> function) {
        return VehiclesPlusAPI.getVehicleModel(string).map(vehicleModel -> Objects.requireNonNull(VehiclesPlusAPI.createVehicle(vehicleModel, null, function))).orElse(null);
    }

    @Nullable
    public static StorageVehicle createVehicle(@NotNull String string, @NotNull Garage garage, @NotNull Function<StorageVehicle, StorageVehicle> function) {
        return VehiclesPlusAPI.getVehicleModel(string).map(vehicleModel -> Objects.requireNonNull(VehiclesPlusAPI.createVehicle(vehicleModel, garage, function))).orElse(null);
    }

    @NotNull
    public static StorageVehicle createVehicle(@NotNull VehicleModel vehicleModel) {
        return Objects.requireNonNull(VehiclesPlusAPI.createVehicle(vehicleModel, null, null));
    }

    @NotNull
    public static StorageVehicle createVehicle(@NotNull VehicleModel vehicleModel, @NotNull Garage garage) {
        return Objects.requireNonNull(VehiclesPlusAPI.createVehicle(vehicleModel, garage, null));
    }

    @Nullable
    public static StorageVehicle createVehicle(@NotNull VehicleModel vehicleModel, @NotNull Function<StorageVehicle, StorageVehicle> function) {
        return VehiclesPlusAPI.createVehicle(vehicleModel, null, function);
    }

    @Nullable
    public static StorageVehicle createVehicle(@NotNull VehicleModel vehicleModel, @Nullable Garage garage, @Nullable Function<StorageVehicle, StorageVehicle> function) {
        StorageVehicle storageVehicle = new StorageVehicle(UUID.randomUUID(), vehicleModel, garage == null);
        if (function != null) {
            storageVehicle = function.apply(storageVehicle);
        }
        if (storageVehicle == null) {
            return null;
        }
        vehicles.put(storageVehicle.getUuid(), storageVehicle);
        if (garage != null) {
            garage.addVehicle(storageVehicle.getUuid());
        }
        storageVehicle.save();
        if (garage != null) {
            garage.save();
        }
        return storageVehicle;
    }

    @Nullable
    public static Vehicle getVehicle(UUID uUID) {
        return vehicles.get(uUID);
    }

    public static boolean doesPlayerOwnVehicles(@NotNull OfflinePlayer offlinePlayer) {
        return VehiclesPlusAPI.getGarages(offlinePlayer).stream().anyMatch(garage -> garage.getVehicles().size() > 0);
    }

    @NotNull
    public static List<Vehicle> getVehicles(@NotNull OfflinePlayer offlinePlayer) {
        return VehiclesPlusAPI.getGarages(offlinePlayer).stream().flatMap(garage -> garage.getVehicles().stream().map(VehiclesPlusAPI::getVehicle)).collect(Collectors.toList());
    }

    @NotNull
    public static Optional<SpawnedVehicle> getVehicle(@NotNull Player player) {
        return vehicles.values().stream().filter(Vehicle::isSpawned).map(Vehicle::getSpawnedVehicle).filter(spawnedVehicle -> spawnedVehicle.getPart(Seat.class, Seat::isSteer).flatMap(Seat::getPassenger).stream().anyMatch(player2 -> player2.getUniqueId() == player.getUniqueId())).findFirst();
    }

    @NotNull
    public static Optional<SpawnedVehicle> getVehicleFromHolder(@NotNull ArmorStand armorStand) {
        return vehicles.values().stream().filter(vehicle -> vehicle.isSpawned() && vehicle.getSpawnedVehicle().getHolder().getUniqueId() == armorStand.getUniqueId()).map(Vehicle::getSpawnedVehicle).findFirst();
    }

    @NotNull
    public static Optional<SpawnedVehicle> getVehicleFromPart(@NotNull ArmorStand armorStand) {
        if (!armorStand.hasMetadata(NBTDataType.V_UUID.name())) {
            return Optional.empty();
        }
        List list = armorStand.getMetadata(NBTDataType.V_UUID.name());
        if (list.isEmpty()) {
            return Optional.empty();
        }
        MetadataValue metadataValue = (MetadataValue)list.get(0);
        if (metadataValue == null) {
            return Optional.empty();
        }
        Object object = metadataValue.value();
        if (!(object instanceof UUID)) {
            return Optional.empty();
        }
        return Optional.ofNullable(VehiclesPlusAPI.getVehicle((UUID)object).getSpawnedVehicle());
    }

    @NotNull
    public static List<StorageVehicle> getStorageVehicles() {
        return vehicles.values().stream().filter(Predicate.not(Vehicle::isSpawned)).map(Vehicle::getStorageVehicle).collect(Collectors.toList());
    }

    @NotNull
    public static List<SpawnedVehicle> getSpawnedVehicles() {
        return vehicles.values().stream().filter(Vehicle::isSpawned).map(Vehicle::getSpawnedVehicle).collect(Collectors.toList());
    }

    @NotNull
    public static List<StorageVehicle> getStorageVehicles(@NotNull OfflinePlayer offlinePlayer, boolean bl) {
        return VehiclesPlusAPI.getGarages(offlinePlayer).stream().flatMap(garage -> garage.getVehicles().stream().filter(uUID -> !VehiclesPlusAPI.getVehicle(uUID).isSpawned() || !bl)).map(uUID -> VehiclesPlusAPI.getVehicle(uUID).getStorageVehicle(bl)).collect(Collectors.toList());
    }

    @NotNull
    public static List<SpawnedVehicle> getSpawnedVehicles(@NotNull OfflinePlayer offlinePlayer) {
        return VehiclesPlusAPI.getGarages(offlinePlayer).stream().flatMap(garage -> garage.getVehicles().stream().filter(uUID -> VehiclesPlusAPI.getVehicle(uUID).isSpawned())).map(uUID -> VehiclesPlusAPI.getVehicle(uUID).getSpawnedVehicle()).collect(Collectors.toList());
    }

    public static void addGarage(@NotNull Garage garage, boolean bl) {
        if (bl) {
            garages.put(garage.getName(), garage);
        } else {
            garages.putIfAbsent(garage.getName(), garage);
        }
    }

    public static boolean removeGarage(@NotNull String string) {
        if (!garages.containsKey(string)) {
            return false;
        }
        try {
            DataStorage.getInstance().deleteGarage(garages.get(string));
        } catch (DataStorageException dataStorageException) {
            dataStorageException.printStackTrace();
            return false;
        }
        garages.remove(string);
        return true;
    }

    @NotNull
    public static Garage getPersonalGarage(@NotNull OfflinePlayer offlinePlayer) {
        Optional<Garage> optional = VehiclesPlusAPI.getGarages(offlinePlayer).stream().filter(Garage::isPersonal).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        Garage garage = new Garage(offlinePlayer.getName(), offlinePlayer.getUniqueId(), "&a" + offlinePlayer.getName() + " his garage", true);
        VehiclesPlusAPI.addGarage(garage, false);
        garage.save();
        return garage;
    }

    @NotNull
    public static List<Garage> getGarages(@NotNull OfflinePlayer offlinePlayer) {
        return VehiclesPlusAPI.getGarages(offlinePlayer, false);
    }

    @NotNull
    public static List<Garage> getGarages(@NotNull OfflinePlayer offlinePlayer, boolean bl) {
        return garages.values().stream().filter(garage -> {
            if (!bl) {
                return garage.getOwner().getUniqueId().equals(offlinePlayer.getUniqueId()) || garage.getMembers().stream().anyMatch(garageMember -> garageMember.getMember().equals(offlinePlayer.getUniqueId()));
            }
            return garage.getOwner().getUniqueId().equals(offlinePlayer.getUniqueId());
        }).collect(Collectors.toList());
    }

    @NotNull
    public static Optional<Garage> getGarage(@NotNull String string) {
        return garages.values().stream().filter(garage -> garage.getName().equalsIgnoreCase(string)).findFirst();
    }

    @NotNull
    public static Optional<Garage> getGarage(@NotNull Vehicle vehicle) {
        return garages.values().stream().filter(garage -> garage.getVehicles().contains(vehicle.getStorageVehicle().getUuid())).findAny();
    }

    public static void registerHook(Runnable runnable) {
        hooks.add(runnable);
    }

    public static void registerPart(@NotNull Class<? extends Part> clazz) {
        PartTypeName partTypeName = clazz.getAnnotation(PartTypeName.class);
        if (partTypeName == null) {
            throw new IllegalArgumentException("The part class " + clazz.getName() + " is not annotated with PartTypeName!");
        }
        JacksonHelper.getPartTypes().add(new NamedType(clazz, partTypeName.value()));
    }

    @Generated
    public static Map<String, FuelType> getFuelTypes() {
        return fuelTypes;
    }

    @Generated
    public static Map<String, RimDesign> getRimDesigns() {
        return rimDesigns;
    }

    @Generated
    public static Map<String, VehicleType> getVehicleTypes() {
        return vehicleTypes;
    }

    @Generated
    public static Map<String, VehicleModel> getVehicleModels() {
        return vehicleModels;
    }

    @Generated
    public static Map<UUID, Vehicle> getVehicles() {
        return vehicles;
    }

    @Generated
    public static Map<String, Garage> getGarages() {
        return garages;
    }

    @Generated
    public static List<Runnable> getHooks() {
        return hooks;
    }
}

