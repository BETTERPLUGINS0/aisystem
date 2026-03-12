package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.poi.PoiType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.poi.PoiTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DebugPoiInfo {
   private final Vector3i pos;
   private final PoiType poiType;
   private final int freeTicketCount;

   public DebugPoiInfo(Vector3i pos, PoiType poiType, int freeTicketCount) {
      this.pos = pos;
      this.poiType = poiType;
      this.freeTicketCount = freeTicketCount;
   }

   public static DebugPoiInfo read(PacketWrapper<?> wrapper) {
      Vector3i pos = wrapper.readBlockPosition();
      PoiType poiType = (PoiType)wrapper.readMappedEntity((IRegistry)PoiTypes.getRegistry());
      int freeTicketCount = wrapper.readVarInt();
      return new DebugPoiInfo(pos, poiType, freeTicketCount);
   }

   public static void write(PacketWrapper<?> wrapper, DebugPoiInfo info) {
      wrapper.writeBlockPosition(info.pos);
      wrapper.writeMappedEntity(info.poiType);
      wrapper.writeVarInt(info.freeTicketCount);
   }

   public Vector3i getPos() {
      return this.pos;
   }

   public PoiType getPoiType() {
      return this.poiType;
   }

   public int getFreeTicketCount() {
      return this.freeTicketCount;
   }
}
