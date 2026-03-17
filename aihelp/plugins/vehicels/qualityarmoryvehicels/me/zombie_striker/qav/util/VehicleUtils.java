/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package me.zombie_striker.qav.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.zombie_striker.qav.ItemFact;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.UnlockedVehicle;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.util.xseries.XItemStack;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class VehicleUtils {
    private static final List<UUID> OVERRIDE_WHITELIST = new ArrayList<UUID>();

    public static void callback(VehicleEntity vehicleEntity, Player player) {
        VehicleUtils.callback(vehicleEntity, player, "Callback");
    }

    public static void callback(VehicleEntity vehicleEntity, Player player, String string) {
        if (player != null) {
            for (ItemStack itemStack : vehicleEntity.getTrunk()) {
                if (itemStack == null) continue;
                XItemStack.giveOrDrop(player, itemStack);
            }
            for (ItemStack itemStack : vehicleEntity.getFuels().getContents()) {
                if (itemStack == null) continue;
                XItemStack.giveOrDrop(player, itemStack);
            }
        }
        vehicleEntity.getFuels().clear();
        vehicleEntity.getTrunk().clear();
        vehicleEntity.deconstruct(player, string);
        if (player != null) {
            if (!Main.enableGarage) {
                XItemStack.giveOrDrop(player, ItemFact.getItem(vehicleEntity.getType()));
            } else {
                List<UnlockedVehicle> list = QualityArmoryVehicles.unlockedVehicles((OfflinePlayer)player);
                UnlockedVehicle unlockedVehicle = QualityArmoryVehicles.findUnlockedVehicle((OfflinePlayer)player, vehicleEntity.getType());
                if (unlockedVehicle == null) {
                    unlockedVehicle = new UnlockedVehicle(vehicleEntity.getType(), vehicleEntity.getHealth(), true);
                } else {
                    list.remove(unlockedVehicle);
                }
                unlockedVehicle.setInGarage(true);
                unlockedVehicle.setHealth(vehicleEntity.getHealth());
                list.add(unlockedVehicle);
                QualityArmoryVehicles.setUnlockedVehicles((OfflinePlayer)player, list);
            }
        }
    }

    public static boolean isOverrideWhitelisted(UUID uUID) {
        return OVERRIDE_WHITELIST.contains(uUID);
    }

    public static void setOverrideWhitelisted(UUID uUID, boolean bl) {
        if (bl) {
            OVERRIDE_WHITELIST.add(uUID);
        } else {
            OVERRIDE_WHITELIST.remove(uUID);
        }
    }

    public static void toggleOverrideWhitelisted(UUID uUID) {
        VehicleUtils.setOverrideWhitelisted(uUID, !VehicleUtils.isOverrideWhitelisted(uUID));
    }
}

