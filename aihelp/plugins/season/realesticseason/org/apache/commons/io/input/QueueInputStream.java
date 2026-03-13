package org.apache.commons.io.input;

import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.io.output.QueueOutputStream;

public class QueueInputStream extends InputStream {
   private final BlockingQueue<Integer> blockingQueue;

   public QueueInputStream() {
      this(new LinkedBlockingQueue());
   }

   public QueueInputStream(BlockingQueue<Integer> var1) {
      this.blockingQueue = (BlockingQueue)Objects.requireNonNull(var1, "blockingQueue");
   }

   public QueueOutputStream newQueueOutputStream() {
      return new QueueOutputStream(this.blockingQueue);
   }

   public int read() {
      Integer var1 = (Integer)this.blockingQueue.poll();
      return var1 == null ? -1 : 255 & var1;
   }
}
