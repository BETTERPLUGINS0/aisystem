package com.nisovin.shopkeepers.ui.editor;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.shopkeeper.TradingRecipeDraft;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class DefaultTradingRecipesAdapter<O> implements TradingRecipesAdapter {
   protected DefaultTradingRecipesAdapter() {
   }

   public abstract List<TradingRecipeDraft> getTradingRecipes();

   public int updateTradingRecipes(Player player, List<? extends TradingRecipeDraft> recipes) {
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(recipes, (String)"recipes is null");

      assert this.getOffers() != null && !CollectionUtils.containsNull(this.getOffers());

      List<O> newOffers = new ArrayList(this.getOffers());
      int oldOffersSize = newOffers.size();
      int changedOffers = 0;
      boolean clearedAtLeastOneOffer = false;

      for(int index = 0; index < recipes.size(); ++index) {
         TradingRecipeDraft recipe = (TradingRecipeDraft)recipes.get(index);
         Validate.notNull(recipe, (String)"recipes contains null");
         O newOffer = recipe.isValid() ? this.createOffer(recipe) : null;
         if (newOffer == null) {
            this.handleInvalidTradingRecipe(player, recipe);
            if (index < oldOffersSize) {
               newOffers.set(index, (Object)null);
               clearedAtLeastOneOffer = true;
               ++changedOffers;
            }
         } else if (index < oldOffersSize) {
            O oldOffer = Unsafe.assertNonNull(newOffers.get(index));
            if (!this.areOffersEqual(oldOffer, newOffer)) {
               newOffers.set(index, newOffer);
               ++changedOffers;
            }
         } else {
            newOffers.add(newOffer);
            ++changedOffers;
         }
      }

      if (changedOffers > 0) {
         if (clearedAtLeastOneOffer) {
            newOffers.removeIf(Objects::isNull);
         }

         this.setOffers((List)Unsafe.cast(newOffers));
      }

      return changedOffers;
   }

   protected abstract List<? extends O> getOffers();

   protected abstract void setOffers(List<? extends O> var1);

   @Nullable
   protected abstract O createOffer(TradingRecipeDraft var1);

   protected boolean areOffersEqual(@NonNull O oldOffer, @NonNull O newOffer) {
      return oldOffer.equals(newOffer);
   }

   protected void handleInvalidTradingRecipe(Player player, TradingRecipeDraft invalidRecipe) {
   }
}
