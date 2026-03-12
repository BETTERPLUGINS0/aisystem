package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.io.OutputStream;

@ApiStatus.Internal
public class NetStreamOutputWrapper extends NetStreamOutput {
   private final PacketWrapper<?> wrapper;

   public NetStreamOutputWrapper(PacketWrapper<?> wrapper) {
      super((OutputStream)null);
      this.wrapper = wrapper;
   }

   public void write(int b) {
      this.wrapper.writeByte(b);
   }

   public void write(byte[] b) {
      this.write(b, 0, b.length);
   }

   public void write(byte[] b, int off, int len) {
      ByteBufHelper.writeBytes(this.wrapper.buffer, b, off, len);
   }

   public void flush() {
   }

   public void close() {
   }
}
