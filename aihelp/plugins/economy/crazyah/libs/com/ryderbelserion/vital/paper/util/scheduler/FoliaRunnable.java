package libs.com.ryderbelserion.vital.paper.util.scheduler;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FoliaRunnable implements Runnable {
   @Nullable
   private GlobalRegionScheduler globalRegionScheduler;
   @Nullable
   private EntityScheduler entityScheduler;
   @Nullable
   private RegionScheduler regionScheduler;
   @Nullable
   private AsyncScheduler asyncScheduler;
   @Nullable
   private Runnable entityRetired;
   @Nullable
   private TimeUnit timeUnit;
   private ScheduledTask task;
   private World world;
   private int chunkX;
   private int chunkZ;

   public FoliaRunnable(@NotNull AsyncScheduler scheduler, @Nullable TimeUnit timeUnit) {
      this.asyncScheduler = scheduler;
      this.timeUnit = timeUnit;
   }

   public FoliaRunnable(@NotNull EntityScheduler scheduler, @Nullable Runnable retired) {
      this.entityScheduler = scheduler;
      this.entityRetired = retired;
   }

   public FoliaRunnable(@NotNull GlobalRegionScheduler scheduler) {
      this.globalRegionScheduler = scheduler;
   }

   public FoliaRunnable(@NotNull RegionScheduler scheduler, @NotNull Location location) {
      this(scheduler, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4);
   }

   public FoliaRunnable(@NotNull RegionScheduler scheduler, @NotNull World world, int chunkX, int chunkZ) {
      this.regionScheduler = scheduler;
      this.world = world;
      this.chunkX = chunkX;
      this.chunkZ = chunkZ;
   }

   public final boolean isCancelled() throws IllegalStateException {
      this.checkScheduled();
      return this.task.isCancelled();
   }

   public void cancel() throws IllegalStateException {
      this.cancel((Consumer)null);
   }

   public void run() {
   }

   public void cancel(@Nullable Consumer<FoliaRunnable> consumer) throws IllegalStateException {
      this.task.cancel();
      if (consumer != null) {
         consumer.accept(this);
      }

   }

   public final boolean execute(@NotNull JavaPlugin plugin) throws IllegalArgumentException, IllegalStateException {
      this.checkNotYetScheduled();
      if (this.globalRegionScheduler != null) {
         this.globalRegionScheduler.execute(plugin, this);
      } else {
         if (this.entityScheduler != null) {
            return this.entityScheduler.execute(plugin, this, this.entityRetired, 1L);
         }

         if (this.regionScheduler != null) {
            this.regionScheduler.execute(plugin, this.world, this.chunkX, this.chunkZ, this);
         } else {
            if (this.asyncScheduler == null) {
               throw new UnsupportedOperationException("The task type is not supported.");
            }

            this.asyncScheduler.runNow(plugin, (scheduledTask) -> {
               this.run();
            });
         }
      }

      return true;
   }

   @NotNull
   public final ScheduledTask run(@NotNull JavaPlugin plugin) throws IllegalArgumentException, IllegalStateException {
      this.checkNotYetScheduled();
      if (this.globalRegionScheduler != null) {
         return this.setupTask(this.globalRegionScheduler.run(plugin, (scheduledTask) -> {
            this.run();
         }));
      } else if (this.entityScheduler != null) {
         return this.setupTask(this.entityScheduler.run(plugin, (scheduledTask) -> {
            this.run();
         }, this.entityRetired));
      } else if (this.regionScheduler != null) {
         return this.setupTask(this.regionScheduler.run(plugin, this.world, this.chunkX, this.chunkZ, (scheduledTask) -> {
            this.run();
         }));
      } else if (this.asyncScheduler != null) {
         return this.setupTask(this.asyncScheduler.runDelayed(plugin, (scheduledTask) -> {
            this.run();
         }, 50L, TimeUnit.MILLISECONDS));
      } else {
         throw new UnsupportedOperationException("The task type is not supported.");
      }
   }

   @NotNull
   public final ScheduledTask runDelayed(@NotNull JavaPlugin plugin, long delay) throws IllegalArgumentException, IllegalStateException {
      this.checkNotYetScheduled();
      delay = Math.max(1L, delay);
      if (this.globalRegionScheduler != null) {
         return this.setupTask(this.globalRegionScheduler.runDelayed(plugin, (scheduledTask) -> {
            this.run();
         }, delay));
      } else if (this.entityScheduler != null) {
         return this.setupTask(this.entityScheduler.runDelayed(plugin, (scheduledTask) -> {
            this.run();
         }, this.entityRetired, delay));
      } else if (this.regionScheduler != null) {
         return this.setupTask(this.regionScheduler.runDelayed(plugin, this.world, this.chunkX, this.chunkZ, (scheduledTask) -> {
            this.run();
         }, delay));
      } else if (this.asyncScheduler != null && this.timeUnit != null) {
         return this.setupTask(this.asyncScheduler.runDelayed(plugin, (scheduledTask) -> {
            this.run();
         }, delay, this.timeUnit));
      } else {
         throw new UnsupportedOperationException("The task type is not supported.");
      }
   }

   @NotNull
   public final ScheduledTask runAtFixedRate(@NotNull JavaPlugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
      this.checkNotYetScheduled();
      delay = Math.max(1L, delay);
      period = Math.max(1L, period);
      if (this.globalRegionScheduler != null) {
         return this.setupTask(this.globalRegionScheduler.runAtFixedRate(plugin, (scheduledTask) -> {
            this.run();
         }, delay, period));
      } else if (this.entityScheduler != null) {
         return this.setupTask(this.entityScheduler.runAtFixedRate(plugin, (scheduledTask) -> {
            this.run();
         }, this.entityRetired, delay, period));
      } else if (this.regionScheduler != null) {
         return this.setupTask(this.regionScheduler.runAtFixedRate(plugin, this.world, this.chunkX, this.chunkZ, (scheduledTask) -> {
            this.run();
         }, delay, period));
      } else if (this.asyncScheduler != null && this.timeUnit != null) {
         return this.setupTask(this.asyncScheduler.runAtFixedRate(plugin, (scheduledTask) -> {
            this.run();
         }, delay, period, this.timeUnit));
      } else {
         throw new UnsupportedOperationException("The task type is not supported.");
      }
   }

   public final int getTaskId() throws IllegalStateException {
      this.checkScheduled();
      return this.task.hashCode();
   }

   private void checkScheduled() {
      if (this.task == null) {
         throw new IllegalStateException("Not scheduled yet");
      }
   }

   private void checkNotYetScheduled() {
      if (this.task != null) {
         throw new IllegalStateException("Already scheduled as " + this.task.hashCode());
      }
   }

   @NotNull
   private ScheduledTask setupTask(@NotNull ScheduledTask task) {
      return this.task = task;
   }
}
