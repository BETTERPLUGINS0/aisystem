package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.waypoint;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EmptyWaypointInfo implements WaypointInfo {
   public static final EmptyWaypointInfo EMPTY = new EmptyWaypointInfo();

   private EmptyWaypointInfo() {
   }

   @ApiStatus.Internal
   public static EmptyWaypointInfo read(PacketWrapper<?> wrapper) {
      return EMPTY;
   }

   @ApiStatus.Internal
   public static void write(PacketWrapper<?> wrapper, WaypointInfo info) {
   }

   public WaypointInfo.Type getType() {
      return WaypointInfo.Type.EMPTY;
   }
}
