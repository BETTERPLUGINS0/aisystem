package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.waypoint;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Vec3iWaypointInfo implements WaypointInfo {
   private final Vector3i position;

   public Vec3iWaypointInfo(Vector3i position) {
      this.position = position;
   }

   @ApiStatus.Internal
   public static Vec3iWaypointInfo read(PacketWrapper<?> wrapper) {
      return new Vec3iWaypointInfo(Vector3i.read(wrapper));
   }

   @ApiStatus.Internal
   public static void write(PacketWrapper<?> wrapper, WaypointInfo info) {
      Vector3i.write(wrapper, ((Vec3iWaypointInfo)info).position);
   }

   public WaypointInfo.Type getType() {
      return WaypointInfo.Type.VEC3I;
   }

   public Vector3i getPosition() {
      return this.position;
   }
}
