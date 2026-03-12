package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.path;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DebugNode {
   private final Vector3i pos;
   private final float walkedDistance;
   private final float costMalus;
   private final boolean closed;
   private final DebugPathType type;
   private final float f;

   public DebugNode(Vector3i pos, float walkedDistance, float costMalus, boolean closed, DebugPathType type, float f) {
      this.pos = pos;
      this.walkedDistance = walkedDistance;
      this.costMalus = costMalus;
      this.closed = closed;
      this.type = type;
      this.f = f;
   }

   public static DebugNode read(PacketWrapper<?> wrapper) {
      Vector3i pos = new Vector3i(wrapper.readInt(), wrapper.readInt(), wrapper.readInt());
      float walkedDistance = wrapper.readFloat();
      float costMalus = wrapper.readFloat();
      boolean closed = wrapper.readBoolean();
      DebugPathType type = (DebugPathType)wrapper.readEnum((Enum[])DebugPathType.values());
      float f = wrapper.readFloat();
      return new DebugNode(pos, walkedDistance, costMalus, closed, type, f);
   }

   public static void write(PacketWrapper<?> wrapper, DebugNode node) {
      wrapper.writeInt(node.pos.x);
      wrapper.writeInt(node.pos.y);
      wrapper.writeInt(node.pos.z);
      wrapper.writeFloat(node.walkedDistance);
      wrapper.writeFloat(node.costMalus);
      wrapper.writeBoolean(node.closed);
      wrapper.writeEnum(node.type);
      wrapper.writeFloat(node.f);
   }

   public Vector3i getPos() {
      return this.pos;
   }

   public float getWalkedDistance() {
      return this.walkedDistance;
   }

   public float getCostMalus() {
      return this.costMalus;
   }

   public boolean isClosed() {
      return this.closed;
   }

   public DebugPathType getType() {
      return this.type;
   }

   public float getF() {
      return this.f;
   }
}
