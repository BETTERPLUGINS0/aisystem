package ac.grim.grimac.platform.bukkit.scheduler.bukkit;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.scheduler.GlobalRegionScheduler;
import ac.grim.grimac.platform.api.scheduler.TaskHandle;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class BukkitGlobalRegionScheduler implements GlobalRegionScheduler {
   private final BukkitScheduler bukkitScheduler = Bukkit.getScheduler();

   public void execute(@NotNull GrimPlugin plugin, @NotNull Runnable task) {
      this.bukkitScheduler.runTask(GrimACBukkitLoaderPlugin.LOADER, task);
   }

   public TaskHandle run(@NotNull GrimPlugin plugin, @NotNull Runnable task) {
      return new BukkitTaskHandle(this.bukkitScheduler.runTask(GrimACBukkitLoaderPlugin.LOADER, task));
   }

   public TaskHandle runDelayed(@NotNull GrimPlugin plugin, @NotNull Runnable task, long delay) {
      return new BukkitTaskHandle(this.bukkitScheduler.runTaskLater(GrimACBukkitLoaderPlugin.LOADER, task, delay));
   }

   public TaskHandle runAtFixedRate(@NotNull GrimPlugin plugin, @NotNull Runnable task, long initialDelayTicks, long periodTicks) {
      return new BukkitTaskHandle(this.bukkitScheduler.runTaskTimer(GrimACBukkitLoaderPlugin.LOADER, task, initialDelayTicks, periodTicks));
   }

   public void cancel(@NotNull GrimPlugin plugin) {
      this.bukkitScheduler.cancelTasks(GrimACBukkitLoaderPlugin.LOADER);
   }
}
