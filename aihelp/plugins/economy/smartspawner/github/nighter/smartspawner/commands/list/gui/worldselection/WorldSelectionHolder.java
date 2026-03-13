package github.nighter.smartspawner.commands.list.gui.worldselection;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class WorldSelectionHolder implements InventoryHolder {
   private final String targetServer;

   public WorldSelectionHolder() {
      this.targetServer = null;
   }

   public WorldSelectionHolder(String targetServer) {
      this.targetServer = targetServer;
   }

   public String getTargetServer() {
      return this.targetServer;
   }

   public boolean isRemoteServer() {
      return this.targetServer != null;
   }

   public Inventory getInventory() {
      return null;
   }
}
