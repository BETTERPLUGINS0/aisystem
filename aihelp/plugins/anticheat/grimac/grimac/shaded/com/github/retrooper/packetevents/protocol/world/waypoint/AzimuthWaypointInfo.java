package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.waypoint;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AzimuthWaypointInfo implements WaypointInfo {
   private final float angle;

   public AzimuthWaypointInfo(float angle) {
      this.angle = angle;
   }

   @ApiStatus.Internal
   public static AzimuthWaypointInfo read(PacketWrapper<?> wrapper) {
      return new AzimuthWaypointInfo(wrapper.readFloat());
   }

   @ApiStatus.Internal
   public static void write(PacketWrapper<?> wrapper, WaypointInfo info) {
      wrapper.writeFloat(((AzimuthWaypointInfo)info).angle);
   }

   public WaypointInfo.Type getType() {
      return WaypointInfo.Type.AZIMUTH;
   }

   public float getAngle() {
      return this.angle;
   }
}
