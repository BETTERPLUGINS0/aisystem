package com.nisovin.shopkeepers.container.protection;

import com.nisovin.shopkeepers.container.ShopContainers;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;

class InventoryMoveItemListener implements Listener {
   private final ProtectedContainers protectedContainers;

   InventoryMoveItemListener(ProtectedContainers protectedContainers) {
      this.protectedContainers = protectedContainers;
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onInventoryMoveItem(InventoryMoveItemEvent event) {
      assert event.getSource() != null && event.getDestination() != null;

      if (this.isProtectedInventory(event.getSource()) || this.isProtectedInventory(event.getDestination())) {
         event.setCancelled(true);
      }

   }

   private boolean isProtectedInventory(Inventory inventory) {
      assert inventory != null;

      Location inventoryLocation = inventory.getLocation();
      if (inventoryLocation == null) {
         return false;
      } else {
         Block block = inventoryLocation.getBlock();
         return !ShopContainers.isSupportedContainer(block.getType()) ? false : this.protectedContainers.isContainerProtected(block, (Player)null);
      }
   }
}
