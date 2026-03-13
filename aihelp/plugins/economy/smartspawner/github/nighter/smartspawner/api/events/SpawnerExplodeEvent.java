package github.nighter.smartspawner.api.events;

import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpawnerExplodeEvent extends SpawnerBreakEvent {
   private static final HandlerList handlers = new HandlerList();
   private final boolean exploded;

   public SpawnerExplodeEvent(Entity entity, Location location, int quantity, boolean exploded) {
      super(entity, location, quantity);
      this.exploded = exploded;
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
   public boolean isExploded() {
      return this.exploded;
   }
}
