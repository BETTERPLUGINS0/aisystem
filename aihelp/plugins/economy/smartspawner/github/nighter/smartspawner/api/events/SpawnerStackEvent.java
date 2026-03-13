package github.nighter.smartspawner.api.events;

import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpawnerStackEvent extends Event implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;
   private final Location location;
   private final int oldStackSize;
   private final int newStackSize;
   private final SpawnerStackEvent.StackSource source;
   private boolean cancelled;

   public SpawnerStackEvent(Player player, Location location, int oldStackSize, int newStackSize, SpawnerStackEvent.StackSource source) {
      this.cancelled = false;
      this.player = player;
      this.location = location;
      this.oldStackSize = oldStackSize;
      this.newStackSize = newStackSize;
      this.source = source;
   }

   public SpawnerStackEvent(Player player, Location location, int oldStackSize, int newStackSize) {
      this(player, location, oldStackSize, newStackSize, SpawnerStackEvent.StackSource.PLACE);
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
   public int getOldStackSize() {
      return this.oldStackSize;
   }

   @Generated
   public int getNewStackSize() {
      return this.newStackSize;
   }

   @Generated
   public SpawnerStackEvent.StackSource getSource() {
      return this.source;
   }

   @Generated
   public boolean isCancelled() {
      return this.cancelled;
   }

   @Generated
   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public static enum StackSource {
      PLACE,
      GUI;

      // $FF: synthetic method
      private static SpawnerStackEvent.StackSource[] $values() {
         return new SpawnerStackEvent.StackSource[]{PLACE, GUI};
      }
   }
}
