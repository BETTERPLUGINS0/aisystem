package me.SuperRonanCraft.BetterRTP.lib.folialib.impl;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import me.SuperRonanCraft.BetterRTP.lib.folialib.FoliaLib;
import me.SuperRonanCraft.BetterRTP.lib.folialib.enums.EntityTaskResult;
import me.SuperRonanCraft.BetterRTP.lib.folialib.util.InvalidTickDelayNotifier;
import me.SuperRonanCraft.BetterRTP.lib.folialib.util.TimeConverter;
import me.SuperRonanCraft.BetterRTP.lib.folialib.wrapper.task.WrappedFoliaTask;
import me.SuperRonanCraft.BetterRTP.lib.folialib.wrapper.task.WrappedTask;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FoliaImplementation implements ServerImplementation {
   private final JavaPlugin plugin;
   private final GlobalRegionScheduler globalRegionScheduler;
   private final AsyncScheduler asyncScheduler;

   public FoliaImplementation(FoliaLib foliaLib) {
      this.plugin = foliaLib.getPlugin();
      this.globalRegionScheduler = this.plugin.getServer().getGlobalRegionScheduler();
      this.asyncScheduler = this.plugin.getServer().getAsyncScheduler();
   }

   public CompletableFuture<Void> runNextTick(Consumer<WrappedTask> consumer) {
      CompletableFuture<Void> future = new CompletableFuture();
      this.globalRegionScheduler.run(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
         future.complete((Object)null);
      });
      return future;
   }

   public CompletableFuture<Void> runAsync(Consumer<WrappedTask> consumer) {
      CompletableFuture<Void> future = new CompletableFuture();
      this.asyncScheduler.runNow(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
         future.complete((Object)null);
      });
      return future;
   }

   public WrappedTask runLater(Runnable runnable, long delay) {
      if (delay <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), delay);
         delay = 1L;
      }

      return this.wrapTask(this.globalRegionScheduler.runDelayed(this.plugin, (task) -> {
         runnable.run();
      }, delay));
   }

   public void runLater(Consumer<WrappedTask> consumer, long delay) {
      if (delay <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), delay);
         delay = 1L;
      }

      this.globalRegionScheduler.runDelayed(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
      }, delay);
   }

   public WrappedTask runLater(Runnable runnable, long delay, TimeUnit unit) {
      return this.runLater(runnable, TimeConverter.toTicks(delay, unit));
   }

   public void runLater(Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
      this.runLater(consumer, TimeConverter.toTicks(delay, unit));
   }

   public WrappedTask runLaterAsync(Runnable runnable, long delay) {
      return this.runLaterAsync(runnable, TimeConverter.toMillis(delay), TimeUnit.MILLISECONDS);
   }

   public void runLaterAsync(Consumer<WrappedTask> consumer, long delay) {
      this.runLaterAsync(consumer, TimeConverter.toMillis(delay), TimeUnit.MILLISECONDS);
   }

   public WrappedTask runLaterAsync(Runnable runnable, long delay, TimeUnit unit) {
      return this.wrapTask(this.asyncScheduler.runDelayed(this.plugin, (task) -> {
         runnable.run();
      }, delay, unit));
   }

   public void runLaterAsync(Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
      this.asyncScheduler.runDelayed(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
      }, delay, unit);
   }

   public WrappedTask runTimer(Runnable runnable, long delay, long period) {
      if (delay <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), delay);
         delay = 1L;
      }

      if (period <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), period);
         period = 1L;
      }

      return this.wrapTask(this.globalRegionScheduler.runAtFixedRate(this.plugin, (task) -> {
         runnable.run();
      }, delay, period));
   }

   public void runTimer(Consumer<WrappedTask> consumer, long delay, long period) {
      if (delay <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), delay);
         delay = 1L;
      }

      if (period <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), period);
         period = 1L;
      }

      this.globalRegionScheduler.runAtFixedRate(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
      }, delay, period);
   }

   public WrappedTask runTimer(Runnable runnable, long delay, long period, TimeUnit unit) {
      return this.runTimer(runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
   }

   public void runTimer(Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
      this.runTimer(consumer, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
   }

   public WrappedTask runTimerAsync(Runnable runnable, long delay, long period) {
      return this.runTimerAsync(runnable, TimeConverter.toMillis(delay), TimeConverter.toMillis(period), TimeUnit.MILLISECONDS);
   }

   public void runTimerAsync(Consumer<WrappedTask> consumer, long delay, long period) {
      this.runTimerAsync(consumer, TimeConverter.toMillis(delay), TimeConverter.toMillis(period), TimeUnit.MILLISECONDS);
   }

   public WrappedTask runTimerAsync(Runnable runnable, long delay, long period, TimeUnit unit) {
      return this.wrapTask(this.asyncScheduler.runAtFixedRate(this.plugin, (task) -> {
         runnable.run();
      }, delay, period, unit));
   }

   public void runTimerAsync(Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
      this.asyncScheduler.runAtFixedRate(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
      }, delay, period, unit);
   }

   public CompletableFuture<Void> runAtLocation(Location location, Consumer<WrappedTask> consumer) {
      CompletableFuture<Void> future = new CompletableFuture();
      this.plugin.getServer().getRegionScheduler().run(this.plugin, location, (task) -> {
         consumer.accept(this.wrapTask(task));
         future.complete((Object)null);
      });
      return future;
   }

   public WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay) {
      if (delay <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), delay);
         delay = 1L;
      }

      return this.wrapTask(this.plugin.getServer().getRegionScheduler().runDelayed(this.plugin, location, (task) -> {
         runnable.run();
      }, delay));
   }

   public void runAtLocationLater(Location location, Consumer<WrappedTask> consumer, long delay) {
      if (delay <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), delay);
         delay = 1L;
      }

      this.plugin.getServer().getRegionScheduler().runDelayed(this.plugin, location, (task) -> {
         consumer.accept(this.wrapTask(task));
      }, delay);
   }

   public WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay, TimeUnit unit) {
      return this.runAtLocationLater(location, runnable, TimeConverter.toTicks(delay, unit));
   }

   public void runAtLocationLater(Location location, Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
      this.runAtLocationLater(location, consumer, TimeConverter.toTicks(delay, unit));
   }

   public WrappedTask runAtLocationTimer(Location location, Runnable runnable, long delay, long period) {
      if (delay <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), delay);
         delay = 1L;
      }

      if (period <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), period);
         period = 1L;
      }

      return this.wrapTask(this.plugin.getServer().getRegionScheduler().runAtFixedRate(this.plugin, location, (task) -> {
         runnable.run();
      }, delay, period));
   }

   public void runAtLocationTimer(Location location, Consumer<WrappedTask> consumer, long delay, long period) {
      if (delay <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), delay);
         delay = 1L;
      }

      if (period <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), period);
         period = 1L;
      }

      this.plugin.getServer().getRegionScheduler().runAtFixedRate(this.plugin, location, (task) -> {
         consumer.accept(this.wrapTask(task));
      }, delay, period);
   }

   public WrappedTask runAtLocationTimer(Location location, Runnable runnable, long delay, long period, TimeUnit unit) {
      return this.runAtLocationTimer(location, runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
   }

   public void runAtLocationTimer(Location location, Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
      this.runAtLocationTimer(location, consumer, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
   }

   public CompletableFuture<EntityTaskResult> runAtEntity(Entity entity, Consumer<WrappedTask> consumer) {
      CompletableFuture<EntityTaskResult> future = new CompletableFuture();
      ScheduledTask scheduledTask = entity.getScheduler().run(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
         future.complete(EntityTaskResult.SUCCESS);
      }, (Runnable)null);
      if (scheduledTask == null) {
         future.complete(EntityTaskResult.SCHEDULER_RETIRED);
      }

      return future;
   }

   public CompletableFuture<EntityTaskResult> runAtEntityWithFallback(Entity entity, Consumer<WrappedTask> consumer, Runnable fallback) {
      CompletableFuture<EntityTaskResult> future = new CompletableFuture();
      ScheduledTask scheduledTask = entity.getScheduler().run(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
         future.complete(EntityTaskResult.SUCCESS);
      }, () -> {
         fallback.run();
         future.complete(EntityTaskResult.ENTITY_RETIRED);
      });
      if (scheduledTask == null) {
         future.complete(EntityTaskResult.SCHEDULER_RETIRED);
      }

      return future;
   }

   public WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay) {
      if (delay <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), delay);
         delay = 1L;
      }

      return this.wrapTask(entity.getScheduler().runDelayed(this.plugin, (task) -> {
         runnable.run();
      }, (Runnable)null, delay));
   }

   public void runAtEntityLater(Entity entity, Consumer<WrappedTask> consumer, long delay) {
      if (delay <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), delay);
         delay = 1L;
      }

      entity.getScheduler().runDelayed(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
      }, (Runnable)null, delay);
   }

   public WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay, TimeUnit unit) {
      return this.runAtEntityLater(entity, runnable, TimeConverter.toTicks(delay, unit));
   }

   public void runAtEntityLater(Entity entity, Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
      this.runAtEntityLater(entity, consumer, TimeConverter.toTicks(delay, unit));
   }

   public WrappedTask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period) {
      if (delay <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), delay);
         delay = 1L;
      }

      if (period <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), period);
         period = 1L;
      }

      return this.wrapTask(entity.getScheduler().runAtFixedRate(this.plugin, (task) -> {
         runnable.run();
      }, (Runnable)null, delay, period));
   }

   public void runAtEntityTimer(Entity entity, Consumer<WrappedTask> consumer, long delay, long period) {
      if (delay <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), delay);
         delay = 1L;
      }

      if (period <= 0L) {
         InvalidTickDelayNotifier.notifyOnce(this.plugin.getLogger(), period);
         period = 1L;
      }

      entity.getScheduler().runAtFixedRate(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
      }, (Runnable)null, delay, period);
   }

   public WrappedTask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period, TimeUnit unit) {
      return this.runAtEntityTimer(entity, runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
   }

   public void runAtEntityTimer(Entity entity, Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
      this.runAtEntityTimer(entity, consumer, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
   }

   public void cancelTask(WrappedTask task) {
      task.cancel();
   }

   public void cancelAllTasks() {
      this.globalRegionScheduler.cancelTasks(this.plugin);
      this.asyncScheduler.cancelTasks(this.plugin);
   }

   public Player getPlayer(String name) {
      return this.plugin.getServer().getPlayer(name);
   }

   public Player getPlayerExact(String name) {
      return this.plugin.getServer().getPlayerExact(name);
   }

   public Player getPlayer(UUID uuid) {
      return this.plugin.getServer().getPlayer(uuid);
   }

   public CompletableFuture<Boolean> teleportAsync(Player player, Location location) {
      return player.teleportAsync(location);
   }

   public WrappedTask wrapTask(Object nativeTask) {
      if (!(nativeTask instanceof ScheduledTask)) {
         throw new IllegalArgumentException("The nativeTask provided must be a ScheduledTask. Got: " + nativeTask.getClass().getName() + " instead.");
      } else {
         return new WrappedFoliaTask((ScheduledTask)nativeTask);
      }
   }
}
