package advancedplugins.pm2.cv.models.api.nms.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import lombok.Generated;
import org.bukkit.entity.Player;

public class PipelineWrapper {
   private static final ScheduledExecutorService SCHEDULED_SERVICE = Executors.newScheduledThreadPool(10);
   private final Player player;
   private final Consumer<Object> flushAndWrite;
   private final ClientDesyncMonitor desyncMonitor;
   private long delay;
   private final BlockingQueue<Object> packetQueue = new LinkedBlockingQueue(1000);
   private final AtomicBoolean processing = new AtomicBoolean(false);

   public PipelineWrapper(Player var1, Consumer<Object> var2) {
      this.player = var1;
      this.flushAndWrite = var2;
      this.desyncMonitor = new ClientDesyncMonitor(this);
   }

   public void writeAndFlush(Object var1) {
      if (!this.packetQueue.offer(var1)) {
         System.err.println("Queue full, dropping packet");
      } else {
         if (this.processing.compareAndSet(false, true)) {
            this.processNextPacket();
         }

      }
   }

   private void processNextPacket() {
      Object var1 = this.packetQueue.poll();
      if (var1 == null) {
         this.processing.set(false);
      } else {
         if (this.delay <= 0L) {
            this.flushAndWrite.accept(var1);
            this.processNextPacket();
         } else {
            SCHEDULED_SERVICE.schedule(() -> {
               this.flushAndWrite.accept(var1);
               this.processNextPacket();
            }, this.delay, TimeUnit.MILLISECONDS);
         }

      }
   }

   @Generated
   public Player getPlayer() {
      return this.player;
   }

   @Generated
   public Consumer<Object> getFlushAndWrite() {
      return this.flushAndWrite;
   }

   @Generated
   public ClientDesyncMonitor getDesyncMonitor() {
      return this.desyncMonitor;
   }

   @Generated
   public long getDelay() {
      return this.delay;
   }

   @Generated
   public BlockingQueue<Object> getPacketQueue() {
      return this.packetQueue;
   }

   @Generated
   public AtomicBoolean getProcessing() {
      return this.processing;
   }

   @Generated
   public void setDelay(long var1) {
      this.delay = var1;
   }
}
