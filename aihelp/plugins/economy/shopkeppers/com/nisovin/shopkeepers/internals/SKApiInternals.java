package com.nisovin.shopkeepers.internals;

import com.nisovin.shopkeepers.api.internal.ApiInternals;
import com.nisovin.shopkeepers.api.shopkeeper.offers.BookOffer;
import com.nisovin.shopkeepers.api.shopkeeper.offers.PriceOffer;
import com.nisovin.shopkeepers.api.shopkeeper.offers.TradeOffer;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.shopkeeper.SKShopkeeperSnapshot;
import com.nisovin.shopkeepers.shopkeeper.offers.SKBookOffer;
import com.nisovin.shopkeepers.shopkeeper.offers.SKPriceOffer;
import com.nisovin.shopkeepers.shopkeeper.offers.SKTradeOffer;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.inventory.SKUnmodifiableItemStack;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;

public class SKApiInternals implements ApiInternals {
   @PolyNull
   public UnmodifiableItemStack createUnmodifiableItemStack(@PolyNull ItemStack itemStack) {
      return SKUnmodifiableItemStack.of(itemStack);
   }

   public PriceOffer createPriceOffer(ItemStack item, int price) {
      return new SKPriceOffer(item, price);
   }

   public PriceOffer createPriceOffer(UnmodifiableItemStack item, int price) {
      return new SKPriceOffer(item, price);
   }

   public TradeOffer createTradeOffer(ItemStack resultItem, ItemStack item1, @Nullable ItemStack item2) {
      return new SKTradeOffer(resultItem, item1, item2);
   }

   public TradeOffer createTradeOffer(UnmodifiableItemStack resultItem, UnmodifiableItemStack item1, @Nullable UnmodifiableItemStack item2) {
      return new SKTradeOffer(resultItem, item1, item2);
   }

   public BookOffer createBookOffer(String bookTitle, int price) {
      return new SKBookOffer(bookTitle, price);
   }

   public int getShopkeeperSnapshotMaxNameLength() {
      return 64;
   }

   public boolean isShopkeeperSnapshotNameValid(String name) {
      return SKShopkeeperSnapshot.isNameValid(name);
   }

   public boolean isEmpty(@Nullable UnmodifiableItemStack itemStack) {
      return ItemUtils.isEmpty(itemStack);
   }
}
