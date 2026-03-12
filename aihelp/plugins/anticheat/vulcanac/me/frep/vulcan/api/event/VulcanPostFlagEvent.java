package me.frep.vulcan.api.event;

import me.frep.vulcan.api.check.Check;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VulcanPostFlagEvent extends Event implements Cancellable {
   private boolean cancelled;
   private final Player player;
   private final Check check;
   private final String info;
   private final long timestamp = System.currentTimeMillis();
   private static final HandlerList handlers = new HandlerList();

   public VulcanPostFlagEvent(Player var1, Check var2, String var3) {
      super(true);
      this.player = var1;
      this.check = var2;
      this.info = var3;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean var1) {
      this.cancelled = var1;
   }

   public Player getPlayer() {
      return this.player;
   }

   public Check getCheck() {
      return this.check;
   }

   public String getInfo() {
      return this.info;
   }

   public long getTimestamp() {
      return this.timestamp;
   }
}
