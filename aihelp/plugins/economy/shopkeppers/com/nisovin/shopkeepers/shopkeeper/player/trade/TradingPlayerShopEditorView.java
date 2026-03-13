package com.nisovin.shopkeepers.shopkeeper.player.trade;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.shopkeeper.TradingRecipeDraft;
import com.nisovin.shopkeepers.shopkeeper.player.PlaceholderItems;
import com.nisovin.shopkeepers.shopkeeper.player.PlayerShopEditorView;
import com.nisovin.shopkeepers.ui.UIHelpers;
import com.nisovin.shopkeepers.ui.editor.EditorLayout;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.util.inventory.InventoryViewUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class TradingPlayerShopEditorView extends PlayerShopEditorView {
   protected TradingPlayerShopEditorView(TradingPlayerShopEditorViewProvider viewProvider, Player player, UIState uiState) {
      super(viewProvider, player, uiState);
   }

   protected TradingRecipeDraft getEmptyTrade() {
      return Settings.DerivedSettings.tradingEmptyTrade;
   }

   protected TradingRecipeDraft getEmptyTradeSlotItems() {
      return Settings.DerivedSettings.tradingEmptyTradeSlotItems;
   }

   protected void handlePlayerInventoryClick(InventoryClickEvent event) {
      if (!event.isShiftClick()) {
         UIHelpers.swapCursor(event.getView(), event.getRawSlot());
      }
   }

   protected void handleTradesClick(InventoryClickEvent event) {
      EditorLayout layout = this.getLayout();
      int rawSlot = event.getRawSlot();

      assert layout.isTradesArea(rawSlot);

      Inventory inventory = event.getInventory();
      ItemStack cursor = event.getCursor();
      if (!ItemUtils.isEmpty(cursor)) {
         ItemStack cursorClone = ItemUtils.copySingleItem((ItemStack)Unsafe.assertNonNull(cursor));
         this.placeCursorInTrades(event.getView(), rawSlot, cursorClone);
      } else {
         int tradeColumn = layout.getTradeColumn(rawSlot);
         if (this.isEmptyTrade(inventory, tradeColumn)) {
            return;
         }

         int minAmount = 0;
         UnmodifiableItemStack emptySlotItem;
         if (layout.isResultRow(rawSlot)) {
            minAmount = 1;
            emptySlotItem = this.getEmptyTradeSlotItems().getResultItem();
         } else if (layout.isItem1Row(rawSlot)) {
            emptySlotItem = this.getEmptyTradeSlotItems().getItem1();
         } else {
            assert layout.isItem2Row(rawSlot);

            emptySlotItem = this.getEmptyTradeSlotItems().getItem2();
         }

         ItemStack newItem = this.updateItemAmountOnClick(event, minAmount, emptySlotItem);
         if (newItem == null) {
            this.updateTradeColumn(inventory, tradeColumn);
         }
      }

   }

   private void placeCursorInTrades(InventoryView view, int rawSlot, ItemStack cursorClone) {
      assert !ItemUtils.isEmpty(cursorClone);

      cursorClone.setAmount(1);
      ItemStack cursorFinal = PlaceholderItems.replace(cursorClone);
      Bukkit.getScheduler().runTask(ShopkeepersPlugin.getInstance(), () -> {
         if (view.getPlayer().getOpenInventory() == view) {
            Inventory inventory = view.getTopInventory();
            inventory.setItem(rawSlot, cursorFinal);
            EditorLayout layout = this.getLayout();
            this.updateTradeColumn(inventory, layout.getTradeColumn(rawSlot));
         }
      });
   }

   protected void onInventoryDragEarly(InventoryDragEvent event) {
      event.setCancelled(true);
      ItemStack cursorClone = event.getOldCursor();
      if (!ItemUtils.isEmpty(cursorClone)) {
         assert cursorClone != null;

         Set<Integer> rawSlots = event.getRawSlots();
         if (rawSlots.size() == 1) {
            InventoryView view = event.getView();
            int rawSlot = (Integer)rawSlots.iterator().next();
            EditorLayout layout = this.getLayout();
            if (layout.isTradesArea(rawSlot)) {
               this.placeCursorInTrades(view, rawSlot, cursorClone);
            } else if (InventoryViewUtils.isPlayerInventory(view, rawSlot)) {
               UIHelpers.swapCursorDelayed(view, rawSlot);
            }

         }
      }
   }
}
