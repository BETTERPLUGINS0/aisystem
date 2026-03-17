package advancedplugins.pm2.cv.vehicle.storage;

import advancedplugins.pm2.cv.api.util.inventory.ItemStackUtil;
import advancedplugins.pm2.cv.api.vehicle.item.storage.VehicleItemHolder;
import advancedplugins.pm2.cv.handler.PluginHandlerAdapter;
import advancedplugins.pm2.cv.handler.PluginHandlerOptions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

@PluginHandlerOptions(
   eventListener = true,
   packetInjector = false
)
public class VehicleStorageListener extends PluginHandlerAdapter {
   @EventHandler
   public void onPlayerInventoryClick(InventoryClickEvent event) {
      ItemStack var2 = var1.getCursor();
      ItemStack var3 = var1.getCurrentItem();
      ItemStack var4 = var1.getHotbarButton() == -1 ? null : var1.getWhoClicked().getInventory().getItem(var1.getHotbarButton());
      if (ItemStackUtil.hasNamespacedKey(var2, VehicleItemHolder.BLOCKING_ITEM_NAMESPACE, PersistentDataType.STRING)) {
         var1.setCancelled(true);
         var1.setResult(Result.DENY);
      }

      if (ItemStackUtil.hasNamespacedKey(var3, VehicleItemHolder.BLOCKING_ITEM_NAMESPACE, PersistentDataType.STRING)) {
         var1.setCancelled(true);
         var1.setResult(Result.DENY);
      }

      if (ItemStackUtil.hasNamespacedKey(var4, VehicleItemHolder.BLOCKING_ITEM_NAMESPACE, PersistentDataType.STRING)) {
         var1.setCancelled(true);
         var1.setResult(Result.DENY);
      }

   }
}
