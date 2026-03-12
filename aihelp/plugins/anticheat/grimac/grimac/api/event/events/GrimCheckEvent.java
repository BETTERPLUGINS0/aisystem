package ac.grim.grimac.api.event.events;

import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.api.event.Cancellable;
import ac.grim.grimac.api.event.GrimEvent;
import lombok.Generated;

public abstract class GrimCheckEvent extends GrimEvent implements GrimUserEvent, Cancellable {
   private final GrimUser user;
   protected final AbstractCheck check;
   private boolean cancelled;

   public GrimCheckEvent(GrimUser user, AbstractCheck check) {
      super(true);
      this.user = user;
      this.check = check;
   }

   public GrimUser getUser() {
      return this.user;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public boolean isCancellable() {
      return true;
   }

   public double getViolations() {
      return this.check.getViolations();
   }

   public boolean isSetback() {
      return this.check.getViolations() > this.check.getSetbackVL();
   }

   @Generated
   public AbstractCheck getCheck() {
      return this.check;
   }
}
