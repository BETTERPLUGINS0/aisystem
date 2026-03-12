package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DebugGameEventListenerInfo {
   private final int listenerRadius;

   public DebugGameEventListenerInfo(int listenerRadius) {
      this.listenerRadius = listenerRadius;
   }

   public static DebugGameEventListenerInfo read(PacketWrapper<?> wrapper) {
      int listenerRadius = wrapper.readVarInt();
      return new DebugGameEventListenerInfo(listenerRadius);
   }

   public static void write(PacketWrapper<?> wrapper, DebugGameEventListenerInfo info) {
      wrapper.writeVarInt(info.listenerRadius);
   }

   public int getListenerRadius() {
      return this.listenerRadius;
   }
}
