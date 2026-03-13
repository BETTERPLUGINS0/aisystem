package com.volmit.iris.core.pregenerator;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.service.PreservationSVC;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.mantle.MantleChunk;
import com.volmit.iris.util.mantle.flag.MantleFlag;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.RollingSequence;
import com.volmit.iris.util.paper.PaperLib;
import com.volmit.iris.util.plugin.chunk.TicketHolder;
import com.volmit.iris.util.profile.LoadBalancer;
import com.volmit.iris.util.scheduling.J;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

public class ChunkUpdater {
   private static final String REGION_PATH;
   private final AtomicBoolean paused = new AtomicBoolean();
   private final AtomicBoolean cancelled = new AtomicBoolean();
   private final TicketHolder holder;
   private final RollingSequence chunksPerSecond = new RollingSequence(5);
   private final AtomicInteger totalMaxChunks = new AtomicInteger();
   private final AtomicInteger chunksProcessed = new AtomicInteger();
   private final AtomicInteger chunksProcessedLast = new AtomicInteger();
   private final AtomicInteger chunksUpdated = new AtomicInteger();
   private final AtomicBoolean serverEmpty = new AtomicBoolean(true);
   private final AtomicLong lastCpsTime = new AtomicLong(M.ms());
   private final int maxConcurrency = IrisSettings.get().getUpdater().getMaxConcurrency();
   private final int coreLimit = (int)Math.max((double)Runtime.getRuntime().availableProcessors() * IrisSettings.get().getUpdater().getThreadMultiplier(), 1.0D);
   private final Semaphore semaphore;
   private final LoadBalancer loadBalancer;
   private final AtomicLong startTime;
   private final ChunkUpdater.Dimensions dimensions;
   private final PregenTask task;
   private final ExecutorService chunkExecutor;
   private final ScheduledExecutorService scheduler;
   private final CountDownLatch latch;
   private final Engine engine;
   private final World world;

   public ChunkUpdater(World world) {
      this.semaphore = new Semaphore(this.maxConcurrency);
      this.loadBalancer = new LoadBalancer(this.semaphore, this.maxConcurrency, IrisSettings.get().getUpdater().emptyMsRange);
      this.startTime = new AtomicLong();
      this.chunkExecutor = IrisSettings.get().getUpdater().isNativeThreads() ? Executors.newFixedThreadPool(this.coreLimit) : Executors.newVirtualThreadPerTaskExecutor();
      this.scheduler = Executors.newScheduledThreadPool(1);
      this.engine = IrisToolbelt.access(var1).getEngine();
      this.world = var1;
      this.holder = Iris.tickets.getHolder(var1);
      this.dimensions = this.calculateWorldDimensions(new File(var1.getWorldFolder(), "region"));
      this.task = this.dimensions.task();
      this.totalMaxChunks.set(this.dimensions.count * 1024);
      this.latch = new CountDownLatch(this.totalMaxChunks.get());
   }

   public String getName() {
      return this.world.getName();
   }

   public int getChunks() {
      return this.totalMaxChunks.get();
   }

   public void start() {
      this.unloadAndSaveAllChunks();
      this.update();
   }

   public boolean pause() {
      this.unloadAndSaveAllChunks();
      if (this.paused.get()) {
         this.paused.set(false);
         return false;
      } else {
         this.paused.set(true);
         return true;
      }
   }

   public void stop() {
      this.unloadAndSaveAllChunks();
      this.cancelled.set(true);
   }

