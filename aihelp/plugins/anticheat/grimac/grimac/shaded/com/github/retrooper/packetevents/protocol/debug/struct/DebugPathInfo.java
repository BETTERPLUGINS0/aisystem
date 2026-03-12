package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.path.DebugPath;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DebugPathInfo {
   private final DebugPath path;
   private final float maxNodeDistance;

   public DebugPathInfo(DebugPath path, float maxNodeDistance) {
      this.path = path;
      this.maxNodeDistance = maxNodeDistance;
   }

   public static DebugPathInfo read(PacketWrapper<?> wrapper) {
      DebugPath path = DebugPath.read(wrapper);
      float maxNodeDistance = wrapper.readFloat();
      return new DebugPathInfo(path, maxNodeDistance);
   }

   public static void write(PacketWrapper<?> wrapper, DebugPathInfo info) {
      DebugPath.write(wrapper, info.path);
      wrapper.writeFloat(info.maxNodeDistance);
   }

   public DebugPath getPath() {
      return this.path;
   }

   public float getMaxNodeDistance() {
      return this.maxNodeDistance;
   }
}
