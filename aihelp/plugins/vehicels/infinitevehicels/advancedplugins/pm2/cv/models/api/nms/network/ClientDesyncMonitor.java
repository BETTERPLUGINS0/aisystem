package advancedplugins.pm2.cv.models.api.nms.network;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Generated;

public class ClientDesyncMonitor {
   private final PipelineWrapper pipeline;
   private final LinkedList<Long> clientSyncTime = new LinkedList();
   private final AtomicInteger pingCounter = new AtomicInteger();
   private boolean testing;
   private boolean hasPinged;
   private long lastTestTime;
   private long clientSyncOrigin;
   private long pongReceiveTime;

   public ClientDesyncMonitor(PipelineWrapper var1) {
      this.pipeline = var1;
      this.startTest();
   }

   public void startTest() {
      this.testing = true;
      this.hasPinged = false;
      ModelAPI.getAPI().getModelUpdaters().startDesyncMonitor(this.pipeline.getPlayer().getUniqueId());
   }

   public void stopTest() {
      this.testing = false;
      this.lastTestTime = System.currentTimeMillis();
      ModelAPI.getAPI().getModelUpdaters().stopDesyncMonitor(this.pipeline.getPlayer().getUniqueId());
      if (!this.clientSyncTime.isEmpty()) {
         this.clientSyncOrigin = (Long)this.clientSyncTime.getLast();
         long var1 = this.clientSyncOrigin - this.pongReceiveTime;
         long var3 = Math.abs(var1 - 25L);
         if (var3 > 15L) {
            if (var1 > 25L) {
               this.pipeline.setDelay(var1 - 25L);
            } else {
               this.pipeline.setDelay(var1 + 25L);
            }
         }
      }

   }

   public void recordClientSyncTime(long var1) {
      this.clientSyncTime.add(var1);
      if (this.clientSyncTime.size() > 10) {
         this.clientSyncTime.poll();
      }

      if (var1 > this.pongReceiveTime && this.testing && this.hasPinged) {
         this.stopTest();
      }

   }

   public void recordPongTime(long var1) {
      if (this.testing) {
         this.pongReceiveTime = var1;
         if (!this.clientSyncTime.isEmpty() && var1 > (Long)this.clientSyncTime.getLast()) {
            this.hasPinged = true;
         }
      }

   }

   public boolean clientTickShifted() {
      if (this.clientSyncTime.isEmpty()) {
         return true;
      } else {
         long var1 = (Long)this.clientSyncTime.getLast();
         return (var1 - this.clientSyncOrigin) % 50L < 40L;
      }
   }

   public boolean shouldRetest() {
      return System.currentTimeMillis() - this.lastTestTime > 60000L;
   }

   @Generated
   public PipelineWrapper getPipeline() {
      return this.pipeline;
   }

   @Generated
   public LinkedList<Long> getClientSyncTime() {
      return this.clientSyncTime;
   }

   @Generated
   public AtomicInteger getPingCounter() {
      return this.pingCounter;
   }

   @Generated
   public boolean isTesting() {
      return this.testing;
   }

   @Generated
   public boolean isHasPinged() {
      return this.hasPinged;
   }

   @Generated
   public long getLastTestTime() {
      return this.lastTestTime;
   }

   @Generated
   public long getClientSyncOrigin() {
      return this.clientSyncOrigin;
   }

   @Generated
   public long getPongReceiveTime() {
      return this.pongReceiveTime;
   }

   @Generated
   public void setTesting(boolean var1) {
      this.testing = var1;
   }

   @Generated
   public void setHasPinged(boolean var1) {
      this.hasPinged = var1;
   }

   @Generated
   public void setLastTestTime(long var1) {
      this.lastTestTime = var1;
   }

   @Generated
   public void setClientSyncOrigin(long var1) {
      this.clientSyncOrigin = var1;
   }

   @Generated
   public void setPongReceiveTime(long var1) {
      this.pongReceiveTime = var1;
   }
}
