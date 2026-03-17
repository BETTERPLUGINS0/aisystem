package advancedplugins.pm2.cv.api.event;

import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class VehicleMoveEvent extends VehicleEvent {
   private static final HandlerList HANDLERS = new HandlerList();
   @NotNull
   private final Location from;
   @NotNull
   private final Location to;

   public VehicleMoveEvent(@NotNull Vehicle var1, boolean var2, @NotNull Location var3, @NotNull Location var4) {
      super(var1, var2);
      this.from = var3;
      this.to = var4;
   }

   public VehicleMoveEvent(@NotNull Vehicle var1, @NotNull Location var2, @NotNull Location var3) {
      this(var1, !Bukkit.isPrimaryThread(), var2, var3);
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return HANDLERS;
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS;
   }

   @NotNull
   public Location getFrom() {
      return this.from;
   }

   @NotNull
   public Location getTo() {
      return this.to;
   }
}
