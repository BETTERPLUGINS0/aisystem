package com.nisovin.shopkeepers.shopkeeper.player.trade;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.offers.TradeOffer;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
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

public class TradingPlayerShopEditorViewProvider extends PlayerShopEditorViewProvider {
   protected TradingPlayerShopEditorViewProvider(SKTradingPlayerShopkeeper shopkeeper) {
      super(shopkeeper, new TradingPlayerShopEditorViewProvider.TradingRecipesAdapter(shopkeeper));
   }

   public SKTradingPlayerShopkeeper getShopkeeper() {
      return (SKTradingPlayerShopkeeper)super.getShopkeeper();
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new TradingPlayerShopEditorView(this, player, uiState);
   }

   private static class TradingRecipesAdapter extends DefaultTradingRecipesAdapter<TradeOffer> {
      private final SKTradingPlayerShopkeeper shopkeeper;

      private TradingRecipesAdapter(SKTradingPlayerShopkeeper shopkeeper) {
         assert shopkeeper != null;

         this.shopkeeper = shopkeeper;
      }

      public List<TradingRecipeDraft> getTradingRecipes() {
         List<? extends TradeOffer> offers = this.shopkeeper.getOffers();
         List<TradingRecipeDraft> recipes = new ArrayList(offers.size() + 8);
         offers.forEach((offer) -> {
            TradingRecipeDraft recipe = new TradingRecipeDraft(offer.getResultItem(), offer.getItem1(), offer.getItem2());
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
               if (!this.shopkeeper.hasOffer(containerItem) && !InventoryUtils.contains((Iterable)newRecipes, (ItemStack)containerItem)) {
                  containerItem = ItemUtils.copySingleItem(containerItem);
                  TradingRecipeDraft recipe = new TradingRecipeDraft(containerItem, (ItemStack)null, (ItemStack)null);
                  recipes.add(recipe);
                  newRecipes.add(containerItem);
               }
            }
         }

         return recipes;
      }

      protected List<? extends TradeOffer> getOffers() {
         return this.shopkeeper.getOffers();
      }

      protected void setOffers(List<? extends TradeOffer> newOffers) {
         this.shopkeeper.setOffers(newOffers);
      }

      @Nullable
      protected TradeOffer createOffer(TradingRecipeDraft recipe) {
         assert recipe != null && recipe.isValid();

         UnmodifiableItemStack resultItem = (UnmodifiableItemStack)Unsafe.assertNonNull(recipe.getResultItem());
         UnmodifiableItemStack item1 = (UnmodifiableItemStack)Unsafe.assertNonNull(recipe.getRecipeItem1());
         UnmodifiableItemStack item2 = recipe.getRecipeItem2();
         resultItem = PlaceholderItems.replaceNonNull(resultItem);
         item1 = PlaceholderItems.replaceNonNull(item1);
         item2 = PlaceholderItems.replace(item2);
         return TradeOffer.create(resultItem, item1, item2);
      }
   }
}
