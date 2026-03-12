package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class AsyncScheduler {
   private BukkitScheduler bukkitScheduler;
   private io.papermc.paper.threadedregions.scheduler.AsyncScheduler asyncScheduler;

   protected AsyncScheduler() {
      if (FoliaScheduler.isFolia) {
         this.asyncScheduler = Bukkit.getAsyncScheduler();
      } else {
         this.bukkitScheduler = Bukkit.getScheduler();
      }

   }

   public TaskWrapper runNow(@NotNull Plugin plugin, @NotNull Consumer<Object> task) {
      return !FoliaScheduler.isFolia ? new TaskWrapper(this.bukkitScheduler.runTaskAsynchronously(plugin, () -> {
         task.accept((Object)null);
      })) : new TaskWrapper(this.asyncScheduler.runNow(plugin, (o) -> {
         task.accept((Object)null);
      }));
   }

   public TaskWrapper runDelayed(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay, @NotNull TimeUnit timeUnit) {
      return !FoliaScheduler.isFolia ? new TaskWrapper(this.bukkitScheduler.runTaskLaterAsynchronously(plugin, () -> {
         task.accept((Object)null);
      }, this.convertTimeToTicks(delay, timeUnit))) : new TaskWrapper(this.asyncScheduler.runDelayed(plugin, (o) -> {
         task.accept((Object)null);
      }, delay, timeUnit));
   }

   public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay, long period, @NotNull TimeUnit timeUnit) {
      if (period < 1L) {
         period = 1L;
      }

      return !FoliaScheduler.isFolia ? new TaskWrapper(this.bukkitScheduler.runTaskTimerAsynchronously(plugin, () -> {
         task.accept((Object)null);
      }, this.convertTimeToTicks(delay, timeUnit), this.convertTimeToTicks(period, timeUnit))) : new TaskWrapper(this.asyncScheduler.runAtFixedRate(plugin, (o) -> {
         task.accept((Object)null);
      }, delay, period, timeUnit));
   }

   public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long initialDelayTicks, long periodTicks) {
      if (periodTicks < 1L) {
         periodTicks = 1L;
      }

      return !FoliaScheduler.isFolia ? new TaskWrapper(this.bukkitScheduler.runTaskTimerAsynchronously(plugin, () -> {
         task.accept((Object)null);
      }, initialDelayTicks, periodTicks)) : new TaskWrapper(this.asyncScheduler.runAtFixedRate(plugin, (o) -> {
         task.accept((Object)null);
      }, initialDelayTicks * 50L, periodTicks * 50L, TimeUnit.MILLISECONDS));
   }

   public void cancel(@NotNull Plugin plugin) {
      if (!FoliaScheduler.isFolia) {
         this.bukkitScheduler.cancelTasks(plugin);
      } else {
         this.asyncScheduler.cancelTasks(plugin);
      }
   }

   private long convertTimeToTicks(long time, TimeUnit timeUnit) {
      return timeUnit.toMillis(time) / 50L;
   }
}
