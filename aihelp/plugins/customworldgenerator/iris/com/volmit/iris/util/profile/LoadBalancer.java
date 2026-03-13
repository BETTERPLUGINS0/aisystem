package com.volmit.iris.util.profile;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.util.math.M;
import java.util.concurrent.Semaphore;
import lombok.Generated;

public class LoadBalancer extends MsptTimings {
   private final Semaphore semaphore;
   private final int maxPermits;
   private final double range;
   private int minMspt;
   private int maxMspt;
   private int permits;
   private int lastMspt;
   private long lastTime;

   public LoadBalancer(Semaphore semaphore, int maxPermits, IrisSettings.MsRange range) {
      this(var1, var2, var3.getMin(), var3.getMax());
   }

   public LoadBalancer(Semaphore semaphore, int maxPermits, int minMspt, int maxMspt) {
      this.lastTime = M.ms();
      this.semaphore = var1;
      this.maxPermits = var2;
      this.minMspt = var3;
      this.maxMspt = var4;
      this.range = (double)(var4 - var3);
      this.setName("LoadBalancer");
      this.start();
   }

   protected void update(int raw) {
      this.lastTime = M.ms();
      int var2 = var1;
      if (var1 < this.lastMspt) {
         int var3 = (int)Math.max((double)this.lastMspt * IrisSettings.get().getUpdater().getChunkLoadSensitivity(), 1.0D);
         var2 = Math.max(var1, var3);
      }

      this.lastMspt = var2;
      var2 = Math.max(var2 - this.minMspt, 0);
      double var7 = (double)var2 / this.range;
      int var5 = (int)((double)this.maxPermits * var7);
      var5 = Math.min(var5, this.maxPermits - 20);
      int var6 = var5 - this.permits;
      this.permits = var5;
      if (var6 != 0) {
         Iris.debug("Adjusting load to %s (%s) permits (%s mspt, %.2f)".formatted(new Object[]{var5, var6, var1, var7}));
         if (var6 > 0) {
            this.semaphore.acquireUninterruptibly(var6);
         } else {
            this.semaphore.release(Math.abs(var6));
         }

      }
   }

   public void close() {
      this.interrupt();
      this.semaphore.release(this.permits);
   }

   public void setRange(IrisSettings.MsRange range) {
      this.minMspt = var1.getMin();
      this.maxMspt = var1.getMax();
   }

   @Generated
   public Semaphore getSemaphore() {
      return this.semaphore;
   }

   @Generated
   public int getMaxPermits() {
      return this.maxPermits;
   }

   @Generated
   public double getRange() {
      return this.range;
   }

   @Generated
   public int getMinMspt() {
      return this.minMspt;
   }

   @Generated
   public int getMaxMspt() {
      return this.maxMspt;
   }

   @Generated
   public int getPermits() {
      return this.permits;
   }

   @Generated
   public int getLastMspt() {
      return this.lastMspt;
   }

   @Generated
   public long getLastTime() {
      return this.lastTime;
   }

   @Generated
   public void setMinMspt(final int minMspt) {
      this.minMspt = var1;
   }

   @Generated
   public void setMaxMspt(final int maxMspt) {
      this.maxMspt = var1;
   }
}
