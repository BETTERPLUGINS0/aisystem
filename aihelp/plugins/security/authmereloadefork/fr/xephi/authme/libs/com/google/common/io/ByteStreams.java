package fr.xephi.authme.libs.com.google.common.io;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.math.IntMath;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public final class ByteStreams {
   private static final int BUFFER_SIZE = 8192;
   private static final int ZERO_COPY_CHUNK_SIZE = 524288;
   private static final int MAX_ARRAY_LEN = 2147483639;
   private static final int TO_BYTE_ARRAY_DEQUE_SIZE = 20;
   private static final OutputStream NULL_OUTPUT_STREAM = new OutputStream() {
      public void write(int b) {
      }

      public void write(byte[] b) {
         Preconditions.checkNotNull(b);
      }

      public void write(byte[] b, int off, int len) {
         Preconditions.checkNotNull(b);
         Preconditions.checkPositionIndexes(off, off + len, b.length);
      }

      public String toString() {
         return "ByteStreams.nullOutputStream()";
      }
   };

   static byte[] createBuffer() {
      return new byte[8192];
   }

   private ByteStreams() {
   }

   @CanIgnoreReturnValue
   public static long copy(InputStream from, OutputStream to) throws IOException {
      Preconditions.checkNotNull(from);
      Preconditions.checkNotNull(to);
      byte[] buf = createBuffer();
      long total = 0L;

      while(true) {
         int r = from.read(buf);
         if (r == -1) {
            return total;
         }

         to.write(buf, 0, r);
         total += (long)r;
      }
   }

   @CanIgnoreReturnValue
   public static long copy(ReadableByteChannel from, WritableByteChannel to) throws IOException {
      Preconditions.checkNotNull(from);
      Preconditions.checkNotNull(to);
      long oldPosition;
      if (from instanceof FileChannel) {
         FileChannel sourceChannel = (FileChannel)from;
         oldPosition = sourceChannel.position();
         long position = oldPosition;

         long copied;
         do {
            do {
               copied = sourceChannel.transferTo(position, 524288L, to);
               position += copied;
               sourceChannel.position(position);
            } while(copied > 0L);
         } while(position < sourceChannel.size());

         return position - oldPosition;
      } else {
         ByteBuffer buf = ByteBuffer.wrap(createBuffer());
         oldPosition = 0L;

         while(from.read(buf) != -1) {
            Java8Compatibility.flip(buf);

            while(buf.hasRemaining()) {
               oldPosition += (long)to.write(buf);
            }

            Java8Compatibility.clear(buf);
         }

         return oldPosition;
      }
   }

   private static byte[] toByteArrayInternal(InputStream in, Queue<byte[]> bufs, int totalLen) throws IOException {
      int initialBufferSize = Math.min(8192, Math.max(128, Integer.highestOneBit(totalLen) * 2));

      for(int bufSize = initialBufferSize; totalLen < 2147483639; bufSize = IntMath.saturatedMultiply(bufSize, bufSize < 4096 ? 4 : 2)) {
         byte[] buf = new byte[Math.min(bufSize, 2147483639 - totalLen)];
         bufs.add(buf);

         int r;
         for(int off = 0; off < buf.length; totalLen += r) {
            r = in.read(buf, off, buf.length - off);
            if (r == -1) {
               return combineBuffers(bufs, totalLen);
            }

            off += r;
         }
      }

      if (in.read() == -1) {
         return combineBuffers(bufs, 2147483639);
      } else {
         throw new OutOfMemoryError("input is too large to fit in a byte array");
      }
   }

   private static byte[] combineBuffers(Queue<byte[]> bufs, int totalLen) {
      if (bufs.isEmpty()) {
         return new byte[0];
      } else {
         byte[] result = (byte[])bufs.remove();
         if (result.length == totalLen) {
            return result;
         } else {
            int remaining = totalLen - result.length;

            int bytesToCopy;
            for(result = Arrays.copyOf(result, totalLen); remaining > 0; remaining -= bytesToCopy) {
               byte[] buf = (byte[])bufs.remove();
               bytesToCopy = Math.min(remaining, buf.length);
               int resultOffset = totalLen - remaining;
               System.arraycopy(buf, 0, result, resultOffset, bytesToCopy);
            }

            return result;
         }
      }
   }

   public static byte[] toByteArray(InputStream in) throws IOException {
      Preconditions.checkNotNull(in);
      return toByteArrayInternal(in, new ArrayDeque(20), 0);
   }

   static byte[] toByteArray(InputStream in, long expectedSize) throws IOException {
      Preconditions.checkArgument(expectedSize >= 0L, "expectedSize (%s) must be non-negative", expectedSize);
      if (expectedSize > 2147483639L) {
         throw new OutOfMemoryError((new StringBuilder(62)).append(expectedSize).append(" bytes is too large to fit in a byte array").toString());
      } else {
         byte[] bytes = new byte[(int)expectedSize];

         int b;
         int read;
         for(int remaining = (int)expectedSize; remaining > 0; remaining -= read) {
            b = (int)expectedSize - remaining;
            read = in.read(bytes, b, remaining);
            if (read == -1) {
               return Arrays.copyOf(bytes, b);
            }
         }

         b = in.read();
         if (b == -1) {
            return bytes;
         } else {
            Queue<byte[]> bufs = new ArrayDeque(22);
            bufs.add(bytes);
            bufs.add(new byte[]{(byte)b});
            return toByteArrayInternal(in, bufs, bytes.length + 1);
         }
      }
   }

   @CanIgnoreReturnValue
   @Beta
   public static long exhaust(InputStream in) throws IOException {
      long total = 0L;

      long read;
      for(byte[] buf = createBuffer(); (read = (long)in.read(buf)) != -1L; total += read) {
      }

      return total;
   }

   @Beta
   public static ByteArrayDataInput newDataInput(byte[] bytes) {
      return newDataInput(new ByteArrayInputStream(bytes));
   }

   @Beta
   public static ByteArrayDataInput newDataInput(byte[] bytes, int start) {
      Preconditions.checkPositionIndex(start, bytes.length);
      return newDataInput(new ByteArrayInputStream(bytes, start, bytes.length - start));
   }

   @Beta
   public static ByteArrayDataInput newDataInput(ByteArrayInputStream byteArrayInputStream) {
      return new ByteStreams.ByteArrayDataInputStream((ByteArrayInputStream)Preconditions.checkNotNull(byteArrayInputStream));
   }

   @Beta
   public static ByteArrayDataOutput newDataOutput() {
      return newDataOutput(new ByteArrayOutputStream());
   }

   @Beta
   public static ByteArrayDataOutput newDataOutput(int size) {
      if (size < 0) {
         throw new IllegalArgumentException(String.format("Invalid size: %s", size));
      } else {
         return newDataOutput(new ByteArrayOutputStream(size));
      }
   }

   @Beta
   public static ByteArrayDataOutput newDataOutput(ByteArrayOutputStream byteArrayOutputStream) {
      return new ByteStreams.ByteArrayDataOutputStream((ByteArrayOutputStream)Preconditions.checkNotNull(byteArrayOutputStream));
   }

   @Beta
   public static OutputStream nullOutputStream() {
      return NULL_OUTPUT_STREAM;
   }

   @Beta
   public static InputStream limit(InputStream in, long limit) {
      return new ByteStreams.LimitedInputStream(in, limit);
   }

   @Beta
   public static void readFully(InputStream in, byte[] b) throws IOException {
      readFully(in, b, 0, b.length);
   }

   @Beta
   public static void readFully(InputStream in, byte[] b, int off, int len) throws IOException {
      int read = read(in, b, off, len);
      if (read != len) {
         throw new EOFException((new StringBuilder(81)).append("reached end of stream after reading ").append(read).append(" bytes; ").append(len).append(" bytes expected").toString());
      }
   }

   @Beta
   public static void skipFully(InputStream in, long n) throws IOException {
      long skipped = skipUpTo(in, n);
      if (skipped < n) {
         throw new EOFException((new StringBuilder(100)).append("reached end of stream after skipping ").append(skipped).append(" bytes; ").append(n).append(" bytes expected").toString());
      }
   }

   static long skipUpTo(InputStream in, long n) throws IOException {
      long totalSkipped = 0L;

      long skipped;
      for(byte[] buf = null; totalSkipped < n; totalSkipped += skipped) {
         long remaining = n - totalSkipped;
         skipped = skipSafely(in, remaining);
         if (skipped == 0L) {
            int skip = (int)Math.min(remaining, 8192L);
            if (buf == null) {
               buf = new byte[skip];
            }

            if ((skipped = (long)in.read(buf, 0, skip)) == -1L) {
               break;
            }
         }
      }

      return totalSkipped;
   }

   private static long skipSafely(InputStream in, long n) throws IOException {
      int available = in.available();
      return available == 0 ? 0L : in.skip(Math.min((long)available, n));
   }

   @ParametricNullness
   @Beta
   @CanIgnoreReturnValue
   public static <T> T readBytes(InputStream input, ByteProcessor<T> processor) throws IOException {
      Preconditions.checkNotNull(input);
      Preconditions.checkNotNull(processor);
      byte[] buf = createBuffer();

      int read;
      do {
         read = input.read(buf);
      } while(read != -1 && processor.processBytes(buf, 0, read));

      return processor.getResult();
   }

   @Beta
   @CanIgnoreReturnValue
   public static int read(InputStream in, byte[] b, int off, int len) throws IOException {
      Preconditions.checkNotNull(in);
      Preconditions.checkNotNull(b);
      if (len < 0) {
         throw new IndexOutOfBoundsException(String.format("len (%s) cannot be negative", len));
      } else {
         Preconditions.checkPositionIndexes(off, off + len, b.length);

         int total;
         int result;
         for(total = 0; total < len; total += result) {
            result = in.read(b, off + total, len - total);
            if (result == -1) {
               break;
            }
         }

         return total;
      }
   }

   private static final class LimitedInputStream extends FilterInputStream {
      private long left;
      private long mark = -1L;

      LimitedInputStream(InputStream in, long limit) {
         super(in);
         Preconditions.checkNotNull(in);
         Preconditions.checkArgument(limit >= 0L, "limit must be non-negative");
         this.left = limit;
      }

      public int available() throws IOException {
         return (int)Math.min((long)this.in.available(), this.left);
      }

      public synchronized void mark(int readLimit) {
         this.in.mark(readLimit);
         this.mark = this.left;
      }

      public int read() throws IOException {
         if (this.left == 0L) {
            return -1;
         } else {
            int result = this.in.read();
            if (result != -1) {
               --this.left;
            }

            return result;
         }
      }

      public int read(byte[] b, int off, int len) throws IOException {
         if (this.left == 0L) {
            return -1;
         } else {
            len = (int)Math.min((long)len, this.left);
            int result = this.in.read(b, off, len);
            if (result != -1) {
               this.left -= (long)result;
            }

            return result;
         }
      }

      public synchronized void reset() throws IOException {
         if (!this.in.markSupported()) {
            throw new IOException("Mark not supported");
         } else if (this.mark == -1L) {
            throw new IOException("Mark not set");
         } else {
            this.in.reset();
            this.left = this.mark;
         }
      }

      public long skip(long n) throws IOException {
         n = Math.min(n, this.left);
         long skipped = this.in.skip(n);
         this.left -= skipped;
         return skipped;
      }
   }

   private static class ByteArrayDataOutputStream implements ByteArrayDataOutput {
      final DataOutput output;
      final ByteArrayOutputStream byteArrayOutputStream;

      ByteArrayDataOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
         this.byteArrayOutputStream = byteArrayOutputStream;
         this.output = new DataOutputStream(byteArrayOutputStream);
      }

      public void write(int b) {
         try {
            this.output.write(b);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void write(byte[] b) {
         try {
            this.output.write(b);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void write(byte[] b, int off, int len) {
         try {
            this.output.write(b, off, len);
         } catch (IOException var5) {
            throw new AssertionError(var5);
         }
      }

      public void writeBoolean(boolean v) {
         try {
            this.output.writeBoolean(v);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeByte(int v) {
         try {
            this.output.writeByte(v);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeBytes(String s) {
         try {
            this.output.writeBytes(s);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeChar(int v) {
         try {
            this.output.writeChar(v);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeChars(String s) {
         try {
            this.output.writeChars(s);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeDouble(double v) {
         try {
            this.output.writeDouble(v);
         } catch (IOException var4) {
            throw new AssertionError(var4);
         }
      }

      public void writeFloat(float v) {
         try {
            this.output.writeFloat(v);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeInt(int v) {
         try {
            this.output.writeInt(v);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeLong(long v) {
         try {
            this.output.writeLong(v);
         } catch (IOException var4) {
            throw new AssertionError(var4);
         }
      }

      public void writeShort(int v) {
         try {
            this.output.writeShort(v);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeUTF(String s) {
         try {
            this.output.writeUTF(s);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public byte[] toByteArray() {
         return this.byteArrayOutputStream.toByteArray();
      }
   }

   private static class ByteArrayDataInputStream implements ByteArrayDataInput {
      final DataInput input;

      ByteArrayDataInputStream(ByteArrayInputStream byteArrayInputStream) {
         this.input = new DataInputStream(byteArrayInputStream);
      }

      public void readFully(byte[] b) {
         try {
            this.input.readFully(b);
         } catch (IOException var3) {
            throw new IllegalStateException(var3);
         }
      }

      public void readFully(byte[] b, int off, int len) {
         try {
            this.input.readFully(b, off, len);
         } catch (IOException var5) {
            throw new IllegalStateException(var5);
         }
      }

      public int skipBytes(int n) {
         try {
            return this.input.skipBytes(n);
         } catch (IOException var3) {
            throw new IllegalStateException(var3);
         }
      }

      public boolean readBoolean() {
         try {
            return this.input.readBoolean();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public byte readByte() {
         try {
            return this.input.readByte();
         } catch (EOFException var2) {
            throw new IllegalStateException(var2);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public int readUnsignedByte() {
         try {
            return this.input.readUnsignedByte();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public short readShort() {
         try {
            return this.input.readShort();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public int readUnsignedShort() {
         try {
            return this.input.readUnsignedShort();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public char readChar() {
         try {
            return this.input.readChar();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public int readInt() {
         try {
            return this.input.readInt();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public long readLong() {
         try {
            return this.input.readLong();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public float readFloat() {
         try {
            return this.input.readFloat();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public double readDouble() {
         try {
            return this.input.readDouble();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      @CheckForNull
      public String readLine() {
         try {
            return this.input.readLine();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public String readUTF() {
         try {
            return this.input.readUTF();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }
   }
}
