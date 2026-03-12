package com.lenis0012.bukkit.loginsecurity.events;

import com.lenis0012.bukkit.loginsecurity.session.AuthAction;
import com.lenis0012.bukkit.loginsecurity.session.AuthActionType;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AuthActionEvent extends Event implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   protected final PlayerSession session;
   protected final AuthAction action;
   private boolean cancelled = false;
   private String cancelledMessage = null;

   public AuthActionEvent(PlayerSession session, AuthAction action) {
      super(true);
      this.session = session;
      this.action = action;
   }

   public Player getPlayer() {
      return this.session.getPlayer();
   }

   public AuthAction getAction() {
      return this.action;
   }

   public PlayerSession getSession() {
      return this.session;
   }

   public AuthActionType getType() {
      return this.action.getType();
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public String getCancelledMessage() {
      return this.cancelledMessage;
   }

   public void setCancelledMessage(String cancelledMessage) {
      this.cancelledMessage = cancelledMessage;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
