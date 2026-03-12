package fr.xephi.authme.libs.com.google.common.hash;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.Immutable;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;

@Immutable
@ElementTypesAreNonnullByDefault
abstract class AbstractNonStreamingHashFunction extends AbstractHashFunction {
   public Hasher newHasher() {
      return this.newHasher(32);
   }

   public Hasher newHasher(int expectedInputSize) {
      Preconditions.checkArgument(expectedInputSize >= 0);
      return new AbstractNonStreamingHashFunction.BufferingHasher(expectedInputSize);
   }

   public HashCode hashInt(int input) {
      return this.hashBytes(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(input).array());
   }

   public HashCode hashLong(long input) {
      return this.hashBytes(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(input).array());
   }

   public HashCode hashUnencodedChars(CharSequence input) {
      int len = input.length();
      ByteBuffer buffer = ByteBuffer.allocate(len * 2).order(ByteOrder.LITTLE_ENDIAN);

      for(int i = 0; i < len; ++i) {
         buffer.putChar(input.charAt(i));
      }

      return this.hashBytes(buffer.array());
   }

   public HashCode hashString(CharSequence input, Charset charset) {
      return this.hashBytes(input.toString().getBytes(charset));
   }

   public abstract HashCode hashBytes(byte[] var1, int var2, int var3);

   public HashCode hashBytes(ByteBuffer input) {
      return this.newHasher(input.remaining()).putBytes(input).hash();
   }

   private static final class ExposedByteArrayOutputStream extends ByteArrayOutputStream {
      ExposedByteArrayOutputStream(int expectedInputSize) {
         super(expectedInputSize);
      }

      void write(ByteBuffer input) {
         int remaining = input.remaining();
         if (this.count + remaining > this.buf.length) {
            this.buf = Arrays.copyOf(this.buf, this.count + remaining);
         }

         input.get(this.buf, this.count, remaining);
         this.count += remaining;
      }

      byte[] byteArray() {
         return this.buf;
      }

      int length() {
         return this.count;
      }
   }

   private final class BufferingHasher extends AbstractHasher {
      final AbstractNonStreamingHashFunction.ExposedByteArrayOutputStream stream;

      BufferingHasher(int expectedInputSize) {
         this.stream = new AbstractNonStreamingHashFunction.ExposedByteArrayOutputStream(expectedInputSize);
      }

      public Hasher putByte(byte b) {
         this.stream.write(b);
         return this;
      }

      public Hasher putBytes(byte[] bytes, int off, int len) {
         this.stream.write(bytes, off, len);
         return this;
      }

      public Hasher putBytes(ByteBuffer bytes) {
         this.stream.write(bytes);
         return this;
      }

      public HashCode hash() {
         return AbstractNonStreamingHashFunction.this.hashBytes(this.stream.byteArray(), 0, this.stream.length());
      }
   }
}
