package com.nisovin.shopkeepers.shopkeeper.player.trade;

import com.nisovin.shopkeepers.shopkeeper.player.PlayerShopTradingViewProvider;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TradingPlayerShopTradingViewProvider extends PlayerShopTradingViewProvider {
   protected TradingPlayerShopTradingViewProvider(SKTradingPlayerShopkeeper shopkeeper) {
      super(shopkeeper);
   }

   public SKTradingPlayerShopkeeper getShopkeeper() {
      return (SKTradingPlayerShopkeeper)super.getShopkeeper();
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new TradingPlayerShopTradingView(this, player, uiState);
   }
}
