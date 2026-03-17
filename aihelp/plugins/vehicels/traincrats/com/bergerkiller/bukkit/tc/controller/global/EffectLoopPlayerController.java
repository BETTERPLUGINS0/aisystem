package com.bergerkiller.bukkit.tc.controller.global;

import com.bergerkiller.bukkit.common.component.LibraryComponent;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.control.effect.EffectLoop;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class EffectLoopPlayerController implements LibraryComponent, TrainCarts.Provider {
   private final TrainCarts plugin;
   private final Queue<EffectLoop> startPendingSync = new ConcurrentLinkedQueue();
   private final List<EffectLoop> syncRunning = new ArrayList();
   private final EffectLoopPlayerController.AsyncWorker asyncWorker = new EffectLoopPlayerController.AsyncWorker(1);

   public EffectLoopPlayerController(TrainCarts plugin) {
      this.plugin = plugin;
   }

   public TrainCarts getTrainCarts() {
      return this.plugin;
   }

   public EffectLoop.Player createPlayer() {
      return new EffectLoopPlayerController.EffectLoopPlayer();
   }

   public EffectLoop.Player createPlayer(int limit) {
      return new EffectLoopPlayerController.EffectLoopPlayer(limit);
   }

   public void enable() {
      BukkitScheduler var10000 = Bukkit.getScheduler();
      TrainCarts var10001 = this.plugin;
      EffectLoopPlayerController.AsyncWorker var10002 = this.asyncWorker;
      Objects.requireNonNull(var10002);
      var10000.scheduleSyncDelayedTask(var10001, var10002::start);
   }

   public void disable() {
      this.asyncWorker.stop();
      this.syncRunning.clear();
      this.startPendingSync.clear();
   }

   public void updateSync() {
      EffectLoop loop;
      while((loop = (EffectLoop)this.startPendingSync.poll()) != null) {
         this.syncRunning.add(loop);
      }

      this.syncRunning.removeIf((e) -> {
         return !e.advance(EffectLoop.Time.ONE_TICK, EffectLoop.Time.ZERO, false);
      });
   }

   private void schedule(EffectLoop loop, EffectLoop.RunMode runMode) {
      if (runMode == EffectLoop.RunMode.SYNCHRONOUS) {
         this.startPendingSync.add(loop);
      } else {
         this.asyncWorker.schedule(loop);
      }

   }

   private static class AsyncWorker {
      private static final long INTERVAL = 25000000L;
      private final Queue<EffectLoop> startPendingAsync = new ConcurrentLinkedQueue();
      private final Thread effectLoopThread;
      private volatile boolean stopping = false;

      public AsyncWorker(int n) {
         this.effectLoopThread = new Thread(this::processAsync, "TrainCarts.EffectLoopPlayer" + n);
         this.effectLoopThread.setDaemon(true);
      }

      public void start() {
         this.stopping = false;
         this.effectLoopThread.start();
      }

      public void stop() {
         this.stopping = true;

         try {
            this.effectLoopThread.join(1000L);
         } catch (InterruptedException var2) {
         }

         this.startPendingAsync.clear();
      }

      public void schedule(EffectLoop loop) {
         this.startPendingAsync.add(loop);
      }

      public void processAsync() {
         EffectLoop.Time zero_duration = EffectLoop.Time.ZERO;
         List<EffectLoop> asyncRunning = new ArrayList();
         long lastTime = System.nanoTime();
         long parkUntil = lastTime + 25000000L;

         while(!this.stopping) {
            LockSupport.parkNanos(parkUntil - System.nanoTime());
            long now = System.nanoTime();
            EffectLoop.Time elapsedTime = EffectLoop.Time.nanos(now - lastTime);
            lastTime = now;
            parkUntil += 25000000L;
            if (now >= parkUntil + 25000000L) {
               parkUntil = now;
            }

            EffectLoop loop;
            while((loop = (EffectLoop)this.startPendingAsync.poll()) != null) {
               asyncRunning.add(loop);
            }

            asyncRunning.removeIf((e) -> {
               return !e.advance(elapsedTime, zero_duration, false);
            });
         }

      }
   }

   private class EffectLoopPlayer implements EffectLoop.Player, TrainCarts.Provider {
      private final Semaphore semaphore;

      public EffectLoopPlayer() {
         this.semaphore = new Semaphore(TCConfig.maxConcurrentEffectLoops);
      }

      public EffectLoopPlayer(int limit) {
         if (limit == 0) {
            this.semaphore = new Semaphore(1);
         } else if (limit < 0) {
            this.semaphore = new Semaphore(TCConfig.maxConcurrentEffectLoops);
         } else {
            this.semaphore = new Semaphore(Math.min(limit, TCConfig.maxConcurrentEffectLoops));
         }

      }

      public TrainCarts getTrainCarts() {
         return EffectLoopPlayerController.this.plugin;
      }

      public void play(EffectLoop loop, EffectLoop.RunMode runMode) {
         if (this.semaphore.tryAcquire()) {
            EffectLoopPlayerController.this.schedule(new EffectLoopPlayerController.EffectLoopWrap(this, loop), runMode);
         }

      }

      public void onEffectLoopDone() {
         this.semaphore.release();
      }
   }

   private static class EffectLoopWrap implements EffectLoop {
      private final EffectLoopPlayerController.EffectLoopPlayer player;
      private final EffectLoop base;

      public EffectLoopWrap(EffectLoopPlayerController.EffectLoopPlayer player, EffectLoop loop) {
         this.player = player;
         this.base = loop;
      }

      public boolean advance(EffectLoop.Time dt, EffectLoop.Time duration, boolean loop) {
         try {
            if (this.base.advance(dt, duration, loop)) {
               return true;
            }
         } catch (Throwable var5) {
            this.player.getTrainCarts().getLogger().log(Level.SEVERE, "An error occurred inside an effect loop", var5);
         }

         this.player.onEffectLoopDone();
         return false;
      }

      public void resetToBeginning() {
         this.base.resetToBeginning();
      }
   }
}
