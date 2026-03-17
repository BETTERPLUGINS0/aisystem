/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.inventories.fuel;

import java.util.Map;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.vehicles.fuel.FuelType;
import nl.sbdeveloper.vehiclesplus.inventories.fuel.FuelAmountGUI;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.MainUtil;
import nl.sbdeveloper.vehiclesplus.utils.inventories.PaginationInventory;
import org.bukkit.entity.Player;

public class FuelGUI
extends PaginationInventory {
    public FuelGUI(Player player) {
        super(5, Locale.getMessage(PluginMessage.INVENTORIES_FUEL_SHOP_TITLE));
        for (FuelType fuelType : VehiclesPlusAPI.getFuelTypes().values()) {
            this.addItem(ClickableItem.of(new ItemBuilder(fuelType.getFuel()).displayname(ColorUtil.__(Locale.getMessage(PluginMessage.INVENTORIES_FUEL_SHOP_ITEM_NAME, (Map<String, String>)Map.of((Object)"%type%", (Object)fuelType.getName())))).lore(ColorUtil.__(Locale.getMessage(PluginMessage.INVENTORIES_FUEL_SHOP_ITEM_LORE, (Map<String, String>)Map.of((Object)"%price%", (Object)MainUtil.___(fuelType.getPricePerLiter()))))).getItemStack(), inventoryClickEvent -> new FuelAmountGUI(player, fuelType)));
        }
        this.open(player);
    }
}

