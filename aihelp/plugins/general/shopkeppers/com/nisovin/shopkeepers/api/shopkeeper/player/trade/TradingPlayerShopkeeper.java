package com.nisovin.shopkeepers.api.shopkeeper.player.trade;

import com.nisovin.shopkeepers.api.shopkeeper.offers.TradeOffer;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import java.util.List;

public interface TradingPlayerShopkeeper extends PlayerShopkeeper {
   List<? extends TradeOffer> getOffers();

   void clearOffers();

   void setOffers(List<? extends TradeOffer> var1);

   void addOffer(TradeOffer var1);

   void addOffers(List<? extends TradeOffer> var1);
}
