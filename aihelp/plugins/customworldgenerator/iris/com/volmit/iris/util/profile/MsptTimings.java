package com.volmit.iris.util.profile;

import com.volmit.iris.util.math.M;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.Looper;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.bukkit.Bukkit;

public abstract class MsptTimings extends Looper {
   private final AtomicInteger currentTick = new AtomicInteger(0);
   private int lastTick;
   private int lastMspt;
   private long lastTime;
   private int taskId = -1;

   public MsptTimings() {
      this.setName("MsptTimings");
      this.setPriority(9);
      this.setDaemon(true);
   }

   public static MsptTimings of(Consumer<Integer> update) {
      return new MsptTimings.Simple(var0);
   }

   protected final long loop() {
      if (this.startTickTask()) {
         return 200L;
      } else {
         long var1 = M.ms();
         int var3 = this.currentTick.get();
         int var4 = var3 - this.lastTick;
         if (var4 == 0) {
            return 200L;
         } else {
            this.lastTick = var3;
            int var5 = (int)(var1 - this.lastTime);
            this.lastTime = var1;
            int var6 = var5 / var4;
            var6 -= 50;
            var6 = Math.max(var6, 0);
            this.lastMspt = var6;
            this.update(var6);
            return 200L;
         }
      }
   }

   public final int getMspt() {
      return this.lastMspt;
   }

   protected abstract void update(int mspt);

   private boolean startTickTask() {
      if (this.taskId == -1 || !Bukkit.getScheduler().isQueued(this.taskId) && !Bukkit.getScheduler().isCurrentlyRunning(this.taskId)) {
         this.taskId = J.sr(() -> {
            if (this.isInterrupted()) {
               J.csr(this.taskId);
            } else {
               this.currentTick.incrementAndGet();
            }
         }, 1);
         return this.taskId != -1;
      } else {
         return false;
      }
   }

   private static class Simple extends MsptTimings {
      private final Consumer<Integer> update;

      private Simple(Consumer<Integer> update) {
         this.update = var1;
         this.start();
      }

      protected void update(int mspt) {
         if (this.update != null) {
            this.update.accept(var1);
         }
      }
   }
}
