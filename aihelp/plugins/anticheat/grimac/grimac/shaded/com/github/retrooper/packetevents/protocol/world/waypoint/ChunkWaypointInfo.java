package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.waypoint;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ChunkWaypointInfo implements WaypointInfo {
   private final int chunkX;
   private final int chunkZ;

   public ChunkWaypointInfo(int chunkX, int chunkZ) {
      this.chunkX = chunkX;
      this.chunkZ = chunkZ;
   }

   @ApiStatus.Internal
   public static ChunkWaypointInfo read(PacketWrapper<?> wrapper) {
      return new ChunkWaypointInfo(wrapper.readVarInt(), wrapper.readVarInt());
   }

   @ApiStatus.Internal
   public static void write(PacketWrapper<?> wrapper, WaypointInfo info) {
      ChunkWaypointInfo chunkInfo = (ChunkWaypointInfo)info;
      wrapper.writeVarInt(chunkInfo.chunkX);
      wrapper.writeVarInt(chunkInfo.chunkZ);
   }

   public WaypointInfo.Type getType() {
      return WaypointInfo.Type.CHUNK;
   }

   public int getChunkX() {
      return this.chunkX;
   }

   public int getChunkZ() {
      return this.chunkZ;
   }
}
