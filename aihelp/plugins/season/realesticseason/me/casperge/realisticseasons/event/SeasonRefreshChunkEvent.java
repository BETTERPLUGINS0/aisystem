package me.casperge.realisticseasons.event;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SeasonRefreshChunkEvent extends Event implements Cancellable {
   private static final HandlerList HANDLERS = new HandlerList();
   private final Chunk chunk;
   private final Player p;
   private boolean isCancelled;

   public SeasonRefreshChunkEvent(Chunk var1, Player var2) {
      this.chunk = var1;
      this.p = var2;
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

   public Chunk getChunk() {
      return this.chunk;
   }

   public Player getPlayer() {
      return this.p;
   }
}
