package com.nisovin.shopkeepers.ui.confirmations;

import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import com.nisovin.shopkeepers.ui.lib.ViewProvider;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ConfirmationView extends View {
   private static final int INVENTORY_SIZE = 9;
   private static final int SLOT_CONFIRM = 0;
   private static final int SLOT_CANCEL = 8;
   private boolean playerDecided = false;

   ConfirmationView(ViewProvider provider, Player player, UIState uiState) {
      super(provider, player, uiState);
   }

   public boolean isAcceptedState(UIState uiState) {
      return uiState instanceof ConfirmationUIState;
   }

   private ConfirmationUIState getConfig() {
      return (ConfirmationUIState)this.getInitialUIState();
   }

   @Nullable
   protected InventoryView openInventoryView() {
      ConfirmationUIState config = this.getConfig();
      Inventory inventory = Bukkit.createInventory((InventoryHolder)null, 9, config.getTitle());
      this.updateInventory(inventory);
      Player player = this.getPlayer();
      return player.openInventory(inventory);
   }

   private void updateInventory(Inventory inventory) {
      ConfirmationUIState config = this.getConfig();
      ItemStack confirmItem = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
      confirmItem = ItemUtils.setDisplayNameAndLore(confirmItem, Messages.confirmationUiConfirm, config.getConfirmationLore());
      inventory.setItem(0, confirmItem);
      ItemStack cancelItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
      cancelItem = ItemUtils.setDisplayNameAndLore(cancelItem, Messages.confirmationUiCancel, Messages.confirmationUiCancelLore);
      inventory.setItem(8, cancelItem);
   }

   public void updateInventory() {
      Inventory inventory = this.getInventory();
      this.updateInventory(inventory);
      this.syncInventory();
   }

   protected void onInventoryClickEarly(InventoryClickEvent event) {
      event.setCancelled(true);
      if (!this.isAutomaticShiftLeftClick()) {
         ConfirmationUIState config = this.getConfig();
         int slot = event.getRawSlot();
         if (slot == 0) {
            this.playerDecided = true;
            this.closeDelayedAndRunTask(config.getAction());
         } else if (slot == 8) {
            this.playerDecided = true;
            this.closeDelayedAndRunTask(config.getOnCancelled());
         }

      }
   }

   protected void onInventoryDragEarly(InventoryDragEvent event) {
      event.setCancelled(true);
   }

   protected void onInventoryClose(@Nullable InventoryCloseEvent closeEvent) {
      if (!this.playerDecided) {
         Player player = this.getPlayer();
         TextUtils.sendMessage(player, (Text)Messages.confirmationUiAborted);
      }

   }
}
