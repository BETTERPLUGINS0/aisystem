package fr.xephi.authme.libs.com.google.common.hash;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@ElementTypesAreNonnullByDefault
@CanIgnoreReturnValue
abstract class AbstractStreamingHasher extends AbstractHasher {
   private final ByteBuffer buffer;
   private final int bufferSize;
   private final int chunkSize;

   protected AbstractStreamingHasher(int chunkSize) {
      this(chunkSize, chunkSize);
   }

   protected AbstractStreamingHasher(int chunkSize, int bufferSize) {
      Preconditions.checkArgument(bufferSize % chunkSize == 0);
      this.buffer = ByteBuffer.allocate(bufferSize + 7).order(ByteOrder.LITTLE_ENDIAN);
      this.bufferSize = bufferSize;
      this.chunkSize = chunkSize;
   }

   protected abstract void process(ByteBuffer var1);

   protected void processRemaining(ByteBuffer bb) {
      Java8Compatibility.position(bb, bb.limit());
      Java8Compatibility.limit(bb, this.chunkSize + 7);

      while(bb.position() < this.chunkSize) {
         bb.putLong(0L);
      }

      Java8Compatibility.limit(bb, this.chunkSize);
      Java8Compatibility.flip(bb);
      this.process(bb);
   }

   public final Hasher putBytes(byte[] bytes, int off, int len) {
      return this.putBytesInternal(ByteBuffer.wrap(bytes, off, len).order(ByteOrder.LITTLE_ENDIAN));
   }

   public final Hasher putBytes(ByteBuffer readBuffer) {
      ByteOrder order = readBuffer.order();

      Hasher var3;
      try {
         readBuffer.order(ByteOrder.LITTLE_ENDIAN);
         var3 = this.putBytesInternal(readBuffer);
      } finally {
         readBuffer.order(order);
      }

      return var3;
   }

   private Hasher putBytesInternal(ByteBuffer readBuffer) {
      if (readBuffer.remaining() <= this.buffer.remaining()) {
         this.buffer.put(readBuffer);
         this.munchIfFull();
         return this;
      } else {
         int bytesToCopy = this.bufferSize - this.buffer.position();

         for(int i = 0; i < bytesToCopy; ++i) {
            this.buffer.put(readBuffer.get());
         }

         this.munch();

         while(readBuffer.remaining() >= this.chunkSize) {
            this.process(readBuffer);
         }

         this.buffer.put(readBuffer);
         return this;
      }
   }

   public final Hasher putByte(byte b) {
      this.buffer.put(b);
      this.munchIfFull();
      return this;
   }

   public final Hasher putShort(short s) {
      this.buffer.putShort(s);
      this.munchIfFull();
      return this;
   }

   public final Hasher putChar(char c) {
      this.buffer.putChar(c);
      this.munchIfFull();
      return this;
   }

   public final Hasher putInt(int i) {
      this.buffer.putInt(i);
      this.munchIfFull();
      return this;
   }

   public final Hasher putLong(long l) {
      this.buffer.putLong(l);
      this.munchIfFull();
      return this;
   }

   public final HashCode hash() {
      this.munch();
      Java8Compatibility.flip(this.buffer);
      if (this.buffer.remaining() > 0) {
         this.processRemaining(this.buffer);
         Java8Compatibility.position(this.buffer, this.buffer.limit());
      }

      return this.makeHash();
   }

   protected abstract HashCode makeHash();

   private void munchIfFull() {
      if (this.buffer.remaining() < 8) {
         this.munch();
      }

   }

   private void munch() {
      Java8Compatibility.flip(this.buffer);

      while(this.buffer.remaining() >= this.chunkSize) {
         this.process(this.buffer);
      }

      this.buffer.compact();
   }
}
