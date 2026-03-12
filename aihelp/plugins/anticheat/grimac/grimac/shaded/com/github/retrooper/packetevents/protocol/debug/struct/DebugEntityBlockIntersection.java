package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DebugEntityBlockIntersection {
   private final DebugEntityBlockIntersection.IntersectionType type;

   public DebugEntityBlockIntersection(DebugEntityBlockIntersection.IntersectionType type) {
      this.type = type;
   }

   public static DebugEntityBlockIntersection read(PacketWrapper<?> wrapper) {
      DebugEntityBlockIntersection.IntersectionType type = (DebugEntityBlockIntersection.IntersectionType)wrapper.readEnum((Enum[])DebugEntityBlockIntersection.IntersectionType.values());
      return new DebugEntityBlockIntersection(type);
   }

   public static void write(PacketWrapper<?> wrapper, DebugEntityBlockIntersection intersection) {
      wrapper.writeEnum(intersection.type);
   }

   public DebugEntityBlockIntersection.IntersectionType getType() {
      return this.type;
   }

   public static enum IntersectionType {
      IN_BLOCK,
      IN_FLUID,
      IN_AIR;

      // $FF: synthetic method
      private static DebugEntityBlockIntersection.IntersectionType[] $values() {
         return new DebugEntityBlockIntersection.IntersectionType[]{IN_BLOCK, IN_FLUID, IN_AIR};
      }
   }
}
