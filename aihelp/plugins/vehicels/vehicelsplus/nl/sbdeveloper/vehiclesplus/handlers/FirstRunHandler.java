/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.handlers;

import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.vehicles.HolderItemPosition;
import nl.sbdeveloper.vehiclesplus.api.vehicles.defaults.impl.DefaultBike;
import nl.sbdeveloper.vehiclesplus.api.vehicles.defaults.impl.DefaultBoat;
import nl.sbdeveloper.vehiclesplus.api.vehicles.defaults.impl.DefaultCar;
import nl.sbdeveloper.vehiclesplus.api.vehicles.defaults.impl.DefaultHelicopter;
import nl.sbdeveloper.vehiclesplus.api.vehicles.defaults.impl.DefaultHovercraft;
import nl.sbdeveloper.vehiclesplus.api.vehicles.defaults.impl.DefaultPlane;
import nl.sbdeveloper.vehiclesplus.api.vehicles.defaults.impl.DefaultTank;
import nl.sbdeveloper.vehiclesplus.api.vehicles.fuel.FuelType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.rims.RimDesign;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.FrictionType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.MovementType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.TiltType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.VehicleType;
import nl.sbdeveloper.vehiclesplus.handlers.StorageHandler;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;

public class FirstRunHandler {
    public static void run() {
        if (VehiclesPlus.getStorage().getConfig().getVersion() >= 2) {
            return;
        }
        VehiclesPlus.getInstance().getLogger().info("First run detected! Loading defaults...");
        FirstRunHandler.saveRimDesign();
        FirstRunHandler.saveFuelType();
        FirstRunHandler.saveVehicleTypes();
        VehiclesPlus.getStorage().setVersion(2);
    }

    private static void saveVehicleTypes() {
        StorageHandler.saveTypeWithDefaultModel(VehicleType.builder().name("helicopters").movementType(MovementType.AIR).frictionType(FrictionType.LOW_FRICTION).tiltType(TiltType.FORWARD_BACKWARD).defaultModel(DefaultHelicopter.class).build());
        StorageHandler.saveTypeWithDefaultModel(VehicleType.builder().name("hovercrafts").movementType(MovementType.WATER).movementType(MovementType.LAND).frictionType(FrictionType.LOW_FRICTION).defaultModel(DefaultHovercraft.class).build());
        StorageHandler.saveTypeWithDefaultModel(VehicleType.builder().name("planes").movementType(MovementType.LAND).movementType(MovementType.AIR).frictionType(FrictionType.HIGH_FRICTION).tiltType(TiltType.STEERING).tiltType(TiltType.ASCEND_DESCENT).defaultModel(DefaultPlane.class).build());
        StorageHandler.saveTypeWithDefaultModel(VehicleType.builder().name("boats").movementType(MovementType.WATER).frictionType(FrictionType.LOW_FRICTION).tiltType(TiltType.STEERING).defaultModel(DefaultBoat.class).build());
        StorageHandler.saveTypeWithDefaultModel(VehicleType.builder().name("bikes").movementType(MovementType.LAND).frictionType(FrictionType.HIGH_FRICTION).tiltType(TiltType.STEERING).defaultModel(DefaultBike.class).build());
        StorageHandler.saveTypeWithDefaultModel(VehicleType.builder().name("tanks").movementType(MovementType.LAND).frictionType(FrictionType.HIGH_FRICTION).defaultModel(DefaultTank.class).build());
        StorageHandler.saveTypeWithDefaultModel(VehicleType.builder().name("cars").movementType(MovementType.LAND).frictionType(FrictionType.HIGH_FRICTION).defaultModel(DefaultCar.class).build());
    }

    private static void saveRimDesign() {
        RimDesign rimDesign = new RimDesign(VehiclesPlusPluginManager.getConfig().getDefaultRimDesignId(), new ItemBuilder(XMaterial.LEATHER_CHESTPLATE.parseItem()).customModelData(1, itemBuilder -> itemBuilder.durability(1).unbreakable()).getItemStack(), HolderItemPosition.HEAD, 1000.0f);
        StorageHandler.save(rimDesign, "rims", rimDesign.getName());
        if (VehiclesPlusAPI.getRimDesign(rimDesign.getName()).isEmpty()) {
            VehiclesPlusAPI.getRimDesigns().put(rimDesign.getName(), rimDesign);
        }
    }

    private static void saveFuelType() {
        FuelType fuelType = new FuelType("gasoline", new ItemBuilder(XMaterial.LEATHER_HELMET.parseItem()).customModelData(1, itemBuilder -> itemBuilder.durability(1).unbreakable()).getItemStack(), 1.5);
        StorageHandler.save(fuelType, "fuels", fuelType.getName());
    }
}

