package com.nisovin.shopkeepers.shopkeeper.player.sell;

import com.nisovin.shopkeepers.shopkeeper.player.PlayerShopTradingViewProvider;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SellingPlayerShopTradingViewProvider extends PlayerShopTradingViewProvider {
   protected SellingPlayerShopTradingViewProvider(SKSellingPlayerShopkeeper shopkeeper) {
      super(shopkeeper);
   }

   public SKSellingPlayerShopkeeper getShopkeeper() {
      return (SKSellingPlayerShopkeeper)super.getShopkeeper();
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new SellingPlayerShopTradingView(this, player, uiState);
   }
}
