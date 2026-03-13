package com.nisovin.shopkeepers.api.shopkeeper.player.book;

import com.nisovin.shopkeepers.api.shopkeeper.offers.BookOffer;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface BookPlayerShopkeeper extends PlayerShopkeeper {
   List<? extends BookOffer> getOffers();

   @Nullable
   BookOffer getOffer(ItemStack var1);

   @Nullable
   BookOffer getOffer(UnmodifiableItemStack var1);

   @Nullable
   BookOffer getOffer(String var1);

   void removeOffer(String var1);

   void clearOffers();

   void setOffers(List<? extends BookOffer> var1);

   void addOffer(BookOffer var1);

   void addOffers(List<? extends BookOffer> var1);
}
