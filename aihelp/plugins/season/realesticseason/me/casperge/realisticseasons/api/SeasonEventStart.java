package me.casperge.realisticseasons.api;

import me.casperge.realisticseasons.seasonevent.SeasonCustomEvent;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SeasonEventStart extends Event implements Cancellable {
   private static final HandlerList HANDLERS = new HandlerList();
   private final World world;
   private SeasonCustomEvent e;
   private boolean isCancelled;

   public SeasonEventStart(World var1, SeasonCustomEvent var2) {
      this.world = var1;
      this.e = var2;
   }

   public boolean isCancelled() {
      return this.isCancelled;
   }

   public void setCancelled(boolean var1) {
      this.isCancelled = var1;
   }

   public HandlerList getHandlers() {
      return HANDLERS;
   }

   public static HandlerList getHandlerList() {
      return HANDLERS;
   }

   public World getWorld() {
      return this.world;
   }

   public SeasonCustomEvent getCustomEvent() {
      return this.e;
   }
}
