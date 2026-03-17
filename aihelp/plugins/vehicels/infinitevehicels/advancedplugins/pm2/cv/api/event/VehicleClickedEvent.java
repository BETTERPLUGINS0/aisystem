package advancedplugins.pm2.cv.api.event;

import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import me.coley.recaf.metadata.InsnComment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class VehicleClickedEvent extends VehicleEvent implements Cancellable {
   private static final HandlerList HANDLERS = new HandlerList();
   @NotNull
   private final Player player;
   @NotNull
   private final VehicleClickedEvent.ClickType clickType;
   private boolean cancelled;

   @InsnComment(
      At_1 = "8WY2NEMihFT3p0aCNVZ6hHSZ5mWVBnRTlXWU1UeJRVT4x2VVZkdS9kTRxkWUZjMIZEexFzN"
   )
   @NotNull
   public static HandlerList getHandlerList() {
      return HANDLERS;
   }

   public VehicleClickedEvent(@NotNull Vehicle var1, @NotNull Player var2, @NotNull VehicleClickedEvent.ClickType var3) {
      super(var1);
      this.player = var2;
      this.clickType = var3;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean var1) {
      this.cancelled = var1;
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS;
   }

   @NotNull
   public Player getPlayer() {
      return this.player;
   }

   @NotNull
   public VehicleClickedEvent.ClickType getClickType() {
      return this.clickType;
   }

   public static enum ClickType {
      LEFT_CLICK,
      LEFT_CLICK_CROUCHING,
      RIGHT_CLICK,
      RIGHT_CLICK_CROUCHING;

      public boolean isLeftClick() {
         return this == LEFT_CLICK || this == LEFT_CLICK_CROUCHING;
      }

      public boolean isRightClick() {
         return this == RIGHT_CLICK || this == RIGHT_CLICK_CROUCHING;
      }

      // $FF: synthetic method
      private static VehicleClickedEvent.ClickType[] $values() {
         return new VehicleClickedEvent.ClickType[]{LEFT_CLICK, LEFT_CLICK_CROUCHING, RIGHT_CLICK, RIGHT_CLICK_CROUCHING};
      }
   }
}
