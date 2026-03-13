package com.nisovin.shopkeepers.shopkeeper.player.book;

import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.currency.Currencies;
import com.nisovin.shopkeepers.shopkeeper.TradingRecipeDraft;
import com.nisovin.shopkeepers.shopkeeper.player.PlayerShopEditorView;
import com.nisovin.shopkeepers.ui.editor.EditorLayout;
import com.nisovin.shopkeepers.ui.lib.UIState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BookPlayerShopEditorView extends PlayerShopEditorView {
   protected BookPlayerShopEditorView(BookPlayerShopEditorViewProvider viewProvider, Player player, UIState uiState) {
      super(viewProvider, player, uiState);
   }

   protected TradingRecipeDraft getEmptyTrade() {
      return Settings.DerivedSettings.bookEmptyTrade;
   }

   protected TradingRecipeDraft getEmptyTradeSlotItems() {
      return Settings.DerivedSettings.bookEmptyTradeSlotItems;
   }

   protected void handleTradesClick(InventoryClickEvent event) {
      EditorLayout layout = this.getLayout();

      assert layout.isTradesArea(event.getRawSlot());

      Inventory inventory = this.getInventory();
      int rawSlot = event.getRawSlot();
      ItemStack resultItem;
      UnmodifiableItemStack emptySlotItem;
      if (layout.isItem1Row(rawSlot)) {
         resultItem = this.getTradeResultItem(inventory, layout.getTradeColumn(rawSlot));
         if (resultItem == null) {
            return;
         }

         emptySlotItem = this.getEmptyTradeSlotItems().getItem1();
         this.updateTradeCostItemOnClick(event, Currencies.getBase(), emptySlotItem);
      } else if (layout.isItem2Row(rawSlot)) {
         resultItem = this.getTradeResultItem(inventory, layout.getTradeColumn(rawSlot));
         if (resultItem == null) {
            return;
         }

         emptySlotItem = this.getEmptyTradeSlotItems().getItem2();
         this.updateTradeCostItemOnClick(event, Currencies.getHighOrNull(), emptySlotItem);
      }

   }
}
