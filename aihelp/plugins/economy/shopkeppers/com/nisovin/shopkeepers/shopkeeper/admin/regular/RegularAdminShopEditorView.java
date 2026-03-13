package com.nisovin.shopkeepers.shopkeeper.admin.regular;

import com.nisovin.shopkeepers.shopkeeper.TradingRecipeDraft;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperEditorView;
import com.nisovin.shopkeepers.ui.lib.UIState;
import org.bukkit.entity.Player;

public class RegularAdminShopEditorView extends ShopkeeperEditorView {
   protected RegularAdminShopEditorView(RegularAdminShopEditorViewProvider viewProvider, Player player, UIState uiState) {
      super(viewProvider, player, uiState);
   }

   protected TradingRecipeDraft getEmptyTrade() {
      return TradingRecipeDraft.EMPTY;
   }

   protected TradingRecipeDraft getEmptyTradeSlotItems() {
      return TradingRecipeDraft.EMPTY;
   }
}
