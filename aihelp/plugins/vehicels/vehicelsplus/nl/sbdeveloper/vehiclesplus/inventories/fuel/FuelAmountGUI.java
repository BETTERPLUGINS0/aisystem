/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.inventories.fuel;

import java.util.List;
import java.util.Map;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.vehicles.fuel.FuelType;
import nl.sbdeveloper.vehiclesplus.handlers.EconomyAdapter;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.MainUtil;
import nl.sbdeveloper.vehiclesplus.utils.inventories.PaginationInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FuelAmountGUI
extends PaginationInventory {
    public FuelAmountGUI(Player player, FuelType fuelType) {
        super(5, Locale.getMessage(PluginMessage.INVENTORIES_FUEL_AMOUNT_TITLE));
        List<Integer> list = VehiclesPlus.getStorage().getConfig().getFuelGUIAmounts();
        for (int n : list) {
            this.addItem(ClickableItem.of(new ItemBuilder(fuelType.getFuel().clone()).displayname(ColorUtil.__(Locale.getMessage(PluginMessage.INVENTORIES_FUEL_AMOUNT_ITEM_NAME, (Map<String, String>)Map.of((Object)"%type%", (Object)fuelType.getName(), (Object)"%amount%", (Object)String.valueOf(n))))).lore(ColorUtil.__(Locale.getMessage(PluginMessage.INVENTORIES_FUEL_AMOUNT_ITEM_LORE, (Map<String, String>)Map.of((Object)"%price%", (Object)MainUtil.___(fuelType.getPricePerLiter() * (double)n))))).getItemStack(), inventoryClickEvent -> {
                if (!EconomyAdapter.withdraw(player, fuelType.getPricePerLiter() * (double)n)) {
                    return;
                }
                player.getInventory().addItem(new ItemStack[]{fuelType.getFuel(n)});
                player.sendMessage(Locale.getMessage(PluginMessage.INVENTORIES_FUEL_BOUGHT, (Map<String, String>)Map.of((Object)"%liters%", (Object)String.valueOf(n), (Object)"%type%", (Object)fuelType.getName())));
                this.close(player);
            }));
        }
        this.open(player);
    }
}

