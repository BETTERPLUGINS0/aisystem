package me.SuperRonanCraft.BetterRTP.lib.folialib.impl;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import me.SuperRonanCraft.BetterRTP.lib.folialib.FoliaLib;
import me.SuperRonanCraft.BetterRTP.lib.folialib.enums.EntityTaskResult;
import me.SuperRonanCraft.BetterRTP.lib.folialib.util.TimeConverter;
import me.SuperRonanCraft.BetterRTP.lib.folialib.wrapper.task.WrappedBukkitTask;
import me.SuperRonanCraft.BetterRTP.lib.folialib.wrapper.task.WrappedTask;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class SpigotImplementation implements ServerImplementation {
   private final JavaPlugin plugin;
   @NotNull
   private final BukkitScheduler scheduler;

   public SpigotImplementation(FoliaLib foliaLib) {
      this.plugin = foliaLib.getPlugin();
      this.scheduler = this.plugin.getServer().getScheduler();
   }

   public CompletableFuture<Void> runNextTick(Consumer<WrappedTask> consumer) {
      CompletableFuture<Void> future = new CompletableFuture();
      this.scheduler.runTask(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
         future.complete((Object)null);
      });
      return future;
   }

   public CompletableFuture<Void> runAsync(Consumer<WrappedTask> consumer) {
      CompletableFuture<Void> future = new CompletableFuture();
      this.scheduler.runTaskAsynchronously(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
         future.complete((Object)null);
      });
      return future;
   }

   public WrappedTask runLater(Runnable runnable, long delay) {
      return this.wrapTask(this.scheduler.runTaskLater(this.plugin, runnable, delay));
   }

   public void runLater(Consumer<WrappedTask> consumer, long delay) {
      this.scheduler.runTaskLater(this.plugin, (task) -> {
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
      return this.wrapTask(this.scheduler.runTaskLaterAsynchronously(this.plugin, runnable, delay));
   }

   public void runLaterAsync(Consumer<WrappedTask> consumer, long delay) {
      this.scheduler.runTaskLaterAsynchronously(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
      }, delay);
   }

   public WrappedTask runLaterAsync(Runnable runnable, long delay, TimeUnit unit) {
      return this.runLaterAsync(runnable, TimeConverter.toTicks(delay, unit));
   }

   public void runLaterAsync(Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
      this.runLaterAsync(consumer, TimeConverter.toTicks(delay, unit));
   }

   public WrappedTask runTimer(Runnable runnable, long delay, long period) {
      return this.wrapTask(this.scheduler.runTaskTimer(this.plugin, runnable, delay, period));
   }

   public void runTimer(Consumer<WrappedTask> consumer, long delay, long period) {
      this.scheduler.runTaskTimer(this.plugin, (task) -> {
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
      return this.wrapTask(this.scheduler.runTaskTimerAsynchronously(this.plugin, runnable, delay, period));
   }

   public void runTimerAsync(Consumer<WrappedTask> consumer, long delay, long period) {
      this.scheduler.runTaskTimerAsynchronously(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
      }, delay, period);
   }

   public WrappedTask runTimerAsync(Runnable runnable, long delay, long period, TimeUnit unit) {
      return this.runTimerAsync(runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
   }

   public void runTimerAsync(Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
      this.runTimerAsync(consumer, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
   }

   public CompletableFuture<Void> runAtLocation(Location location, Consumer<WrappedTask> consumer) {
      return this.runNextTick(consumer);
   }

   public WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay) {
      return this.runLater(runnable, delay);
   }

   public void runAtLocationLater(Location location, Consumer<WrappedTask> consumer, long delay) {
      this.runLater(consumer, delay);
   }

   public WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay, TimeUnit unit) {
      return this.runAtLocationLater(location, runnable, TimeConverter.toTicks(delay, unit));
   }

   public void runAtLocationLater(Location location, Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
      this.runAtLocationLater(location, consumer, TimeConverter.toTicks(delay, unit));
   }

   public WrappedTask runAtLocationTimer(Location location, Runnable runnable, long delay, long period) {
      return this.runTimer(runnable, delay, period);
   }

   public void runAtLocationTimer(Location location, Consumer<WrappedTask> consumer, long delay, long period) {
      this.runTimer(consumer, delay, period);
   }

   public WrappedTask runAtLocationTimer(Location location, Runnable runnable, long delay, long period, TimeUnit unit) {
      return this.runAtLocationTimer(location, runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
   }

   public void runAtLocationTimer(Location location, Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
      this.runAtLocationTimer(location, consumer, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
   }

   public CompletableFuture<EntityTaskResult> runAtEntity(Entity entity, Consumer<WrappedTask> consumer) {
      CompletableFuture<EntityTaskResult> future = new CompletableFuture();
      this.scheduler.runTask(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
         future.complete(EntityTaskResult.SUCCESS);
      });
      return future;
   }

   public CompletableFuture<EntityTaskResult> runAtEntityWithFallback(Entity entity, Consumer<WrappedTask> consumer, Runnable fallback) {
      CompletableFuture<EntityTaskResult> future = new CompletableFuture();
      this.scheduler.runTask(this.plugin, (task) -> {
         if (entity.isValid()) {
            consumer.accept(this.wrapTask(task));
            future.complete(EntityTaskResult.SUCCESS);
         } else {
            fallback.run();
            future.complete(EntityTaskResult.ENTITY_RETIRED);
         }

      });
      return future;
   }

   public WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay) {
      return this.wrapTask(this.scheduler.runTaskLater(this.plugin, runnable, delay));
   }

   public void runAtEntityLater(Entity entity, Consumer<WrappedTask> consumer, long delay) {
      this.scheduler.runTaskLater(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
      }, delay);
   }

   public WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay, TimeUnit unit) {
      return this.runAtEntityLater(entity, runnable, TimeConverter.toTicks(delay, unit));
   }

   public void runAtEntityLater(Entity entity, Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
      this.runAtEntityLater(entity, consumer, TimeConverter.toTicks(delay, unit));
   }

   public WrappedTask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period) {
      return this.wrapTask(this.scheduler.runTaskTimer(this.plugin, runnable, delay, period));
   }

   public void runAtEntityTimer(Entity entity, Consumer<WrappedTask> consumer, long delay, long period) {
      this.scheduler.runTaskTimer(this.plugin, (task) -> {
         consumer.accept(this.wrapTask(task));
      }, delay, period);
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
      this.scheduler.cancelTasks(this.plugin);
   }

   public Player getPlayer(String name) {
      return this.getPlayerFromMainThread(() -> {
         return this.plugin.getServer().getPlayer(name);
      });
   }

   public Player getPlayerExact(String name) {
      return this.getPlayerFromMainThread(() -> {
         return this.plugin.getServer().getPlayerExact(name);
      });
   }

   public Player getPlayer(UUID uuid) {
      return this.getPlayerFromMainThread(() -> {
         return this.plugin.getServer().getPlayer(uuid);
      });
   }

   private Player getPlayerFromMainThread(Supplier<Player> playerSupplier) {
      if (this.plugin.getServer().isPrimaryThread()) {
         return (Player)playerSupplier.get();
      } else {
         try {
            BukkitScheduler var10000 = this.scheduler;
            JavaPlugin var10001 = this.plugin;
            Objects.requireNonNull(playerSupplier);
            return (Player)var10000.callSyncMethod(var10001, playerSupplier::get).get();
         } catch (ExecutionException | InterruptedException var3) {
            var3.printStackTrace();
            return null;
         }
      }
   }

   public CompletableFuture<Boolean> teleportAsync(Player player, Location location) {
      CompletableFuture<Boolean> future = new CompletableFuture();
      this.runAtEntity(player, (task) -> {
         if (player.isValid() && player.isOnline()) {
            player.teleport(location);
            future.complete(true);
         } else {
            future.complete(false);
         }

      });
      return future;
   }

   public WrappedTask wrapTask(Object nativeTask) {
      if (!(nativeTask instanceof BukkitTask)) {
         throw new IllegalArgumentException("The nativeTask provided must be a BukkitTask. Got: " + nativeTask.getClass().getName() + " instead.");
      } else {
         return new WrappedBukkitTask((BukkitTask)nativeTask);
      }
   }
}
