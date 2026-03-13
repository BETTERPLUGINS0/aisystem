package me.casperge.realisticseasons.api;

import me.casperge.realisticseasons.seasonevent.SeasonCustomEvent;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SeasonEventEnd extends Event {
   private static final HandlerList HANDLERS = new HandlerList();
   private final World world;
   private SeasonCustomEvent e;

   public SeasonEventEnd(World var1, SeasonCustomEvent var2) {
      this.world = var1;
      this.e = var2;
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
