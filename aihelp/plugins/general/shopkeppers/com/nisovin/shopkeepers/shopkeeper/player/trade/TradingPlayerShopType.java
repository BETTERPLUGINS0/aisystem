package com.nisovin.shopkeepers.shopkeeper.player.trade;

import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.player.AbstractPlayerShopType;
import java.util.Arrays;
import java.util.List;

public final class TradingPlayerShopType extends AbstractPlayerShopType<SKTradingPlayerShopkeeper> {
   public TradingPlayerShopType() {
      super("trade", Arrays.asList("trading"), "shopkeeper.player.trade", SKTradingPlayerShopkeeper.class);
   }

   public String getDisplayName() {
      return Messages.shopTypeTrading;
   }

   public String getDescription() {
      return Messages.shopTypeDescTrading;
   }

   public String getSetupDescription() {
      return Messages.shopSetupDescTrading;
   }

   public List<? extends String> getTradeSetupDescription() {
      return Messages.tradeSetupDescTrading;
   }

   protected SKTradingPlayerShopkeeper createNewShopkeeper() {
      return new SKTradingPlayerShopkeeper();
   }
}
