package ac.grim.grimac.api.events;

import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/** @deprecated */
@Deprecated(
   since = "1.2.1.0",
   forRemoval = true
)
public class GrimQuitEvent extends Event implements GrimUserEvent {
   private static final HandlerList handlers = new HandlerList();
   private final GrimUser user;

   public GrimQuitEvent(GrimUser user) {
      super(true);
      this.user = user;
   }

   public GrimUser getUser() {
      return this.user;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
