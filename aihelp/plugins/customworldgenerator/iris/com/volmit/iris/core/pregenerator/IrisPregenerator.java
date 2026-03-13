package com.volmit.iris.core.pregenerator;

import com.volmit.iris.Iris;
import com.volmit.iris.core.tools.IrisPackBenchmarking;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.RollingSequence;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.Looper;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class IrisPregenerator {
   private static final double INVALID = 9.223372036854776E18D;
   private final PregenTask task;
   private final PregeneratorMethod generator;
   private final PregenListener listener;
   private final Looper ticker;
   private final AtomicBoolean paused;
   private final AtomicBoolean shutdown;
   private final RollingSequence cachedPerSecond;
   private final RollingSequence chunksPerSecond;
   private final RollingSequence chunksPerMinute;
   private final RollingSequence regionsPerMinute;
   private final KList<Integer> chunksPerSecondHistory;
   private final AtomicLong generated;
   private final AtomicLong generatedLast;
   private final AtomicLong generatedLastMinute;
   private final AtomicLong cached;
   private final AtomicLong cachedLast;
   private final AtomicLong cachedLastMinute;
   private final AtomicLong totalChunks;
   private final AtomicLong startTime;
   private final ChronoLatch minuteLatch;
   private final AtomicReference<String> currentGeneratorMethod;
   private final KSet<Position2> generatedRegions;
   private final KSet<Position2> retry;
   private final KSet<Position2> net;
   private final ChronoLatch cl;
   private final ChronoLatch saveLatch = new ChronoLatch(30000L);
   private final IrisPackBenchmarking benchmarking = IrisPackBenchmarking.getInstance();

   public IrisPregenerator(PregenTask task, PregeneratorMethod generator, PregenListener listener) {
      this.listener = this.listenify(var3);
      this.cl = new ChronoLatch(5000L);
      this.generatedRegions = new KSet(new Position2[0]);
      this.shutdown = new AtomicBoolean(false);
      this.paused = new AtomicBoolean(false);
      this.task = var1;
      this.generator = var2;
      this.retry = new KSet(new Position2[0]);
      this.net = new KSet(new Position2[0]);
      this.currentGeneratorMethod = new AtomicReference("Void");
      this.minuteLatch = new ChronoLatch(60000L, false);
      this.cachedPerSecond = new RollingSequence(5);
      this.chunksPerSecond = new RollingSequence(10);
      this.chunksPerMinute = new RollingSequence(10);
      this.regionsPerMinute = new RollingSequence(10);
      this.chunksPerSecondHistory = new KList();
      this.generated = new AtomicLong(0L);
      this.generatedLast = new AtomicLong(0L);
      this.generatedLastMinute = new AtomicLong(0L);
      this.cached = new AtomicLong();
      this.cachedLast = new AtomicLong(0L);
      this.cachedLastMinute = new AtomicLong(0L);
      this.totalChunks = new AtomicLong(0L);
      var1.iterateAllChunks((var1x, var2x) -> {
         this.totalChunks.incrementAndGet();
      });
      this.startTime = new AtomicLong(M.ms());
      this.ticker = new Looper() {
         protected long loop() {
            long var1 = IrisPregenerator.this.computeETA();
            long var3x = IrisPregenerator.this.cached.get() - IrisPregenerator.this.cachedLast.get();
            IrisPregenerator.this.cachedLast.set(IrisPregenerator.this.cached.get());
            IrisPregenerator.this.cachedPerSecond.put((double)var3x);
            long var5 = IrisPregenerator.this.generated.get() - IrisPregenerator.this.generatedLast.get() - var3x;
            IrisPregenerator.this.generatedLast.set(IrisPregenerator.this.generated.get());
            if (var3x == 0L || var5 != 0L) {
               IrisPregenerator.this.chunksPerSecond.put((double)var5);
               IrisPregenerator.this.chunksPerSecondHistory.add((Object)((int)var5));
            }

            if (IrisPregenerator.this.minuteLatch.flip()) {
               long var7 = IrisPregenerator.this.cached.get() - IrisPregenerator.this.cachedLastMinute.get();
               IrisPregenerator.this.cachedLastMinute.set(IrisPregenerator.this.cached.get());
               long var9 = IrisPregenerator.this.generated.get() - IrisPregenerator.this.generatedLastMinute.get() - var7;
               IrisPregenerator.this.generatedLastMinute.set(IrisPregenerator.this.generated.get());
               if (var7 == 0L || var9 != 0L) {
                  IrisPregenerator.this.chunksPerMinute.put((double)var9);
                  IrisPregenerator.this.regionsPerMinute.put((double)var9 / 1024.0D);
               }
            }

            boolean var11 = IrisPregenerator.this.cachedPerSecond.getAverage() != 0.0D;
            var3.onTick(var11 ? IrisPregenerator.this.cachedPerSecond.getAverage() : IrisPregenerator.this.chunksPerSecond.getAverage(), IrisPregenerator.this.chunksPerMinute.getAverage(), IrisPregenerator.this.regionsPerMinute.getAverage(), (double)IrisPregenerator.this.generated.get() / (double)IrisPregenerator.this.totalChunks.get(), IrisPregenerator.this.generated.get(), IrisPregenerator.this.totalChunks.get(), IrisPregenerator.this.totalChunks.get() - IrisPregenerator.this.generated.get(), var1, M.ms() - IrisPregenerator.this.startTime.get(), (String)IrisPregenerator.this.currentGeneratorMethod.get(), var11);
            if (IrisPregenerator.this.cl.flip()) {
               double var8 = (double)IrisPregenerator.this.generated.get() / (double)IrisPregenerator.this.totalChunks.get() * 100.0D;
               Iris.info("%s: %s of %s (%.0f%%), %s/s ETA: %s", IrisPregenerator.this.benchmarking != null ? "Benchmarking" : "Pregen", Form.f(IrisPregenerator.this.generated.get()), Form.f(IrisPregenerator.this.totalChunks.get()), var8, var11 ? "Cached " + Form.f((int)IrisPregenerator.this.cachedPerSecond.getAverage()) : Form.f((int)IrisPregenerator.this.chunksPerSecond.getAverage()), Form.duration(var1, 2));
            }

            return 1000L;
         }
      };
   }

   private long computeETA() {
      double var1 = (double)((long)(this.generated.get() > 1024L ? (double)(this.totalChunks.get() - this.generated.get()) * ((double)(M.ms() - this.startTime.get()) / (double)this.generated.get()) : (double)(this.totalChunks.get() - this.generated.get()) / this.chunksPerSecond.getAverage() * 1000.0D));
      return Double.isFinite(var1) && var1 != 9.223372036854776E18D ? (long)var1 : 0L;
   }

   public void close() {
      this.shutdown.set(true);
   }

   public void start() {
      this.init();
      this.ticker.start();
      this.checkRegions();
      PrecisionStopwatch var1 = PrecisionStopwatch.start();
      this.task.iterateRegions((var1x, var2) -> {
         this.visitRegion(var1x, var2, true);
      });
      this.task.iterateRegions((var1x, var2) -> {
         this.visitRegion(var1x, var2, false);
      });
      Iris.info("Pregen took " + Form.duration((long)var1.getMilliseconds()));
      this.shutdown();
      if (this.benchmarking == null) {
         Iris.info(String.valueOf(C.IRIS) + "Pregen stopped.");
      } else {
         this.benchmarking.finishedBenchmark(this.chunksPerSecondHistory);
      }

   }

   private void checkRegions() {
      this.task.iterateRegions(this::checkRegion);
   }

   private void init() {
      this.generator.init();
      this.generator.save();
   }

   private void shutdown() {
      this.listener.onSaving();
      this.generator.close();
      this.ticker.interrupt();
      this.listener.onClose();
      Mantle var1 = this.getMantle();
      if (var1 != null) {
         var1.trim(0L, 0);
      }

   }

   private void visitRegion(int x, int z, boolean regions) {
      while(this.paused.get() && !this.shutdown.get()) {
         J.sleep(50L);
      }

      if (this.shutdown.get()) {
         this.listener.onRegionSkipped(var1, var2);
      } else {
         Position2 var4 = new Position2(var1, var2);
         if (!this.generatedRegions.contains(var4)) {
            this.currentGeneratorMethod.set(this.generator.getMethod(var1, var2));
            boolean var5 = false;
            if (this.generator.supportsRegions(var1, var2, this.listener) && var3) {
               var5 = true;
               this.listener.onRegionGenerating(var1, var2);
               this.generator.generateRegion(var1, var2, this.listener);
            } else if (!var3) {
               var5 = true;
               this.listener.onRegionGenerating(var1, var2);
               this.task.iterateChunks(var1, var2, (var1x, var2x) -> {
                  while(this.paused.get() && !this.shutdown.get()) {
                     J.sleep(50L);
                  }

                  this.generator.generateChunk(var1x, var2x, this.listener);
               });
            }

            if (var5) {
               this.listener.onRegionGenerated(var1, var2);
               if (this.saveLatch.flip()) {
                  this.listener.onSaving();
                  this.generator.save();
               }

               this.generatedRegions.add(var4);
               this.checkRegions();
            }

         }
      }
   }

   private void checkRegion(int x, int z) {
      if (!this.generatedRegions.contains(new Position2(var1, var2))) {
         this.generator.supportsRegions(var1, var2, this.listener);
      }
   }

   public void pause() {
      this.paused.set(true);
   }

   public void resume() {
      this.paused.set(false);
   }

   private PregenListener listenify(PregenListener listener) {
      return new PregenListener() {
         public void onTick(double chunksPerSecond, double chunksPerMinute, double regionsPerMinute, double percent, long generated, long totalChunks, long chunksRemaining, long eta, long elapsed, String method, boolean cached) {
            var1.onTick(var1x, var3, var5, var7, var9, var11, var13, var15, var17, var19, var20);
         }

         public void onChunkGenerating(int x, int z) {
            var1.onChunkGenerating(var1x, var2);
         }

         public void onChunkGenerated(int x, int z, boolean c) {
            var1.onChunkGenerated(var1x, var2, var3);
            IrisPregenerator.this.generated.addAndGet(1L);
            if (var3) {
               IrisPregenerator.this.cached.addAndGet(1L);
            }

         }

         public void onRegionGenerated(int x, int z) {
            var1.onRegionGenerated(var1x, var2);
         }

         public void onRegionGenerating(int x, int z) {
            var1.onRegionGenerating(var1x, var2);
         }

         public void onChunkCleaned(int x, int z) {
            var1.onChunkCleaned(var1x, var2);
         }

         public void onRegionSkipped(int x, int z) {
            var1.onRegionSkipped(var1x, var2);
         }

         public void onNetworkStarted(int x, int z) {
            IrisPregenerator.this.net.add(new Position2(var1x, var2));
         }

         public void onNetworkFailed(int x, int z) {
            IrisPregenerator.this.retry.add(new Position2(var1x, var2));
         }

         public void onNetworkReclaim(int revert) {
            IrisPregenerator.this.generated.addAndGet((long)(-var1x));
         }

         public void onNetworkGeneratedChunk(int x, int z) {
            IrisPregenerator.this.generated.addAndGet(1L);
         }

         public void onNetworkDownloaded(int x, int z) {
            IrisPregenerator.this.net.remove(new Position2(var1x, var2));
         }

         public void onClose() {
            var1.onClose();
         }

         public void onSaving() {
            var1.onSaving();
         }

         public void onChunkExistsInRegionGen(int x, int z) {
            var1.onChunkExistsInRegionGen(var1x, var2);
         }
      };
   }

   public boolean paused() {
      return this.paused.get();
   }

   public Mantle getMantle() {
      return this.generator.getMantle();
   }
}
