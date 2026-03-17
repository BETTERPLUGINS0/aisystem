package advancedplugins.pm2.cv.models.api.model.rpc.events;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ModelDismountEvent extends AbstractEvent implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final IVisualModel vehicle;
   private final Entity passenger;
   private final boolean isDriver;
   private final Mount seat;
   private boolean cancelled;

   public ModelDismountEvent(IVisualModel var1, Entity var2, boolean var3, Mount var4) {
      this.vehicle = var1;
      this.passenger = var2;
      this.isDriver = var3;
      this.seat = var4;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return handlers;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public IVisualModel getVehicle() {
      return this.vehicle;
   }

   public Entity getPassenger() {
      return this.passenger;
   }

   public boolean isDriver() {
      return this.isDriver;
   }

   public Mount getSeat() {
      return this.seat;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean var1) {
      this.cancelled = var1;
   }
}
