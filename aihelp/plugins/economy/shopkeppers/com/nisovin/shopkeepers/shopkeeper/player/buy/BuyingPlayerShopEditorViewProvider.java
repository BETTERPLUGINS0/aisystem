package com.nisovin.shopkeepers.shopkeeper.player.buy;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.offers.PriceOffer;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.currency.Currencies;
import com.nisovin.shopkeepers.currency.Currency;
import com.nisovin.shopkeepers.shopkeeper.TradingRecipeDraft;
import com.nisovin.shopkeepers.shopkeeper.player.PlaceholderItems;
import com.nisovin.shopkeepers.shopkeeper.player.PlayerShopEditorViewProvider;
import com.nisovin.shopkeepers.ui.editor.DefaultTradingRecipesAdapter;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import com.nisovin.shopkeepers.util.inventory.InventoryUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BuyingPlayerShopEditorViewProvider extends PlayerShopEditorViewProvider {
   protected BuyingPlayerShopEditorViewProvider(SKBuyingPlayerShopkeeper shopkeeper) {
      super(shopkeeper, new BuyingPlayerShopEditorViewProvider.TradingRecipesAdapter(shopkeeper));
   }

   public SKBuyingPlayerShopkeeper getShopkeeper() {
      return (SKBuyingPlayerShopkeeper)super.getShopkeeper();
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new BuyingPlayerShopEditorView(this, player, uiState);
   }

   private static class TradingRecipesAdapter extends DefaultTradingRecipesAdapter<PriceOffer> {
      private final SKBuyingPlayerShopkeeper shopkeeper;

      private TradingRecipesAdapter(SKBuyingPlayerShopkeeper shopkeeper) {
         assert shopkeeper != null;

         this.shopkeeper = shopkeeper;
      }

      public List<TradingRecipeDraft> getTradingRecipes() {
         List<? extends PriceOffer> offers = this.shopkeeper.getOffers();
         List<TradingRecipeDraft> recipes = new ArrayList(offers.size() + 8);
         Currency baseCurrency = Currencies.getBase();
         offers.forEach((offer) -> {
            UnmodifiableItemStack tradedItem = offer.getItem();
            UnmodifiableItemStack currencyItem = baseCurrency.getItemData().createUnmodifiableItemStack(offer.getPrice());
            TradingRecipeDraft recipe = new TradingRecipeDraft(currencyItem, tradedItem, (UnmodifiableItemStack)null);
            recipes.add(recipe);
         });
         List<ItemStack> newRecipes = new ArrayList();
         ItemStack[] containerContents = this.shopkeeper.getContainerContents();
         ItemStack[] var6 = containerContents;
         int var7 = containerContents.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            ItemStack containerItem = var6[var8];
            if (containerItem != null && !ItemUtils.isEmpty(containerItem)) {
               containerItem = PlaceholderItems.replaceNonNull(containerItem);
               if (!Currencies.matchesAny(containerItem) && this.shopkeeper.getOffer(containerItem) == null && !InventoryUtils.contains((Iterable)newRecipes, (ItemStack)containerItem)) {
                  containerItem = ItemUtils.copySingleItem(containerItem);
                  TradingRecipeDraft recipe = new TradingRecipeDraft((ItemStack)null, containerItem, (ItemStack)null);
                  recipes.add(recipe);
                  newRecipes.add(containerItem);
               }
            }
         }

         return recipes;
      }

      protected List<? extends PriceOffer> getOffers() {
         return this.shopkeeper.getOffers();
      }

      protected void setOffers(List<? extends PriceOffer> newOffers) {
         this.shopkeeper.setOffers(newOffers);
      }

      @Nullable
      protected PriceOffer createOffer(TradingRecipeDraft recipe) {
         assert recipe != null && recipe.isValid();

         assert recipe.getItem2() == null;

         UnmodifiableItemStack priceItem = (UnmodifiableItemStack)Unsafe.assertNonNull(recipe.getResultItem());
         if (!Currencies.getBase().getItemData().matches(priceItem)) {
            Log.debug(this.shopkeeper.getLogPrefix() + "Price item does not match the base currency!");
            return null;
         } else {
            assert priceItem.getAmount() > 0;

            int price = priceItem.getAmount();
            UnmodifiableItemStack tradedItem = (UnmodifiableItemStack)Unsafe.assertNonNull(recipe.getItem1());
            tradedItem = PlaceholderItems.replaceNonNull(tradedItem);
            return PriceOffer.create(tradedItem, price);
         }
      }
   }
}
