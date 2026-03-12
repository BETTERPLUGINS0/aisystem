package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DebugGameEventInfo {
   private final ResourceLocation event;
   private final Vector3d pos;

   public DebugGameEventInfo(ResourceLocation event, Vector3d pos) {
      this.event = event;
      this.pos = pos;
   }

   public static DebugGameEventInfo read(PacketWrapper<?> wrapper) {
      ResourceLocation event = ResourceLocation.read(wrapper);
      Vector3d pos = Vector3d.read(wrapper);
      return new DebugGameEventInfo(event, pos);
   }

   public static void write(PacketWrapper<?> wrapper, DebugGameEventInfo info) {
      ResourceLocation.write(wrapper, info.event);
      Vector3d.write(wrapper, info.pos);
   }

   public ResourceLocation getEvent() {
      return this.event;
   }

   public Vector3d getPos() {
      return this.pos;
   }
}
