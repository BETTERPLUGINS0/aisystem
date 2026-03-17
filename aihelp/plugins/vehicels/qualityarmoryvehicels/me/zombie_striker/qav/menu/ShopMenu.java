/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.zombie_striker.qav.ItemFact;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.UnlockedVehicle;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.fuel.FuelItemStack;
import me.zombie_striker.qav.fuel.RepairItemStack;
import me.zombie_striker.qav.gui.guis.GuiItem;
import me.zombie_striker.qav.menu.Menu;
import me.zombie_striker.qav.perms.PermissionHandler;
import me.zombie_striker.qav.qamini.EconHandler;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ShopMenu
extends Menu {
    private static final Map<UUID, Long> LAST_SHOP = new HashMap<UUID, Long>();

    public ShopMenu(@NotNull Player player) {
        super(4, MessagesConfig.MENU_SHOP_TITLE, player);
    }

    @Override
    public void setupItems() {
        this.setPageButtons();
        if (Main.repairItem.shouldBeInShop()) {
            this.addItem(new GuiItem(ItemFact.a(Main.repairItem.getMaterial(), Main.repairItem.getData(), true, Main.repairItem.getName(), MessagesConfig.ICONLORE_COST + Main.repairItem.getCost()), inventoryClickEvent -> {
                if (!this.canShop(inventoryClickEvent.getWhoClicked())) {
                    return;
                }
                RepairItemStack repairItemStack = Main.repairItem;
                if (!EconHandler.hasEnough(repairItemStack.getCost(), (Player)inventoryClickEvent.getWhoClicked())) {
                    inventoryClickEvent.getWhoClicked().sendMessage(Main.prefix + MessagesConfig.MESSAGE_NOT_ENOUGH_MONEY);
                    return;
                }
                if (inventoryClickEvent.getWhoClicked().getInventory().firstEmpty() == -1) {
                    inventoryClickEvent.getWhoClicked().sendMessage("Your inventory is full.");
                    Main.DEBUG("inventory was full");
                    return;
                }
                EconHandler.pay(repairItemStack.getCost(), (Player)inventoryClickEvent.getWhoClicked());
                inventoryClickEvent.getWhoClicked().getInventory().addItem(new ItemStack[]{repairItemStack.asItem()});
                LAST_SHOP.put(inventoryClickEvent.getWhoClicked().getUniqueId(), System.currentTimeMillis());
            }));
        }
        for (FuelItemStack object : FuelItemStack.getFuels()) {
            if (!object.isAllowedInShop()) continue;
            this.addItem(new GuiItem(ItemFact.a(object.getMaterial(), object.getData(), true, object.getDisplayname(), MessagesConfig.ICONLORE_COST + object.getCost()), inventoryClickEvent -> {
                if (!this.canShop(inventoryClickEvent.getWhoClicked())) {
                    return;
                }
                if (!EconHandler.hasEnough(object.getCost(), (Player)inventoryClickEvent.getWhoClicked())) {
                    inventoryClickEvent.getWhoClicked().sendMessage(Main.prefix + MessagesConfig.MESSAGE_NOT_ENOUGH_MONEY);
                    return;
                }
                if (inventoryClickEvent.getWhoClicked().getInventory().firstEmpty() == -1) {
                    inventoryClickEvent.getWhoClicked().sendMessage("Your inventory is full.");
                    Main.DEBUG("inventory was full");
                    return;
                }
                EconHandler.pay(object.getCost(), (Player)inventoryClickEvent.getWhoClicked());
                inventoryClickEvent.getWhoClicked().getInventory().addItem(new ItemStack[]{object.getItemStack()});
                LAST_SHOP.put(inventoryClickEvent.getWhoClicked().getUniqueId(), System.currentTimeMillis());
            }));
        }
        for (AbstractVehicle abstractVehicle : Main.vehicleTypes) {
            if (!abstractVehicle.isAllowedInShop() || Main.enable_RequirePermToBuyVehicle && !PermissionHandler.canDrive(this.player, abstractVehicle)) continue;
            this.addItem(new GuiItem(ItemFact.a(abstractVehicle.getMaterial(), abstractVehicle.getItemData(), true, abstractVehicle.getDisplayname(), MessagesConfig.ICONLORE_COST + abstractVehicle.getCost()), inventoryClickEvent -> {
                if (!this.canShop(inventoryClickEvent.getWhoClicked())) {
                    return;
                }
                if (Main.enableVehicleLimiter) {
                    int n = PermissionHandler.getMaxOwnVehicles((Player)inventoryClickEvent.getWhoClicked());
                    int n2 = QualityArmoryVehicles.getOwnedVehicles(inventoryClickEvent.getWhoClicked().getUniqueId()).size();
                    if (n2 >= n) {
                        inventoryClickEvent.getWhoClicked().sendMessage(Main.prefix + MessagesConfig.MESSAGE_TOO_MANY_VEHICLES);
                        return;
                    }
                }
                if (EconHandler.hasEnough(abstractVehicle.getCost(), (Player)inventoryClickEvent.getWhoClicked())) {
                    ItemStack itemStack = ItemFact.getItem(abstractVehicle);
                    if (inventoryClickEvent.getWhoClicked().getInventory().firstEmpty() == -1) {
                        inventoryClickEvent.getWhoClicked().sendMessage("Your inventory is full.");
                        Main.DEBUG("inventory was full");
                        return;
                    }
                    EconHandler.pay(abstractVehicle.getCost(), (Player)inventoryClickEvent.getWhoClicked());
                    inventoryClickEvent.getWhoClicked().sendMessage(Main.prefix + MessagesConfig.MESSAGE_BOUGHT_CAR.replace("%car%", abstractVehicle.getDisplayname()).replace("%price%", abstractVehicle.getCost() + ""));
                    if (Main.enableGarage) {
                        QualityArmoryVehicles.addUnlockedVehicle((OfflinePlayer)((Player)inventoryClickEvent.getWhoClicked()), new UnlockedVehicle(abstractVehicle, abstractVehicle.getMaxHealth(), true));
                    } else {
                        inventoryClickEvent.getWhoClicked().getInventory().addItem(new ItemStack[]{itemStack});
                    }
                    Main.DEBUG("Finished paying for vehicle");
                    LAST_SHOP.put(inventoryClickEvent.getWhoClicked().getUniqueId(), System.currentTimeMillis());
                } else {
                    inventoryClickEvent.getWhoClicked().sendMessage(Main.prefix + MessagesConfig.MESSAGE_NOT_ENOUGH_MONEY);
                }
            }));
        }
    }

    private boolean canShop(HumanEntity humanEntity) {
        if (Main.enableShopCooldown && LAST_SHOP.containsKey(humanEntity.getUniqueId())) {
            if (System.currentTimeMillis() - LAST_SHOP.get(humanEntity.getUniqueId()) < 500L) {
                humanEntity.sendMessage(MessagesConfig.COOLDOWN.replace("%time%", "500"));
                return false;
            }
            LAST_SHOP.remove(humanEntity.getUniqueId());
        }
        return true;
    }
}

