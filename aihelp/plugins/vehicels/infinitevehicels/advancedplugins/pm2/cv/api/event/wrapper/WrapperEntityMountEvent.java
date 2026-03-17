package advancedplugins.pm2.cv.api.event.wrapper;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public class WrapperEntityMountEvent extends EntityEvent implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private boolean cancelled;
   private final Entity mount;

   public WrapperEntityMountEvent(@NotNull Entity var1, @NotNull Entity var2) {
      super(var1);
      this.mount = var2;
   }

   @NotNull
   public Entity getMount() {
      return this.mount;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return handlers;
   }

   public void setCancelled(boolean var1) {
      this.cancelled = var1;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }
}
