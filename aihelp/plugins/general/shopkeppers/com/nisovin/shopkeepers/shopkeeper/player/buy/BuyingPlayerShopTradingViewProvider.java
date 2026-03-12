package com.nisovin.shopkeepers.shopkeeper.player.buy;

import com.nisovin.shopkeepers.shopkeeper.player.PlayerShopTradingViewProvider;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BuyingPlayerShopTradingViewProvider extends PlayerShopTradingViewProvider {
   protected BuyingPlayerShopTradingViewProvider(SKBuyingPlayerShopkeeper shopkeeper) {
      super(shopkeeper);
   }

   public SKBuyingPlayerShopkeeper getShopkeeper() {
      return (SKBuyingPlayerShopkeeper)super.getShopkeeper();
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new BuyingPlayerShopTradingView(this, player, uiState);
   }
}
