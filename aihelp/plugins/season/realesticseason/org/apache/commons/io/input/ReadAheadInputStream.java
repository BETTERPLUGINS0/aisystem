package org.apache.commons.io.input;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReadAheadInputStream extends InputStream {
   private static final ThreadLocal<byte[]> oneByte = ThreadLocal.withInitial(() -> {
      return new byte[1];
   });
   private final ReentrantLock stateChangeLock;
   private ByteBuffer activeBuffer;
   private ByteBuffer readAheadBuffer;
   private boolean endOfStream;
   private boolean readInProgress;
   private boolean readAborted;
   private Throwable readException;
   private boolean isClosed;
   private boolean isUnderlyingInputStreamBeingClosed;
   private boolean isReading;
   private final AtomicBoolean isWaiting;
   private final InputStream underlyingInputStream;
   private final ExecutorService executorService;
   private final boolean shutdownExecutorService;
   private final Condition asyncReadComplete;

   private static ExecutorService newExecutorService() {
      return Executors.newSingleThreadExecutor(ReadAheadInputStream::newThread);
   }

   private static Thread newThread(Runnable var0) {
      Thread var1 = new Thread(var0, "commons-io-read-ahead");
      var1.setDaemon(true);
      return var1;
   }

   public ReadAheadInputStream(InputStream var1, int var2) {
      this(var1, var2, newExecutorService(), true);
   }

   public ReadAheadInputStream(InputStream var1, int var2, ExecutorService var3) {
      this(var1, var2, var3, false);
   }

   private ReadAheadInputStream(InputStream var1, int var2, ExecutorService var3, boolean var4) {
      this.stateChangeLock = new ReentrantLock();
      this.isWaiting = new AtomicBoolean(false);
      this.asyncReadComplete = this.stateChangeLock.newCondition();
      if (var2 <= 0) {
         throw new IllegalArgumentException("bufferSizeInBytes should be greater than 0, but the value is " + var2);
      } else {
         this.executorService = (ExecutorService)Objects.requireNonNull(var3, "executorService");
         this.underlyingInputStream = (InputStream)Objects.requireNonNull(var1, "inputStream");
         this.shutdownExecutorService = var4;
         this.activeBuffer = ByteBuffer.allocate(var2);
         this.readAheadBuffer = ByteBuffer.allocate(var2);
         this.activeBuffer.flip();
         this.readAheadBuffer.flip();
      }
   }

   public int available() {
      this.stateChangeLock.lock();

      int var1;
      try {
         var1 = (int)Math.min(2147483647L, (long)this.activeBuffer.remaining() + (long)this.readAheadBuffer.remaining());
      } finally {
         this.stateChangeLock.unlock();
      }

      return var1;
   }

   private void checkReadException() {
      if (this.readAborted) {
         if (this.readException instanceof IOException) {
            throw (IOException)this.readException;
         } else {
            throw new IOException(this.readException);
         }
      }
   }

   public void close() {
      boolean var1 = false;
      this.stateChangeLock.lock();

      try {
         if (this.isClosed) {
            return;
         }

         this.isClosed = true;
         if (!this.isReading) {
            var1 = true;
            this.isUnderlyingInputStreamBeingClosed = true;
         }
      } finally {
         this.stateChangeLock.unlock();
      }

      if (this.shutdownExecutorService) {
         try {
            this.executorService.shutdownNow();
            this.executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
         } catch (InterruptedException var11) {
            InterruptedIOException var3 = new InterruptedIOException(var11.getMessage());
            var3.initCause(var11);
            throw var3;
         } finally {
            if (var1) {
               this.underlyingInputStream.close();
            }

         }
      }

   }

   private void closeUnderlyingInputStreamIfNecessary() {
      boolean var1 = false;
      this.stateChangeLock.lock();

      try {
         this.isReading = false;
         if (this.isClosed && !this.isUnderlyingInputStreamBeingClosed) {
            var1 = true;
         }
      } finally {
         this.stateChangeLock.unlock();
      }

      if (var1) {
         try {
            this.underlyingInputStream.close();
         } catch (IOException var5) {
         }
      }

   }

   private boolean isEndOfStream() {
      return !this.activeBuffer.hasRemaining() && !this.readAheadBuffer.hasRemaining() && this.endOfStream;
   }

   public int read() {
      if (this.activeBuffer.hasRemaining()) {
         return this.activeBuffer.get() & 255;
      } else {
         byte[] var1 = (byte[])oneByte.get();
         return this.read(var1, 0, 1) == -1 ? -1 : var1[0] & 255;
      }
   }

   public int read(byte[] var1, int var2, int var3) {
      if (var2 >= 0 && var3 >= 0 && var3 <= var1.length - var2) {
         if (var3 == 0) {
            return 0;
         } else {
            if (!this.activeBuffer.hasRemaining()) {
               this.stateChangeLock.lock();

               try {
                  this.waitForAsyncReadComplete();
                  if (!this.readAheadBuffer.hasRemaining()) {
                     this.readAsync();
                     this.waitForAsyncReadComplete();
                     if (this.isEndOfStream()) {
                        byte var4 = -1;
                        return var4;
                     }
                  }

                  this.swapBuffers();
                  this.readAsync();
               } finally {
                  this.stateChangeLock.unlock();
               }
            }

            var3 = Math.min(var3, this.activeBuffer.remaining());
            this.activeBuffer.get(var1, var2, var3);
            return var3;
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   private void readAsync() {
      this.stateChangeLock.lock();

      byte[] var1;
      label44: {
         try {
            var1 = this.readAheadBuffer.array();
            if (!this.endOfStream && !this.readInProgress) {
               this.checkReadException();
               this.readAheadBuffer.position(0);
               this.readAheadBuffer.flip();
               this.readInProgress = true;
               break label44;
            }
         } finally {
            this.stateChangeLock.unlock();
         }

         return;
      }

      this.executorService.execute(() -> {
         this.stateChangeLock.lock();

         label550: {
            try {
               if (!this.isClosed) {
                  this.isReading = true;
                  break label550;
               }

               this.readInProgress = false;
            } finally {
               this.stateChangeLock.unlock();
            }

            return;
         }

         int var2 = 0;
         int var3 = 0;
         int var4 = var1.length;
         Throwable var5 = null;

         try {
            do {
               var2 = this.underlyingInputStream.read(var1, var3, var4);
               if (var2 <= 0) {
                  break;
               }

               var3 += var2;
               var4 -= var2;
            } while(var4 > 0 && !this.isWaiting.get());
         } catch (Throwable var39) {
            var5 = var39;
            if (var39 instanceof Error) {
               throw (Error)var39;
            }
         } finally {
            this.stateChangeLock.lock();

            try {
               this.readAheadBuffer.limit(var3);
               if (var2 >= 0 && !(var5 instanceof EOFException)) {
                  if (var5 != null) {
                     this.readAborted = true;
                     this.readException = var5;
                  }
               } else {
                  this.endOfStream = true;
               }

               this.readInProgress = false;
               this.signalAsyncReadComplete();
            } finally {
               this.stateChangeLock.unlock();
            }

            this.closeUnderlyingInputStreamIfNecessary();
         }

      });
   }

   private void signalAsyncReadComplete() {
      this.stateChangeLock.lock();

      try {
         this.asyncReadComplete.signalAll();
      } finally {
         this.stateChangeLock.unlock();
      }

   }

   public long skip(long var1) {
      if (var1 <= 0L) {
         return 0L;
      } else if (var1 <= (long)this.activeBuffer.remaining()) {
         this.activeBuffer.position((int)var1 + this.activeBuffer.position());
         return var1;
      } else {
         this.stateChangeLock.lock();

         long var3;
         try {
            var3 = this.skipInternal(var1);
         } finally {
            this.stateChangeLock.unlock();
         }

         return var3;
      }
   }

   private long skipInternal(long var1) {
      assert this.stateChangeLock.isLocked();

      this.waitForAsyncReadComplete();
      if (this.isEndOfStream()) {
         return 0L;
      } else {
         int var3;
         if ((long)this.available() >= var1) {
            var3 = (int)var1;
            var3 -= this.activeBuffer.remaining();

            assert var3 > 0;

            this.activeBuffer.position(0);
            this.activeBuffer.flip();
            this.readAheadBuffer.position(var3 + this.readAheadBuffer.position());
            this.swapBuffers();
            this.readAsync();
            return var1;
         } else {
            var3 = this.available();
            long var4 = var1 - (long)var3;
            this.activeBuffer.position(0);
            this.activeBuffer.flip();
            this.readAheadBuffer.position(0);
            this.readAheadBuffer.flip();
            long var6 = this.underlyingInputStream.skip(var4);
            this.readAsync();
            return (long)var3 + var6;
         }
      }
   }

   private void swapBuffers() {
      ByteBuffer var1 = this.activeBuffer;
      this.activeBuffer = this.readAheadBuffer;
      this.readAheadBuffer = var1;
   }

   private void waitForAsyncReadComplete() {
      this.stateChangeLock.lock();

      try {
         this.isWaiting.set(true);

         while(this.readInProgress) {
            this.asyncReadComplete.await();
         }
      } catch (InterruptedException var6) {
         InterruptedIOException var2 = new InterruptedIOException(var6.getMessage());
         var2.initCause(var6);
         throw var2;
      } finally {
         this.isWaiting.set(false);
         this.stateChangeLock.unlock();
      }

      this.checkReadException();
   }
}
