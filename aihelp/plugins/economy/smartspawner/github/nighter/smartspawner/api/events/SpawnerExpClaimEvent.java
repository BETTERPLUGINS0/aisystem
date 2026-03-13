package github.nighter.smartspawner.api.events;

import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpawnerExpClaimEvent extends Event implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;
   private final Location location;
   private int expAmount;
   private boolean cancelled = false;

   public SpawnerExpClaimEvent(Player player, Location location, int expAmount) {
      this.player = player;
      this.location = location;
      this.expAmount = expAmount;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return handlers;
   }

   @Generated
   public Player getPlayer() {
      return this.player;
   }

   @Generated
   public Location getLocation() {
      return this.location;
   }

   @Generated
   public int getExpAmount() {
      return this.expAmount;
   }

   @Generated
   public boolean isCancelled() {
      return this.cancelled;
   }

   @Generated
   public void setExpAmount(int expAmount) {
      this.expAmount = expAmount;
   }

   @Generated
   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }
}
