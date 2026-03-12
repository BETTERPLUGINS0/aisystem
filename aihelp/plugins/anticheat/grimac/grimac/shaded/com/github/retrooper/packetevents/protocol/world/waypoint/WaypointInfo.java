package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.waypoint;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface WaypointInfo {
   WaypointInfo.Type getType();

   public static enum Type {
      EMPTY(EmptyWaypointInfo::read, EmptyWaypointInfo::write),
      VEC3I(Vec3iWaypointInfo::read, Vec3iWaypointInfo::write),
      CHUNK(ChunkWaypointInfo::read, ChunkWaypointInfo::write),
      AZIMUTH(AzimuthWaypointInfo::read, AzimuthWaypointInfo::write);

      private final PacketWrapper.Reader<WaypointInfo> reader;
      private final PacketWrapper.Writer<WaypointInfo> writer;

      private Type(PacketWrapper.Reader<WaypointInfo> reader, PacketWrapper.Writer<WaypointInfo> writer) {
         this.reader = reader;
         this.writer = writer;
      }

      @ApiStatus.Internal
      public WaypointInfo read(PacketWrapper<?> wrapper) {
         return (WaypointInfo)this.reader.apply(wrapper);
      }

      @ApiStatus.Internal
      public void write(PacketWrapper<?> wrapper, WaypointInfo info) {
         this.writer.accept(wrapper, info);
      }

      // $FF: synthetic method
      private static WaypointInfo.Type[] $values() {
         return new WaypointInfo.Type[]{EMPTY, VEC3I, CHUNK, AZIMUTH};
      }
   }
}
