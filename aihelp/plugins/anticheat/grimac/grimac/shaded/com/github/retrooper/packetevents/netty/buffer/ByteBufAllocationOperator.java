package ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer;

public interface ByteBufAllocationOperator {
   Object wrappedBuffer(byte[] bytes);

   Object copiedBuffer(byte[] bytes);

   Object buffer();

   Object buffer(int initialCapacity);

   Object directBuffer();

   Object directBuffer(int initialCapacity);

   Object compositeBuffer();

   Object compositeBuffer(int maxNumComponents);

   Object emptyBuffer();
}
