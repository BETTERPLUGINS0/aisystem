/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  java.lang.StackWalker
 *  java.lang.StackWalker$Option
 *  lombok.Generated
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.sbdeveloper.vehiclesplus;

import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.Wheel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.BikeSeat;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Seat;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.TurretSeat;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.BikeSkin;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Rotor;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Skin;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Turret;
import nl.sbdeveloper.vehiclesplus.config.Config;
import nl.sbdeveloper.vehiclesplus.utils.jackson.JacksonHelper;
import org.bukkit.plugin.java.JavaPlugin;

public class VehiclesPlusPluginManager {
    private static JavaPlugin vehiclesPlusPlugin;
    private static Config config;

    private VehiclesPlusPluginManager() {
    }

    public static JavaPlugin getVehiclesPlusPlugin() {
        assert (VehiclesPlusPluginManager.isInternal());
        return vehiclesPlusPlugin;
    }

    public static void init(JavaPlugin javaPlugin, Config config) {
        assert (VehiclesPlusPluginManager.isInternal());
        if (vehiclesPlusPlugin != null) {
            return;
        }
        vehiclesPlusPlugin = javaPlugin;
        VehiclesPlusPluginManager.config = config;
    }

    public static void load() {
        assert (VehiclesPlusPluginManager.isInternal());
        VehiclesPlusAPI.registerPart(Seat.class);
        VehiclesPlusAPI.registerPart(BikeSeat.class);
        VehiclesPlusAPI.registerPart(TurretSeat.class);
        VehiclesPlusAPI.registerPart(Wheel.class);
        VehiclesPlusAPI.registerPart(Skin.class);
        VehiclesPlusAPI.registerPart(BikeSkin.class);
        VehiclesPlusAPI.registerPart(Rotor.class);
        VehiclesPlusAPI.registerPart(Turret.class);
        VehiclesPlusAPI.getHooks().forEach(Runnable::run);
    }

    public static void reinit(JavaPlugin javaPlugin, Config config) {
        assert (VehiclesPlusPluginManager.isInternal());
        JacksonHelper.getPartTypes().clear();
        VehiclesPlusAPI.getVehicleTypes().clear();
        VehiclesPlusAPI.getFuelTypes().clear();
        VehiclesPlusAPI.getRimDesigns().clear();
        VehiclesPlusAPI.getVehicleModels().clear();
        vehiclesPlusPlugin = null;
        VehiclesPlusPluginManager.init(javaPlugin, config);
        VehiclesPlusPluginManager.load();
    }

    private static boolean isInternal() {
        StackWalker stackWalker = StackWalker.getInstance((StackWalker.Option)StackWalker.Option.RETAIN_CLASS_REFERENCE);
        return stackWalker.getCallerClass().getPackageName().startsWith(VehiclesPlusPluginManager.class.getPackageName());
    }

    @Generated
    public static Config getConfig() {
        return config;
    }
}

