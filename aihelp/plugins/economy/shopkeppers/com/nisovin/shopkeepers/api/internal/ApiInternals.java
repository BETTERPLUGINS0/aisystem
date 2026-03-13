package com.nisovin.shopkeepers.api.internal;

import com.nisovin.shopkeepers.api.shopkeeper.offers.BookOffer;
import com.nisovin.shopkeepers.api.shopkeeper.offers.PriceOffer;
import com.nisovin.shopkeepers.api.shopkeeper.offers.TradeOffer;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;

public interface ApiInternals {
   static ApiInternals getInstance() {
      return InternalShopkeepersAPI.getPlugin().getApiInternals();
   }

   @PolyNull
   UnmodifiableItemStack createUnmodifiableItemStack(@PolyNull ItemStack var1);

   PriceOffer createPriceOffer(ItemStack var1, int var2);

   PriceOffer createPriceOffer(UnmodifiableItemStack var1, int var2);

   TradeOffer createTradeOffer(ItemStack var1, ItemStack var2, @Nullable ItemStack var3);

   TradeOffer createTradeOffer(UnmodifiableItemStack var1, UnmodifiableItemStack var2, @Nullable UnmodifiableItemStack var3);

   BookOffer createBookOffer(String var1, int var2);

   int getShopkeeperSnapshotMaxNameLength();

   boolean isShopkeeperSnapshotNameValid(String var1);

   boolean isEmpty(@Nullable UnmodifiableItemStack var1);
}
