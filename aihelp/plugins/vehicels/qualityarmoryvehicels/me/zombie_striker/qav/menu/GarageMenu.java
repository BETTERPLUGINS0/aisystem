/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.menu;

import java.util.List;
import me.zombie_striker.qav.ItemFact;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.UnlockedVehicle;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.gui.guis.GuiItem;
import me.zombie_striker.qav.menu.Menu;
import me.zombie_striker.qav.perms.PermissionHandler;
import me.zombie_striker.qav.util.VehicleUtils;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GarageMenu
extends Menu {
    private final Player target;

    public GarageMenu(@NotNull Player player, @NotNull Player player2) {
        super(4, player.getUniqueId() == player2.getUniqueId() ? MessagesConfig.MENU_GARAGE_TITLE : String.format(MessagesConfig.MENU_OTHER_GARAGE_TITLE, player2.getName()), player);
        this.target = player2;
    }

    @Override
    public void setupItems() {
        this.setPageButtons();
        List<UnlockedVehicle> list = QualityArmoryVehicles.unlockedVehicles((OfflinePlayer)this.target);
        for (UnlockedVehicle unlockedVehicle : list) {
            this.addItem(new GuiItem(ItemFact.getItem(unlockedVehicle.getVehicleType()), inventoryClickEvent -> {
                Object object2;
                Main.DEBUG("Open Garage");
                if (!Main.enableGarage) {
                    inventoryClickEvent.getWhoClicked().getInventory().addItem(new ItemStack[]{inventoryClickEvent.getCurrentItem()});
                    inventoryClickEvent.getWhoClicked().closeInventory();
                    inventoryClickEvent.getWhoClicked().sendMessage("Something went wrong with the garage. Only the plugin should be allowed to add items to the garage.");
                    return;
                }
                AbstractVehicle abstractVehicle = QualityArmoryVehicles.getVehicleByItem(inventoryClickEvent.getCurrentItem());
                if (Main.requirePermissionToDrive && !PermissionHandler.canDrive(this.player, abstractVehicle)) {
                    inventoryClickEvent.getWhoClicked().sendMessage(Main.prefix + MessagesConfig.MESSAGE_NO_PERM_DRIVE);
                    Main.DEBUG("Cannot drive because player does not have permission");
                    return;
                }
                int n = 0;
                for (VehicleEntity object22 : QualityArmoryVehicles.getOwnedVehicles(this.target.getUniqueId())) {
                    if (object22 == null || object22.getDriverSeat() == null || object22.getModelEntity() == null || object22.getType() != abstractVehicle) continue;
                    ++n;
                }
                int n2 = 0;
                for (Object object2 : list) {
                    if (((UnlockedVehicle)object2).getVehicleType() != abstractVehicle) continue;
                    ++n2;
                }
                UnlockedVehicle unlockedVehicle = QualityArmoryVehicles.findUnlockedVehicle((OfflinePlayer)this.player, abstractVehicle);
                if (!unlockedVehicle.isInGarage()) {
                    inventoryClickEvent.getWhoClicked().closeInventory();
                    QualityArmoryVehicles.removeUnlockedVehicle((OfflinePlayer)this.target, unlockedVehicle);
                    Main.vehicles.stream().filter(vehicleEntity -> vehicleEntity.getOwner().equals(this.target.getUniqueId())).filter(vehicleEntity -> vehicleEntity.getType().getName().equals(unlockedVehicle.getVehicleType().getName())).findFirst().ifPresent(vehicleEntity -> VehicleUtils.callback(vehicleEntity, this.target, "Garage callback"));
                    return;
                }
                if (Main.enableVehicleLimiter && QualityArmoryVehicles.getOwnedVehicles(this.target.getUniqueId()).size() >= PermissionHandler.getMaxOwnVehicles(this.target)) {
                    inventoryClickEvent.getWhoClicked().sendMessage(Main.prefix + MessagesConfig.MESSAGE_TOO_MANY_VEHICLES);
                    inventoryClickEvent.getWhoClicked().closeInventory();
                    return;
                }
                if (n >= n2) {
                    inventoryClickEvent.getWhoClicked().sendMessage(Main.prefix + MessagesConfig.MESSAGE_TOO_MANY_VEHICLES_Type);
                    Main.DEBUG("Player has too many vehicles.");
                    return;
                }
                object2 = QualityArmoryVehicles.spawnVehicle(unlockedVehicle, (Player)inventoryClickEvent.getWhoClicked());
                if (object2 == null) {
                    return;
                }
                if (!Main.enableGarageCallback) {
                    QualityArmoryVehicles.removeUnlockedVehicle((OfflinePlayer)this.target, unlockedVehicle);
                } else {
                    QualityArmoryVehicles.removeUnlockedVehicle((OfflinePlayer)this.target, unlockedVehicle);
                    unlockedVehicle.setInGarage(false);
                    QualityArmoryVehicles.addUnlockedVehicle((OfflinePlayer)this.target, unlockedVehicle);
                }
                if (Main.garageFuel) {
                    ((VehicleEntity)object2).setFuel(32000);
                }
                ((VehicleEntity)object2).getDriverSeat().setPassenger((Entity)inventoryClickEvent.getWhoClicked());
                Main.DEBUG("Set as passager and added fuel");
                inventoryClickEvent.getWhoClicked().closeInventory();
            }));
        }
    }
}

