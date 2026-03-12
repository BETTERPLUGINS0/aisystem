package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.shopkeeper.SKTradingRecipe;
import com.nisovin.shopkeepers.shopkeeper.TradingRecipeDraft;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class MerchantUtils {
   public static final MerchantUtils.MerchantRecipeComparator MERCHANT_RECIPES_EQUAL_ITEMS = new MerchantUtils.MerchantRecipeComparator() {
      public boolean equals(@Nullable MerchantRecipe recipe1, @Nullable MerchantRecipe recipe2) {
         if (recipe1 == recipe2) {
            return true;
         } else if (recipe1 != null && recipe2 != null) {
            if (!recipe1.getResult().equals(recipe2.getResult())) {
               return false;
            } else {
               List<ItemStack> ingredients1 = recipe1.getIngredients();
               ingredients1.removeIf(ItemUtils::isEmpty);
               List<ItemStack> ingredients2 = recipe2.getIngredients();
               ingredients2.removeIf(ItemUtils::isEmpty);
               return ingredients1.equals(ingredients2);
            }
         } else {
            return false;
         }
      }
   };
   public static final MerchantUtils.MerchantRecipeComparator MERCHANT_RECIPES_IGNORE_USES_EXCEPT_BLOCKED = new MerchantUtils.MerchantRecipeComparator() {
      public boolean equals(@Nullable MerchantRecipe recipe1, @Nullable MerchantRecipe recipe2) {
         if (recipe1 == recipe2) {
            return true;
         } else if (recipe1 != null && recipe2 != null) {
            boolean isBlocked1 = recipe1.getUses() >= recipe1.getMaxUses();
            boolean isBlocked2 = recipe2.getUses() >= recipe2.getMaxUses();
            if (isBlocked1 != isBlocked2) {
               return false;
            } else if (recipe1.hasExperienceReward() != recipe2.hasExperienceReward()) {
               return false;
            } else if (recipe1.getPriceMultiplier() != recipe2.getPriceMultiplier()) {
               return false;
            } else if (recipe1.getVillagerExperience() != recipe2.getVillagerExperience()) {
               return false;
            } else if (!recipe1.getResult().equals(recipe2.getResult())) {
               return false;
            } else {
               List<ItemStack> ingredients1 = recipe1.getIngredients();
               ingredients1.removeIf(ItemUtils::isEmpty);
               List<ItemStack> ingredients2 = recipe2.getIngredients();
               ingredients2.removeIf(ItemUtils::isEmpty);
               return ingredients1.equals(ingredients2);
            }
         } else {
            return false;
         }
      }
   };

   @Nullable
   public static TradingRecipe getActiveTradingRecipe(MerchantInventory merchantInventory) {
      MerchantRecipe merchantRecipe = merchantInventory.getSelectedRecipe();
      return merchantRecipe == null ? null : createTradingRecipe(merchantRecipe);
   }

   @Nullable
   public static TradingRecipe getSelectedTradingRecipe(MerchantInventory merchantInventory) {
      int selectedRecipeIndex = merchantInventory.getSelectedRecipeIndex();
      List<MerchantRecipe> merchantRecipes = merchantInventory.getMerchant().getRecipes();
      if (merchantRecipes.isEmpty()) {
         return null;
      } else {
         MerchantRecipe merchantRecipe = (MerchantRecipe)merchantRecipes.get(selectedRecipeIndex);
         return createTradingRecipe(merchantRecipe);
      }
   }

   public static TradingRecipe createTradingRecipe(MerchantRecipe merchantRecipe) {
      Validate.notNull(merchantRecipe, (String)"merchantRecipe is null");
      List<ItemStack> ingredients = merchantRecipe.getIngredients();
      UnmodifiableItemStack item1 = UnmodifiableItemStack.ofNonNull((ItemStack)ingredients.get(0));
      UnmodifiableItemStack item2 = null;
      if (ingredients.size() > 1) {
         item2 = UnmodifiableItemStack.of(ItemUtils.getNullIfEmpty((ItemStack)ingredients.get(1)));
      }

      UnmodifiableItemStack resultItem = UnmodifiableItemStack.ofNonNull(merchantRecipe.getResult().clone());
      return new SKTradingRecipe(resultItem, item1, item2);
   }

   public static TradingRecipeDraft createTradingRecipeDraft(MerchantRecipe merchantRecipe) {
      Validate.notNull(merchantRecipe, (String)"merchantRecipe is null");
      List<ItemStack> ingredients = merchantRecipe.getIngredients();
      UnmodifiableItemStack item1 = UnmodifiableItemStack.ofNonNull((ItemStack)ingredients.get(0));
      UnmodifiableItemStack item2 = null;
      if (ingredients.size() > 1) {
         item2 = UnmodifiableItemStack.of(ItemUtils.getNullIfEmpty((ItemStack)ingredients.get(1)));
      }

      UnmodifiableItemStack resultItem = UnmodifiableItemStack.ofNonNull(merchantRecipe.getResult().clone());
      return new TradingRecipeDraft(resultItem, item1, item2);
   }

   public static MerchantRecipe createMerchantRecipe(TradingRecipe recipe) {
      Validate.notNull(recipe, (String)"recipe is null");
      MerchantRecipe merchantRecipe = createMerchantRecipe(recipe.getResultItem(), recipe.getItem1(), recipe.getItem2());
      if (recipe.isOutOfStock()) {
         merchantRecipe.setMaxUses(0);
      }

      return merchantRecipe;
   }

   public static MerchantRecipe createMerchantRecipe(TradingRecipeDraft recipe) {
      Validate.notNull(recipe, (String)"recipe is null");
      Validate.isTrue(recipe.isValid(), "recipe is not valid");
      return createMerchantRecipe((UnmodifiableItemStack)Unsafe.assertNonNull(recipe.getResultItem()), (UnmodifiableItemStack)Unsafe.assertNonNull(recipe.getRecipeItem1()), recipe.getRecipeItem2());
   }

   public static MerchantRecipe createMerchantRecipe(UnmodifiableItemStack resultItem, UnmodifiableItemStack buyItem1, @Nullable UnmodifiableItemStack buyItem2) {
      assert !ItemUtils.isEmpty(resultItem) && !ItemUtils.isEmpty(buyItem1);

      MerchantRecipe merchantRecipe = new MerchantRecipe(resultItem.copy(), Integer.MAX_VALUE);
      merchantRecipe.setExperienceReward(false);
      merchantRecipe.addIngredient(ItemUtils.asItemStack(buyItem1));
      if (buyItem2 != null) {
         merchantRecipe.addIngredient(ItemUtils.asItemStack(buyItem2));
      }

      return merchantRecipe;
   }

   public static List<TradingRecipeDraft> createTradingRecipeDrafts(List<? extends MerchantRecipe> merchantRecipes) {
      List<TradingRecipeDraft> tradingRecipeDrafts = new ArrayList(merchantRecipes.size());
      merchantRecipes.forEach((merchantRecipe) -> {
         tradingRecipeDrafts.add(createTradingRecipeDraft(merchantRecipe));
      });
      return tradingRecipeDrafts;
   }

   public static List<MerchantRecipe> createMerchantRecipes(List<? extends TradingRecipe> recipes) {
      List<MerchantRecipe> merchantRecipes = new ArrayList(recipes.size());
      recipes.forEach((recipe) -> {
         merchantRecipes.add(createMerchantRecipe(recipe));
      });
      return merchantRecipes;
   }

   private MerchantUtils() {
   }

   public abstract static class MerchantRecipeComparator {
      public abstract boolean equals(@Nullable MerchantRecipe var1, @Nullable MerchantRecipe var2);

      public boolean equals(@Nullable List<? extends MerchantRecipe> recipes1, @Nullable List<? extends MerchantRecipe> recipes2) {
         if (recipes1 == recipes2) {
            return true;
         } else if (recipes1 != null && recipes2 != null) {
            if (recipes1.size() != recipes2.size()) {
               return false;
            } else {
               for(int i = 0; i < recipes1.size(); ++i) {
                  MerchantRecipe recipe1 = (MerchantRecipe)recipes1.get(i);
                  MerchantRecipe recipe2 = (MerchantRecipe)recipes2.get(i);
                  if (!this.equals(recipe1, recipe2)) {
                     return false;
                  }
               }

               return true;
            }
         } else {
            return false;
         }
      }
   }
}
