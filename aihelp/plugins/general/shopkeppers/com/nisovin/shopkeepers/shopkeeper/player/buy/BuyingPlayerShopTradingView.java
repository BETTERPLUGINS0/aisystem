package com.nisovin.shopkeepers.shopkeeper.player.buy;

import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.shopkeeper.offers.PriceOffer;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.currency.Currencies;
import com.nisovin.shopkeepers.currency.Currency;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.player.PlayerShopTradingView;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.trading.Trade;
import com.nisovin.shopkeepers.ui.trading.TradingContext;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BuyingPlayerShopTradingView extends PlayerShopTradingView {
   @Nullable
   private PriceOffer currentOffer = null;

   protected BuyingPlayerShopTradingView(BuyingPlayerShopTradingViewProvider provider, Player player, UIState uiState) {
      super(provider, player, uiState);
   }

   public SKBuyingPlayerShopkeeper getShopkeeperNonNull() {
      return (SKBuyingPlayerShopkeeper)super.getShopkeeperNonNull();
   }

   protected boolean prepareTrade(Trade trade) {
      if (!super.prepareTrade(trade)) {
         return false;
      } else {
         SKBuyingPlayerShopkeeper shopkeeper = this.getShopkeeperNonNull();
         Player tradingPlayer = trade.getTradingPlayer();
         TradingRecipe tradingRecipe = trade.getTradingRecipe();
         UnmodifiableItemStack boughtItem = tradingRecipe.getItem1();
         PriceOffer offer = shopkeeper.getOffer(boughtItem);
         if (offer == null) {
            TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeUnexpectedTrade);
            this.debugPreventedTrade("Could not find the offer corresponding to the trading recipe!");
            return false;
         } else {
            int expectedBoughtItemAmount = offer.getItem().getAmount();
            if (expectedBoughtItemAmount != boughtItem.getAmount()) {
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
         PriceOffer offer = (PriceOffer)Unsafe.assertNonNull(this.currentOffer);
         ItemStack[] newContainerContents = (ItemStack[])Unsafe.assertNonNull(this.newContainerContents);
         int remaining = this.removeCurrency(newContainerContents, offer.getPrice());
         if (remaining > 0) {
            TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeInsufficientCurrency);
            this.debugPreventedTrade("The shop's container does not contain enough currency.");
            return false;
         } else if (remaining < 0) {
            TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeInsufficientStorageSpace);
            this.debugPreventedTrade("The shop's container does not have enough space to split large currency items.");
            return false;
         } else {
            ShopkeeperTradeEvent tradeEvent = trade.getTradeEvent();
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

   protected void onTradeOver(TradingContext tradingContext) {
      super.onTradeOver(tradingContext);
      this.currentOffer = null;
   }

   protected int removeCurrency(ItemStack[] contents, int amount) {
      Validate.notNull(contents, (String)"contents is null");
      Validate.isTrue(amount >= 0, "amount cannot be negative");
      if (amount == 0) {
         return 0;
      } else {
         int remaining = amount;
         Currency baseCurrency = Currencies.getBase();

         int remainingHigh;
         int slot;
         for(int k = 0; k < 2; ++k) {
            for(remainingHigh = 0; remainingHigh < contents.length; ++remainingHigh) {
               ItemStack itemStack = contents[remainingHigh];
               if (baseCurrency.getItemData().matches(itemStack)) {
                  assert itemStack != null;

                  slot = itemStack.getAmount();
                  if (k == 1 || slot < itemStack.getMaxStackSize()) {
                     int newAmount = slot - remaining;
                     if (newAmount > 0) {
                        itemStack = itemStack.clone();
                        contents[remainingHigh] = itemStack;
                        itemStack.setAmount(newAmount);
                        remaining = 0;
                        break;
                     }

                     contents[remainingHigh] = null;
                     remaining = -newAmount;
                     if (newAmount == 0) {
                        break;
                     }
                  }
               }
            }

            if (remaining == 0) {
               break;
            }
         }

         if (remaining == 0) {
            return 0;
         } else if (!Currencies.isHighCurrencyEnabled()) {
            return remaining;
         } else {
            Currency highCurrency = Currencies.getHigh();
            remainingHigh = (int)Math.ceil((double)remaining / (double)highCurrency.getValue());
            remaining -= remainingHigh * highCurrency.getValue();

            assert remaining <= 0;

            int stackSize;
            int maxStackSize;
            ItemStack itemStack;
            for(maxStackSize = 0; maxStackSize < 2; ++maxStackSize) {
               for(slot = 0; slot < contents.length; ++slot) {
                  itemStack = contents[slot];
                  if (highCurrency.getItemData().matches(itemStack)) {
                     assert itemStack != null;

                     stackSize = itemStack.getAmount();
                     if (maxStackSize == 1 || stackSize < itemStack.getMaxStackSize()) {
                        int newAmount = stackSize - remainingHigh;
                        if (newAmount > 0) {
                           itemStack = itemStack.clone();
                           contents[slot] = itemStack;
                           itemStack.setAmount(newAmount);
                           remainingHigh = 0;
                           break;
                        }

                        contents[slot] = null;
                        remainingHigh = -newAmount;
                        if (newAmount == 0) {
                           break;
                        }
                     }
                  }
               }

               if (remainingHigh == 0) {
                  break;
               }
            }

            remaining += remainingHigh * highCurrency.getValue();
            if (remaining >= 0) {
               return remaining;
            } else {
               assert remaining < 0;

               remaining = -remaining;
               maxStackSize = baseCurrency.getMaxStackSize();

               for(slot = 0; slot < contents.length; ++slot) {
                  itemStack = contents[slot];
                  if (ItemUtils.isEmpty(itemStack)) {
                     stackSize = Math.min(remaining, maxStackSize);
                     contents[slot] = baseCurrency.getItemData().createItemStack(stackSize);
                     remaining -= stackSize;
                     if (remaining == 0) {
                        break;
                     }
                  }
               }

               remaining = -remaining;
               return remaining;
            }
         }
      }
   }
}
