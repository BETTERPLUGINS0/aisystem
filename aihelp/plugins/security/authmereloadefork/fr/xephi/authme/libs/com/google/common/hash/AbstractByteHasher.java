package fr.xephi.authme.libs.com.google.common.hash;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@ElementTypesAreNonnullByDefault
@CanIgnoreReturnValue
abstract class AbstractByteHasher extends AbstractHasher {
   private final ByteBuffer scratch;

   AbstractByteHasher() {
      this.scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
   }

   protected abstract void update(byte var1);

   protected void update(byte[] b) {
      this.update(b, 0, b.length);
   }

   protected void update(byte[] b, int off, int len) {
      for(int i = off; i < off + len; ++i) {
         this.update(b[i]);
      }

   }

   protected void update(ByteBuffer b) {
      if (b.hasArray()) {
         this.update(b.array(), b.arrayOffset() + b.position(), b.remaining());
         Java8Compatibility.position(b, b.limit());
      } else {
         for(int remaining = b.remaining(); remaining > 0; --remaining) {
            this.update(b.get());
         }
      }

   }

   private Hasher update(int bytes) {
      try {
         this.update(this.scratch.array(), 0, bytes);
      } finally {
         Java8Compatibility.clear(this.scratch);
      }

      return this;
   }

   public Hasher putByte(byte b) {
      this.update(b);
      return this;
   }

   public Hasher putBytes(byte[] bytes) {
      Preconditions.checkNotNull(bytes);
      this.update(bytes);
      return this;
   }

   public Hasher putBytes(byte[] bytes, int off, int len) {
      Preconditions.checkPositionIndexes(off, off + len, bytes.length);
      this.update(bytes, off, len);
      return this;
   }

   public Hasher putBytes(ByteBuffer bytes) {
      this.update(bytes);
      return this;
   }

   public Hasher putShort(short s) {
      this.scratch.putShort(s);
      return this.update((int)2);
   }

   public Hasher putInt(int i) {
      this.scratch.putInt(i);
      return this.update((int)4);
   }

   public Hasher putLong(long l) {
      this.scratch.putLong(l);
      return this.update((int)8);
   }

   public Hasher putChar(char c) {
      this.scratch.putChar(c);
      return this.update((int)2);
   }
}
