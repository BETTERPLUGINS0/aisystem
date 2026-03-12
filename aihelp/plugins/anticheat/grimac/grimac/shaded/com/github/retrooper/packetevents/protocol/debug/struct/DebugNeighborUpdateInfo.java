package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DebugNeighborUpdateInfo {
   private final Vector3i pos;

   public DebugNeighborUpdateInfo(Vector3i pos) {
      this.pos = pos;
   }

   public static DebugNeighborUpdateInfo read(PacketWrapper<?> wrapper) {
      Vector3i pos = wrapper.readBlockPosition();
      return new DebugNeighborUpdateInfo(pos);
   }

   public static void write(PacketWrapper<?> wrapper, DebugNeighborUpdateInfo info) {
      wrapper.writeBlockPosition(info.pos);
   }

   public Vector3i getPos() {
      return this.pos;
   }
}
