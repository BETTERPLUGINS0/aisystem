/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.inventories.vehicles.shop;

import java.util.Map;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.inventories.vehicles.shop.VehicleShopBuyConfirmationGUI;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.inventories.PaginationInventory;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class VehicleShopSelectGarageGUI
extends PaginationInventory {
    public VehicleShopSelectGarageGUI(Player player, VehicleModel vehicleModel) {
        super(5, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_SHOP_GARAGE_TITLE));
        for (Garage garage : VehiclesPlusAPI.getGarages((OfflinePlayer)player)) {
            int n = VehiclesPlus.getStorage().getConfig().getLimits().getHave();
            if (n != -1 && VehiclesPlusAPI.getVehicles().size() >= n) {
                this.addItem(ClickableItem.empty(new ItemBuilder(XMaterial.BARRIER.parseItem()).displayname(ColorUtil.__(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_SHOP_GARAGE_ITEM_NAME, (Map<String, String>)Map.of((Object)"%garage%", (Object)garage.getName())))).lore(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_SHOP_GARAGE_ITEM_LORE_LIMITED)).getItemStack()));
                continue;
            }
            this.addItem(ClickableItem.of(new ItemBuilder(XMaterial.CHEST.parseItem()).displayname(ColorUtil.__(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_SHOP_GARAGE_ITEM_NAME, (Map<String, String>)Map.of((Object)"%garage%", (Object)garage.getName())))).lore(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_SHOP_GARAGE_ITEM_LORE_AVAILABLE, (Map<String, String>)Map.of((Object)"%owner%", (Object)garage.getOwner().getName()))).getItemStack(), inventoryClickEvent -> new VehicleShopBuyConfirmationGUI(player, vehicleModel, garage)));
        }
        this.open(player);
    }
}

