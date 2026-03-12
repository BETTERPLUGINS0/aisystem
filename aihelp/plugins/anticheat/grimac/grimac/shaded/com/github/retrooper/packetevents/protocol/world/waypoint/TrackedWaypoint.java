package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.waypoint;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Either;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TrackedWaypoint {
   private final Either<UUID, String> identifier;
   private final WaypointIcon icon;
   private final WaypointInfo info;

   public TrackedWaypoint(Either<UUID, String> identifier, WaypointIcon icon, WaypointInfo info) {
      this.identifier = identifier;
      this.icon = icon;
      this.info = info;
   }

   public static TrackedWaypoint read(PacketWrapper<?> wrapper) {
      Either<UUID, String> identifier = wrapper.readEither(PacketWrapper::readUUID, PacketWrapper::readString);
      WaypointIcon icon = WaypointIcon.read(wrapper);
      WaypointInfo info = ((WaypointInfo.Type)wrapper.readEnum(WaypointInfo.Type.class)).read(wrapper);
      return new TrackedWaypoint(identifier, icon, info);
   }

   public static void write(PacketWrapper<?> wrapper, TrackedWaypoint waypoint) {
      wrapper.writeEither(waypoint.identifier, PacketWrapper::writeUUID, PacketWrapper::writeString);
      WaypointIcon.write(wrapper, waypoint.icon);
      wrapper.writeEnum(waypoint.info.getType());
      waypoint.info.getType().write(wrapper, waypoint.info);
   }

   public Either<UUID, String> getIdentifier() {
      return this.identifier;
   }

   public WaypointIcon getIcon() {
      return this.icon;
   }

   public WaypointInfo getInfo() {
      return this.info;
   }
}
