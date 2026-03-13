package com.nisovin.shopkeepers.ui.hiring;

import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class HiringView extends View {
   protected HiringView(HiringViewProvider provider, Player player, UIState uiState) {
      super(provider, player, uiState);
   }

   protected void onInventoryClose(@Nullable InventoryCloseEvent closeEvent) {
   }

   protected void onInventoryClickEarly(InventoryClickEvent event) {
      event.setCancelled(true);
   }

   protected void onInventoryDragEarly(InventoryDragEvent event) {
      event.setCancelled(true);
   }
}
