package com.nisovin.shopkeepers.api.trading;

import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;

public interface TradeEffect {
   void onTradeAborted(ShopkeeperTradeEvent var1);

   void onTradeApplied(ShopkeeperTradeEvent var1);
}
