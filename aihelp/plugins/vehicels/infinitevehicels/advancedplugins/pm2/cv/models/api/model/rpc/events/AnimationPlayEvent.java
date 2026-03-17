package advancedplugins.pm2.cv.models.api.model.rpc.events;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AnimationPlayEvent extends AbstractEvent implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final IVisualModel model;
   private final IAnimationProperty property;
   private boolean cancelled;

   public AnimationPlayEvent(IVisualModel var1, IAnimationProperty var2) {
      this.model = var1;
      this.property = var2;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return handlers;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public IVisualModel getModel() {
      return this.model;
   }

   public IAnimationProperty getProperty() {
      return this.property;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean var1) {
      this.cancelled = var1;
   }
}
