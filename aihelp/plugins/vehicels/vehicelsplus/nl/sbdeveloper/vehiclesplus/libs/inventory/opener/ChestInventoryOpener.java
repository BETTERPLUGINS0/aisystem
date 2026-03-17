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

import com.google.common.base.Preconditions;
import nl.sbdeveloper.vehiclesplus.libs.inventory.InventoryManager;
import nl.sbdeveloper.vehiclesplus.libs.inventory.SmartInventory;
import nl.sbdeveloper.vehiclesplus.libs.inventory.opener.InventoryOpener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ChestInventoryOpener
implements InventoryOpener {
    @Override
    public Inventory open(SmartInventory smartInventory, Player player) {
        Preconditions.checkArgument(smartInventory.getColumns() == 9, "The column count for the chest inventory must be 9, found: %s.", new Object[]{smartInventory.getColumns()});
        Preconditions.checkArgument(smartInventory.getRows() >= 1 && smartInventory.getRows() <= 6, "The row count for the chest inventory must be between 1 and 6, found: %s", new Object[]{smartInventory.getRows()});
        InventoryManager inventoryManager = smartInventory.getManager();
        Inventory inventory = Bukkit.createInventory((InventoryHolder)player, (int)(smartInventory.getRows() * smartInventory.getColumns()), (String)smartInventory.getTitle());
        this.fill(inventory, inventoryManager.getContents(player).get());
        player.openInventory(inventory);
        return inventory;
    }

    @Override
    public boolean supports(InventoryType inventoryType) {
        return inventoryType == InventoryType.CHEST || inventoryType == InventoryType.ENDER_CHEST;
    }
}

