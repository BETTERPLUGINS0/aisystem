package com.nisovin.shopkeepers.shopkeeper.player.sell;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.offers.PriceOffer;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.currency.Currencies;
import com.nisovin.shopkeepers.shopkeeper.TradingRecipeDraft;
import com.nisovin.shopkeepers.shopkeeper.player.PlaceholderItems;
import com.nisovin.shopkeepers.shopkeeper.player.PlayerShopEditorViewProvider;
import com.nisovin.shopkeepers.ui.editor.DefaultTradingRecipesAdapter;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import com.nisovin.shopkeepers.util.inventory.InventoryUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SellingPlayerShopEditorViewProvider extends PlayerShopEditorViewProvider {
   protected SellingPlayerShopEditorViewProvider(SKSellingPlayerShopkeeper shopkeeper) {
      super(shopkeeper, new SellingPlayerShopEditorViewProvider.TradingRecipesAdapter(shopkeeper));
   }

   public SKSellingPlayerShopkeeper getShopkeeper() {
      return (SKSellingPlayerShopkeeper)super.getShopkeeper();
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new SellingPlayerShopEditorView(this, player, uiState);
   }

   private static class TradingRecipesAdapter extends DefaultTradingRecipesAdapter<PriceOffer> {
      private final SKSellingPlayerShopkeeper shopkeeper;

      private TradingRecipesAdapter(SKSellingPlayerShopkeeper shopkeeper) {
         assert shopkeeper != null;

         this.shopkeeper = shopkeeper;
      }

      public List<TradingRecipeDraft> getTradingRecipes() {
         List<? extends PriceOffer> offers = this.shopkeeper.getOffers();
         List<TradingRecipeDraft> recipes = new ArrayList(offers.size() + 8);
         offers.forEach((offer) -> {
            ItemStack tradedItem = ItemUtils.asItemStack(offer.getItem());
            TradingRecipeDraft recipe = SellingPlayerShopEditorViewProvider.createTradingRecipeDraft(tradedItem, offer.getPrice());
            recipes.add(recipe);
         });
         List<ItemStack> newRecipes = new ArrayList();
         ItemStack[] containerContents = this.shopkeeper.getContainerContents();
         ItemStack[] var5 = containerContents;
         int var6 = containerContents.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            ItemStack containerItem = var5[var7];
            if (containerItem != null && !ItemUtils.isEmpty(containerItem)) {
               containerItem = PlaceholderItems.replaceNonNull(containerItem);
               if (!Currencies.matchesAny(containerItem) && this.shopkeeper.getOffer(containerItem) == null && !InventoryUtils.contains((Iterable)newRecipes, (ItemStack)containerItem)) {
                  containerItem = ItemUtils.copySingleItem(containerItem);
                  TradingRecipeDraft recipe = SellingPlayerShopEditorViewProvider.createTradingRecipeDraft(containerItem, 0);
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

         int price = SellingPlayerShopEditorViewProvider.getPrice(this.shopkeeper, recipe);
         if (price <= 0) {
            return null;
         } else {
            UnmodifiableItemStack resultItem = (UnmodifiableItemStack)Unsafe.assertNonNull(recipe.getResultItem());
            resultItem = PlaceholderItems.replaceNonNull(resultItem);
            return PriceOffer.create(resultItem, price);
         }
      }
   }
}
