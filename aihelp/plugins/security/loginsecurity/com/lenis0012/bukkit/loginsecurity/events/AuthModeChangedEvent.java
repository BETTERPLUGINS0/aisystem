package com.lenis0012.bukkit.loginsecurity.events;

import com.lenis0012.bukkit.loginsecurity.session.AuthMode;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AuthModeChangedEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final PlayerSession session;
   private final AuthMode previous;
   private final AuthMode current;

   public AuthModeChangedEvent(PlayerSession session, AuthMode previous, AuthMode current) {
      super(true);
      this.session = session;
      this.previous = previous;
      this.current = current;
   }

   public PlayerSession getSession() {
      return this.session;
   }

   public AuthMode getPreviousMode() {
      return this.previous;
   }

   public AuthMode getCurrentMode() {
      return this.current;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
