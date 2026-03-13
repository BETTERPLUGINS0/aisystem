package com.volmit.iris.util.mantle.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.concurrent.Semaphore;

public class SynchronizedChannel implements Closeable {
   private final FileChannel channel;
   private final Semaphore lock;
   private transient boolean closed;

   SynchronizedChannel(FileChannel channel, Semaphore lock) {
      this.channel = var1;
      this.lock = var2;
   }

   public InputStream read() {
      if (this.closed) {
         throw new IOException("Channel is closed!");
      } else {
         return DelegateStream.read(this.channel);
      }
   }

   public OutputStream write() {
      if (this.closed) {
         throw new IOException("Channel is closed!");
      } else {
         return DelegateStream.write(this.channel);
      }
   }

   public void close() {
      if (!this.closed) {
         this.closed = true;
         this.lock.release();
      }
   }
}
