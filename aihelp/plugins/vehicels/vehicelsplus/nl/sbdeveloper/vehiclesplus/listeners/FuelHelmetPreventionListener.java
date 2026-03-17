/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockDispenseArmorEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryType$SlotType
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.listeners;

import nl.sbdeveloper.vehiclesplus.api.nbt.NBTDataType;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FuelHelmetPreventionListener
implements Listener {
    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        if (this.isFuelItem(playerInteractEvent.getItem())) {
            playerInteractEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        if ((this.isFuelItem(inventoryClickEvent.getCurrentItem()) || this.isFuelItem(inventoryClickEvent.getCursor())) && inventoryClickEvent.getSlotType() == InventoryType.SlotType.ARMOR) {
            inventoryClickEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onDispenseArmor(BlockDispenseArmorEvent blockDispenseArmorEvent) {
        if (this.isFuelItem(blockDispenseArmorEvent.getItem())) {
            blockDispenseArmorEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onArmorStandInteraction(PlayerInteractEvent playerInteractEvent) {
        if (this.isFuelItem(playerInteractEvent.getItem())) {
            playerInteractEvent.setCancelled(true);
        }
    }

    private boolean isFuelItem(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        if (itemStack.getType().isAir()) {
            return false;
        }
        NBTItem nBTItem = new NBTItem(itemStack);
        return nBTItem.hasTag(NBTDataType.FUEL_TYPE.name());
    }
}

