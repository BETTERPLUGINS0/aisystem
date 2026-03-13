package com.nisovin.shopkeepers.api.events;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.trading.TradeEffect;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopkeeperTradeEvent extends ShopkeeperEvent implements Cancellable {
   private final Player player;
   private final InventoryClickEvent clickEvent;
   private final TradingRecipe tradingRecipe;
   private final UnmodifiableItemStack offeredItem1;
   @Nullable
   private final UnmodifiableItemStack offeredItem2;
   private final boolean swappedItemOrder;
   @Nullable
   private UnmodifiableItemStack receivedItem1;
   @Nullable
   private UnmodifiableItemStack receivedItem2;
   @Nullable
   private UnmodifiableItemStack resultItem;
   private boolean receivedItem1Altered = false;
   private boolean receivedItem2Altered = false;
   private boolean resultItemAltered = false;
   private List<TradeEffect> tradeEffects = new ArrayList();
   private boolean cancelled = false;
   private static final HandlerList handlers = new HandlerList();

   public ShopkeeperTradeEvent(Shopkeeper shopkeeper, Player player, InventoryClickEvent clickEvent, TradingRecipe tradingRecipe, UnmodifiableItemStack offeredItem1, @Nullable UnmodifiableItemStack offeredItem2, boolean swappedItemOrder) {
      super(shopkeeper);
      Preconditions.checkNotNull(player, "player is null");
      Preconditions.checkNotNull(clickEvent, "clickEvent is null");
      Preconditions.checkNotNull(tradingRecipe, "tradingRecipe is null");
      Preconditions.checkNotNull(offeredItem1, "offeredItem1 is null");
      this.player = player;
      this.clickEvent = clickEvent;
      this.tradingRecipe = tradingRecipe;
      this.offeredItem1 = offeredItem1;
      this.offeredItem2 = offeredItem2;
      this.swappedItemOrder = swappedItemOrder;
      this.receivedItem1 = offeredItem1;
      this.receivedItem2 = offeredItem2;
      this.resultItem = tradingRecipe.getResultItem();
   }

   public Player getPlayer() {
      return this.player;
   }

   public InventoryClickEvent getClickEvent() {
      return this.clickEvent;
   }

   public TradingRecipe getTradingRecipe() {
      return this.tradingRecipe;
   }

   public UnmodifiableItemStack getOfferedItem1() {
      return this.offeredItem1;
   }

   @Nullable
   public UnmodifiableItemStack getOfferedItem2() {
      return this.offeredItem2;
   }

   public boolean hasOfferedItem2() {
      return this.offeredItem2 != null;
   }

   public boolean isItemOrderSwapped() {
      return this.swappedItemOrder;
   }

   @Nullable
   public UnmodifiableItemStack getReceivedItem1() {
      return this.receivedItem1;
   }

   public void setReceivedItem1(@Nullable UnmodifiableItemStack itemStack) {
      this.receivedItem1Altered = true;
      this.receivedItem1 = itemStack;
   }

   public boolean isReceivedItem1Altered() {
      return this.receivedItem1Altered;
   }

   @Nullable
   public UnmodifiableItemStack getReceivedItem2() {
      return this.receivedItem2;
   }

   public void setReceivedItem2(@Nullable UnmodifiableItemStack itemStack) {
      this.receivedItem2Altered = true;
      this.receivedItem2 = itemStack;
   }

   public boolean isReceivedItem2Altered() {
      return this.receivedItem2Altered;
   }

   @Nullable
   public UnmodifiableItemStack getResultItem() {
      return this.resultItem;
   }

   public void setResultItem(@Nullable UnmodifiableItemStack itemStack) {
      this.resultItemAltered = true;
      this.resultItem = itemStack;
   }

   public boolean isResultItemAltered() {
      return this.resultItemAltered;
   }

   public List<TradeEffect> getTradeEffects() {
      return this.tradeEffects;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
