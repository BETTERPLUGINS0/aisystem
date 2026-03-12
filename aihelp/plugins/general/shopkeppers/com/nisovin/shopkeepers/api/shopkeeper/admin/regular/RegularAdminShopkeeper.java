package com.nisovin.shopkeepers.api.shopkeeper.admin.regular;

import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.offers.TradeOffer;
import java.util.List;

public interface RegularAdminShopkeeper extends AdminShopkeeper {
   List<? extends TradeOffer> getOffers();

   void clearOffers();

   void setOffers(List<? extends TradeOffer> var1);

   void addOffer(TradeOffer var1);

   void addOffers(List<? extends TradeOffer> var1);
}
