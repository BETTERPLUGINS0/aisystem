package advancedplugins.pm2.cv.models.api.model.rpc.events;

import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AddModelEvent extends AbstractEvent implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final IModelContainer target;
   private final IVisualModel model;
   private boolean overrideHitbox;
   private boolean cancelled;

   public AddModelEvent(IModelContainer var1, IVisualModel var2) {
      this.target = var1;
      this.model = var2;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return handlers;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public IModelContainer getTarget() {
      return this.target;
   }

   public IVisualModel getModel() {
      return this.model;
   }

   public boolean isOverrideHitbox() {
      return this.overrideHitbox;
   }

   public void setOverrideHitbox(boolean var1) {
      this.overrideHitbox = var1;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean var1) {
      this.cancelled = var1;
   }
}
