package ac.grim.grimac.api.events;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import lombok.Generated;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/** @deprecated */
@Deprecated(
   since = "1.2.1.0",
   forRemoval = true
)
public class GrimReloadEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final boolean success;

   public GrimReloadEvent(boolean success) {
      super(true);
      this.success = success;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   @Generated
   public boolean isSuccess() {
      return this.success;
   }
}
