package com.nisovin.shopkeepers.api.shopkeeper.player.buy;

import com.nisovin.shopkeepers.api.shopkeeper.offers.PriceOffer;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface BuyingPlayerShopkeeper extends PlayerShopkeeper {
   List<? extends PriceOffer> getOffers();

   @Nullable
   PriceOffer getOffer(ItemStack var1);

   @Nullable
   PriceOffer getOffer(UnmodifiableItemStack var1);

   void removeOffer(ItemStack var1);

   void removeOffer(UnmodifiableItemStack var1);

   void clearOffers();

   void setOffers(List<? extends PriceOffer> var1);

   void addOffer(PriceOffer var1);

   void addOffers(List<? extends PriceOffer> var1);
}
