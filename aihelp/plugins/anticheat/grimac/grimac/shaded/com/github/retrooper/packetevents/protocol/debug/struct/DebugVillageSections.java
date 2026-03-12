package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DebugVillageSections {
   public static final DebugVillageSections INSTANCE = new DebugVillageSections();

   private DebugVillageSections() {
   }

   public static DebugVillageSections read(PacketWrapper<?> wrapper) {
      return INSTANCE;
   }

   public static void write(PacketWrapper<?> wrapper, DebugVillageSections sections) {
   }
}
