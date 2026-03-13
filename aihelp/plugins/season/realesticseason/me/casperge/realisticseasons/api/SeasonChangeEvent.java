package me.casperge.realisticseasons.api;

import me.casperge.realisticseasons.season.Season;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SeasonChangeEvent extends Event implements Cancellable {
   private static final HandlerList HANDLERS = new HandlerList();
   private final World world;
   private final Season newSeason;
   private final Season oldSeason;
   private boolean isCancelled;

   public SeasonChangeEvent(World var1, Season var2, Season var3) {
      this.world = var1;
      this.newSeason = var2;
      this.oldSeason = var3;
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

   public Season getNewSeason() {
      return this.newSeason;
   }

   public Season getOldSeason() {
      return this.oldSeason;
   }
}
