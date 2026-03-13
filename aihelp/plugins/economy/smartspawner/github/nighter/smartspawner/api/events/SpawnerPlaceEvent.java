package github.nighter.smartspawner.api.events;

import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpawnerPlaceEvent extends SpawnerEvent implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;
   private final EntityType entityType;
   private boolean cancelled = false;

   public SpawnerPlaceEvent(Player player, Location location, EntityType entityType, int quantity) {
      super(location, quantity);
      this.player = player;
      this.entityType = entityType;
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
   public EntityType getEntityType() {
      return this.entityType;
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
