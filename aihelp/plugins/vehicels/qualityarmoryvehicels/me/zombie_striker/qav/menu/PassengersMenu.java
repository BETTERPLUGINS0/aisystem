/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.menu;

import me.zombie_striker.qav.ItemFact;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.gui.components.GuiAction;
import me.zombie_striker.qav.gui.guis.GuiItem;
import me.zombie_striker.qav.menu.Menu;
import me.zombie_striker.qav.perms.PermissionHandler;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PassengersMenu
extends Menu {
    private final VehicleEntity ve;

    public PassengersMenu(@NotNull Player player, VehicleEntity vehicleEntity) {
        super(3, MessagesConfig.MENU_PASSAGER_SEATS_TITLE.replace("%cartype%", vehicleEntity.getType().getDisplayname()), player);
        this.ve = vehicleEntity;
    }

    @Override
    public void setupItems() {
        ItemStack itemStack;
        String string;
        this.setPageButtons();
        GuiAction<InventoryClickEvent> guiAction = inventoryClickEvent -> {
            Main.DEBUG("Clicked setaspassager");
            if (inventoryClickEvent.getCurrentItem() != null && this.ve != null) {
                if (inventoryClickEvent.getCurrentItem().getType() == Material.BRICK_STAIRS) {
                    Entity entity = this.ve.getDriverSeat().getPassenger();
                    if (entity == null) {
                        if (!Main.requirePermissionToDrive || PermissionHandler.canDrive(this.player, this.ve.getType())) {
                            this.ve.getDriverSeat().setPassenger((Entity)inventoryClickEvent.getWhoClicked());
                            Main.DEBUG("Added player to seat!");
                        } else {
                            Main.DEBUG("Stopped player from being added to seat!");
                        }
                    } else {
                        Main.DEBUG("Another passager is already in driver seat : " + entity.getName());
                    }
                } else {
                    Entity entity = this.ve.getPassager(inventoryClickEvent.getCurrentItem().getAmount() - 1);
                    if (entity == null && inventoryClickEvent.getSlot() - 1 < this.ve.getType().getPassagerSpots().size()) {
                        QualityArmoryVehicles.setAddPassager(this.ve, (Player)inventoryClickEvent.getWhoClicked(), inventoryClickEvent.getCurrentItem().getAmount() - 1);
                        Main.DEBUG("Added player to seat!");
                    } else {
                        Main.DEBUG("Another passager is already in the " + (inventoryClickEvent.getCurrentItem().getAmount() - 1) + " seat : " + (entity != null && entity.getPassenger() != null ? entity.getPassenger().getName() : "ERROR"));
                    }
                }
                this.close(inventoryClickEvent.getWhoClicked());
            }
        };
        if (this.ve.getDriverSeat() == null) {
            return;
        }
        if (this.ve.getDriverSeat().getPassenger() != null) {
            Entity entity = this.ve.getDriverSeat().getPassenger();
            string = MessagesConfig.ICON_PASSAGERS_FULL.replace("%name%", entity != null ? entity.getName() : "ERROR");
            itemStack = ItemFact.a(Material.BARRIER, string, MessagesConfig.ICONLORE_PASSAGERS_DRIVERSEAT);
        } else {
            string = MessagesConfig.ICON_PASSAGERS_EMPTY;
            itemStack = ItemFact.a(Material.BRICK_STAIRS, string, MessagesConfig.ICONLORE_PASSAGERS_DRIVERSEAT);
        }
        this.setItem(0, new GuiItem(itemStack, guiAction));
        for (int i = 0; i < this.ve.getType().getPassagerSpots().size(); ++i) {
            boolean bl;
            Entity entity = this.ve.getPassager(i);
            boolean bl2 = bl = entity != null;
            if (bl && entity.getPassenger() == null) {
                bl = false;
                this.ve.updateSeats();
            }
            if (bl) {
                Entity entity2 = entity.getPassenger();
                string = MessagesConfig.ICON_PASSAGERS_FULL.replace("%name%", entity2 != null ? entity2.getName() : "ERROR");
                itemStack = ItemFact.a(Material.BARRIER, string, new String[0]);
            } else {
                string = MessagesConfig.ICON_PASSAGERS_EMPTY;
                itemStack = ItemFact.a(Material.COBBLESTONE_STAIRS, string, new String[0]);
            }
            itemStack.setAmount(i + 1);
            this.addItem(new GuiItem(itemStack, guiAction));
        }
    }
}

