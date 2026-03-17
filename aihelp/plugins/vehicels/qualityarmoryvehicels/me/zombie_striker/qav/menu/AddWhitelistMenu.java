/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.menu;

import me.zombie_striker.qav.ItemFact;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.gui.components.GuiAction;
import me.zombie_striker.qav.gui.guis.GuiItem;
import me.zombie_striker.qav.menu.Menu;
import me.zombie_striker.qav.menu.OverviewMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class AddWhitelistMenu
extends Menu {
    private final OverviewMenu overview;
    private final VehicleEntity ve;

    public AddWhitelistMenu(@NotNull Player player, OverviewMenu overviewMenu, VehicleEntity vehicleEntity) {
        super(4, MessagesConfig.MENU_ADD_ALLOWED_TITLE.replace("%cartype%", vehicleEntity.getType().getDisplayname()), player);
        this.overview = overviewMenu;
        this.ve = vehicleEntity;
    }

    @Override
    public void setupItems() {
        this.setPageButtons();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (this.ve.getWhiteList() != null && this.ve.getWhiteList().contains(player.getUniqueId())) continue;
            if (Main.useHeads) {
                this.addItem(new GuiItem(ItemFact.askull(player.getName(), ChatColor.YELLOW + player.getName(), new String[0]), this.getAction(player)));
                continue;
            }
            this.addItem(new GuiItem(ItemFact.a(Material.STONE, ChatColor.YELLOW + player.getName(), new String[0]), this.getAction(player)));
        }
    }

    private GuiAction<InventoryClickEvent> getAction(Player player) {
        return inventoryClickEvent -> {
            Main.DEBUG("Opend add whitelist");
            inventoryClickEvent.getWhoClicked().sendMessage(Main.prefix + MessagesConfig.MESSAGE_ADD_PLAYER_WHITELIST.replace("%name%", player.getName()));
            if (!this.ve.allowUserDriver(player.getUniqueId())) {
                this.ve.addToWhitelist(player.getUniqueId());
            }
            this.overview.open();
            Main.DEBUG("Added to whitelist");
        };
    }
}

