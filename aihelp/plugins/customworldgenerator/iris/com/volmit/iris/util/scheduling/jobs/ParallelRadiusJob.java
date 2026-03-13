package com.volmit.iris.util.scheduling.jobs;

import com.volmit.iris.util.math.Spiraler;
import com.volmit.iris.util.parallel.MultiBurst;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Generated;

public abstract class ParallelRadiusJob implements Job {
   @Generated
   private final Object $lock;
   private final ExecutorService service;
   private final AtomicInteger completed;
   private volatile int radiusX;
   private volatile int radiusZ;
   private volatile int offsetX;
   private volatile int offsetZ;
   private volatile int total;
   private final Semaphore lock;
   private final int lockSize;

   public ParallelRadiusJob(int concurrent) {
      this(var1, MultiBurst.burst);
   }

   public ParallelRadiusJob(int concurrent, ExecutorService service) {
      this.$lock = new Object[0];
      this.service = var2;
      this.completed = new AtomicInteger(0);
      this.lock = new Semaphore(var1);
      this.lockSize = var1;
   }

   public ParallelRadiusJob retarget(int radius, int offsetX, int offsetZ) {
      return this.retarget(var1, var1, var2, var3);
   }

   public ParallelRadiusJob retarget(int radiusX, int radiusZ, int offsetX, int offsetZ) {
      synchronized(this.$lock) {
         this.completed.set(0);
         this.radiusX = var1;
         this.radiusZ = var2;
         this.offsetX = var3;
         this.offsetZ = var4;
         this.total = (var1 * 2 + 1) * (var2 * 2 + 1);
         return this;
      }
   }

   public void execute() {
      synchronized(this.$lock) {
         try {
            (new Spiraler(this.radiusX * 2 + 3, this.radiusZ * 2 + 3, this::submit)).drain();
            this.lock.acquire(this.lockSize);
            this.lock.release(this.lockSize);
         } catch (Throwable var4) {
            throw var4;
         }

      }
   }

   private void submit(int x, int z) {
      try {
         if (Math.abs(var1) <= this.radiusX && Math.abs(var2) <= this.radiusZ) {
            this.lock.acquire();
            this.service.submit(() -> {
               try {
                  this.execute(var1 + this.offsetX, var2 + this.offsetZ);
               } finally {
                  this.completeWork();
               }

            });
         }
      } catch (Throwable var4) {
         throw var4;
      }
   }

   protected abstract void execute(int x, int z);

   public void completeWork() {
      this.completed.incrementAndGet();
      this.lock.release();
   }

   public int getTotalWork() {
      return this.total;
   }

   public int getWorkCompleted() {
      return this.completed.get();
   }
}
