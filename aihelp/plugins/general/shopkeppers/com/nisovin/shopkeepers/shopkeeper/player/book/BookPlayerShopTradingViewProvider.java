package com.nisovin.shopkeepers.shopkeeper.player.book;

import com.nisovin.shopkeepers.shopkeeper.player.PlayerShopTradingViewProvider;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BookPlayerShopTradingViewProvider extends PlayerShopTradingViewProvider {
   protected BookPlayerShopTradingViewProvider(SKBookPlayerShopkeeper shopkeeper) {
      super(shopkeeper);
   }

   public SKBookPlayerShopkeeper getShopkeeper() {
      return (SKBookPlayerShopkeeper)super.getShopkeeper();
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new BookPlayerShopTradingView(this, player, uiState);
   }
}
