package org.apache.commons.io.output;

import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.io.input.QueueInputStream;

public class QueueOutputStream extends OutputStream {
   private final BlockingQueue<Integer> blockingQueue;

   public QueueOutputStream() {
      this(new LinkedBlockingQueue());
   }

   public QueueOutputStream(BlockingQueue<Integer> var1) {
      this.blockingQueue = (BlockingQueue)Objects.requireNonNull(var1, "blockingQueue");
   }

   public QueueInputStream newQueueInputStream() {
      return new QueueInputStream(this.blockingQueue);
   }

   public void write(int var1) {
      try {
         this.blockingQueue.put(255 & var1);
      } catch (InterruptedException var4) {
         Thread.currentThread().interrupt();
         InterruptedIOException var3 = new InterruptedIOException();
         var3.initCause(var4);
         throw var3;
      }
   }
}
