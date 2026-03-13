package com.nisovin.shopkeepers.shopkeeper.player.sell;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.shopkeeper.offers.PriceOffer;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.player.PlayerShopTradingView;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.trading.Trade;
import com.nisovin.shopkeepers.ui.trading.TradingContext;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SellingPlayerShopTradingView extends PlayerShopTradingView {
   @Nullable
   private PriceOffer currentOffer = null;

   protected SellingPlayerShopTradingView(SellingPlayerShopTradingViewProvider provider, Player player, UIState uiState) {
      super(provider, player, uiState);
   }

   public SKSellingPlayerShopkeeper getShopkeeperNonNull() {
      return (SKSellingPlayerShopkeeper)super.getShopkeeperNonNull();
   }

   protected boolean prepareTrade(Trade trade) {
      if (!super.prepareTrade(trade)) {
         return false;
      } else {
         SKSellingPlayerShopkeeper shopkeeper = this.getShopkeeperNonNull();
         Player tradingPlayer = trade.getTradingPlayer();
         TradingRecipe tradingRecipe = trade.getTradingRecipe();
         UnmodifiableItemStack soldItem = tradingRecipe.getResultItem();
         PriceOffer offer = shopkeeper.getOffer(soldItem);
         if (offer == null) {
            TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeUnexpectedTrade);
            this.debugPreventedTrade("Could not find the offer corresponding to the trading recipe!");
            return false;
         } else {
            int expectedSoldItemAmount = offer.getItem().getAmount();
            if (expectedSoldItemAmount != soldItem.getAmount()) {
               TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeUnexpectedTrade);
               this.debugPreventedTrade("The offer does not match the trading recipe!");
               return false;
            } else {
               this.currentOffer = offer;
               return true;
            }
         }
      }
   }

   protected boolean finalTradePreparation(Trade trade) {
      if (!super.finalTradePreparation(trade)) {
         return false;
      } else {
         Player tradingPlayer = trade.getTradingPlayer();
         TradingRecipe tradingRecipe = trade.getTradingRecipe();
         PriceOffer offer = (PriceOffer)Unsafe.assertNonNull(this.currentOffer);
         ItemStack[] newContainerContents = (ItemStack[])Unsafe.assertNonNull(this.newContainerContents);
         UnmodifiableItemStack soldItem = tradingRecipe.getResultItem();
         if (InventoryUtils.removeItems(newContainerContents, soldItem) != 0) {
            TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeInsufficientStock);
            this.debugPreventedTrade("The shop's container does not contain the required items.");
            return false;
         } else {
            int amountAfterTaxes = this.getAmountAfterTaxes(offer.getPrice());
            if (this.addCurrencyItems(newContainerContents, amountAfterTaxes) != 0) {
               TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeInsufficientStorageSpace);
               this.debugPreventedTrade("The shop's container cannot hold the traded items.");
               return false;
            } else {
               return true;
            }
         }
      }
   }

   protected void onTradeOver(TradingContext tradingContext) {
      super.onTradeOver(tradingContext);
      this.currentOffer = null;
   }
}
