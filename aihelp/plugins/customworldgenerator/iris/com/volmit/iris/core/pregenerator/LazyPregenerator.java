package com.volmit.iris.core.pregenerator;

import com.google.gson.Gson;
import com.volmit.iris.Iris;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.RollingSequence;
import com.volmit.iris.util.math.Spiraler;
import com.volmit.iris.util.paper.PaperLib;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class LazyPregenerator extends Thread implements Listener {
   private static LazyPregenerator instance;
   private final LazyPregenerator.LazyPregenJob job;
   private final File destination;
   private final int maxPosition;
   private World world;
   private final long rate;
   private final ChronoLatch latch;
   private static AtomicInteger lazyGeneratedChunks;
   private final AtomicInteger generatedLast;
   private final AtomicInteger lazyTotalChunks;
   private final AtomicLong startTime;
   private final RollingSequence chunksPerSecond;
   private final RollingSequence chunksPerMinute;
   private static final Map<String, LazyPregenerator.LazyPregenJob> jobs = new HashMap();
   private final ExecutorService executorService;

   public LazyPregenerator(LazyPregenerator.LazyPregenJob job, File destination) {
      this.executorService = Executors.newSingleThreadExecutor();
      this.job = var1;
      this.destination = var2;
      this.maxPosition = (new Spiraler(var1.getRadiusBlocks() * 2, var1.getRadiusBlocks() * 2, (var0, var1x) -> {
      })).count();
      this.world = Bukkit.getWorld(var1.getWorld());
      this.rate = Math.round(1.0D / ((double)var1.getChunksPerMinute() / 60.0D) * 1000.0D);
      this.latch = new ChronoLatch(15000L);
      this.startTime = new AtomicLong(M.ms());
      this.chunksPerSecond = new RollingSequence(10);
      this.chunksPerMinute = new RollingSequence(10);
      lazyGeneratedChunks = new AtomicInteger(0);
      this.generatedLast = new AtomicInteger(0);
      this.lazyTotalChunks = new AtomicInteger((int)Math.ceil(Math.pow(2.0D * (double)var1.getRadiusBlocks() / 16.0D, 2.0D)));
      jobs.put(var1.getWorld(), var1);
      instance = this;
   }

   public LazyPregenerator(File file) {
      this((LazyPregenerator.LazyPregenJob)(new Gson()).fromJson(IO.readAll(var1), LazyPregenerator.LazyPregenJob.class), var1);
   }

   public static void loadLazyGenerators() {
      Iterator var0 = Bukkit.getWorlds().iterator();

      while(var0.hasNext()) {
         World var1 = (World)var0.next();
         File var2 = new File(var1.getWorldFolder(), "lazygen.json");
         if (var2.exists()) {
            try {
               LazyPregenerator var3 = new LazyPregenerator(var2);
               var3.start();
               Iris.info("Started Lazy Pregenerator: " + String.valueOf(var3.job));
            } catch (IOException var4) {
               throw new RuntimeException(var4);
            }
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
         J.sleep(this.rate);
         this.tick();
      }

      try {
         this.saveNow();
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public void tick() {
      LazyPregenerator.LazyPregenJob var1 = (LazyPregenerator.LazyPregenJob)jobs.get(this.world.getName());
      if (this.latch.flip() && !var1.paused) {
         long var2 = this.computeETA();
         this.save();
         int var4 = lazyGeneratedChunks.get() - this.generatedLast.get();
         this.generatedLast.set(lazyGeneratedChunks.get());
         var4 /= 15;
         this.chunksPerSecond.put((double)var4);
         this.chunksPerMinute.put((double)(var4 * 60));
         if (!var1.isSilent()) {
            String var10000 = String.valueOf(C.IRIS);
            Iris.info("LazyGen: " + var10000 + this.world.getName() + String.valueOf(C.RESET) + " RTT: " + Form.f(lazyGeneratedChunks.get()) + " of " + Form.f(this.lazyTotalChunks.get()) + " " + Form.f((int)this.chunksPerMinute.getAverage()) + "/m ETA: " + Form.duration((double)var2, 2));
         }
      }

      int var5;
      if (lazyGeneratedChunks.get() >= this.lazyTotalChunks.get()) {
         if (var1.isHealing()) {
            var5 = (var1.getHealingPosition() + 1) % this.maxPosition;
            var1.setHealingPosition(var5);
            this.tickRegenerate(this.getChunk(var5));
         } else {
            Iris.info("Completed Lazy Gen!");
            this.interrupt();
         }
      } else {
         var5 = var1.getPosition() + 1;
         var1.setPosition(var5);
         if (!var1.paused) {
            this.tickGenerate(this.getChunk(var5));
         }
      }

   }

   private long computeETA() {
      return (long)((double)(this.lazyTotalChunks.get() - lazyGeneratedChunks.get()) / this.chunksPerMinute.getAverage()) * 1000L;
   }

   private void tickGenerate(Position2 chunk) {
      this.executorService.submit(() -> {
         CountDownLatch var2 = new CountDownLatch(1);
         if (PaperLib.isPaper()) {
            PaperLib.getChunkAtAsync(this.world, var1.getX(), var1.getZ(), true).thenAccept((var2x) -> {
               Iris.verbose("Generated Async " + String.valueOf(var1));
               var2.countDown();
            });
         } else {
            J.s(() -> {
               this.world.getChunkAt(var1.getX(), var1.getZ());
               Iris.verbose("Generated " + String.valueOf(var1));
               var2.countDown();
            });
         }

         try {
            var2.await();
         } catch (InterruptedException var4) {
         }

         lazyGeneratedChunks.addAndGet(1);
      });
   }

   private void tickRegenerate(Position2 chunk) {
      J.s(() -> {
         this.world.regenerateChunk(var1.getX(), var1.getZ());
      });
      Iris.verbose("Regenerated " + String.valueOf(var1));
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

   public static void setPausedLazy(World world) {
      LazyPregenerator.LazyPregenJob var1 = (LazyPregenerator.LazyPregenJob)jobs.get(var0.getName());
      if (isPausedLazy(var0)) {
         var1.paused = false;
      } else {
         var1.paused = true;
      }

      String var10000;
      if (var1.paused) {
         var10000 = String.valueOf(C.BLUE);
         Iris.info(var10000 + "LazyGen: " + String.valueOf(C.IRIS) + var0.getName() + String.valueOf(C.BLUE) + " Paused");
      } else {
         var10000 = String.valueOf(C.BLUE);
         Iris.info(var10000 + "LazyGen: " + String.valueOf(C.IRIS) + var0.getName() + String.valueOf(C.BLUE) + " Resumed");
      }

   }

   public static boolean isPausedLazy(World world) {
      LazyPregenerator.LazyPregenJob var1 = (LazyPregenerator.LazyPregenJob)jobs.get(var0.getName());
      return var1 != null && var1.isPaused();
   }

   public void shutdownInstance(World world) {
      String var10000 = String.valueOf(C.IRIS);
      Iris.info("LazyGen: " + var10000 + var1.getName() + String.valueOf(C.BLUE) + " Shutting down..");
      LazyPregenerator.LazyPregenJob var2 = (LazyPregenerator.LazyPregenJob)jobs.get(var1.getName());
      File var3 = new File(Bukkit.getWorldContainer(), var1.getName());
      final File var4 = new File(var3, "lazygen.json");
      if (var2 == null) {
         Iris.error("No Lazygen job found for world: " + var1.getName());
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
                  Iris.info("LazyGen: " + var10000 + var1.getName() + String.valueOf(C.BLUE) + " File deleted and instance closed.");
               }
            }).runTaskLater(Iris.instance, 20L);
         } catch (Exception var9) {
            Iris.error("Failed to shutdown Lazygen for " + var1.getName());
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
   public static LazyPregenerator getInstance() {
      return instance;
   }

   public static class LazyPregenJob {
      private String world;
      private int healingPosition;
      private boolean healing;
      private int chunksPerMinute;
      private int radiusBlocks;
      private int position;
      boolean silent;
      boolean paused;

      @Generated
      private static int $default$healingPosition() {
         return 0;
      }

      @Generated
      private static boolean $default$healing() {
         return false;
      }

      @Generated
      private static int $default$chunksPerMinute() {
         return 32;
      }

      @Generated
      private static int $default$radiusBlocks() {
         return 5000;
      }

      @Generated
      private static int $default$position() {
         return 0;
      }

      @Generated
      private static boolean $default$silent() {
         return false;
      }

      @Generated
      private static boolean $default$paused() {
         return false;
      }

      @Generated
      LazyPregenJob(final String world, final int healingPosition, final boolean healing, final int chunksPerMinute, final int radiusBlocks, final int position, final boolean silent, final boolean paused) {
         this.world = var1;
         this.healingPosition = var2;
         this.healing = var3;
         this.chunksPerMinute = var4;
         this.radiusBlocks = var5;
         this.position = var6;
         this.silent = var7;
         this.paused = var8;
      }

      @Generated
      public static LazyPregenerator.LazyPregenJob.LazyPregenJobBuilder builder() {
         return new LazyPregenerator.LazyPregenJob.LazyPregenJobBuilder();
      }

      @Generated
      public String getWorld() {
         return this.world;
      }

      @Generated
      public int getHealingPosition() {
         return this.healingPosition;
      }

      @Generated
      public boolean isHealing() {
         return this.healing;
      }

      @Generated
      public int getChunksPerMinute() {
         return this.chunksPerMinute;
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
      public boolean isSilent() {
         return this.silent;
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
      public void setHealingPosition(final int healingPosition) {
         this.healingPosition = var1;
      }

      @Generated
      public void setHealing(final boolean healing) {
         this.healing = var1;
      }

      @Generated
      public void setChunksPerMinute(final int chunksPerMinute) {
         this.chunksPerMinute = var1;
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
      public void setSilent(final boolean silent) {
         this.silent = var1;
      }

      @Generated
      public void setPaused(final boolean paused) {
         this.paused = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof LazyPregenerator.LazyPregenJob)) {
            return false;
         } else {
            LazyPregenerator.LazyPregenJob var2 = (LazyPregenerator.LazyPregenJob)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.getHealingPosition() != var2.getHealingPosition()) {
               return false;
            } else if (this.isHealing() != var2.isHealing()) {
               return false;
            } else if (this.getChunksPerMinute() != var2.getChunksPerMinute()) {
               return false;
            } else if (this.getRadiusBlocks() != var2.getRadiusBlocks()) {
               return false;
            } else if (this.getPosition() != var2.getPosition()) {
               return false;
            } else if (this.isSilent() != var2.isSilent()) {
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
         return var1 instanceof LazyPregenerator.LazyPregenJob;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var4 = var2 * 59 + this.getHealingPosition();
         var4 = var4 * 59 + (this.isHealing() ? 79 : 97);
         var4 = var4 * 59 + this.getChunksPerMinute();
         var4 = var4 * 59 + this.getRadiusBlocks();
         var4 = var4 * 59 + this.getPosition();
         var4 = var4 * 59 + (this.isSilent() ? 79 : 97);
         var4 = var4 * 59 + (this.isPaused() ? 79 : 97);
         String var3 = this.getWorld();
         var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
         return var4;
      }

      @Generated
      public String toString() {
         String var10000 = this.getWorld();
         return "LazyPregenerator.LazyPregenJob(world=" + var10000 + ", healingPosition=" + this.getHealingPosition() + ", healing=" + this.isHealing() + ", chunksPerMinute=" + this.getChunksPerMinute() + ", radiusBlocks=" + this.getRadiusBlocks() + ", position=" + this.getPosition() + ", silent=" + this.isSilent() + ", paused=" + this.isPaused() + ")";
      }

      @Generated
      public static class LazyPregenJobBuilder {
         @Generated
         private String world;
         @Generated
         private boolean healingPosition$set;
         @Generated
         private int healingPosition$value;
         @Generated
         private boolean healing$set;
         @Generated
         private boolean healing$value;
         @Generated
         private boolean chunksPerMinute$set;
         @Generated
         private int chunksPerMinute$value;
         @Generated
         private boolean radiusBlocks$set;
         @Generated
         private int radiusBlocks$value;
         @Generated
         private boolean position$set;
         @Generated
         private int position$value;
         @Generated
         private boolean silent$set;
         @Generated
         private boolean silent$value;
         @Generated
         private boolean paused$set;
         @Generated
         private boolean paused$value;

         @Generated
         LazyPregenJobBuilder() {
         }

         @Generated
         public LazyPregenerator.LazyPregenJob.LazyPregenJobBuilder world(final String world) {
            this.world = var1;
            return this;
         }

         @Generated
         public LazyPregenerator.LazyPregenJob.LazyPregenJobBuilder healingPosition(final int healingPosition) {
            this.healingPosition$value = var1;
            this.healingPosition$set = true;
            return this;
         }

         @Generated
         public LazyPregenerator.LazyPregenJob.LazyPregenJobBuilder healing(final boolean healing) {
            this.healing$value = var1;
            this.healing$set = true;
            return this;
         }

         @Generated
         public LazyPregenerator.LazyPregenJob.LazyPregenJobBuilder chunksPerMinute(final int chunksPerMinute) {
            this.chunksPerMinute$value = var1;
            this.chunksPerMinute$set = true;
            return this;
         }

         @Generated
         public LazyPregenerator.LazyPregenJob.LazyPregenJobBuilder radiusBlocks(final int radiusBlocks) {
            this.radiusBlocks$value = var1;
            this.radiusBlocks$set = true;
            return this;
         }

         @Generated
         public LazyPregenerator.LazyPregenJob.LazyPregenJobBuilder position(final int position) {
            this.position$value = var1;
            this.position$set = true;
            return this;
         }

         @Generated
         public LazyPregenerator.LazyPregenJob.LazyPregenJobBuilder silent(final boolean silent) {
            this.silent$value = var1;
            this.silent$set = true;
            return this;
         }

         @Generated
         public LazyPregenerator.LazyPregenJob.LazyPregenJobBuilder paused(final boolean paused) {
            this.paused$value = var1;
            this.paused$set = true;
            return this;
         }

         @Generated
         public LazyPregenerator.LazyPregenJob build() {
            int var1 = this.healingPosition$value;
            if (!this.healingPosition$set) {
               var1 = LazyPregenerator.LazyPregenJob.$default$healingPosition();
            }

            boolean var2 = this.healing$value;
            if (!this.healing$set) {
               var2 = LazyPregenerator.LazyPregenJob.$default$healing();
            }

            int var3 = this.chunksPerMinute$value;
            if (!this.chunksPerMinute$set) {
               var3 = LazyPregenerator.LazyPregenJob.$default$chunksPerMinute();
            }

            int var4 = this.radiusBlocks$value;
            if (!this.radiusBlocks$set) {
               var4 = LazyPregenerator.LazyPregenJob.$default$radiusBlocks();
            }

            int var5 = this.position$value;
            if (!this.position$set) {
               var5 = LazyPregenerator.LazyPregenJob.$default$position();
            }

            boolean var6 = this.silent$value;
            if (!this.silent$set) {
               var6 = LazyPregenerator.LazyPregenJob.$default$silent();
            }

            boolean var7 = this.paused$value;
            if (!this.paused$set) {
               var7 = LazyPregenerator.LazyPregenJob.$default$paused();
            }

            return new LazyPregenerator.LazyPregenJob(this.world, var1, var2, var3, var4, var5, var6, var7);
         }

         @Generated
         public String toString() {
            return "LazyPregenerator.LazyPregenJob.LazyPregenJobBuilder(world=" + this.world + ", healingPosition$value=" + this.healingPosition$value + ", healing$value=" + this.healing$value + ", chunksPerMinute$value=" + this.chunksPerMinute$value + ", radiusBlocks$value=" + this.radiusBlocks$value + ", position$value=" + this.position$value + ", silent$value=" + this.silent$value + ", paused$value=" + this.paused$value + ")";
         }
      }
   }
}
