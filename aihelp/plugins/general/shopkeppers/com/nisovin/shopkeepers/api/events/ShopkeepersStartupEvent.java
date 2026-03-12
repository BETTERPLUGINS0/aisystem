package com.nisovin.shopkeepers.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/** @deprecated */
@Deprecated
public class ShopkeepersStartupEvent extends Event {
   private static final HandlerList handlers = new HandlerList();

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