   private void update() {
      Iris.info("Updating..");

      try {
         this.startTime.set(System.currentTimeMillis());
         this.scheduler.scheduleAtFixedRate(() -> {
            try {
               if (!this.paused.get()) {
                  long var1 = this.computeETA();
                  int var3 = this.chunksProcessed.get();
                  double var4 = (double)(var3 - this.chunksProcessedLast.getAndSet(var3));
                  double var6 = var4 / ((double)(M.ms() - this.lastCpsTime.getAndSet(M.ms())) / 1000.0D);
                  this.chunksPerSecond.put(var6);
                  double var8 = (double)var3 / (double)this.totalMaxChunks.get() * 100.0D;
                  if (!this.cancelled.get()) {
                     String var10000 = Form.f(var3);
                     Iris.info("Updated: " + var10000 + " of " + Form.f(this.totalMaxChunks.get()) + " (%.0f%%) " + Form.f(this.chunksPerSecond.getAverage()) + "/s, ETA: " + Form.duration(var1, 2), var8);
                  }
               }
            } catch (Exception var10) {
               Iris.reportError(var10);
               var10.printStackTrace();
            }

         }, 0L, 3L, TimeUnit.SECONDS);
         this.scheduler.scheduleAtFixedRate(() -> {
            boolean var1 = Bukkit.getOnlinePlayers().isEmpty();
            if (this.serverEmpty.getAndSet(var1) != var1) {
               this.loadBalancer.setRange(var1 ? IrisSettings.get().getUpdater().emptyMsRange : IrisSettings.get().getUpdater().defaultMsRange);
            }
         }, 0L, 10L, TimeUnit.SECONDS);
         Thread var1 = new Thread(() -> {
            this.run();
            this.close();
         }, "Iris Chunk Updater - " + this.world.getName());
         var1.setPriority(10);
         var1.start();
         ((PreservationSVC)Iris.service(PreservationSVC.class)).register(var1);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void close() {
      try {
         this.loadBalancer.close();
         this.semaphore.acquire(256);
         this.chunkExecutor.shutdown();
         this.chunkExecutor.awaitTermination(5L, TimeUnit.SECONDS);
         this.scheduler.shutdownNow();
         this.unloadAndSaveAllChunks();
      } catch (Exception var2) {
      }

      if (this.cancelled.get()) {
         Iris.info("Updated: " + Form.f(this.chunksUpdated.get()) + " Chunks");
         Iris.info("Irritated: " + Form.f(this.chunksProcessed.get()) + " of " + Form.f(this.totalMaxChunks.get()));
         Iris.info("Stopped updater.");
      } else {
         Iris.info("Processed: " + Form.f(this.chunksProcessed.get()) + " Chunks");
         Iris.info("Finished Updating: " + Form.f(this.chunksUpdated.get()) + " Chunks");
      }

   }

   private void run() {
      this.task.iterateRegions((var1, var2) -> {
         if (!this.cancelled.get()) {
            while(this.paused.get()) {
               J.sleep(50L);
            }

            if (var1 >= this.dimensions.min.getX() && var1 <= this.dimensions.max.getX() && var2 >= this.dimensions.min.getZ() && var2 <= this.dimensions.max.getZ() && (new File(this.world.getWorldFolder(), REGION_PATH + var1 + "." + var2 + ".mca")).exists()) {
               this.task.iterateChunks(var1, var2, (var1x, var2x) -> {
                  while(this.paused.get() && !this.cancelled.get()) {
                     J.sleep(50L);
                  }

                  try {
                     this.semaphore.acquire();
                  } catch (InterruptedException var4) {
                     return;
                  }

                  this.chunkExecutor.submit(() -> {
                     try {
                        if (!this.cancelled.get()) {
                           this.processChunk(var1x, var2x);
                        }
                     } finally {
                        this.latch.countDown();
                        this.semaphore.release();
                     }

                  });
               });
            }
         }
      });
   }

   private void processChunk(int x, int z) {
      if (!this.loadChunksIfGenerated(var1, var2)) {
         this.chunksProcessed.getAndIncrement();
      } else {
         MantleChunk var3 = this.engine.getMantle().getMantle().getChunk(var1, var2).use();

         try {
            Chunk var4 = this.world.getChunkAt(var1, var2);
            this.engine.updateChunk(var4);
            this.removeTickets(var1, var2);
         } finally {
            this.chunksUpdated.incrementAndGet();
            this.chunksProcessed.getAndIncrement();
            var3.release();
         }

      }
   }

   private boolean loadChunksIfGenerated(int x, int z) {
      if (this.engine.getMantle().getMantle().hasFlag(var1, var2, MantleFlag.ETCHED)) {
         return false;
      } else {
         for(int var3 = -1; var3 <= 1; ++var3) {
            for(int var4 = -1; var4 <= 1; ++var4) {
               if (!PaperLib.isChunkGenerated(this.world, var1 + var3, var2 + var4)) {
                  return false;
               }
            }
         }

         AtomicBoolean var10 = new AtomicBoolean(true);
         CountDownLatch var11 = new CountDownLatch(9);

         for(int var5 = -1; var5 <= 1; ++var5) {
            for(int var6 = -1; var6 <= 1; ++var6) {
               int var7 = var1 + var5;
               int var8 = var2 + var6;
               PaperLib.getChunkAtAsync(this.world, var7, var8, false, true).thenAccept((var3x) -> {
                  if (var3x != null && var3x.isGenerated()) {
                     this.holder.addTicket(var3x);
                     var11.countDown();
                  } else {
                     var11.countDown();
                     var10.set(false);
                  }
               });
            }
         }

         try {
            var11.await();
         } catch (InterruptedException var9) {
            Iris.info("Interrupted while waiting for chunks to load");
         }

         if (var10.get()) {
            return true;
         } else {
            this.removeTickets(var1, var2);
            return false;
         }
      }
   }

   private void removeTickets(int x, int z) {
      for(int var3 = -1; var3 <= 1; ++var3) {
         for(int var4 = -1; var4 <= 1; ++var4) {
            this.holder.removeTicket(var1 + var3, var2 + var4);
         }
      }

   }

   private void unloadAndSaveAllChunks() {
      try {
         J.sfut(() -> {
            if (this.world == null) {
               Iris.warn("World was null somehow...");
            } else {
               this.world.save();
            }
         }).get();
      } catch (Throwable var2) {
         Iris.reportError(var2);
         var2.printStackTrace();
      }

   }

   private long computeETA() {
      return (long)(this.totalMaxChunks.get() > 1024 ? (double)(this.totalMaxChunks.get() - this.chunksProcessed.get()) * ((double)(M.ms() - this.startTime.get()) / (double)this.chunksProcessed.get()) : (double)(this.totalMaxChunks.get() - this.chunksProcessed.get()) / this.chunksPerSecond.getAverage() * 1000.0D);
   }

   private ChunkUpdater.Dimensions calculateWorldDimensions(File regionDir) {
      File[] var2 = var1.listFiles((var0, var1x) -> {
         return var1x.endsWith(".mca");
      });
      int var3 = Integer.MAX_VALUE;
      int var4 = Integer.MIN_VALUE;
      int var5 = Integer.MAX_VALUE;
      int var6 = Integer.MIN_VALUE;
      File[] var7 = var2;
      int var8 = var2.length;

      int var9;
      for(var9 = 0; var9 < var8; ++var9) {
         File var10 = var7[var9];
         String[] var11 = var10.getName().split("\\.");
         int var12 = Integer.parseInt(var11[1]);
         int var13 = Integer.parseInt(var11[2]);
         var3 = Math.min(var3, var12);
         var4 = Math.max(var4, var12);
         var5 = Math.min(var5, var13);
         var6 = Math.max(var6, var13);
      }

      int var14 = var3 + (var4 - var3) / 2;
      var8 = var5 + (var6 - var5) / 2;
      var9 = var4 - var3 + 1;
      int var15 = var6 - var5 + 1;
      return new ChunkUpdater.Dimensions(new Position2(var3, var5), new Position2(var4, var6), var9 * var15, PregenTask.builder().radiusZ((int)Math.ceil((double)var15 / 2.0D * 512.0D)).radiusX((int)Math.ceil((double)var9 / 2.0D * 512.0D)).center(new Position2(var14, var8)).build());
   }

   static {
      REGION_PATH = "region" + File.separator + "r.";
   }

   private static record Dimensions(Position2 min, Position2 max, int count, PregenTask task) {
      private Dimensions(Position2 min, Position2 max, int count, PregenTask task) {
         this.min = var1;
         this.max = var2;
         this.count = var3;
         this.task = var4;
      }

      public Position2 min() {
         return this.min;
      }

      public Position2 max() {
         return this.max;
      }

      public int count() {
         return this.count;
      }

      public PregenTask task() {
         return this.task;
      }
   }
}
