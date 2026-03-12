package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class BlockBoundingBox {
   private final int minX;
   private final int minY;
   private final int minZ;
   private final int maxX;
   private final int maxY;
   private final int maxZ;

   public BlockBoundingBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
      this.minX = Math.min(minX, maxX);
      this.minY = Math.min(minY, maxY);
      this.minZ = Math.min(minZ, maxZ);
      this.maxX = Math.max(maxX, minX);
      this.maxY = Math.max(maxY, minY);
      this.maxZ = Math.max(maxZ, minZ);
   }

   public static BlockBoundingBox read(PacketWrapper<?> wrapper) {
      Vector3i minPos = wrapper.readBlockPosition();
      Vector3i maxPos = wrapper.readBlockPosition();
      return new BlockBoundingBox(minPos.x, minPos.y, minPos.z, maxPos.x, maxPos.y, maxPos.z);
   }

   public static void write(PacketWrapper<?> wrapper, BlockBoundingBox box) {
      wrapper.writeBlockPosition(new Vector3i(box.minX, box.minY, box.minZ));
      wrapper.writeBlockPosition(new Vector3i(box.maxX, box.maxY, box.maxZ));
   }

   public int getMinX() {
      return this.minX;
   }

   public int getMinY() {
      return this.minY;
   }

   public int getMinZ() {
      return this.minZ;
   }

   public int getMaxX() {
      return this.maxX;
   }

   public int getMaxY() {
      return this.maxY;
   }

   public int getMaxZ() {
      return this.maxZ;
   }
}
