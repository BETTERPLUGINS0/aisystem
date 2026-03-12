package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.io.InputStream;

@ApiStatus.Internal
public class NetStreamInputWrapper extends NetStreamInput {
   private final PacketWrapper<?> wrapper;

   public NetStreamInputWrapper(PacketWrapper<?> wrapper) {
      super((InputStream)null);
      this.wrapper = wrapper;
   }

   public int read() {
      return this.wrapper.readUnsignedByte();
   }

   public int read(byte[] b) {
      return this.read(b, 0, b.length);
   }

   public int read(byte[] b, int off, int len) {
      int ri = ByteBufHelper.readerIndex(this.wrapper.buffer);
      ByteBufHelper.readBytes(this.wrapper.buffer, b, off, len);
      return ByteBufHelper.readerIndex(this.wrapper.buffer) - ri;
   }

   public long skip(long n) {
      int ri = ByteBufHelper.readerIndex(this.wrapper.buffer);
      ByteBufHelper.skipBytes(this.wrapper.buffer, (int)n);
      return (long)(ByteBufHelper.readerIndex(this.wrapper.buffer) - ri);
   }

   public int available() {
      return ByteBufHelper.readableBytes(this.wrapper.buffer);
   }

   public void close() {
   }

   public void mark(int readlimit) {
      throw new UnsupportedOperationException();
   }

   public void reset() {
      throw new UnsupportedOperationException();
   }

   public boolean markSupported() {
      return false;
   }

   public PacketWrapper<?> getWrapper() {
      return this.wrapper;
   }
}
