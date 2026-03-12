package ac.grim.grimac.api.event;

import lombok.Generated;

public abstract class GrimEvent {
   private boolean cancelled;
   private final boolean async;

   protected GrimEvent() {
      this(false);
   }

   protected GrimEvent(boolean async) {
      this.cancelled = false;
      this.async = async;
   }

   public void setCancelled(boolean cancelled) {
      if (!this.isCancellable()) {
         throw new IllegalStateException("Event " + this.getEventName() + " is not cancellable");
      } else {
         this.cancelled = cancelled;
      }
   }

   public boolean isCancellable() {
      return false;
   }

   public String getEventName() {
      return this.getClass().getSimpleName();
   }

   @Generated
   public boolean isCancelled() {
      return this.cancelled;
   }
}
