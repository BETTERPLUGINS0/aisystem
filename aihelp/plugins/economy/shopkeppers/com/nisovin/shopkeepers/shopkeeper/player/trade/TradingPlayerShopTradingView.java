package com.nisovin.shopkeepers.shopkeeper.player.trade;

import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.shopkeeper.offers.TradeOffer;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.player.PlayerShopTradingView;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.trading.Trade;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TradingPlayerShopTradingView extends PlayerShopTradingView {
   protected TradingPlayerShopTradingView(TradingPlayerShopTradingViewProvider provider, Player player, UIState uiState) {
      super(provider, player, uiState);
   }

   public SKTradingPlayerShopkeeper getShopkeeperNonNull() {
      return (SKTradingPlayerShopkeeper)super.getShopkeeperNonNull();
   }

   protected boolean prepareTrade(Trade trade) {
      if (!super.prepareTrade(trade)) {
         return false;
      } else {
         SKTradingPlayerShopkeeper shopkeeper = this.getShopkeeperNonNull();
         Player tradingPlayer = trade.getTradingPlayer();
         TradingRecipe tradingRecipe = trade.getTradingRecipe();
         TradeOffer offer = shopkeeper.getOffer(tradingRecipe);
         if (offer == null) {
            TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeUnexpectedTrade);
            this.debugPreventedTrade("Could not find the offer corresponding to the trading recipe!");
            return false;
         } else {
            return true;
         }
      }
   }

   protected boolean finalTradePreparation(Trade trade) {
      if (!super.finalTradePreparation(trade)) {
         return false;
      } else {
         Player tradingPlayer = trade.getTradingPlayer();
         TradingRecipe tradingRecipe = trade.getTradingRecipe();
         ItemStack[] newContainerContents = (ItemStack[])Unsafe.assertNonNull(this.newContainerContents);
         UnmodifiableItemStack resultItem = tradingRecipe.getResultItem();

         assert resultItem != null;

         if (InventoryUtils.removeItems(newContainerContents, resultItem) != 0) {
            TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeInsufficientStock);
            this.debugPreventedTrade("The shop's container does not contain the required items.");
            return false;
         } else {
            ShopkeeperTradeEvent tradeEvent = (ShopkeeperTradeEvent)Unsafe.assertNonNull(trade.getTradeEvent());
            UnmodifiableItemStack receivedItem1 = tradeEvent.getReceivedItem1();
            UnmodifiableItemStack receivedItem2 = tradeEvent.getReceivedItem2();
            if (this.addReceivedItem(newContainerContents, receivedItem1) == 0 && this.addReceivedItem(newContainerContents, receivedItem2) == 0) {
               return true;
            } else {
               TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeInsufficientStorageSpace);
               this.debugPreventedTrade("The shop's container cannot hold the received items.");
               return false;
            }
         }
      }
   }
}
