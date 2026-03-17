/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerSwapHandItemsEvent
 *  org.bukkit.plugin.Plugin
 */
package me.zombie_striker.qav.finput;

import java.util.HashMap;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.finput.FInput;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.Plugin;

public class FInputManager
implements Listener {
    public static String CAR_HONK = "HONK";
    public static String POLICE_SIREN = "SIREN";
    public static String MININUKE_BOMBER = "MININUKE_BOMBER";
    public static String TNTBOMBER = "TNT_BOMBER";
    public static String LAUNCHER_40mm = "40MM_LAUNCHER";
    public static String LAUNCHER_556 = "BULLETS_556";
    public static HashMap<String, FInput> handlers = new HashMap();

    public static void init(Main main) {
        Bukkit.getPluginManager().registerEvents((Listener)new FInputManager(), (Plugin)main);
    }

    public static void add(FInput fInput) {
        handlers.put(fInput.getName().toUpperCase(), fInput);
    }

    public static FInput getHandler(String string) {
        return handlers.get(string.toUpperCase());
    }

    @EventHandler
    public void onF(PlayerSwapHandItemsEvent playerSwapHandItemsEvent) {
        if (playerSwapHandItemsEvent.getPlayer().getVehicle() != null && QualityArmoryVehicles.isVehicle(playerSwapHandItemsEvent.getPlayer().getVehicle())) {
            VehicleEntity vehicleEntity = QualityArmoryVehicles.getVehicleEntityByEntity(playerSwapHandItemsEvent.getPlayer().getVehicle());
            if (vehicleEntity == null) {
                return;
            }
            if (playerSwapHandItemsEvent.getPlayer().getVehicle().equals((Object)vehicleEntity.getDriverSeat())) {
                FInput fInput = vehicleEntity.getType().getInput(FInput.ClickType.F);
                if (fInput == null) {
                    return;
                }
                playerSwapHandItemsEvent.setCancelled(true);
                Main.DEBUG("Calling " + fInput + " Input for " + vehicleEntity.getType().getName());
                fInput.onInput(vehicleEntity);
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getPlayer().getVehicle() != null && QualityArmoryVehicles.isVehicle(playerInteractEvent.getPlayer().getVehicle())) {
            VehicleEntity vehicleEntity = QualityArmoryVehicles.getVehicleEntityByEntity(playerInteractEvent.getPlayer().getVehicle());
            if (vehicleEntity == null) {
                return;
            }
            if (playerInteractEvent.getPlayer().getVehicle().equals((Object)vehicleEntity.getDriverSeat())) {
                FInput fInput = null;
                if (playerInteractEvent.getAction().equals((Object)Action.LEFT_CLICK_AIR) || playerInteractEvent.getAction().equals((Object)Action.LEFT_CLICK_BLOCK)) {
                    fInput = vehicleEntity.getType().getInput(FInput.ClickType.LEFT);
                } else if (playerInteractEvent.getAction().equals((Object)Action.RIGHT_CLICK_AIR) || playerInteractEvent.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK)) {
                    fInput = vehicleEntity.getType().getInput(FInput.ClickType.RIGHT);
                }
                if (fInput == null) {
                    return;
                }
                playerInteractEvent.setCancelled(true);
                Main.DEBUG("Calling " + fInput + " Input for " + vehicleEntity.getType().getName());
                fInput.onInput(vehicleEntity);
            }
        }
    }
}

