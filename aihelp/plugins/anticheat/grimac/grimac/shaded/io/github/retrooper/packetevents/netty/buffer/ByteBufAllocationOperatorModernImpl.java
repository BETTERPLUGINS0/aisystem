package ac.grim.grimac.shaded.io.github.retrooper.packetevents.netty.buffer;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufAllocationOperator;
import io.netty.buffer.Unpooled;

public class ByteBufAllocationOperatorModernImpl implements ByteBufAllocationOperator {
   public Object wrappedBuffer(byte[] bytes) {
      return Unpooled.wrappedBuffer(bytes);
   }

   public Object copiedBuffer(byte[] bytes) {
      return Unpooled.copiedBuffer(bytes);
   }

   public Object buffer() {
      return Unpooled.buffer();
   }

   public Object buffer(int initialCapacity) {
      return Unpooled.buffer(initialCapacity);
   }

   public Object directBuffer() {
      return Unpooled.directBuffer();
   }

   public Object directBuffer(int initialCapacity) {
      return Unpooled.directBuffer(initialCapacity);
   }

   public Object compositeBuffer() {
      return Unpooled.compositeBuffer();
   }

   public Object compositeBuffer(int maxNumComponents) {
      return Unpooled.compositeBuffer(maxNumComponents);
   }

   public Object emptyBuffer() {
      return Unpooled.EMPTY_BUFFER;
   }
}
