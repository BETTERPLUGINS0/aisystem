package com.volmit.iris.util.mantle.io;

import com.volmit.iris.util.io.IO;
import java.nio.channels.FileChannel;
import java.util.concurrent.Semaphore;

class Holder {
   private final FileChannel channel;
   private final Semaphore semaphore = new Semaphore(1);
   private volatile boolean closed;

   Holder(FileChannel channel) {
      this.channel = var1;
      IO.lock(var1);
   }

   SynchronizedChannel acquire() {
      this.semaphore.acquireUninterruptibly();
      if (this.closed) {
         this.semaphore.release();
         return null;
      } else {
         return new SynchronizedChannel(this.channel, this.semaphore);
      }
   }

   void close() {
      this.semaphore.acquireUninterruptibly();

      try {
         if (this.closed) {
            return;
         }

         this.closed = true;
         this.channel.close();
      } finally {
         this.semaphore.release();
      }

   }
}
