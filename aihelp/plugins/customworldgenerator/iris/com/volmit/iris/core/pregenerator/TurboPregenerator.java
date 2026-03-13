package com.volmit.iris.core.pregenerator;

import com.google.gson.Gson;
import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.RollingSequence;
import com.volmit.iris.util.math.Spiraler;
import com.volmit.iris.util.paper.PaperLib;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.HyperLock;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TurboPregenerator extends Thread implements Listener {
   private static TurboPregenerator instance;
   private final TurboPregenerator.TurboPregenJob job;
   private final File destination;
   private final int maxPosition;
   private World world;
   private final ChronoLatch latch;
   private static AtomicInteger turboGeneratedChunks;
   private final AtomicInteger generatedLast;
   private final AtomicLong cachedLast;
   private final RollingSequence cachePerSecond;
   private final AtomicInteger turboTotalChunks;
   private final AtomicLong startTime;
   private final RollingSequence chunksPerSecond;
   private final RollingSequence chunksPerMinute;
   private KList<Position2> queue;
   private ConcurrentHashMap<Integer, Position2> cache;
   private AtomicInteger maxWaiting;
   private ReentrantLock cachinglock;
   private AtomicBoolean caching;
   private final HyperLock hyperLock;
   private MultiBurst burst;
   private static final Map<String, TurboPregenerator.TurboPregenJob> jobs = new HashMap();
   private final ExecutorService executorService;

   public TurboPregenerator(TurboPregenerator.TurboPregenJob job, File destination) {
      this.executorService = Executors.newFixedThreadPool(10);
      this.job = var1;
      this.queue = new KList(512);
      this.maxWaiting = new AtomicInteger(128);
      this.destination = var2;
      this.maxPosition = (new Spiraler(var1.getRadiusBlocks() * 2, var1.getRadiusBlocks() * 2, (var0, var1x) -> {
      })).count();
      this.world = Bukkit.getWorld(var1.getWorld());
      this.latch = new ChronoLatch(3000L);
      this.burst = MultiBurst.burst;
      this.hyperLock = new HyperLock();
      this.startTime = new AtomicLong(M.ms());
      this.cachePerSecond = new RollingSequence(10);
      this.chunksPerSecond = new RollingSequence(10);
      this.chunksPerMinute = new RollingSequence(10);
      turboGeneratedChunks = new AtomicInteger(0);
      this.generatedLast = new AtomicInteger(0);
      this.cachedLast = new AtomicLong(0L);
      this.caching = new AtomicBoolean(false);
      this.turboTotalChunks = new AtomicInteger((int)Math.ceil(Math.pow(2.0D * (double)var1.getRadiusBlocks() / 16.0D, 2.0D)));
      this.cache = new ConcurrentHashMap(this.turboTotalChunks.get());
      this.cachinglock = new ReentrantLock();
      jobs.put(var1.getWorld(), var1);
      instance = this;
   }

   public TurboPregenerator(File file) {
      this((TurboPregenerator.TurboPregenJob)(new Gson()).fromJson(IO.readAll(var1), TurboPregenerator.TurboPregenJob.class), var1);
   }

   public static void loadTurboGenerator(String i) {
      World var1 = Bukkit.getWorld(var0);
      File var2 = new File(var1.getWorldFolder(), "turbogen.json");
      if (var2.exists()) {
         try {
            TurboPregenerator var3 = new TurboPregenerator(var2);
            var3.start();
            Iris.info("Started Turbo Pregenerator: " + String.valueOf(var3.job));
         } catch (IOException var4) {
            throw new RuntimeException(var4);
         }
      }

   }

   @EventHandler
   public void on(WorldUnloadEvent e) {
      if (var1.getWorld().equals(this.world)) {
         this.interrupt();
      }

   }

   public void run() {
      while(!interrupted()) {
         this.tick();
      }

      try {
         this.saveNow();
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public void tick() {
      TurboPregenerator.TurboPregenJob var1 = (TurboPregenerator.TurboPregenJob)jobs.get(this.world.getName());
      if (!this.cachinglock.isLocked() && this.cache.isEmpty() && !this.caching.get()) {
         ExecutorService var2 = Executors.newFixedThreadPool(1);
         var2.submit(this::cache);
      }

      String var10000;
      long var5;
      if (this.latch.flip() && this.caching.get()) {
         var5 = this.cache.mappingCount() - this.cachedLast.get();
         this.cachedLast.set(this.cache.mappingCount());
         var5 /= 3L;
         this.cachePerSecond.put((double)var5);
         var10000 = String.valueOf(C.IRIS);
         Iris.info("TurboGen: " + var10000 + this.world.getName() + String.valueOf(C.RESET) + String.valueOf(C.BLUE) + " Caching: " + Form.f(this.cache.mappingCount()) + " of " + Form.f(this.turboTotalChunks.get()) + " " + Form.f((int)this.cachePerSecond.getAverage()) + "/s");
      }

      if (this.latch.flip() && !var1.paused && !this.cachinglock.isLocked()) {
         var5 = this.computeETA();
         this.save();
         int var4 = turboGeneratedChunks.get() - this.generatedLast.get();
         this.generatedLast.set(turboGeneratedChunks.get());
         var4 /= 3;
         this.chunksPerSecond.put((double)var4);
         this.chunksPerMinute.put((double)(var4 * 60));
         var10000 = String.valueOf(C.IRIS);
         Iris.info("TurboGen: " + var10000 + this.world.getName() + String.valueOf(C.RESET) + " RTT: " + Form.f(turboGeneratedChunks.get()) + " of " + Form.f(this.turboTotalChunks.get()) + " " + Form.f((int)this.chunksPerSecond.getAverage()) + "/s ETA: " + Form.duration((double)var5, 2));
      }

      if (turboGeneratedChunks.get() >= this.turboTotalChunks.get()) {
         Iris.info("Completed Turbo Gen!");
         this.interrupt();
      } else if (!this.cachinglock.isLocked()) {
         int var6 = var1.getPosition() + 1;
         var1.setPosition(var6);
         if (!var1.paused) {
            if (this.queue.size() < this.maxWaiting.get()) {
               Position2 var3 = (Position2)this.cache.get(var6);
               this.queue.add((Object)var3);
            }

            this.waitForChunksPartial();
         }
      }

   }

   private void cache() {
      if (!this.cachinglock.isLocked()) {
         this.cachinglock.lock();
         this.caching.set(true);
         PrecisionStopwatch var1 = PrecisionStopwatch.start();
         BurstExecutor var2 = MultiBurst.burst.burst(this.turboTotalChunks.get());
         var2.setMulticore(true);
         int[] var3 = IntStream.rangeClosed(0, this.turboTotalChunks.get()).toArray();
         AtomicInteger var4 = new AtomicInteger(this.turboTotalChunks.get());
         int var5 = Runtime.getRuntime().availableProcessors();
         if (var5 > 1) {
            --var5;
         }

         ExecutorService var6 = Executors.newFixedThreadPool(var5);
         int[] var7 = var3;
         int var8 = var3.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            int var10 = var7[var9];
            var2.queue(() -> {
               this.cache.put(var10, this.getChunk(var10));
               var4.addAndGet(-1);
            });
         }

         var2.complete();
         if (var4.get() < 0) {
            this.cachinglock.unlock();
            this.caching.set(false);
            Iris.info("Completed Caching in: " + Form.duration(var1.getMilliseconds(), 2));
         }
      } else {
         Iris.error("TurboCache is locked!");
      }

   }

   private void waitForChunksPartial() {
      while(!this.queue.isEmpty() && this.maxWaiting.get() > this.queue.size()) {
         try {
            Iterator var1 = (new KList(this.queue)).iterator();

            while(var1.hasNext()) {
               Position2 var2 = (Position2)var1.next();
               this.tickGenerate(var2);
               this.queue.remove(var2);
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

   }

   private long computeETA() {
      return (long)((double)(this.turboTotalChunks.get() - turboGeneratedChunks.get()) / this.chunksPerMinute.getAverage()) * 1000L;
   }

   private void tickGenerate(Position2 chunk) {
      this.executorService.submit(() -> {
         CountDownLatch var2 = new CountDownLatch(1);
         PaperLib.getChunkAtAsync(this.world, var1.getX(), var1.getZ(), true).thenAccept((var1x) -> {
            var2.countDown();
         });

         try {
            var2.await();
         } catch (InterruptedException var4) {
         }

         turboGeneratedChunks.addAndGet(1);
      });
   }

   public Position2 getChunk(int position) {
      int var2 = -1;
      AtomicInteger var3 = new AtomicInteger();
      AtomicInteger var4 = new AtomicInteger();
      Spiraler var5 = new Spiraler(this.job.getRadiusBlocks() * 2, this.job.getRadiusBlocks() * 2, (var2x, var3x) -> {
         var3.set(var2x);
         var4.set(var3x);
      });

      while(var5.hasNext() && var2++ < var1) {
         var5.next();
      }

      return new Position2(var3.get(), var4.get());
   }

   public void save() {
      J.a(() -> {
         try {
            this.saveNow();
         } catch (Throwable var2) {
            var2.printStackTrace();
         }

      });
   }

   public static void setPausedTurbo(World world) {
      TurboPregenerator.TurboPregenJob var1 = (TurboPregenerator.TurboPregenJob)jobs.get(var0.getName());
      if (isPausedTurbo(var0)) {
         var1.paused = false;
      } else {
         var1.paused = true;
      }

      String var10000;
      if (var1.paused) {
         var10000 = String.valueOf(C.BLUE);
         Iris.info(var10000 + "TurboGen: " + String.valueOf(C.IRIS) + var0.getName() + String.valueOf(C.BLUE) + " Paused");
      } else {
         var10000 = String.valueOf(C.BLUE);
         Iris.info(var10000 + "TurboGen: " + String.valueOf(C.IRIS) + var0.getName() + String.valueOf(C.BLUE) + " Resumed");
      }

   }

   public static boolean isPausedTurbo(World world) {
      TurboPregenerator.TurboPregenJob var1 = (TurboPregenerator.TurboPregenJob)jobs.get(var0.getName());
      return var1 != null && var1.isPaused();
   }

   public void shutdownInstance(World world) {
      String var10000 = String.valueOf(C.IRIS);
      Iris.info("turboGen: " + var10000 + var1.getName() + String.valueOf(C.BLUE) + " Shutting down..");
      TurboPregenerator.TurboPregenJob var2 = (TurboPregenerator.TurboPregenJob)jobs.get(var1.getName());
      File var3 = new File(Bukkit.getWorldContainer(), var1.getName());
      final File var4 = new File(var3, "turbogen.json");
      if (var2 == null) {
         Iris.error("No turbogen job found for world: " + var1.getName());
      } else {
         try {
            if (!var2.isPaused()) {
               var2.setPaused(true);
            }

            this.save();
            jobs.remove(var1.getName());
            (new BukkitRunnable(this) {
               public void run() {
                  while(var4.exists()) {
                     var4.delete();
                     J.sleep(1000L);
                  }

                  String var10000 = String.valueOf(C.IRIS);
                  Iris.info("turboGen: " + var10000 + var1.getName() + String.valueOf(C.BLUE) + " File deleted and instance closed.");
               }
            }).runTaskLater(Iris.instance, 20L);
         } catch (Exception var9) {
            Iris.error("Failed to shutdown turbogen for " + var1.getName());
            var9.printStackTrace();
         } finally {
            this.saveNow();
            this.interrupt();
         }

      }
   }

   public void saveNow() {
      IO.writeAll(this.destination, (Object)(new Gson()).toJson(this.job));
   }

   @Generated
   public static TurboPregenerator getInstance() {
      return instance;
   }

   public static class TurboPregenJob {
      private String world;
      private int radiusBlocks;
      private int position;
      boolean paused;

      @Generated
      private static int $default$radiusBlocks() {
         return 5000;
      }

      @Generated
      private static int $default$position() {
         return 0;
      }

      @Generated
      private static boolean $default$paused() {
         return false;
      }

      @Generated
      TurboPregenJob(final String world, final int radiusBlocks, final int position, final boolean paused) {
         this.world = var1;
         this.radiusBlocks = var2;
         this.position = var3;
         this.paused = var4;
      }

      @Generated
      public static TurboPregenerator.TurboPregenJob.TurboPregenJobBuilder builder() {
         return new TurboPregenerator.TurboPregenJob.TurboPregenJobBuilder();
      }

      @Generated
      public String getWorld() {
         return this.world;
      }

      @Generated
      public int getRadiusBlocks() {
         return this.radiusBlocks;
      }

      @Generated
      public int getPosition() {
         return this.position;
      }

      @Generated
      public boolean isPaused() {
         return this.paused;
      }

      @Generated
      public void setWorld(final String world) {
         this.world = var1;
      }

      @Generated
      public void setRadiusBlocks(final int radiusBlocks) {
         this.radiusBlocks = var1;
      }

      @Generated
      public void setPosition(final int position) {
         this.position = var1;
      }

      @Generated
      public void setPaused(final boolean paused) {
         this.paused = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof TurboPregenerator.TurboPregenJob)) {
            return false;
         } else {
            TurboPregenerator.TurboPregenJob var2 = (TurboPregenerator.TurboPregenJob)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.getRadiusBlocks() != var2.getRadiusBlocks()) {
               return false;
            } else if (this.getPosition() != var2.getPosition()) {
               return false;
            } else if (this.isPaused() != var2.isPaused()) {
               return false;
            } else {
               String var3 = this.getWorld();
               String var4 = var2.getWorld();
               if (var3 == null) {
                  if (var4 != null) {
                     return false;
                  }
               } else if (!var3.equals(var4)) {
                  return false;
               }

               return true;
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof TurboPregenerator.TurboPregenJob;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var4 = var2 * 59 + this.getRadiusBlocks();
         var4 = var4 * 59 + this.getPosition();
         var4 = var4 * 59 + (this.isPaused() ? 79 : 97);
         String var3 = this.getWorld();
         var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
         return var4;
      }

      @Generated
      public String toString() {
         String var10000 = this.getWorld();
         return "TurboPregenerator.TurboPregenJob(world=" + var10000 + ", radiusBlocks=" + this.getRadiusBlocks() + ", position=" + this.getPosition() + ", paused=" + this.isPaused() + ")";
      }

      @Generated
      public static class TurboPregenJobBuilder {
         @Generated
         private String world;
         @Generated
         private boolean radiusBlocks$set;
         @Generated
         private int radiusBlocks$value;
         @Generated
         private boolean position$set;
         @Generated
         private int position$value;
         @Generated
         private boolean paused$set;
         @Generated
         private boolean paused$value;

         @Generated
         TurboPregenJobBuilder() {
         }

         @Generated
         public TurboPregenerator.TurboPregenJob.TurboPregenJobBuilder world(final String world) {
            this.world = var1;
            return this;
         }

         @Generated
         public TurboPregenerator.TurboPregenJob.TurboPregenJobBuilder radiusBlocks(final int radiusBlocks) {
            this.radiusBlocks$value = var1;
            this.radiusBlocks$set = true;
            return this;
         }

         @Generated
         public TurboPregenerator.TurboPregenJob.TurboPregenJobBuilder position(final int position) {
            this.position$value = var1;
            this.position$set = true;
            return this;
         }

         @Generated
         public TurboPregenerator.TurboPregenJob.TurboPregenJobBuilder paused(final boolean paused) {
            this.paused$value = var1;
            this.paused$set = true;
            return this;
         }

         @Generated
         public TurboPregenerator.TurboPregenJob build() {
            int var1 = this.radiusBlocks$value;
            if (!this.radiusBlocks$set) {
               var1 = TurboPregenerator.TurboPregenJob.$default$radiusBlocks();
            }

            int var2 = this.position$value;
            if (!this.position$set) {
               var2 = TurboPregenerator.TurboPregenJob.$default$position();
            }

            boolean var3 = this.paused$value;
            if (!this.paused$set) {
               var3 = TurboPregenerator.TurboPregenJob.$default$paused();
            }

            return new TurboPregenerator.TurboPregenJob(this.world, var1, var2, var3);
         }

         @Generated
         public String toString() {
            return "TurboPregenerator.TurboPregenJob.TurboPregenJobBuilder(world=" + this.world + ", radiusBlocks$value=" + this.radiusBlocks$value + ", position$value=" + this.position$value + ", paused$value=" + this.paused$value + ")";
         }
      }
   }
}
