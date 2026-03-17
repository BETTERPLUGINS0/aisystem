package advancedplugins.pm2.cv.models.api.model.rpc.events;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AnimationEndEvent extends AbstractEvent {
   private static final HandlerList handlers = new HandlerList();
   private final IVisualModel model;
   private final IAnimationProperty property;

   public AnimationEndEvent(IVisualModel var1, IAnimationProperty var2) {
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
}
