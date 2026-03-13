package com.nisovin.shopkeepers.shopkeeper.player.buy;

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

public class BuyingPlayerShopEditorView extends PlayerShopEditorView {
   protected BuyingPlayerShopEditorView(BuyingPlayerShopEditorViewProvider viewProvider, Player player, UIState uiState) {
      super(viewProvider, player, uiState);
   }

   protected TradingRecipeDraft getEmptyTrade() {
      return Settings.DerivedSettings.buyingEmptyTrade;
   }

   protected TradingRecipeDraft getEmptyTradeSlotItems() {
      return Settings.DerivedSettings.buyingEmptyTradeSlotItems;
   }

   protected void handleTradesClick(InventoryClickEvent event) {
      EditorLayout layout = this.getLayout();

      assert layout.isTradesArea(event.getRawSlot());

      Inventory inventory = this.getInventory();
      int rawSlot = event.getRawSlot();
      if (layout.isResultRow(rawSlot)) {
         ItemStack tradedItem = this.getTradeItem1(inventory, layout.getTradeColumn(rawSlot));
         if (tradedItem == null) {
            return;
         }

         UnmodifiableItemStack emptySlotItem = this.getEmptyTradeSlotItems().getResultItem();
         this.updateTradeCostItemOnClick(event, Currencies.getBase(), emptySlotItem);
      } else if (layout.isItem1Row(rawSlot)) {
         UnmodifiableItemStack emptySlotItem = this.getEmptyTradeSlotItems().getItem1();
         this.updateItemAmountOnClick(event, 1, emptySlotItem);
      }

   }
}
