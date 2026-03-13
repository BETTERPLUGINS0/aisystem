package github.nighter.smartspawner.api.events;

import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpawnerBreakEvent extends SpawnerEvent {
   private static final HandlerList handlers = new HandlerList();
   private final Entity entity;

   public SpawnerBreakEvent(Entity entity, Location location, int quantity) {
      super(location, quantity);
      this.entity = entity;
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
   public Entity getEntity() {
      return this.entity;
   }
}
