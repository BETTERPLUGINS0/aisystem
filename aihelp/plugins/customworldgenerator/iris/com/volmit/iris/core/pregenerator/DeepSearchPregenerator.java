package com.volmit.iris.core.pregenerator;

import com.google.gson.Gson;
import com.volmit.iris.Iris;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.RollingSequence;
import com.volmit.iris.util.math.Spiraler;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DeepSearchPregenerator extends Thread implements Listener {
   private static DeepSearchPregenerator instance;
   private final DeepSearchPregenerator.DeepSearchJob job;
   private final File destination;
   private final int maxPosition;
   private World world;
   private final ChronoLatch latch;
   private static AtomicInteger foundChunks;
   private final AtomicInteger foundLast;
   private final AtomicInteger foundTotalChunks;
   private final AtomicLong startTime;
   private final RollingSequence chunksPerSecond;
   private final RollingSequence chunksPerMinute;
   private final AtomicInteger chunkCachePos;
   private final AtomicInteger chunkCacheSize;
   private int pos;
   private final AtomicInteger foundCacheLast;
   private final AtomicInteger foundCache;
   private LinkedHashMap<Integer, Position2> chunkCache;
   private KList<Position2> chunkQueue;
   private final ReentrantLock cacheLock;
   private static final Map<String, DeepSearchPregenerator.DeepSearchJob> jobs = new HashMap();
   private final ExecutorService executorService = Executors.newSingleThreadExecutor();

   public DeepSearchPregenerator(DeepSearchPregenerator.DeepSearchJob job, File destination) {
      this.job = var1;
      this.chunkCacheSize = new AtomicInteger();
      this.chunkCachePos = new AtomicInteger(1000);
      this.foundCacheLast = new AtomicInteger();
      this.foundCache = new AtomicInteger();
      this.cacheLock = new ReentrantLock();
      this.destination = var2;
      this.chunkCache = new LinkedHashMap();
      this.maxPosition = (new Spiraler(var1.getRadiusBlocks() * 2, var1.getRadiusBlocks() * 2, (var0, var1x) -> {
      })).count();
      this.world = Bukkit.getWorld(var1.getWorld().getUID());
      this.chunkQueue = new KList();
      this.latch = new ChronoLatch(3000L);
      this.startTime = new AtomicLong(M.ms());
      this.chunksPerSecond = new RollingSequence(10);
      this.chunksPerMinute = new RollingSequence(10);
      foundChunks = new AtomicInteger(0);
      this.foundLast = new AtomicInteger(0);
      this.foundTotalChunks = new AtomicInteger((int)Math.ceil(Math.pow(2.0D * (double)var1.getRadiusBlocks() / 16.0D, 2.0D)));
      this.pos = 0;
      jobs.put(var1.getWorld().getName(), var1);
      instance = this;
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
      DeepSearchPregenerator.DeepSearchJob var1 = (DeepSearchPregenerator.DeepSearchJob)jobs.get(this.world.getName());
      if (this.latch.flip() && !var1.paused) {
         if (this.cacheLock.isLocked()) {
            Iris.info("DeepFinder: Caching: " + this.chunkCachePos.get() + " Of " + this.chunkCacheSize.get());
         } else {
            long var2 = this.computeETA();
            this.save();
            int var4 = foundChunks.get() - this.foundLast.get();
            this.foundLast.set(foundChunks.get());
            var4 /= 3;
            this.chunksPerSecond.put((double)var4);
            this.chunksPerMinute.put((double)(var4 * 60));
            String var10000 = String.valueOf(C.IRIS);
            Iris.info("DeepFinder: " + var10000 + this.world.getName() + String.valueOf(C.RESET) + " Searching: " + Form.f(foundChunks.get()) + " of " + Form.f(this.foundTotalChunks.get()) + " " + Form.f((int)this.chunksPerSecond.getAverage()) + "/s ETA: " + Form.duration((double)var2, 2));
         }
      }

      if (foundChunks.get() >= this.foundTotalChunks.get()) {
         Iris.info("Completed DeepSearch!");
         this.interrupt();
      }

   }

   private long computeETA() {
      return (long)((double)(this.foundTotalChunks.get() - foundChunks.get()) / this.chunksPerSecond.getAverage()) * 1000L;
   }

   private void queueSystem(Position2 chunk) {
      if (this.chunkQueue.isEmpty()) {
         for(int var2 = 512; var2 != 0; --var2) {
            this.pos = this.job.getPosition() + 1;
            this.chunkQueue.add((Object)this.getChunk(this.pos));
         }
      }

   }

   private void findInChunk(World world, int x, int z) {
      int var4 = var2 * 16;
      int var5 = var3 * 16;
      Engine var6 = IrisToolbelt.access(var1).getEngine();

      for(int var7 = 0; var7 < 16; ++var7) {
         for(int var8 = 0; var8 < 16; ++var8) {
            int var9 = var6.getHeight(var4 + var7, var5 + var8);
            if (var9 > 300) {
               File var10 = new File("pluginsirisfound.txt");
               FileWriter var11 = new FileWriter(var10);
               if (!var10.exists()) {
                  var10.createNewFile();
               }

               IrisBiome var12 = var6.getBiome(var4, var6.getHeight(), var5);
               Iris.info("Found at! " + var4 + ", " + var5 + "Biome ID: " + var12.getName() + ", ");
               var11.write("Biome at: X: " + var4 + " Z: " + var5 + "Biome ID: " + var12.getName() + ", ");
               return;
            }
         }
      }

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

   public static void setPausedDeep(World world) {
      DeepSearchPregenerator.DeepSearchJob var1 = (DeepSearchPregenerator.DeepSearchJob)jobs.get(var0.getName());
      if (isPausedDeep(var0)) {
         var1.paused = false;
      } else {
         var1.paused = true;
      }

      String var10000;
      if (var1.paused) {
         var10000 = String.valueOf(C.BLUE);
         Iris.info(var10000 + "DeepSearch: " + String.valueOf(C.IRIS) + var0.getName() + String.valueOf(C.BLUE) + " Paused");
      } else {
         var10000 = String.valueOf(C.BLUE);
         Iris.info(var10000 + "DeepSearch: " + String.valueOf(C.IRIS) + var0.getName() + String.valueOf(C.BLUE) + " Resumed");
      }

   }

   public static boolean isPausedDeep(World world) {
      DeepSearchPregenerator.DeepSearchJob var1 = (DeepSearchPregenerator.DeepSearchJob)jobs.get(var0.getName());
      return var1 != null && var1.isPaused();
   }

   public void shutdownInstance(World world) {
      String var10000 = String.valueOf(C.IRIS);
      Iris.info("DeepSearch: " + var10000 + var1.getName() + String.valueOf(C.BLUE) + " Shutting down..");
      DeepSearchPregenerator.DeepSearchJob var2 = (DeepSearchPregenerator.DeepSearchJob)jobs.get(var1.getName());
      File var3 = new File(Bukkit.getWorldContainer(), var1.getName());
      final File var4 = new File(var3, "DeepSearch.json");
      if (var2 == null) {
         Iris.error("No DeepSearch job found for world: " + var1.getName());
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
                  Iris.info("DeepSearch: " + var10000 + var1.getName() + String.valueOf(C.BLUE) + " File deleted and instance closed.");
               }
            }).runTaskLater(Iris.instance, 20L);
         } catch (Exception var9) {
            Iris.error("Failed to shutdown DeepSearch for " + var1.getName());
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
   public static DeepSearchPregenerator getInstance() {
      return instance;
   }

   public static class DeepSearchJob {
      private World world;
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
      DeepSearchJob(final World world, final int radiusBlocks, final int position, final boolean paused) {
         this.world = var1;
         this.radiusBlocks = var2;
         this.position = var3;
         this.paused = var4;
      }

      @Generated
      public static DeepSearchPregenerator.DeepSearchJob.DeepSearchJobBuilder builder() {
         return new DeepSearchPregenerator.DeepSearchJob.DeepSearchJobBuilder();
      }

      @Generated
      public World getWorld() {
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
      public void setWorld(final World world) {
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
         } else if (!(var1 instanceof DeepSearchPregenerator.DeepSearchJob)) {
            return false;
         } else {
            DeepSearchPregenerator.DeepSearchJob var2 = (DeepSearchPregenerator.DeepSearchJob)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.getRadiusBlocks() != var2.getRadiusBlocks()) {
               return false;
            } else if (this.getPosition() != var2.getPosition()) {
               return false;
            } else if (this.isPaused() != var2.isPaused()) {
               return false;
            } else {
               World var3 = this.getWorld();
               World var4 = var2.getWorld();
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
         return var1 instanceof DeepSearchPregenerator.DeepSearchJob;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var4 = var2 * 59 + this.getRadiusBlocks();
         var4 = var4 * 59 + this.getPosition();
         var4 = var4 * 59 + (this.isPaused() ? 79 : 97);
         World var3 = this.getWorld();
         var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
         return var4;
      }

      @Generated
      public String toString() {
         String var10000 = String.valueOf(this.getWorld());
         return "DeepSearchPregenerator.DeepSearchJob(world=" + var10000 + ", radiusBlocks=" + this.getRadiusBlocks() + ", position=" + this.getPosition() + ", paused=" + this.isPaused() + ")";
      }

      @Generated
      public static class DeepSearchJobBuilder {
         @Generated
         private World world;
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
         DeepSearchJobBuilder() {
         }

         @Generated
         public DeepSearchPregenerator.DeepSearchJob.DeepSearchJobBuilder world(final World world) {
            this.world = var1;
            return this;
         }

         @Generated
         public DeepSearchPregenerator.DeepSearchJob.DeepSearchJobBuilder radiusBlocks(final int radiusBlocks) {
            this.radiusBlocks$value = var1;
            this.radiusBlocks$set = true;
            return this;
         }

         @Generated
         public DeepSearchPregenerator.DeepSearchJob.DeepSearchJobBuilder position(final int position) {
            this.position$value = var1;
            this.position$set = true;
            return this;
         }

         @Generated
         public DeepSearchPregenerator.DeepSearchJob.DeepSearchJobBuilder paused(final boolean paused) {
            this.paused$value = var1;
            this.paused$set = true;
            return this;
         }

         @Generated
         public DeepSearchPregenerator.DeepSearchJob build() {
            int var1 = this.radiusBlocks$value;
            if (!this.radiusBlocks$set) {
               var1 = DeepSearchPregenerator.DeepSearchJob.$default$radiusBlocks();
            }

            int var2 = this.position$value;
            if (!this.position$set) {
               var2 = DeepSearchPregenerator.DeepSearchJob.$default$position();
            }

            boolean var3 = this.paused$value;
            if (!this.paused$set) {
               var3 = DeepSearchPregenerator.DeepSearchJob.$default$paused();
            }

            return new DeepSearchPregenerator.DeepSearchJob(this.world, var1, var2, var3);
         }

         @Generated
         public String toString() {
            String var10000 = String.valueOf(this.world);
            return "DeepSearchPregenerator.DeepSearchJob.DeepSearchJobBuilder(world=" + var10000 + ", radiusBlocks$value=" + this.radiusBlocks$value + ", position$value=" + this.position$value + ", paused$value=" + this.paused$value + ")";
         }
      }
   }
}
