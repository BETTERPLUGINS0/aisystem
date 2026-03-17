package advancedplugins.pm2.cv.models.api.model.rpc.events;

import advancedplugins.pm2.cv.models.api.model.rpc.generator.ModelGenerator;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ModelRegistrationEvent extends AbstractEvent {
   private static final HandlerList handlers = new HandlerList();
   private final ModelGenerator.Phase phase;

   public ModelRegistrationEvent(ModelGenerator.Phase var1) {
      this.phase = var1;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return handlers;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public ModelGenerator.Phase getPhase() {
      return this.phase;
   }
}
