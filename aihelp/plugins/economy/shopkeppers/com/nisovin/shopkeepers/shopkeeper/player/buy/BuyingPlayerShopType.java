package com.nisovin.shopkeepers.shopkeeper.player.buy;

import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.player.AbstractPlayerShopType;
import java.util.Arrays;
import java.util.List;

public final class BuyingPlayerShopType extends AbstractPlayerShopType<SKBuyingPlayerShopkeeper> {
   public BuyingPlayerShopType() {
      super("buy", Arrays.asList("buying"), "shopkeeper.player.buy", SKBuyingPlayerShopkeeper.class);
   }

   public String getDisplayName() {
      return Messages.shopTypeBuying;
   }

   public String getDescription() {
      return Messages.shopTypeDescBuying;
   }

   public String getSetupDescription() {
      return Messages.shopSetupDescBuying;
   }

   public List<? extends String> getTradeSetupDescription() {
      return Messages.tradeSetupDescBuying;
   }

   protected SKBuyingPlayerShopkeeper createNewShopkeeper() {
      return new SKBuyingPlayerShopkeeper();
   }
}
