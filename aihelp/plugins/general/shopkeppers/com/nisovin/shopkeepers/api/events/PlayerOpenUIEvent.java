package com.nisovin.shopkeepers.api.events;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.ui.UIType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerOpenUIEvent extends Event implements Cancellable {
   private final UIType uiType;
   private final Player player;
   private final boolean silentRequest;
   private boolean cancelled = false;
   private static final HandlerList handlers = new HandlerList();

   public PlayerOpenUIEvent(UIType uiType, Player player, boolean silentRequest) {
      Preconditions.checkNotNull(uiType, "uiType is null");
      Preconditions.checkNotNull(player, "player is null");
      this.uiType = uiType;
      this.player = player;
      this.silentRequest = silentRequest;
   }

   public UIType getUIType() {
      return this.uiType;
   }

   public Player getPlayer() {
      return this.player;
   }

   public boolean isSilentRequest() {
      return this.silentRequest;
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
