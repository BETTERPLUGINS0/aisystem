package github.nighter.smartspawner.api.events;

import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpawnerRemoveEvent extends SpawnerEvent implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;
   private final int changeAmount;
   private boolean cancelled = false;

   public SpawnerRemoveEvent(Player player, Location location, int newQuantity, int changeAmount) {
      super(location, newQuantity);
      this.player = player;
      this.changeAmount = changeAmount;
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
   public int getChangeAmount() {
      return this.changeAmount;
   }

   @Generated
   public boolean isCancelled() {
      return this.cancelled;
   }

   @Generated
   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }
}
