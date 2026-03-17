/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;
import me.zombie_striker.qav.ItemFact;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.fuel.FuelItemStack;
import me.zombie_striker.qav.gui.guis.GuiItem;
import me.zombie_striker.qav.menu.AddWhitelistMenu;
import me.zombie_striker.qav.menu.Menu;
import me.zombie_striker.qav.menu.PassengersMenu;
import me.zombie_striker.qav.menu.RemoveWhitelistMenu;
import me.zombie_striker.qav.util.VehicleUtils;
import me.zombie_striker.qav.util.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OverviewMenu
extends Menu {
    private final VehicleEntity ve;

    public OverviewMenu(@NotNull Player player, @NotNull VehicleEntity vehicleEntity) {
        super(2, MessagesConfig.MENU_OVERVIEW_TITLE.replace("%cartype%", vehicleEntity.getType().getDisplayname()), player);
        this.ve = vehicleEntity;
    }

    @Override
    public void setupItems() {
        int n;
        Object object2;
        if (Main.enableTrunks) {
            object2 = Arrays.asList(this.ve.getTrunk().getContents());
            n = 7;
            boolean bl = object2.size() > n;
            String[] object3 = new String[1 + (bl ? n : object2.size())];
            object3[0] = MessagesConfig.ICONLORE_TRUNK_CONTAINS;
            int n2 = 1;
            Iterator iterator = object2.iterator();
            while (iterator.hasNext()) {
                ItemStack itemStack = (ItemStack)iterator.next();
                if (bl && n2 == n) {
                    object3[n2] = ChatColor.GRAY + "+" + (object2.size() - n2) + " more...";
                    break;
                }
                if (itemStack == null) continue;
                object3[n2] = ChatColor.GRAY + itemStack.getType().name() + ":" + itemStack.getAmount();
                ++n2;
            }
            this.setItem(4, new GuiItem(ItemFact.a(Material.CHEST, MessagesConfig.ICON_TRUNK, object3), inventoryClickEvent -> {
                Main.DEBUG("will open trunk");
                inventoryClickEvent.getWhoClicked().openInventory(this.ve.getTrunk());
            }));
        }
        if (this.ve.getWhiteList() != null) {
            object2 = new String[1 + this.ve.getWhiteList().size()];
            object2[0] = MessagesConfig.ICONLORE_LIST_WHITELIST;
            n = 1;
            for (UUID uUID : this.ve.getWhiteList()) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer((UUID)uUID);
                object2[n] = ChatColor.GRAY + offlinePlayer.getName();
                ++n;
            }
            this.setItem(1, new GuiItem(ItemFact.askull(null, MessagesConfig.ICON_ADD_WHITELIST, (String[])object2), inventoryClickEvent -> {
                Main.DEBUG("will open add whitelist");
                new AddWhitelistMenu((Player)inventoryClickEvent.getWhoClicked(), this, this.ve).open();
            }));
            this.setItem(10, new GuiItem(ItemFact.a(Material.BARRIER, MessagesConfig.ICON_REMOVE_WHITELIST, (String[])object2), inventoryClickEvent -> {
                Main.DEBUG("will open add whitelist");
                new RemoveWhitelistMenu((Player)inventoryClickEvent.getWhoClicked(), this, this.ve).open();
            }));
        }
        this.setItem(5, new GuiItem(ItemFact.a(Material.COBBLESTONE_STAIRS, MessagesConfig.ICON_PASSAGERS, new String[0]), inventoryClickEvent -> {
            Main.DEBUG("will open passagers");
            new PassengersMenu((Player)inventoryClickEvent.getWhoClicked(), this.ve).open();
        }));
        this.setItem(0, new GuiItem(ItemFact.a(XMaterial.OAK_SIGN.parseMaterial(), MessagesConfig.translatePublic(this.ve), MessagesConfig.ICONLORE_PUBLIC), inventoryClickEvent -> {
            Main.DEBUG("Swapping ispublic from " + this.ve.allowsPassagers() + " to " + !this.ve.allowsPassagers());
            this.ve.setAllowsPassagers(!this.ve.allowsPassagers());
            this.updateItem(0, ItemFact.a(XMaterial.OAK_SIGN.parseMaterial(), MessagesConfig.translatePublic(this.ve), MessagesConfig.ICONLORE_PUBLIC));
        }));
        if (this.ve.getType().enableFuel()) {
            object2 = new ArrayList<ItemStack>(Arrays.asList(this.ve.getFuels().getContents()));
            n = 0;
            String[] stringArray = new String[1];
            Iterator iterator = object2.iterator();
            while (iterator.hasNext()) {
                ItemStack itemStack = (ItemStack)iterator.next();
                if (itemStack == null) continue;
                n += FuelItemStack.getFuelForItem(itemStack) * itemStack.getAmount();
            }
            stringArray[0] = MessagesConfig.ICONLORE_TRUNK_CONTAINS + " " + n / 20 + "s";
            this.setItem(7, new GuiItem(ItemFact.a(Material.COAL, MessagesConfig.ICON_CHECK_FUEL, stringArray), inventoryClickEvent -> {
                Main.DEBUG("will open check fuel");
                inventoryClickEvent.getWhoClicked().openInventory(this.ve.getFuels());
            }));
        }
        this.setItem(16, new GuiItem(ItemFact.a(Material.IRON_BLOCK, MessagesConfig.ICON_HEALTH, MessagesConfig.ICONLORE_HEALTH_FORMAT.replace("%maxhealth%", this.ve.getType().getMaxHealth() + "").replace("%health%", "" + this.ve.getHealth()))));
        if (Main.allowVehiclePickup) {
            this.setItem(8, new GuiItem(ItemFact.a(this.ve.getType().getMaterial(), this.ve.getType().getItemData(), true, MessagesConfig.ICON_PICKUP, MessagesConfig.ICONLORE_PICKUP_OWNER, MessagesConfig.ICONLORE_PICKUP_TRUNK), inventoryClickEvent -> {
                if (!inventoryClickEvent.getWhoClicked().hasPermission("qualityarmoryvehicles.pickupvehicle")) {
                    return;
                }
                Main.DEBUG("will open pickup");
                if (this.ve.getOwner() == null || this.ve.getOwner().equals(inventoryClickEvent.getWhoClicked().getUniqueId())) {
                    inventoryClickEvent.getWhoClicked().closeInventory();
                    Entity entity = this.ve.getDriverSeat().getPassenger();
                    if (entity != null) {
                        inventoryClickEvent.getWhoClicked().sendMessage(Main.prefix + MessagesConfig.MESSAGE_CannotPickupWhileInVehicle);
                        return;
                    }
                    inventoryClickEvent.getWhoClicked().closeInventory();
                    if (inventoryClickEvent.getWhoClicked().getInventory().firstEmpty() == -1) {
                        inventoryClickEvent.getWhoClicked().sendMessage(Main.prefix + MessagesConfig.MESSAGE_PICKUP_DROPPED);
                    }
                    VehicleUtils.callback(this.ve, (Player)inventoryClickEvent.getWhoClicked(), "pickup_menu");
                }
            }));
        }
        if (this.ve.getOwner() != null) {
            object2 = Bukkit.getOfflinePlayer((UUID)this.ve.getOwner());
            String string = object2.getName() != null ? object2.getName() : "Null Name";
            this.setItem(17, new GuiItem(ItemFact.a(Material.BARRIER, MessagesConfig.ICON_OWNERSHIP, MessagesConfig.ICONLORE_currentowner.replaceAll("%owner%", string)), inventoryClickEvent -> {
                if (this.ve.getOwner() != null && this.ve.getOwner().equals(inventoryClickEvent.getWhoClicked().getUniqueId())) {
                    this.ve.setOwner(null);
                    inventoryClickEvent.getWhoClicked().sendMessage(Main.prefix + MessagesConfig.MESSAGE_NO_OWNER_NOW);
                    inventoryClickEvent.getWhoClicked().closeInventory();
                }
            }));
        }
    }
}

