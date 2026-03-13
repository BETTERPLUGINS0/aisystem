package com.volmit.iris.core.pregenerator.methods;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.pregenerator.PregenListener;
import com.volmit.iris.core.pregenerator.PregeneratorMethod;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.paper.PaperLib;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.scheduling.J;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Chunk;
import org.bukkit.World;

public class AsyncPregenMethod implements PregeneratorMethod {
   private static final AtomicInteger THREAD_COUNT = new AtomicInteger();
   private final World world;
   private final AsyncPregenMethod.Executor executor;
   private final Semaphore semaphore;
   private final int threads;
   private final boolean urgent;
   private final Map<Chunk, Long> lastUse;

   public AsyncPregenMethod(World world, int unusedThreads) {
      if (!PaperLib.isPaper()) {
         throw new UnsupportedOperationException("Cannot use PaperAsync on non paper!");
      } else {
         this.world = var1;
         this.executor = (AsyncPregenMethod.Executor)(IrisSettings.get().getPregen().isUseTicketQueue() ? new AsyncPregenMethod.TicketExecutor() : new AsyncPregenMethod.ServiceExecutor());
         this.threads = IrisSettings.get().getPregen().getMaxConcurrency();
         this.semaphore = new Semaphore(this.threads, true);
         this.urgent = IrisSettings.get().getPregen().useHighPriority;
         this.lastUse = new KMap();
      }
   }

   private void unloadAndSaveAllChunks() {
      try {
         J.sfut(() -> {
            if (this.world == null) {
               Iris.warn("World was null somehow...");
            } else {
               long var1 = M.ms() - 10000L;
               this.lastUse.entrySet().removeIf((var2) -> {
                  Chunk var3 = (Chunk)var2.getKey();
                  Long var4 = (Long)var2.getValue();
                  if (var3.isLoaded() && var4 != null) {
                     if (var4 < var1) {
                        var3.unload();
                        return true;
                     } else {
                        return false;
                     }
                  } else {
                     return true;
                  }
               });
               this.world.save();
            }
         }).get();
      } catch (Throwable var2) {
         var2.printStackTrace();
      }

   }

   public void init() {
      this.unloadAndSaveAllChunks();
      increaseWorkerThreads();
   }

   public String getMethod(int x, int z) {
      return "Async";
   }

   public void close() {
      this.semaphore.acquireUninterruptibly(this.threads);
      this.unloadAndSaveAllChunks();
      this.executor.shutdown();
      resetWorkerThreads();
   }

   public void save() {
      this.unloadAndSaveAllChunks();
   }

   public boolean supportsRegions(int x, int z, PregenListener listener) {
      return false;
   }

   public void generateRegion(int x, int z, PregenListener listener) {
      throw new UnsupportedOperationException();
   }

   public void generateChunk(int x, int z, PregenListener listener) {
      var3.onChunkGenerating(var1, var2);

      try {
         this.semaphore.acquire();
      } catch (InterruptedException var5) {
         return;
      }

      this.executor.generate(var1, var2, var3);
   }

   public Mantle getMantle() {
      return IrisToolbelt.isIrisWorld(this.world) ? IrisToolbelt.access(this.world).getEngine().getMantle().getMantle() : null;
   }

   public static void increaseWorkerThreads() {
      THREAD_COUNT.updateAndGet((var0) -> {
         if (var0 > 0) {
            return 1;
         } else {
            int var1 = IrisSettings.get().getConcurrency().getWorldGenThreads();

            try {
               Field var2 = Class.forName("ca.spottedleaf.moonrise.common.util.MoonriseCommon").getDeclaredField("WORKER_POOL");
               Object var3 = var2.get((Object)null);
               int var4 = ((Thread[])var3.getClass().getDeclaredMethod("getCoreThreads").invoke(var3)).length;
               if (var4 >= var1) {
                  return 0;
               } else {
                  var3.getClass().getDeclaredMethod("adjustThreadCount", Integer.TYPE).invoke(var3, var1);
                  return var4;
               }
            } catch (Throwable var5) {
               Iris.warn("Failed to increase worker threads, if you are on paper or a fork of it please increase it manually to " + var1);
               Iris.warn("For more information see https://docs.papermc.io/paper/reference/global-configuration#chunk_system_worker_threads");
               if (var5 instanceof InvocationTargetException) {
                  Iris.reportError(var5);
                  var5.printStackTrace();
               }

               return 0;
            }
         }
      });
   }

   public static void resetWorkerThreads() {
      THREAD_COUNT.updateAndGet((var0) -> {
         if (var0 == 0) {
            return 0;
         } else {
            try {
               Field var1 = Class.forName("ca.spottedleaf.moonrise.common.util.MoonriseCommon").getDeclaredField("WORKER_POOL");
               Object var2 = var1.get((Object)null);
               Method var3 = var2.getClass().getDeclaredMethod("adjustThreadCount", Integer.TYPE);
               var3.invoke(var2, var0);
               return 0;
            } catch (Throwable var4) {
               Iris.reportError(var4);
               Iris.error("Failed to reset worker threads");
               var4.printStackTrace();
               return var0;
            }
         }
      });
   }

   private class TicketExecutor implements AsyncPregenMethod.Executor {
      public void generate(int x, int z, PregenListener listener) {
         PaperLib.getChunkAtAsync(AsyncPregenMethod.this.world, var1, var2, true, AsyncPregenMethod.this.urgent).exceptionally((var0) -> {
            Iris.reportError(var0);
            var0.printStackTrace();
            return null;
         }).thenAccept((var4) -> {
            AsyncPregenMethod.this.semaphore.release();
            var3.onChunkGenerated(var1, var2);
            var3.onChunkCleaned(var1, var2);
            if (var4 != null) {
               AsyncPregenMethod.this.lastUse.put(var4, M.ms());
            }
         });
      }
   }

   private class ServiceExecutor implements AsyncPregenMethod.Executor {
      private final ExecutorService service = IrisSettings.get().getPregen().isUseVirtualThreads() ? Executors.newVirtualThreadPerTaskExecutor() : new MultiBurst("Iris Async Pregen");

      public void generate(int x, int z, PregenListener listener) {
         this.service.submit(() -> {
            try {
               PaperLib.getChunkAtAsync(AsyncPregenMethod.this.world, var1, var2, true, AsyncPregenMethod.this.urgent).thenAccept((var4) -> {
                  var3.onChunkGenerated(var1, var2);
                  var3.onChunkCleaned(var1, var2);
                  if (var4 != null) {
                     AsyncPregenMethod.this.lastUse.put(var4, M.ms());
                  }
               }).get();
            } catch (InterruptedException var9) {
            } catch (Throwable var10) {
               Iris.reportError(var10);
               var10.printStackTrace();
            } finally {
               AsyncPregenMethod.this.semaphore.release();
            }

         });
      }

      public void shutdown() {
         this.service.shutdown();
      }
   }

   private interface Executor {
      void generate(int x, int z, PregenListener listener);

      default void shutdown() {
      }
   }
}
