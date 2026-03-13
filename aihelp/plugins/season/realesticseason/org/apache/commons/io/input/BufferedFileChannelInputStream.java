package org.apache.commons.io.input;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public final class BufferedFileChannelInputStream extends InputStream {
   private final ByteBuffer byteBuffer;
   private final FileChannel fileChannel;
   private static final Class<?> DIRECT_BUFFER_CLASS = getDirectBufferClass();

   private static Class<?> getDirectBufferClass() {
      Class var0 = null;

      try {
         var0 = Class.forName("sun.nio.ch.DirectBuffer");
      } catch (ClassNotFoundException | IllegalAccessError var2) {
      }

      return var0;
   }

   private static boolean isDirectBuffer(Object var0) {
      return DIRECT_BUFFER_CLASS != null && DIRECT_BUFFER_CLASS.isInstance(var0);
   }

   public BufferedFileChannelInputStream(File var1) {
      this((File)var1, 8192);
   }

   public BufferedFileChannelInputStream(File var1, int var2) {
      this(var1.toPath(), var2);
   }

   public BufferedFileChannelInputStream(Path var1) {
      this((Path)var1, 8192);
   }

   public BufferedFileChannelInputStream(Path var1, int var2) {
      Objects.requireNonNull(var1, "path");
      this.fileChannel = FileChannel.open(var1, StandardOpenOption.READ);
      this.byteBuffer = ByteBuffer.allocateDirect(var2);
      this.byteBuffer.flip();
   }

   public synchronized int available() {
      return this.byteBuffer.remaining();
   }

   private void clean(ByteBuffer var1) {
      if (isDirectBuffer(var1)) {
         this.cleanDirectBuffer(var1);
      }

   }

   private void cleanDirectBuffer(ByteBuffer var1) {
      String var2 = System.getProperty("java.specification.version");
      Class var3;
      Method var4;
      if ("1.8".equals(var2)) {
         try {
            var3 = Class.forName("sun.misc.Cleaner");
            var4 = DIRECT_BUFFER_CLASS.getMethod("cleaner");
            Object var5 = var4.invoke(var1);
            if (var5 != null) {
               Method var6 = var3.getMethod("clean");
               var6.invoke(var5);
            }
         } catch (ReflectiveOperationException var8) {
            throw new IllegalStateException(var8);
         }
      } else {
         try {
            var3 = Class.forName("sun.misc.Unsafe");
            var4 = var3.getMethod("invokeCleaner", ByteBuffer.class);
            Field var9 = var3.getDeclaredField("theUnsafe");
            var9.setAccessible(true);
            var4.invoke(var9.get((Object)null), var1);
         } catch (ReflectiveOperationException var7) {
            throw new IllegalStateException(var7);
         }
      }

   }

   public synchronized void close() {
      try {
         this.fileChannel.close();
      } finally {
         this.clean(this.byteBuffer);
      }

   }

   public synchronized int read() {
      return !this.refill() ? -1 : this.byteBuffer.get() & 255;
   }

   public synchronized int read(byte[] var1, int var2, int var3) {
      if (var2 >= 0 && var3 >= 0 && var2 + var3 >= 0 && var2 + var3 <= var1.length) {
         if (!this.refill()) {
            return -1;
         } else {
            var3 = Math.min(var3, this.byteBuffer.remaining());
            this.byteBuffer.get(var1, var2, var3);
            return var3;
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   private boolean refill() {
      if (this.byteBuffer.hasRemaining()) {
         return true;
      } else {
         this.byteBuffer.clear();

         int var1;
         for(var1 = 0; var1 == 0; var1 = this.fileChannel.read(this.byteBuffer)) {
         }

         this.byteBuffer.flip();
         return var1 >= 0;
      }
   }

   public synchronized long skip(long var1) {
      if (var1 <= 0L) {
         return 0L;
      } else if ((long)this.byteBuffer.remaining() >= var1) {
         this.byteBuffer.position(this.byteBuffer.position() + (int)var1);
         return var1;
      } else {
         long var3 = (long)this.byteBuffer.remaining();
         long var5 = var1 - var3;
         this.byteBuffer.position(0);
         this.byteBuffer.flip();
         return var3 + this.skipFromFileChannel(var5);
      }
   }

   private long skipFromFileChannel(long var1) {
      long var3 = this.fileChannel.position();
      long var5 = this.fileChannel.size();
      if (var1 > var5 - var3) {
         this.fileChannel.position(var5);
         return var5 - var3;
      } else {
         this.fileChannel.position(var3 + var1);
         return var1;
      }
   }
}
