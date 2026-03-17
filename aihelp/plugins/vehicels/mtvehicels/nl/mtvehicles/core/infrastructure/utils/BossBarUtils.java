/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.boss.BarColor
 *  org.bukkit.boss.BarFlag
 *  org.bukkit.boss.BarStyle
 *  org.bukkit.boss.BossBar
 *  org.bukkit.entity.Player
 */
package nl.mtvehicles.core.infrastructure.utils;

import java.util.HashMap;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarUtils {
    public static HashMap<String, BossBar> Fuelbar = new HashMap();

    public static void setBossBarValue(double counter, String licensePlate) {
        if (((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED)).booleanValue() && ((Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED)).booleanValue()) {
            if (!Fuelbar.containsKey(licensePlate)) {
                return;
            }
            Fuelbar.get(licensePlate).setProgress(counter);
            Fuelbar.get(licensePlate).setTitle(Math.round(counter * 100.0) + "% " + TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.BOSSBAR_FUEL)));
            Double fuel = VehicleData.fuel.get(licensePlate);
            if (fuel < 30.0) {
                Fuelbar.get(licensePlate).setColor(BarColor.RED);
                return;
            }
            if (fuel < 60.0) {
                Fuelbar.get(licensePlate).setColor(BarColor.YELLOW);
                return;
            }
            if (fuel < 100.0) {
                Fuelbar.get(licensePlate).setColor(BarColor.GREEN);
            }
        }
    }

    public static void removeBossBar(Player player, String licensePlate) {
        if (((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED)).booleanValue() && ((Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED)).booleanValue()) {
            Fuelbar.get(licensePlate).removePlayer(player);
        }
    }

    public static void addBossBar(Player player, String licensePlate) {
        if (((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED)).booleanValue() && ((Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED)).booleanValue()) {
            double fuel = (Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL);
            String fuelString = String.valueOf(fuel);
            BossBar bar = Bukkit.createBossBar((String)(Math.round(Double.parseDouble(fuelString)) + "% " + TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.BOSSBAR_FUEL))), (BarColor)BarColor.GREEN, (BarStyle)BarStyle.SOLID, (BarFlag[])new BarFlag[0]);
            Fuelbar.put(licensePlate, bar);
            if (fuel < 30.0) {
                Fuelbar.get(licensePlate).setColor(BarColor.RED);
            }
            if (fuel < 60.0) {
                Fuelbar.get(licensePlate).setColor(BarColor.YELLOW);
            }
            if (fuel < 100.0) {
                Fuelbar.get(licensePlate).setColor(BarColor.GREEN);
            }
            Fuelbar.get(licensePlate).addPlayer(player);
        }
    }
}

