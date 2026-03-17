/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 */
package nl.sbdeveloper.vehiclesplus.libs.inventory.opener;

import com.google.common.collect.ImmutableList;
import java.util.List;
import nl.sbdeveloper.vehiclesplus.libs.inventory.InventoryManager;
import nl.sbdeveloper.vehiclesplus.libs.inventory.SmartInventory;
import nl.sbdeveloper.vehiclesplus.libs.inventory.opener.InventoryOpener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class SpecialInventoryOpener
implements InventoryOpener {
    private static final List<InventoryType> SUPPORTED = ImmutableList.of(InventoryType.FURNACE, InventoryType.WORKBENCH, InventoryType.DISPENSER, InventoryType.DROPPER, InventoryType.ENCHANTING, InventoryType.BREWING, InventoryType.ANVIL, InventoryType.BEACON, InventoryType.HOPPER);

    @Override
    public Inventory open(SmartInventory smartInventory, Player player) {
        InventoryManager inventoryManager = smartInventory.getManager();
        Inventory inventory = Bukkit.createInventory((InventoryHolder)player, (InventoryType)smartInventory.getType(), (String)smartInventory.getTitle());
        this.fill(inventory, inventoryManager.getContents(player).get());
        player.openInventory(inventory);
        return inventory;
    }

    @Override
    public boolean supports(InventoryType inventoryType) {
        return SUPPORTED.contains(inventoryType);
    }
}

