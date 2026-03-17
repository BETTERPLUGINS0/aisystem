/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.PlaceholderAPI
 *  me.clip.placeholderapi.expansion.PlaceholderExpansion
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 */
package nl.mtvehicles.core.infrastructure.dependencies;

import java.text.DecimalFormat;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderUtils
extends PlaceholderExpansion {
    private final Main plugin = Main.instance;

    public String getAuthor() {
        return "MTVehicles";
    }

    public String getIdentifier() {
        return "mtv";
    }

    public String getVersion() {
        return VersionModule.getPluginVersion();
    }

    public boolean persist() {
        return true;
    }

    public String onRequest(OfflinePlayer p, String parameter) {
        if (parameter.equalsIgnoreCase("fuel_pricePerLitre")) {
            return ConfigModule.defaultConfig.get(DefaultConfig.Option.GAS_STATIONS_FILL_JERRYCANS_PRICE_PER_LITRE).toString();
        }
        if (parameter.equalsIgnoreCase("vehicle_licensePlate")) {
            if (!p.isOnline()) {
                return "";
            }
            if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
                return "";
            }
            return VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
        }
        if (parameter.equalsIgnoreCase("vehicle_name")) {
            if (!p.isOnline()) {
                return "";
            }
            if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
                return "";
            }
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString();
        }
        if (parameter.equalsIgnoreCase("vehicle_type")) {
            if (!p.isOnline()) {
                return "";
            }
            if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
                return "";
            }
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return VehicleType.valueOf(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.VEHICLE_TYPE).toString()).getName();
        }
        if (parameter.equalsIgnoreCase("vehicle_fuel")) {
            if (!p.isOnline()) {
                return "";
            }
            if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
                return "";
            }
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            Double fuel = VehicleUtils.getVehicle(licensePlate).getCurrentFuel();
            if (fuel == null) {
                return "";
            }
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(fuel) + " %";
        }
        if (parameter.equalsIgnoreCase("vehicle_speed")) {
            if (!p.isOnline()) {
                return "";
            }
            if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
                return "";
            }
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            Double speed = VehicleUtils.getVehicle(licensePlate).getCurrentSpeed();
            if (speed == null) {
                return "0.0 blocks/sec";
            }
            DecimalFormat df = new DecimalFormat("#.###");
            return df.format(speed) + " blocks/sec";
        }
        if (parameter.equalsIgnoreCase("vehicle_maxspeed")) {
            if (!p.isOnline()) {
                return "";
            }
            if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
                return "";
            }
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            DecimalFormat df = new DecimalFormat("#.###");
            return df.format((Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.MAX_SPEED) * 20.0) + " blocks/sec";
        }
        if (parameter.equalsIgnoreCase("vehicle_place")) {
            if (!p.isOnline()) {
                return "";
            }
            if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
                return "";
            }
            Vehicle.Seat seat = Vehicle.Seat.getSeat(p.getPlayer());
            return seat == null ? "" : seat.toString();
        }
        if (parameter.equalsIgnoreCase("vehicle_seats")) {
            if (!p.isOnline()) {
                return "";
            }
            if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
                return "";
            }
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
            return String.valueOf(vehicle.getSeatsAmount());
        }
        if (parameter.equalsIgnoreCase("vehicle_uuid")) {
            if (!p.isOnline()) {
                return "";
            }
            if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
                return "";
            }
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return VehicleUtils.getUUID(licensePlate);
        }
        if (parameter.equalsIgnoreCase("vehicle_owner")) {
            if (!p.isOnline()) {
                return "";
            }
            if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
                return "";
            }
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return VehicleUtils.getVehicle(licensePlate).getOwnerName();
        }
        return null;
    }

    public static String parsePlaceholders(Player player, String text) {
        return PlaceholderAPI.setPlaceholders((Player)player, (String)text);
    }

    public void unregisterOnDisable() {
        this.unregister();
    }
}

