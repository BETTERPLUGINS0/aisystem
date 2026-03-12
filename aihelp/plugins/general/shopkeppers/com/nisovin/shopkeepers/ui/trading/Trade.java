package com.nisovin.shopkeepers.ui.trading;

import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.KeyValueStore;
import com.nisovin.shopkeepers.util.java.MapBasedKeyValueStore;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Trade {
   private final TradingContext tradingContext;
   private final int tradeNumber;
   private final TradingRecipe tradingRecipe;
   private final ItemStack offeredItem1;
   @Nullable
   private final ItemStack offeredItem2;
   private final boolean swappedItemOrder;
   private final KeyValueStore metadata = new MapBasedKeyValueStore();
   private final ShopkeeperTradeEvent tradeEvent;
   private boolean tradeEventCalled = false;

   Trade(TradingContext tradingContext, int tradeNumber, TradingRecipe tradingRecipe, ItemStack offeredItem1, @Nullable ItemStack offeredItem2, boolean swappedItemOrder) {
      Validate.notNull(tradingContext, (String)"tradingContext is null");
      Validate.isTrue(tradeNumber >= 1, "tradeNumber is less than 1");
      Validate.notNull(tradingRecipe, (String)"tradingRecipe is null");
      Validate.notNull(offeredItem1, (String)"offeredItem1 is null");
      this.tradingContext = tradingContext;
      this.tradeNumber = tradeNumber;
      this.tradingRecipe = tradingRecipe;
      this.offeredItem1 = offeredItem1;
      this.offeredItem2 = offeredItem2;
      this.swappedItemOrder = swappedItemOrder;
      ItemStack eventOfferedItem1 = ItemUtils.copyWithAmount(offeredItem1, tradingRecipe.getItem1().getAmount());
      ItemStack eventOfferedItem2 = ItemUtils.cloneOrNullIfEmpty(offeredItem2);
      if (eventOfferedItem2 != null) {
         UnmodifiableItemStack recipeItem2 = (UnmodifiableItemStack)Unsafe.assertNonNull(tradingRecipe.getItem2());
         eventOfferedItem2.setAmount(recipeItem2.getAmount());
      }

      this.tradeEvent = new ShopkeeperTradeEvent(tradingContext.getShopkeeper(), tradingContext.getTradingPlayer(), tradingContext.getInventoryClickEvent(), tradingRecipe, UnmodifiableItemStack.ofNonNull(eventOfferedItem1), UnmodifiableItemStack.of(eventOfferedItem2), swappedItemOrder);
   }

   public TradingContext getTradingContext() {
      return this.tradingContext;
   }

   public Shopkeeper getShopkeeper() {
      return this.tradingContext.getShopkeeper();
   }

   public InventoryClickEvent getInventoryClickEvent() {
      return this.tradingContext.getInventoryClickEvent();
   }

   public MerchantInventory getMerchantInventory() {
      return this.tradingContext.getMerchantInventory();
   }

   public Player getTradingPlayer() {
      return this.tradingContext.getTradingPlayer();
   }

   public PlayerInventory getPlayerInventory() {
      return this.tradingContext.getPlayerInventory();
   }

   public int getTradeNumber() {
      return this.tradeNumber;
   }

   public TradingRecipe getTradingRecipe() {
      return this.tradingRecipe;
   }

   public ItemStack getOfferedItem1() {
      return this.offeredItem1;
   }

   @Nullable
   public ItemStack getOfferedItem2() {
      return this.offeredItem2;
   }

   public boolean isItemOrderSwapped() {
      return this.swappedItemOrder;
   }

   public KeyValueStore getMetadata() {
      return this.metadata;
   }

   public ShopkeeperTradeEvent getTradeEvent() {
      return this.tradeEvent;
   }

   public ShopkeeperTradeEvent callTradeEvent() {
      this.tradeEventCalled = true;
      Bukkit.getPluginManager().callEvent(this.tradeEvent);
      return this.tradeEvent;
   }

   public boolean isTradeEventCalled() {
      return this.tradeEventCalled;
   }
}
