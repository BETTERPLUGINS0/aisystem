package ac.grim.grimac.platform.bukkit.scheduler.bukkit;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.scheduler.AsyncScheduler;
import ac.grim.grimac.platform.api.scheduler.PlatformScheduler;
import ac.grim.grimac.platform.api.scheduler.TaskHandle;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class BukkitAsyncScheduler implements AsyncScheduler {
   private final BukkitScheduler bukkitScheduler = Bukkit.getScheduler();

   public TaskHandle runNow(@NotNull GrimPlugin plugin, @NotNull Runnable task) {
      return new BukkitTaskHandle(this.bukkitScheduler.runTaskAsynchronously(GrimACBukkitLoaderPlugin.LOADER, task));
   }

   public TaskHandle runDelayed(@NotNull GrimPlugin plugin, @NotNull Runnable task, long delay, @NotNull TimeUnit timeUnit) {
      return new BukkitTaskHandle(this.bukkitScheduler.runTaskLaterAsynchronously(GrimACBukkitLoaderPlugin.LOADER, task, PlatformScheduler.convertTimeToTicks(delay, timeUnit)));
   }

   public TaskHandle runAtFixedRate(@NotNull GrimPlugin plugin, @NotNull Runnable task, long delay, long period, @NotNull TimeUnit timeUnit) {
      return new BukkitTaskHandle(this.bukkitScheduler.runTaskTimerAsynchronously(GrimACBukkitLoaderPlugin.LOADER, task, PlatformScheduler.convertTimeToTicks(delay, timeUnit), PlatformScheduler.convertTimeToTicks(period, timeUnit)));
   }

   public TaskHandle runAtFixedRate(@NotNull GrimPlugin plugin, @NotNull Runnable task, long initialDelayTicks, long periodTicks) {
      return new BukkitTaskHandle(this.bukkitScheduler.runTaskTimerAsynchronously(GrimACBukkitLoaderPlugin.LOADER, task, initialDelayTicks, periodTicks));
   }

   public void cancel(@NotNull GrimPlugin plugin) {
      this.bukkitScheduler.cancelTasks(GrimACBukkitLoaderPlugin.LOADER);
   }
}
