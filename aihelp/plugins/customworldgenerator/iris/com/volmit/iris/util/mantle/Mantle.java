package com.volmit.iris.util.mantle;

import com.google.common.util.concurrent.AtomicDouble;
import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.mantle.EngineMantle;
import com.volmit.iris.engine.mantle.MantleWriter;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.documentation.RegionCoordinates;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.function.Consumer3;
import com.volmit.iris.util.function.Consumer4;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.mantle.flag.MantleFlag;
import com.volmit.iris.util.mantle.io.IOWorker;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.matter.Matter;
import com.volmit.iris.util.matter.MatterSlice;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.HyperLock;
import com.volmit.iris.util.parallel.MultiBurst;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.Generated;
import org.bukkit.Chunk;

public class Mantle {
   private static final int LOCK_SIZE = 32767;
   private final File dataFolder;
   private final int worldHeight;
   private final KMap<Long, Long> lastUse;
   private final KMap<Long, TectonicPlate> loadedRegions;
   private final HyperLock hyperLock = new HyperLock();
   private final AtomicBoolean closed = new AtomicBoolean(false);
   private final MultiBurst ioBurst;
   private final Semaphore ioTrim;
   private final Semaphore ioTectonicUnload;
   private final IOWorker worker;
   private final AtomicDouble adjustedIdleDuration;
   private final KSet<Long> toUnload;

   @BlockCoordinates
   public Mantle(File dataFolder, int worldHeight) {
      this.dataFolder = var1;
      this.worldHeight = var2;
      this.ioTrim = new Semaphore(32767, true);
      this.ioTectonicUnload = new Semaphore(32767, true);
      this.loadedRegions = new KMap();
      this.lastUse = new KMap();
      this.ioBurst = MultiBurst.ioBurst;
      this.adjustedIdleDuration = new AtomicDouble(0.0D);
      this.toUnload = new KSet(new Long[0]);
      this.worker = new IOWorker(var1, var2);
      String var10000 = String.valueOf(C.DARK_AQUA);
      Iris.debug("Opened The Mantle " + var10000 + var1.getAbsolutePath());
   }

   public static File fileForRegion(File folder, int x, int z) {
      return fileForRegion(var0, key(var1, var2), true);
   }

   public static File fileForRegion(File folder, Long key, boolean convert) {
      File var3 = oldFileForRegion(var0, var1);
      File var4 = new File(var0, "pv." + var1 + ".ttp.lz4b");
      if (var3.exists() && !var4.exists() && var2) {
         return var3;
      } else {
         if (!var4.getParentFile().exists()) {
            var4.getParentFile().mkdirs();
         }

         return var4;
      }
   }

   public static File oldFileForRegion(File folder, Long key) {
      return new File(var0, "p." + var1 + ".ttp.lz4b");
   }

   public static Long key(int x, int z) {
      return Cache.key(var0, var1);
   }

   @ChunkCoordinates
   public void raiseFlag(int x, int z, MantleFlag flag, Runnable r) {
      if (!this.hasFlag(var1, var2, var3)) {
         this.flag(var1, var2, var3, true);
         var4.run();
      }

   }

   @ChunkCoordinates
   public MantleWriter write(EngineMantle engineMantle, int x, int z, int radius, boolean multicore) {
      return new MantleWriter(var1, this, var2, var3, var4, var5);
   }

   @ChunkCoordinates
   public void lowerFlag(int x, int z, MantleFlag flag, Runnable r) {
      if (this.hasFlag(var1, var2, var3)) {
         this.flag(var1, var2, var3, false);
         var4.run();
      }

   }

   @ChunkCoordinates
   public MantleChunk getChunk(int x, int z) {
      return this.get(var1 >> 5, var2 >> 5).getOrCreate(var1 & 31, var2 & 31);
   }

