package me.casperge.realisticseasons.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SeasonParticleStartEvent extends Event implements Cancellable {
   private static final HandlerList HANDLERS = new HandlerList();
   private boolean isCancelled;
   private Location loc;
   private Player p;
   private SeasonParticle particle;

   public SeasonParticleStartEvent(Location var1, Player var2, SeasonParticle var3) {
      this.loc = var1;
      this.p = var2;
      this.particle = var3;
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

   public Player getPlayer() {
      return this.p;
   }

   public SeasonParticle getParticleType() {
      return this.particle;
   }

   public Location getLocation() {
      return this.loc;
   }
}
