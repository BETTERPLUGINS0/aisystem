package ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;

public class UnpooledByteBufAllocationHelper {
   public static Object wrappedBuffer(byte[] bytes) {
      return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().wrappedBuffer(bytes);
   }

   public static Object copiedBuffer(byte[] bytes) {
      return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().copiedBuffer(bytes);
   }

   public static Object buffer() {
      return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().buffer();
   }

   public static Object directBuffer() {
      return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().directBuffer();
   }

   public static Object compositeBuffer() {
      return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().compositeBuffer();
   }

   public static Object buffer(int initialCapacity) {
      return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().buffer(initialCapacity);
   }

   public static Object directBuffer(int initialCapacity) {
      return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().directBuffer(initialCapacity);
   }

   public static Object compositeBuffer(int maxNumComponents) {
      return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().compositeBuffer(maxNumComponents);
   }

   public static Object emptyBuffer() {
      return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().emptyBuffer();
   }
}
