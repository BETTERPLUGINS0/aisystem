package com.nisovin.shopkeepers.shopkeeper.admin.regular;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.offers.TradeOffer;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.TradingRecipeDraft;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.SKDefaultUITypes;
import com.nisovin.shopkeepers.ui.editor.DefaultTradingRecipesAdapter;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperEditorViewProvider;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.Nullable;

public class RegularAdminShopEditorViewProvider extends ShopkeeperEditorViewProvider {
   protected RegularAdminShopEditorViewProvider(SKRegularAdminShopkeeper shopkeeper) {
      super(SKDefaultUITypes.EDITOR(), shopkeeper, new RegularAdminShopEditorViewProvider.TradingRecipesAdapter(shopkeeper));
   }

   public SKRegularAdminShopkeeper getShopkeeper() {
      return (SKRegularAdminShopkeeper)super.getShopkeeper();
   }

   public boolean canAccess(Player player, boolean silent) {
      if (!super.canAccess(player, silent)) {
         return false;
      } else if (!this.getShopkeeper().getType().hasPermission(player)) {
         if (!silent) {
            this.debugNotOpeningUI(player, "Player is missing the permission to edit this type of shopkeeper.");
            TextUtils.sendMessage(player, (Text)Messages.noPermission);
         }

         return false;
      } else {
         return true;
      }
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new RegularAdminShopEditorView(this, player, uiState);
   }

   private static class TradingRecipesAdapter extends DefaultTradingRecipesAdapter<TradeOffer> {
      private final SKRegularAdminShopkeeper shopkeeper;

      private TradingRecipesAdapter(SKRegularAdminShopkeeper shopkeeper) {
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
         return TradeOffer.create(resultItem, item1, item2);
      }

      protected void handleInvalidTradingRecipe(Player player, TradingRecipeDraft invalidRecipe) {
         ItemStack resultItem = ItemUtils.copyOrNull(invalidRecipe.getResultItem());
         ItemStack item1 = ItemUtils.copyOrNull(invalidRecipe.getItem1());
         ItemStack item2 = ItemUtils.copyOrNull(invalidRecipe.getItem2());
         PlayerInventory playerInventory = player.getInventory();
         if (item1 != null) {
            playerInventory.addItem(new ItemStack[]{item1});
         }

         if (item2 != null) {
            playerInventory.addItem(new ItemStack[]{item2});
         }

         if (resultItem != null) {
            playerInventory.addItem(new ItemStack[]{resultItem});
         }

      }
   }
}
