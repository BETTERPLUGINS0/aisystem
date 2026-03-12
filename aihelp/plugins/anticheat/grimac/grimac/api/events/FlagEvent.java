package ac.grim.grimac.api.events;

import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import lombok.Generated;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/** @deprecated */
@Deprecated(
   since = "1.2.1.0",
   forRemoval = true
)
public class FlagEvent extends Event implements GrimUserEvent, Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final GrimUser user;
   private final AbstractCheck check;
   private final String verbose;
   private boolean cancelled;

   public FlagEvent(GrimUser user, AbstractCheck check, String verbose) {
      super(true);
      this.user = user;
      this.check = check;
      this.verbose = verbose;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancel) {
      this.cancelled = cancel;
   }

   public double getViolations() {
      return this.check.getViolations();
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public boolean isSetback() {
      return this.check.getViolations() > this.check.getSetbackVL();
   }

   @Generated
   public GrimUser getUser() {
      return this.user;
   }

   @Generated
   public AbstractCheck getCheck() {
      return this.check;
   }

   @Generated
   public String getVerbose() {
      return this.verbose;
   }
}
