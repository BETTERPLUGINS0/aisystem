package github.nighter.smartspawner.api.events;

import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpawnerEggChangeEvent extends Event implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;
   private final Location location;
   private final EntityType oldEntityType;
   private final EntityType newEntityType;
   private boolean cancelled = false;

   public SpawnerEggChangeEvent(Player player, Location location, EntityType oldEntityType, EntityType newEntityType) {
      this.player = player;
      this.location = location;
      this.oldEntityType = oldEntityType;
      this.newEntityType = newEntityType;
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
   public EntityType getOldEntityType() {
      return this.oldEntityType;
   }

   @Generated
   public EntityType getNewEntityType() {
      return this.newEntityType;
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
