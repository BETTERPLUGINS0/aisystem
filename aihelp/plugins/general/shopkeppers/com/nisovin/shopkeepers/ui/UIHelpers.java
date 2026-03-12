package com.nisovin.shopkeepers.ui;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public final class UIHelpers {
   public static void swapCursorDelayed(InventoryView view, int rawSlot) {
      Bukkit.getScheduler().runTask(ShopkeepersPlugin.getInstance(), () -> {
         if (view.getPlayer().getOpenInventory() == view) {
            swapCursor(view, rawSlot);
         }
      });
   }

   public static void swapCursor(InventoryView view, int rawSlot) {
      ItemStack cursor = ItemUtils.getNullIfEmpty(view.getCursor());
      ItemStack current = ItemUtils.getNullIfEmpty(view.getItem(rawSlot));
      view.setItem(rawSlot, cursor);
      view.setCursor(current);
   }

   private UIHelpers() {
   }
}
