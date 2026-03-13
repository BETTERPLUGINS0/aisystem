package com.nisovin.shopkeepers.shopkeeper.player.book;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.shopkeeper.offers.BookOffer;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.player.PlayerShopTradingView;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.trading.Trade;
import com.nisovin.shopkeepers.ui.trading.TradingContext;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.BookItems;
import com.nisovin.shopkeepers.util.inventory.InventoryUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.function.Predicate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BookPlayerShopTradingView extends PlayerShopTradingView {
   private static final Predicate<ItemStack> WRITABLE_BOOK_MATCHER;
   @Nullable
   private BookOffer currentOffer = null;

   protected BookPlayerShopTradingView(BookPlayerShopTradingViewProvider provider, Player player, UIState uiState) {
      super(provider, player, uiState);
   }

   public SKBookPlayerShopkeeper getShopkeeperNonNull() {
      return (SKBookPlayerShopkeeper)super.getShopkeeperNonNull();
   }

   protected boolean prepareTrade(Trade trade) {
      if (!super.prepareTrade(trade)) {
         return false;
      } else {
         SKBookPlayerShopkeeper shopkeeper = this.getShopkeeperNonNull();
         Player tradingPlayer = trade.getTradingPlayer();
         TradingRecipe tradingRecipe = trade.getTradingRecipe();
         UnmodifiableItemStack bookItem = tradingRecipe.getResultItem();
         BookMeta bookMeta = BookItems.getBookMeta(bookItem);
         if (bookMeta != null && BookItems.isCopy(bookMeta)) {
            String bookTitle = BookItems.getTitle(bookMeta);
            if (bookTitle == null) {
               TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeUnexpectedTrade);
               this.debugPreventedTrade("Could not determine the book title of the traded item!");
               return false;
            } else {
               BookOffer offer = shopkeeper.getOffer(bookTitle);
               if (offer == null) {
                  TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeUnexpectedTrade);
                  this.debugPreventedTrade("Could not find the offer corresponding to the trading recipe!");
                  return false;
               } else {
                  this.currentOffer = offer;
                  return true;
               }
            }
         } else {
            TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeUnexpectedTrade);
            this.debugPreventedTrade("The traded item is no valid book copy!");
            return false;
         }
      }
   }

   protected boolean finalTradePreparation(Trade trade) {
      if (!super.finalTradePreparation(trade)) {
         return false;
      } else {
         Player tradingPlayer = trade.getTradingPlayer();
         BookOffer offer = (BookOffer)Unsafe.assertNonNull(this.currentOffer);
         ItemStack[] newContainerContents = (ItemStack[])Unsafe.assertNonNull(this.newContainerContents);
         if (InventoryUtils.removeItems(newContainerContents, (Predicate)WRITABLE_BOOK_MATCHER, 1) != 0) {
            TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeInsufficientWritableBooks);
            this.debugPreventedTrade("The shop's container does not contain any writable (book-and-quill) items.");
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

   static {
      WRITABLE_BOOK_MATCHER = ItemUtils.itemsOfType(Material.WRITABLE_BOOK);
   }
}
