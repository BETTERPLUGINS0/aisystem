package com.nisovin.shopkeepers.api.shopkeeper.offers;

import com.nisovin.shopkeepers.api.internal.ApiInternals;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import org.bukkit.inventory.ItemStack;

public interface PriceOffer {
   static PriceOffer create(ItemStack item, int price) {
      return ApiInternals.getInstance().createPriceOffer(item, price);
   }

   static PriceOffer create(UnmodifiableItemStack item, int price) {
      return ApiInternals.getInstance().createPriceOffer(item, price);
   }

   UnmodifiableItemStack getItem();

   int getPrice();
}
