package com.nisovin.shopkeepers.api.shopkeeper;

import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface TradingRecipe {
   UnmodifiableItemStack getResultItem();

   UnmodifiableItemStack getItem1();

   @Nullable
   UnmodifiableItemStack getItem2();

   boolean hasItem2();

   boolean isOutOfStock();

   boolean areItemsEqual(@Nullable ItemStack var1, @Nullable ItemStack var2, @Nullable ItemStack var3);

   boolean areItemsEqual(@Nullable UnmodifiableItemStack var1, @Nullable UnmodifiableItemStack var2, @Nullable UnmodifiableItemStack var3);

   boolean areItemsEqual(@Nullable TradingRecipe var1);
}
