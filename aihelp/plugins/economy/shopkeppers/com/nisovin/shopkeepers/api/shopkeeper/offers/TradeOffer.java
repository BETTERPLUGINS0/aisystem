package com.nisovin.shopkeepers.api.shopkeeper.offers;

import com.nisovin.shopkeepers.api.internal.ApiInternals;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface TradeOffer {
   static TradeOffer create(ItemStack resultItem, ItemStack item1, @Nullable ItemStack item2) {
      return ApiInternals.getInstance().createTradeOffer(resultItem, item1, item2);
   }

   static TradeOffer create(UnmodifiableItemStack resultItem, UnmodifiableItemStack item1, @Nullable UnmodifiableItemStack item2) {
      return ApiInternals.getInstance().createTradeOffer(resultItem, item1, item2);
   }

   UnmodifiableItemStack getResultItem();

   UnmodifiableItemStack getItem1();

   @Nullable
   UnmodifiableItemStack getItem2();

   boolean hasItem2();

   boolean areItemsEqual(ItemStack var1, ItemStack var2, @Nullable ItemStack var3);

   boolean areItemsEqual(UnmodifiableItemStack var1, UnmodifiableItemStack var2, @Nullable UnmodifiableItemStack var3);

   boolean areItemsEqual(TradingRecipe var1);
}