   public void getChunks(final int minChunkX, final int maxChunkX, final int minChunkZ, final int maxChunkZ, int parallelism, final Consumer3<Integer, Integer, MantleChunk> consumer) {
      if (var5 <= 0) {
         var5 = 1;
      }

      Semaphore var7 = new Semaphore(var5);
      int var8 = var1 >> 5;
      int var9 = var2 >> 5;
      int var10 = var3 >> 5;
      int var11 = var4 >> 5;
      int var12 = var1 & 31;
      int var13 = var2 & 31;
      int var14 = var3 & 31;
      int var15 = var4 & 31;
      AtomicReference var16 = new AtomicReference();

      for(int var17 = var8; var17 <= var9; ++var17) {
         int var18 = var17 == var8 ? var12 : 0;
         int var19 = var17 == var9 ? var13 : 31;

         for(int var20 = var10; var20 <= var11; ++var20) {
            int var21 = var20 == var10 ? var14 : 0;
            int var22 = var20 == var11 ? var15 : 31;
            int var23 = var17 << 5;
            int var24 = var20 << 5;
            var7.acquireUninterruptibly();
            Throwable var25 = (Throwable)var16.get();
            if (var25 != null) {
               if (var25 instanceof RuntimeException) {
                  RuntimeException var26 = (RuntimeException)var25;
                  throw var26;
               }

               if (var25 instanceof Error) {
                  Error var27 = (Error)var25;
                  throw var27;
               }

               throw new RuntimeException((Throwable)var16.get());
            }

            CompletableFuture var10000 = this.getFuture(var17, var20).thenAccept((var7x) -> {
               MantleChunk var8 = var7x.getOrCreate(0, 0).use();

               try {
                  for(int var9 = var18; var9 <= var19; ++var9) {
                     for(int var10 = var21; var10 <= var22; ++var10) {
                        var6.accept(var23 + var9, var24 + var10, var7x.getOrCreate(var9, var10));
                     }
                  }
               } finally {
                  var8.release();
               }

            }).exceptionally((var1x) -> {
               var16.set(var1x);
               return null;
            });
            Objects.requireNonNull(var7);
            var10000.thenRun(var7::release);
         }
      }

      var7.acquireUninterruptibly(var5);
   }

   @ChunkCoordinates
   public void flag(int x, int z, MantleFlag flag, boolean flagged) {
      this.get(var1 >> 5, var2 >> 5).getOrCreate(var1 & 31, var2 & 31).flag(var3, var4);
   }

   public void deleteChunk(int x, int z) {
      this.get(var1 >> 5, var2 >> 5).delete(var1 & 31, var2 & 31);
   }

   @RegionCoordinates
   public boolean hasTectonicPlate(int x, int z) {
      Long var3 = key(var1, var2);
      return this.loadedRegions.containsKey(var3) || fileForRegion(this.dataFolder, var3, true).exists();
   }

   @ChunkCoordinates
   public <T> void iterateChunk(int x, int z, Class<T> type, Consumer4<Integer, Integer, Integer, T> iterator) {
      this.get(var1 >> 5, var2 >> 5).getOrCreate(var1 & 31, var2 & 31).iterate(var3, var4);
   }

   @ChunkCoordinates
   public boolean hasFlag(int x, int z, MantleFlag flag) {
      return !this.hasTectonicPlate(var1 >> 5, var2 >> 5) ? false : this.get(var1 >> 5, var2 >> 5).getOrCreate(var1 & 31, var2 & 31).isFlagged(var3);
   }

   @BlockCoordinates
   public <T> void set(int x, int y, int z, T t) {
      if (this.closed.get()) {
         throw new RuntimeException("The Mantle is closed");
      } else if (var2 >= 0 && var2 < this.worldHeight) {
         Matter var5 = this.get(var1 >> 4 >> 5, var3 >> 4 >> 5).getOrCreate(var1 >> 4 & 31, var3 >> 4 & 31).getOrCreate(var2 >> 4);
         var5.slice(var5.getClass(var4)).set(var1 & 15, var2 & 15, var3 & 15, var4);
      }
   }

   @BlockCoordinates
   public <T> void remove(int x, int y, int z, Class<T> t) {
      if (this.closed.get()) {
         throw new RuntimeException("The Mantle is closed");
      } else if (var2 >= 0 && var2 < this.worldHeight) {
         Matter var5 = this.get(var1 >> 4 >> 5, var3 >> 4 >> 5).getOrCreate(var1 >> 4 & 31, var3 >> 4 & 31).getOrCreate(var2 >> 4);
         var5.slice(var4).set(var1 & 15, var2 & 15, var3 & 15, (Object)null);
      }
   }

   @BlockCoordinates
   public <T> T get(int x, int y, int z, Class<T> t) {
      if (this.closed.get()) {
         throw new RuntimeException("The Mantle is closed");
      } else if (!this.hasTectonicPlate(var1 >> 4 >> 5, var3 >> 4 >> 5)) {
         return null;
      } else {
         return var2 >= 0 && var2 < this.worldHeight ? this.get(var1 >> 4 >> 5, var3 >> 4 >> 5).getOrCreate(var1 >> 4 & 31, var3 >> 4 & 31).getOrCreate(var2 >> 4).slice(var4).get(var1 & 15, var2 & 15, var3 & 15) : null;
      }
   }

