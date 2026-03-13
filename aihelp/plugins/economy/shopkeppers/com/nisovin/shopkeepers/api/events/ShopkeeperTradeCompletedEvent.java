package com.nisovin.shopkeepers.api.events;

import com.google.common.base.Preconditions;
import org.bukkit.event.HandlerList;

public class ShopkeeperTradeCompletedEvent extends ShopkeeperEvent {
   private final ShopkeeperTradeEvent completedTrade;
   private static final HandlerList handlers = new HandlerList();

   public ShopkeeperTradeCompletedEvent(ShopkeeperTradeEvent tradeEvent) {
      super(tradeEvent.getShopkeeper());
      Preconditions.checkNotNull(tradeEvent, "tradeEvent is null");
      this.completedTrade = tradeEvent;
   }

   public ShopkeeperTradeEvent getCompletedTrade() {
      return this.completedTrade;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
