package com.nisovin.shopkeepers.api.events;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.internal.ApiInternals;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UpdateItemEvent extends Event {
   private final UnmodifiableItemStack orginalItem;
   private UnmodifiableItemStack item;
   private boolean itemAltered = false;
   private static final HandlerList handlers = new HandlerList();

   public UpdateItemEvent(UnmodifiableItemStack originalItem) {
      Preconditions.checkArgument(!ApiInternals.getInstance().isEmpty(originalItem), "originalItem is empty");
      this.orginalItem = originalItem;
      this.item = originalItem;
   }

   public UnmodifiableItemStack getOriginalItem() {
      return this.orginalItem;
   }

   public UnmodifiableItemStack getItem() {
      return this.item;
   }

   public void setItem(UnmodifiableItemStack item) {
      Preconditions.checkArgument(!ApiInternals.getInstance().isEmpty(item), "item is empty");
      if (!this.item.equals((Object)item)) {
         this.itemAltered = true;
         this.item = item;
      }
   }

   public boolean isItemAltered() {
      return this.itemAltered;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