   public boolean isClosed() {
      return this.closed.get();
   }

   public void set(int x, int y, int z, Matter matter) {
      Iterator var5 = var4.getSliceMap().values().iterator();

      while(var5.hasNext()) {
         MatterSlice var6 = (MatterSlice)var5.next();
         var6.iterate((var4x, var5x, var6x, var7) -> {
            this.set(var4x + var1, var5x + var2, var6x + var3, var7);
         });
      }

   }

   public synchronized void close() {
      String var10000 = String.valueOf(C.DARK_AQUA);
      Iris.debug("Closing The Mantle " + var10000 + this.dataFolder.getAbsolutePath());
      if (!this.closed.getAndSet(true)) {
         this.hyperLock.disable();
         BurstExecutor var1 = this.ioBurst.burst(this.toUnload.size());
         this.loadedRegions.forEach((var2, var3x) -> {
            var1.queue(() -> {
               try {
                  var3x.close();
                  this.worker.write(fileForRegion(this.dataFolder, var2, false).getName(), var3x);
                  oldFileForRegion(this.dataFolder, var2).delete();
               } catch (Throwable var4) {
                  String var10000 = String.valueOf(C.DARK_GREEN);
                  Iris.error("Failed to write Tectonic Plate " + var10000 + Cache.keyX(var2) + " " + Cache.keyZ(var2));
                  Iris.reportError(var4);
                  var4.printStackTrace();
               }

            });
         });
         this.loadedRegions.clear();

         try {
            var1.complete();
         } catch (Throwable var4) {
            Iris.reportError(var4);
         }

         try {
            this.worker.close();
         } catch (Throwable var3) {
            Iris.reportError(var3);
         }

         IO.delete(new File(this.dataFolder, ".tmp"));
         var10000 = String.valueOf(C.DARK_AQUA);
         Iris.debug("The Mantle has Closed " + var10000 + this.dataFolder.getAbsolutePath());
      }
   }

   public synchronized void trim(long baseIdleDuration, int tectonicLimit) {
      if (this.closed.get()) {
         throw new RuntimeException("The Mantle is closed");
      } else {
         double var4 = (double)var1;
         if (this.loadedRegions.size() > var3) {
            var4 = Math.max(var4 - 1000.0D * (double)(this.loadedRegions.size() - var3) / (double)var3 * 100.0D * 0.4D, 4000.0D);
         }

         this.adjustedIdleDuration.set(var4);
         this.ioTrim.acquireUninterruptibly(32767);

         try {
            Iris.debug("Trimming Tectonic Plates older than " + Form.duration(var4, 0));
            if (!this.lastUse.isEmpty()) {
               double var6 = (double)M.ms() - var4;
               Iterator var8 = this.lastUse.keySet().iterator();

               while(var8.hasNext()) {
                  long var9 = (Long)var8.next();
                  this.hyperLock.withLong(var9, () -> {
                     Long var5 = (Long)this.lastUse.get(var9);
                     if (var5 != null && (double)var5 < var6) {
                        this.toUnload.add(var9);
                        Iris.debug("Tectonic Region added to unload");
                     }

                  });
               }

               return;
            }
         } catch (Throwable var14) {
            Iris.reportError(var14);
            return;
         } finally {
            this.ioTrim.release(32767);
         }

      }
   }

