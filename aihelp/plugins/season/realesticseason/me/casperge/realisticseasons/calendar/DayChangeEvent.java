package me.casperge.realisticseasons.calendar;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DayChangeEvent extends Event {
   private static final HandlerList HANDLERS = new HandlerList();
   private World w;
   private Date from;
   private Date to;

   public DayChangeEvent(World var1, Date var2, Date var3) {
      this.w = var1;
      this.from = var2;
      this.to = var3;
   }

   public HandlerList getHandlers() {
      return HANDLERS;
   }

   public static HandlerList getHandlerList() {
      return HANDLERS;
   }

   public World getWorld() {
      return this.w;
   }

   public Date getFrom() {
      return this.from;
   }

   public Date getTo() {
      return this.to;
   }
}