   public synchronized int unloadTectonicPlate(int tectonicLimit) {
      if (this.closed.get()) {
         throw new RuntimeException("The Mantle is closed");
      } else {
         AtomicInteger var2 = new AtomicInteger();
         BurstExecutor var3 = this.ioBurst.burst(this.toUnload.size());
         var3.setMulticore(this.toUnload.size() > var1);
         this.ioTectonicUnload.acquireUninterruptibly(32767);

         try {
            double var4 = (double)M.ms() - this.adjustedIdleDuration.get();
            Iterator var6 = this.toUnload.iterator();

            while(var6.hasNext()) {
               long var7 = (Long)var6.next();
               var3.queue(() -> {
                  this.hyperLock.withLong(var7, () -> {
                     TectonicPlate var6 = (TectonicPlate)this.loadedRegions.get(var7);
                     String var10000;
                     if (var6 == null) {
                        var10000 = String.valueOf(C.DARK_GREEN);
                        Iris.debug("Tectonic Plate was added to unload while not loaded " + var10000 + Cache.keyX(var7) + " " + Cache.keyZ(var7));
                        this.toUnload.remove(var7);
                     } else {
                        Long var7x = (Long)this.lastUse.getOrDefault(var7, 0L);
                        if (this.toUnload.contains(var7) && !((double)var7x >= var4)) {
                           if (var6.inUse()) {
                              var10000 = String.valueOf(C.DARK_GREEN);
                              Iris.debug("Tectonic Plate was added to unload while in use " + var10000 + var6.getX() + " " + var6.getZ());
                              this.use(var7);
                           } else {
                              try {
                                 var6.close();
                                 this.worker.write(fileForRegion(this.dataFolder, var7, false).getName(), var6);
                                 oldFileForRegion(this.dataFolder, var7).delete();
                                 this.loadedRegions.remove(var7, var6);
                                 this.lastUse.remove(var7);
                                 this.toUnload.remove(var7);
                                 var2.incrementAndGet();
                                 var10000 = String.valueOf(C.DARK_GREEN);
                                 Iris.debug("Unloaded Tectonic Plate " + var10000 + Cache.keyX(var7) + " " + Cache.keyZ(var7));
                              } catch (InterruptedException | IOException var9) {
                                 Iris.reportError(var9);
                              }

                           }
                        }
                     }
                  });
               });
            }

            var3.complete();
         } catch (Throwable var12) {
            Iris.reportError(var12);
            var12.printStackTrace();
            var3.complete();
         } finally {
            this.ioTectonicUnload.release(32767);
         }

         return var2.get();
      }
   }

   @RegionCoordinates
   private TectonicPlate get(int x, int z) {
      boolean var3 = this.ioTrim.tryAcquire();
      boolean var4 = this.ioTectonicUnload.tryAcquire();

      try {
         TectonicPlate var5;
         if (var3 && var4) {
            Long var19 = key(var1, var2);
            TectonicPlate var6 = (TectonicPlate)this.loadedRegions.get(var19);
            if (var6 != null && !var6.isClosed()) {
               this.use(var19);
               TectonicPlate var7 = var6;
               return var7;
            }
         } else {
            try {
               var5 = (TectonicPlate)this.getSafe(var1, var2).get();
               return var5;
            } catch (Throwable var17) {
               var17.printStackTrace();
            }
         }

         try {
            var5 = (TectonicPlate)this.getSafe(var1, var2).get();
            return var5;
         } catch (InterruptedException var14) {
            Iris.warn("Failed to get Tectonic Plate " + var1 + " " + var2 + " Due to a thread intterruption (hotload?)");
            Iris.reportError(var14);
         } catch (ExecutionException var15) {
            Iris.warn("Failed to get Tectonic Plate " + var1 + " " + var2 + " Due to a thread execution exception (engine close?)");
            Iris.reportError(var15);
         } catch (Throwable var16) {
            Iris.warn("Failed to get Tectonic Plate " + var1 + " " + var2 + " Due to a unknown exception");
            Iris.reportError(var16);
         }
      } finally {
         if (var3) {
            this.ioTrim.release();
         }

         if (var4) {
            this.ioTectonicUnload.release();
         }

      }

      Iris.warn("Retrying to get " + var1 + " " + var2 + " Mantle Region");
      return this.get(var1, var2);
   }

   private CompletableFuture<TectonicPlate> getFuture(int x, int z) {
      boolean var3 = this.ioTrim.tryAcquire();
      boolean var4 = this.ioTectonicUnload.tryAcquire();
      Function var5 = (var3x) -> {
         if (var3) {
            this.ioTrim.release();
         }

         if (var4) {
            this.ioTectonicUnload.release();
         }

         return var3x;
      };
      Supplier var6 = () -> {
         return this.getSafe(var1, var2).exceptionally((var2x) -> {
            if (var2x instanceof InterruptedException) {
               Iris.warn("Failed to get Tectonic Plate " + var1 + " " + var2 + " Due to a thread intterruption (hotload?)");
               Iris.reportError(var2x);
            } else {
               Iris.warn("Failed to get Tectonic Plate " + var1 + " " + var2 + " Due to a unknown exception");
               Iris.reportError(var2x);
               var2x.printStackTrace();
            }

            return null;
         }).thenCompose((var4) -> {
            var5.apply(var4);
            if (var4 != null) {
               return CompletableFuture.completedFuture(var4);
            } else {
               Iris.warn("Retrying to get " + var1 + " " + var2 + " Mantle Region");
               return this.getFuture(var1, var2);
            }
         });
      };
      if (var3 && var4) {
         Long var7 = key(var1, var2);
         TectonicPlate var8 = (TectonicPlate)this.loadedRegions.get(var7);
         if (var8 != null && !var8.isClosed()) {
            this.use(var7);
            return CompletableFuture.completedFuture((TectonicPlate)var5.apply(var8));
         } else {
            return (CompletableFuture)var6.get();
         }
      } else {
         return this.getSafe(var1, var2).thenApply(var5).exceptionallyCompose((var1x) -> {
            var1x.printStackTrace();
            return (CompletionStage)var6.get();
         });
      }
   }

   @RegionCoordinates
   protected CompletableFuture<TectonicPlate> getSafe(int x, int z) {
      return this.ioBurst.completableFuture(() -> {
         return (TectonicPlate)this.hyperLock.withResult(var1, var2, () -> {
            Long var3 = key(var1, var2);
            this.use(var3);
            TectonicPlate var4 = (TectonicPlate)this.loadedRegions.get(var3);
            if (var4 != null && !var4.isClosed()) {
               return var4;
            } else {
               File var6 = fileForRegion(this.dataFolder, var1, var2);
               TectonicPlate var5;
               if (!var6.exists()) {
                  var5 = new TectonicPlate(this.worldHeight, var1, var2);
                  this.loadedRegions.put(var3, var5);
                  Iris.debug("Created new Tectonic Plate " + String.valueOf(C.DARK_GREEN) + var1 + " " + var2);
                  this.use(var3);
                  return var5;
               } else {
                  try {
                     Iris.addPanic("reading.tectonic-plate", var6.getAbsolutePath());
                     var5 = this.worker.read(var6.getName());
                     if (var5.getX() != var1 || var5.getZ() != var2) {
                        Iris.warn("Loaded Tectonic Plate " + var1 + "," + var2 + " but read it as " + var5.getX() + "," + var5.getZ() + "... Assuming " + var1 + "," + var2);
                     }

                     this.loadedRegions.put(var3, var5);
                     String var10000 = String.valueOf(C.DARK_GREEN);
                     Iris.debug("Loaded Tectonic Plate " + var10000 + var1 + " " + var2 + String.valueOf(C.DARK_AQUA) + " " + var6.getName());
                  } catch (Throwable var8) {
                     Iris.error("Failed to read Tectonic Plate " + var6.getAbsolutePath() + " creating a new chunk instead.");
                     Iris.reportError(var8);
                     if (!(var8 instanceof EOFException)) {
                        var8.printStackTrace();
                     }

                     Iris.panic();
                     var5 = new TectonicPlate(this.worldHeight, var1, var2);
                     this.loadedRegions.put(var3, var5);
                     Iris.debug("Created new Tectonic Plate (Due to Load Failure) " + String.valueOf(C.DARK_GREEN) + var1 + " " + var2);
                  }

                  this.use(var3);
                  return var5;
               }
            }
         });
      });
   }

   private void use(Long key) {
      this.lastUse.put(var1, M.ms());
      this.toUnload.remove(var1);
   }

   public void saveAll() {
   }

   public MantleChunk getChunk(Chunk e) {
      return this.getChunk(var1.getX(), var1.getZ());
   }

   public void deleteChunkSlice(int x, int z, Class<?> c) {
      if (!IrisToolbelt.isRetainingMantleDataForSlice(var3.getCanonicalName())) {
         this.getChunk(var1, var2).deleteSlices(var3);
      }
   }

   public int getLoadedRegionCount() {
      return this.loadedRegions.size();
   }

   public int getUnloadRegionCount() {
      return this.toUnload.size();
   }

   public double getAdjustedIdleDuration() {
      return this.adjustedIdleDuration.get();
   }

   public <T> void set(int x, int y, int z, MatterSlice<T> slice) {
      if (!var4.isEmpty()) {
         var4.iterateSync((var4x, var5, var6, var7) -> {
            this.set(var1 + var4x, var2 + var5, var3 + var6, var7);
         });
      }
   }

   public boolean isLoaded(Chunk c) {
      return this.loadedRegions.containsKey(key(var1.getX() >> 5, var1.getZ() >> 5));
   }

   public boolean shouldReduce(Engine engine) {
      return !var1.isStudio() || IrisSettings.get().getPerformance().isTrimMantleInStudio();
   }

   @Generated
   public int getWorldHeight() {
      return this.worldHeight;
   }
}
